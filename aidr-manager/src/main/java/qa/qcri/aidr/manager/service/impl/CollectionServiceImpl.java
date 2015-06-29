package qa.qcri.aidr.manager.service.impl;

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
import qa.qcri.aidr.manager.dto.FetcheResponseDTO;
import qa.qcri.aidr.manager.dto.FetcherRequestDTO;
import qa.qcri.aidr.manager.dto.PingResponse;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;
import qa.qcri.aidr.manager.hibernateEntities.UserConnection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.repository.AuthenticateTokenRepository;
import qa.qcri.aidr.manager.repository.CollectionLogRepository;
import qa.qcri.aidr.manager.repository.CollectionRepository;
import qa.qcri.aidr.manager.repository.UserConnectionRepository;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.util.CollectionStatus;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;



import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static qa.qcri.aidr.manager.util.CollectionType.SMS;
import static qa.qcri.aidr.manager.util.CollectionType.Twitter;

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
	private AuthenticateTokenRepository authenticateTokenRepository;

	//@Autowired	// gf 3 way
	//private Client client;
	//private Client client = ClientBuilder.newClient();
	@Value("${fetchMainUrl}")
	private String fetchMainUrl;
	@Value("${twitter.consumerKey}")
	private String consumerKey;
	@Value("${twitter.consumerSecret}")
	private String consumerSecret;

	private String accessTokenStr = null;
	private String accessTokenSecretStr = null;

	@Override
	@Transactional(readOnly = false)
	public void update(AidrCollection collection) throws Exception {
		collectionRepository.update(collection);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(AidrCollection collection) throws Exception {
		collectionRepository.delete(collection);

	}

	@Override
	@Transactional
	public void create(AidrCollection collection) throws Exception {
		collectionRepository.save(collection);
	}

	//  this method is common to get collection and should not filter by status
	@Override
	@Transactional(readOnly = true)
	public AidrCollection findById(Integer id) throws Exception {
		return collectionRepository.findById(id);
	}

	//  this method is common to get collection and should not filter by status
	@Override
	@Transactional(readOnly = true)
	public AidrCollection findByCode(String code) throws Exception {
		return collectionRepository.findByCode(code);
	}

	@Override
	@Transactional(readOnly = true)
	public AidrCollection findTrashedById(Integer id) throws Exception {
		AidrCollection temp = collectionRepository.findById(id);
		if (temp.getStatus().equals(CollectionStatus.TRASHED)) {
			return temp;
		}
		return null;

	}

	@Override
	@Transactional(readOnly = true)
	public AidrCollection findTrashedByCode(String code) throws Exception {
		AidrCollection temp = collectionRepository.findByCode(code);
		if (temp.getStatus().equals(CollectionStatus.TRASHED)) {
			return temp;
		}
		return null;

	}

	@Override
	@Transactional(readOnly = true)
	public List<AidrCollection> findAll(Integer start, Integer limit, UserEntity user, boolean onlyTrashed) throws Exception {
		return collectionRepository.getPaginatedData(start, limit, user, onlyTrashed);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AidrCollection> findAllForPublic(Integer start, Integer limit, Enum statusValue) throws Exception {
		//logger.info("statusValue: " + statusValue);
		return collectionRepository.getPaginatedDataForPublic(start, limit, statusValue);
	}


	//    @Override
	//    @Transactional(readOnly = true)
	//    public CollectionDataResponse findAll(Integer start, Integer limit, Integer userId) throws Exception {
	//        return collectionRepository.getPaginatedData(start, limit, userId);
	//    }

	@Override
	@Transactional(readOnly = true)
	public List<AidrCollection> searchByName(String query, Integer userId) throws Exception {
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
	public AidrCollection getRunningCollectionStatusByUser(Integer userId) throws Exception {
		return collectionRepository.getRunningCollectionStatusByUser(userId);
	}

	@Override
	@Transactional(readOnly = false)
	public AidrCollection updateAndGetRunningCollectionStatusByUser(Integer userId) throws Exception {
		AidrCollection collection = collectionRepository.getRunningCollectionStatusByUser(userId);
		if (collection != null){
			logger.info("User with ID: '" + userId + "' has a running collection with code: '" + collection.getCode() + "'. Trying to update collection from fetcher." );
			return statusByCollection(collection);
		} else {
			logger.info("User with ID: '" + userId + "' don't have any running collections. Nothing to update." );
			//            If there is no running collection there is still can be collection with status 'Initializing'.
			//            This happens because we update collection information from fetcher before collection was started.
			//            So we need to update from Fetcher this kind of collections as well.
			collection = collectionRepository.getInitializingCollectionStatusByUser(userId);
			if (collection != null) {
				return statusByCollection(collection);
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public AidrCollection start(Integer collectionId) throws Exception {

		// We are going to start new collection. Lets stop collection which is running for owner of the new collection.
		AidrCollection dbCollection = collectionRepository.findById(collectionId);
		Integer userId = dbCollection.getUser().getId();
		AidrCollection alreadyRunningCollection = collectionRepository.getRunningCollectionStatusByUser(userId);
		if (alreadyRunningCollection != null) {
			this.stop(alreadyRunningCollection.getId());
		}

		return startFetcher(prepareFetcherRequest(dbCollection), dbCollection);
	}

	@Transactional(readOnly = true)
	public FetcherRequestDTO prepareFetcherRequest(AidrCollection dbCollection) {
		FetcherRequestDTO dto = new FetcherRequestDTO();

		UserConnection userconnection = userConnectionRepository.fetchbyUsername(dbCollection.getUser().getUserName());
		dto.setAccessToken(userconnection.getAccessToken());
		dto.setAccessTokenSecret(userconnection.getSecret());
		dto.setConsumerKey(consumerKey);
		dto.setConsumerSecret(consumerSecret);
		dto.setCollectionName(dbCollection.getName());
		dto.setCollectionCode(dbCollection.getCode());
		dto.setToFollow(getFollowTwitterIDs(dbCollection.getFollow(), dbCollection.getUser().getUserName()));
		dto.setToFollow(dbCollection.getFollow());
		dto.setToTrack(dbCollection.getTrack());
		dto.setGeoLocation(dbCollection.getGeo());
		dto.setGeoR(dbCollection.getGeoR());
		dto.setLanguageFilter(dbCollection.getLangFilters());

		// Added by koushik
		accessTokenStr = dto.getAccessToken();
		accessTokenSecretStr = dto.getAccessTokenSecret();

		return dto;
	}

	@Override
	@Transactional(readOnly = false)
	public AidrCollection stop(Integer collectionId) throws Exception {
		AidrCollection collection = collectionRepository.findById(collectionId);
		
		// Follwoing 2 lines added by koushik for downloadCount bug
		AidrCollection c = this.statusByCollection(collection);
		collection.setCount(c.getCount());
		
		AidrCollection updateCollection = stopAidrFetcher(collection);

		AidrCollectionLog collectionLog = new AidrCollectionLog(collection);
		collectionLogRepository.save(collectionLog);

		return updateCollection;
	}
	
	//MEGHNA: method for stopping a collection on FATAL_ERROR
	//separate method from stop needed to prevent looping in
	//updateStatusCollection() method
	public AidrCollection stopFatalError(Integer collectionId) throws Exception {
		AidrCollection collection = collectionRepository.findById(collectionId);
		collection = collectionRepository.stop(collection.getId());
		
		AidrCollection updateCollection = stopAidrFetcher(collection);

		AidrCollectionLog collectionLog = new AidrCollectionLog(collection);
		collectionLogRepository.save(collectionLog);		

		return updateCollection;
	}

	public AidrCollection startFetcher(FetcherRequestDTO fetcherRequest, AidrCollection aidrCollection) {
		try {
			/**
			 * Rest call to Fetcher
			 */
			Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

			if (aidrCollection.getCollectionType() == Twitter) {
				WebTarget webResource = client.target(fetchMainUrl + "/twitter/start");

				System.out.println("In startFetcher...");
				ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();

				Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
						.post(Entity.json(objectMapper.writeValueAsString(fetcherRequest)), Response.class);

				System.out.println("ObjectMapper: " + objectMapper.writeValueAsString(fetcherRequest));
				//System.out.println("Response = " + clientResponse);

				String jsonResponse = clientResponse.readEntity(String.class);

				logger.info("NEW STRING: " + jsonResponse);
				FetcheResponseDTO response = objectMapper.readValue(jsonResponse, FetcheResponseDTO.class);
				logger.info("start Response from fetchMain " + objectMapper.writeValueAsString(response));
				aidrCollection.setStatus(CollectionStatus.getByStatus(response.getStatusCode()));
			} else if (aidrCollection.getCollectionType() == SMS){
				WebTarget webResource = client.target(fetchMainUrl + "/sms/start?collection_code=" + URLEncoder.encode(aidrCollection.getCode(), "UTF-8"));
				Response response = webResource.request(MediaType.APPLICATION_JSON).get();
				if (response.getStatus() == 200)
					aidrCollection.setStatus(CollectionStatus.RUNNING);
			}
			/**
			 * Update Status To database
			 */
			collectionRepository.update(aidrCollection);
			return aidrCollection;
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
			throw new AidrException("Error while Getting training data for Crisis and Model.", e);
		}
	}

	//@SuppressWarnings("deprecation")
	public AidrCollection stopAidrFetcher(AidrCollection collection) {
		try {
			/**
			 * Rest call to Fetcher
			 */
			Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
			String path = "";
			if (collection.getCollectionType() == Twitter) {
				path = "/twitter/stop?id=";
			} else if(collection.getCollectionType() == SMS){
				path = "/sms/stop?collection_code=";
			}

			WebTarget webResource = client.target(fetchMainUrl + path + URLEncoder.encode(collection.getCode(), "UTF-8"));

			Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			collection = updateStatusCollection(jsonResponse, collection);

			/**
			 * Change Database Status
			 */
			return this.collectionRepository.stop(collection.getId());
		} catch (Exception e) {
			logger.error("Error while stopping Remote FetchMain Collection", e);
		}
		return null;
	}

	private AidrCollection updateStatusCollection(String jsonResponse, AidrCollection collection) throws Exception {
		ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
		FetcheResponseDTO response = objectMapper.readValue(jsonResponse, FetcheResponseDTO.class);
		if (response != null) {
			//MEGHNA: moved setting collection count to top of the method
			//to avoid individual status blocks setting collection count below
			if (response.getCollectionCount() != null && !response.getCollectionCount().equals(collection.getCount())) {
				collection.setCount(response.getCollectionCount());		// Commented by koushik for downloadCount bug
				//MEGHNA: uncommented - setting count at the beginning always makes this condition false 
				//so lastdocument never set 
				String lastDocument = response.getLastDocument();
				if (lastDocument != null)
					collection.setLastDocument(lastDocument);
				collectionRepository.update(collection);
			}
			
			if (!CollectionStatus.getByStatus(response.getStatusCode()).equals(collection.getStatus())) {
				
				collection.setStatus(CollectionStatus.getByStatus(response.getStatusCode()));
				
				logger.debug("Collection Status: " + CollectionStatus.getByStatus(response.getStatusCode()));
				
				switch(CollectionStatus.getByStatus(response.getStatusCode()))
				{				
				case NOT_FOUND:
				case STOPPED:
					collection.setStatus(CollectionStatus.NOT_RUNNING);					
				case RUNNING_WARNING:
				case WARNING:
					collectionRepository.update(collection);
					break;
				case RUNNING:
					collection = collectionRepository.start(collection.getId());
					break;
				case FATAL_ERROR:					
					//collection = collectionRepository.stop(collection.getId());						
					this.stopFatalError(collection.getId());
					break;
				default:
					break;
				}
				
				//if local=running and fetcher=NOT-FOUND then put local as NOT-RUNNING
				/*
				if (CollectionStatus.NOT_FOUND.equals(CollectionStatus.getByStatus(response.getStatusCode()))) {
					//collection.setCount(response.getCollectionCount());
					collection.setStatus(CollectionStatus.NOT_RUNNING);
					collectionRepository.update(collection);
				}
				
				if (CollectionStatus.STOPPED.equals(CollectionStatus.getByStatus(response.getStatusCode()))) {
					//collection.setCount(response.getCollectionCount());
					collection.setStatus(CollectionStatus.STOPPED);
					collectionRepository.update(collection);
				}

				if (CollectionStatus.RUNNING.equals(CollectionStatus.getByStatus(response.getStatusCode()))) {
					//collection.setCount(response.getCollectionCount());
					collection = collectionRepository.start(collection.getId());
				}
				
				if (CollectionStatus.FATAL_ERROR.equals(CollectionStatus.getByStatus(response.getStatusCode()))) {
					collection.setStatus(CollectionStatus.FATAL_ERROR);
					collection = collectionRepository.stop(collection.getId());						
					this.stopFatalError(collection.getId());
					}
					*/
			}
		}
		return collection;
	}

	//@SuppressWarnings("deprecation")
	@Override
	public AidrCollection statusById(Integer id) throws Exception {
		AidrCollection collection = this.findById(id);
		return statusByCollection(collection);
	}

	//@SuppressWarnings("deprecation")
	@Override
	public AidrCollection statusByCollection(AidrCollection collection) throws Exception {
		if (collection != null) {
			try {
				/**
				 * Make a call to fetcher Status Rest API
				 */
				Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

				String path = "";
				if (collection.getCollectionType() == Twitter) {
					path = "/twitter/status?id=";
				} else if(collection.getCollectionType() == SMS){
					path = "/sms/status?collection_code=";
				}

				WebTarget webResource = client.target(fetchMainUrl + path + URLEncoder.encode(collection.getCode(), "UTF-8"));
				Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();

				String jsonResponse = clientResponse.readEntity(String.class);
				//System.out.println("**********Collector response : " + jsonResponse);

				collection = updateStatusCollection(jsonResponse, collection);
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
	public List<AidrCollection> getRunningCollections() throws Exception {
		return collectionRepository.getRunningCollections();
	}

	@Override
	@Transactional(readOnly = true)
	public List<AidrCollection> getRunningCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) throws Exception {
		return collectionRepository.getRunningCollections(start, limit, terms, sortColumn, sortDirection);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getRunningCollectionsCount(String terms) throws Exception {
		return collectionRepository.getRunningCollectionsCount(terms);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AidrCollection> getStoppedCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) throws Exception {
		return collectionRepository.getStoppedCollections(start, limit, terms, sortColumn, sortDirection);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getStoppedCollectionsCount(String terms) throws Exception {
		return collectionRepository.getStoppedCollectionsCount(terms);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getCollectionsCount(UserEntity user, boolean onlyTrashed) throws Exception {
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
	public List<AidrCollection> geAllCollectionByUser(Integer userId) throws Exception{
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
					System.out.println("Received string: " + followList + ", Split follow string: " + userList);
					for (String user: userList) {
						System.out.println("Looking at follow data: " + user);
						if (StringUtils.isNumeric(user.trim())) {
							try {
								userIdList[j] = Long.parseLong(user.trim());
								System.out.println("Going to fetch twitter userData for the following twitterID: " + userIdList[j]);
								++j;
							} catch (Exception ex) {
								logger.error("Exception in parsing string to number: ", ex);
							}		
						} else {
							userNameList[i] = user.trim();
							System.out.println("Going to fetch twitter userData for the following screen name: " + userNameList[i]);
							++i;
						}
					}
					userNameList = ArrayUtils.subarray(userNameList, 0, i);
					userIdList = ArrayUtils.subarray(userIdList, 0, j);
				} catch (Exception e) {
					e.printStackTrace();
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
				System.out.println("Created follow twitterID list: " + followIDs.toString());
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
					System.out.println("Received string: " + followList + ", Split follow string: " + userList);
					for (String user: userList) {
						System.out.println("Looking at follow data: " + user);
						if (StringUtils.isNumeric(user.trim())) {
							try {
								userIdList[j] = Long.parseLong(user.trim());
								System.out.println("Going to fetch twitter userData for the following twitterID: " + userIdList[j]);
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
				for (User u: dataList) {
					followScreenNames.append(u.getScreenName()).append(",");
				}
				followScreenNames.deleteCharAt(followScreenNames.lastIndexOf(","));
				System.out.println("Created follow twitterID list: " + followScreenNames.toString());
				return followScreenNames.toString();		
			}
			else {
				return null;
			}

		}
		return null;
	}


	private List<User> getUserDataFromScreenName(String[] userNameList, String userName)	{		
		if (userNameList != null) {
			try {
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(consumerKey, consumerSecret);
				AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);
				twitter.setOAuthAccessToken(accessToken);

				ResponseList<User> list = twitter.lookupUsers(userNameList);
				System.out.println("Successfully looked up in Twitter by screen name, size of list: " + list.size());
				return (list != null ? list : new ArrayList<User>());
			} catch (Exception e) {
				e.printStackTrace();
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
				System.out.println("Successfully looked up in Twitter by ID, size of list: " + list.size());
				return (list != null ? list : new ArrayList<User>());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<User>();
	}
}
