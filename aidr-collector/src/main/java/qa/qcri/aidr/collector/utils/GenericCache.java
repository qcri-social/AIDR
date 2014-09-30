/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.utils;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.CollectorStatus;
import qa.qcri.aidr.collector.collectors.TwitterStreamTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static qa.qcri.aidr.collector.utils.ConfigProperties.getProperty;

/**
 *
 * @author Imran
 */
public class GenericCache {

    private String key;
    private Map<String, TwitterStreamTracker> twitterTrackerMap = null; //keeps twitter tracker object
    private Map<String, Long> countersMap = null; //keeps downloaded docs counter
    private Map<String, CollectionTask> twtConfigMap = null; // keeps twitter configuartions tokens and keys of a particular collections
    private Map<String, String> lastDownloadedDocumentMap = null; // stores last downloaded document
    private Map<String, CollectionTask> failedCollections = null; // keeps failed collections
    private CollectorStatus collectorStatus; // keeps collector status inforamtion
    private Map<String, String> SMSCollections;

    private GenericCache() {
        twitterTrackerMap = new HashMap<String, TwitterStreamTracker>();
        countersMap = new HashMap<String, Long>();
        twtConfigMap = new HashMap<String, CollectionTask>();
        lastDownloadedDocumentMap = new HashMap<String, String>();
        failedCollections = new HashMap<String, CollectionTask>();
        SMSCollections = new HashMap<String, String>();
        collectorStatus = new CollectorStatus();
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

    public CollectionTask getTwtConfigMap(String key) {
        return twtConfigMap.get(key);
    }

    public void setTwtConfigMap(String key, CollectionTask twtConfigMap) {
        this.twtConfigMap.put(key, twtConfigMap);
    }

    public void delTwtConfigMap(String key) {
        this.twtConfigMap.remove(key);
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
            Long smsCounter = this.countersMap.remove(code);
            String lastDownloadedDoc = this.lastDownloadedDocumentMap.get(code);

            task.setStatusCode(status);
            task.setCollectionCount(smsCounter);
            task.setLastDocument(lastDownloadedDoc);
        } else {
            task.setStatusCode(getProperty("STATUS_CODE_COLLECTION_NOTFOUND"));
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
    public List<CollectionTask> getAllConfigs() {
        List<CollectionTask> mappersList = new ArrayList<CollectionTask>();
        if (twtConfigMap != null) {
            for (Map.Entry pairs : twtConfigMap.entrySet()) {
                CollectionTask oldTask = (CollectionTask) pairs.getValue();
                CollectionTask task = oldTask.clone();
                Long tweetsCounter = this.countersMap.get(task.getCollectionCode());
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
        task.setConsumerKey(null);
        task.setConsumerSecret(null);

        return task;
    }

    public CollectionTask getConfig(String id) {

        if (!(this.twtConfigMap.containsKey(id))) {
            return null;
        }
        CollectionTask task = this.twtConfigMap.get(id).clone();
        if (task != null) {
            Long tweetsCounter = this.countersMap.remove(task.getCollectionCode());
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
                        if (!(qm.getStatusCode().equals(getProperty("STATUS_CODE_COLLECTION_ERROR")))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isTwtConfigExists(CollectionTask qm) {

        for (Map.Entry pairs : twtConfigMap.entrySet()) {
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
    
    

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
