package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.List;

import qa.qcri.aidr.manager.hibernateEntities.AidrTask;

public interface TaskRepository extends GenericRepository<AidrTask, Serializable> {

	public List<AidrTask> getAllTasksForACollection(Integer collectionId);
}
