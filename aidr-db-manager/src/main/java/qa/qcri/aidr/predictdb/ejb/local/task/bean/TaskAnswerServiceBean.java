package qa.qcri.aidr.predictdb.ejb.local.task.bean;


import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictdb.ejb.local.task.TaskAnswerService;


/**
 * 
 * @author Koushik
 *
 */
@Stateless(name="TaskAnswerServiceBean")
public class TaskAnswerServiceBean extends AbstractTaskManagerServiceBean<TaskAnswer, String> implements TaskAnswerService {
	
	private Logger logger = LoggerFactory.getLogger(TaskAnswerServiceBean.class);
	private ErrorLog elog = new ErrorLog();
	
    protected TaskAnswerServiceBean(){
        super(TaskAnswer.class);
    }

    @Override
    public void insertTaskAnswer(TaskAnswer taskAnswer) {
        if (taskAnswer != null) {
    	logger.info("Going to insert answer = " + taskAnswer.getAnswer() + " for  taskId = " + taskAnswer.getDocumentID());
    	save(taskAnswer);
        } else {
        	logger.warn("Warning! Attempted to insert null task answer!");
        }
    }

	@Override
	public List<TaskAnswer> getTaskAnswer(Long documentID) {
		Criterion criterion = Restrictions.eq("documentID", documentID);
		return getAllByCriteria(criterion);
	}

	@Override
	public TaskAnswer getTaskAnswer(Long documentID, Long userID) {
		Criterion criterion = Restrictions.conjunction()
								.add(Restrictions.eq("documentID", documentID))
								.add(Restrictions.eq("userID", userID));
		return getByCriteria(criterion);
	}

}
