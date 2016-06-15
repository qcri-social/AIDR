/**
 * Creates a subscription object for REDIS to listen for streaming tweets
 * 
 * @author Imran
 */
package qa.qcri.aidr.persister.tagger;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import qa.qcri.aidr.common.redis.LoadShedder;
import qa.qcri.aidr.entity.TwitterDataFeed;
import qa.qcri.aidr.io.FileSystemOperations;
import qa.qcri.aidr.service.DataFeedService;
import qa.qcri.aidr.utils.ClassifiedTweet;
import qa.qcri.aidr.utils.JsonDeserializer;
import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;
import redis.clients.jedis.JedisPubSub;


public class TaggerSubscriber extends JedisPubSub {
	
	private static Logger logger = Logger.getLogger(TaggerSubscriber.class.getName());
	
    private String persisterDir;
    private String collectionDir;
    private BufferedWriter out = null;
    private String collectionCode;
    private File file;
    private long itemsWrittenToFile = 0;
    private int fileVolumnNumber = 1;
    DataFeedService dataFeedService = null;
    	
    private static ConcurrentHashMap<String, LoadShedder> redisLoadShedder = null;
    
    public TaggerSubscriber() {
    }

    public TaggerSubscriber(String fileLoc, String collectionCode) {
        //remove leading and trailing double quotes from collectionCode
        //fileVolumnNumber = FileSystemOperations.getLatestFileVolumeNumber4Tagger(collectionCode);
    	fileVolumnNumber = FileSystemOperations.getLatestFileVolumeNumber(collectionCode);
        this.collectionCode = collectionCode.replaceAll("^\"|\"$", ""); // removing spaces
        this.persisterDir = fileLoc.replaceAll("^\"|\"$", "");
        collectionDir = createNewDirectory();
        createNewFile();
        createBufferWriter();
        if (null == redisLoadShedder) {
        	redisLoadShedder = new ConcurrentHashMap<String, LoadShedder>(20);
        }
        String channelName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TAGGER_CHANNEL)
				+ collectionCode;
		redisLoadShedder
				.put(channelName,
						new LoadShedder(
								Integer.parseInt(PersisterConfigurator
										.getInstance()
										.getProperty(
												PersisterConfigurationProperty.PERSISTER_LOAD_LIMIT)),
								Integer.parseInt(PersisterConfigurator
										.getInstance()
										.getProperty(
												PersisterConfigurationProperty.PERSISTER_LOAD_CHECK_INTERVAL_MINUTES)),
								true,channelName));
		logger.info("Created loadshedder for channel: " + (PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TAGGER_CHANNEL)+collectionCode));
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/spring-servlet.xml");
        dataFeedService = (DataFeedService) appContext.getBean("dataFeedService");
    }

    @Override
    public void onMessage(String channel, String message) {
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
    	logger.info("Received message for channel: " + channel);
    	logger.info("isLoadShedder for channel " + channel + " = " + redisLoadShedder.containsKey(channel));
    	if (redisLoadShedder.get(channel).canProcess()) {
    		logger.info("can process write for: " + channel);
    		writeToFile(message);
    		writeToPostgres(message);
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
        	logger.error(collectionCode + " error in creating new file at location " + collectionDir);
        }
    }

    private String createNewDirectory() {
        File theDir = new File(persisterDir + collectionCode);
        if (!theDir.exists()) {
            System.out.println("creating directory: " + persisterDir + collectionCode);
            boolean result = theDir.mkdir();
            
            if (result) {
            	logger.info("DIR created for collection: " + collectionCode);
                return persisterDir + collectionCode + "/";
            } 
            
        }
        return persisterDir + collectionCode + "/";
    }

    private void createBufferWriter() {
        try {
            //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file, true), Charset.forName("UTF-8")), Integer.parseInt(getProperty("DEFAULT_FILE_WRITER_BUFFER_SIZE")));
        	out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file, true)), Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_FILE_WRITER_BUFFER_SIZE)));
        	
        } catch (IOException ex) {
        	logger.error(collectionCode + "Error in creating Buffered writer");
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
    
    //Persisting To Postgres
    private void writeToPostgres(String message) {
        try{
        	JSONObject msgJson  = new JSONObject(message);
            TwitterDataFeed dataFeed = new TwitterDataFeed();
            dataFeed.setCode(collectionCode);
            dataFeed.setFeed(msgJson);
            JSONObject aidrJson = msgJson.getJSONObject("aidr");
			dataFeed.setAidr(aidrJson);
			dataFeed.setSource(aidrJson.getString("doctype"));
			if(msgJson.has("coordinates") && !msgJson.isNull("coordinates")){
				dataFeed.setGeo(msgJson.getJSONObject("coordinates"));
			}
			if(msgJson.has("place") && !msgJson.isNull("place")){
				dataFeed.setPlace(msgJson.getJSONObject("place"));
			}
            dataFeedService.persist(dataFeed);
            
        }catch(Exception e){
        	logger.error("Exception while persisting to postgres db ", e );
        }
    }

    private void isTimeToCreateNewFile() {
        if (itemsWrittenToFile >= Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_FILE_VOLUMN_LIMIT))) {
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
        	logger.error(collectionCode + "Error in closing file writer");
        }
    }

    private final static String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");  //yyyy-MM-dd_hh:mm:ss
        //df.setTimeZone(TimeZone.getTimeZone("PST"));  
        return df.format(new Date());
    }
}
