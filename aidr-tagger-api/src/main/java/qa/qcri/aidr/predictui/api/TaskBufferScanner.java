package qa.qcri.aidr.predictui.api;

/**
 * @author Koushik
 * 
 * RESTful service to start thread at tagger-api initialization for monitoring
 * and deleting stale tasks from the DB based on configurable parameter: TASK_EXPIRY_AGE_LIMIT.
 * The thread periodically wakes up and scans the DB. The implemented REST services are:
 * 
 * 		a) ping
 * 		b) restart
 *      c) stop
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.ConfigurationPropertyFileException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotRecognizedException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotSetException;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictui.facade.TaskBufferScannerFacade;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurationProperty;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurator;

@Path("/taskscanner")
@Singleton
@Startup
public class TaskBufferScanner  {
	@Context
	private UriInfo context;
	@EJB
	private TaskBufferScannerFacade taskBufferScannerEJB;

	public TaskBufferScanner() {
		
	}

	//private static Logger logger = Logger.getLogger(TaskBufferScanner.class);
	private static Logger logger = Logger.getLogger(TaskBufferScanner.class);
	private static ErrorLog elog = new ErrorLog();

	private static ExecutorService executorService = null;
	private static boolean threadStatus;
	private static boolean scan;
	@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	private EntityManager em;

	/**
	 * Starts the scanner thread at initialization time
	 */
	@PostConstruct
	public void contextInitialized() {
		threadStatus = false;
		executorService = Executors.newCachedThreadPool();
		boolean isSuccess = startTaskBufferScannerThread(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.TASK_EXPIRY_AGE_LIMIT), TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.TASK_BUFFER_SCAN_INTERVAL));
		if (isSuccess) {
			logger.info("Context Initialized - started task buffer scanner thread.");
		}
		else {
			logger.error("[TaskBufferScanner] Fatal error - could not start task buffer scanner thread!");
		}
	}

	@PreDestroy
	public void contextDestroyed() {
		// TODO Auto-generated method stub
		if (executorService != null) shutdownAndAwaitTermination();
		threadStatus = false;
		logger.info("Context destroyed");
	}

	public boolean isThreadRunning() {
		return threadStatus;
	}

	public boolean startTaskBufferScannerThread(final String taskMaxAge, final String taskScanInterval) {
		System.out.println("[startTaskBufferScannerThread] Attempting to start new thread");
		scan = true;
		try {
			executorService.execute(new Runnable() {
				public void run() {
					try {
						while (scan) {
							try {
								// Hibernate code to delete stale tasks from Document entity/table goes here
								final long TASK_SCAN_INTERVAL = taskBufferScannerEJB.parseTime(taskScanInterval);
								threadStatus = true;
								taskBufferScannerEJB.ScanTaskBuffer(taskMaxAge, taskScanInterval);

								Thread.sleep(TASK_SCAN_INTERVAL);
							} catch (InterruptedException e) {} 
						}
					} finally {
						threadStatus = false;
						logger.info("[run] Done with thread.");
					}
				}
			});
		} catch (RejectedExecutionException e) {
			logger.error("Fatal error executing thread! Terminating.");
			logger.error(elog.toStringException(e));
			threadStatus = false;
			return false;
		} catch (NullPointerException e) {
			logger.error("Fatal error executing thread! Terminating.");
			logger.error(elog.toStringException(e));
			threadStatus = false;
			return false;
		} 
		return true;
	}

	/**
	 * 
	 * @param scanInterval periodicity of scanning the DB
	 * @param maxTaskAge age of tasks to remove
	 * @return 
	 * REST service to restart the scanner thread with possible new 
	 * scanInterval and maxTaskAge parameters.
	 */
	@Path("/restart")
	@GET
	public Response restartScanner(@QueryParam("interval") String scanInterval,
			@QueryParam("maxage") String maxTaskAge) {
		if (null == executorService) {
			executorService = Executors.newCachedThreadPool();
			logger.info("Created new executor Thread Pool");
		}
		final String newMaxTaskAge;
		final String newScanInterval;
		if (null == scanInterval || null == maxTaskAge) {
			newMaxTaskAge = TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.TASK_EXPIRY_AGE_LIMIT);
			newScanInterval = TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.TASK_BUFFER_SCAN_INTERVAL);
		} else {
			newMaxTaskAge = maxTaskAge;
			newScanInterval = scanInterval;
		}

		if (!isThreadRunning()) { 
			startTaskBufferScannerThread(newMaxTaskAge, newScanInterval);
			logger.info("Restarted task buffer scanning service");
			String responseStr = "{\"application\":\"TaskBufferScanner\", \"status\":\"RESTARTED\"}";
			return Response.ok(responseStr).build();
		}
		else {
			logger.warn("Task Buffer Scanner Thread already running!");
		}
		String responseStr = "{\"application\":\"TaskBufferScanner\", \"status\":\"ALREADY RUNNING\"}";
		return Response.ok(responseStr).build();
	}

	/**
	 * Stops the scanner thread service
	 * @return
	 */
	@Path("/stop")
	@GET
	public Response stopScanner() {
		scan = false;
		threadStatus = false;
		if (executorService != null) {
			shutdownAndAwaitTermination();
		}
		else {
			logger.info("No active Task Buffer Scanner Thread found to kill!");
		}
		logger.info("Stopped task buffer scanning service");
		String responseStr = "{\"application\":\"TaskBufferScanner\", \"status\":\"STOPPED\"}";
		return Response.ok(responseStr).build();
	}

	/**
	 * 
	 * @return status of scanner thread - running or stopped.
	 */

	@GET
	@Path("/ping")
	@Produces("application/json")
	public Response pingTaskBufferScanner() {
		//System.out.println("[pingTaskBufferScanner] Received PING request");
		String responseStr = null;
		if (isThreadRunning()) {
			responseStr = "{\"application\":\"TaskBufferScanner\", \"status\":\"RUNNING\"}";
		} else {
			responseStr = "{\"application\":\"TaskBufferScanner\", \"status\":\"STOPPED\"}";
		}
		return Response.ok(responseStr).build(); 
	}

	// cleanup all threads 
	void shutdownAndAwaitTermination() {

		executorService.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
				executorService.shutdownNow();                         // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!executorService.awaitTermination(1, TimeUnit.SECONDS))
					logger.error("[shutdownAndAwaitTermination] Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			executorService.shutdownNow();
			// Preserve interrupt status
			try {
				Thread.currentThread().interrupt();
			} catch (Exception e) {}
		}
		executorService = null;
	}

	
}
