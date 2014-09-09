/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.persister.tagger;

/**
 *
 * @author Imran
 */
import qa.qcri.aidr.persister.collector.*;

import java.io.File;
//import java.util.logging.Level;
//import java.util.logging.Logger;






import redis.clients.jedis.JedisPubSub;

import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import qa.qcri.aidr.utils.ClassifiedTweet;
import qa.qcri.aidr.utils.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import qa.qcri.aidr.utils.JsonDeserializer;
import qa.qcri.aidr.common.redis.LoadShedder;
import qa.qcri.aidr.io.FileSystemOperations;
import qa.qcri.aidr.logging.ErrorLog;

public class TaggerSubscriber extends JedisPubSub {
	
	private static Logger logger = Logger.getLogger(TaggerSubscriber.class.getName());
	private static ErrorLog elog = new ErrorLog();
	
    private String persisterDir;
    private String collectionDir;
    private BufferedWriter out = null;
    private String collectionCode;
    private File file;
    private long itemsWrittenToFile = 0;
    private int fileVolumnNumber = 1;
    	
    private static ConcurrentHashMap<String, LoadShedder> redisLoadShedder = null;
    
    public TaggerSubscriber() {
    }

    public TaggerSubscriber(String fileLoc, String collectionCode) {
        //remove leading and trailing double quotes from collectionCode
        fileVolumnNumber = FileSystemOperations.getLatestFileVolumeNumber4Tagger(collectionCode);
        this.collectionCode = collectionCode.replaceAll("^\"|\"$", ""); // removing spaces
        this.persisterDir = fileLoc.replaceAll("^\"|\"$", "");
        collectionDir = createNewDirectory();
        createNewFile();
        createBufferWriter();
        if (null == redisLoadShedder) {
        	redisLoadShedder = new ConcurrentHashMap<String, LoadShedder>(20);
        }
        redisLoadShedder.put(Config.TAGGER_CHANNEL+collectionCode, new LoadShedder(Config.PERSISTER_LOAD_LIMIT, Config.PERSISTER_LOAD_CHECK_INTERVAL_MINUTES, true));
        logger.info("Created loadshedder for channel: " + (Config.TAGGER_CHANNEL+collectionCode));
    }

    @Override
    public void onMessage(String channel, String message) {
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
    	logger.info("Received message for channel: " + channel);
    	logger.info("isLoadShedder for channel " + channel + " = " + redisLoadShedder.containsKey(channel));
    	if (redisLoadShedder.get(channel).canProcess(channel)) {
    		logger.info("can process write for: " + channel);
    		writeToFile(message);
        }
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        logger.info("Tagger persister: Unsubscribed Successfully from channel pattern = " + pattern);
        closeFileWriting();
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        logger.info("Tagger persister: Subscribed Successfully to persist channel pattern = " + pattern);
    }

    private void createNewFile() {
        try {
            file = new File(collectionDir + "Classified_" + collectionCode + "_" + getDateTime() + "_vol-" + fileVolumnNumber + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException ex) {
            //Logger.getLogger(TaggerSubscriber.class.getName()).log(Level.SEVERE, null, ex);
        	logger.error(collectionCode + " error in creating new file at location " + collectionDir);
        	logger.error(elog.toStringException(ex));
        }
    }

    private String createNewDirectory() {
        File theDir = new File(persisterDir + collectionCode + "/output");
        if (!theDir.exists()) {
            System.out.println("creating directory: " + persisterDir + collectionCode + "/output");
            boolean result = theDir.mkdir();
            
            if (result) {
            	logger.info("DIR created for collection: " + collectionCode);
                return persisterDir + collectionCode + "/output/";
            } 
            
        }
        return persisterDir + collectionCode + "/output/";
    }

    private void createBufferWriter() {
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file, true), Charset.forName("UTF-8")), Config.DEFAULT_FILE_WRITER_BUFFER_SIZE);
        } catch (IOException ex) {
            //Logger.getLogger(TaggerSubscriber.class.getName()).log(Level.SEVERE, null, ex);
        	logger.error(collectionCode + "Error in creating Buffered writer");
        	logger.error(elog.toStringException(ex));
        }

    }

    private void writeToFile(String message) {
        try {
            out.write(message+"\n");
            itemsWrittenToFile++;
            isTimeToCreateNewFile();
        } catch (IOException ex) {
            //Logger.getLogger(TaggerSubscriber.class.getName()).log(Level.SEVERE, null, ex);
        	logger.error(collectionCode + "Error in writing to file");
        	logger.error(elog.toStringException(ex));
        }
        
        // Debug code added by koushik
        JsonDeserializer jsd = new JsonDeserializer();
        ClassifiedTweet tweet = jsd.getClassifiedTweet(message);
        if (null == tweet) {
        	//System.err.println("[writeToFile] REDIS output JSON data format error!!! Offending tweet: " + tweet.getTweetID());
        } else {
        	if (null == tweet.getLabelName_1() && tweet.getNominalLabels().isEmpty()) {
        		//System.err.println("[writeToFile] REDIS output faulty tweet with empty nominal label: " + tweet.getTweetID());
        	}
        }
    }

    private void isTimeToCreateNewFile() {
        if (itemsWrittenToFile >= Config.DEFAULT_FILE_VOLUMN_LIMIT) {
            closeFileWriting();
            itemsWrittenToFile = 0;
            fileVolumnNumber++;
            createNewFile();
            createBufferWriter();
        }
    }

    public void closeFileWriting() {
        try {
            out.flush();
            out.close();
        } catch (IOException ex) {
            //Logger.getLogger(TaggerSubscriber.class.getName()).log(Level.SEVERE, null, ex);
        	logger.error(collectionCode + "Error in closing file writer");
        	logger.error(elog.toStringException(ex));
        }
    }

    private final static String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");  //yyyy-MM-dd_hh:mm:ss
        //df.setTimeZone(TimeZone.getTimeZone("PST"));  
        return df.format(new Date());
    }
}
