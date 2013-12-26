/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.init;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpServlet;
import qa.qcri.aidr.collector.beans.CollectorStatus;

import qa.qcri.aidr.collector.collectors.TwitterStreamTracker;
import qa.qcri.aidr.collector.utils.GenericCache;

/**
 *
 * @author Imran
 */
@Singleton
@Startup
public class CollectorStartStopController extends HttpServlet {

    public CollectorStartStopController() {
    }

    @PostConstruct
    private void startup() { 
        //Startup tasks go here
        System.out.println("AIDR-Collector: Starting up...");
        //task todo
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String startDate = dateFormat.format(cal.getTime());
        GenericCache.getInstance().setCollectorStatus(new CollectorStatus(startDate, "RUNNING", 0));
        System.out.println("AIDR-Collector: Startup procedure completed @ " + startDate);
    }

    @PreDestroy
    private void shutdown() { 
        //Shutdown tasks go here
        System.out.println("AIDR-Collector: Shutting Down...");
         List<TwitterStreamTracker> trackersList = GenericCache.getInstance().getAllTwitterTrackers();
        for (TwitterStreamTracker tracker : trackersList) {
            System.out.println("Stopping collection: " + tracker.getCollectionCode());
            tracker.abortCollection();
        }
        System.out.println("AIDR-Collector: Shutdown procedure completed.");
    
    }

    public static CollectorStartStopController getInstance() {
        return StartUPBeanHolder.INSTANCE;
    }
    
    private static class StartUPBeanHolder {

        private static final CollectorStartStopController INSTANCE = new CollectorStartStopController();
    }
}
