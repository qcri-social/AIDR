package qa.qcri.aidr.trainer.api.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAnswerDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskManagerRemote;
import qa.qcri.aidr.trainer.api.Jedis.JedisNotifier;
import qa.qcri.aidr.trainer.api.entity.DocumentNominalLabel;
import qa.qcri.aidr.trainer.api.entity.TaskAnswer;
import qa.qcri.aidr.trainer.api.service.CrisisService;
import qa.qcri.aidr.trainer.api.service.DocumentNominalLabelService;
import qa.qcri.aidr.trainer.api.service.DocumentService;
import qa.qcri.aidr.trainer.api.service.TaskAnswerService;
import qa.qcri.aidr.trainer.api.service.TaskAssignmentService;
import qa.qcri.aidr.trainer.api.template.PybossaTemplate;
import qa.qcri.aidr.trainer.api.template.TaskAnswerResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas, koushik
 * Date: 9/15/13
 * Time: 7:57 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("taskAnswerService")
@Transactional(readOnly = true)
public class TaskAnswerServiceImpl implements TaskAnswerService{

	protected static Logger logger = Logger.getLogger(TaskAnswerServiceImpl.class);

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

	@Autowired TaskManagerRemote<DocumentDTO, Long> taskManager;

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
					//System.out.println("jedisNotifier created : " + jedisNotifier);
					//logger.info("jedisNotifier created : " + jedisNotifier);
				}
				catch (Exception e){
					logger.error("jedisNotifier creation error for :" + taskAnswerResponse.getDocumentID() + e.getStackTrace());
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
	public void markOnHasHumanTag(Long documentID){
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
		if(taskAnswerList.size() > 0) {
			TaskAnswer taskAnswer = taskAnswerList.get(0);
			TaskAnswerDTO t = taskAnswer.toDTO();
			try {
				taskManager.insertTaskAnswer(t);
			} catch (Exception e) {
				logger.error("[addToTaskAnswer] Error in saving task answer : " + taskAnswer+"\t"+e.getStackTrace());
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
			//System.out.println("taskAnswerResponse = " + taskAnswerResponse);
			List<TaskAnswer> taskAnswerList = taskAnswerResponse != null ? taskAnswerResponse.getTaskAnswerList() : null;

			if(taskAnswerResponse != null && documentService.findDocument(taskAnswerResponse.getDocumentID()) != null){
				documentService.updateHasHumanLabel(taskAnswerResponse.getDocumentID(), true);
				//System.out.println("Updated hasHumanLabel field of documentID = " + taskAnswerResponse.getDocumentID());
				for(int i = 0; i < taskAnswerList.size(); i++){
					TaskAnswer taskAnswer = taskAnswerList.get(i);
					//taskAnswerDao.insertTaskAnswer(taskAnswer);
					TaskAnswerDTO t = taskAnswer.toDTO();
					try {
						taskManager.insertTaskAnswer(t);
					} catch (Exception e) {
						logger.error("Error in processing task answer:"+e.getStackTrace());
						//System.err.println("[processTaskAnswer] Error in saving task answer : " + taskAnswer);
					}
				}


				List<DocumentNominalLabel> documentNominalLabelSet =   taskAnswerResponse.getDocumentNominalLabelList();
				//logger.info("Size of documentNominalLabel list = " + documentNominalLabelSet.size());
				if (documentNominalLabelSet != null) {
					for(int i = 0; i < documentNominalLabelSet.size(); i++){
						DocumentNominalLabel documentNominalLabel = documentNominalLabelSet.get(i);
						//System.out.println("Looking at documentNominalLabel: " + documentNominalLabel.getDocumentID() + ", " + documentNominalLabel.getNominalLabelID() + ", duplicate status = " + documentNominalLabelService.foundDuplicateEntry(documentNominalLabel));
						if(!documentNominalLabelService.foundDuplicateEntry(documentNominalLabel)){
							//System.out.println("Attempting to save documentNominalLabel: " + documentNominalLabel.getDocumentID() + ", " + documentNominalLabel.getNominalLabelID());
							documentNominalLabelService.saveDocumentNominalLabel(documentNominalLabel);
						}
					}
				}
				if (jedisNotifier == null) {
					try {
						jedisNotifier= new JedisNotifier();
						logger.info("jedisNotifier created : " + jedisNotifier);
					}
					catch (Exception e){
						logger.error("jedisNotifier creation error for: " + data+"\t"+e.getStackTrace());
					}
				}
				//System.out.println("Attempting to push to JEDIS now: " + taskAnswerResponse.getDocumentID());
				//System.out.println("taskAnswerResponse jedisJson = " + taskAnswerResponse.getJedisJson());
				jedisNotifier.notifyToJedis(taskAnswerResponse.getJedisJson());

				//System.out.println("Attempting remove task from task_assignment table now: " + taskAnswerResponse.getDocumentID());
				taskAssignmentService.revertTaskAssignment(taskAnswerResponse.getDocumentID(), taskAnswerResponse.getUserID());

			}
			else{
				logger.info("************************** Document doesn't exist ************************** ****************************************************");
			}
		}
		catch(Exception e){
			logger.error("Exception for saving task answer data : " + data+"\t"+e.getStackTrace());
		}

	}
}
