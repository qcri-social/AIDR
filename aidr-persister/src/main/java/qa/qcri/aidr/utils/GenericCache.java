/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.utils;

import qa.qcri.aidr.persister.collction.RedisCollectionPersister;
import qa.qcri.aidr.persister.collector.RedisCollectorPersister;
import qa.qcri.aidr.persister.tagger.RedisTaggerPersister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Imran
 */
public class GenericCache {

    private Map<String, Long> countersMap = null; //keeps downloaded docs counter
    private Map<String, RedisCollectorPersister> persisterMap = null; // keeps collector persisters 
    private Map<String, RedisTaggerPersister> taggerPersisterMap = null; // keeps tagger persisters 
    private Map<String, RedisCollectionPersister> collectionPersisterMap = null; // keeps collection persisters

    private GenericCache() {
        countersMap = new HashMap<String, Long>();
        persisterMap = new HashMap<String, RedisCollectorPersister>();
        taggerPersisterMap = new HashMap<String, RedisTaggerPersister>();
        collectionPersisterMap = new HashMap<>();
    }

    public static GenericCache getInstance() {
        return GenericSingletonHolder.INSTANCE;
    }

    /**
     * @return the taggerPersisterMap
     */
    public RedisTaggerPersister getTaggerPersisterMap(String key) {
        return taggerPersisterMap.get(key);
    }

    /**
     * @param taggerPersisterMap the taggerPersisterMap to set
     */
    public void setTaggerPersisterMap(String key, RedisTaggerPersister taggerPersister) {
        taggerPersisterMap.put(key, taggerPersister);
    }

    /**
     * Delete persister entry for given collectionCode
     * @param key is collectionCode
     * @throws InterruptedException
     */
    public void delTaggerPersisterMap(String key) throws InterruptedException {
    	RedisTaggerPersister p = taggerPersisterMap.get(key);
        p.suspendMe();
        taggerPersisterMap.remove(key);
    }
    
    private static class GenericSingletonHolder {

        private static final GenericCache INSTANCE = new GenericCache();
    }

    public RedisCollectorPersister getPersisterObject(String key) {
        return persisterMap.get(key);
    }

    public void setPersisterObject(String key, RedisCollectorPersister p) {
        persisterMap.put(key, p);
    }

    public void delPersisterObject(String key) throws InterruptedException {
        RedisCollectorPersister p = persisterMap.get(key);
        p.suspendMe();
        persisterMap.remove(key);
    }

    public RedisCollectionPersister getCollectionPersisterObject(String key) {
        return collectionPersisterMap.get(key);
    }

    public void setCollectionPersisterMap(String key, RedisCollectionPersister collectionPersister) {
        collectionPersisterMap.put(key, collectionPersister);
    }

    /**
     * Delete persister entry for given collectionCode
     * @param key is collectionCode
     * @throws InterruptedException
     */
    public RedisCollectionPersister delCollectionPersisterMap(String key) throws InterruptedException {
    	return collectionPersisterMap.remove(key);
    }

    /**
     * 
     * @return List of active collector persisters
     */
    public List<RedisCollectorPersister> getAllPersisters() {
        List<RedisCollectorPersister> persistersList = new ArrayList<RedisCollectorPersister>();
        if (persisterMap != null) {
            for (Map.Entry pairs : persisterMap.entrySet()) {
                persistersList.add((RedisCollectorPersister) pairs.getValue());
            }
        }
        return persistersList;
    }
    
    /**
     * 
     * @return List of active tagger persisters
     */
    public List<RedisTaggerPersister> getAllTaggerPersisters() {
        List<RedisTaggerPersister> persistersList = new ArrayList<RedisTaggerPersister>();
        if (taggerPersisterMap != null) {
            for (Map.Entry pairs : taggerPersisterMap.entrySet()) {
                persistersList.add((RedisTaggerPersister) pairs.getValue());
            }
        }
        return persistersList;
    }
}
