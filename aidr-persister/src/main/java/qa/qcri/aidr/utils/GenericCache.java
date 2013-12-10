/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import qa.qcri.aidr.persister.collector.RedisCollectorPersister;
import qa.qcri.aidr.persister.tagger.RedisTaggerPersister;

/**
 *
 * @author Imran
 */
public class GenericCache {

    private Map<String, Long> countersMap = null; //keeps downloaded docs counter
    private Map<String, RedisCollectorPersister> persisterMap = null; // keeps collector persisters 
    private Map<String, RedisTaggerPersister> taggerPersisterMap = null; // keeps tagger persisters 

    private GenericCache() {
        countersMap = new HashMap<String, Long>();
        persisterMap = new HashMap<String, RedisCollectorPersister>();
        taggerPersisterMap = new HashMap<String, RedisTaggerPersister>();
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

    public List<RedisCollectorPersister> getAllPersisters() {
        List<RedisCollectorPersister> persistersList = new ArrayList<RedisCollectorPersister>();
        if (persisterMap != null) {
            for (Map.Entry pairs : persisterMap.entrySet()) {
                persistersList.add((RedisCollectorPersister) pairs.getValue());
            }
        }
        return persistersList;
    }
}
