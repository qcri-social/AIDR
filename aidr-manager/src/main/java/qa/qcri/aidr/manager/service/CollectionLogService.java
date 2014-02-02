package qa.qcri.aidr.manager.service;

import qa.qcri.aidr.manager.dto.CollectionLogDataResponse;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollectionLog;

import java.util.List;
import java.util.Map;

public interface CollectionLogService {

    public void update(AidrCollectionLog collection) throws Exception;

    public void delete(AidrCollectionLog collection) throws Exception;

    public void create(AidrCollectionLog collection) throws Exception;

    public AidrCollectionLog findById(Integer id) throws Exception;

    public CollectionLogDataResponse findAll(Integer start, Integer limit) throws Exception;

    public CollectionLogDataResponse findAllForCollection(Integer start, Integer limit, Integer collectionId) throws Exception;

    public Integer countTotalDownloadedItemsForCollection(Integer collectionId) throws Exception;

    public Map<Integer, Integer> countTotalDownloadedItemsForCollectionIds(List<Integer> ids) throws Exception;

    public String generateCSVLink(String code) throws Exception;

    public String generateTweetIdsLink(String code) throws Exception;

}