package qa.qcri.aidr.task.ejb;


import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.task.entities.TaskAnswer;



@Local
public interface TaskAnswerService extends AbstractTaskManagerService<TaskAnswer, String>{
    void insertTaskAnswer(TaskAnswer taskAnswer);
    List<TaskAnswer> getTaskAnswer(Long documentID);
    TaskAnswer getTaskAnswer(Long documentID, Long userID);
}
