package qa.qcri.aidr.predict.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import qa.qcri.aidr.predict.DataStore;
import qa.qcri.aidr.predict.data.Document;

/**
 * Abstract class for simplifying handling of pipeline processes that consume
 * Documents from an Redis queue (input), perform some processing, and push the
 * Document to another queue (output). It has basic functionality for estimating
 * its own processing capacity based on recent execution times.
 * 
 * @author jrogstadius
 */
public abstract class PipelineProcess extends Loggable implements Runnable {
	
	private static Logger logger = Logger.getLogger(PipelineProcess.class);
	private static ErrorLog elog = new ErrorLog();
    
	static class ExecutionTime {
        public double dT;
        public long T;

        public ExecutionTime(long T, double dT) {
            this.T = T;
            this.dT = dT;
        }
    }

    public byte[] inputQueueName;
    public byte[] outputQueueName;
    LinkedList<ExecutionTime> executionTimes = new LinkedList<ExecutionTime>();
    public long inputCount = 0;
    public long outputCount = 0;

    public void run() {
        if (inputQueueName == null) {
        	logger.error("No input queue set");
        	throw new RuntimeException("No input queue set");
        }

        while (true) {
            if (Thread.interrupted())
                return;

            Jedis jedis = DataStore.getJedisConnection();

            try {
                // Get an item from the input queue (wait for 60 seconds, then
                // start over)
                List<byte[]> byteDoc = jedis.blpop(60, inputQueueName);
                if (byteDoc == null) {
                    idle();
                    continue;
                }

                long startTime = System.nanoTime();
                inputCount++;

                // Deserialize item
                Document item;
                try {
                    item = Serializer.deserialize(byteDoc.get(1));
                } catch (ClassNotFoundException | IOException e) {
                    logger.error("Error when deserializing input document.");
                    logger.error(elog.toStringException(e));
                    continue;
                }

                // Process the item
                logger.info("Going to process item from crisis: " + item.getCrisisCode() 
                		+ ", having docType: " + item.getDoctype() 
                		+ ", with id: " + item.getDocumentID());
                logger.info("Raw json from REDIS: " + byteDoc.get(1));
                processItem(item);

                // Push to output queue
                if (outputQueueName != null) {
                    try {
                        jedis.rpush(outputQueueName, Serializer.serialize(item));
                        outputCount++;
                    } catch (IOException e) {
                        logger.error("Error when serializing output document.");
                        logger.error(elog.toStringException(e));
                    }
                }

                long stopTime = System.nanoTime();
                double latency = (stopTime - startTime) / 1000000.0;
                pushExecutionTime(System.currentTimeMillis(), latency);
                        

            } finally {
                DataStore.close(jedis);
            }
        }
    }

    synchronized void pushExecutionTime(long T, double dT) {
        executionTimes.add(new ExecutionTime(T, dT));
        if (executionTimes.size() > 100)
            executionTimes.remove();
    }

    public synchronized double getMaxItemsPerSecond() {
        if (executionTimes.size() < 1)
            return 1;

        ExecutionTime[] times = executionTimes
                .toArray(new ExecutionTime[executionTimes.size()]);
        double sum = 0;
        for (int i = 0; i < times.length; i++)
            sum += times[i].dT;
        double meanSecondsPerItem = 0.001 * sum / (double) times.length;
        return 1.0 / meanSecondsPerItem;
    }

    public synchronized double getCurrentItemsPerSecond() {
        long now = System.currentTimeMillis();
        ArrayList<ExecutionTime> past5seconds = new ArrayList<ExecutionTime>();
        for (ExecutionTime t : executionTimes) {
            if (now - t.T < 5000)
                past5seconds.add(t);
        }

        if (past5seconds.size() < 2)
            return 0;

        return 1000
                * (double) past5seconds.size()
                / (past5seconds.get(past5seconds.size() - 1).T - past5seconds
                        .get(0).T);
    }

    public double getLoad() {
        return getCurrentItemsPerSecond() / getMaxItemsPerSecond();
    }

    protected abstract void processItem(Document item);

    protected void idle() {
    }
}
