package qa.qcri.aidr.predict.communication;

import qa.qcri.aidr.predict.DataStore;
import qa.qcri.aidr.predict.classification.DocumentLabel;
import qa.qcri.aidr.predict.common.*;
import qa.qcri.aidr.predict.data.DocumentJSONConverter;
import qa.qcri.aidr.predict.data.Document;

import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import static qa.qcri.aidr.predict.common.ConfigProperties.getProperty;

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
    private static ErrorLog elog = new ErrorLog();
    
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
            String jsonDoc = DocumentJSONConverter.getDocumentSetJson(doc);
            pushToClients(doc, jsonDoc);
            PushToRedisStream(doc.getCrisisCode(), jsonDoc);
            outputCount++;
        }
    }

    private Document getItem() {
        Jedis jedis = DataStore.getJedisConnection();
        List<byte[]> byteDoc = jedis.blpop(60,
                getProperty("redis_for_output_queue").getBytes());
        DataStore.close(jedis);

        if (byteDoc == null) // This is null if the blpop timed out
            return null;

        try {
            return Serializer.deserialize(byteDoc.get(1));
        } catch (ClassNotFoundException | IOException e) {
            logger.error("Exception when parsing document set from stream.");
            logger.error(elog.toStringException(e));
            return null;
        }
    }

    private void pushToTaskWriteQueue(Document doc) {
        Jedis jedis = DataStore.getJedisConnection();
        try {
            jedis.rpush(getProperty("redis_label_task_write_queue").getBytes(),
                    Serializer.serialize(doc));
        } catch (IOException e) {
            logger.error("Exception while serializing DocumentSet");
            logger.error(elog.toStringException(e));
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
                getProperty("redis_output_channel_prefix") + "." + crisisCode,
                docJson);
        DataStore.close(redis);
    }

    @Override
    protected void processItem(Document item) {
        run();
    }
}
