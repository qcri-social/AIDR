package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import qa.qcri.aidr.manager.dto.CollectionLogDataResponse;
import qa.qcri.aidr.manager.persistence.entities.CollectionLog;

public interface CollectionLogRepository extends GenericRepository<CollectionLog, Serializable> {

    //public Boolean exist(String code);

    public CollectionLogDataResponse getPaginatedData(Integer start, Integer limit);

    public CollectionLogDataResponse getPaginatedDataForCollection(Integer start, Integer limit, Long collectionId);

    public Integer countTotalDownloadedItemsForCollection(Long collectionId);
    
    public Integer countLogsStartedInInterval(Long collectionId, Date startDate, Date endDate);

    public Map<Integer, Integer> countTotalDownloadedItemsForCollectionIds(List<Long> ids);
    
    @Override
	public void save(CollectionLog collectionLog);

    Long countTotalTweets();
}
