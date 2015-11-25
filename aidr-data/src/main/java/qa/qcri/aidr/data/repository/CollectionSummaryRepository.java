package qa.qcri.aidr.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import qa.qcri.aidr.data.persistence.entity.CollectionSummary;

public interface CollectionSummaryRepository extends CrudRepository<CollectionSummary, Long>{

	public static final String FETCH_ALL_ZERO_TWEET_COLLECTION_ORDER_BY_COLLECTION_CREATION_DATE = 
			"SELECT col FROM CollectionSummary col WHERE col.totalCount > 0 ORDER BY col.collectionCreationDate DESC";
	
    List<CollectionSummary> findByName(String name);
    
    @Query(FETCH_ALL_ZERO_TWEET_COLLECTION_ORDER_BY_COLLECTION_CREATION_DATE)
    List<CollectionSummary> getCollectionsOrderByCollectionCreationDate();
    
    CollectionSummary findByCode(String code);
}
