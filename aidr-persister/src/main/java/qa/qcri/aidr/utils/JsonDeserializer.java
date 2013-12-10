/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.utils;

import org.supercsv.io.ICsvBeanWriter;
import qa.qcri.aidr.utils.Config;

import java.io.*;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import qa.qcri.aidr.utils.Tweet;
import qa.qcri.aidr.io.FileSystemOperations;
import qa.qcri.aidr.io.ReadWriteCSV;

import java.nio.charset.Charset;
import net.minidev.json.JSONArray;

/**
 *
 * @author Imran
 */
public class JsonDeserializer {

    //This method generates tweetIds csv from all the jsons of a collection
    public String generateJson2TweetIdsCSV(String collectionCode) {
        String fileName = "";
        List<String> fileNames = FileSystemOperations.getAllJSONFileVolumes(collectionCode);
        List<Tweet> tweetsList = new ArrayList();
        ReadWriteCSV csv = new ReadWriteCSV();
        BufferedReader br = null;
        String fileToDelete = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/" + collectionCode + "_tweetIds.csv";
        FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name
        for (String file : fileNames) {
            String fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/" + file;
            try {
                br = new BufferedReader(new FileReader(fileLocation));
                String line;
                while ((line = br.readLine()) != null) {
                    Tweet tweet = new Tweet();
                    try {
                        Object obj = JSONValue.parseStrict(line);
                        JSONObject jsonObj = (JSONObject) obj;
                        tweet.setTweetID(jsonObj.get("id").toString());
                        tweetsList.add(tweet);
                        if (tweetsList.size() <= 10000) { //after every 10k write to CSV file
                            tweetsList.add(tweet);
                        } else {
                            fileName = csv.writeCollectorTweetIDSCSV(tweetsList, collectionCode, collectionCode + "_tweetIds");
                            tweetsList.clear();
                        }
                    } catch (ParseException ex) {
                    }
                }

                br.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fileName = csv.writeCollectorTweetIDSCSV(tweetsList, collectionCode, collectionCode + "_tweetIds");
        tweetsList.clear();

        return fileName;
    }

    public String generateClassifiedJson2TweetIdsCSV(String collectionCode) {
        String fileName = "";
        List<String> fileNames = FileSystemOperations.getClassifiedFileVolumes(collectionCode);
        List<ClassifiedTweet> tweetsList = new ArrayList();
        ReadWriteCSV csv = new ReadWriteCSV();
        BufferedReader br = null;
        String fileToDelete = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/" + "Classified_" + collectionCode + "_tweetIds.csv";
        System.out.println("Deleteing file : " + fileToDelete);
        FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name
        for (String file : fileNames) {
            String fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/" + file;
            System.out.println("Reading file " + fileLocation);
            try {
                br = new BufferedReader(new FileReader(fileLocation));
                String line;
                while ((line = br.readLine()) != null) {
                    ClassifiedTweet tweet = getClassifiedTweet(line);
                    tweetsList.add(tweet);
                    if (tweetsList.size() <= 10000) { //after every 10k write to CSV file
                        tweetsList.add(tweet);
                    } else {
                        fileName = csv.writeClassifiedTweetIDsCSV(tweetsList, collectionCode, "Classified_" + collectionCode + "_tweetIds");
                        tweetsList.clear();
                    }

                }

                br.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fileName = csv.writeClassifiedTweetIDsCSV(tweetsList, collectionCode, "Classified_" + collectionCode + "_tweetIds");
        tweetsList.clear();

        return fileName;
    }

    private final static String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");  //yyyy-MM-dd_hh:mm:ss
        return df.format(new Date());
    }

    public String generateJSON2CSV_100K_BasedOnTweetCount(String collectionCode) {
        BufferedReader br = null;
        boolean isCSVGenerated = true;
        String fileName = "";
        ICsvBeanWriter beanWriter = null;

        try {

            String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode;
            String fileNameforCSVGen = collectionCode + "_last_100k_tweets";
            fileName = fileNameforCSVGen + ".csv";
            FileSystemOperations.deleteFile(Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/" + fileNameforCSVGen + ".csv");

            File folder = new File(folderLocation);
            File[] listOfFiles = folder.listFiles();

            Arrays.sort(listOfFiles, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                }
            });

            List<Tweet> tweetsList = new ArrayList();
            ReadWriteCSV csv = new ReadWriteCSV();
            long maxCSVWriteSize = 10000;
            long currentSize = 0;

            createTweetList:
            {
                for (int i = 0; i < listOfFiles.length; i++) {
                    File f = listOfFiles[i];
                    String currentFileName = f.getName();
                    if (currentFileName.endsWith(".json")) {
                        String line;
                        System.out.println("Reading file : " + f.getAbsolutePath());
                        InputStream is = new FileInputStream(f.getAbsolutePath());
                        br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                        while ((line = br.readLine()) != null) {
                            try {
                                Tweet tweet = getTweet(line);

                                if (tweetsList.size() <= maxCSVWriteSize && currentSize <= Config.TWEETS_EXPORT_LIMIT_100K) {
                                    //write to arrayList
                                    tweetsList.add(tweet);
                                    currentSize++;
                                    // System.out.println("currentSize  : " + currentSize);
                                } else {

                                    //write csv file
                                    beanWriter = csv.writeCollectorTweetsCSV(tweetsList, collectionCode, fileNameforCSVGen, beanWriter);
                                    // empty arraylist
                                    tweetsList.clear();

                                }
                                if (beanWriter != null) {
                                    // System.out.println("beanWriter  : " +beanWriter.getLineNumber());
                                    if (currentSize >= Config.TWEETS_EXPORT_LIMIT_100K) {
                                        // write to the csv file
                                        break createTweetList;
                                    }
                                }

                            } catch (ParseException ex) {
                                //Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, "JSON file parsing exception at line {0}", lineNumber);
                            }
                        }
                        beanWriter = csv.writeCollectorTweetsCSV(tweetsList, collectionCode, fileNameforCSVGen, beanWriter);
                        System.out.println("final beanWriter  : " + beanWriter.getRowNumber());
                        tweetsList.clear();
                        br.close();
                    }
                }
            }
            //fileName = csv.writeCollectorTweetIDSCSV(tweetsList, collectionCode, fileNameforCSVGen);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, "File not found." + ex);
            isCSVGenerated = false;
        } catch (IOException ex) {
            Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(ReadWriteCSV.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return fileName;
    }

    public String taggerGenerateJSON2CSV_100K_BasedOnTweetCount(String collectionCode, int exportLimit) {
        BufferedReader br = null;
        String fileName = "";
        ICsvBeanWriter beanWriter = null;

        try {

            String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode;
            String fileNameforCSVGen = "Classified_" + collectionCode + "_last_100k_tweets";
            fileName = fileNameforCSVGen + ".csv";
            FileSystemOperations.deleteFile(Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/" + fileNameforCSVGen + ".csv");

            File folder = new File(folderLocation);
            File[] listOfFiles = folder.listFiles();
            // to get only Tagger's files
            ArrayList<File> taggerFilesList = new ArrayList();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (StringUtils.startsWith(listOfFiles[i].getName(), "Classified_")) {
                    taggerFilesList.add(listOfFiles[i]);
                }
            }

            Object[] objectsArray = taggerFilesList.toArray();
            File[] taggerFiles = Arrays.copyOf(objectsArray, objectsArray.length, File[].class);
            Arrays.sort(taggerFiles, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                }
            });

            List<ClassifiedTweet> tweetsList = new ArrayList();
            ReadWriteCSV csv = new ReadWriteCSV();
            long maxCSVWriteSize = 10000;
            long currentSize = 0;

            createTweetList:
            {
                for (int i = 0; i < taggerFiles.length; i++) {
                    File f = taggerFiles[i];
                    String currentFileName = f.getName();
                    if (currentFileName.endsWith(".json")) {
                        String line;
                        System.out.println("Reading file : " + f.getAbsolutePath());
                        InputStream is = new FileInputStream(f.getAbsolutePath());
                        br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                        while ((line = br.readLine()) != null) {
                            ClassifiedTweet tweet = getClassifiedTweet(line);

                            if (tweetsList.size() <= maxCSVWriteSize && currentSize <= exportLimit) {
                                //write to arrayList
                                tweetsList.add(tweet);
                                currentSize++;
                            } else {

                                //write csv file
                                beanWriter = csv.writeClassifiedTweetsCSV(tweetsList, collectionCode, fileNameforCSVGen, beanWriter);
                                // empty arraylist
                                tweetsList.clear();

                            }
                            if (beanWriter != null) {
                                if (currentSize >= exportLimit) {
                                    break createTweetList;
                                }
                            }
                        }
                        beanWriter = csv.writeClassifiedTweetsCSV(tweetsList, collectionCode, fileNameforCSVGen, beanWriter);
                        tweetsList.clear();
                        br.close();
                    }
                }
            }
            //fileName = csv.writeCollectorTweetIDSCSV(tweetsList, collectionCode, fileNameforCSVGen);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, "File not found." + ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(ReadWriteCSV.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return fileName;
    }
    
    public List<ClassifiedTweet> getNClassifiedTweetsJSON(String collectionCode, int exportLimit) {
        List<ClassifiedTweet> tweetsList = new ArrayList();
        BufferedReader br = null;
        try {

            String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode;
            File folder = new File(folderLocation);
            File[] listOfFiles = folder.listFiles();
            // to get only Tagger's files
            ArrayList<File> taggerFilesList = new ArrayList();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (StringUtils.startsWith(listOfFiles[i].getName(), "Classified_")) {
                    taggerFilesList.add(listOfFiles[i]);
                }
            }
            Object[] objectsArray = taggerFilesList.toArray();
            File[] taggerFiles = Arrays.copyOf(objectsArray, objectsArray.length, File[].class);
            Arrays.sort(taggerFiles, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                }
            });
            
                for (int i = 0; i < taggerFiles.length; i++) {
                    File f = taggerFiles[i];
                    String currentFileName = f.getName();
                    if (currentFileName.endsWith(".json")) {
                        String line;
                        System.out.println("Reading file : " + f.getAbsolutePath());
                        InputStream is = new FileInputStream(f.getAbsolutePath());
                        br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                        while ((line = br.readLine()) != null) {
                            ClassifiedTweet tweet = getClassifiedTweet(line);
                            if (tweetsList.size() < exportLimit) {
                                tweetsList.add(tweet);
                            } else {
                              break;
                            }
                        }
                    }
                }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, "File not found." + ex);
        } catch (IOException ex) {
            Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return tweetsList;
    }

    public static void main(String[] args) {
        JsonDeserializer jc = new JsonDeserializer();
        String fileName = jc.generateClassifiedJson2TweetIdsCSV("iscram2014");
        //String fileName = jc.generateJson2TweetIdsCSV("prism_nsa");
        System.out.println("File name: " + fileName);

        //jc.generateJson2TweetIdsCSV("syria_en");
        //FileSystemOperations.deleteFile("CandFlood2013_20130922_vol-1.json.csv");
    }

    private Tweet getTweet(String line) throws ParseException {
        Tweet tweet = new Tweet();
        Object obj = JSONValue.parseStrict(line);
        JSONObject jsonObj = (JSONObject) obj;
        tweet.setTweetID(jsonObj.get("id").toString());
        tweet.setMessage(jsonObj.get("text").toString());
        tweet.setCreatedAt(jsonObj.get("created_at").toString());

        JSONObject jsonUserObj = (JSONObject) jsonObj.get("user");
        tweet.setUserID(jsonUserObj.get("id").toString());
        String userScreenName = jsonUserObj.get("screen_name").toString();
        tweet.setUserName(userScreenName);
        tweet.setTweetURL("https://twitter.com/" + userScreenName + "/status/" + tweet.getTweetID());
        if (jsonUserObj.get("url") != null) {
            tweet.setUserURL(jsonUserObj.get("url").toString());
        }

        return tweet;
    }

    private ClassifiedTweet getClassifiedTweet(String line) {
        ClassifiedTweet tweet = new ClassifiedTweet();
        try {

            Object obj = JSONValue.parseStrict(line);
            JSONObject jsonObj = (JSONObject) obj;
            if (jsonObj.get("id") != null) {
                tweet.setTweetID(jsonObj.get("id").toString());
            }

            if (jsonObj.get("text") != null) {
                tweet.setMessage(jsonObj.get("text").toString());
            }

            if (jsonObj.get("created_at") != null) {
                tweet.setCreatedAt(jsonObj.get("created_at").toString());
            }

            JSONObject jsonUserObj = null;
            if (jsonObj.get("user") != null) {
                jsonUserObj = (JSONObject) jsonObj.get("user");
                if (jsonUserObj.get("id") != null) {
                    tweet.setUserID(jsonUserObj.get("id").toString());
                }

                if (jsonUserObj.get("screen_name") != null) {
                    tweet.setUserName(jsonUserObj.get("screen_name").toString());
                    tweet.setTweetURL("https://twitter.com/" + tweet.getUserName() + "/status/" + tweet.getTweetID());
                }
                if (jsonUserObj.get("url") != null) {
                    tweet.setUserURL(jsonUserObj.get("url").toString());
                }
            }

            JSONObject aidrObject = null;
            if (jsonObj.get("aidr") != null) {
                aidrObject = (JSONObject) jsonObj.get("aidr");
                if (aidrObject.get("crisis_name") != null) {
                    tweet.setCrisisName(aidrObject.get("crisis_name").toString());
                }

                if (aidrObject.get("nominal_labels") != null) {
                    JSONArray nominalLabels = (JSONArray) aidrObject.get("nominal_labels");
                    String allLabelNames = "";
                    String allLabelDescriptinos = "";
                    String allConfidences = "";
                    String humanLabeled = "";
                    for (int i = 0; i < nominalLabels.size(); i++) {
                        JSONObject label = (JSONObject) nominalLabels.get(i);
                        allLabelNames += label.get("label_name") + ";";
                        allLabelDescriptinos += label.get("label_description") + ";";
                        allConfidences += label.get("confidence") + ";";
                        humanLabeled += label.get("from_human") + ";";

                    }

                    tweet.setLabelName(allLabelNames);
                    tweet.setLabelDescription(allLabelDescriptinos);
                    tweet.setConfidence(allConfidences);
                    tweet.setHumanLabeled(humanLabeled);
                }

            }

        } catch (ParseException ex) {
            //Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tweet;
    }
}
