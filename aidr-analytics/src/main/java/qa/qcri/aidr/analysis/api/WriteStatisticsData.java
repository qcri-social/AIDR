package qa.qcri.aidr.analysis.api;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
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
import qa.qcri.aidr.analysis.stat.*;
import qa.qcri.aidr.analysis.utils.GranularityData;
import qa.qcri.aidr.analysis.facade.ConfidenceStatisticsResourceFacade;
import qa.qcri.aidr.analysis.facade.TagDataStatisticsResourceFacade;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.common.values.ReturnCode;
import qa.qcri.aidr.output.filter.ClassifiedFilteredTweet;
import qa.qcri.aidr.output.filter.NominalLabel;
import qa.qcri.aidr.output.getdata.ChannelBufferManager;

@Path("/save/")
public class WriteStatisticsData extends ChannelBufferManager implements ServletContextListener {

	// Debugging
	private static Logger logger = Logger.getLogger(WriteStatisticsData.class.getSuperclass());
	private static ErrorLog elog = new ErrorLog();

	private volatile boolean runFlag = false;

	@EJB
	private TagDataStatisticsResourceFacade tagDataEJB;

	@EJB
	private ConfidenceStatisticsResourceFacade confDataEJB;

	private ExecutorService executorServicePool = null;
	private Thread t = null;
	
	private static final String SENTINEL = "#";
	private static ConcurrentHashMap<String, Object> tagDataMap = null;
	private static ConcurrentHashMap<String, Object> confDataMap = null;
	private static ConcurrentHashMap<String, Long> channelMap = null;
	private List<Long> granularityList = null;
	private long lastTagDataCheckedTime = 0;
	private long lastConfDataCheckedTime = 0;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Initializing channel buffer manager");
		System.out.println("[contextInitialized] Initializing channel buffer manager");
		tagDataMap = new ConcurrentHashMap<String, Object>();
		confDataMap = new ConcurrentHashMap<String, Object>();
		channelMap = new ConcurrentHashMap<String, Long>();

		granularityList = GranularityData.getGranularities();

		initiateChannelBufferManager(GetStatistics.CHANNEL_REG_EX);

		runFlag = true;
		t = new Thread(new WriterThread());
		t.setName("AIDR-Analysis DB Writer Thread");
		executorServicePool = getExecutorServicePool();
		if (executorServicePool != null) {
			executorServicePool.submit(t);
		}
		logger.info("Done initializing channel buffer manager");
		System.out.println("[contextInitialized] Done initializing channel buffer manager");
		logger.info("Context Initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		tagDataMap.clear();
		confDataMap.clear();
		runFlag = false;
		close();
		logger.info("Context destroyed");
	}
	
	@GET
	@Path("/tracked/channels")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTrackedChannelsList() {
		Set<String> channelList = getActiveChannelsList();
		JSONObject json = new JSONObject();
		json.put("channels", channelList);
		return Response.ok(json.toJSONString()).build();
	}
	
	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		JsonObject obj = Json.createObjectBuilder()
							.add("aidr-analysis/writeData", "RUNNING")
							.build();
		
		return Response.ok(obj).build();
	}
	
	private ReturnCode writeOutputDataToTagDataDB(Long granularity, Long timestamp) {
		//System.out.println("tagDataMap size: " + (tagDataMap != null ? tagDataMap.size() : "null"));
		try {
			for (String key: tagDataMap.keySet()) {
				String[] data = key.split(SENTINEL);
				// data[0] = channelName
				// data[1] = attributeCode
				// data[2] = labelCode
				TagDataMapRecord tCount = (TagDataMapRecord) tagDataMap.get(key);
				TagData t = new TagData(data[0], timestamp, granularity, data[1], data[2], tCount.getCount(granularity));
				System.out.println("Will attempt to persist data for: " + t.getCrisisCode() + ", " + t.getAttributeCode() 
						+ ", " + t.getLabelCode() + ", " + t.getTimestamp() + ", " + t.getGranularity() + ": " + t.getCount());
				tagDataEJB.writeData(t);
				tCount.resetCount(granularity);
				TagDataMapRecord temp = (TagDataMapRecord) tagDataMap.get(key);
				//System.out.println("[writeOutputDataToTagDataDB] After reset, count for key = " + key + " is = " + temp.getCount(granularity));
				
			}
		} catch (Exception e) {
			System.err.println("[writeOutputDataToTagDataDB] Error in writing to TagDataDB table!");
			e.printStackTrace();
			return ReturnCode.ERROR;
		}
		return ReturnCode.SUCCESS;
	}

	private ReturnCode writeOutputDataToConfDataDB(Long granularity, Long timestamp) {
		try {
			for (String key: confDataMap.keySet()) {
				String[] data = key.split(SENTINEL);
				// data[0] = channelName
				// data[1] = attributeCode
				// data[2] = labelCode
				// data[3] = bin
				ConfDataMapRecord fCount = (ConfDataMapRecord) confDataMap.get(key);
				ConfidenceData f = new ConfidenceData(data[0], timestamp, granularity, data[1], data[2], Integer.parseInt(data[3]), fCount.getCount(granularity));
				confDataEJB.writeData(f);
				fCount.resetCount(granularity);
				ConfDataMapRecord temp = (ConfDataMapRecord) confDataMap.get(key);
				//System.out.println("[writeOutputDataToConfDataDB] After reset, count for key = " + key + " is = " + temp.getCount(granularity));
			}
		} catch (Exception e) {
			System.err.println("[writeOutputDataToConfDataDB] Error in writing to ConfidenceDataDB table!");
			e.printStackTrace();
			return ReturnCode.ERROR;
		}
		return ReturnCode.SUCCESS;
	}

	private String getBinNumber(float confidence) {
		int bin = 0;
		for (int i = 10;i <= 100;i += 10) {
			if (i > (confidence * 100)) {
				return Integer.toString(bin);
			}
			++bin;
		}
		return Integer.toString(bin-1);
	}

	/**
	 * This method is the 'producer' - producing statistics data to be written to the respective tables
	 * 
	 * @param subscriptionPattern REDIS pattern that Jedis is subscribed to
	 * @param channelName Name of the channel on which message received from REDIS
	 * @param receivedMessage Received message for the given channelName
	 */
	@Override
	public void manageChannelBuffersWrapper(final String subscriptionPattern, final String channelName, final String receivedMessage) {
		//System.out.println("Firing manageChannelBuffer on message from channel: " + channelName);
		manageChannelBuffers(subscriptionPattern, channelName, receivedMessage);
		if (null == channelName) {
			logger.error("Something terribly wrong! Fatal error in: " + channelName);
		} else {
			try {
				ClassifiedFilteredTweet classifiedTweet = new ClassifiedFilteredTweet().deserialize(receivedMessage);
				if (classifiedTweet != null && classifiedTweet.getNominalLabels() != null) {
					channelMap.putIfAbsent(classifiedTweet.getCrisisCode(), System.currentTimeMillis());
	
					for (NominalLabel nb: classifiedTweet.getNominalLabels()) {
						StringBuffer keyPrefix = new StringBuffer()
												.append(classifiedTweet.getCrisisCode()).append(SENTINEL)
												.append(nb.attribute_code).append(SENTINEL)
												.append(nb.label_code);
						String tagDataKey = new String(keyPrefix);
						String confDataKey = new String(keyPrefix.append(SENTINEL).append(getBinNumber(nb.confidence)));
						if (tagDataMap.containsKey(tagDataKey)) {
							TagDataMapRecord t = (TagDataMapRecord) tagDataMap.get(tagDataKey);
							t.incrementAllCounts();
							tagDataMap.put(tagDataKey, t);
						} else {
							TagDataMapRecord t = new TagDataMapRecord(granularityList);
							tagDataMap.put(tagDataKey, t);
							System.out.println("[manageChannelBuffersWrapper] New Tag map entry with key: " + tagDataKey + " value = " + tagDataMap.get(tagDataKey));
						}
						if (confDataMap.containsKey(confDataKey)) {
							ConfDataMapRecord f = (ConfDataMapRecord) confDataMap.get(confDataKey);
							f.incrementAllCounts();
							confDataMap.put(confDataKey, f);
						} else {
							ConfDataMapRecord t = new ConfDataMapRecord(granularityList);
							confDataMap.put(confDataKey, t);
							System.out.println("[manageChannelBuffersWrapper] New Conf map entry with key: " + confDataKey  + " value = " + confDataMap.get(confDataKey));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Periodically check if any channel is down - if so, delete all in-memory data for that channel
		lastTagDataCheckedTime = periodicInactiveChannelCheck(lastTagDataCheckedTime, tagDataMap);
		lastConfDataCheckedTime = periodicInactiveChannelCheck(lastConfDataCheckedTime, confDataMap);
	}

	private long periodicInactiveChannelCheck(long lastCheckedTime, ConcurrentHashMap<String, Object> dataMap) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastCheckedTime > CHECK_INTERVAL) {
			for (String key: channelMap.keySet()) {
				if ((currentTime - channelMap.get(key)) > NO_DATA_TIMEOUT) {
					System.out.println("[periodicInactiveChannelCheck] Deleting data for inactive channel = " + key);
					int deleteCount = deleteMapRecords(key, dataMap);
					System.out.println("[periodicInactiveChannelCheck] Deleted records count for inactive channel <" + key + "> is = " + deleteCount);
				}
			}
		}
		return currentTime;
	}
	
	private int deleteMapRecords(final String key, ConcurrentHashMap<String, Object> dataMap) {
		int count = 0;
		for (String keyVal: dataMap.keySet()) {
			MapRecord data = (MapRecord) dataMap.get(key);
			synchronized(dataMap) {
				if (keyVal.contains(key) && data != null && data.isCountZeroForAllGranularity()) {
					dataMap.remove(keyVal);
					++count;
				}
			}
		}
		return count;
	}
	
	
	private class WriterThread implements Runnable {

		/**
		 * The 'consumer' method - consumes the statistics data produced by writing to respective tables in DB
		 */
		@Override
		public void run() {
			System.out.println("Started writer thread: " + t.getName());
			Map<Long, Long> lastTagDataWriteTime = new TreeMap<Long, Long>();
			Map<Long, Long> lastConfDataWriteTime = new TreeMap<Long, Long>();
			for (Long g: granularityList) {
				lastTagDataWriteTime.put(g, 0L);
				lastConfDataWriteTime.put(g, 0L);
			}
			while (runFlag) {
				long currentTime = System.currentTimeMillis();
				if (granularityList != null) {
					for (Long granularity: granularityList) {
						if (0 == lastTagDataWriteTime.get(granularity) || (currentTime - lastTagDataWriteTime.get(granularity)) >= granularity) {
							// Write to DB table
							ReturnCode retVal = writeOutputDataToTagDataDB(granularity, currentTime);	
							lastTagDataWriteTime.put(granularity, currentTime);
							System.out.println("[ToTagDataDB] retVal = " + retVal);
							if (ReturnCode.SUCCESS.equals(retVal)) {
								System.out.println("[ToTagDataDB] Successfully wrote for granularity: " + granularity + " at time = " + new Date(currentTime));
							}
						}
						if (0 == lastConfDataWriteTime.get(granularity) || (currentTime - lastConfDataWriteTime.get(granularity)) >= granularity) {
							// Write to DB table
							ReturnCode retVal = writeOutputDataToConfDataDB(granularity, currentTime);
							System.out.println("[ToConfDataDB] retVal = " + retVal);
							lastConfDataWriteTime.put(granularity, currentTime);
							if (ReturnCode.SUCCESS.equals(retVal)) {
								System.out.println("[ToConfDataDB] Successfully wrote for granularity: " + granularity + " at time = " + new Date(currentTime));
							}
						}
					}
					try {
						Thread.sleep(granularityList.get(0)-1000);	// sleep for the minimum granularity period
					} catch (InterruptedException e) {}
				}
			}
			System.out.println("Done work - exiting thread: " + t.getName());
		}
	}

}
