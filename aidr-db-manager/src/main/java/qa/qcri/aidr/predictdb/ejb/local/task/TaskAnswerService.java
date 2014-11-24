package qa.qcri.aidr.predictdb.ejb.local.task;


import java.util.List;

import javax.ejb.Local;

@Local
public interface TaskAnswerService extends AbstractTaskManagerService<TaskAnswer, String>{
    void insertTaskAnswer(TaskAnswer taskAnswer);
    List<TaskAnswer> getTaskAnswer(Long documentID);
    TaskAnswer getTaskAnswer(Long documentID, Long userID);
}
