package qa.qcri.aidr.collector.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.CollectorStatus;
import qa.qcri.aidr.collector.beans.FacebookCollectionTask;
import qa.qcri.aidr.collector.beans.TwitterCollectionTask;
import qa.qcri.aidr.collector.collectors.FacebookFeedTracker;
import qa.qcri.aidr.collector.collectors.TwitterStreamTracker;

/**
 *
 * This class is responsible of keeping complex data structures in the main-memory for a fast access. 
 */
public class GenericCache {

    private String key;
    private Map<String, TwitterStreamTracker> twitterTrackerMap = null; //keeps twitter tracker object
    private Map<String, Long> countersMap = null; //keeps downloaded docs counter
    private Map<String, TwitterCollectionTask> twtConfigMap = null; // keeps twitter configuartions tokens and keys of a particular collections
    private Map<String, String> lastDownloadedDocumentMap = null; // stores last downloaded document
    private Map<String, CollectionTask> failedCollections = null; // keeps failed collections
    private CollectorStatus collectorStatus; // keeps collector status inforamtion
    private Map<String, String> SMSCollections;
    private Map<String, FacebookCollectionTask> fbConfigMap =  null;
    private Map<String, FacebookFeedTracker> fbTrackerMap = null; //keeps twitter tracker object
    private Map<String, Integer> reconnectAttempts;
    private Map<String, Boolean> fbSyncObjMap;
    private Map<String, Integer> fbSyncStateMap;
    private static CollectorConfigurator configProperties = CollectorConfigurator.getInstance();
    
    private GenericCache() {
        twitterTrackerMap = new HashMap<String, TwitterStreamTracker>();
        countersMap = new ConcurrentHashMap<String, Long>();
        twtConfigMap = new HashMap<String, TwitterCollectionTask>();
        fbConfigMap = new HashMap<String, FacebookCollectionTask>();
        fbTrackerMap = new HashMap<String, FacebookFeedTracker>();
        lastDownloadedDocumentMap = new HashMap<String, String>();
        failedCollections = new HashMap<String, CollectionTask>();
        SMSCollections = new HashMap<String, String>();
        collectorStatus = new CollectorStatus();
        reconnectAttempts = new HashMap<String,Integer>();
        fbSyncObjMap = new ConcurrentHashMap<String, Boolean>();
        fbSyncStateMap = new ConcurrentHashMap<String, Integer>();
    }

    public static GenericCache getInstance() {
        return GenericSingletonHolder.INSTANCE;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    private static class GenericSingletonHolder {

        private static final GenericCache INSTANCE = new GenericCache();
    }
    
    public void setCollectorStatus(CollectorStatus cStatus){
        this.collectorStatus=cStatus;
    }
    
    public CollectorStatus getCollectorStatus(){
        return this.collectorStatus;
    }

    public void setTwitterTracker(String key, TwitterStreamTracker tracker) {
        this.twitterTrackerMap.put(key, tracker);
    }

    public void delTwitterTracker(String key) {
        this.twitterTrackerMap.remove(key);
    }

    public TwitterStreamTracker getTwitterTracker(String key) {
        return this.twitterTrackerMap.get(key);
    }

    public void setFailedCollection(String key, CollectionTask task) {
        this.failedCollections.put(key, task);
    }

    public void delFailedCollection(String key) {
        this.failedCollections.remove(key);
    }

    public void setFacebookTracker(String key, FacebookFeedTracker tracker) {
        this.fbTrackerMap.put(key, tracker);
    }

    public void delFacebookTracker(String key) {
        this.fbTrackerMap.remove(key);
    }

    public FacebookFeedTracker getFacebookTracker(String key) {
        return this.fbTrackerMap.get(key);
    }

    public CollectionTask getFailedCollectionTask(String key) {

        if (this.failedCollections.containsKey(key)) {
            CollectionTask task = this.failedCollections.get(key).clone();
            return ommitKeys(task);
        }
        return null;
    }

    public List<CollectionTask> getAllFailedCollections() {
        List<CollectionTask> collectionList = new ArrayList<CollectionTask>();
        if (failedCollections != null) {
            for (Map.Entry pairs : failedCollections.entrySet()) {
                CollectionTask oldTask = (CollectionTask) pairs.getValue();
                CollectionTask task = oldTask.clone();
                collectionList.add(ommitKeys(task));
            }

            return collectionList;
        }

        return collectionList;
    }

    public void setLastDownloadedDoc(String cacheKey, String doc) {
        this.lastDownloadedDocumentMap.put(cacheKey, doc);
    }

    public void delLastDownloadedDoc(String key) {
        this.lastDownloadedDocumentMap.remove(key);
    }

    public String getLastDownloadedDoc(String key) {
        return this.lastDownloadedDocumentMap.get(key);
    }

    public TwitterCollectionTask getTwtConfigMap(String key) {
        return twtConfigMap.get(key);
    }

    public FacebookCollectionTask getFbConfigMap(String key) {
        return fbConfigMap.get(key);
    }
    
    public void setTwtConfigMap(String key, TwitterCollectionTask config) {
        this.twtConfigMap.put(key, config);
    }

    public void setFbConfigMap(String key, FacebookCollectionTask config) {
        this.fbConfigMap.put(key, config);
    }

    public void delTwtConfigMap(String key) {
        this.twtConfigMap.remove(key);
    }

    public void delFbConfigMap(String key) {
        this.fbConfigMap.remove(key);
    }
    
    public Map<String, String> getSMSCollections() {
        return SMSCollections;
    }

    public String getSMSCollection(String code) {
        return SMSCollections.get(code);
    }

    public void putSMSCollection(String code, String status) {
        this.SMSCollections.put(code, status);
    }

    public void removeSMSCollection(String code) {
        this.SMSCollections.remove(code);
    }

    public synchronized void increaseSMSCount(String code) {
        Long count = countersMap.get(code);
        if(count == null)
            count = 0L;
        countersMap.put(code, ++count);
    }

    public synchronized Long getSMSCount(String code){
        return countersMap.get(code);
    }

    public CollectionTask getSMSConfig(String code) {
        CollectionTask task = new CollectionTask();
        String status = SMSCollections.get(code);

        task.setCollectionCode(code);
        if (status != null) {
            Long smsCounter = this.countersMap.get(code);
            if(smsCounter == null)
                smsCounter = 0L;

            String lastDownloadedDoc = this.lastDownloadedDocumentMap.get(code);

            task.setStatusCode(status);
            task.setCollectionCount(smsCounter);
            task.setLastDocument(lastDownloadedDoc);
        } else {
            task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_NOTFOUND));
        }

        return task;
    }

    
    public List<CollectionTask> getAllRunningCollectionTasks(){
         List<CollectionTask> collections = new ArrayList<CollectionTask>();
         
         if (twtConfigMap != null) {
            for (Map.Entry pairs : twtConfigMap.entrySet()) {
                CollectionTask oldTask = (CollectionTask) pairs.getValue();
                CollectionTask task = oldTask.clone();
                Long tweetsCounter = this.countersMap.get(task.getCollectionCode());
                String lastDownloadedDoc = this.lastDownloadedDocumentMap.get(task.getCollectionCode());
                task.setCollectionCount(tweetsCounter);
                task.setLastDocument(lastDownloadedDoc);
                collections.add(task);
            }
        }
        return collections;
    }
    
    public Boolean isCollectionRunning(String collectionCode){
       if (twtConfigMap != null) {
          if(twtConfigMap.containsKey(collectionCode)){
        	  return true;
          }
       }
       return false;
   }
    
    
    public List<CollectionTask> getAllConfigs() {
        List<CollectionTask> mappersList = new ArrayList<CollectionTask>();
        if (twtConfigMap != null) {
            for (Map.Entry pairs : twtConfigMap.entrySet()) {
                CollectionTask oldTask = (CollectionTask) pairs.getValue();
                CollectionTask task = oldTask.clone();
                Long tweetsCounter = this.countersMap.get(task.getCollectionCode());
                if(tweetsCounter == null)
                    tweetsCounter = 0L;
                String lastDownloadedDoc = this.lastDownloadedDocumentMap.get(task.getCollectionCode());
                task.setCollectionCount(tweetsCounter);
                task.setLastDocument(lastDownloadedDoc);
                mappersList.add(ommitKeys(task));
            }
        }
        if (fbConfigMap != null) {
            for (Map.Entry pairs : fbConfigMap.entrySet()) {
                CollectionTask oldTask = (CollectionTask) pairs.getValue();
                CollectionTask task = oldTask.clone();
                Long tweetsCounter = this.countersMap.get(task.getCollectionCode());
                if(tweetsCounter == null)
                    tweetsCounter = 0L;
                String lastDownloadedDoc = this.lastDownloadedDocumentMap.get(task.getCollectionCode());
                task.setCollectionCount(tweetsCounter);
                task.setLastDocument(lastDownloadedDoc);
                mappersList.add(ommitKeys(task));
            }
        }
        return mappersList;
    }

    private CollectionTask ommitKeys(CollectionTask task) {

        task.setAccessToken(null);
        task.setAccessTokenSecret(null);
        return task;
    }

    public CollectionTask getConfig(String id, String provider) {

    	CollectionTask task =  null;
        if (!(this.twtConfigMap.containsKey(id) || this.fbConfigMap.containsKey(id))) {
            return null;
        }
        
        if("twitter".equals(provider)) {
        	task = this.twtConfigMap.get(id).clone();
        } else {
        	task = this.fbConfigMap.get(id).clone();
        }
        
        if (task != null) {
            Long tweetsCounter = this.countersMap.get(task.getCollectionCode());
            if(tweetsCounter == null)
                tweetsCounter = 0L;
            String lastDownloadedDoc = this.lastDownloadedDocumentMap.get(task.getCollectionCode());
            task.setCollectionCount(tweetsCounter);
            task.setLastDocument(lastDownloadedDoc);
            return ommitKeys(task);
        } else {
            task = this.failedCollections.get(id);
            if (task != null) {
                return ommitKeys(task);
            }
        }

        return task;

    }

    public boolean isRuningTwtConfigExists(CollectionTask qm) {
        for (Map.Entry pairs : twtConfigMap.entrySet()) {
            CollectionTask storedQM = (CollectionTask) pairs.getValue();
            if (storedQM != null) {
                if (storedQM.equals(qm)) {
                    if (qm.getStatusCode() != null) {
                        if (!(qm.getStatusCode().equals(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR)))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isConfigExists(CollectionTask qm) {

    	// check for twitter collection
        for (Map.Entry pairs : twtConfigMap.entrySet()) {
            CollectionTask storedQM = (CollectionTask) pairs.getValue();
            if (storedQM.equals(qm)) {
                return true;
            }
        }
        
        // check for fb collection
        for (Map.Entry pairs : fbConfigMap.entrySet()) {
            CollectionTask storedQM = (CollectionTask) pairs.getValue();
            if (storedQM.equals(qm)) {
                return true;
            }
        }
        return false;
    }

    // keeps track of tweets counters for various running collections
    public void incrCounter(String key, Long counter) {
        if (countersMap.containsKey(key)) {
            countersMap.put(key, countersMap.get(key) + counter);
        } else {
            countersMap.put(key, counter);
        }
    }

    public Long getCounterStatus(String key) {
        return countersMap.get(key);
    }

    public void deleteCounter(String key) {
        countersMap.remove(key);
    }

    public List<TwitterStreamTracker> getAllTwitterTrackers() {
        List<TwitterStreamTracker> trackersList = new ArrayList<TwitterStreamTracker>();
        for (Map.Entry pairs : twitterTrackerMap.entrySet()) {
            String key = (String) pairs.getKey();
            trackersList.add((TwitterStreamTracker) pairs.getValue());
        }

        return trackersList;
    }
    
    //keep track to number of reconnect attempts for for various running collections
    public int incrAttempt(String key) {
        if (reconnectAttempts.containsKey(key)) {
        	reconnectAttempts.put(key, reconnectAttempts.get(key) + 1);
        } else {
        	reconnectAttempts.put(key, 1);
        }
        return reconnectAttempts.get(key);
    }
    
    //reset number of reconnect attempts when collection is able to establish connection
    public void resetAttempt(String key) {
        if (reconnectAttempts.containsKey(key)) {
        	reconnectAttempts.put(key, 0);
        }
    }
    
    public int getReconnectAttempts(String key) {
        return reconnectAttempts.get(key);
    }

    public void delReconnectAttempts(String key) {
    	reconnectAttempts.remove(key);
    }
    
    public Long getTotalCountSinceLastRestart() {
    	
    	Long totalCount = 0L;
    	for (Map.Entry pairs : countersMap.entrySet()) {
    		if(pairs.getValue() != null) {
    			totalCount = totalCount +  (Long) pairs.getValue() ;
    		}
    	}
    	return totalCount;
    }
    
    public List<String> getEligibleFacebookCollectionsToRun() {
    	
    	List<String> collectionList = new ArrayList<String>();
    	Long runTime = null;

    	for(Map.Entry pair : this.fbConfigMap.entrySet()) {
    		FacebookCollectionTask task = (FacebookCollectionTask) pair.getValue();
    		if(task.getLastExecutionTime() != null) {
	    		runTime = task.getLastExecutionTime().getTime() + task.getFetchInterval();
	    		if(runTime >= new Date().getTime()) {
	    			collectionList.add((String) pair.getKey());
	    		}
    		}
    	}
    	
    	return collectionList;
    }

    public Boolean getFbSyncObjMap(String key){
    	if(fbSyncObjMap.containsKey(key)){
    		return fbSyncObjMap.get(key);
    	}
    	return null;
    }
    
    public void setFbSyncObjMap(String key, Boolean obj){
    		fbSyncObjMap.put(key, obj);
    }
    
    public void delFbSyncObjMap(String key){
    	fbSyncObjMap.remove(key);
    }

    
    public Integer getFbSyncStateMap(String key){
    	if(fbSyncStateMap.containsKey(key)){
    		return fbSyncStateMap.get(key);
    	}
    	return null;
    }
    
    public void setFbSyncStateMap(String key, Integer state){
    	fbSyncStateMap.put(key, state);
    	
    }
    
    public void delFbSyncStateMap(String key){
    	fbSyncStateMap.remove(key);
    }
    
}
