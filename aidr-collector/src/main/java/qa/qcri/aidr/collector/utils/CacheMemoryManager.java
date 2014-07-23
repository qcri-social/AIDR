/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.utils;

//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.CacheConfiguration;

/**
 *
 * @author Muhammad Imran
 */
public class CacheMemoryManager {
	
	private static Logger logger = Logger.getLogger(CacheMemoryManager.class.getName());
	
    private static CacheMemoryManager instance;
    private CacheManager manager;
    private CacheConfiguration config;
    private Cache cache;

    private CacheMemoryManager() {
        createCache();
    }

    public static CacheMemoryManager getInstance() {
        if (instance == null) {
            instance = new CacheMemoryManager();
        }
        return instance;
    }

    private Cache getCache() {
        return cache;
    }

    public Integer getSize() {
        return cache.getSize();
    }

    private void setConfiguration(CacheConfiguration config) {

//        <cache name="ResevalMashCache"
//        eternal="true"
//        maxElementsInMemory="100"
//        overflowToDisk="false"
//        diskPersistent="false"
//        memoryStoreEvictionPolicy="LRU"
//        timeToLiveSeconds="100"
//        statistics="true"
//        />

        config.setName("AIDRFetcherCache");
        config.setEternal(true);
        config.setTimeToIdleSeconds(0);
        config.setTimeToLiveSeconds(600); // 86400 1 day
        config.setOverflowToDisk(false);
        config.setDiskPersistent(false);
        config.maxElementsInMemory(30);
        config.setMemoryStoreEvictionPolicy("LRU");


//        FactoryConfiguration peerListenerFactory = new FactoryConfiguration();
//        peerListenerFactory.className("org.terracotta.ehcachedx.monitor.probe.ProbePeerListenerFactory");
//        peerListenerFactory.properties("monitorAddress=localhost, monitorPort=9889, memoryMeasurement=true");
//        //cacheManagerConfig.addCacheManagerPeerListenerFactory(peerListenerFactory);
//        //config.addca
//        
//       // CacheConfiguration.CacheEventListenerFactoryConfiguration cacheManagerConfig = new CacheEventListenerFactoryConfiguration();
//        config.addCacheEventListenerFactory(new CacheConfiguration.CacheEventListenerFactoryConfiguration().listenFor(peerListenerFactory));
//        //cacheConfig.statistics(true);
//        config.statistics(true);
    }

    public CacheManager getCacheManager() {
        return manager;
    }

    private void createCache() {
        try {
            manager = new CacheManager();
            cache = manager.getCache("AIDRFetcherCache");
            
            if (!manager.cacheExists("AIDRFetcherCache")) {
                manager.addCache(cache);
            }
        } catch (Exception ex) {
            //Logger.getLogger(CacheMemoryManager.class.getName()).log(Level.SEVERE, null, ex);
        	logger.error("Exception in creating cache");
        	ex.printStackTrace();
        }

    }
}
