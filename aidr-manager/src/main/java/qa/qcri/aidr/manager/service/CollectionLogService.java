package qa.qcri.aidr.manager.service;

import java.util.List;
import java.util.Map;

import qa.qcri.aidr.manager.dto.CollectionLogDataResponse;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.persistence.entities.CollectionLog;

public interface CollectionLogService {

    public void update(CollectionLog collection) throws Exception;

    public void delete(CollectionLog collection) throws Exception;

    public void create(CollectionLog collection) throws Exception;

    public CollectionLog findById(Long id) throws Exception;

    public CollectionLogDataResponse findAll(Integer start, Integer limit) throws Exception;

    public CollectionLogDataResponse findAllForCollection(Integer start, Integer limit, Long collectionId) throws Exception;

    public Integer countTotalDownloadedItemsForCollection(Long collectionId) throws Exception;

    public Map<Integer, Integer> countTotalDownloadedItemsForCollectionIds(List<Long> ids) throws Exception;

    public Map<String, Object> generateCSVLink(String code) throws Exception;

    public Map<String, Object> generateTweetIdsLink(String code) throws Exception;

	public Map<String, Object> generateJSONLink(String code, String jsonType) throws AidrException;

	public Map<String, Object> generateJsonTweetIdsLink(String code, String jsonType) throws AidrException;

	Long countTotalTweets();

}