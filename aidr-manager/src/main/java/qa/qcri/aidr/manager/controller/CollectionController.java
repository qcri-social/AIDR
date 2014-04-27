package qa.qcri.aidr.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import qa.qcri.aidr.manager.dto.AidrCollectionTotalDTO;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.service.CollectionLogService;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.TaggerService;
import qa.qcri.aidr.manager.util.CollectionStatus;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@Controller
@RequestMapping("protected/collection")
public class CollectionController extends BaseController{

	private Logger logger = Logger.getLogger(CollectionController.class);

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private CollectionLogService collectionLogService;

	@Autowired
	private TaggerService taggerService;

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
			collection.setPubliclyListed(true); 	// TODO: change default behavior to user choice
			Calendar now = Calendar.getInstance();
			collection.setCreatedDate(now.getTime());
			List<UserEntity> managers = new ArrayList<UserEntity>();
			managers.add(entity);
			collection.setManagers(managers);
			collectionService.create(collection);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while saving AidrCollection Info to database", e);
			return getUIWrapper(false); 
		}
	}

	@RequestMapping(value = "/updateDuration.action", method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> updateDuration(AidrCollection collection) throws Exception {
		logger.info("Save AidrCollection to Database having code : "+collection.getCode());
		try{
			AidrCollection dbCollection = collectionService.findById(collection.getId());
			dbCollection.setDurationHours(collection.getDurationHours());
			collectionService.update(dbCollection);
			return getUIWrapper(true);
		}catch(Exception e){
			logger.error("Error while updating collection duration", e);
			return getUIWrapper(false);
		}
	}

	@RequestMapping(value = "/updatePublicListing.action", method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> updatePubliclyListed(AidrCollection collection) throws Exception {
		logger.info("[updatePubliclyListed] Save AidrCollection to Database having code : "+collection.getCode());
		try{
			AidrCollection dbCollection = collectionService.findById(collection.getId());
			System.out.println("[updatePubliclyListed] old status: " + dbCollection.getPubliclyListed() + ", new status: " + collection.getPubliclyListed());
			dbCollection.setPubliclyListed(collection.getPubliclyListed()); 
			collectionService.update(dbCollection);
			return getUIWrapper(true);
		}catch(Exception e){
			logger.error("Error while updating collection publicly Listed status", e);
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

	@RequestMapping(value = "/trash.action", method = { RequestMethod.POST ,RequestMethod.GET })
	@ResponseBody
	public Map<String,Object> trash( AidrCollection collection) throws Exception {
		if (collection.getStatus().equals(CollectionStatus.STOPPED)) {
			logger.info("Trashing collection having code " + collection.getCode());
			try{
				AidrCollection c = collectionService.stop(collection.getId());
				collection.setStatus(CollectionStatus.TRASHED);
				collectionService.update(collection);
				if (taggerService.trashCollection(collection) > 0) {
					return getUIWrapper(true);
				} else {
					logger.error("Attempting to trash collection " + collection.getCode() + " failed!");
					return getUIWrapper(false);
				}
			}catch(Exception e){
				logger.error("Error while trashing AIDR Collection ", e);
				return getUIWrapper(false); 
			}
		} else {
			logger.error("Attempting to trash a running collection. Collection must be in stopped state!");
			return getUIWrapper(false);
		}
	}

	@RequestMapping(value = "/untrash.action", method = { RequestMethod.POST ,RequestMethod.GET })
	@ResponseBody
	public Map<String,Object> untrash( AidrCollection collection) throws Exception {
		if (collection.getStatus().equals(CollectionStatus.TRASHED)) {
			logger.info("Untrashing collection having code " + collection.getCode());
			try{
				if (taggerService.untrashCollection(collection) > 0) {
					collection.setStatus(CollectionStatus.STOPPED);
					collectionService.update(collection);
					AidrCollection c = collectionService.start(collection.getId());
					return getUIWrapper(true);  
				} else {
					logger.error("Attempting to untrash collection " + collection.getCode() + " failed! ");
					return getUIWrapper(false);
				}
			}catch(Exception e){
				logger.error("Error while untrashing AIDR Collection ", e);
				return getUIWrapper(false); 
			}
		} else {
			logger.error("Attempting to untrash collection " + collection.getCode() + " failed! ");
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
				AidrCollection collectionAfterStop = collectionService.stopAidrFetcher(dbCollection);

				//              save current state of the collection to collectionLog
				AidrCollectionLog collectionLog = new AidrCollectionLog();
				collectionLog.setCount(dbCollection.getCount());
				collectionLog.setEndDate(collectionAfterStop.getEndDate());
				collectionLog.setFollow(dbCollection.getFollow());
				collectionLog.setGeo(dbCollection.getGeo());
				collectionLog.setLangFilters(dbCollection.getLangFilters());
				collectionLog.setStartDate(dbCollection.getStartDate());
				collectionLog.setTrack(dbCollection.getTrack());
				collectionLog.setCollectionID(collectionId);
				collectionLogService.create(collectionLog);

				//              set some fields from old collection and update collection
				collection.setStartDate(dbCollection.getStartDate());
				collection.setEndDate(collectionAfterStop.getEndDate());
				collection.setUser(dbCollection.getUser());
				collection.setManagers(dbCollection.getManagers());
				collection.setCreatedDate(dbCollection.getCreatedDate());
				collection.setPubliclyListed(dbCollection.getPubliclyListed());
				collectionService.update(collection);

				//              start collection
				collectionService.startFetcher(collectionService.prepareFetcherRequest(collection), collection);
			} else {
				AidrCollection dbCollection = collectionService.findById(collection.getId());
				collection.setStartDate(dbCollection.getStartDate());
				collection.setUser(dbCollection.getUser());
				collection.setManagers(dbCollection.getManagers());
				collection.setCreatedDate(dbCollection.getCreatedDate());
				collection.setPubliclyListed(dbCollection.getPubliclyListed());
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
	public Map<String,Object>  findAll(@RequestParam Integer start, @RequestParam Integer limit,
			@DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 50;
		UserEntity userEntity = getAuthenticatedUser();
		if (userEntity != null) {
			List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();
			Integer count = 0;
			try {
				Integer userId = userEntity.getId();
				//                Call update from Fetcher and then get list with updated items
				collectionService.updateAndGetRunningCollectionStatusByUser(userId);

				count = collectionService.getCollectionsCount(userEntity);
				if (count > 0) {
					List<AidrCollection> data = collectionService.findAll(start, limit, userEntity);
					for (AidrCollection collection : data) {
						if (trashed != null && trashed.equalsIgnoreCase("yes")) {
							AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
							dtoList.add(dto);
						} else {
							if (!CollectionStatus.TRASHED.equals(collection.getStatus())) {
								AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
								dtoList.add(dto);
							}
						}


					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return getUIWrapper(dtoList, count.longValue());
		}
		return getUIWrapper(false);
	}

	@RequestMapping(value = "/search.action", method = RequestMethod.GET)
	@ResponseBody
	public List<AidrCollection> search(@RequestParam String query) throws Exception {
		UserEntity userEntity = getAuthenticatedUser();
		if(userEntity!=null){
			return collectionService.searchByName(query, userEntity.getId());
		}
		return new ArrayList<AidrCollection>();
	}

	@RequestMapping(value = "/exist.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> exist(@RequestParam String code) throws Exception {
		return getUIWrapper(collectionService.exist(code),true);
	}

	@RequestMapping(value = "/existName.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> existName(@RequestParam String name) throws Exception {
		return getUIWrapper(collectionService.existName(name.trim()),true);
	}

	@RequestMapping(value = "/getRunningCollectionStatusByUser.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> runningCollectionByUser(@RequestParam(value = "id") Integer userId) throws Exception {
		if(userId != null){
			AidrCollection collection = collectionService.getRunningCollectionStatusByUser(userId);
			return getUIWrapper(collection,true);
		}
		return getUIWrapper(false);
	}

	@RequestMapping(value = "/start.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> start(@RequestParam Integer id) throws Exception {
		try {
			AidrCollection collection = collectionService.findById(id);
			if (!collection.getStatus().equals(CollectionStatus.TRASHED)) {
				collection = collectionService.start(id);
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

	@RequestMapping(value = "/getAllRunning.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAllRunning(@RequestParam Integer start,
			@RequestParam Integer limit,
			@RequestParam(value = "terms", required = false, defaultValue = "") String terms,
			@RequestParam(value = "sortColumn", required = false, defaultValue = "") String sortColumn,
			@RequestParam(value = "sortDirection", required = false, defaultValue = "") String sortDirection) throws Exception {
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 20;

		UserEntity userEntity = getAuthenticatedUser();
		if (userEntity != null) {
			Long total = collectionService.getRunningCollectionsCount(terms);

			List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();
			if (total > 0) {
				List<AidrCollection> collections = collectionService.getRunningCollections(start, limit, terms, sortColumn, sortDirection);

				List<String> collectionCodes = new ArrayList<String>();
				List<Integer> collectionIds = new ArrayList<Integer>();
				for (AidrCollection collection : collections) {
					collectionCodes.add(collection.getCode());
					collectionIds.add(collection.getId());
				}

				Map<String, Integer> taggersForCollections = Collections.emptyMap();
				try {
					taggersForCollections = taggerService.getTaggersForCollections(collectionCodes);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Map<Integer, Integer> totalCountsFromLogForCollections = Collections.emptyMap();
				try {
					totalCountsFromLogForCollections = collectionLogService.countTotalDownloadedItemsForCollectionIds(collectionIds);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (AidrCollection collection : collections) {
					AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
					if (dto != null) {
						Integer totalCount;
						if (totalCountsFromLogForCollections.containsKey(collection.getId())) {
							totalCount = totalCountsFromLogForCollections.get(collection.getId());
						} else {
							totalCount = 0;
						}

						if (CollectionStatus.RUNNING.equals(dto.getStatus()) || CollectionStatus.RUNNING_WARNING.equals(dto.getStatus())) {
							totalCount += dto.getCount();
						}
						dto.setTotalCount(totalCount);
						dtoList.add(dto);

						if (taggersForCollections.containsKey(collection.getCode())) {
							dto.setTaggersCount(taggersForCollections.get(collection.getCode()));
						}
					}
				}
			}
			return getUIWrapper(dtoList, total);
		}
		return getUIWrapper(false);
	}

	@RequestMapping(value = "/getAllStopped.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  getAllStopped(@RequestParam Integer start,
			@RequestParam Integer limit,
			@RequestParam (value = "terms", required = false, defaultValue = "") String terms,
			@RequestParam (value = "sortColumn", required = false, defaultValue = "") String sortColumn,
			@RequestParam (value = "sortDirection", required = false, defaultValue = "") String sortDirection) throws Exception {
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit :20;

		UserEntity userEntity = getAuthenticatedUser();
		if(userEntity != null){
			Long total = collectionService.getStoppedCollectionsCount(terms);

			List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();
			if (total > 0) {
				List<AidrCollection> collections = collectionService.getStoppedCollections(start, limit, terms, sortColumn, sortDirection);

				List<String> collectionCodes = new ArrayList<String>();
				List<Integer> collectionIds = new ArrayList<Integer>();
				for (AidrCollection collection : collections) {
					collectionCodes.add(collection.getCode());
					collectionIds.add(collection.getId());
				}

				Map<String, Integer> taggersForCollections = Collections.emptyMap();
				try {
					taggersForCollections = taggerService.getTaggersForCollections(collectionCodes);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Map<Integer, Integer> totalCountsFromLogForCollections = Collections.emptyMap();
				try {
					totalCountsFromLogForCollections = collectionLogService.countTotalDownloadedItemsForCollectionIds(collectionIds);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (AidrCollection collection : collections) {
					AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
					if (dto != null) {
						Integer totalCount;
						if (totalCountsFromLogForCollections.containsKey(collection.getId())) {
							totalCount = totalCountsFromLogForCollections.get(collection.getId());
						} else {
							totalCount = 0;
						}

						if (CollectionStatus.RUNNING.equals(dto.getStatus()) || CollectionStatus.RUNNING_WARNING.equals(dto.getStatus())) {
							totalCount += dto.getCount();
						}
						dto.setTotalCount(totalCount);
						dtoList.add(dto);

						if (taggersForCollections.containsKey(collection.getCode())) {
							dto.setTaggersCount(taggersForCollections.get(collection.getCode()));
						}
					}
				}
			}
			return getUIWrapper(dtoList, total);
		}
		return getUIWrapper(false);
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

		UserEntity user = collection.getUser();
		user.setRoles(null);
		dto.setUser(user);

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
		dto.setDurationHours(collection.getDurationHours());

		List<UserEntity> managers = collection.getManagers();
		for (UserEntity manager : managers) {
			manager.setRoles(null);
		}
		dto.setManagers(managers);

		return dto;
	}

}
