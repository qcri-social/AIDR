package qa.qcri.aidr.manager.repository;

import qa.qcri.aidr.manager.dto.CollectionLogDataResponse;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface CollectionLogRepository extends GenericRepository<AidrCollectionLog, Serializable> {

    //public Boolean exist(String code);

    public CollectionLogDataResponse getPaginatedData(Integer start, Integer limit);

    public CollectionLogDataResponse getPaginatedDataForCollection(Integer start, Integer limit, Integer collectionId);

    public Integer countTotalDownloadedItemsForCollection(Integer collectionId);

    public Map<Integer, Integer> countTotalDownloadedItemsForCollectionIds(List<Integer> ids);
}
