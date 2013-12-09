package qa.qcri.aidr.trainer.api.dao.impl;

import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.TaskAnswerDao;
import qa.qcri.aidr.trainer.api.entity.TaskAnswer;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class TaskAnswerDaoImpl extends AbstractDaoImpl<TaskAnswer, String> implements TaskAnswerDao {

    protected TaskAnswerDaoImpl(){
        super(TaskAnswer.class);
    }

    @Override
    public void insertTaskAnswer(TaskAnswer taskAnswer) {
        save(taskAnswer);

       // saveOrUpdate(taskAnswer);
      //  System.out.println("taskID: " + taskAnswer.getTaskID() + "\n");
    }

}
