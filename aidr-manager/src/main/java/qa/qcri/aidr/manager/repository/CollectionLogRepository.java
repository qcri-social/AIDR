package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import qa.qcri.aidr.manager.dto.CollectionLogDataResponse;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;

public interface CollectionLogRepository extends GenericRepository<AidrCollectionLog, Serializable> {

    //public Boolean exist(String code);

    public CollectionLogDataResponse getPaginatedData(Integer start, Integer limit);

    public CollectionLogDataResponse getPaginatedDataForCollection(Integer start, Integer limit, Integer collectionId);

    public Integer countTotalDownloadedItemsForCollection(Integer collectionId);
    
    public Integer countLogsStartedInInterval(Integer collectionId, Date startDate, Date endDate);

    public Map<Integer, Integer> countTotalDownloadedItemsForCollectionIds(List<Integer> ids);
}
