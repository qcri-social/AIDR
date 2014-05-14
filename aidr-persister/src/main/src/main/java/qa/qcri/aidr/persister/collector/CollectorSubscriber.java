/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.persister.collector;

/**
 *
 * @author Imran
 */
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import redis.clients.jedis.JedisPubSub;

import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import qa.qcri.aidr.utils.Config;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import qa.qcri.aidr.io.FileSystemOperations;

public class CollectorSubscriber extends JedisPubSub {

    private String persisterDir;
    private String collectionDir;
    private BufferedWriter out = null;
    private String collectionCode;
    private File file;
    private long itemsWrittenToFile = 0;
    private int fileVolumnNumber = 1;

    public CollectorSubscriber() {
    }

    public CollectorSubscriber(String fileLoc, String collectionCode) {
        //remove leading and trailing double quotes from collectionCode
        fileVolumnNumber = FileSystemOperations.getLatestFileVolumeNumber(collectionCode);
        this.collectionCode = collectionCode.replaceAll("^\"|\"$", "");
        this.persisterDir = fileLoc.replaceAll("^\"|\"$", "");
        collectionDir = createNewDirectory();
        createNewFile();
        createBufferWriter();
    }

    @Override
    public void onMessage(String channel, String message) {
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        writeToFile(message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        System.out.println("Unsubscribed Successfully to channel = " + collectionCode);
        closeFileWriting();
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("Subscribed Successfully to persist channel = " + collectionCode);
    }

    private void createNewFile() {
        try {
            file = new File(collectionDir + collectionCode + "_" + getDateTime() + "_vol-" + fileVolumnNumber + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException ex) {
            Logger.getLogger(CollectorSubscriber.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String createNewDirectory() {
        File theDir = new File(persisterDir + collectionCode);
        if (!theDir.exists()) {
            System.out.println("creating directory: " + persisterDir + collectionCode);
            boolean result = theDir.mkdir();
            
            if (result) {
                System.out.println("DIR created");
                return persisterDir + collectionCode + "/";
            } 
            
        }
        return persisterDir + collectionCode + "/";
    }

    private void createBufferWriter() {
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file, true), Charset.forName("UTF-8")), Config.DEFAULT_FILE_WRITER_BUFFER_SIZE);
        } catch (IOException ex) {
            Logger.getLogger(CollectorSubscriber.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void writeToFile(String message) {
        try {
            out.write(message+"\n");
            itemsWrittenToFile++;
            isTimeToCreateNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CollectorSubscriber.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CollectorSubscriber.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private final static String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");  //yyyy-MM-dd_hh:mm:ss
        //df.setTimeZone(TimeZone.getTimeZone("PST"));  
        return df.format(new Date());
    }
}
