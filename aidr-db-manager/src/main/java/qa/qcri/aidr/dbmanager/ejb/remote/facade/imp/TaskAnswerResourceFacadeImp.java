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


/**
 * 
 * @author Koushik
 *
 */
@Stateless(name="TaskAnswerResourceFacadeImp")
public class TaskAnswerResourceFacadeImp extends CoreDBServiceFacadeImp<TaskAnswer, Long> implements TaskAnswerResourceFacade {

	private Logger logger = Logger.getLogger("db-manager-log");

	protected TaskAnswerResourceFacadeImp(){
		super(TaskAnswer.class);
	}

	@Override
	public TaskAnswerDTO insertTaskAnswer(TaskAnswerDTO taskAnswer) {
		if (taskAnswer != null) {
			logger.info("Going to insert answer = " + taskAnswer.getAnswer() + " for  taskId = " + taskAnswer.getDocumentID());
			try {
				TaskAnswer t = taskAnswer.toEntity();
				em.persist(t);
				em.flush();			
				
				return getTaskAnswer(taskAnswer.getDocumentID(), taskAnswer.getUserID());
			} catch (Exception e) {
				System.out.println("Unable to save taskAnswer: " + taskAnswer.getDocumentID() + ", " + taskAnswer.getUserID() + ", " + taskAnswer.getAnswer());
				e.printStackTrace();
				return null;
			}
		} else {
			logger.warn("Warning! Attempted to insert null task answer!");
			return null;
		}
	}

	@Override
	public List<TaskAnswerDTO> getTaskAnswer(Long documentID) {
		Criterion criterion = Restrictions.eq("id.documentId", documentID);
		List<TaskAnswer> list = getAllByCriteria(criterion);
		if (list != null && list.isEmpty()) {
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

}
