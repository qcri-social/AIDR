package qa.qcri.aidr.manager.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import qa.qcri.aidr.manager.dto.AidrCollectionTotalDTO;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;
import qa.qcri.aidr.manager.service.CollectionLogService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.manager.dto.CollectionDataResponse;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.util.CollectionStatus;

@Controller
@RequestMapping("protected/collection")
public class CollectionController extends BaseController{

	private Logger logger = Logger.getLogger(CollectionController.class);
	
	@Autowired
	private CollectionService collectionService;

    @Autowired
    private CollectionLogService collectionLogService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@RequestMapping(value = "/save.action", method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> save(AidrCollection collection) throws Exception {
		logger.info("Save AidrCollection to Database having code : "+collection.getCode());
		try{
			UserEntity entity = getAuthenticatedUser();
			collection.setUser(entity);
			collection.setStatus(CollectionStatus.NOT_RUNNING);
			Calendar now = Calendar.getInstance();
			collection.setCreatedDate(now.getTime());
			collectionService.create(collection);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while saving AidrCollection Info to database", e);
			return getUIWrapper(false); 
		}
	}
	
	@RequestMapping(value = "/delete.action", method = { RequestMethod.POST ,RequestMethod.GET })
	@ResponseBody
	public Map<String,Object> delete( AidrCollection collection) throws Exception {
		logger.info("Deleting AidrCollection Info from Database having id "+collection.getId());
		try{
			collectionService.delete(collection);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while deleting AIDR Collection Info from database", e);
			return getUIWrapper(false); 
		}
	}
	
	@RequestMapping(value = "/update.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update( AidrCollection collection) throws Exception {
        Integer collectionId = collection.getId();
		logger.info("Updating AidrCollection into Database having id " + collectionId);
		try{
            CollectionStatus status = collection.getStatus();
            if (CollectionStatus.RUNNING_WARNING.equals(status) || CollectionStatus.RUNNING.equals(status)) {
                AidrCollection dbCollection = collectionService.findById(collectionId);
//              stop collection
                collectionService.stopAidrFetcher(dbCollection);

//              save current state of the collection to collectionLog
                AidrCollectionLog collectionLog = new AidrCollectionLog();
                collectionLog.setCount(dbCollection.getCount());
                collectionLog.setEndDate(dbCollection.getEndDate());
                collectionLog.setFollow(dbCollection.getFollow());
                collectionLog.setGeo(dbCollection.getGeo());
                collectionLog.setLangFilters(dbCollection.getLangFilters());
                collectionLog.setStartDate(dbCollection.getStartDate());
                collectionLog.setTrack(dbCollection.getTrack());
                collectionLog.setCollectionID(collectionId);
                collectionLogService.create(collectionLog);

//              set some fields from old collection and update collection
                collection.setStartDate(dbCollection.getStartDate());
                collection.setEndDate(dbCollection.getEndDate());
                collection.setUser(dbCollection.getUser());
                collection.setCreatedDate(dbCollection.getCreatedDate());
                collectionService.update(collection);

//              start collection
                collectionService.startFetcher(collectionService.prepareFetcherRequest(collection), collection);
            } else {
                AidrCollection dbCollection = collectionService.findById(collection.getId());
                collection.setStartDate(dbCollection.getStartDate());
                collection.setUser(dbCollection.getUser());
                collection.setCreatedDate(dbCollection.getCreatedDate());
                collectionService.update(collection);
            }
			return getUIWrapper(true);
		}catch(Exception e){
			logger.error("Error while Updating AidrCollection  Info into database", e);
			return getUIWrapper(false); 
		}
	}
	
	@RequestMapping(value = "/findById.action", method = RequestMethod.GET)
	@ResponseBody
	public AidrCollectionTotalDTO findById(Integer id) throws Exception {
		logger.info("Fetch AidrCollection for Id  "+id);
        AidrCollection collection = collectionService.findById(id);
        AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
        if (dto != null) {
            Integer totalCount = collectionLogService.countTotalDownloadedItemsForCollection(id);
            if (CollectionStatus.RUNNING.equals(dto.getStatus()) || CollectionStatus.RUNNING_WARNING.equals(dto.getStatus())){
                totalCount += dto.getCount();
            }
            dto.setTotalCount(totalCount);
        }
        return dto;
	}
	
	@RequestMapping(value = "/findAll.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  findAll(@RequestParam Integer start, @RequestParam Integer limit ) throws Exception {
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit :50;
		UserEntity userEntity = getAuthenticatedUser();
		if(userEntity!=null){
            Integer userId = userEntity.getId();
//            Call update from Fetcher and then get list with updated items
            try {
                collectionService.updateAndGetRunningCollectionStatusByUser(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            CollectionDataResponse dataResponse = collectionService.findAll(start, limit, userId);
			return getUIWrapper(dataResponse.getData(),dataResponse.getTotal());
		}
		return getUIWrapper(false);

	}
	
	@RequestMapping(value = "/search.action", method = RequestMethod.GET)
	@ResponseBody
	public List<AidrCollection> search(@RequestParam String query) throws Exception {
		UserEntity userEntity = getAuthenticatedUser();
		if(userEntity!=null){
			return collectionService.searchByName(query , userEntity.getId());
		}
		return new ArrayList<AidrCollection>();
	}
	
	@RequestMapping(value = "/exist.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> exist(@RequestParam String code) throws Exception {
       return  getUIWrapper(collectionService.exist(code),true);
	}
	
	@RequestMapping(value = "/getRunningCollectionStatusByUser.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> runningCollectionByUser() throws Exception {
	   UserEntity userEntity = getAuthenticatedUser();
	   if(userEntity!=null){
		  return getUIWrapper(collectionService.getRunningCollectionStatusByUser(userEntity.getId()),true);
	   }
	   return getUIWrapper(false);
	}
	
	@RequestMapping(value = "/start.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> start(@RequestParam Integer id) throws Exception {
        try {
            UserEntity userEntity = getAuthenticatedUser();
            if (userEntity != null) {
                AidrCollection collection = collectionService.start(id, userEntity.getId());
                AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
                if (dto != null) {
                    Integer totalCount = collectionLogService.countTotalDownloadedItemsForCollection(id);
                    if (CollectionStatus.RUNNING.equals(dto.getStatus()) || CollectionStatus.RUNNING_WARNING.equals(dto.getStatus())){
                        totalCount += dto.getCount();
                    }
                    dto.setTotalCount(totalCount);
                } else {
                    return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
                }
                return getUIWrapper(dto, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getUIWrapper(false);
	}
	
	@RequestMapping(value = "/stop.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> stop(@RequestParam Integer id) throws Exception {
         AidrCollection collection = collectionService.stop(id);
         AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
         if (dto != null) {
             Integer totalCount = collectionLogService.countTotalDownloadedItemsForCollection(id);
             if (CollectionStatus.RUNNING.equals(dto.getStatus()) || CollectionStatus.RUNNING_WARNING.equals(dto.getStatus())){
                 totalCount += dto.getCount();
             }
             dto.setTotalCount(totalCount);
         } else {
             return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
         }
         return getUIWrapper(dto, true);
	}
	
	
	@RequestMapping(value = "/refreshCount.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> refreshCount(@RequestParam Integer id) throws Exception {
        AidrCollection collection = null;
        AidrCollectionTotalDTO dto = null;
        try {
            collection = collectionService.statusById(id);
            dto = convertAidrCollectionToDTO(collection);
            if (dto != null) {
                Integer totalCount = collectionLogService.countTotalDownloadedItemsForCollection(id);
                if (CollectionStatus.RUNNING.equals(dto.getStatus()) || CollectionStatus.RUNNING_WARNING.equals(dto.getStatus())){
                    totalCount += dto.getCount();
                }
                dto.setTotalCount(totalCount);
            } else {
                return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
        }
        return getUIWrapper(dto,true);
	}

    @RequestMapping(value = "/updateAndGetRunningCollectionStatusByUser.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> updateAndGetRunningCollectionByUser() throws Exception {
        UserEntity userEntity = getAuthenticatedUser();
        if(userEntity!=null){
            try {
                return getUIWrapper(collectionService.updateAndGetRunningCollectionStatusByUser(userEntity.getId()),true);
            } catch (Exception e) {
                e.printStackTrace();
                return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
            }
        }
        return getUIWrapper(false);
    }

    @RequestMapping(value = "/generateCSVLink.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> generateCSVLink(@RequestParam String code) throws Exception {
        String result = "";
        try {
            result = collectionLogService.generateCSVLink(code);
        } catch (Exception e) {
            e.printStackTrace();
            return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
        }
        return getUIWrapper(result,true);
    }

    @RequestMapping(value = "/generateTweetIdsLink.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> generateTweetIdsLink(@RequestParam String code) throws Exception {
        String result = "";
        try {
            result = collectionLogService.generateTweetIdsLink(code);
        } catch (Exception e) {
            e.printStackTrace();
            return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
        }
        return getUIWrapper(result,true);
    }

    private AidrCollectionTotalDTO convertAidrCollectionToDTO(AidrCollection collection){
        if (collection == null){
            return null;
        }

        AidrCollectionTotalDTO dto = new AidrCollectionTotalDTO();

        dto.setId(collection.getId());
        dto.setCode(collection.getCode());
        dto.setName(collection.getName());
        dto.setTarget(collection.getTarget());
        dto.setUser(collection.getUser());
        if (collection.getCount() != null) {
            dto.setCount(collection.getCount());
        } else {
            dto.setCount(0);
        }
        dto.setStatus(collection.getStatus());
        dto.setTrack(collection.getTrack());
        dto.setFollow(collection.getFollow());
        dto.setGeo(collection.getGeo());
        dto.setLangFilters(collection.getLangFilters());
        dto.setStartDate(collection.getStartDate());
        dto.setEndDate(collection.getEndDate());
        dto.setCreatedDate(collection.getCreatedDate());
        dto.setLastDocument(collection.getLastDocument());

        return dto;
    }
	
}
