package qa.qcri.aidr.predict.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;

import qa.qcri.aidr.predict.DataStore;
import qa.qcri.aidr.predict.common.Serializer;
import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;
import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.data.DocumentJSONConverter;
import redis.clients.jedis.Jedis;

/**
 * InputWorker maintains a persistent HTTP connection to clients who provide
 * unclassified documents in JSON format. The documents are parsed into
 * DocumentSet objects, which are then pushed to a Redis queue for further
 * processing.
 * 
 * @author jrogstadius
 */
public class HttpInputWorker implements Runnable {

    static int connectionID = 0;
    int connectionInstanceID;
    private Socket client;
    
    private static Logger logger = Logger.getLogger(HttpInputWorker.class);
    
    HttpInputWorker(Socket client) {
        this.client = client;
        connectionInstanceID = connectionID++;
    }

    public void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
        } catch (IOException e) {
            logger.warn("Could not create input stream reader. " + e.getMessage());
            return;
        }
        logger.info("Created new InputWorker (" + connectionInstanceID
                + ")");

        // Process input data
        try {
            String line = "";

            while (((line = in.readLine()) != null)) {
                if (Thread.interrupted())
                    return;

                logger.info("Received: " + line);
                Document doc = ParseJSONDocument(line);
                //doc.setSourceIP(client.getInetAddress());
                if (doc != null)
                    enqueue(doc);
            }
        } catch (IOException e) {
            logger.warn("Read failed in connection "
                    + connectionInstanceID);
        }

        try {
            client.close();
        } catch (IOException e) {
            logger.error("Error when closing connection " + connectionInstanceID);
        }
    }

    private Document ParseJSONDocument(String json) {
        Document doc = null;
        try {
            doc = DocumentJSONConverter.parseDocument(json);
            //doc.setSourceIP(client.getInetAddress());
        } catch (Exception e) {
            logger.error("Error when parsing input JSON : " + json);
        }
        return doc;
    }

    private void enqueue(Document doc) {
        Jedis jedis = DataStore.getJedisConnection();

		try {
			jedis.rpush(
					TaggerConfigurator
							.getInstance()
							.getProperty(
									TaggerConfigurationProperty.REDIS_FOR_EXTRACTION_QUEUE)
							.getBytes(), Serializer.serialize(doc));
		} catch (IOException e) {
			logger.warn("Error when serializing input document : " + doc != null ? doc.toString() : "no doc");
		} finally {
			DataStore.close(jedis);
		}
	}
}
