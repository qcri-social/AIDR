package qa.qcri.aidr.manager.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.common.values.DownloadType;
import qa.qcri.aidr.manager.dto.AidrCollectionTotalDTO;
import qa.qcri.aidr.manager.dto.CollectionDetailsInfo;
import qa.qcri.aidr.manager.dto.CollectionUpdateInfo;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.service.CollectionCollaboratorService;
import qa.qcri.aidr.manager.service.CollectionLogService;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.TaggerService;
import qa.qcri.aidr.manager.service.UserService;
import qa.qcri.aidr.manager.util.CollectionStatus;

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

	@Autowired
	private UserService userService;

	@Autowired 
	private CollectionCollaboratorService collaboratorService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}


	@RequestMapping(value = "/create", method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> createCollection(@ModelAttribute CollectionDetailsInfo collectionDetailsInfo,
			@RequestParam(value = "runAfterCreate", defaultValue = "false", required = false)
			Boolean runAfterCreate) throws Exception {

		
		logger.info("Save Collection to Database having code : "+ collectionDetailsInfo.getCode());
		
		try{
			UserAccount user = getAuthenticatedUser();
			if(collectionDetailsInfo.getPurpose().trim().length()>1000 || StringUtils.isEmpty(collectionDetailsInfo.getPurpose())){
				throw new Exception("Colection purpose is not valid !!!");
			}
			Collection collection = collectionService.create(collectionDetailsInfo, user);
			
			if(collection == null) {
				return getUIWrapper(false);
			}
			//Running collection right after creation
			if (runAfterCreate && collection != null) {
				return start(collection.getId());
			} 

			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while saving Collection Info to database", e);
			return getUIWrapper(false); 
		}
	}
	
	@RequestMapping(value = "/save.action", method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> save(
			Collection collection,
			@RequestParam(value = "runAfterCreate", defaultValue = "false", required = false)
			Boolean runAfterCreate) throws Exception {

		logger.info("Save Collection to Database having code : "+collection.getCode());
		//logger.info("Following users: " + collection.getFollow());
		try{
			UserAccount entity = getAuthenticatedUser();
			collection.setOwner(entity);
			collection.setStatus(CollectionStatus.NOT_RUNNING);
			collection.setPubliclyListed(true); 	// TODO: change default behavior to user choice

			
			if(collection.getGeoR().equalsIgnoreCase("null"))
				collection.setGeoR(null);

			//Running collection right after creation
			if (runAfterCreate) {
				return start(collection.getId());
			}

			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while saving Collection Info to database", e);
			return getUIWrapper(false); 
		}
	}
	@RequestMapping(value = "/updateDuration.action", method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> updateDuration(@RequestParam String code, @RequestParam Integer duration) throws Exception {
		//logger.info("Save Collection to Database having code : "+collection.getCode());
		try{
			Collection dbCollection = collectionService.findByCode(code);
			dbCollection.setDurationHours(duration);
			collectionService.update(dbCollection);
			return getUIWrapper(true);
		}catch(Exception e){
			logger.error("Error while updating collection duration", e);
			return getUIWrapper(false);
		}
	}

	@RequestMapping(value = "/updatePublicListing.action", method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> updatePubliclyListed(@RequestParam Long id, @RequestParam boolean publiclyListed) throws Exception {
		//logger.info("[updatePubliclyListed] Save Collection to Database having code : "+collection.getCode());
		try{
			Collection dbCollection = collectionService.findById(id);
			dbCollection.setPubliclyListed(publiclyListed); 
			collectionService.update(dbCollection);
			return getUIWrapper(true);
		}catch(Exception e){
			logger.error("Error while updating collection publicly Listed status", e);
			return getUIWrapper(false);
		}
	}

	@RequestMapping(value = "/delete.action", method = { RequestMethod.POST ,RequestMethod.GET })
	@ResponseBody
	public Map<String,Object> delete( Collection collection) throws Exception {
		//logger.info("Deleting Collection Info from Database having id "+collection.getId());
		try{
			collectionService.delete(collection);
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while deleting AIDR Collection Info from database", e);
			return getUIWrapper(false); 
		}
	}

	@RequestMapping(value = "/trash.action", method = { RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> trash(@RequestParam Long id) throws Exception {
		//logger.info("In trash collection, id = " + id);
		try {
			Collection collection = collectionService.findById(id);
			if (null == collection) {
				collection = collectionService.findTrashedById(id);
				if (collection != null) {
					AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, null);
					return getUIWrapper(dto, true);
				} else {
					String msg = "Attempting to trash collection " + id + " failed as collection not found!";
					logger.error(msg);
					return getUIWrapper(false, msg);
				}
			}
			//Otherwise, collection exists and needs trashing
			if (collection.getStatus().equals(CollectionStatus.INITIALIZING) || 
					collection.getStatus().equals(CollectionStatus.RUNNING) || 
					collection.getStatus().equals(CollectionStatus.RUNNING_WARNING)) {
				String msg = "Attempting to trash a running collection. Collection must be in stopped state!";
				logger.warn(msg);
				return getUIWrapper(false, msg);
			} else {
				// Trash collection
				//logger.info("Received request to trash collection code: " + collection.getCode());
				CollectionStatus oldStatus = collection.getStatus();
				if (oldStatus.equals(CollectionStatus.STOPPED)
						|| oldStatus.equals(CollectionStatus.NOT_RUNNING)) {
					//logger.info("Trashing collection having code " + collection.getCode());
					try {
						//collection = collectionService.stop(collection.getId());
						collection.setStatus(CollectionStatus.TRASHED);
						collection.setTrashed(true);
						collectionService.update(collection);
						if (taggerService.trashCollection(collection) > 0) {
							AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, null);
							//logger.info("Attempting to trash collection " + collection.getCode() + " succeeded!");
							return getUIWrapper(dto, true);
						} else {
							String msg = "Attempting to trash collection " + collection.getCode() + " failed!";
							logger.error(msg);
							// restore collection status to STOPPED
							collection.setStatus(oldStatus);
							collection.setTrashed(false);
							collectionService.update(collection);
							return getUIWrapper(false, msg);
						}
					} catch(Exception e) {
						String msg = "Error while trashing AIDR Collection - couldn't stop!";
						logger.error(msg, e);
						if (!collection.getStatus().equals(oldStatus)) {
							// restore collection status
							collection.setTrashed(false);
							collection.setStatus(oldStatus);
							collectionService.update(collection);
						}
						return getUIWrapper(false, msg);
					}
				} else {
					String msg = "Attempting to trash collection " + collection.getCode() + " failed as collection status = " + oldStatus.getStatus();
					logger.error(msg);
					return getUIWrapper(false, msg);
				}
			}
		} catch(Exception e){
			String msg = "Error while attempting to trash AIDR Collection - not found! ";
			logger.error(msg, e);
			return getUIWrapper(false, msg);
		}
	}

	@RequestMapping(value = "/classifier/enable", method = { RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> enableClassifier(@RequestParam String collectionCode) throws Exception {
			
		boolean success = collectionService.enableClassifier(collectionCode, getAuthenticatedUser());
				
		if(success) {
			return getUIWrapper(success);
		} else {
			return getUIWrapper(success, "Error in enabling classifier.");
		}
	}	

	@RequestMapping(value = "/untrash.action", method = { RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> untrash( Collection collection) throws Exception {
		Collection trashedCollection = collectionService.findTrashedById(collection.getId());
		if (trashedCollection != null) {
			//logger.info("Untrashing collection having code " + trashedCollection.getCode());
			try{
					trashedCollection.setStatus(CollectionStatus.STOPPED);
					trashedCollection.setTrashed(Boolean.FALSE);
					collectionService.update(trashedCollection);
					return getUIWrapper(true);  
			}catch(Exception e){
				String msg = "Error while untrashing AIDR Collection " + trashedCollection.getCode();
				logger.error(msg,e);
				return getUIWrapper(false, msg);
			}
		} else {
			String msg = "Attempting to untrash collection " + collection.getCode() + " that does not exist.";
			logger.error(msg);
			return getUIWrapper(false, msg);
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(CollectionUpdateInfo collectionInfo) throws Exception {

		UserAccount account = getAuthenticatedUser();
		Long accountId = null;
		if(account != null) {
			accountId = account.getId();
		}
		boolean success = collectionService.updateCollection(collectionInfo, accountId);
		return getUIWrapper(success);
	}

	@RequestMapping(value = "/findById.action", method = RequestMethod.GET)
	@ResponseBody
	public AidrCollectionTotalDTO findById(Long id) throws Exception {
		//logger.info("Fetch Collection for Id  "+id);
		Collection collection = collectionService.findById(id);
		List<UserAccount> collaborators = collaboratorService.fetchCollaboratorsByCollection(id);
		//logger.info("found collection: " + collection.getCode());
		AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, collaborators);
		if (dto != null) {
			Integer totalCount = collectionLogService.countTotalDownloadedItemsForCollection(id);
			//logger.info("returned tweet count = " + totalCount);
			if (CollectionStatus.RUNNING.equals(dto.getStatus()) || CollectionStatus.RUNNING_WARNING.equals(dto.getStatus())){
				totalCount += dto.getCount();
			}
			dto.setTotalCount(totalCount);
		}
		//logger.info("returning dto: " + dto.getCode());
		return dto;
	}

	@RequestMapping(value = "/findAll.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  findAll(@RequestParam Integer start, @RequestParam Integer limit,
			@DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 50;
		String userName="";
		UserAccount userEntity = getAuthenticatedUser();
		if (userEntity != null) {
			userName = userEntity.getUserName();
			List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();
			Integer count = 0;
			boolean onlyTrashed = false;
			if (trashed != null && trashed.equalsIgnoreCase("yes")) {
				onlyTrashed = true;
			}
			
			boolean sourceOutage = false;
			try {
				// Call update from Fetcher and then get list with updated items
				/*collectionService.updateAndGetRunningCollectionStatusByUser(userId);				
				*/
				//MEGHNA: refactored code to prevent multiple DB calls. collection status is updated
				//from db in the findAll call itself
				count = collectionService.getCollectionsCount(userEntity, onlyTrashed);								
				if(count > 0)
				{
					List<Collection> data = collectionService.findAll(start, limit, userEntity, onlyTrashed);
					List<String> collectionCodes = new ArrayList<String>(data.size());
					for (Collection collection : data) {
						switch(collection.getStatus())
						{
						case INITIALIZING:
						case RUNNING:
						case RUNNING_WARNING:
						case WARNING:
							collectionService.statusByCollection(collection, userEntity.getId());							
						default:
							break;						
						}
						AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, null);
						dtoList.add(dto);
						collectionCodes.add(collection.getCode());						
						if(collection.isSourceOutage()) {
							sourceOutage = true;
						}
					}
					
					Map<String, Integer> collectionsClassifiers = taggerService.countCollectionsClassifiers(collectionCodes);
					for (AidrCollectionTotalDTO dto : dtoList) {
						dto.setClassifiersNumber(collectionsClassifiers.get(dto.getCode()));
					}
				}
			} catch (Exception e) {
				logger.error("Error while finding all collections for current user: "+userName, e);
			}
			Map<String, Object> uiWrapper = getUIWrapper(dtoList, count.longValue());
			uiWrapper.put("sourceOutage", sourceOutage);
			return uiWrapper;
		}
		return getUIWrapper(false);
	}

	@RequestMapping(value = "/search.action", method = RequestMethod.GET)
	@ResponseBody
	public List<Collection> search(@RequestParam String query) throws Exception {
		UserAccount userEntity = getAuthenticatedUser();
		if(userEntity!=null){
			return collectionService.searchByName(query, userEntity.getId());
		}
		return new ArrayList<Collection>();
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
	public Map<String,Object> runningCollectionByUser(@RequestParam(value = "id") Long userId) throws Exception {
		if(userId != null){
			Collection collection = collectionService.getRunningCollectionStatusByUser(userId);
			return getUIWrapper(collection,true);
		}
		return getUIWrapper(false);
	}

	@RequestMapping(value = "/start.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> start(@RequestParam Long id) throws Exception {
		try {
			Collection collection = collectionService.findById(id);
			if (!collection.getStatus().equals(CollectionStatus.TRASHED)) {
				collection = collectionService.start(id);
				AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, null);
				if (dto != null) {
					Integer totalCount = collectionLogService.countTotalDownloadedItemsForCollection(id);
					dto.setTotalCount(totalCount);
				} else {
					return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
				}
				return getUIWrapper(dto, true);
			}
		} catch (Exception e) {
			logger.error("Error while fetching the collection by Id: "+id); 
		}
		return getUIWrapper(false);
	}

	@RequestMapping(value = "/stop.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> stop(@RequestParam Long id) throws Exception {
		
		Long accountId = null;
		UserAccount account = getAuthenticatedUser();
		if(account != null) {
			accountId = account.getId();
		}
		Collection collection = collectionService.stop(id, accountId);
		AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, null);
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
	public Map<String,Object> refreshCount(@RequestParam Long id) throws Exception {
		Collection collection = null;
		AidrCollectionTotalDTO dto = null;
		try {
			Long accountId = null;
			UserAccount account = getAuthenticatedUser();
			if(account != null) {
				accountId = account.getId();
			}
			collection = collectionService.statusById(id, accountId);
			List<UserAccount>  collaborators = collaboratorService.fetchCollaboratorsByCollection(collection.getId());
			dto = convertAidrCollectionToDTO(collection, collaborators);
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
			logger.error("Error while refreshing the count for collectionId: "+id, e); 
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		return getUIWrapper(dto,true);
	}

	@RequestMapping(value = "/updateAndGetRunningCollectionStatusByUser.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> updateAndGetRunningCollectionByUser() throws Exception {
		UserAccount userEntity = getAuthenticatedUser();
		if(userEntity!=null){
			try {
				return getUIWrapper(collectionService.updateAndGetRunningCollectionStatusByUser(userEntity.getId()),true);
			} catch (Exception e) {
				logger.error("Exception while updating and getting running collection by user");
				return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
			}
		}
		return getUIWrapper(false);
	}

	@RequestMapping(value = "/generateCSVLink.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> generateCSVLink(@RequestParam String code) throws Exception {
		Map<String, Object> result = null;
		try {
			result = collectionLogService.generateCSVLink(code);
			if (result != null && result.get("url") != null) {
				return getUIWrapper(result.get("url"),true);
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			logger.error("Exception in generating CSV download link for collection: " + code, e);
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
	}

	@RequestMapping(value = "/generateTweetIdsLink.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> generateTweetIdsLink(@RequestParam String code) throws Exception {
		Map<String, Object> result = null;
		try {
			result = collectionLogService.generateTweetIdsLink(code);
			if (result != null && result.get("url") != null) {
				return getUIWrapper(result.get("url"),true, null, (String)result.get("message"));
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			logger.error("Exception in generating CSV TweetIDs download link for collection: " + code, e);
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
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

		UserAccount userEntity = getAuthenticatedUser();
		if (userEntity != null) {
			Long total = collectionService.getRunningCollectionsCount(terms);

			List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();
			if (total > 0) {
				List<Collection> collections = collectionService.getRunningCollections(start, limit, terms, sortColumn, sortDirection);

				List<String> collectionCodes = new ArrayList<String>();
				List<Long> collectionIds = new ArrayList<Long>();
				for (Collection collection : collections) {
					collectionCodes.add(collection.getCode());
					collectionIds.add(collection.getId());
				}

				Map<String, Integer> taggersForCollections = Collections.emptyMap();
				try {
					taggersForCollections = taggerService.getTaggersForCollections(collectionCodes);
				} catch (Exception e) {
					logger.error("[getAllRunning.action] Error while getting taggers for collections: "+Arrays.toString(collectionCodes.toArray()), e);
				}

				Map<Integer, Integer> totalCountsFromLogForCollections = Collections.emptyMap();
				try {
					totalCountsFromLogForCollections = collectionLogService.countTotalDownloadedItemsForCollectionIds(collectionIds);
				} catch (Exception e) {
					logger.error("[getAllRunning.action] Error while getting total counts from log for collectionIds: "+Arrays.toString(collectionIds.toArray()), e);
				}

				for (Collection collection : collections) {
					AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, null);
					if (dto != null) {
						Integer totalCount;
						if (totalCountsFromLogForCollections.containsKey(collection.getId().intValue())) {
							totalCount = totalCountsFromLogForCollections.get(collection.getId().intValue());
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

		UserAccount userEntity = getAuthenticatedUser();
		if(userEntity != null){
			Long total = collectionService.getStoppedCollectionsCount(terms);

			List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();
			if (total > 0) {
				List<Collection> collections = collectionService.getStoppedCollections(start, limit, terms, sortColumn, sortDirection);

				List<String> collectionCodes = new ArrayList<String>();
				List<Long> collectionIds = new ArrayList<Long>();
				for (Collection collection : collections) {
					collectionCodes.add(collection.getCode());
					collectionIds.add(collection.getId());
				}

				Map<String, Integer> taggersForCollections = Collections.emptyMap();
				try {
					if(!collectionCodes.isEmpty()){
						taggersForCollections = taggerService.getTaggersForCollections(collectionCodes);
					}
				} catch (Exception e) {
					logger.error("[getAllStopped.action] Error while getting taggers for collections: "+Arrays.toString(collectionCodes.toArray()), e);
				}

				Map<Integer, Integer> totalCountsFromLogForCollections = Collections.emptyMap();
				try {
					if(!collectionIds.isEmpty()){
						totalCountsFromLogForCollections = collectionLogService.countTotalDownloadedItemsForCollectionIds(collectionIds);
					}
				} catch (Exception e) {
					logger.error("[getAllStopped.action] Error while getting total counts from log for collectionIds: "+Arrays.toString(collectionIds.toArray()), e);
				}

				for (Collection collection : collections) {
					AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, null);
					if (dto != null) {
						Integer totalCount;
						if (totalCountsFromLogForCollections.containsKey(collection.getId().intValue())) {
							totalCount = totalCountsFromLogForCollections.get(collection.getId().intValue());
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

	private AidrCollectionTotalDTO convertAidrCollectionToDTO(Collection collection, List<UserAccount> managers){
		if (collection == null){
			return null;
		}

		AidrCollectionTotalDTO dto = new AidrCollectionTotalDTO();

		dto.setId(collection.getId());
		dto.setCode(collection.getCode());
		dto.setName(collection.getName());
		//dto.setTarget(collection.getTarget());
        dto.setGeoR(collection.getGeoR());

		UserAccount user = collection.getOwner();
		dto.setUser(user);

		if (collection.getCount() != null) {
			dto.setCount(collection.getCount());
		} else {
			dto.setCount(0);
		}
		dto.setStatus(collection.getStatus());
		dto.setTrack(collection.getTrack());
		
		try {
		if(user != null) {
			dto.setFollow(collectionService.getFollowTwitterScreenNames(collection.getFollow(), user.getUserName()));
		}	
		} catch(RuntimeException e) {
			logger.error("Error", e);
		}
		dto.setGeo(collection.getGeo());
		dto.setLangFilters(collection.getLangFilters());
		dto.setStartDate(collection.getStartDate());
		dto.setEndDate(collection.getEndDate());
		dto.setCreatedDate(collection.getCreatedAt());
		dto.setLastDocument(collection.getLastDocument());
		dto.setDurationHours(collection.getDurationHours());
		dto.setPubliclyListed(collection.isPubliclyListed());
		dto.setCrisisType(collection.getCrisisType());
		dto.setCollectionType(collection.getProvider());
		dto.setHasTaggerOutput(collection.isClassifierEnabled());
		dto.setManagers(managers);
		dto.setPurpose(collection.getPurpose());
		
		return dto;
	}


	@RequestMapping(value = "/generateJSONLink.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> generateJSONLink(@RequestParam String code, 
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType) throws Exception {
		Map<String, Object> result = null;
		try {
			result = collectionLogService.generateJSONLink(code, jsonType);
			if (result != null && result.get("url") != null) {
				return getUIWrapper(result.get("url"),true);
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			logger.error("Error while generating JSON Link for collection: "+code +"and jsonType: "+jsonType, e);
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
	}

	@RequestMapping(value = "/generateJsonTweetIdsLink.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> generateJsonTweetIdsLink(@RequestParam String code,
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType) throws Exception {
		Map<String, Object> result = null;
		try {
			result = collectionLogService.generateJsonTweetIdsLink(code, jsonType);
			if (result != null && result.get("url") != null) {
				return getUIWrapper(result.get("url"),true, null, (String)result.get("message"));
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			logger.error("Error while generating JSON Tweet Ids Link for collection: "+code +"and jsonType: "+jsonType, e);
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
	}

	@RequestMapping(value = "/getTwitterUserIds.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTwitterUserID(@RequestParam(value ="userId", required=true) Long userId, @RequestParam("userList") String userList) {
		try {
			UserAccount user = userService.getById(userId);
			if (user != null) {
				String userName = user.getUserName();
				if (userList != null && !userList.isEmpty()) {
					String dataList = collectionService.getFollowTwitterIDs(userList, userName);
					if (dataList != null) {
						return getUIWrapper(dataList, true);
					} else {
						return getUIWrapper(null, false, 0L, "Error in twitter user data lookup");
					}
				} else {
					return getUIWrapper(null, false, 0L, "User list to lookup is empty");
				}
			} else {
				return getUIWrapper(null, false, 0L, "User ID provided is incorrect or doesn't exist");
			}
		} catch (Exception e) {
			logger.error("Error while getting twitter userIds", e);
			return getUIWrapper(false, "Exception in twitter user data lookup.");
		}
	}
}
