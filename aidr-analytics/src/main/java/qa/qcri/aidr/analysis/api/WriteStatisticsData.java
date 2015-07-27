package qa.qcri.aidr.analysis.api;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

import qa.qcri.aidr.analysis.entity.ConfidenceData;
import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.analysis.stat.ConfDataMapRecord;
import qa.qcri.aidr.analysis.stat.TagDataMapRecord;
import qa.qcri.aidr.analysis.utils.AnalyticsConfigurator;
import qa.qcri.aidr.analysis.utils.ChannelBufferManager;
import qa.qcri.aidr.analysis.utils.ConfCounterKey;
import qa.qcri.aidr.analysis.utils.CounterKey;
import qa.qcri.aidr.analysis.utils.OutputConfigurator;
import qa.qcri.aidr.analysis.facade.ConfidenceStatisticsResourceFacade;
import qa.qcri.aidr.analysis.facade.TagDataStatisticsResourceFacade;
import qa.qcri.aidr.common.values.ReturnCode;



/**
 * This class provides an interface to automatically start/stop/manage the analytics DB writer service at deployment time
 * to monitor REDIS channels for data to store statistical data into the aidr analytics DB.
 * 
 * @author: koushik
 */

@Path("/save/")
public class WriteStatisticsData implements ServletContextListener {

	// Debugging
	private static Logger logger = Logger.getLogger(WriteStatisticsData.class);

	private volatile boolean runFlag = false;
	
	@EJB
	private TagDataStatisticsResourceFacade tagDataEJB;

	@EJB
	private ConfidenceStatisticsResourceFacade confDataEJB;

	private ExecutorService executorServicePool = null;
	private Thread t = null;

	//private static final String SENTINEL = "#";
	//private static ConcurrentHashMap<CounterKey, Object> tagDataMap = null;
	//private static ConcurrentHashMap<CounterKey, Object> confDataMap = null;
	//private static ConcurrentHashMap<String, Long> channelMap = null;
	private List<Long> granularityList = null;


	private static ChannelBufferManager cbManager = null; 			// managing buffers for each publishing channel
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Initializing channel buffer manager");
		
		AnalyticsConfigurator configurator = AnalyticsConfigurator.getInstance();
		granularityList = configurator.getGranularities();
		
		if (null == cbManager) {
			cbManager = new ChannelBufferManager();
			cbManager.initiateChannelBufferManager(GetStatistics.CHANNEL_REG_EX);
			logger.info("Done initializing channel buffer manager with regEx pattern: " + GetStatistics.CHANNEL_REG_EX);
			System.out.println("[contextInitialized] Done initializing channel buffer manager with regEx pattern: " + GetStatistics.CHANNEL_REG_EX);
		}
		//tagDataMap = ChannelBufferManager.getTagDataMap();
		//confDataMap = ChannelBufferManager.getConfDataMap();
		//channelMap = ChannelBufferManager.getChannelMap();
		
		runFlag = true;
		t = new Thread(new WriterThread());
		t.setName("AIDR-Analysis DB Writer Thread");
		executorServicePool = cbManager.getExecutorServicePool();
		if (executorServicePool != null) {
			executorServicePool.submit(t);
			System.out.println("Executor thread pool initialized, submitted thread: " + t.getName() + " to pool = " + executorServicePool);
		}
		logger.info("Done initializing channel buffer manager");
		System.out.println("Done initializing channel buffer manager");
		System.out.print("Initialized with granularities: ");
		for (Long g: granularityList) {
			System.out.print(g + "\t");
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		runFlag = false;
		//cbManager.close();
		logger.info("Context destroyed");
	}

	@GET
	@Path("/tracked/channels")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTrackedChannelsList() {
		Set<String> channelList = cbManager.getActiveChannelsList();
		JSONObject json = new JSONObject();
		json.put("channels", channelList);
		return Response.ok(json.toJSONString()).build();
	}

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		JsonObject obj = Json.createObjectBuilder().add("aidr-analysis/writeData", "RUNNING").build();

		return Response.ok(obj).build();
	}

	private ReturnCode writeOutputDataToTagDataDB(Long granularity,Long timestamp) {
		// logger.info("tagDataMap size: " + (tagDataMap != null ?
		// tagDataMap.size() : "null"));
		try {
			for (CounterKey key : cbManager.getTagDataMap().keySet()) {
				TagDataMapRecord tCount = (TagDataMapRecord) ChannelBufferManager.getTagDataMap().get(key);
				TagData t = new TagData(key.getCrisisCode(), timestamp, granularity, key.getAttributeCode(), key.getLabelCode(), tCount.getCount(granularity));
				if (tCount.getCount(granularity) > 0) {
					System.out.println("Will attempt persistence of tag key: " + key.toString());
					tagDataEJB.writeData(t);
				}
				tCount.resetCount(granularity);
			}
		} catch (Exception e) {
			logger.error("Error in writing to TagDataDB table!", e);
			return ReturnCode.ERROR;
		}
		return ReturnCode.SUCCESS;
	}

	private ReturnCode writeOutputDataToConfDataDB(Long granularity, Long timestamp) {
		try {
			for (CounterKey key : cbManager.getConfDataMap().keySet()) {
				ConfDataMapRecord fCount = (ConfDataMapRecord) ChannelBufferManager.getConfDataMap().get(key);
				ConfidenceData f = new ConfidenceData(key.getCrisisCode(), timestamp, granularity, key.getAttributeCode(), key.getLabelCode(),
														Integer.parseInt(((ConfCounterKey) key).getBinNumber()), fCount.getCount(granularity));
				if (fCount.getCount(granularity) > 0) {
					System.out.println("Will attempt persistence of conf key: " + key.toString());
					confDataEJB.writeData(f);
				}
				fCount.resetCount(granularity);
			}
		} catch (Exception e) {
			logger.error("Error in writing to ConfidenceDataDB table!", e);
			return ReturnCode.ERROR;
		}
		return ReturnCode.SUCCESS;
	}


	private class WriterThread implements Runnable {

		/**
		 * The 'consumer' method - consumes the statistics data produced by
		 * writing to respective tables in DB
		 */
		@Override
		public void run() {
			logger.info("Started aidr-analytics writer thread: " + t.getName());
			System.out.println("Started aidr-analytics writer thread: " + t.getName());
			Map<Long, Long> lastTagDataWriteTime = new TreeMap<Long, Long>();
			Map<Long, Long> lastConfDataWriteTime = new TreeMap<Long, Long>();
			for (Long g : granularityList) {
				lastTagDataWriteTime.put(g, 0L);
				lastConfDataWriteTime.put(g, 0L);
			}
			while (runFlag) {
				long currentTime = System.currentTimeMillis();
				if (granularityList != null) {
					for (Long granularity : granularityList) {
						if (0 == lastTagDataWriteTime.get(granularity) || (currentTime - lastTagDataWriteTime.get(granularity)) >= granularity) {
							// Write to DB table
							ReturnCode retVal = writeOutputDataToTagDataDB(granularity, currentTime);
							lastTagDataWriteTime.put(granularity, currentTime);
							//logger.info("retVal = " + retVal);
							if (ReturnCode.SUCCESS.equals(retVal)) {
								//logger.info("Successfully wrote for granularity: " + granularity + " at time = " + new Date(currentTime));
								//System.out.println("Successfully wrote for granularity: " + granularity + " at time = " + new Date(currentTime));
							}
						}
						if (0 == lastConfDataWriteTime.get(granularity) || (currentTime - lastConfDataWriteTime.get(granularity)) >= granularity) {
							// Write to DB table
							ReturnCode retVal = writeOutputDataToConfDataDB(granularity, currentTime);
							//logger.info("retVal = " + retVal);
							lastConfDataWriteTime.put(granularity, currentTime);
							if (ReturnCode.SUCCESS.equals(retVal)) {
								//logger.info("Successfully wrote for granularity: " + granularity + " at time = " + new Date(currentTime));
							}
						}
					}
					try {
						Thread.sleep(granularityList.get(0) - 1000); // sleep for the minimum granularity period
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}
	
	
}
