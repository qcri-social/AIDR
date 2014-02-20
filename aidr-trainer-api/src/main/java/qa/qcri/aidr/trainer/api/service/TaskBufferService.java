package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.entity.TaskBuffer;
import qa.qcri.aidr.trainer.api.template.TaskBufferJsonModel;

import java.util.List;


public interface TaskBufferService {
    List<TaskBuffer> findAssignableTaskBuffer(String columnName, Integer value,String userName ,Integer maxresult);
    List<TaskBuffer> findAvailableaskBufferByCririsID(Long cririsID, String userName ,Integer assignedCount, Integer maxresult);
    List<TaskBufferJsonModel> findOneTaskBufferByCririsID(Long cririsID,String userName ,Integer assignedCount, Integer maxresult);

}
