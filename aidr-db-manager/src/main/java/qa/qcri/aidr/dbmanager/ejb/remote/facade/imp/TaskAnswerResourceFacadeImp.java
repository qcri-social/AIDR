/**
 * Implements operations for managing the task_answer table of the aidr_predict DB
 * 
 * @author Koushik
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.dbmanager.dto.TaskAnswerDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskAnswerResourceFacade;
import qa.qcri.aidr.dbmanager.entities.task.TaskAnswer;


@Stateless(name="TaskAnswerResourceFacadeImp")
public class TaskAnswerResourceFacadeImp extends CoreDBServiceFacadeImp<TaskAnswer, Long> implements TaskAnswerResourceFacade {

	private Logger logger = Logger.getLogger("db-manager-log");

	protected TaskAnswerResourceFacadeImp(){
		super(TaskAnswer.class);
	}

	@Override
	public void insertTaskAnswer(TaskAnswerDTO taskAnswer) {
		if (taskAnswer != null) {
			logger.debug("Going to insert answer = " + taskAnswer.getAnswer() + " for  documentId = " + taskAnswer.getDocumentID());
			try {
				TaskAnswerDTO answer = getTaskAnswer(taskAnswer.getDocumentID(), taskAnswer.getUserID());
				if(answer == null) {
					TaskAnswer t = taskAnswer.toEntity();
					em.persist(t);
					em.flush();			
				}
			} catch (Exception e) {
				logger.error("Unable to save taskAnswer: " + taskAnswer.getDocumentID() + ", " + taskAnswer.getUserID() + ", " + taskAnswer.getAnswer(), e);
			}
		} else {
			logger.warn("Warning! Attempted to insert null task answer!");
		}
	}

	@Override
	public List<TaskAnswerDTO> getTaskAnswer(Long documentID) {
		Criterion criterion = Restrictions.eq("id.documentId", documentID);
		List<TaskAnswer> list = getAllByCriteria(criterion);
		if (list != null && !list.isEmpty()) {
			List<TaskAnswerDTO> dtoList = new ArrayList<TaskAnswerDTO>();
			for (TaskAnswer t: list) {
				dtoList.add(new TaskAnswerDTO(t));
			}
			return dtoList;
		}
		return null;
	}

	@Override
	public TaskAnswerDTO getTaskAnswer(Long documentID, Long userID) {
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("id.documentId", documentID))
				.add(Restrictions.eq("id.userId", userID));
		TaskAnswer t = getByCriteria(criterion);
		return t != null ? new TaskAnswerDTO(t) : null;
	}

	@Override
	public int undoTaskAnswer(Long documentID, Long userID) {
		try {
			TaskAnswerDTO taskAnswerDTO = (TaskAnswerDTO) getTaskAnswer(documentID, userID);
			if(taskAnswerDTO!=null){
				Object managed = em.merge(taskAnswerDTO.toEntity());
				em.remove(managed);
				em.flush();
				return 1;
			}
		} catch (Exception e) {
			logger.error("Error in undo operation!");
		}
		return 0;
	}
	
	@Override
	public boolean deleteTaskAnswer(Long documentID) {
		
		try {
			Criterion criterion = Restrictions.eq("id.documentId", documentID);
			List<TaskAnswer> answers = getAllByCriteria(criterion);
			if(answers != null) {
				
				// delete task
				for (TaskAnswer taskAnswer : answers) {
					delete(taskAnswer);
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("Error in deleting taskAnswer give documentID : " + documentID);
			return false;
		}
	}
	
}
