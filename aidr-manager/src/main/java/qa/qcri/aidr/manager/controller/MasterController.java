package qa.qcri.aidr.manager.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.manager.hibernateEntities.AidrMaster;
import qa.qcri.aidr.manager.service.MasterService;

@Controller
@RequestMapping("protected/master")
public class MasterController extends BaseController{

	private Logger logger = Logger.getLogger(MasterController.class);
	
	@Autowired
	private MasterService masterService;
	
	@RequestMapping(value = "/getAll.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getAll() throws Exception {
		logger.info("Get all AIDRMaster Details");
		try{
			List<AidrMaster> response = masterService.getAll();
			logger.info("Size of businessCards "+response!=null ? response.size():0);
			return getUIWrapper(response,true);  
		}catch(Exception e){
			logger.error("Error while getting all AIDRMaster Details", e);
			return getUIWrapper(false); 
		}
	}

	@RequestMapping(value = "/save.action", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> save(AidrMaster master) throws Exception {
		logger.info("Save Master Info to Database having key : "+master.getKey());
		try{
			masterService.create(master);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while saving AIDR Master Info to database", e);
			return getUIWrapper(false); 
		}
	}
	
	@RequestMapping(value = "/delete.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> delete(AidrMaster master) throws Exception {
		logger.info("Deleting AidrMaster Info from Database having id "+master.getId());
		try{
			masterService.delete(master);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while deleting AIDR Master Info from database", e);
			return getUIWrapper(false); 
		}
	}
	
	@RequestMapping(value = "/update.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(AidrMaster master) throws Exception {
		logger.info("Updating AidrMaster Info into Database having id "+master.getId());
		try{
			masterService.update(master);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while Updating AIDR Master Info into database", e);
			return getUIWrapper(false); 
		}
	}
	
	@RequestMapping(value = "/findById.action", method = RequestMethod.GET)
	@ResponseBody
	public AidrMaster findById(Integer id) throws Exception {
		logger.info("Fetch MasterInfo for Id  "+id);
		return masterService.findById(id);
	}
	
	@RequestMapping(value = "/findBykey.action", method = RequestMethod.GET)
	@ResponseBody
	public AidrMaster findByKey(String key) throws Exception {
		logger.info("Fetch MasterInfo for key  "+key);
		return masterService.findByKey(key);
	}
	
	
}
