package qa.qcri.aidr.manager.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.manager.dto.AidrCollectionTotalDTO;
import qa.qcri.aidr.manager.dto.CollectionBriefInfo;
import qa.qcri.aidr.manager.dto.CollectionDetailsInfo;
import qa.qcri.aidr.manager.dto.CollectionSummaryInfo;
import qa.qcri.aidr.manager.dto.TaggerCrisisType;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.service.CollectionCollaboratorService;
import qa.qcri.aidr.manager.service.CollectionLogService;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.CrisisTypeService;
import qa.qcri.aidr.manager.service.TaggerService;
import qa.qcri.aidr.manager.util.CollectionStatus;
import qa.qcri.aidr.manager.util.GeoRestrictionType;
import qa.qcri.aidr.manager.util.JsonDataValidator;
import qa.qcri.aidr.manager.util.SMS;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("public/collection")
public class PublicController extends BaseController{
	private static Logger logger = Logger.getLogger(PublicController.class);
	
	@Autowired
	private CollectionService collectionService;

	@Autowired
	private TaggerService taggerService;

	@Autowired
	private CollectionLogService collectionLogService;

	@Autowired
	private CrisisTypeService crisisTypeService;
	
	@Autowired 
	private CollectionCollaboratorService collaboratorService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

    @RequestMapping(value = "/updateGeo.action", method = RequestMethod.POST)
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseBody
    public Map<String,Object> update(@RequestBody final String jsonCollection ) throws Exception {
        logger.info("updateGeo.action JSON:" + jsonCollection);

        if(jsonCollection == null){

            return getUIWrapper(false);
        }
        if(!JsonDataValidator.isValidEMSCJson(jsonCollection)){

            return getUIWrapper(false);
        }

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonCollection);

        JSONObject jsonObject = (JSONObject) obj;

        String geoString = (String)jsonObject.get("geo");
        long collectionId = (Long)jsonObject.get("id");
        long durationInHours = (Long)jsonObject.get("durationInHours");
        Boolean updateDuration = (Boolean)jsonObject.get("updateDuration");

        if(updateDuration){
            String token = (String)jsonObject.get("token");
            if(!collectionService.isValidToken(token)){
                logger.error("authentication failed : token - " + token);
                return getUIWrapper(false);
            }
        }

        try{
            //logger.info("try:" + geoString) ;
            Collection dbCollection = collectionService.findById(collectionId);
            if(dbCollection != null) {
	            CollectionStatus status = dbCollection.getStatus();
	
	            if (CollectionStatus.RUNNING_WARNING.equals(status) || CollectionStatus.RUNNING.equals(status) || CollectionStatus.INITIALIZING.equals(status)) {
	            	collectionService.stop(dbCollection.getId(), 1L);
	            }
	
	            dbCollection.setGeo(geoString);
	            dbCollection.setGeoR(GeoRestrictionType.STRICT.name().toLowerCase());
	            dbCollection.setDurationHours((int)durationInHours);
	            collectionService.update(dbCollection);
	
	            if(!geoString.isEmpty() && geoString != null) {
	                // status
	                collectionService.startFetcher(collectionService.prepareFetcherRequest(dbCollection), dbCollection);
	            }
	            return getUIWrapper(true);
            }

        }catch(Exception e){
            logger.error(String.format("Exception while Updating Collection : "+jsonCollection, e));
        }
        
        return getUIWrapper(false);
    }

    @RequestMapping(value = "/findByRequestCode.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object>  findByRequestCode(@QueryParam("code") String code) throws Exception {
        try {
        	//logger.info("Finding collection by code: "+code);
            Collection data = collectionService.findByCode(code);
            return getUIWrapper(data, true);

        } catch (Exception e) {
        	logger.error("Exception while finding collection by code: "+code, e);
            return getUIWrapper(false);
        }

    }

    @RequestMapping(value = "/findAll.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  findAll(@RequestParam Integer start, @RequestParam Integer limit,  @RequestParam Enum statusValue,
			@DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 50;

		try {

			List<Collection> data = collectionService.findAllForPublic(start, limit, statusValue);
			logger.info("[findAll] fetched data size: " + ((data != null) ? data.size() : 0));
			return getUIWrapper(data, true);

		} catch (Exception e) {
			logger.error("Error in find All collection for public",e);
			return getUIWrapper(false);
		}

		//return getUIWrapper(false);
	}


	@RequestMapping(value = "/create", method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> createCollection(CollectionDetailsInfo collectionDetailsInfo,
			@RequestParam(value = "runAfterCreate", defaultValue = "false", required = false)
			Boolean runAfterCreate) throws Exception {

		
		logger.info("Save Collection to Database having code : "+ collectionDetailsInfo.getCode());
		
		
		//logger.info("Following users: " + collection.getFollow());
		try{
			UserAccount user = getAuthenticatedUser();
			Collection collection = collectionService.create(collectionDetailsInfo, user);
			
			return getUIWrapper(true);  
		}catch(Exception e){
			logger.error("Error while saving Collection Info to database", e);
			return getUIWrapper(false); 
		}
	}
	

	@RequestMapping(value = "/findAllRunning.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  findAllRunning(@RequestParam Integer start, @RequestParam Integer limit,
			@DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 50;
		Integer count = 0;
		List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();

		try {
			//logger.info("*************************************************  CollectionStatus.RUNNING ****************************");
			List<Collection> data = collectionService.findAllForPublic(start, limit, CollectionStatus.RUNNING);
			//logger.info("data size : " + data.size());

			for (Collection collection : data) {
				String taggingOutPut = taggerService.loadLatestTweetsWithCount(collection.getCode(), 1);
				//String stripped = taggingOutPut.substring(1, taggingOutPut.lastIndexOf("]"));
				//logger.info("stripped taggingOutPut : " + taggingOutPut );
				if(!JsonDataValidator.isEmptySON(taggingOutPut))  {
					AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, true);
					dtoList.add(dto);
					count = count +1;
				}
			}
			//logger.info("count = " + count);
			return getUIWrapper(dtoList, count.longValue());

		} catch (Exception e) {
			logger.error("Error in find All Running collection for public",e);
			return getUIWrapper(false);
		}

	}

	@RequestMapping(value = "/findAllRunningWithNoOutput.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  findAllRunningWithNoOutput(@RequestParam Integer start, @RequestParam Integer limit,
			@DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 50;
		Integer count = 0;
		List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();

		try {
			List<Collection> data = collectionService.findAllForPublic(start, limit, CollectionStatus.RUNNING);
			//count = collectionService.getPublicCollectionsCount(CollectionStatus.RUNNING);
			for (Collection collection : data) {
				String taggingOutPut = taggerService.loadLatestTweetsWithCount(collection.getCode(), 1);
				if(JsonDataValidator.isEmptySON(taggingOutPut))  {
					AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, false);
					dtoList.add(dto);
					count = count +1;
				}
			}
			//logger.info("count = " + count);
			return getUIWrapper(dtoList, count.longValue());

		} catch (Exception e) {
			logger.error("Error in find All Running collection With No Output for public",e);
			return getUIWrapper(false);
		}

		//return getUIWrapper(false);
	}

	@RequestMapping(value = "/findAllStoped.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object>  findAllStop(@RequestParam Integer start, @RequestParam Integer limit,
			@DefaultValue("no") @QueryParam("trashed") String trashed) throws Exception {
		start = (start != null) ? start : 0;
		limit = (limit != null) ? limit : 50;
		Integer count = 0;
		List<AidrCollectionTotalDTO> dtoList = new ArrayList<AidrCollectionTotalDTO>();
		try {
			List<Collection> data = collectionService.findAllForPublic(start, limit, CollectionStatus.STOPPED);
			count = collectionService.getPublicCollectionsCount(CollectionStatus.STOPPED);
			boolean hasTagggerOutput;
			for (Collection collection : data) {
				String taggingOutPut = taggerService.loadLatestTweetsWithCount(collection.getCode(), 1);
				if(JsonDataValidator.isEmptySON(taggingOutPut))  {
					hasTagggerOutput = false;
				}
				else{
					hasTagggerOutput = true;
				}

				AidrCollectionTotalDTO dto = convertAidrCollectionToDTO(collection, hasTagggerOutput);
				dtoList.add(dto);
			}
			//logger.info("count = " + count);
			return getUIWrapper(dtoList, count.longValue());

		} catch (Exception e) {
			logger.error("Error in finding All stopped collections",e);
			return getUIWrapper(false);
		}
	}

	@RequestMapping(value = "/findById.action", method = RequestMethod.GET)
	@ResponseBody
	public AidrCollectionTotalDTO findById(Long id) throws Exception {

		Collection collection = collectionService.findById(id);
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
			logger.error("Error in generateTweetIdsLink for collection : " + code, e);
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
			logger.error("Error while getting attributes and labels for crisis: " + id, e);
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
			logger.error("Error while loading latest tweets for collection : " + code + " and constraints : " + constraints, e);
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		return getUIWrapper(result,true);
	}


	@RequestMapping(value = "/getPublicFlagStatus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Boolean> getPublicFlagStatus() {
		List<Collection> resultList;
		try {
			//long startTime = System.currentTimeMillis();
			resultList = collectionService.getRunningCollections();
			if (resultList != null) {
				Map<String, Boolean> runningCollections = new HashMap<String, Boolean>(resultList.size());
				for (Collection c: resultList) {
					runningCollections.put(c.getCode(), c.isPubliclyListed());
				}
				//logger.debug("Fetched map to send: " + runningCollections);
				return runningCollections;
			}
		} catch (Exception e) {
			logger.error("Unable to fetch list of running collections from DB for public", e);
		}
		return null;
	}


	@RequestMapping(value = "/getChannelPublicFlagStatus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Boolean> getCollectionPublicFlagStatus(@QueryParam("channelCode") String channelCode) {
		Collection collection = null;
		try {
			//long startTime = System.currentTimeMillis();
			collection = collectionService.findByCode(channelCode);
			//System.out.println("Time to retrieve publiclyStatus from DB: " + (System.currentTimeMillis() - startTime));

			if (collection != null) {
				Map<String, Boolean> result = new HashMap<String, Boolean>();
				result.put(channelCode, collection.isPubliclyListed());
				//logger.debug("Fetched map to send: " + result);
				return result;
			}
		} catch (Exception e) {
			logger.error("Unable to fetch running status for collection: "+channelCode,e);
		}
		return null;
	}


	@RequestMapping(value = "/findTotalCount", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Integer> findTotalCount(final String collectionCode) throws Exception {
		try {
			Collection collection = collectionService.findByCode(collectionCode);
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
			logger.error("Unable to fetch total count of downloaded documents for collection = " + collectionCode, e);
		}
		return null;
	}

	
	private AidrCollectionTotalDTO convertAidrCollectionToDTO(Collection collection, boolean hasTaggerOutput){
		if (collection == null){
			return null;
		}

		AidrCollectionTotalDTO dto = new AidrCollectionTotalDTO();

		dto.setId(collection.getId());
		dto.setCode(collection.getCode());
		dto.setName(collection.getName());
		//dto.setTarget(collection.getTarget());

		UserAccount user = collection.getOwner();
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
		dto.setCreatedDate(collection.getCreatedAt());
		dto.setLastDocument(collection.getLastDocument());
		dto.setDurationHours(collection.getDurationHours());
		dto.setPubliclyListed(collection.isPubliclyListed());
		dto.setCrisisType(collection.getCrisisType());
		dto.setHasTaggerOutput(hasTaggerOutput);
        dto.setCollectionType(collection.getProvider());
		dto.setCrisisType(collection.getCrisisType());
		
		List<UserAccount> managers = collaboratorService.fetchCollaboratorsByCollection(collection.getId());
		dto.setManagers(managers);

		return dto;
	}

	private String getCrisisTypeName(int typeID){
		String name = "Not specified";
		try {
			List<TaggerCrisisType> crisisTypes = taggerService.getAllCrisisTypes();

			for (TaggerCrisisType cType : crisisTypes) {
				if(cType.getCrisisTypeID() == typeID) {
					name = cType.getName();
				}
			}

		} catch (AidrException e) {
			logger.error("Error while fetching all crisisTypes for public",e);
		}

		return name;
	}

	@RequestMapping(value = "/stopAllRunningCollection", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> stopAllRunningCollections(@RequestBody final String jsonString) throws Exception {
		if(jsonString == null){
			return getUIWrapper(false);
		}
		
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
		
		String token = (String)jsonObject.get("token");
		
		if(collectionService.isValidToken(token)){
			List<Collection> runningCollections = collectionService.getRunningCollections();
			
			List<Collection> stoppedCollections = new ArrayList<Collection>();
			
			for (Collection aidrCollection : runningCollections) {
				stoppedCollections.add(collectionService.stop(aidrCollection.getId(), 1L));
			}
			return getUIWrapper(stoppedCollections, true);
		}
		else{
			return getUIWrapper("Authentication Failed", false);
		}
	}

	
	@RequestMapping(value = "/startCollections", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> startMultipleCollections(@RequestBody String jsonString) throws Exception {
		if(jsonString == null){
			return getUIWrapper(false);
		}
		
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
		
		String token = (String)jsonObject.get("token");
		List<Collection> collections = new ArrayList<Collection>();
		
		if(collectionService.isValidToken(token)){
			ObjectMapper mapper = new ObjectMapper();
			collections = Arrays.asList(mapper.readValue(jsonObject.get("collections").toString(), Collection[].class));
			List<String> startedCollections = new ArrayList<String>();

			for (Collection collection : collections) {
				collection = collectionService.findByCode(collection.getCode());
				if (!collection.getStatus().equals(CollectionStatus.TRASHED)) {
					collection = collectionService.start(collection.getId());
					startedCollections.add(collection.getCode());
				}
			} 
			return getUIWrapper(startedCollections, true);
		}
		else{
			return getUIWrapper("Authentication Failed", false);
		}
	}
	
	@RequestMapping(value = "/sms/push/{collectionCode}", method = RequestMethod.POST)
	@ResponseBody
    public Map<String,Object> receive(@PathVariable(value = "collectionCode") String collectionCode, @RequestHeader("apiKey") String apiKey, @RequestBody SMS sms) throws Exception {
		
		if(collectionService.isValidAPIKey(collectionCode, apiKey)){
			Boolean response = collectionService.pushSMS(collectionCode, sms);
			if(response){
				return getUIWrapper("Successfully posted SMS", true);
						//Response.ok("Successfully posted SMS").build();
			}
			else{
				return getUIWrapper("Exception while posting SMS", false); //Response.ok("Exception while posting SMS").build();
			}
		}
		else{
			return getUIWrapper("API Key is not valid", false); //Response.ok("API Key is not valid").build();
		}
	}
	
	@RequestMapping(value = "/all")
	@ResponseBody
	public List<CollectionSummaryInfo> getCollections() {
		List<CollectionSummaryInfo> summaryInfos = new ArrayList<CollectionSummaryInfo>();
		try {
			summaryInfos =  collectionService.getAllCollectionData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("error", e);
			return Collections.EMPTY_LIST;
		}
		
		return summaryInfos;
	}
	
	@RequestMapping(value = "/list")
	@ResponseBody
	public List<CollectionBriefInfo> getCollectionsBriefInfo(@RequestParam(defaultValue="false") Boolean micromappersEnabled) {
		List<CollectionBriefInfo> briefInfos = new ArrayList<CollectionBriefInfo>();
		try {
			briefInfos = collectionService.getMicromappersFilteredCollections(micromappersEnabled);
		} catch (Exception e) {
			logger.error("Error is fetching collection list.", e);
		}
		
		return briefInfos;
	}
	
}
