/**
 * 
 */
package qa.qcri.aidr.data.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.data.persistence.entity.CollectionSummary;
import qa.qcri.aidr.data.repository.CollectionSummaryRepository;

/**
 * @author Latika
 *
 */
@Service
public class CollectionSummaryDao {

	@Autowired
	private CollectionSummaryRepository collectionSummaryRepository;
	
	public List<CollectionSummary> getAllCollections() {
		List<CollectionSummary> collectionSummaries = collectionSummaryRepository.findAllByOrderByCollectionCreationDateDesc();
		return collectionSummaries;
	}
	
	public void saveUpdateCollectionSummary(CollectionSummary summary){
		collectionSummaryRepository.save(summary);
	}
	
	public void saveUpdateCollectionSummaryList(List<CollectionSummary> summaryList){
		collectionSummaryRepository.save(summaryList);
	}
	
	public CollectionSummary getByCode(String code) {
		return collectionSummaryRepository.findByCode(code);
	}
}
