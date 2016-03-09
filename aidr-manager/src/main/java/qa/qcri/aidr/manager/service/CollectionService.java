package qa.qcri.aidr.manager.service;

import java.util.List;

import qa.qcri.aidr.manager.dto.CollectionBriefInfo;
import qa.qcri.aidr.manager.dto.CollectionDetailsInfo;
import qa.qcri.aidr.manager.dto.CollectionSummaryInfo;
import qa.qcri.aidr.manager.dto.CollectionUpdateInfo;
import qa.qcri.aidr.manager.dto.FetcherRequestDTO;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.persistence.entities.UserAccount;
import qa.qcri.aidr.manager.util.SMS;

public interface CollectionService {

    public Integer getPublicCollectionsCount(Enum statusValue) throws Exception;

    public List<Collection> findAllForPublic(Integer start, Integer limit, Enum statusValue) throws Exception;

    public boolean updateCollection(CollectionUpdateInfo collection, Long userId) throws Exception;
    public void update(Collection collection) throws Exception;
    public void delete(Collection collection) throws Exception;

    public Collection create(CollectionDetailsInfo collectionInfo, UserAccount user) throws Exception;

    public Collection findById(Long id) throws Exception;

    public Collection findByCode(String code) throws Exception;

    public List<Collection> findAll(Integer start, Integer limit, UserAccount user, boolean onlyTrashed) throws Exception;

    public Integer getCollectionsCount(UserAccount user, boolean onlyTrashed) throws Exception;

    public List<Collection> searchByName(String query, Long userId) throws Exception;

    public Boolean exist(String code) throws Exception;

    public Boolean existName(String name) throws Exception;

    public Collection getRunningCollectionStatusByUser(Long userId) throws Exception;

    public Collection updateAndGetRunningCollectionStatusByUser(Long userId) throws Exception;

    public Collection start(Long collectionId) throws Exception;

    public Collection stop(Long collectionId, Long userId) throws Exception;

    public Collection statusById(Long collectionId, Long userId) throws Exception;

    public Collection stopAidrFetcher(Collection collection, Long userId) throws Exception;

    public Collection startFetcher(FetcherRequestDTO fetcherRequest, Collection Collection) throws Exception;

    public boolean pingCollector() throws AidrException;

    public FetcherRequestDTO prepareFetcherRequest(Collection dbCollection);

    public Collection statusByCollection(Collection collection, Long userId) throws Exception;

    public List<Collection> getRunningCollections() throws Exception;

    public List<Collection> getRunningCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) throws Exception;

    public Long getRunningCollectionsCount(String terms) throws Exception;

    public List<Collection> getStoppedCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection) throws Exception;

    public Long getStoppedCollectionsCount(String terms) throws Exception;

    public Boolean isValidToken(String token) throws Exception;

    public List<Collection> geAllCollectionByUser(Long userId) throws Exception;

    public Collection findTrashedByCode(String code) throws Exception;

    public Collection findTrashedById(Long id) throws Exception;
    
	public String getFollowTwitterIDs(String followList, String userName);

	public String getFollowTwitterScreenNames(String followList, String userName);

	public boolean enableClassifier(String code, UserAccount currentUser);

	public Boolean pushSMS(String collectionCode, SMS sms);
	
	public Boolean isValidAPIKey(String code, String apiKey) throws Exception;

	public List<CollectionSummaryInfo> getAllCollectionData();
	
	public List<CollectionBriefInfo> getMicromappersFilteredCollections(boolean micromappersEnabled);
}
