package qa.qcri.aidr.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.manager.dto.AidrCollectionTotalDTO;
import qa.qcri.aidr.manager.dto.TaggerCrisisType;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.service.CollectionLogService;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.TaggerService;
import qa.qcri.aidr.manager.util.CollectionStatus;
import qa.qcri.aidr.manager.util.JsonDataValidator;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("public/collection")
public class PublicController extends BaseController{
	private static Logger logger = Logger.getLogger(PublicController.class);
	private static ErrorLog elog = new ErrorLog();
	
	@Autowired
	private CollectionService collectionService;

	@Autowired
	private TaggerService taggerService;

	@Autowired
	private CollectionLogService collectionLogService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

    @RequestMapping(value = "/findByRequestCode.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>  findByRequestCode(@QueryParam("code") String code) throws Exception {
        try {
            AidrCollection data = collectionService.findByCode(code);
            return getUIWrapper(data, true);

        } catch (Exception e) {
            logger.error(elog.toStringException(e));
            return getUIWrapper(false);
        }

    }

    @RequestMapping(value = "/findAll.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  findAll(@RequestParam Integer start, @RequestParam Integer limit,  @RequestParam Enum statusValue,
			@DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
		//logger.info("public findall is called");
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 50;

		try {

			List<AidrCollection> data = collectionService.findAllForPublic(start, limit, statusValue);
			logger.info("[findAll] fetched data size: " + ((data != null) ? data.size() : 0));
			return getUIWrapper(data, true);

		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			return getUIWrapper(false);
		}

		//return getUIWrapper(false);
	}

	@RequestMapping(value = "/findAllRunning.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  findAllRunning(@RequestParam Integer start, @RequestParam Integer limit,
			@DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
		// logger.info("public findAllRunning is called");
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 50;
		Integer count = 0;
		List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();

		try {
			logger.info("*************************************************  CollectionStatus.RUNNING ****************************");
			List<AidrCollection> data = collectionService.findAllForPublic(start, limit, CollectionStatus.RUNNING);
			logger.info("data size : " + data.size());

			for (AidrCollection collection : data) {
				String taggingOutPut = taggerService.loadLatestTweetsWithCount(collection.getCode(), 1);
				//String stripped = taggingOutPut.substring(1, taggingOutPut.lastIndexOf("]"));
				logger.info("stripped taggingOutPut : " + taggingOutPut );
				if(!JsonDataValidator.isEmptySON(taggingOutPut))  {
					AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, true);
					dtoList.add(dto);
					count = count +1;
				}
			}
			logger.info("count = " + count);
			return getUIWrapper(dtoList, count.longValue());

		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			return getUIWrapper(false);
		}

	}

	@RequestMapping(value = "/findAllRunningWithNoOutput.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  findAllRunningWithNoOutput(@RequestParam Integer start, @RequestParam Integer limit,
			@DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
		// logger.info("public findAllRunning is called");
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 50;
		Integer count = 0;
		List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();

		try {
			//    logger.info("*************************************************  CollectionStatus.RUNNING ****************************");
			List<AidrCollection> data = collectionService.findAllForPublic(start, limit, CollectionStatus.RUNNING);
			//     logger.info("data size : " + data.size());
			//count = collectionService.getPublicCollectionsCount(CollectionStatus.RUNNING);
			for (AidrCollection collection : data) {
				String taggingOutPut = taggerService.loadLatestTweetsWithCount(collection.getCode(), 1);
				//System.out.println("taggingOutPut : " + taggingOutPut);
				if(JsonDataValidator.isEmptySON(taggingOutPut))  {
					AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, false);
					dtoList.add(dto);
					count = count +1;
				}
			}
			logger.info("count = " + count);
			return getUIWrapper(dtoList, count.longValue());

		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			return getUIWrapper(false);
		}

		//return getUIWrapper(false);
	}

	@RequestMapping(value = "/findAllStoped.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  findAllStop(@RequestParam Integer start, @RequestParam Integer limit,
			@DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
		// logger.info("public findAllStop is called");
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 50;
		Integer count = 0;
		List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();
		try {
			// logger.info("*************************************************  CollectionStatus.STOPPED ****************************");
			List<AidrCollection> data = collectionService.findAllForPublic(start, limit, CollectionStatus.STOPPED);
			count = collectionService.getPublicCollectionsCount(CollectionStatus.STOPPED);
			//  logger.info("data size : " + data.size());
			boolean hasTagggerOutput;
			for (AidrCollection collection : data) {
				String taggingOutPut = taggerService.loadLatestTweetsWithCount(collection.getCode(), 1);
				//System.out.println("taggingOutPut : " + taggingOutPut);
				if(JsonDataValidator.isEmptySON(taggingOutPut))  {
					hasTagggerOutput = false;
				}
				else{
					hasTagggerOutput = true;
				}

				AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, hasTagggerOutput);
				dtoList.add(dto);
			}
			logger.info("count = " + count);
			return getUIWrapper(dtoList, count.longValue());

		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			return getUIWrapper(false);
		}

		//return getUIWrapper(false);
	}

	@RequestMapping(value = "/findById.action", method = RequestMethod.GET)
	@ResponseBody
	public AidrCollectionTotalDTO findById(Integer id) throws Exception {

		AidrCollection collection = collectionService.findById(id);
		AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, false);
		if (dto != null) {
			Integer totalCount = collectionLogService.countTotalDownloadedItemsForCollection(id);
			if (CollectionStatus.RUNNING.equals(dto.getStatus()) || CollectionStatus.RUNNING_WARNING.equals(dto.getStatus())){
				totalCount += dto.getCount();
			}
			dto.setTotalCount(totalCount);
		}
		return dto;
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
				return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
			}
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
	}

	@RequestMapping(value = "/getAttributesAndLabelsByCrisisId.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getAttributesAndLabelsByCrisisId(@RequestParam Integer id) throws Exception {
		String result = "";
		try {
			result = taggerService.getAttributesAndLabelsByCrisisId(id);
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		return getUIWrapper(result,true);
	}


	@RequestMapping(value = "/loadLatestTweets.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> loadLatestTweets(@RequestParam String code, @RequestParam String constraints) throws Exception {
		String result = "";
		try {
			result = taggerService.loadLatestTweets(code, constraints);
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		return getUIWrapper(result,true);
	}


	@RequestMapping(value = "/getPublicFlagStatus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Boolean> getPublicFlagStatus() {
		List<AidrCollection> resultList;
		try {
			long startTime = System.currentTimeMillis();
			resultList = collectionService.getRunningCollections();
			System.out.println("Time to retrieve map from DB: " + (System.currentTimeMillis() - startTime));

			if (resultList != null) {
				Map<String, Boolean> runningCollections = new HashMap<String, Boolean>(resultList.size());
				for (AidrCollection c: resultList) {
					runningCollections.put(c.getCode(), c.getPubliclyListed());
				}
				logger.debug("Fetched map to send: " + runningCollections);
				return runningCollections;
			}
		} catch (Exception e) {
			logger.error("Unable to fetch list of running collections from DB");
			logger.error(elog.toStringException(e));
		}
		return null;
	}


	@RequestMapping(value = "/getChannelPublicFlagStatus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Boolean> getCollectionPublicFlagStatus(@QueryParam("channelCode") String channelCode) {
		AidrCollection collection = null;
		try {
			long startTime = System.currentTimeMillis();
			collection = collectionService.findByCode(channelCode);
			System.out.println("Time to retrieve publiclyStatus from DB: " + (System.currentTimeMillis() - startTime));

			if (collection != null) {
				Map<String, Boolean> result = new HashMap<String, Boolean>();
				result.put(channelCode, collection.getPubliclyListed());
				logger.debug("Fetched map to send: " + result);
				return result;
			}
		} catch (Exception e) {
			logger.error("Unable to fetch list of running collections from DB");
			logger.error(elog.toStringException(e));
		}
		return null;
	}


	@RequestMapping(value = "/findTotalCount", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Integer> findTotalCount(final String collectionCode) throws Exception {
		try {
			AidrCollection collection = collectionService.findByCode(collectionCode);
			AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, false);
			if (dto != null) {
				Integer totalCount = collectionLogService.countTotalDownloadedItemsForCollection(dto.getId());
				if (CollectionStatus.RUNNING.equals(dto.getStatus()) || CollectionStatus.RUNNING_WARNING.equals(dto.getStatus())){
					totalCount += dto.getCount();
				}
				dto.setTotalCount(totalCount);
			}
			Map<String, Integer> result = new HashMap<String, Integer>();
			result.put(collectionCode, dto.getTotalCount());
			return result;
		} catch (Exception e) {
			logger.error("Unable to fetch total count of downloaded documents for collection = " + collectionCode);
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	private AidrCollectionTotalDTO convertAidrCollectionToDTO(AidrCollection collection, boolean hasTaggerOutput){
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
		dto.setHasTaggerOutput(hasTaggerOutput);


		if(collection.getCrisisType() != null){
			dto.setCrisisTypeName(getCrisisTypeName(collection.getCrisisType()));
		}

		List<UserEntity> managers = collection.getManagers();
		for (UserEntity manager : managers) {
			manager.setRoles(null);
		}
		dto.setManagers(managers);

		return dto;
	}

	private String getCrisisTypeName(int typeID){
		String name = "Not specified";
		// System.out.println("getCrisisTypeName: " + typeID);
		try {
			List<TaggerCrisisType> crisisTypes = taggerService.getAllCrisisTypes();

			for (TaggerCrisisType cType : crisisTypes) {
				if(cType.getCrisisTypeID() == typeID) {
					name = cType.getName();
				}
			}

		} catch (AidrException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		return name;
	}



}
