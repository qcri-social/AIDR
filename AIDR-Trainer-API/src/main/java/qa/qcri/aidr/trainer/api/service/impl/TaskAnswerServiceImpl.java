package qa.qcri.aidr.trainer.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.Jedis.JedisNotifier;
import qa.qcri.aidr.trainer.api.dao.TaskAnswerDao;
import qa.qcri.aidr.trainer.api.entity.*;
import qa.qcri.aidr.trainer.api.service.*;

import java.util.List;

import qa.qcri.aidr.trainer.api.template.PybossaTemplate;
import qa.qcri.aidr.trainer.api.template.TaskAnswerResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 7:57 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("taskAnswerService")
@Transactional(readOnly = true)
public class TaskAnswerServiceImpl implements TaskAnswerService{

    protected static Logger logger = Logger.getLogger("service");
    private JedisNotifier jedisNotifier ;

    @Autowired
    private CrisisService crisisService;

    @Autowired
    private TaskAnswerDao taskAnswerDao;

    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @Autowired
    private DocumentNominalLabelService documentNominalLabelService;

    @Autowired
    private DocumentService documentService;

    @Override
    @Transactional(readOnly = false)
    public void insertTaskAnswer(String data) {

       // logger.debug("insertTaskAnswer..: " + data);

        PybossaTemplate pybossaTemplate = new PybossaTemplate();
        TaskAnswerResponse taskAnswerResponse = pybossaTemplate.getPybossaTaskAnswer(data, crisisService);

        List<TaskAnswer> taskAnswerList = taskAnswerResponse.getTaskAnswerList();


        if(documentService.findDocument(taskAnswerResponse.getDocumentID()) != null){
            documentService.updateHasHumanLabel(taskAnswerResponse.getDocumentID(), true);
            taskAssignmentService.revertTaskAssignment(taskAnswerResponse.getDocumentID(), taskAnswerResponse.getUserID());

            for(int i = 0; i < taskAnswerList.size(); i++){
                TaskAnswer taskAnswer = taskAnswerList.get(i);
                taskAnswerDao.insertTaskAnswer(taskAnswer);
            }


            List<DocumentNominalLabel> documentNominalLabelSet =   taskAnswerResponse.getDocumentNominalLabelList();

            for(int i = 0; i < documentNominalLabelSet.size(); i++){
                DocumentNominalLabel documentNominalLabel = documentNominalLabelSet.get(i);
                if(!documentNominalLabelService.foundDuplicateEntry(documentNominalLabel)){
                    documentNominalLabelService.saveDocumentNominalLabel(documentNominalLabel);}
            }

            if(jedisNotifier == null) {
                try {
                    jedisNotifier= new JedisNotifier();
                   // logger.debug("jedisNotifier created : " + jedisNotifier);
                }
                catch (Exception e){
                    logger.error("jedisNotifier creation : " + e);
                }
            }


            jedisNotifier.notifyToJedis(taskAnswerResponse.getJedisJson());

        }
        else{
            logger.debug("************************** Document doesn't exist ************************** ****************************************************");
        }



    }

}
