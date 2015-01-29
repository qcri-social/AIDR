package qa.qcri.aidr.manager.service;

import qa.qcri.aidr.manager.dto.FetcherRequestDTO;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;

import java.util.List;

public interface CollectionService {

    public Integer getPublicCollectionsCount(Enum statusValue) throws Exception;

    public List<AidrCollection> findAllForPublic(Integer start, Integer limit, Enum statusValue) throws Exception;

    public void update(AidrCollection collection) throws Exception;

    public void delete(AidrCollection collection) throws Exception;

    public void create(AidrCollection collection) throws Exception;

    public AidrCollection findById(Integer id) throws Exception;

    public AidrCollection findByCode(String code) throws Exception;

    public List<AidrCollection> findAll(Integer start, Integer limit, UserEntity user, boolean onlyTrashed) throws Exception;

    public Integer getCollectionsCount(UserEntity user, boolean onlyTrashed) throws Exception;

    public List<AidrCollection> searchByName(String query, Integer userId) throws Exception;

    public Boolean exist(String code) throws Exception;

    public Boolean existName(String name) throws Exception;

    public AidrCollection getRunningCollectionStatusByUser(Integer userId) throws Exception;

    public AidrCollection updateAndGetRunningCollectionStatusByUser(Integer userId) throws Exception;

    public AidrCollection start(Integer collectionId) throws Exception;

    public AidrCollection stop(Integer collectionId) throws Exception;

    public AidrCollection statusById(Integer collectionId) throws Exception;

    public AidrCollection stopAidrFetcher(AidrCollection collection) throws Exception;

    public AidrCollection startFetcher(FetcherRequestDTO fetcherRequest, AidrCollection aidrCollection) throws Exception;

    public boolean pingCollector() throws AidrException;

    public FetcherRequestDTO prepareFetcherRequest(AidrCollection dbCollection);

    public AidrCollection statusByCollection(AidrCollection collection) throws Exception;

    public List<AidrCollection> getRunningCollections() throws Exception;

    public List<AidrCollection> getRunningCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) throws Exception;

    public Long getRunningCollectionsCount(String terms) throws Exception;

    public List<AidrCollection> getStoppedCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) throws Exception;

    public Long getStoppedCollectionsCount(String terms) throws Exception;

    public Boolean isValidToken(String token) throws Exception;

    public List<AidrCollection> geAllCollectionByUser(Integer userId) throws Exception;

    public AidrCollection findTrashedByCode(String code) throws Exception;

    public AidrCollection findTrashedById(Integer id) throws Exception;
    
	public String getFollowTwitterIDs(String followList, String userName);

}
