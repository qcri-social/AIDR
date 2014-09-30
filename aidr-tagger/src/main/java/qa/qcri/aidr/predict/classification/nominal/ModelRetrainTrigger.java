package qa.qcri.aidr.predict.classification.nominal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import qa.qcri.aidr.predict.DataStore;
import qa.qcri.aidr.predict.classification.nominal.CrisisAttributePair;
import qa.qcri.aidr.predict.common.Config;
import qa.qcri.aidr.predict.common.Event;
import qa.qcri.aidr.predict.common.Loggable;
import qa.qcri.aidr.predict.dbentities.ModelFamilyEC;

import static qa.qcri.aidr.predict.common.ConfigProperties.getProperty;

/**
 * ModelRetrainTrigger gets notified of new training samples through a Redis
 * queue. When a sufficient number of samples have arrived, it throws an event
 * triggering rebuilding of the relevant model.
 * 
 * @author jrogstadius
 */
class ModelRetrainTrigger extends Loggable implements Runnable {

    public Event<CrisisAttributePair> onRetrain = new Event<CrisisAttributePair>();
    int timeThreshold = 60000; // 1000*60*10; //TODO: Model re-training
                               // threshold should be dynamic
    HashMap<Integer, HashMap<Integer, Integer>> newSampleCounts = new HashMap<Integer, HashMap<Integer, Integer>>();
    HashMap<Integer, HashMap<Integer, Long>> rebuildTimestamps = new HashMap<Integer, HashMap<Integer, Long>>();
    HashMap<Integer, List<Integer>> forceRetrains = new HashMap<Integer, List<Integer>>();

    public void run() {
        while (true) {
            parseTrainingSamples();
            checkRetrainThresholds();
            forceRetrains.clear();
            try {
				Thread.sleep(10000);	// sleep for 10sec before next attempt
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
        }
    }

    public void initialize(ArrayList<ModelFamilyEC> modelStates) {
        for (ModelFamilyEC m : modelStates) {
            increment(m.getCrisisID(), new int[] { m.getNominalAttribute().getNominalAttributeID()},
                    m.getTrainingExampleCount() % Integer.parseInt(getProperty("sampleCountThreshold")));
        }
    }

    private int parseTrainingSamples() {
        Jedis redis = DataStore.getJedisConnection();
        int newSampleCount = 0;

        String line = null;
        long consumptionStart = new Date().getTime();
        try {
            while ((line = getInfoMessage(redis)) != null
                    && new Date().getTime() - consumptionStart < timeThreshold) {

                log(Loggable.LogLevel.INFO, "A training sample has arrived");

                // Parse notification containing event id and attribute ids
                JSONObject obj = new JSONObject(line);
                int crisisID = obj.getInt("crisis_id");
                JSONArray attrArr = obj.getJSONArray("attributes");
                int[] attributeIDs = new int[attrArr.length()];
                for (int i = 0; i < attrArr.length(); i++) {
                    attributeIDs[i] = attrArr.getInt(i);
                }
                increment(crisisID, attributeIDs, 1);
                if (obj.has("force_retrain") && obj.getBoolean("force_retrain")) {
                    if (!forceRetrains.containsKey(crisisID)) {
                        forceRetrains.put(crisisID, new ArrayList<Integer>());
                    }
                    for (int attributeID : attributeIDs) {
                        forceRetrains.get(crisisID).add(attributeID);
                    }
                }

                newSampleCount++;
            }
        } catch (Exception e) {
            log("Exception while processing training sample message queue", e);
        } finally {
            DataStore.close(redis);
        }

        return newSampleCount;
    }

    private String getInfoMessage(Jedis redis) {
        List<String> result = redis.blpop(5,
                getProperty("redis_training_sample_info_queue"));
        if (result == null || result.size() != 2) {
            return null; // Result is null on timeout, size should always be 2
        }
        return result.get(1);
    }

    private void increment(int crisisID, int[] attributeIDs, int incrementValue) {
        for (int id : attributeIDs) {
            if (!newSampleCounts.containsKey(crisisID)) {
                newSampleCounts.put(crisisID, new HashMap<Integer, Integer>());
                rebuildTimestamps.put(crisisID, new HashMap<Integer, Long>());
            }

            if (!newSampleCounts.get(crisisID).containsKey(id)) {
                newSampleCounts.get(crisisID).put(id, incrementValue);
                rebuildTimestamps.get(crisisID).put(id, (long) 0);
            } else {
                newSampleCounts.get(crisisID).put(id,
                        newSampleCounts.get(crisisID).get(id) + incrementValue);
            }
        }
    }

    private void checkRetrainThresholds() {
        // For each event and ontology where there are enough new training
        // samples, and it was long enough since the last model was built,
        // retrain
        for (int crisisID : newSampleCounts.keySet()) {
            HashMap<Integer, Integer> eventMap = newSampleCounts.get(crisisID);
            for (int attributeID : eventMap.keySet()) {
                long now = new Date().getTime();
                if (eventMap.get(attributeID) >= Integer.parseInt(getProperty("sampleCountThreshold"))
                        && (now - rebuildTimestamps.get(crisisID).get(
                                attributeID)) >= timeThreshold) {

                    retrain(crisisID, attributeID);
                } else {
                    if (forceRetrains.containsKey(crisisID)
                            && forceRetrains.get(crisisID)
                                    .contains(attributeID)) {
                        retrain(crisisID, attributeID);
                    }
                }
            }
        }
    }

    private void retrain(int crisisID, int attributeID) {
        log(Loggable.LogLevel.INFO, "Time to retrain model for " + crisisID
                + " attribute " + attributeID);
        onRetrain.fire(this, new CrisisAttributePair(crisisID, attributeID));

        newSampleCounts.get(crisisID).put(attributeID, 0);
        rebuildTimestamps.get(crisisID).put(attributeID, new Date().getTime());
    }
}
