package qa.qcri.aidr.predict.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import redis.clients.jedis.Jedis;
import qa.qcri.aidr.predict.common.*;
import qa.qcri.aidr.predict.data.DocumentJSONConverter;
import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.DataStore;

/**
 * InputWorker maintains a persistent HTTP connection to clients who provide
 * unclassified documents in JSON format. The documents are parsed into
 * DocumentSet objects, which are then pushed to a Redis queue for further
 * processing.
 * 
 * @author jrogstadius
 */
public class HttpInputWorker extends Loggable implements Runnable {

    static int connectionID = 0;
    int connectionInstanceID;
    private Socket client;

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
            log(LogLevel.ERROR, "Could not create input stream reader");
            return;
        }
        log(LogLevel.INFO, "Created new InputWorker (" + connectionInstanceID
                + ")");

        // Process input data
        try {
            String line = "";

            while (((line = in.readLine()) != null)) {
                if (Thread.interrupted())
                    return;

                log(LogLevel.INFO, "Received: " + line);
                Document doc = ParseJSONDocument(line);
                doc.setSourceIP(client.getInetAddress());
                if (doc != null)
                    enqueue(doc);
            }
        } catch (IOException e) {
            log(LogLevel.WARNING, "Read failed in connection "
                    + connectionInstanceID);
        }

        log(LogLevel.INFO, "Closing connection " + connectionInstanceID);
        try {
            client.close();
        } catch (IOException e) {
            log("Error when closing connection " + connectionInstanceID, e);
        }
    }

    private Document ParseJSONDocument(String json) {
        Document doc = null;
        try {
            doc = DocumentJSONConverter.parseDocument(json);
            doc.setSourceIP(client.getInetAddress());
        } catch (Exception e) {
            log("Error when parsing input JSON", e);
        }
        return doc;
    }

    private void enqueue(Document doc) {
        Jedis jedis = DataStore.getJedisConnection();

        try {
            jedis.rpush(Config.REDIS_FOR_EXTRACTION_QUEUE.getBytes(),
                    Serializer.serialize(doc));
        } catch (IOException e) {
            log("Error when serializing input document.", e);
        } finally {
            DataStore.close(jedis);
        }
    }
}
