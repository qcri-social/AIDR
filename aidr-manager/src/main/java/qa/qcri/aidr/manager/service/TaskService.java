package qa.qcri.aidr.manager.service;

import java.util.List;

import qa.qcri.aidr.manager.hibernateEntities.AidrTask;

public interface TaskService {
	public void update(AidrTask task) throws Exception;
	public void delete(AidrTask task) throws Exception;
	public void create(AidrTask task) throws Exception;
	public AidrTask findById(Integer id) throws Exception; 
	public List <AidrTask> getAllTasksForACollection(Integer collectionId) throws Exception; 
}
