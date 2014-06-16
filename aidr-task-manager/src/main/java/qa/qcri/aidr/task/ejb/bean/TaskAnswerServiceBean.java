package qa.qcri.aidr.task.ejb.bean;


import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.task.ejb.TaskAnswerService;
import qa.qcri.aidr.task.entities.TaskAnswer;

/**
 * 
 * @author Koushik
 *
 */
@Stateless(name="TaskAnswerServiceBean")
public class TaskAnswerServiceBean extends AbstractTaskManagerServiceBean<TaskAnswer, String> implements TaskAnswerService {

    protected TaskAnswerServiceBean(){
        super(TaskAnswer.class);
    }

    @Override
    public void insertTaskAnswer(TaskAnswer taskAnswer) {
        save(taskAnswer);
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
