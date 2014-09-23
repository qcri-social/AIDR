package qa.qcri.aidr.analysis.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import qa.qcri.aidr.analysis.entity.FrequencyData;
import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.analysis.stat.*;
import qa.qcri.aidr.analysis.utils.GranularityData;
import qa.qcri.aidr.analysis.facade.FrequencyStatisticsResourceFacade;
import qa.qcri.aidr.analysis.facade.TagDataStatisticsResourceFacade;
import qa.qcri.aidr.common.code.DateFormatConfig;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.common.values.ReturnCode;
import qa.qcri.aidr.output.filter.ClassifiedFilteredTweet;
import qa.qcri.aidr.output.filter.NominalLabel;
import qa.qcri.aidr.output.getdata.ChannelBufferManager;

@Path("/save/")
public class WriteStatisticsData extends ChannelBufferManager implements ServletContextListener {

	// Debugging
	private static Logger logger = Logger.getLogger(WriteStatisticsData.class);
	private static ErrorLog elog = new ErrorLog();

	private volatile boolean runFlag = false;

	@EJB
	private TagDataStatisticsResourceFacade tagDataEJB;

	@EJB
	private FrequencyStatisticsResourceFacade freqDataEJB;

	private static ConcurrentHashMap<String, Object> tagDataMap = null;
	private static ConcurrentHashMap<String, Object> freqDataMap = null;

	private Thread t = null;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Initializing channel buffer manager");
		System.out.println("[contextInitialized] Initializing channel buffer manager");
		tagDataMap = new ConcurrentHashMap<String, Object>();
		freqDataMap = new ConcurrentHashMap<String, Object>();
		initiateChannelBufferManager(GetStatistics.CHANNEL_REG_EX);

		runFlag = true;
		t = new Thread(new WriterThread());
		t.setName("AIDR-Analysis DB Writer Thread");
		t.start();

		logger.info("Done initializing channel buffer manager");
		System.out.println("[contextInitialized] Done initializing channel buffer manager");
		logger.info("Context Initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		tagDataMap.clear();
		freqDataMap.clear();
		runFlag = false;
		super.close();
		if (t.isAlive())
			try {
				t.join();
			} catch (InterruptedException e) {
				System.out.println("Thread join interrupted for thread: " + t.getName());
				e.printStackTrace();
			}

		logger.info("Context destroyed");
	}

	@POST
	@Path("/tagData")
	public Map<String, Object> writeDataToTagDataDB(TagData tagData) {
		Map<String, Object> response = new HashMap<String, Object>();
		ReturnCode retVal = tagDataEJB.writeData(tagData);
		response.put("code", retVal);
		return response;
	}

	@POST
	@Path("/freqData")
	public Map<String, Object> writeDataToFreqDataDB(FrequencyData freqData) {
		Map<String, Object> response = new HashMap<String, Object>();
		ReturnCode retVal = freqDataEJB.writeData(freqData);
		response.put("code", retVal);
		return response;
	}

	private ReturnCode writeOutputDataToTagDataDB(Long granularity, Long timestamp) {
		TreeMap<String, Object> tempMap = new TreeMap<String, Object>();
		try {
			tempMap.putAll(tagDataMap);
			for (String key: tempMap.keySet()) {
				String[] data = key.split("_");
				// data[0] = channelName
				// data[1] = attributeCode
				// data[2] = labelCode
				TagData t = new TagData(data[0], timestamp, granularity, data[1], data[2], ((TagDataMapRecord) tempMap.get(key)).getCount());
				tagDataEJB.writeData(t);	
			}
		} catch (Exception e) {
			System.err.println("[writeOutputDataToTagDataDB] Error in writing to TagDataDB table!");
			return ReturnCode.FAIL;
		}
		return ReturnCode.SUCCESS;
	}

	private ReturnCode writeOutputDataToFreqDataDB(Long granularity, Long timestamp) {
		TreeMap<String, Object> tempMap = new TreeMap<String, Object>();
		try {
			tempMap.putAll(tagDataMap);
			for (String key: tempMap.keySet()) {
				String[] data = key.split("_");
				// data[0] = channelName
				// data[1] = attributeCode
				// data[2] = labelCode
				// data[3] = bin
				FrequencyData t = new FrequencyData(data[0], timestamp, granularity, data[1], data[2], Integer.parseInt(data[3]), ((FreqDataMapRecord) tempMap.get(key)).getCount());
				freqDataEJB.writeData(t);	
			}
		} catch (Exception e) {
			System.err.println("[writeOutputDataToFreqDataDB] Error in writing to FrequencyDataDB table!");
			return ReturnCode.FAIL;
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
	public void manageChannelBuffers(final String subscriptionPattern, final String channelName, final String receivedMessage) {
		System.out.println("Firing manageChannelBuffer on message from channel: " + channelName);
		if (null == channelName) {
			logger.error("Something terribly wrong! Fatal error in: " + channelName);
		} else {
			ClassifiedFilteredTweet classifiedTweet = new ClassifiedFilteredTweet().deserialize(receivedMessage);
			if (classifiedTweet.getNominalLabels() != null) {
				for (NominalLabel nb: classifiedTweet.getNominalLabels()) {
					String keyPrefix = channelName + "_" + nb.attribute_code + "_" + nb.label_code;
					String tagDataKey = keyPrefix;
					String freqDataKey = keyPrefix + "_" + getBinNumber(nb.confidence);
					if (tagDataMap.contains(tagDataKey)) {
						tagDataMap.put(tagDataKey, ((TagDataMapRecord) tagDataMap.get(tagDataKey)).getCount()+1);
					} else {
						TagDataMapRecord t = new TagDataMapRecord();
						tagDataMap.put(tagDataKey, t);
					}
					if (freqDataMap.contains(freqDataKey)) {
						freqDataMap.put(freqDataKey, ((FreqDataMapRecord) freqDataMap.get(freqDataKey)).getCount()+1);
					} else {
						FreqDataMapRecord t = new FreqDataMapRecord();
						freqDataMap.put(freqDataKey, t);
					}
				}
			}
		}
	}

	private class WriterThread implements Runnable {

		/**
		 * The 'consumer' method - consumes the statistics data produced by writing to respective tables in DB
		 */
		@Override
		public void run() {
			System.out.println("Started writer thread: " + t.getName());
			List<Long> granularityList = GranularityData.getGranularities();
			Map<Long, Long> lastTagDataWriteTime = new TreeMap<Long, Long>();
			Map<Long, Long> lastFreqDataWriteTime = new TreeMap<Long, Long>();
			for (Long g: granularityList) {
				lastTagDataWriteTime.put(g, 0L);
				lastFreqDataWriteTime.put(g, 0L);
			}
			while (runFlag) {
				long currentTime = System.currentTimeMillis();
				if (granularityList != null) {
					for (Long granularity: granularityList) {
						if (0 == lastTagDataWriteTime.get(granularity) || (currentTime - lastTagDataWriteTime.get(granularity)) >= granularity) {
							// Write to DB table
							ReturnCode retVal = writeOutputDataToTagDataDB(granularity, currentTime);	
							lastTagDataWriteTime.put(granularity, currentTime);
						}
						if (0 == lastFreqDataWriteTime.get(granularity) || (currentTime - lastFreqDataWriteTime.get(granularity)) >= granularity) {
							// Write to DB table
							ReturnCode retVal = writeOutputDataToFreqDataDB(granularity, currentTime);
							lastFreqDataWriteTime.put(granularity, currentTime);
						}
					}
					tagDataMap.clear();
					freqDataMap.clear();
					try {
						Thread.sleep(granularityList.get(0)-100);
					} catch (InterruptedException e) {}
				}
			}
			System.out.println("Done work - exiting thread: " + t.getName());
		}
	}

}
