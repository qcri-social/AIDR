package qa.qcri.aidr.predict.communication;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import qa.qcri.aidr.predict.DataStore;
import qa.qcri.aidr.predict.common.PipelineProcess;
import qa.qcri.aidr.predict.common.Serializer;
import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;
import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.data.DocumentJSONConverter;
import redis.clients.jedis.Jedis;

/**
 * OutputMatcher listens to a Redis queue for Documents that are ready for
 * output to consumers. Documents are matched against connected clients based on
 * the filters they provided on connection.
 *
 * @author jrogstadius
 */
public class OutputMatcher extends PipelineProcess  {

    public int inputCount = 0;
    public int outputCount = 0;

    private static Logger logger = Logger.getLogger(OutputMatcher.class);
    
    public void run() {
        while (true) {
            if (Thread.interrupted())
                return;

            // Get an item from the input queue
            Document doc = getItem();
            inputCount++;
            if (doc == null)
                continue;

            pushToTaskWriteQueue(doc);
            //logger.info("Going to add nominal_labels before final output to REDIS");
            String jsonDoc = DocumentJSONConverter.getDocumentSetJson(doc);
            pushToClients(doc, jsonDoc);
            //logger.info("Going to push into REDIS json String: " + jsonDoc);
            PushToRedisStream(doc.getCrisisCode(), jsonDoc);
            outputCount++;
        }
    }

    private Document getItem() {
        Jedis jedis = DataStore.getJedisConnection();
		List<byte[]> byteDoc = jedis
				.blpop(60,
						TaggerConfigurator
								.getInstance()
								.getProperty(
										TaggerConfigurationProperty.REDIS_FOR_OUTPUT_QUEUE)
								.getBytes());
		DataStore.close(jedis);

        if (byteDoc == null) // This is null if the blpop timed out
            return null;

        try {
            return Serializer.deserialize(byteDoc.get(1));
        } catch (ClassNotFoundException | IOException e) {
            logger.error("Exception when parsing document set from stream.");
            return null;
        }
    }

    private void pushToTaskWriteQueue(Document doc) {
        Jedis jedis = DataStore.getJedisConnection();
		try {
			jedis.rpush(
					TaggerConfigurator
							.getInstance()
							.getProperty(
									TaggerConfigurationProperty.REDIS_LABEL_TASK_WRITE_QUEUE)
							.getBytes(), Serializer.serialize(doc));
		} catch (IOException e) {
			logger.error("Exception while serializing DocumentSet.");
		}
		DataStore.close(jedis);
    }

    private void pushToClients(Document doc, String docJson) {
        List<HttpOutputWorker> workers = HttpOutputWorkerIndex.getInstance()
                .getWorkers(doc.getCrisisCode());
        // log(LogLevel.INFO, "Matching clients for event " + doc.getCrisisID()
        // + ": " + workers.size() + " (checking filters)");
        for (HttpOutputWorker worker : workers) {
            // Pass to stream if the consumer has not specified any filters
            // beyond the event ID
            // OR if any filter matches the labels of the document
//            if (worker.filter.crisisCode == doc.getCrisisCode() && 
//                    (!worker.filter.hasAttributeFilters() || worker.filter.match(doc.getLabels(DocumentLabel.class)))) {

            if (worker.filter.crisisCode.equals(doc.getCrisisCode())) {

                worker.push(docJson);
            }
        }
    }

	public static void PushToRedisStream(String crisisCode, String docJson) {
		Jedis redis = DataStore.getJedisConnection();
		redis.publish(
				TaggerConfigurator
						.getInstance()
						.getProperty(
								TaggerConfigurationProperty.REDIS_OUTPUT_CHANNEL_PREFIX)
						+ "." + crisisCode, docJson);
		DataStore.close(redis);
	}

    @Override
    protected void processItem(Document item) {
        run();
    }
}
