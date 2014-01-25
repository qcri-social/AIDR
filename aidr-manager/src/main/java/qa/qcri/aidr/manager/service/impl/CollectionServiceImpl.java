package qa.qcri.aidr.manager.service.impl;

import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.manager.dto.CollectionDataResponse;
import qa.qcri.aidr.manager.dto.FetcherRequestDTO;
import qa.qcri.aidr.manager.dto.FetcheResponseDTO;
import qa.qcri.aidr.manager.dto.PingResponse;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;
import qa.qcri.aidr.manager.hibernateEntities.UserConnection;
import qa.qcri.aidr.manager.repository.CollectionLogRepository;
import qa.qcri.aidr.manager.repository.CollectionRepository;
import qa.qcri.aidr.manager.repository.UserConnectionRepository;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.util.CollectionStatus;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

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
    private Client client;
    @Value("${fetchMainUrl}")
    private String fetchMainUrl;
    @Value("${twitter.consumerKey}")
    private String consumerKey;
    @Value("${twitter.consumerSecret}")
    private String consumerSecret;

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
    public void create(AidrCollection collection) throws Exception {
        collectionRepository.save(collection);
    }

    @Override
    @Transactional(readOnly = true)
    public AidrCollection findById(Integer id) throws Exception {
        return collectionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AidrCollection findByCode(String code) throws Exception {
        return collectionRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionDataResponse findAll(Integer start, Integer limit, Integer userId) throws Exception {
        return collectionRepository.getPaginatedData(start, limit, userId);
    }

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
    public AidrCollection getRunningCollectionStatusByUser(Integer userId) throws Exception {
        return collectionRepository.getRunningCollectionStatusByUser(userId);
    }

    @Override
    @Transactional(readOnly = false)
    public AidrCollection updateAndGetRunningCollectionStatusByUser(Integer userId) throws Exception {
        AidrCollection collection = collectionRepository.getRunningCollectionStatusByUser(userId);
        if (collection != null){
            logger.info("User with ID: '" + userId + "' has Running collection with code: '" + collection.getCode() + "'. Trying to update collection from fetcher." );
            return statusByCollection(collection);
        } else {
            logger.info("User with ID: '" + userId + "' don't has running collections. Nothing to update." );
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
    public AidrCollection start(Integer collectionId, Integer userId) throws Exception {
        AidrCollection alreadyRunningCollection = collectionRepository.getRunningCollectionStatusByUser(userId);
        if (alreadyRunningCollection != null) {
            this.stop(alreadyRunningCollection.getId());
        }
        AidrCollection dbCollection = collectionRepository.findById(collectionId);
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
        dto.setToFollow(dbCollection.getFollow());
        dto.setToTrack(dbCollection.getTrack());
        dto.setGeoLocation(dbCollection.getGeo());
        dto.setLanguageFilter(dbCollection.getLangFilters());
        return dto;
    }

    @Override
    @Transactional(readOnly = false)
    public AidrCollection stop(Integer collectionId) throws Exception {
        AidrCollection collection = collectionRepository.findById(collectionId);
        AidrCollection updateCollection = stopAidrFetcher(collection);

        AidrCollectionLog collectionLog = new AidrCollectionLog();
        collectionLog.setCount(collection.getCount());
        collectionLog.setEndDate(collection.getEndDate());
        collectionLog.setFollow(collection.getFollow());
        collectionLog.setGeo(collection.getGeo());
        collectionLog.setLangFilters(collection.getLangFilters());
        collectionLog.setStartDate(collection.getStartDate());
        collectionLog.setTrack(collection.getTrack());
        collectionLog.setCollectionID(collectionId);
        collectionLogRepository.save(collectionLog);

        return updateCollection;
    }

    public AidrCollection startFetcher(FetcherRequestDTO fetcherRequest, AidrCollection aidrCollection) {
        try {
            /**
             * Rest call to Fetcher
             */
            WebResource webResource = client.resource(fetchMainUrl + "/twitter/start");
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, objectMapper.writeValueAsString(fetcherRequest));
            String jsonResponse = clientResponse.getEntity(String.class);
            FetcheResponseDTO response = objectMapper.readValue(jsonResponse, FetcheResponseDTO.class);
            logger.info("start Response from fetchMain " + objectMapper.writeValueAsString(response));
            aidrCollection.setStatus(CollectionStatus.getByStatus(response.getStatusCode()));
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
            WebResource webResource = client.resource(fetchMainUrl + "/manage/ping");
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            PingResponse pingResponse = objectMapper.readValue(jsonResponse, PingResponse.class);
            if (pingResponse != null && "RUNNING".equals(pingResponse.getStatus())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new AidrException("Error while Getting training data for Crisis and Model.", e);
        }
    }

    @SuppressWarnings("deprecation")
    public AidrCollection stopAidrFetcher(AidrCollection collection) {
        try {
            /**
             * Rest call to Fetcher
             */
            WebResource webResource = client.resource(fetchMainUrl + "/twitter/stop?id=" + URLEncoder.encode(collection.getCode()));
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            String response = clientResponse.getEntity(String.class);
            logger.info(response);
            /**
             * Change Database Status
             */
            return this.collectionRepository.stop(collection.getId());
        } catch (Exception e) {
            logger.error("Error while stopping Remote FetchMain Collection", e);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AidrCollection statusById(Integer id) throws Exception {
        AidrCollection collection = this.findById(id);
        return statusByCollection(collection);
    }

    @SuppressWarnings("deprecation")
    @Override
    public AidrCollection statusByCollection(AidrCollection collection) throws Exception {
        try {
            /**
             * Make a call to fetcher Status Rest API
             */
            WebResource webResource = client.resource(fetchMainUrl + "/twitter/status?id=" + URLEncoder.encode(collection.getCode()));
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);
            logger.info("jsonResponse" + jsonResponse);
            ObjectMapper objectMapper = new ObjectMapper();
            FetcheResponseDTO response = objectMapper.readValue(jsonResponse, FetcheResponseDTO.class);
            if (response != null) {
                if (!CollectionStatus.getByStatus(response.getStatusCode()).equals(collection.getStatus())) {
                    //if local=running and fetcher=NOT-FOUND then put local as NOT-RUNNING
                    if (CollectionStatus.NOT_FOUND.equals(CollectionStatus.getByStatus(response.getStatusCode()))) {
                        collection.setStatus(CollectionStatus.NOT_RUNNING);
                        collectionRepository.update(collection);
                    }

                    if (CollectionStatus.RUNNING.equals(CollectionStatus.getByStatus(response.getStatusCode()))) {
                        collection = collectionRepository.start(collection.getId());
                    }
                }
                if ((response.getTweetsCount() != null && !response.getTweetsCount().equals(collection.getCount())) || response.getLastDocument() != null) {
                    collection.setCount(response.getTweetsCount());
                    collection.setLastDocument(response.getLastDocument());
                    collectionRepository.update(collection);
                }
            }
            return collection;
        } catch (Exception e) {
            String msg = "Error while getting status for collection from Remote FetchMain Collection";
            logger.error(msg, e);
            throw new Exception(msg);
        }
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

}
