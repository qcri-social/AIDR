package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.List;

import qa.qcri.aidr.manager.dto.CollectionDataResponse;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;

public interface CollectionRepository extends GenericRepository<AidrCollection, Serializable> {

	public List<AidrCollection> searchByName(String query,Integer userId) throws Exception ;
	public CollectionDataResponse getPaginatedData(Integer start ,Integer limit,Integer userId);
	public Boolean exist(String code);
	public AidrCollection getRunningCollectionStatusByUser(Integer userId);
	public List<AidrCollection> getRunningCollections();
	public AidrCollection getInitializingCollectionStatusByUser(Integer userId);
	public AidrCollection start(Integer collectionId);
	public AidrCollection stop(Integer collectionId);
	public AidrCollection findByCode(String code);
}
