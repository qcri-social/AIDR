package qa.qcri.aidr.dbmanager.ejb.remote.facade;


import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.dbmanager.dto.TaskAnswerDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.task.TaskAnswer;


@Remote
public interface TaskAnswerResourceFacade extends CoreDBServiceFacade<TaskAnswer, Long>{
    TaskAnswerDTO insertTaskAnswer(TaskAnswerDTO taskAnswer);
    List<TaskAnswerDTO> getTaskAnswer(Long documentID);
    TaskAnswerDTO getTaskAnswer(Long documentID, Long userID);
}
