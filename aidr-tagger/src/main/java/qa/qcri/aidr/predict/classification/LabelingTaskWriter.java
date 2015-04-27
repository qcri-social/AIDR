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

	private long lastDBWrite = 0;
	private long lastTruncateTime = 0;
	public long writeCount = 0;
	ArrayList<Document> writeBuffer = new ArrayList<Document>();

	// Maintain a hash table of currently seen <key> = crisisIDs that are
	// being saved to the DB, with <value> being the number of items per crisisID
	// being saved to the DB. 
	static Map<Integer, Long> activeCrisisIDList = new HashMap<Integer, Long>();	
	static Map<Integer, Double> activeCrisisMaxConf = new HashMap<Integer, Double>();
	static Map<Integer, Long> crisisLastTruncateTime = new HashMap<Integer, Long>();
	
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
		Long currentCrisisIDItemCount = activeCrisisIDList.containsKey(item.getCrisisID().intValue()) ? 
											activeCrisisIDList.get(item.getCrisisID().intValue()) : 0L;
		activeCrisisIDList.put(item.getCrisisID().intValue(), ++currentCrisisIDItemCount);
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
		DataStore.saveDocumentsToDatabase(writeBuffer);
		writeCount += writeBuffer.size(); 
		writeBuffer.clear();

		if (!isTruncateRunLimited() || 0 == lastDBWrite) {
			for (Integer crisisID: activeCrisisIDList.keySet()) {
				logger.info("Looking at possible truncation for crisisID = " + crisisID 
							+ "last save count = " + activeCrisisIDList.get(crisisID) 
							+ " [" + Integer.parseInt(getProperty("max_new_tasks_per_minute")) + "]");
				if (!isTruncateRateLimited(crisisID) || 
						activeCrisisIDList.get(crisisID) > Integer.parseInt(getProperty("max_new_tasks_per_minute"))) {

					logger.info("Going to truncate for crisisID = " + crisisID 
								+ " [" + activeCrisisIDList.get(crisisID) + "] new docs");
					DataStore
					.truncateLabelingTaskBufferForCrisis(crisisID, Integer.parseInt(getProperty("labeling_task_buffer_max_length")));
					activeCrisisIDList.put(crisisID, 0L);	// reset count for next interval
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

	boolean isTruncateRateLimited(int crisisID) {
		Long lastTruncateTime = crisisLastTruncateTime.containsKey(crisisID) ? crisisLastTruncateTime.get(crisisID) : 0L;
		boolean result = (System.currentTimeMillis() - lastTruncateTime) < Integer.parseInt(getProperty("min_truncate_interval_ms"));
		if (!result) crisisLastTruncateTime.put(crisisID, System.currentTimeMillis());
		return result;
	}
	
	boolean isTruncateRunLimited() {
		boolean result = (System.currentTimeMillis() - lastTruncateTime) < Long.parseLong(getProperty("truncate_run_interval_ms"));
		if (!result) lastTruncateTime = System.currentTimeMillis();
		return result;
	}
}
