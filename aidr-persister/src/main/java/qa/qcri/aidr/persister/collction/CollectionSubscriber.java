/**
 * Creates a REDIS subscriber object for a collection
 * 
 * @author Imran
 */
package qa.qcri.aidr.persister.collction;


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
import qa.qcri.aidr.entity.DataFeed;
import qa.qcri.aidr.io.FileSystemOperations;
import qa.qcri.aidr.service.DataFeedService;
import qa.qcri.aidr.service.ImageFeedService;
import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;
import redis.clients.jedis.JedisPubSub;

public class CollectionSubscriber extends JedisPubSub {

    private static Logger logger = Logger.getLogger(CollectionSubscriber.class.getName());

    private String persisterDir;
    private String collectionDir;
    private BufferedWriter out = null;
    private String collectionCode;
    private File file;
    private long itemsWrittenToFile = 0;
    private int fileVolumnNumber = 1;
    private boolean saveMediaEnabled;
    
    private DataFeedService dataFeedService;
    private ImageFeedService imageFeedService;
    
    private static ConcurrentHashMap<String, LoadShedder> redisLoadShedder = null;

    public CollectionSubscriber() {
    }

    public CollectionSubscriber(String fileLoc, String channel, String collectionCode, boolean saveMediaEnabled) {
        //remove leading and trailing double quotes from collectionCode
        fileVolumnNumber = FileSystemOperations.getLatestFileVolumeNumber(collectionCode);
        this.collectionCode = collectionCode.replaceAll("^\"|\"$", "");
        this.persisterDir = fileLoc.replaceAll("^\"|\"$", "");
        collectionDir = createNewDirectory();
        this.saveMediaEnabled = saveMediaEnabled;
        
        createNewFile();
        createBufferWriter();
        if (null == redisLoadShedder) {
            redisLoadShedder = new ConcurrentHashMap<String, LoadShedder>(20);
        }
        redisLoadShedder.put(channel, new LoadShedder(Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_LOAD_LIMIT)), Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_LOAD_CHECK_INTERVAL_MINUTES)), true,channel));
        logger.info("Created loadshedder for channel: " + channel);
        
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/spring-servlet.xml");
        dataFeedService = (DataFeedService) appContext.getBean("dataFeedService");
        imageFeedService = (ImageFeedService) appContext.getBean("imageFeedService");
    }

    @Override
    public void onMessage(String channel, String message) {
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        if (redisLoadShedder.get(channel).canProcess()) {
            writeToFile(message);
            writeToPostgres(message);
        } else {
            logger.info("loadshdder denied write for: " + channel);
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
        logger.info("Unsubscribed Successfully from channel pattern = " + pattern);
        closeFileWriting();
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        logger.info("Subscribed Successfully to persist channel pattern = " + pattern);
    }

    private void createNewFile() {
        try {
            file = new File(collectionDir + collectionCode + "_" + getDateTime() + "_vol-" + fileVolumnNumber + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException ex) {
            logger.error(collectionCode + " Error in creating new file at location " + collectionDir);
        }
    }

    private String createNewDirectory() {
        File theDir = new File(persisterDir + collectionCode);
        if (!theDir.exists()) {
            logger.info("creating directory: " + persisterDir + collectionCode);
            boolean result = theDir.mkdir();

            if (result) {
                logger.info("DIR created for collection: " + collectionCode);
                return persisterDir + collectionCode + "/";
            }else{
            	logger.error(collectionCode+ " Unable to create a new directory: ");
            }

        }
        return persisterDir + collectionCode + "/";
    }

    private void createBufferWriter() {
        try {
            //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file, true), Charset.forName("UTF-8")), Integer.parseInt(getProperty("DEFAULT_FILE_WRITER_BUFFER_SIZE")));
        	out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file, true)), Integer.parseInt(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_FILE_WRITER_BUFFER_SIZE)));
        } catch (IOException ex) {
            logger.error(collectionCode + " Error in creating Buffered writer");
        }

    }

    private void writeToFile(String message) {
        try {
            out.write(message + "\n");
            itemsWrittenToFile++;
            isTimeToCreateNewFile();
        } catch (IOException ex) {
            logger.error(collectionCode + " Error in writing to file");
        }
    }
    
    //Persisting To Postgres
    private void writeToPostgres(String message) {
        try{
        	JSONObject msgJson  = new JSONObject(message);
            DataFeed dataFeed = new DataFeed();
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
			
            Long dataFeedId = dataFeedService.persist(dataFeed);
            if(dataFeedId != null && saveMediaEnabled) {
            	JSONObject entities = msgJson.getJSONObject("entities");
            	if(entities != null && entities.has("media")
            			&& entities.getJSONArray("media") != null
            			&& entities.getJSONArray("media").length() > 0 
            			&& entities.getJSONArray("media").getJSONObject(0).getString("type") != null
            			&& entities.getJSONArray("media").getJSONObject(0).getString("type").equals("photo")) {
            		String imageUrl = entities.getJSONArray("media").getJSONObject(0).getString("media_url");
            		imageFeedService.checkAndSaveIfNotExists(dataFeedId, collectionCode, imageUrl);
            	}
            }
        }catch(Exception e){
        	logger.error("Error in persisting :::: " + message);
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
            if ( out != null ) {
                out.flush();
                out.close();
            }
        } catch (IOException ex) {
            logger.error(collectionCode + " Error in closing file writer");
        }
    }

    private final static String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");  //yyyy-MM-dd_hh:mm:ss
        return df.format(new Date());
    }
}
