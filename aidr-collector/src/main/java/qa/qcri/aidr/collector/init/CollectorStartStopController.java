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

import org.apache.log4j.Logger;

import qa.qcri.aidr.collector.api.FacebookCollectionController;
import qa.qcri.aidr.collector.api.TwitterCollectionController;
import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.CollectorStatus;
import qa.qcri.aidr.collector.utils.GenericCache;

/**
 * Class responsible of executing tasks at the startup of the application as well as at the shutdown of the application.
 * @author Imran
 */
@Singleton
@Startup
public class CollectorStartStopController extends HttpServlet {

	private static Logger logger = Logger
			.getLogger(CollectorStartStopController.class);

	public CollectorStartStopController() {
	}

	@PostConstruct
	private void startup() {
		// Startup tasks go here
		//System.out.println("AIDR-Collector: Starting up...");
		logger.info("AIDR-Collector: Starting up...");
		// task todo
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String startDate = dateFormat.format(cal.getTime());
		GenericCache.getInstance().setCollectorStatus(
				new CollectorStatus(startDate, "RUNNING", 0));
		logger.info("AIDR-Collector: Startup procedure completed @ "
				+ startDate);
	}

	@PreDestroy
	private void shutdown() {
		// Shutdown tasks go here
		//System.out.println("AIDR-Collector: Shutting Down...");
		logger.info("AIDR-Collector: Shutting Down...");
		List<CollectionTask> collections = GenericCache.getInstance()
				.getAllRunningCollectionTasks();
		TwitterCollectionController twitterCollector = new TwitterCollectionController();
		FacebookCollectionController facebookCollector = new FacebookCollectionController();
		for (CollectionTask collection : collections) {
			System.out.println("Stopping " + collection.getCollectionCode());
			logger.info("Stopping " + collection.getCollectionCode());
			if("Facebook".equalsIgnoreCase(collection.getProvider())) {
				facebookCollector.stopTask(collection.getCollectionCode());
			} else {
				twitterCollector.stopTask(collection.getCollectionCode());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.warn("Error while PreDestroying the collector: " + e.getMessage());
			}

		}
		//System.out.println("AIDR-Collector: Shutdown procedure completed.");
		logger.info("AIDR-Collector: Shutdown procedure completed.");

	}

	public static CollectorStartStopController getInstance() {
		return StartUPBeanHolder.INSTANCE;
	}

	private static class StartUPBeanHolder {

		private static final CollectorStartStopController INSTANCE = new CollectorStartStopController();
	}

}
