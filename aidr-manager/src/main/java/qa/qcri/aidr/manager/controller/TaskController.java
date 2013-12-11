package qa.qcri.aidr.manager.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.manager.hibernateEntities.AidrTask;
import qa.qcri.aidr.manager.service.TaskService;

@Controller
@RequestMapping("protected/task")
public class TaskController extends BaseController{ 

	private Logger logger = Logger.getLogger(TaskController.class);
	
	@Autowired
	private TaskService taskService;
	
	@RequestMapping(value = "/save.action", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> save(AidrTask task) throws Exception {
		logger.info("Save AidrTask to Database having key : "+task.getQuery());
		try{
			Calendar now = Calendar.getInstance();
			task.setStartDate(now.getTime());
			task.setStatus("ACTIVE");
			task.setCount(task.getCount()!=null ? task.getCount(): 0);
			taskService.create(task);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while saving AIDR task Info to database", e);
			return getUIWrapper(false); 
		}
	}
	
	@RequestMapping(value = "/delete.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(AidrTask task) throws Exception {
		logger.info("Deleting Task Info from Database having id "+task.getId());
		try{
			AidrTask aidrTask = taskService.findById(task.getId());
			aidrTask.setStatus("IN_ACTIVE");
			Calendar now = Calendar.getInstance();
			aidrTask.setEndDate(now.getTime());
			taskService.update(aidrTask);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while deleting AIDR Master Info from database", e);
			return getUIWrapper(false); 
		}
	}
	
	@RequestMapping(value = "/start.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> start(AidrTask task) throws Exception {
		logger.info("Starting Task having id  "+task.getId());
		try{
			AidrTask aidrTask = taskService.findById(task.getId());
			aidrTask.setStatus("START");
			Calendar now = Calendar.getInstance();
			aidrTask.setStartDate(now.getTime());
			taskService.update(aidrTask);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while deleting AIDR Master Info from database", e);
			return getUIWrapper(false); 
		}
	}
	
	
	@RequestMapping(value = "/stop.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> stop(AidrTask task) throws Exception {
		logger.info("Stoppping Task having id  "+task.getId());
		try{
			AidrTask aidrTask = taskService.findById(task.getId());
			aidrTask.setStatus("STOP");
			Calendar now = Calendar.getInstance();
			aidrTask.setEndDate(now.getTime());
			taskService.update(aidrTask);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while deleting AIDR Master Info from database", e);
			return getUIWrapper(false); 
		}
	}
	
	
	@RequestMapping(value = "/update.action", method = RequestMethod.POST )
	@ResponseBody
	public Map<String,Object> update(@RequestBody AidrTask task) throws Exception {
		logger.info("Updating Aidrtask Info into Database having id "+task.getId());
		System.out.println(task.getStartDate());
		try{
			if(task.getStatus()!=null && !"".equals(task.getStatus())){
				if("ACTIVE".equals(task.getStatus()) || "STOP".equals(task.getStatus())){
					taskService.update(task);
				}else if("START".equals(task.getStatus())){
					/**
					 * Running Task needs to be stopped for any update
					 */
					Calendar now = Calendar.getInstance();
				    task.setStatus("STOP");
				    task.setEndDate(now.getTime());
					taskService.update(task);
				}
			}
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while Updating AIDR task Info into database", e);
			return getUIWrapper(false); 
		}
	}
	
	@RequestMapping(value = "/findById.action", method = RequestMethod.GET)
	@ResponseBody
	public AidrTask findById(Integer id) throws Exception {
		logger.info("Fetch taskInfo for Id  "+id);
		return taskService.findById(id);
	}
	
	
	@RequestMapping(value = "/fetchTasksByCollId.action", method = RequestMethod.GET)
	@ResponseBody
	public List<AidrTask> getTasksByCollection(@RequestParam Integer collectionId) throws Exception {
		return taskService.getAllTasksForACollection(collectionId);
	}
	
}
