package qa.qcri.aidr.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import qa.qcri.aidr.common.code.ResponseWrapperNEW;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.common.values.DownloadType;
import qa.qcri.aidr.manager.dto.*;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.service.CollectionLogService;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.TaggerService;
import qa.qcri.aidr.manager.service.UserService;
import qa.qcri.aidr.manager.util.CollectionStatus;

import twitter4j.User;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.xml.ws.ResponseWrapper;

import java.text.SimpleDateFormat;
import java.util.*;

import static qa.qcri.aidr.manager.util.CollectionType.SMS;

@Controller
@RequestMapping("protected/collection")
public class CollectionController extends BaseController{

	private Logger logger = Logger.getLogger(CollectionController.class);
	private ErrorLog elog = new ErrorLog();

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private CollectionLogService collectionLogService;

	@Autowired
	private TaggerService taggerService;

	@Autowired
	private UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}


	@RequestMapping(value = "/save.action", method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> save(
			AidrCollection collection,
			@RequestParam(value = "runAfterCreate", defaultValue = "false", required = false)
			Boolean runAfterCreate) throws Exception {

		logger.info("Save AidrCollection to Database having code : "+collection.getCode());
		logger.info("Following users: " + collection.getFollow());
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

			if(collection.getCollectionType() == SMS){
				collection.setTrack(null);
				collection.setLangFilters(null);
				collection.setGeo(null);
				collection.setFollow(null);
			}

			collectionService.create(collection);

			//Running collection right after creation
			if (runAfterCreate) {
				return start(collection.getId());
			}

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
	public Map<String,Object> trash(@RequestParam Integer id) throws Exception {
		logger.info("In trash collection, id = " + id);
		try {
			AidrCollection collection = collectionService.findById(id);
			if (null == collection) {
				collection = collectionService.findTrashedById(id);
				if (collection != null) {
					AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
					return getUIWrapper(dto, true);
				} else {
					String msg = "Attempting to trash collection " + collection.getCode() + " failed as collection not found!";
					logger.error(msg);
					return getUIWrapper(false, msg);
				}
			}
			//Otherwise, collection exists and needs trashing
			if (collection.getStatus().equals(CollectionStatus.INITIALIZING) || 
					collection.getStatus().equals(CollectionStatus.RUNNING) || 
					collection.getStatus().equals(CollectionStatus.RUNNING_WARNING)) {
				String msg = "Attempting to trash a running collection. Collection must be in stopped state!";
				logger.error(msg);
				return getUIWrapper(false, msg);
			} else {
				// Trash collection
				logger.info("Received request to trash collection code: " + collection.getCode());
				CollectionStatus oldStatus = collection.getStatus();
				if (oldStatus.equals(CollectionStatus.STOPPED)
						|| oldStatus.equals(CollectionStatus.NOT_RUNNING)) {
					logger.info("Trashing collection having code " + collection.getCode());
					try {
						//collection = collectionService.stop(collection.getId());
						collection.setStatus(CollectionStatus.TRASHED);
						collectionService.update(collection);
						if (taggerService.trashCollection(collection) > 0) {
							AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
							logger.info("Attempting to trash collection " + collection.getCode() + " succeeded!");
							return getUIWrapper(dto, true);
						} else {
							String msg = "Attempting to trash collection " + collection.getCode() + " failed!";
							logger.error(msg);
							// restore collection status to STOPPED
							collection.setStatus(oldStatus);
							collectionService.update(collection);
							return getUIWrapper(false, msg);
						}
					} catch(Exception e) {
						String msg = "Error while trashing AIDR Collection - couldn't stop!";
						logger.error(msg, e);
						if (!collection.getStatus().equals(oldStatus)) {
							// restore collection status
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

	@RequestMapping(value = "/untrash.action", method = { RequestMethod.POST ,RequestMethod.GET })
	@ResponseBody
	public Map<String,Object> untrash( AidrCollection collection) throws Exception {
		AidrCollection trashedCollection = collectionService.findTrashedById(collection.getId());
		if (trashedCollection != null) {
			logger.info("Untrashing collection having code " + trashedCollection.getCode());
			try{
				if (taggerService.untrashCollection(trashedCollection.getCode()) > 0) {
					trashedCollection.setStatus(CollectionStatus.STOPPED);
					collectionService.update(trashedCollection);
					//AidrCollection c = collectionService.start(trashedCollection.getId());
					return getUIWrapper(true);  
				} else {
					String msg = "Attempting to untrash collection " + trashedCollection.getCode() + " failed! ";
					logger.error(msg);
					return getUIWrapper(false, msg);
				}
			}catch(Exception e){
				String msg = "Error while untrashing AIDR Collection " + trashedCollection.getCode();
				logger.error(msg);
				return getUIWrapper(false, msg);
			}
		} else {
			String msg = "Attempting to untrash collection " + collection.getCode() + " that does not exist.";
			logger.error(msg);
			return getUIWrapper(false, msg);
		}
	}

	@RequestMapping(value = "/update.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> update(AidrCollection collection) throws Exception {
		Integer collectionId = collection.getId();
		logger.info("Updating AidrCollection into Database having id " + collectionId);
		try{
			CollectionStatus status = collection.getStatus();
			AidrCollection dbCollection = collectionService.findById(collectionId);

			if (collection.getCollectionType() == SMS) {
				collection.setTrack(null);
				collection.setLangFilters(null);
				collection.setGeo(null);
				collection.setGeoR(null);
				collection.setFollow(null);
			}

			if (CollectionStatus.RUNNING_WARNING.equals(status) || CollectionStatus.RUNNING.equals(status)) {
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
				collection.setFollow(collectionService.getFollowTwitterIDs(collection.getFollow(), collection.getUser().getUserName()));
				collectionService.update(collection);

				//              start collection
				collectionService.startFetcher(collectionService.prepareFetcherRequest(collection), collection);
			} else {
				collection.setStartDate(dbCollection.getStartDate());
				collection.setUser(dbCollection.getUser());
				collection.setManagers(dbCollection.getManagers());
				collection.setCreatedDate(dbCollection.getCreatedDate());
				collection.setPubliclyListed(dbCollection.getPubliclyListed());
				collection.setFollow(collectionService.getFollowTwitterIDs(collection.getFollow(), collection.getUser().getUserName()));
				collectionService.update(collection);
			}

			// if collection type was changed and if crisis for this collection exists so we need to update crisis type
			if (dbCollection.getCrisisType() != null && collection.getCrisisType() != null){
				if (!dbCollection.getCrisisType().equals(collection.getCrisisType())){
					try {
						TaggerCrisisExist taggerCrisisExist = taggerService.isCrisesExist(collection.getCode());
						TaggerCrisis crisis = new TaggerCrisis(taggerCrisisExist.getCrisisId());
						TaggerCrisisType crisisType = new TaggerCrisisType(collection.getCrisisType());
						crisis.setCrisisType(crisisType);
						taggerService.updateCode(crisis);
					} catch (AidrException e) {
						e.printStackTrace();
					}
				}
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
		logger.info("found collection: " + collection.getCode());
		AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
		if (dto != null) {
			Integer totalCount = collectionLogService.countTotalDownloadedItemsForCollection(id);
			logger.info("returned tweet count = " + totalCount);
			if (CollectionStatus.RUNNING.equals(dto.getStatus()) || CollectionStatus.RUNNING_WARNING.equals(dto.getStatus())){
				totalCount += dto.getCount();
			}
			dto.setTotalCount(totalCount);
		}
		logger.info("returning dto: " + dto.getCode());
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
			boolean onlyTrashed = false;
			if (trashed != null && trashed.equalsIgnoreCase("yes")) {
				onlyTrashed = true;
			}
			try {
				Integer userId = userEntity.getId();
				// Call update from Fetcher and then get list with updated items
				collectionService.updateAndGetRunningCollectionStatusByUser(userId);
				count = collectionService.getCollectionsCount(userEntity, onlyTrashed);
				if (count > 0) {
					List<AidrCollection> data = collectionService.findAll(start, limit, userEntity, onlyTrashed);
					List<ValueModel> collectionCodes = new ArrayList<ValueModel>(data.size());
					for (AidrCollection collection : data) {
						AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection);
						dtoList.add(dto);
						collectionCodes.add(new ValueModel(collection.getCode()));
					}
					Map<String, Integer> collectionsClassifiers = taggerService.countCollectionsClassifiers(collectionCodes);
					for (AidrCollectionTotalDTO dto : dtoList) {
						dto.setClassifiersNumber(collectionsClassifiers.get(dto.getCode()));
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
		Map<String, Object> result = null;
		try {
			result = collectionLogService.generateCSVLink(code);
			if (result != null && result.get("url") != null) {
				return getUIWrapper(result.get("url"),true);
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			logger.error("Exception in generating CSV download link for collection: " + code);
			logger.error(elog.toStringException(e));
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
			logger.error("Exception in generating CSV TweetIDs download link for collection: " + code);
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
		dto.setPubliclyListed(collection.getPubliclyListed());
		dto.setCrisisType(collection.getCrisisType());
		dto.setCollectionType(collection.getCollectionType());

		List<UserEntity> managers = collection.getManagers();
		for (UserEntity manager : managers) {
			manager.setRoles(null);
		}
		dto.setManagers(managers);

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
			e.printStackTrace();
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
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
	}

	@RequestMapping(value = "/getTwitterUserIds.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTwitterUserID(@RequestParam(value ="userId", required=true) Integer userId, @RequestParam("userList") String userList) {
		try {
			UserEntity user = userService.getById(userId);
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
			logger.error("exception", e);
			return getUIWrapper(false, "Exception in twitter user data lookup.");
		}
	}
}
