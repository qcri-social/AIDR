package qa.qcri.aidr.output.stream;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ChunkedOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/test/new/chunk/")
public class NewTestChunkedOutput implements ServletContextListener {

	public final int MAX_COUNT = 10000;
	
	// Related to Async Thread management
	public static ExecutorService executorServicePool;
	private AsyncClass asyncResponder = null;
	private ChunkedOutput<String> output = null;
	
	// Debugging
	private static Logger logger = LoggerFactory.getLogger(NewTestChunkedOutput.class);
	/////////////////////////////////////////////////////////////////////////////

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public ChunkedOutput<String> getChunkedResponse(@Context HttpServletRequest request) {
		request.getSession(true).invalidate();
		output = new ChunkedOutput<String>(String.class);
		
		logger.info("Received GET request...");
		asyncResponder = new AsyncClass(output);
		logger.info("[doGet] Attempting async response thread");
		executorServicePool.execute(asyncResponder);
	
		logger.info("End of async main thread");
		return output;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");		// set logging level for slf4j
		executorServicePool = Executors.newFixedThreadPool(5);		// max number of threads
		logger.info("Context Initialized");

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		try {
			if (output != null) output.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[contextDestroyed] Error in closing ChunkedOutput");
		}
		shutdownAndAwaitTermination(executorServicePool);
		logger.info("Context destroyed");
		
	}

	// cleanup all threads 
	void shutdownAndAwaitTermination(ExecutorService threadPool) {
		threadPool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
				threadPool.shutdownNow(); 			// Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!threadPool.awaitTermination(1, TimeUnit.SECONDS))
					logger.error("[shutdownAndAwaitTermination] Pool did not terminate");
				System.err.println("[shutdownAndAwaitTermination] Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			threadPool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
	
	private class AsyncClass implements AsyncListener, Runnable {

		private final ChunkedOutput<String> output;
		private GenerateTestData testData;
		
		public AsyncClass(final ChunkedOutput<String> outputMaster) {
			testData = new GenerateTestData(MAX_COUNT);
			output = outputMaster;
			logger.info("[AsyncClass] param output = " + outputMaster);
			logger.info("[AsyncClass] output = " + output);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			logger.info("[run] Started async run thread...");
			try {
				String chunk = testData.getNextJsonString();
				while (chunk != null) {
					if (!output.isClosed()) {
						output.write(chunk);
						output.write("\n");
					}
					else {
						logger.info("Possible client disconnect...");
						break;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
					chunk = testData.getNextJsonString();
					//logger.info("received from generator: " + chunk);
				}
			} catch (Exception e) {
				// IOException thrown when writing the
				// chunks of response: should be handled
				logger.info("Error in write attempt - possible client disconnect");
			} finally {
				try {
					output.close();
				} catch (IOException e) {
					logger.error("[run] Error attempting closing ChunkedOutput");
				}
			}
			logger.info("Done with async thread - joining with master now");
		}

		@Override
		public void onComplete(AsyncEvent event) throws IOException {
			// TODO Auto-generated method stub
			logger.info("[onComplete] async task completed");
		}

		@Override
		public void onTimeout(AsyncEvent event) throws IOException {
			// TODO Auto-generated method stub
			logger.info("[onTimeout] async task timeout");	
		}

		@Override
		public void onError(AsyncEvent event) throws IOException {
			// TODO Auto-generated method stub
			logger.error("[onError] An error occured while executing task for client ");
		}

		@Override
		public void onStartAsync(AsyncEvent event) throws IOException {
			// TODO Auto-generated method stub
			logger.error("[onStartAsync] async started...");
		}
	}
}
