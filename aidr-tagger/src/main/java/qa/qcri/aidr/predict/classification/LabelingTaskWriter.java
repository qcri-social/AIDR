package qa.qcri.aidr.predict.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predict.DataStore;
import qa.qcri.aidr.predict.common.PipelineProcess;
import qa.qcri.aidr.predict.common.RateLimiter;
import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.featureextraction.WordSet;
import static qa.qcri.aidr.predict.common.ConfigProperties.getProperty;

/**
 * LabelingTaskWriter consumes fully classified items and writes them to the
 * task buffer in the database for human annotation. If more documents are
 * available than what can reasonably be processed by humans, the documents are
 * discarded.
 * 
 * @author jrogstadius
 */
public class LabelingTaskWriter extends PipelineProcess {
	private static Logger logger = Logger.getLogger(LabelingTaskWriter.class);
	private static ErrorLog elog = new ErrorLog();
	
	class DocumentHistory {
		LinkedList<WordSet> recentWordVectors = new LinkedList<WordSet>();
		int bufferSize = 50;

		public boolean addItemIfNovel(Document doc) {
			WordSet w1 = doc.getFeatures(WordSet.class).get(0);

			double maxSim = 0;
			for (WordSet w2 : recentWordVectors) {
				double sim = w2.getSimilarity(w1);
				if (sim > maxSim) {
					if (sim > 0.5) // TODO: This threshold needs some tuning,
						// probably
						return false;
					maxSim = sim;
				}
			}

			recentWordVectors.add(w1);
			if (recentWordVectors.size() > bufferSize)
				recentWordVectors.remove();

			return true;
		}
	}

	long lastDBWrite = 0;
	long lastTruncateTime = 0;
	public long writeCount = 0;
	ArrayList<Document> writeBuffer = new ArrayList<Document>();

	// Maintain a hash table of currently seen <key> = crisisIDs that are
	// being saved to the DB, with <value> being the number of items per crisisID
	// being saved to the DB. 
	Map<Integer, Long> activeCrisisIDList = new HashMap<Integer, Long>();	
	Map<Integer, Double>activeCrisisMaxConf = new HashMap<Integer, Double>();

	RateLimiter taskRateLimiter = new RateLimiter(
            Integer.parseInt(getProperty("max_new_tasks_per_minute")));
	DocumentHistory history = new DocumentHistory();

	protected void processItem(Document item) {
		// Write novel DocumentSets to the database at a maximum rate of up to N
		// items per minute
		if (item.hasHumanLabels()
				|| (!taskRateLimiter.isLimited() && item.isNovel() && history
						.addItemIfNovel(item))) {
			// log(LogLevel.INFO, "LabelingTaskWriter recieved an item");
			//if (item.getValueAsTrainingSample() < activeCrisisMaxConf.get(item.getCrisisID())) 
			{
				//activeCrisisMaxConf.put(item.getCrisisID(), item.getValueAsTrainingSample());
				save(item);
				taskRateLimiter.logEvent();
			}

		}
	}

	void save(Document item) {
		writeBuffer.add(item);
		Long currentCrisisIDItemCount =  
				activeCrisisIDList.containsKey(item.getCrisisID()) ? 
						activeCrisisIDList.get(item.getCrisisID()) : 0L;
						activeCrisisIDList.put(item.getCrisisID().intValue(), currentCrisisIDItemCount+1);
						if (!isWriteRateLimited()) {
							writeToDB();
						}
	}

	@Override
	protected void idle() {
		if (writeBuffer.size() > 0)
			writeToDB();
	}

	void writeToDB() {
		// log(LogLevel.INFO, "Writing " + writeBuffer.size() +
		// " tasks/labeled items from pipeline to DB");
		/*
		try {
			if (null == DataStore.taskManager) DataStore.initTaskManager();
		} catch (Exception e) {
			logger.error("Unable to initialize taskManager: " + DataStore.taskManager);
			logger.error(elog.toStringException(e));
		}*/
		
		
		DataStore.saveDocumentsToDatabase(writeBuffer);
		writeCount += writeBuffer.size(); 
		writeBuffer.clear();
		//DataStore
		//        .truncateLabelingTaskBuffer(Config.LABELING_TASK_BUFFER_MAX_LENGTH);
		if (!isTruncateRunLimited()) {
			for (Iterator<Map.Entry<Integer, Long>> it = activeCrisisIDList.entrySet().iterator();it.hasNext(); ) {
				Map.Entry<Integer, Long> entry = it.next();
				int crisisID = entry.getKey();
				if (!isTruncateRateLimited() || 
						activeCrisisIDList.get(crisisID) > Integer.parseInt(getProperty("max_new_tasks_per_minute"))) {
					System.out.println("[writeToDB] Going to truncate for crisisID = " + crisisID + " [" + activeCrisisIDList.get(crisisID) + "] new docs");
					logger.info("Going to truncate for crisisID = " + crisisID + " [" + activeCrisisIDList.get(crisisID) + "] new docs");
					DataStore
					.truncateLabelingTaskBufferForCrisis(crisisID, Integer.parseInt(getProperty("labeling_task_buffer_max_length")));
					lastTruncateTime = System.currentTimeMillis();
					it.remove();
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {}
				}
			}
		}
		lastDBWrite = System.currentTimeMillis();
	}


	boolean isWriteRateLimited() {
		return (System.currentTimeMillis() - lastDBWrite) < Integer.parseInt(getProperty("max_task_write_fq_ms"));
	}

	boolean isTruncateRateLimited() {
		return (System.currentTimeMillis() - lastTruncateTime) < Integer.parseInt(getProperty("min_truncate_interval_ms"));
	}
	
	boolean isTruncateRunLimited() {
		return (System.currentTimeMillis() - lastTruncateTime) < Long.parseLong(getProperty("truncate_run_interval_ms"));
	}
}
