package qa.qcri.aidr.analysis.api;


import java.util.Date;
import java.util.Iterator;
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
import qa.qcri.aidr.analysis.stat.ConfDataMapRecord;
import qa.qcri.aidr.analysis.stat.MapRecord;
import qa.qcri.aidr.analysis.stat.TagDataMapRecord;
import qa.qcri.aidr.analysis.utils.AnalyticsConfigurator;
import qa.qcri.aidr.analysis.utils.ConfCounterKey;
import qa.qcri.aidr.analysis.utils.CounterKey;
import qa.qcri.aidr.analysis.facade.ConfidenceStatisticsResourceFacade;
import qa.qcri.aidr.analysis.facade.TagDataStatisticsResourceFacade;
import qa.qcri.aidr.common.values.ReturnCode;
import qa.qcri.aidr.common.filter.ClassifiedFilteredTweet;
import qa.qcri.aidr.common.filter.NominalLabel;
import qa.qcri.aidr.output.getdata.ChannelBufferManager;


/**
 * This class provides an interface to automatically start/stop/manage the analytics DB writer service at deployment time
 * to monitor REDIS channels for data to store statistical data into the aidr analytics DB.
 * 
 * @author: koushik
 */

@Path("/save/")
public class WriteStatisticsData extends ChannelBufferManager implements
ServletContextListener {

	// Debugging
	private static Logger logger = Logger.getLogger(WriteStatisticsData.class.getSuperclass());

	private volatile boolean runFlag = false;

	@EJB
	private TagDataStatisticsResourceFacade tagDataEJB;

	@EJB
	private ConfidenceStatisticsResourceFacade confDataEJB;

	private ExecutorService executorServicePool = null;
	private Thread t = null;

	//private static final String SENTINEL = "#";
	private static ConcurrentHashMap<CounterKey, Object> tagDataMap = null;
	private static ConcurrentHashMap<CounterKey, Object> confDataMap = null;
	private static ConcurrentHashMap<String, Long> channelMap = null;
	private List<Long> granularityList = null;
	private long lastTagDataCheckedTime = 0;
	private long lastConfDataCheckedTime = 0;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Initializing channel buffer manager");

		tagDataMap = new ConcurrentHashMap<CounterKey, Object>();
		confDataMap = new ConcurrentHashMap<CounterKey, Object>();
		channelMap = new ConcurrentHashMap<String, Long>();

		AnalyticsConfigurator configurator = AnalyticsConfigurator.getInstance();
		granularityList = configurator.getGranularities();

		initiateChannelBufferManager(GetStatistics.CHANNEL_REG_EX);

		runFlag = true;
		t = new Thread(new WriterThread());
		t.setName("AIDR-Analysis DB Writer Thread");
		executorServicePool = getExecutorServicePool();
		if (executorServicePool != null) {
			executorServicePool.submit(t);
		}
		logger.info("Done initializing channel buffer manager");

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
		JsonObject obj = Json.createObjectBuilder().add("aidr-analysis/writeData", "RUNNING").build();

		return Response.ok(obj).build();
	}

	private ReturnCode writeOutputDataToTagDataDB(Long granularity,Long timestamp) {
		// logger.info("tagDataMap size: " + (tagDataMap != null ?
		// tagDataMap.size() : "null"));
		try {
			for (CounterKey key : tagDataMap.keySet()) {
				// String[] data = key.split(SENTINEL);
				// data[0] = channelName
				// data[1] = attributeCode
				// data[2] = labelCode
				TagDataMapRecord tCount = (TagDataMapRecord) tagDataMap.get(key);
				TagData t = new TagData(key.getCrisisCode(), timestamp, granularity, key.getAttributeCode(), key.getLabelCode(), tCount.getCount(granularity));
				logger.info("Will attempt to persist data for: " + t.getCrisisCode() + ", " + t.getAttributeCode() 
								+ ", " + t.getLabelCode() + ", " + t.getTimestamp() + ", " + t.getGranularity() + ": " + t.getCount());
				tagDataEJB.writeData(t);
				tCount.resetCount(granularity);
				// TagDataMapRecord temp = (TagDataMapRecord) tagDataMap.get(key);
				// logger.info("[writeOutputDataToTagDataDB] After reset, count for key = "
				// + key + " is = " + temp.getCount(granularity));

			}
		} catch (Exception e) {
			logger.error("Error in writing to TagDataDB table!", e);
			return ReturnCode.ERROR;
		}
		return ReturnCode.SUCCESS;
	}

	private ReturnCode writeOutputDataToConfDataDB(Long granularity, Long timestamp) {
		try {
			for (CounterKey key : confDataMap.keySet()) {
				// String[] data = key.split(SENTINEL);
				// data[0] = channelName
				// data[1] = attributeCode
				// data[2] = labelCode
				// data[3] = bin
				ConfDataMapRecord fCount = (ConfDataMapRecord) confDataMap.get(key);
				ConfidenceData f = new ConfidenceData(key.getCrisisCode(), timestamp, granularity, key.getAttributeCode(), key.getLabelCode(),
														Integer.parseInt(((ConfCounterKey) key).getBinNumber()), fCount.getCount(granularity));
				confDataEJB.writeData(f);
				fCount.resetCount(granularity);
				// ConfDataMapRecord temp = (ConfDataMapRecord) confDataMap.get(key);
				// logger.info("After reset, count for key = " + key + " is = " + temp.getCount(granularity));
			}
		} catch (Exception e) {
			logger.error("Error in writing to ConfidenceDataDB table!", e);
			return ReturnCode.ERROR;
		}
		return ReturnCode.SUCCESS;
	}

	private String getBinNumber(float confidence) {
		int bin = 0;
		for (int i = 10; i <= 100; i += 10) {
			if (i > (confidence * 100)) {
				return Integer.toString(bin);
			}
			++bin;
		}
		return Integer.toString(bin - 1);
	}

	/**
	 * This method is the 'producer' - producing statistics data to be written
	 * to the respective tables
	 * 
	 * @param subscriptionPattern
	 *            REDIS pattern that Jedis is subscribed to
	 * @param channelName
	 *  		  Name of the channel on which message received from REDIS
	 * @param receivedMessage
	 *            Received message for the given channelName
	 */
	@Override
	public void manageChannelBuffersWrapper(final String subscriptionPattern, final String channelName, final String receivedMessage) {
		// logger.info("Firing manageChannelBuffer on message from channel: " + channelName);
		manageChannelBuffers(subscriptionPattern, channelName, receivedMessage);
		if (null == channelName) {
			logger.error("Something terribly wrong! Fatal error in: " + channelName);
		} else {
			try {
				ClassifiedFilteredTweet classifiedTweet = new ClassifiedFilteredTweet().deserialize(receivedMessage);
				if (classifiedTweet != null && classifiedTweet.getNominalLabels() != null) {
					channelMap.putIfAbsent(classifiedTweet.getCrisisCode(), System.currentTimeMillis());

					for (NominalLabel nb : classifiedTweet.getNominalLabels()) {
						/*
						StringBuffer keyPrefix = new StringBuffer()
								.append(classifiedTweet.getCrisisCode())
								.append(SENTINEL).append(nb.attribute_code)
								.append(SENTINEL).append(nb.label_code);
						String tagDataKey = new String(keyPrefix);
						String confDataKey = new String(keyPrefix.append(
								SENTINEL).append(getBinNumber(nb.confidence)));
						 */
						CounterKey tagDataKey = new CounterKey(classifiedTweet.getCrisisCode(), nb.attribute_code, nb.label_code);
						CounterKey confDataKey = new ConfCounterKey(classifiedTweet.getCrisisCode(), nb.attribute_code, nb.label_code, getBinNumber(nb.confidence));

						if (tagDataMap.containsKey(tagDataKey)) {
							TagDataMapRecord t = (TagDataMapRecord) tagDataMap.get(tagDataKey);
							t.incrementAllCounts();
							tagDataMap.put(tagDataKey, t);
						} else {
							TagDataMapRecord t = new TagDataMapRecord(granularityList);
							tagDataMap.put(tagDataKey, t);
							logger.info("New Tag map entry with key: " + tagDataKey + " value = " + tagDataMap.get(tagDataKey));
						}
						if (confDataMap.containsKey(confDataKey)) {
							ConfDataMapRecord f = (ConfDataMapRecord) confDataMap.get(confDataKey);
							f.incrementAllCounts();
							confDataMap.put(confDataKey, f);
						} else {
							ConfDataMapRecord t = new ConfDataMapRecord(granularityList);
							confDataMap.put(confDataKey, t);
							logger.info("[manageChannelBuffersWrapper] New Conf map entry with key: " + confDataKey + " value = " + confDataMap.get(confDataKey));
						}
					}
				}
			} catch (Exception e) {
				logger.error("Exception", e);
			}
		}
		// Periodically check if any channel is down - if so, delete all
		// in-memory data for that channel
		lastTagDataCheckedTime = periodicInactiveChannelCheck(lastTagDataCheckedTime, tagDataMap);
		lastConfDataCheckedTime = periodicInactiveChannelCheck(lastConfDataCheckedTime, confDataMap);
	}

	private long periodicInactiveChannelCheck(long lastCheckedTime, ConcurrentHashMap<CounterKey, Object> dataMap) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastCheckedTime > CHECK_INTERVAL) {
			for (String key : channelMap.keySet()) {
				if ((currentTime - channelMap.get(key)) > NO_DATA_TIMEOUT) {
					logger.info("Deleting data for inactive channel = " + key);
					int deleteCount = deleteMapRecordsForCollection(key, dataMap);
					logger.info("Deleted records count for inactive channel <" + key + "> is = " + deleteCount);
				}
			}
		}
		return currentTime;
	}

	private int deleteMapRecordsForCollection(final String deleteCollectionCode, ConcurrentHashMap<CounterKey, Object> dataMap) {
		int count = 0;
		Iterator<CounterKey> itr = dataMap.keySet().iterator();
		while (itr.hasNext()) {
			CounterKey keyVal = itr.next();
			MapRecord data = (MapRecord) dataMap.get(keyVal);
			if (keyVal.getCrisisCode().equals(deleteCollectionCode) && data != null && data.isCountZeroForAllGranularity()) {
				synchronized (dataMap) {		// prevent modification while deletion attempt
					dataMap.remove(keyVal);
					++count;
				}
			}
		}
		return count;
	}

	private class WriterThread implements Runnable {

		/**
		 * The 'consumer' method - consumes the statistics data produced by
		 * writing to respective tables in DB
		 */
		@Override
		public void run() {
			logger.info("Started aidr-analytics writer thread: " + t.getName());
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
							logger.info("retVal = " + retVal);
							if (ReturnCode.SUCCESS.equals(retVal)) {
								logger.info("Successfully wrote for granularity: " + granularity + " at time = " + new Date(currentTime));
							}
						}
						if (0 == lastConfDataWriteTime.get(granularity) || (currentTime - lastConfDataWriteTime.get(granularity)) >= granularity) {
							// Write to DB table
							ReturnCode retVal = writeOutputDataToConfDataDB(granularity, currentTime);
							logger.info("retVal = " + retVal);
							lastConfDataWriteTime.put(granularity, currentTime);
							if (ReturnCode.SUCCESS.equals(retVal)) {
								logger.info("Successfully wrote for granularity: " + granularity + " at time = " + new Date(currentTime));
							}
						}
					}
					try {
						Thread.sleep(granularityList.get(0) - 1000); // sleep for the minimum granularity period
					} catch (InterruptedException e) {
					}
				}
			}
			//logger.info("Done work - exiting thread: " + t.getName());
		}
	}

}
