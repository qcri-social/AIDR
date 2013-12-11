package qa.qcri.aidr.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.manager.hibernateEntities.AidrTask;
import qa.qcri.aidr.manager.repository.TaskRepository;
import qa.qcri.aidr.manager.service.TaskService;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepository;
	
	@Override
	@Transactional(readOnly=false)
	public void update(AidrTask task) throws Exception {
		taskRepository.update(task);
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(AidrTask task) throws Exception {
		taskRepository.delete(task);

	}

	@Override
	@Transactional(readOnly=false)
	public void create(AidrTask task) throws Exception {
		taskRepository.save(task);
	}

	@Override
	@Transactional(readOnly=true)
	public AidrTask findById(Integer id) throws Exception {
		return taskRepository.findById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<AidrTask> getAllTasksForACollection(Integer collectionId) throws Exception {
		return taskRepository.getAllTasksForACollection(collectionId);
	}

}
