package qa.qcri.aidr.manager.service.impl;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.common.code.JacksonWrapper;
import qa.qcri.aidr.manager.dto.CollectionBriefInfo;
import qa.qcri.aidr.manager.dto.CollectionDetailsInfo;
import qa.qcri.aidr.manager.dto.CollectionSummaryInfo;
import qa.qcri.aidr.manager.dto.CollectionUpdateInfo;
import qa.qcri.aidr.manager.dto.FetcheResponseDTO;
import qa.qcri.aidr.manager.dto.FetcherRequestDTO;
import qa.qcri.aidr.manager.dto.PingResponse;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.CollectionLog;
import qa.qcri.aidr.manager.persistence.entities.CrisisType;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.persistence.entities.UserConnection;
import qa.qcri.aidr.manager.repository.AuthenticateTokenRepository;
import qa.qcri.aidr.manager.repository.CollectionLogRepository;
import qa.qcri.aidr.manager.repository.CollectionRepository;
import qa.qcri.aidr.manager.repository.UserConnectionRepository;
import qa.qcri.aidr.manager.service.CollectionCollaboratorService;
import qa.qcri.aidr.manager.service.CollectionLogService;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.CrisisTypeService;
import qa.qcri.aidr.manager.service.TaggerService;
import qa.qcri.aidr.manager.service.WordDictionaryService;
import qa.qcri.aidr.manager.util.CollectionStatus;
import qa.qcri.aidr.manager.util.CollectionType;
import qa.qcri.aidr.manager.util.SMS;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

@Service("collectionService")
public class CollectionServiceImpl implements CollectionService {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private CollectionRepository collectionRepository;
	@Autowired
	private CollectionLogRepository collectionLogRepository;
	@Autowired
	private UserConnectionRepository userConnectionRepository;

	@Autowired
	private TaggerService taggerService;
	
	@Autowired
	private AuthenticateTokenRepository authenticateTokenRepository;

	@Autowired
	private CollectionCollaboratorService collaboratorService;
	
	@Autowired
	private CrisisTypeService crisisTypeService;

	@Autowired
	private WordDictionaryService wordService;
	
	@Value("${fetchMainUrl}")
	private String fetchMainUrl;
	@Value("${twitter.consumerKey}")
	private String consumerKey;
	@Value("${twitter.consumerSecret}")
	private String consumerSecret;

	private String accessTokenStr = null;
	private String accessTokenSecretStr = null;

	@Autowired
	private CollectionLogService collectionLogService;
	
	@Override
	@Transactional(readOnly=false)
	public void update(Collection collection) {
		collectionRepository.update(collection);
	}
	
	@Override
	@Transactional(readOnly=false)
	public boolean updateCollection(CollectionUpdateInfo collectionUpdateInfo, Long userId) {
		
		String filteredTrack = "";
		try {
			Collection collection = findByCode(collectionUpdateInfo.getCode());
			
			if(!collection.getName().equals(collectionUpdateInfo.getName())) {
				if(!existName(collectionUpdateInfo.getName())) {
					collection.setName(collectionUpdateInfo.getName());
				} else {
					return false;
				}
			}
			// if collection exists with same name
			
			collection.setProvider(CollectionType.valueOf(collectionUpdateInfo.getProvider()));
			collection.setFollow(collectionUpdateInfo.getFollow());
			filteredTrack = collectionUpdateInfo.getTrack();
			
			if(!StringUtils.isEmpty(filteredTrack)) {
				filteredTrack = getFilteredTrack(filteredTrack);
				
				if(StringUtils.isEmpty(filteredTrack)) {
					return false;
				}
			}
			collection.setTrack(filteredTrack);
			collection.setGeo(collectionUpdateInfo.getGeo());
			collection.setGeoR(collectionUpdateInfo.getGeoR());
			collection.setDurationHours(Integer.parseInt(collectionUpdateInfo.getDurationHours()));
			collection.setLangFilters(collectionUpdateInfo.getLangFilters());
			
			Long crisisTypeId = Long.parseLong(collectionUpdateInfo.getCrisisType());
			CrisisType crisisType = crisisTypeService.getById(crisisTypeId);
			if(crisisType!=null){
				collection.setCrisisType(crisisType);
			}
			else{
				logger.error("Crisis Type Id: "+crisisTypeId +" does not exist. Can't update the collection : " + collectionUpdateInfo.getCode());
				return false;
			}
			
			if (CollectionType.SMS.equals(collection.getProvider())) {
				collection.setTrack(null);
				collection.setLangFilters(null);
				collection.setGeo(null);
				collection.setGeoR(null);
				collection.setFollow(null);
			}
			
			collection.setFollow(this.getFollowTwitterIDs(collectionUpdateInfo.getFollow(), collection.getOwner().getUserName()));
			collectionRepository.update(collection);
			// first make an entry in log if collection is running
			if (CollectionStatus.RUNNING_WARNING.equals(collection.getStatus()) || CollectionStatus.RUNNING.equals(collection.getStatus())) {
				
				this.stop(collection.getId(), userId);
				this.startFetcher(this.prepareFetcherRequest(collection), collection);
			}
			return true;
		} catch (Exception e) {
			
			logger.error("Unable to update the collection : " + collectionUpdateInfo.getCode(), e);
			return false;
		}
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(Collection collection) throws Exception {
		collectionRepository.delete(collection);

	}

	@Override
	@Transactional
	public Collection create(CollectionDetailsInfo collectionDetailsInfo, UserAccount user) throws Exception {
		
		String filteredTrack = collectionDetailsInfo.getTrack();
		
		if(!StringUtils.isEmpty(filteredTrack)) {
			filteredTrack = getFilteredTrack(filteredTrack);
			
			if(StringUtils.isEmpty(filteredTrack)) {
				return null;
			}
		}
		
		Collection collection = adaptCollectionDetailsInfoToCollection(collectionDetailsInfo, user);
		collection.setTrack(filteredTrack);
		try {
			collectionRepository.save(collection);
			collaboratorService.addCollaboratorToCollection(collectionDetailsInfo.getCode(), user.getId());
			return collection;
		} catch (Exception e) {
			
			logger.error("Error in creating collection.", e);
			return null;
		}
		
	}

	//  this method is common to get collection and should not filter by status
	@Override
	@Transactional(readOnly = true)
	public Collection findById(Long id) throws Exception {
		return collectionRepository.findById(id);
	}

	//  this method is common to get collection and should not filter by status
	@Override
	@Transactional(readOnly = true)
	public Collection findByCode(String code) throws Exception {
		return collectionRepository.findByCode(code);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection findTrashedById(Long id) throws Exception {
		Collection temp = collectionRepository.findById(id);
		if (temp.getStatus().equals(CollectionStatus.TRASHED)) {
			return temp;
		}
		return null;

	}

	@Override
	@Transactional(readOnly = true)
	public Collection findTrashedByCode(String code) throws Exception {
		Collection temp = collectionRepository.findByCode(code);
		if (temp.getStatus().equals(CollectionStatus.TRASHED)) {
			return temp;
		}
		return null;

	}

	@Override
	@Transactional(readOnly = true)
	public List<Collection> findAll(Integer start, Integer limit, UserAccount user, boolean onlyTrashed) throws Exception {
		return collectionRepository.getPaginatedData(start, limit, user, onlyTrashed);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Collection> findAllForPublic(Integer start, Integer limit, Enum statusValue) throws Exception {
		return collectionRepository.getPaginatedDataForPublic(start, limit, statusValue);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Collection> searchByName(String query, Long userId) throws Exception {
		return collectionRepository.searchByName(query, userId);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean exist(String code) throws Exception {
		return collectionRepository.exist(code);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean existName(String name) throws Exception {
		return collectionRepository.existName(name);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection getRunningCollectionStatusByUser(Long userId) throws Exception {
		return collectionRepository.getRunningCollectionStatusByUser(userId);
	}

	@Override
	@Transactional(readOnly = false)
	public Collection updateAndGetRunningCollectionStatusByUser(Long userId) throws Exception {
		Collection collection = collectionRepository.getRunningCollectionStatusByUser(userId);
		if (collection != null){
			logger.info("User with ID: '" + userId + "' has a running collection with code: '" + collection.getCode());
			return statusByCollection(collection, userId);
		} else {
			//logger.info("User with ID: '" + userId + "' don't have any running collections. Nothing to update." );
			//            If there is no running collection there is still can be collection with status 'Initializing'.
			//            This happens because we update collection information from fetcher before collection was started.
			//            So we need to update from Fetcher this kind of collections as well.
			collection = collectionRepository.getInitializingCollectionStatusByUser(userId);
			if (collection != null) {
				return statusByCollection(collection, userId);
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public Collection start(Long collectionId) throws Exception {

		// We are going to start new collection. Lets stop collection which is running for owner of the new collection.
		Collection dbCollection = collectionRepository.findById(collectionId);
		Long userId = dbCollection.getOwner().getId();
		Collection alreadyRunningCollection = collectionRepository.getRunningCollectionStatusByUser(userId);
		if (alreadyRunningCollection != null) {
			this.stop(alreadyRunningCollection.getId(), userId);
		}

		return startFetcher(prepareFetcherRequest(dbCollection), dbCollection);
	}

	@Override
	@Transactional(readOnly = true)
	public FetcherRequestDTO prepareFetcherRequest(Collection dbCollection) {
		FetcherRequestDTO dto = new FetcherRequestDTO();

		UserConnection userconnection = userConnectionRepository.fetchbyUsername(dbCollection.getOwner().getUserName());
		dto.setAccessToken(userconnection.getAccessToken());
		dto.setAccessTokenSecret(userconnection.getSecret());
		dto.setConsumerKey(consumerKey);
		dto.setConsumerSecret(consumerSecret);
		dto.setCollectionName(dbCollection.getName());
		dto.setCollectionCode(dbCollection.getCode());

		dto.setToFollow(getFollowTwitterIDs(dbCollection.getFollow(), dbCollection.getOwner().getUserName()));
		dto.setToFollow(dbCollection.getFollow());
		dto.setToTrack(dbCollection.getTrack());
		dto.setGeoLocation(dbCollection.getGeo());
		dto.setGeoR(dbCollection.getGeoR());
		dto.setLanguageFilter(dbCollection.getLangFilters());
		dto.setSaveMediaEnabled(dbCollection.isSaveMediaEnabled());
		
		// Added by koushik
		accessTokenStr = dto.getAccessToken();
		accessTokenSecretStr = dto.getAccessTokenSecret();

		return dto;
	}

	@Override
	@Transactional(readOnly = false)
	public Collection stop(Long collectionId, Long userId) throws Exception {
		Collection collection = collectionRepository.findById(collectionId);
		
		// Follwoing 2 lines added by koushik for downloadCount bug
		Collection c = this.statusByCollection(collection, userId);
		collection.setCount(c.getCount());
		
		Collection updateCollection = stopAidrFetcher(collection, userId);

		CollectionLog collectionLog = new CollectionLog(collection);
		collectionLog.setUpdatedBy(userId);
		collectionLogRepository.save(collectionLog);

		return updateCollection;
	}
	
	//MEGHNA: method for stopping a collection on FATAL_ERROR
	//separate method from stop needed to prevent looping in
	//updateStatusCollection() method
	public Collection stopFatalError(Long collectionId, Long userId) throws Exception {
		Collection collection = collectionRepository.findById(collectionId);
		//collection = collectionRepository.stop(collection.getId());
		
		Collection updateCollection = stopAidrFetcher(collection, userId);

		CollectionLog collectionLog = new CollectionLog(collection);
		collectionLogRepository.save(collectionLog);		

		return updateCollection;
	}

	@Override
	@Transactional(readOnly = false)
	public Collection startFetcher(FetcherRequestDTO fetcherRequest, Collection collection) {
		try {
			/**
			 * Rest call to Fetcher
			 */
			Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

			if (CollectionType.Twitter.equals(collection.getProvider())) {
				WebTarget webResource = client.target(fetchMainUrl + "/twitter/start");

				ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();

				Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
						.post(Entity.json(objectMapper.writeValueAsString(fetcherRequest)), Response.class);

				//logger.info("ObjectMapper: " + objectMapper.writeValueAsString(fetcherRequest));
				String jsonResponse = clientResponse.readEntity(String.class);
				//logger.info("NEW STRING: " + jsonResponse);
				FetcheResponseDTO response = objectMapper.readValue(jsonResponse, FetcheResponseDTO.class);
				logger.info("start Response from fetchMain " + objectMapper.writeValueAsString(response));
				collection.setStatus(CollectionStatus.getByStatus(response.getStatusCode()));
			} else if (CollectionType.SMS.equals(collection.getProvider())) {
				WebTarget webResource = client.target(fetchMainUrl + "/sms/start?collection_code=" + URLEncoder.encode(collection.getCode(), "UTF-8"));
				Response response = webResource.request(MediaType.APPLICATION_JSON).get();
				if (response.getStatus() == 200)
					collection.setStatus(CollectionStatus.INITIALIZING);
			}
			/**
			 * Update Status To database
			 */
			collectionRepository.update(collection);
			return collection;
		} catch (Exception e) {
			logger.error("Error while starting Remote FetchMain Collection", e);
		}
		return null;
	}

	@Override
	public boolean pingCollector() throws AidrException {
		try {
			Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
			WebTarget webResource = client.target(fetchMainUrl + "/manage/ping");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			PingResponse pingResponse = objectMapper.readValue(jsonResponse, PingResponse.class);
			if (pingResponse != null && "RUNNING".equals(pingResponse.getCurrentStatus())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new AidrException("Error while pinging the collector.", e);
		}
	}

	//@SuppressWarnings("deprecation")
	@Override
	@Transactional(readOnly = false)
	public Collection stopAidrFetcher(Collection collection, Long userId) {
		try {
			/**
			 * Rest call to Fetcher
			 */
			Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
			String path = "";
			if (CollectionType.Twitter.equals(collection.getProvider())) {
				path = "/twitter/stop?id=";
			} else if(CollectionType.SMS.equals(collection.getProvider())) {
				path = "/sms/stop?collection_code=";
			}

			WebTarget webResource = client.target(fetchMainUrl + path + URLEncoder.encode(collection.getCode(), "UTF-8"));

			Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			collection = updateStatusCollection(jsonResponse, collection, userId);

			/**
			 * Change Database Status
			 */
			return this.collectionRepository.stop(collection.getId());
		} catch (Exception e) {
			logger.error("Error while stopping Remote FetchMain Collection", e);
		}
		return null;
	}

	private Collection updateStatusCollection(String jsonResponse, Collection collection, Long accountId) throws Exception {
		ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
		FetcheResponseDTO response = objectMapper.readValue(jsonResponse, FetcheResponseDTO.class);
		if (response != null) {
			//MEGHNA: moved setting collection count to top of the method
			//to avoid individual status blocks setting collection count below
			if (response.getCollectionCount() != null && !response.getCollectionCount().equals(collection.getCount())) {
				collection.setCount(response.getCollectionCount());
				String lastDocument = response.getLastDocument();
				if (lastDocument != null)
					collection.setLastDocument(lastDocument);
				collectionRepository.update(collection);
			}
			collection.setSourceOutage(response.isSourceOutage());
			if (!CollectionStatus.getByStatus(response.getStatusCode()).equals(collection.getStatus())) {
				
				CollectionStatus prevStatus =  collection.getStatus();
				collection.setStatus(CollectionStatus.getByStatus(response.getStatusCode()));
				
				switch(CollectionStatus.getByStatus(response.getStatusCode()))
				{				
				case NOT_FOUND:
				//case STOPPED:
					collection.setStatus(CollectionStatus.NOT_RUNNING);
					
					//Add collectionCount in collectionLog if it was not recorded. 
					if (collection.getStartDate() != null && ((collection.getEndDate() != null 
							&& collection.getStartDate().after(collection.getEndDate())) || collection.getEndDate() == null)) {
						if(collectionLogRepository.countLogsStartedInInterval(collection.getId(), collection.getStartDate(), new Date())==0){
							CollectionLog collectionLog = new CollectionLog(collection);
							collectionLog.setEndDate(new Date());
							collectionLog.setUpdatedBy(accountId);
							collectionLogRepository.save(collectionLog);
						}
					}
				case RUNNING_WARNING:
					if(prevStatus == CollectionStatus.INITIALIZING)
					{
						collection = collectionRepository.start(collection.getId());
						break;
					}
				case WARNING:
					collectionRepository.update(collection);
					break;
				case RUNNING:
					collection = collectionRepository.start(collection.getId());
					break;
				case FATAL_ERROR:					
					//collection = collectionRepository.stop(collection.getId());
					logger.warn("Fatal error, stopping collection " + collection.getId()); 
					if(prevStatus != CollectionStatus.FATAL_ERROR || prevStatus != CollectionStatus.NOT_RUNNING || prevStatus != CollectionStatus.STOPPED)
						this.stopFatalError(collection.getId(), accountId);
					break;
				default:
					break;
				}
			}
		}
		return collection;
	}

	//@SuppressWarnings("deprecation")
	@Transactional
	@Override
	public Collection statusById(Long id, Long userId) throws Exception {
		Collection collection = this.findById(id);
		return statusByCollection(collection, userId);
	}

	//@SuppressWarnings("deprecation")
	@Transactional
	@Override
	public Collection statusByCollection(Collection collection, Long accountId) throws Exception {
		if (collection != null) {
			try {
				/**
				 * Make a call to fetcher Status Rest API
				 */
				Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

				String path = "";
				if (CollectionType.Twitter.equals(collection.getProvider())) {
					path = "/twitter/status?id=";
				} else if(CollectionType.SMS.equals(collection.getProvider())) {
					path = "/sms/status?collection_code=";
				}

				WebTarget webResource = client.target(fetchMainUrl + path + URLEncoder.encode(collection.getCode(), "UTF-8"));
				Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();

				String jsonResponse = clientResponse.readEntity(String.class);
				collection = updateStatusCollection(jsonResponse, collection, accountId);
				return collection;
			} catch (Exception e) {
				String msg = "Error while getting status for collection from Remote FetchMain Collection";
				logger.error(msg, e);
				throw new Exception(msg);
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Collection> getRunningCollections() throws Exception {
		return collectionRepository.getRunningCollections();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Collection> getRunningCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) throws Exception {
		return collectionRepository.getRunningCollections(start, limit, terms, sortColumn, sortDirection);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getRunningCollectionsCount(String terms) throws Exception {
		return collectionRepository.getRunningCollectionsCount(terms);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Collection> getStoppedCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) throws Exception {
		return collectionRepository.getStoppedCollections(start, limit, terms, sortColumn, sortDirection);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getStoppedCollectionsCount(String terms) throws Exception {
		return collectionRepository.getStoppedCollectionsCount(terms);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getCollectionsCount(UserAccount user, boolean onlyTrashed) throws Exception {
		return collectionRepository.getCollectionsCount(user, onlyTrashed);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getPublicCollectionsCount(Enum statusValue) throws Exception {
		return collectionRepository.getPublicCollectionsCount(statusValue);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean isValidToken(String token) throws Exception {
		return authenticateTokenRepository.isAuthorized(token);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Collection> geAllCollectionByUser(Long userId) throws Exception{
		return collectionRepository.getAllCollectionByUser(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public String getFollowTwitterIDs(String followList, String userName) {
		if (followList != null && !followList.isEmpty()) {
			List<String> userList = Arrays.asList(followList.split(","));

			if (null == accessTokenStr || null == accessTokenSecretStr) {
				UserConnection userConnection = userConnectionRepository.fetchbyUsername(userName);
				accessTokenStr = userConnection.getAccessToken();
				accessTokenSecretStr = userConnection.getSecret();
			}

			String[] userNameList = null;
			long[] userIdList = null;
			if (userList != null) {
				try {
					userNameList = new String[userList.size()];
					userIdList = new long[userList.size()];
					int i = 0;
					int j = 0;
					logger.info("Received string: " + followList + ", Split follow string: " + userList);
					for (String user: userList) {
						//logger.info("Looking at follow data: " + user);
						if (StringUtils.isNumeric(user.trim())) {
							try {
								userIdList[j] = Long.parseLong(user.trim());
								//logger.info("Going to fetch twitter userData for the following twitterID: " + userIdList[j]);
								++j;
							} catch (Exception ex) {
								logger.error("Exception in parsing string to number: ", ex);
							}		
						} else {
							userNameList[i] = user.trim();
							//logger.info("Going to fetch twitter userData for the following screen name: " + userNameList[i]);
							++i;
						}
					}
					userNameList = ArrayUtils.subarray(userNameList, 0, i);
					userIdList = ArrayUtils.subarray(userIdList, 0, j);
				} catch (Exception e) {
					logger.error("Exception while getting follow twitter Ids",e);
				}
			}
			List<User> dataList = new ArrayList<User>();
			if (userNameList != null && userNameList.length > 0) {
				dataList.addAll(getUserDataFromScreenName(userNameList, userName));
			}
			if (userIdList != null && userIdList.length > 0) {
				dataList.addAll(getUserDataFromTwitterID(userIdList, userName));
			}
			if (!dataList.isEmpty()) {
				StringBuffer followIDs = new StringBuffer();
				for (User u: dataList) {
					followIDs.append(u.getId()).append(",");
				}
				followIDs.deleteCharAt(followIDs.lastIndexOf(","));
				//logger.info("Created follow twitterID list: " + followIDs.toString());
				return followIDs.toString();		
			}
			else {
				return null;
			}

		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public String getFollowTwitterScreenNames(String followList, String userName) {
		if (followList != null && !followList.isEmpty()) {
			List<String> userList = Arrays.asList(followList.split(","));

			if (null == accessTokenStr || null == accessTokenSecretStr) {
				UserConnection userConnection = userConnectionRepository.fetchbyUsername(userName);
				accessTokenStr = userConnection.getAccessToken();
				accessTokenSecretStr = userConnection.getSecret();
			}

			long[] userIdList = null;
			if (userList != null) {
				try {
					userIdList = new long[userList.size()];
					int j = 0;
					logger.info("Received string: " + followList + ", Split follow string: " + userList);
					for (String user: userList) {
						//logger.info("Looking at follow data: " + user);
						if (StringUtils.isNumeric(user.trim())) {
							try {
								userIdList[j] = Long.parseLong(user.trim());
								//logger.info("Going to fetch twitter userData for the following twitterID: " + userIdList[j]);
								++j;
							} catch (Exception ex) {
								logger.error("Exception in parsing string to number: ", ex);
							}		
						} 
					}
					userIdList = ArrayUtils.subarray(userIdList, 0, j);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<User> dataList = new ArrayList<User>();
			if (userIdList != null && userIdList.length > 0) {
				dataList.addAll(getUserDataFromTwitterID(userIdList, userName));
			}

			if (!dataList.isEmpty()) {
				StringBuffer followScreenNames = new StringBuffer();
				for (User user: dataList) {
					followScreenNames.append(user.getScreenName()).append(",");
				}
				followScreenNames.deleteCharAt(followScreenNames.lastIndexOf(","));
				//logger.info("Created follow twitterID list: " + followScreenNames.toString());
				return followScreenNames.toString();		
			}
			else {
				return null;
			}

		}
		return null;
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean enableClassifier(String code, UserAccount currentUser) {
		
		try {
			Collection collection = findByCode(code);
			if(collection != null) {
				collection.setClassifierEnabled(Boolean.TRUE);
				collection.setClassifierEnabledBy(currentUser);
				
				this.update(collection);
			}
			
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.error("Error in enabling classifier for code : " + code, e);
			return Boolean.FALSE;
		}
	}


	@Override
	@Transactional(readOnly = true)
	public Boolean isValidAPIKey(String code, String apiKey)
			throws Exception {
		Collection collection = findByCode(code);
		if(collection.getOwner().getApiKey().equals(apiKey)){
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean pushSMS(String collectionCode, SMS sms) {
		try {
			/**
			 * Rest call to Fetcher
			 */
			Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

				WebTarget webResource = client.target(fetchMainUrl + "/sms/endpoint/receive/"+ URLEncoder.encode(collectionCode, "UTF-8"));

				ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();

				Response response = webResource.request(MediaType.APPLICATION_JSON)
						.post(Entity.json(objectMapper.writeValueAsString(sms)), Response.class);
				if(response.getStatus()!=200){
					return false;
				}
				else{
					return true;
				}
				
		} catch (Exception e) {
			logger.error("Exception while pushing sms", e);
			return false;
		}
		
	}

	@Override
	public List<CollectionSummaryInfo> getAllCollectionData() {
		List<CollectionSummaryInfo> collectionSummaryInfos = new ArrayList<CollectionSummaryInfo>();
		
		List<Collection> collections = collectionRepository.getAllCollections();
		if(collections != null) {
			collectionSummaryInfos = adaptCollectionListToCollectionSummaryInfoList(collections);
		}
		
		return collectionSummaryInfos;
	}
	

	@Override
	public List<CollectionBriefInfo> getMicromappersFilteredCollections(
			boolean micromappersEnabled) {
		
		List<Collection> collections = collectionRepository.findMicromappersFilteredCollections(micromappersEnabled);
		List<CollectionBriefInfo> briefInfos = new ArrayList<CollectionBriefInfo>();
		
		if(collections != null && collections.size() > 0) {
			briefInfos = adaptCollectionListToCollectionBriefInfoList(collections);
		}
		
		return briefInfos;
	}
	
	private List<User> getUserDataFromScreenName(String[] userNameList, String userName)	{		
		if (userNameList != null) {
			try {
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(consumerKey, consumerSecret);
				AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);
				twitter.setOAuthAccessToken(accessToken);

				ResponseList<User> list = twitter.lookupUsers(userNameList);
				logger.info("Successfully looked up in Twitter by screen name, size of list: " + list.size());
				return (list != null ? list : new ArrayList<User>());
			} catch (Exception e) {
				logger.error("Exception while getting user Data from screen Name for user: "+userName,e);
			}
		}
		return new ArrayList<User>();
	}

	private List<User> getUserDataFromTwitterID(long[] userIdList, String userName)	{		
		if (userIdList != null) {
			try {
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(consumerKey, consumerSecret);
				AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);
				twitter.setOAuthAccessToken(accessToken);

				ResponseList<User> list = twitter.lookupUsers(userIdList);
				logger.info("Successfully looked up in Twitter by ID, size of list: " + list.size());
				return (list != null ? list : new ArrayList<User>());
			} catch (Exception e) {
				logger.error("Exception while getting user Data from TwitterId for user: "+userName,e);
			}
		}
		return new ArrayList<User>();
	}
	
	private Collection adaptCollectionDetailsInfoToCollection(CollectionDetailsInfo collectionInfo, UserAccount user) {
		
		Collection collection = new Collection();
		collection.setDurationHours(collectionInfo.getDurationHours());
		collection.setCode(collectionInfo.getCode());
		collection.setName(collectionInfo.getName());
		collection.setClassifierEnabled(false);
		collection.setProvider(CollectionType.valueOf(collectionInfo.getProvider()));
		collection.setOwner(user);
		collection.setStatus(CollectionStatus.NOT_RUNNING);
		collection.setPubliclyListed(true); 	// TODO: change default behavior to user choice

		collection.setGeoR(collectionInfo.getGeoR());
		collection.setGeo(collectionInfo.getGeo());
		collection.setTrack(collectionInfo.getTrack());
		collection.setCrisisType(crisisTypeService.getById(collectionInfo.getCrisisType()));
		collection.setFollow(collection.getFollow());
		collection.setLangFilters(collectionInfo.getLangFilters());
		collection.setMicromappersEnabled(Boolean.FALSE);
		collection.setProvider(CollectionType.valueOf(collectionInfo.getProvider()));
		collection.setPurpose(collectionInfo.getPurpose());
		
		if(CollectionType.SMS.equals(collectionInfo.getProvider())) {
			collection.setTrack(null);
			collection.setLangFilters(null);
			collection.setGeo(null);
			collection.setFollow(null);
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		collection.setCreatedAt(now);
		collection.setUpdatedAt(now);
		return collection;
	}
	
    private List<CollectionSummaryInfo> adaptCollectionListToCollectionSummaryInfoList(List<Collection> collections) {
    	
    	List<CollectionSummaryInfo> collectionSummaryInfos = new ArrayList<CollectionSummaryInfo>();
    	
    	for(Collection collection : collections) {
    		collectionSummaryInfos.add(adaptCollectionToCollectionSummaryInfo(collection));
    	}

    	return collectionSummaryInfos;
    }
    
    private CollectionSummaryInfo adaptCollectionToCollectionSummaryInfo(Collection collection) {
    	
    	CollectionSummaryInfo summaryInfo = new CollectionSummaryInfo();
    	summaryInfo.setCode(collection.getCode());
    	summaryInfo.setName(collection.getName());
    	summaryInfo.setCurator(collection.getOwner().getUserName());
    	summaryInfo.setStartDate(collection.getStartDate());
    	summaryInfo.setEndDate(collection.getEndDate());
    	summaryInfo.setCollectionCreationDate(collection.getCreatedAt());
    	// TODO to fetch from collection log
    	try {
			summaryInfo.setTotalCount(collectionLogService.countTotalDownloadedItemsForCollection(collection.getId()));
		} catch (Exception e) {
			logger.warn("Error in fetch count from collection log.", e);
			summaryInfo.setTotalCount(collection.getCount());
		}
    	summaryInfo.setStatus(collection.getStatus().getStatus());
    	
    	// TODO summaryInfo.setCreatedAt(collection.getCreatedAt());
    	summaryInfo.setLanguage(collection.getLangFilters());
    	summaryInfo.setKeywords(collection.getTrack());
    	summaryInfo.setGeo(collection.getGeo());
    	summaryInfo.setLabelCount(taggerService.getLabelCount(collection.getId()));
    	summaryInfo.setPubliclyListed(collection.isPubliclyListed());
    	
    	return summaryInfo;
    }

    private List<CollectionBriefInfo> adaptCollectionListToCollectionBriefInfoList(List<Collection> collections) {
    	
    	List<CollectionBriefInfo> collectionBriefInfos = new ArrayList<CollectionBriefInfo>();
    	
    	for(Collection collection : collections) {
    		collectionBriefInfos.add(adaptCollectionToCollectionBriefInfo(collection));
    	}

    	return collectionBriefInfos;
    }
    
    private CollectionBriefInfo adaptCollectionToCollectionBriefInfo(Collection collection) {
    	
    	CollectionBriefInfo briefInfo = new CollectionBriefInfo();
    	briefInfo.setCollectionId(collection.getId());
    	briefInfo.setCollectionCode(collection.getCode());
    	briefInfo.setCollectionName(collection.getName());
    	briefInfo.setMicromappersEnabled(collection.isMicromappersEnabled());
    	briefInfo.setProvider(collection.getProvider().name());
    	return briefInfo;
    }
    
    private String getFilteredTrack(String trackToFilter) {
    	
    	String filteredTrack = "";
    	
    	//trackToFilter = trackToFilter.replaceAll(", ", ",");
    	String[] trackArray = trackToFilter.split(",\\s*");
    	List<String> toFilter = new ArrayList<String>();
    	toFilter.addAll(Arrays.asList(trackArray));
    	
    	List<String> stopWordList = wordService.fetchAllStopWords();
    	
    	toFilter.removeAll(stopWordList);
    	
    	if(toFilter != null && !toFilter.isEmpty()) {
    		trackArray = toFilter.toArray(new String[] {});
        	filteredTrack = Arrays.toString(trackArray);
        	filteredTrack = filteredTrack.substring(1, filteredTrack.length()-1);
    	}
    	
    	return filteredTrack.trim();
    	
    }

}
