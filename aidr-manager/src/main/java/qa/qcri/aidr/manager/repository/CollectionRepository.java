package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.List;

import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserAccount;

public interface CollectionRepository extends GenericRepository<AidrCollection, Serializable> {
    public Integer getPublicCollectionsCount(final Enum statusValue);
    public List<AidrCollection> getPaginatedDataForPublic( Integer start,  Integer limit, Enum statusValue);

    public List<AidrCollection> searchByName(String query, Long userId) throws Exception;

    public List<AidrCollection> getPaginatedData(Integer start, Integer limit, UserAccount user, boolean onlyTrashed);

    public Integer getCollectionsCount(UserAccount user, boolean onlyTrashed);

    public Boolean exist(String code);

    public Boolean existName(String name);

    public AidrCollection getRunningCollectionStatusByUser(Long userId);

    public List<AidrCollection> getRunningCollections();

    public List<AidrCollection> getRunningCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection);

    public Long getRunningCollectionsCount(String terms);

    public List<AidrCollection> getStoppedCollections(Integer start, Integer limit, String terms, String sortColumn, String sortDirection);

    public Long getStoppedCollectionsCount(String terms);

    public AidrCollection getInitializingCollectionStatusByUser(Long userId);

    public AidrCollection start(Integer collectionId);

    public AidrCollection stop(Integer collectionId);

    public AidrCollection findByCode(String code);
    
    public AidrCollection trashCollectionById(Integer collectionId);

    public List<AidrCollection> getAllCollectionByUser(Long userId);

}
