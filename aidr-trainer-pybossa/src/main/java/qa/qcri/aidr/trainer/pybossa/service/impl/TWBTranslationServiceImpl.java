package qa.qcri.aidr.trainer.pybossa.service.impl;

import au.com.bytecode.opencsv.CSVParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.pybossa.dao.TaskTranslationDao;
import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;
import qa.qcri.aidr.trainer.pybossa.entity.ReportTemplate;
import qa.qcri.aidr.trainer.pybossa.entity.TaskTranslation;
import qa.qcri.aidr.trainer.pybossa.format.impl.TranslationRequestModel;
import qa.qcri.aidr.trainer.pybossa.service.ReportTemplateService;
import qa.qcri.aidr.trainer.pybossa.service.TranslationService;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by kamal on 3/22/15.
 */
@Service("translationService")
@Transactional(readOnly = false)
public class TWBTranslationServiceImpl implements TranslationService {

    @Autowired
    private TaskTranslationDao taskTranslationDao;

    @Autowired
    private ReportTemplateService reportTemplateService;
    final private static int MAX_BATCH_SIZE = 1000;
    final private static long MAX_WAIT_TIME_MILLIS = 30000; // one hour
    private static long timeOfLastTranslationProcessingMillis = System.currentTimeMillis(); //initialize at startup


    protected static Logger logger = Logger.getLogger("service.translationService");


    public Map processTranslations(ClientApp clientApp) {
        //pullAllCompletedTranslations(clientApp);
        Long tcProjectId = clientApp.getTcProjectId();
        return pushAllTranslations(clientApp.getClientAppID(), tcProjectId, MAX_WAIT_TIME_MILLIS, MAX_BATCH_SIZE);
    }

    public Map pushAllTranslations(Long clientAppId, Long twbProjectId, long maxTimeToWait, int maxBatchSize) {
        //add ordering
        List<TaskTranslation> translations = findAllTranslationsByClientAppIdAndStatus(clientAppId, TaskTranslation.STATUS_NEW, maxBatchSize);
        Map result = null;
        boolean forceProcessingByTime = false;
        long currentTimeMillis = System.currentTimeMillis();
        if ((currentTimeMillis - timeOfLastTranslationProcessingMillis) >= maxTimeToWait) {
            forceProcessingByTime = true;
        }
        if ((forceProcessingByTime || translations.size() >= maxBatchSize) && (translations.size() > 0)) {
            while (true) {

                TranslationRequestModel model = new TranslationRequestModel();
                model.setContactEmail("test@test.com");
                model.setTitle("Translation Request from Micromappers");
                model.setSourceLanguage("und");
                String[] targets = {"eng"};
                model.setTargetLanguages(targets);
                model.setSourceWordCount(100); //random test
                model.setInstructions("Please translate according to ...");
                model.setDeadline(new Date());
                model.setUrgency("high");
                model.setProjectId(twbProjectId.longValue());

                model.setCallbackURL("https://www.example.com/my-callback-url");
                model.setTranslationList(translations);


                result = pushTranslationRequest(model);

                if (result.get("order_id") != null) {
                    Long orderId = new Long((Integer) result.get("order_id"));
                    updateTranslationsWithOrderId(model.getTranslationList(), orderId);
                }
                translations = findAllTranslationsByClientAppIdAndStatus(clientAppId, TaskTranslation.STATUS_NEW, maxBatchSize);
                if (translations.size() < maxBatchSize) {
                    break;
                }
            }
        }

        return result;

    }

    private void updateTranslationsWithOrderId(List<TaskTranslation> translations, Long orderId) {
        Iterator<TaskTranslation> itr = translations.iterator();
        while (itr.hasNext()) {
            TaskTranslation translation = itr.next();
            translation.setTwbOrderId(orderId);
            translation.setStatus(TaskTranslation.STATUS_IN_PROGRESS);
            updateTranslation(translation);
        }

    }

    public Map pushTranslationRequest(TranslationRequestModel request) {
        return TranslationCenterCommunicator.pushTranslationRequest(request);
    }

    public Map pushDocumentForRequest(TranslationRequestModel request) {
        return TranslationCenterCommunicator.pushDocumentForRequest(request);
    }


    public String pullAllTranslationResponses(Long clientAppId, Long twbProjectId) {
        List<Map> translationResponses = TranslationCenterCommunicator.pullAllTranslationResponses(clientAppId, twbProjectId);
        try {
            processTranslationResponses(translationResponses);
        } catch (Exception exception) {
            logger.debug("Exception caught: " + exception.toString());
        }
        return null;
    }

    private String processTranslationResponses(List<Map> translationResponses) {
        boolean error = false;
        String errorMessage = "";
        Iterator<Map> iterator = translationResponses.iterator();
        while (iterator.hasNext()) {
            try {
                Map response = iterator.next();
                Integer orderId = (Integer) response.get("order_id");
                Integer projectId = (Integer) response.get("project_id");
                List documents = (List) response.get("delivered_documents");
                if (documents.size() > 0) {
                    Map document = (Map) documents.get(documents.size() - 1);
                    processTranslationDocument((String) document.get("download_link"), (String) document.get("self_link"), orderId, projectId);
                } else {
                    throw new RuntimeException("No documents were found for order id: " + orderId + ", project id:" + projectId);
                }
            } catch (Exception ex) {
                logger.debug(ex.toString());
            }

        }
        return null;
    }


    private void processTranslationDocument(String download_link, String selfLink, Integer orderId, Integer projectId) throws Exception {
        try {
            String content = TranslationCenterCommunicator.getTranslationDocumentContent(download_link);
            processResponseDocumentContent(content, orderId, projectId);
            TranslationCenterCommunicator.updateTranslationOrder(selfLink, "accepted", "Translation was accepted");
        } catch (Exception exception) {
            logger.debug("Exception caught: " + exception.toString());
            TranslationCenterCommunicator.updateTranslationOrder(selfLink, "rejected", exception.toString());
        }
    }

    @Transactional
    private void processResponseDocumentContent(String content, Integer orderId, Integer projectId) throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader(content));
        String line;
        String[] toks;
        CSVParser parser = new CSVParser();
        int counter = 1;
        reader.readLine();  //skip the first line which is a header.
        while ((line = reader.readLine()) != null) {
            counter++;
            line = line.trim();
            if (line.length() <= 0) continue;
            try {
                toks = parser.parseLine(line);

                if (toks.length != 4) {
                    throw new RuntimeException("Invalid number of columns in row " + counter);
                }

                updateTranslation(orderId, new Long(toks[0]), toks[1], toks[2], toks[3]);
            } catch (Exception e) {
                logger.error("Invalid line: " + line + " (" + e.getMessage() + ")");
                throw new RuntimeException("Invalid line: " + line + " (" + e.getMessage() + ")");
            }
        }
    }

    private void updateTranslation(Integer orderId, Long taskId, String sourceTranslation, String finalTranslation, String code) throws Exception {
        TaskTranslation taskTranslation = findByTaskId(taskId);
        if (taskTranslation == null) {
            throw new RuntimeException("No translation task found for id:" + taskId);
        } else if (taskTranslation.getTwbOrderId() == null) {
            throw new RuntimeException("No TWB order number found for id:" + taskId);
        } else if (taskTranslation.getTwbOrderId().intValue() != orderId.intValue()) {
            throw new RuntimeException("TWB order number does not match");
        }
        taskTranslation.setTranslatedText(finalTranslation);
        if (code.length() > 9) {
            code = code.substring(0, 9);
        }
        taskTranslation.setAnswerCode(code);
        taskTranslation.setStatus(TaskTranslation.STATUS_RECEIVED);
        updateTranslation(taskTranslation);

        //

        ReportTemplate template = new ReportTemplate(taskTranslation.getTaskQueueID(),
                taskTranslation.getTaskId(), taskTranslation.getTweetID(), taskTranslation.getTranslatedText(),
                taskTranslation.getAuthor(), taskTranslation.getLat(), taskTranslation.getLon(),
                taskTranslation.getUrl(), taskTranslation.getCreated(), taskTranslation.getAnswerCode(), StatusCodeType.TEMPLATE_IS_READY_FOR_EXPORT, Long.parseLong(taskTranslation.getClientAppId()));
        reportTemplateService.saveReportItem(template);


    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void createTranslation(TaskTranslation translation) {
        taskTranslationDao.save(translation);

    }

    @Override
    @Transactional
    public void updateTranslation(TaskTranslation translation) {
        taskTranslationDao.saveOrUpdate(translation);
    }

    @Override
    @Transactional
    public TaskTranslation findById(Long translationId) {
        return taskTranslationDao.findTranslationByID(translationId);
    }

    @Transactional
    public TaskTranslation findByTaskId(Long taskId) {
        return taskTranslationDao.findTranslationByTaskID(taskId);
    }


    @Override
    @Transactional
    public void delete(TaskTranslation translation) {
        taskTranslationDao.delete(translation);
    }

    @Override
    @Transactional
    public List<TaskTranslation> findAllTranslations() {

        return taskTranslationDao.findAllTranslations();
    }

    @Transactional
    public List<TaskTranslation> findAllTranslationsByClientAppIdAndStatus(Long clientAppId, String status, Integer count) {
        return taskTranslationDao.findAllTranslationsByClientAppIdAndStatus(clientAppId, status, count);
    }
}
