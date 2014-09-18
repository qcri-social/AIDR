package qa.qcri.aidr.trainer.api.service.impl;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.trainer.api.Jedis.JedisNotifier;
import qa.qcri.aidr.trainer.api.dao.TaskAnswerDao;
import qa.qcri.aidr.trainer.api.entity.*;
import qa.qcri.aidr.trainer.api.service.*;

import java.util.List;

import qa.qcri.aidr.trainer.api.template.PybossaTemplate;
import qa.qcri.aidr.trainer.api.template.TaskAnswerResponse;
import qa.qcri.aidr.trainer.api.util.TaskManagerEntityMapper;


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

	protected static Logger logger = LoggerFactory.getLogger(TaskAnswerServiceImpl.class);
	private static ErrorLog elog = new ErrorLog();

	private JedisNotifier jedisNotifier ;

	@Autowired
	private CrisisService crisisService;

	//@Autowired
	//private TaskAnswerDao taskAnswerDao;

	@Autowired
	private TaskAssignmentService taskAssignmentService;

	@Autowired
	private DocumentNominalLabelService documentNominalLabelService;

	@Autowired
	private DocumentService documentService;

	@Autowired TaskManagerRemote<qa.qcri.aidr.task.entities.Document, Long> taskManager;

	@Override
	public TaskAnswerResponse getTaskAnswerResponseData(String data){
		//System.out.print("getTaskAnswerResponseData: " + data);
		PybossaTemplate pybossaTemplate = new PybossaTemplate();
		TaskAnswerResponse taskAnswerResponse = pybossaTemplate.getPybossaTaskAnswer(data, crisisService);

		return taskAnswerResponse;
	}

	@Override
	public void pushTaskAnswerToJedis(TaskAnswerResponse taskAnswerResponse) {


		if(documentService.findDocument(taskAnswerResponse.getDocumentID()) != null){

			if(jedisNotifier == null) {
				try {
					jedisNotifier= new JedisNotifier();
					// logger.debug("jedisNotifier created : " + jedisNotifier);
				}
				catch (Exception e){
					logger.error("jedisNotifier creation error for :" + taskAnswerResponse.getDocumentID());
					logger.error(elog.toStringException(e));
				}
			}

			jedisNotifier.notifyToJedis(taskAnswerResponse.getJedisJson());


		}
		else{
			logger.debug("************************** Document doesn't exist ************************** ****************************************************");
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void markOnHasHumanTag(long documentID){
		documentService.updateHasHumanLabel(documentID, true);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void addToTaskAnswer(TaskAnswerResponse taskAnswerResponse){
		List<TaskAnswer> taskAnswerList = taskAnswerResponse.getTaskAnswerList();
		/*
        if(taskAnswerList.size() > 0){
            TaskAnswer taskAnswer = taskAnswerList.get(0);
            taskAnswerDao.insertTaskAnswer(taskAnswer);
        }
		 */
		if(taskAnswerList.size() > 0){
			TaskAnswer taskAnswer = taskAnswerList.get(0);
			TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
			qa.qcri.aidr.task.entities.TaskAnswer t = mapper.reverseTransformTaskAnswer(taskAnswer);
			try {
				taskManager.insertTaskAnswer(t);
			} catch (Exception e) {
				System.err.println("[addToTaskAnswer] Error in saving task answer : " + taskAnswer);
				e.printStackTrace();
			}
		}

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void addToDocumentNominalLabel(TaskAnswerResponse taskAnswerResponse){
		List<DocumentNominalLabel> documentNominalLabelSet =   taskAnswerResponse.getDocumentNominalLabelList();

		//for(int i = 0; i < documentNominalLabelSet.size(); i++){
		if(documentNominalLabelSet.size() > 0){
			DocumentNominalLabel documentNominalLabel = documentNominalLabelSet.get(0);
			if(!documentNominalLabelService.foundDuplicateEntry(documentNominalLabel)){
				documentNominalLabelService.saveDocumentNominalLabel(documentNominalLabel);
			}
		}

		// }
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void removeTaskAssignment(TaskAnswerResponse taskAnswerResponse){
		taskAssignmentService.revertTaskAssignment(taskAnswerResponse.getDocumentID(), taskAnswerResponse.getUserID());
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void processTaskAnswer(String data) {
		// logger.debug("processTaskAnswer..: " + data);
		try{
			PybossaTemplate pybossaTemplate = new PybossaTemplate();
			TaskAnswerResponse taskAnswerResponse = pybossaTemplate.getPybossaTaskAnswer(data, crisisService);
			logger.info("taskAnswerResponse = " + taskAnswerResponse);
			List<TaskAnswer> taskAnswerList = taskAnswerResponse != null ? taskAnswerResponse.getTaskAnswerList() : null;

			if(taskAnswerResponse != null && documentService.findDocument(taskAnswerResponse.getDocumentID()) != null){
				documentService.updateHasHumanLabel(taskAnswerResponse.getDocumentID(), true);
				TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
				for(int i = 0; i < taskAnswerList.size(); i++){
					TaskAnswer taskAnswer = taskAnswerList.get(i);
					//taskAnswerDao.insertTaskAnswer(taskAnswer);
					qa.qcri.aidr.task.entities.TaskAnswer t = mapper.reverseTransformTaskAnswer(taskAnswer);
					try {
						taskManager.insertTaskAnswer(t);
					} catch (Exception e) {
						System.err.println("[processTaskAnswer] Error in saving task answer : " + taskAnswer);
						e.printStackTrace();
					}
				}


				List<DocumentNominalLabel> documentNominalLabelSet =   taskAnswerResponse.getDocumentNominalLabelList();
				if (documentNominalLabelSet != null) {
					for(int i = 0; i < documentNominalLabelSet.size(); i++){
						DocumentNominalLabel documentNominalLabel = documentNominalLabelSet.get(i);
						if(!documentNominalLabelService.foundDuplicateEntry(documentNominalLabel)){
							documentNominalLabelService.saveDocumentNominalLabel(documentNominalLabel);}
					}
				}
				if (jedisNotifier == null) {
					try {
						jedisNotifier= new JedisNotifier();
						// logger.debug("jedisNotifier created : " + jedisNotifier);
					}
					catch (Exception e){
						logger.error("jedisNotifier creation error for: " + data);
						logger.error(elog.toStringException(e));
					}
				}

				jedisNotifier.notifyToJedis(taskAnswerResponse.getJedisJson());

				taskAssignmentService.revertTaskAssignment(taskAnswerResponse.getDocumentID(), taskAnswerResponse.getUserID());

			}
			else{
				logger.info("************************** Document doesn't exist ************************** ****************************************************");
			}
		}
		catch(Exception e){
			logger.error("Exception for saving task answer data : " + data);
			logger.error(elog.toStringException(e));
		}

	}
}
