/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.init;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpServlet;

import qa.qcri.aidr.collector.collectors.TwitterStreamTracker;
import qa.qcri.aidr.collector.utils.GenericCache;

/**
 *
 * @author Imran
 */
@Singleton
@Startup
public class FetcherStartStopController extends HttpServlet {

    public FetcherStartStopController() {
    }

    @PostConstruct
    private void startup() { 
        //Startup tasks go here
        System.out.println("AIDR-Fetcher: Starting up...");
        //task todo
        System.out.println("AIDR-Fetcher: Startup procedure completed");
    }

    @PreDestroy
    private void shutdown() { 
        //Shutdown tasks go here
        System.out.println("AIDR-Fetcher: Shutting Down...");
         List<TwitterStreamTracker> trackersList = GenericCache.getInstance().getAllTwitterTrackers();
        for (TwitterStreamTracker tracker : trackersList) {
            System.out.println("Stopping collection: " + tracker.getCollectionCode());
            tracker.abortCollection();
        }
        System.out.println("AIDR-Fetcher: Shutdown procedure completed.");
    
    }

    public static FetcherStartStopController getInstance() {
        return StartUPBeanHolder.INSTANCE;
    }
    
    private static class StartUPBeanHolder {

        private static final FetcherStartStopController INSTANCE = new FetcherStartStopController();
    }
}
