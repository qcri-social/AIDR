package qa.qcri.aidr.trainer.pybossa.service.impl;

import au.com.bytecode.opencsv.CSVParser;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.pybossa.dao.TaskTranslationDao;
import qa.qcri.aidr.trainer.pybossa.entity.*;
import qa.qcri.aidr.trainer.pybossa.format.impl.TranslationRequestModel;
import qa.qcri.aidr.trainer.pybossa.format.impl.TranslationResponseModel;
import qa.qcri.aidr.trainer.pybossa.service.*;
import qa.qcri.aidr.trainer.pybossa.store.PybossaConf;
import qa.qcri.aidr.trainer.pybossa.store.LookupCode;
import qa.qcri.aidr.trainer.pybossa.store.URLPrefixCode;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;


/**
 * Created by kamal on 3/22/15.
 */
@Service("translationService")
@Transactional(readOnly = false)
public class TWBTranslationServiceImpl implements TranslationService {

    @Autowired
    private TaskTranslationDao taskTranslationDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ReportTemplateService reportTemplateService;

    @Autowired
    private ClientAppResponseService clientAppResponseService;

    @Autowired
    private ClientAppService clientAppService;

    @Autowired
    private ClientAppEventService clientAppEventService;


    final private static int MAX_BATCH_SIZE = 1000;  //
    final private static long MAX_WAIT_TIME_MILLIS = 172800000; // 48 hours
    private static long timeOfLastTranslationProcessingMillis = System.currentTimeMillis(); //initialize at startup

    private PybossaCommunicator pybossaCommunicator = new PybossaCommunicator();

    protected static Logger logger = Logger.getLogger("service.translationService");


    public Map processTranslations(ClientApp clientApp) {
        Long tcProjectId = clientApp.getTcProjectId();
       // processReceivedTranslations(clientApp.getClientAppID(), MAX_BATCH_SIZE);
        pullAllTranslationResponses(clientApp.getClientAppID(), tcProjectId);
        return pushAllTranslations(clientApp.getClientAppID(), tcProjectId, MAX_WAIT_TIME_MILLIS, MAX_BATCH_SIZE);
    }

    public void processReceivedTranslations(Long clientAppId, int maxBatchSize){
        List<TaskTranslation> translations = findAllTranslationsByClientAppIdAndStatus(clientAppId, TaskTranslation.STATUS_RECEIVED, maxBatchSize);

        for (TaskTranslation t : translations){
            processMicroMappers(t, t.getAnswerCode());
        }
    }

    public Map pushAllTranslations(Long clientAppId, Long twbProjectId, long maxTimeToWait, int maxBatchSize) {
        //add ordering
        List<TaskTranslation> translations = findAllTranslationsByClientAppIdAndStatus(clientAppId, TaskTranslation.STATUS_NEW, maxBatchSize);
        Map result = null;
        boolean forceProcessingByTime = false;
        long currentTimeMillis = System.currentTimeMillis();

        if ((currentTimeMillis - timeOfLastTranslationProcessingMillis) >= maxTimeToWait) {
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            if(dayOfWeek != Calendar.MONDAY && dayOfWeek != Calendar.WEDNESDAY && dayOfWeek != Calendar.FRIDAY ) {
                forceProcessingByTime = true;
            }
        }

        if ((forceProcessingByTime || translations.size() >= maxBatchSize) && (translations.size() > 0)) {
            while (true) {
                logger.info("pushAllTranslations start at : " + new Date());
                TranslationRequestModel model = new TranslationRequestModel();
                model.setContactEmail("test@test.com");
                model.setTitle("Translation Request from Micromappers");
                model.setSourceLanguage("und");
                String[] targets = {"eng"};
                model.setTargetLanguages(targets);
                model.setSourceWordCount(100); //random test
                model.setInstructions("Please translate according to ...");
                model.setDeadline(new Date(System.currentTimeMillis() + PybossaConf.TWB_TRANSLATE_DEADLINE));
                model.setUrgency("high");
                model.setProjectId(twbProjectId);
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
                logger.debug(" processTranslationResponses : " + ex.toString());
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

    public void updateTranslation(Integer orderId, Long taskId, String sourceTranslation, String finalTranslation, String code) throws Exception {
        TaskTranslation taskTranslation = findByTaskId(taskId);

        if(taskTranslation.getStatus().equalsIgnoreCase(TaskTranslation.STATUS_COMPLETE)){
            return;
        }

        if(taskTranslation.getStatus().equalsIgnoreCase(TaskTranslation.STATUS_RECEIVED)){
            this.processMicroMappers( taskTranslation, code);
            return;
        }

        if (taskTranslation == null) {
            logger.error("No translation task found for id:" + taskId);
            throw new RuntimeException("No translation task found for id:" + taskId);
        } else if (taskTranslation.getTwbOrderId() == null) {
            logger.error("No TWB order number found for id:" + taskId);
            throw new RuntimeException("No TWB order number found for id:" + taskId);
        } else if (taskTranslation.getTwbOrderId().intValue() != orderId.intValue()) {
            logger.error("TWB order number does not match : TwbOrderId: " + taskTranslation.getTwbOrderId().intValue() + " - Order ID : " + orderId.intValue());
            throw new RuntimeException("TWB order number does not match : TwbOrderId: " + taskTranslation.getTwbOrderId().intValue() + " - Order ID : " + orderId.intValue());
        }
        taskTranslation.setTranslatedText(finalTranslation);
        taskTranslation.setAnswerCode(code);
        taskTranslation.setStatus(TaskTranslation.STATUS_RECEIVED);
        updateTranslation(taskTranslation);

        this.processMicroMappers(taskTranslation, code);
    }


    private void processMicroMappers(TaskTranslation taskTranslation, String code){
        try{

            if(code.equals(null) && !taskTranslation.getAnswerCode().equals(null)){
                code = taskTranslation.getAnswerCode();
            }

            if(taskTranslation.getDocumentId() == null){
                List<TaskQueueResponse> taskResp = clientAppResponseService.getTaskQueueResponse(taskTranslation.getTaskQueueID());

                if(taskResp.size() > 0){
                    String taskInfo = taskResp.get(0).getTaskInfo();
                    JSONParser parser = new JSONParser();
                    JSONArray jsonObject = (JSONArray) parser.parse(taskInfo);
                    Iterator itr= jsonObject.iterator();
                    JSONArray jsonObjectCopy = new JSONArray();

                    while(itr.hasNext()){
                        JSONObject featureJsonObj = (JSONObject)itr.next();
                        JSONObject info = (JSONObject)featureJsonObj.get("info");
                        info.put("category", code);

                        jsonObjectCopy.add(featureJsonObj)  ;
                    }

                    this.processAIDRPushing(taskTranslation, jsonObjectCopy);

                }
            }

            else{
                ClientApp app =  clientAppService.findClientAppByID("clientAppID", taskTranslation.getClientAppId());
                TranslationResponseModel taskRespModel = new TranslationResponseModel(taskTranslation,app,code);
                this.processAIDRPushing(taskTranslation, taskRespModel.getTaskResponse());
            }

            this.processReportTemplatePushing(taskTranslation, code);

        }
        catch(Exception e){
            logger.error("processAIDR:" + e);
            throw new RuntimeException("processAIDR:" + e);
        }
    }

    private void processReportTemplatePushing(TaskTranslation taskTranslation, String userAnswer){
        try{
            if(clientAppEventService.getNextSequenceClientAppEvent(taskTranslation.getClientAppId()) != null){

                JSONParser parser = new JSONParser();

                ClientAppAnswer clientAppAnswer = clientAppResponseService.getClientAppAnswer(taskTranslation.getClientAppId());

                String[] activeAnswers = this.getActiveAnswerKey(clientAppAnswer, parser) ;

                for(int a=0; a < activeAnswers.length; a++){
                    if(activeAnswers[a].equalsIgnoreCase(userAnswer)){
                        ReportTemplate template = new ReportTemplate(taskTranslation.getTaskQueueID(),
                                taskTranslation.getTaskId(), taskTranslation.getTweetID(), taskTranslation.getTranslatedText(),
                                taskTranslation.getAuthor(), taskTranslation.getLat(), taskTranslation.getLon(),
                                taskTranslation.getUrl(), taskTranslation.getCreated().toString(), taskTranslation.getAnswerCode(), LookupCode.TEMPLATE_IS_READY_FOR_EXPORT, taskTranslation.getClientAppId());
                        reportTemplateService.saveReportItem(template);
                    }
                }
            }

        }
        catch(Exception e){
            throw new RuntimeException("TWB processReportTemplatePushing");
        }

    }

    private void processAIDRPushing(TaskTranslation taskTranslation, JSONArray jsonObjectCopy)
    {
        long appID = taskTranslation.getClientAppId();
        ClientApp app =  clientAppService.findClientAppByID("clientAppID", appID);
        String AIDR_TASK_ANSWER_URL = app.getClient().getAidrHostURL() + URLPrefixCode.TASK_ANSWER_SAVE;

        pybossaCommunicator.sendPost(jsonObjectCopy.toJSONString(), AIDR_TASK_ANSWER_URL);

        taskTranslation.setStatus(TaskTranslation.STATUS_COMPLETE);
        updateTranslation(taskTranslation);

    }

    private String[] getActiveAnswerKey(ClientAppAnswer clientAppAnswer, JSONParser parser) throws ParseException {

        String answerKey =   clientAppAnswer.getActiveAnswerKey();

        if(answerKey== null){
            answerKey =   clientAppAnswer.getAnswer();
        }

        JSONArray questionArrary =   (JSONArray) parser.parse(answerKey) ;
        int questionSize =  questionArrary.size();
        String[] questions = new String[questionSize];

        for(int i=0; i< questionSize; i++){
            JSONObject obj = (JSONObject)questionArrary.get(i);
            questions[i] =   (String)obj.get("qa");
        }

        return questions;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void createTranslation(TaskTranslation translation) {
        taskTranslationDao.createTaskTranslation(translation);
        Session session = sessionFactory.getCurrentSession();
        session.flush();

    }

    @Override
    @Transactional
    public void updateTranslation(TaskTranslation translation) {
        taskTranslationDao.saveOrUpdateTaskTranslation(translation);
    }

    @Override
    @Transactional
    public TaskTranslation findById(Long translationId) {
        return taskTranslationDao.findTranslationByID(translationId);
    }

    @Transactional
    public TaskTranslation findByTaskId(Long taskId) {
        TaskTranslation taskTranslation = null;
        try{
            taskTranslation =  taskTranslationDao.findTranslationByTaskID(taskId);
            if(taskTranslation.getTaskId() == null){
                taskTranslation = null;
            }
        }
        catch(Exception e){
            List<TaskTranslation> translations =  taskTranslationDao.findAllTranslationsByTaskID(taskId);
            if(translations.size() > 0)
                taskTranslation = translations.get(0);
        }
        logger.debug("findByTaskId : taskTranslation " + taskTranslation);
        return taskTranslation;
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
