package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.TaskBuffer;

import java.util.List;

public interface TaskBufferDao extends AbstractDao<TaskBuffer, String> {

    List<TaskBuffer> findAllTaskBuffer(String columnName, Integer value, Integer maxresult);
    List<TaskBuffer> findAllTaskBufferByCririsID(Long cririsID, Integer assignedCount, Integer maxresult);
}
