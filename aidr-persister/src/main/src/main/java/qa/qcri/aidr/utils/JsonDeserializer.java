/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.utils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;

import org.supercsv.io.ICsvBeanWriter;

import qa.qcri.aidr.persister.filter.GenericInputQuery;
import qa.qcri.aidr.persister.filter.JsonQueryList;
import qa.qcri.aidr.persister.filter.FilterQueryMatcher;
import qa.qcri.aidr.persister.filter.NominalLabel;
import qa.qcri.aidr.utils.Config;

import java.io.*;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;






//import net.minidev.json.JSONObject;
//import net.minidev.json.JSONValue;
//import net.minidev.json.JSONArray;
//import net.minidev.json.parser.ParseException;
import qa.qcri.aidr.utils.Tweet;
import qa.qcri.aidr.io.FileSystemOperations;
import qa.qcri.aidr.io.ReadWriteCSV;

import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


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
		String fileToDelete = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + "Classified_" + collectionCode + "_tweetIds.csv";
		System.out.println("Deleteing file : " + fileToDelete);
		FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name
		System.out.println(fileNames);
		for (String file : fileNames) {
			String fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + file;
			System.out.println("Reading file " + fileLocation);
			try {
				br = new BufferedReader(new FileReader(fileLocation));
				String line;
				while ((line = br.readLine()) != null) {
					ClassifiedTweet tweet = getClassifiedTweet(line);
					if (tweet != null) {
						if (tweet.getLabelName() != null) {
							tweetsList.add(tweet);
						}
						System.out.println(tweet.getTweetID());
						if (tweetsList.size() <= 10000) { //after every 10k write to CSV file
							if (tweet.getLabelName() != null) {
								tweetsList.add(tweet);
							}
						} else {
							fileName = csv.writeClassifiedTweetIDsCSV(tweetsList, collectionCode, "Classified_" + collectionCode + "_tweetIds");
							tweetsList.clear();
						}
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

	/**
	 * 
	 * @param collectionCode
	 * @param selectedLabels list of user provided label names for filtering tweets
	 * @return JSON to CSV converted tweet IDs filtered by user selected label name
	 */
	public String generateClassifiedJson2TweetIdsCSVFiltered(final String collectionCode, 
			final JsonQueryList queryList) {
		String fileName = "";
		List<String> fileNames = FileSystemOperations.getClassifiedFileVolumes(collectionCode);
		List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>();
		ReadWriteCSV csv = new ReadWriteCSV();
		BufferedReader br = null;
		String fileToDelete = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + "Classified_" + collectionCode + "_tweetIds_filtered.csv";
		System.out.println("Deleteing file : " + fileToDelete);
		FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name

		// Added by koushik - first build the FilterQueryMatcher
		FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
		tweetFilter.queryList.setConstraints(queryList);
		tweetFilter.buildMatcherArray();

		for (String file : fileNames) {
			String fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + file;
			System.out.println("Reading file " + fileLocation);
			try {
				br = new BufferedReader(new FileReader(fileLocation));
				String line;
				while ((line = br.readLine()) != null) {
					ClassifiedTweet tweet = getClassifiedTweet(line);
					if (tweet != null) {
						// Apply filter on tweet
						if (tweet.getLabelName() != null && tweetFilter.getMatcherResult(tweet)) {
							tweetsList.add(tweet);		
						}
						if (tweetsList.size() <= 10000) { //after every 10k write to CSV file
							// Apply filter on tweet
							if (tweet.getLabelName() != null && tweetFilter.getMatcherResult(tweet)) {
								tweetsList.add(tweet);		// Question: WHY DUPLICATE ADDITION? 
							}
						} else {
							fileName = csv.writeClassifiedTweetIDsCSV(tweetsList, collectionCode, "Classified_" + collectionCode + "_tweetIds_filtered");
							tweetsList.clear();
						}
					}
				}

				br.close();

			} catch (FileNotFoundException ex) {
				Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		fileName = csv.writeClassifiedTweetIDsCSV(tweetsList, collectionCode, "Classified_" + collectionCode + "_tweetIds_filtered");
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

			String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output";
			String fileNameforCSVGen = "Classified_" + collectionCode + "_last_100k_tweets";
			fileName = fileNameforCSVGen + ".csv";
			FileSystemOperations.deleteFile(folderLocation + "/" + fileNameforCSVGen + ".csv");

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
					return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());	// koushik: changed sort order?
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
							if (tweet != null) {
								if (tweetsList.size() <= maxCSVWriteSize && currentSize <= exportLimit) {
									//write to arrayList
									if (tweet.getLabelName() != null) {
										tweetsList.add(tweet);
										System.out.println("Added TWEET: " + tweet);
										currentSize++;
									}
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

	/**
	 * 
	 * @param collectionCode c
	 * @param exportLimit
	 * @param selectedLabels list of user provided label names for filtering tweets
	 * @return JSON to CSV converted 100K tweets filtered by user selected label name
	 */
	public String taggerGenerateJSON2CSV_100K_BasedOnTweetCountFiltered(String collectionCode, 
			int exportLimit, 
			final JsonQueryList queryList) {
		BufferedReader br = null;
		String fileName = "";
		ICsvBeanWriter beanWriter = null;

		try {

			String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output";
			String fileNameforCSVGen = "Classified_" + collectionCode + "_last_100k_tweets_filtered";
			fileName = fileNameforCSVGen + ".csv";
			FileSystemOperations.deleteFile(folderLocation + "/" + fileNameforCSVGen + ".csv");

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();
			// to get only Tagger's files
			ArrayList<File> taggerFilesList = new ArrayList<File>();
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

			List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>();
			ReadWriteCSV csv = new ReadWriteCSV();
			long maxCSVWriteSize = 10000;			// Imran: this is for in-memory storage restrictions
			long currentSize = 0;

			createTweetList:
			{
				// Added by koushik - first build the FilterQueryMatcher
				FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
				tweetFilter.queryList.setConstraints(queryList);
				tweetFilter.buildMatcherArray();

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
							if (tweet != null) {
								if (tweetsList.size() <= maxCSVWriteSize && currentSize <= exportLimit) {
									// Apply filter on tweet
									if (tweet.getLabelName() != null && tweetFilter.getMatcherResult(tweet)) { 
										//write to arrayList
										tweetsList.add(tweet);
										System.out.println("filtered TWEET: " + tweet);
										currentSize++;
									}
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

	/**
	 * 
	 * @param collectionCode
	 * @param exportLimit
	 * @param selectedLabels list of user provided label names for filtering tweets
	 * @return JSON format classified tweets filtered by user selected label name
	 */
	public List<ClassifiedTweet> getNClassifiedTweetsJSONFiltered(String collectionCode, 
			int exportLimit,
			final JsonQueryList queryList) {
		List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>();
		BufferedReader br = null;
		try {

			String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode;
			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();
			// to get only Tagger's files
			ArrayList<File> taggerFilesList = new ArrayList<File>();
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

			// Added by koushik - first build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

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
						// Apply filter on tweet
						if (tweetsList.size() < exportLimit) {
							if (tweetFilter.getMatcherResult(tweet)) {
								//write to arrayList
								tweetsList.add(tweet);
							}
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
		String fileName = jc.generateClassifiedJson2TweetIdsCSV("2014-04-chile_earthquake_2014");
		//String fileName = jc.taggerGenerateJSON2CSV_100K_BasedOnTweetCount("2014-04-chile_earthquake_2014", 10);
		//String fileName = jc.generateJson2TweetIdsCSV("prism_nsa");
		System.out.println("File name: " + fileName);

		//testFilterAPIs();
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


	public ClassifiedTweet getClassifiedTweet(String line) {
		//System.out.println("Tweet to PARSE: " + line);
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

						// Added by koushik
						NominalLabel nLabel = new NominalLabel();
						nLabel.attibute_code = label.get("attribute_code").toString();
						nLabel.label_code = label.get("label_code") != null ? label.get("label_code").toString() : null;
						nLabel.confidence = Float.parseFloat(label.get("confidence").toString());

						nLabel.attribute_name = label.get("attribute_name").toString();
						nLabel.label_name = label.get("label_name").toString();
						nLabel.attribute_description = label.get("attribute_description").toString();
						nLabel.label_description = label.get("label_description").toString();
						nLabel.from_human = Boolean.parseBoolean(label.get("from_human").toString());

						tweet.nominal_labels.add(nLabel);
					}

					tweet.setLabelName(allLabelNames);
					tweet.setLabelDescription(allLabelDescriptinos);
					tweet.setConfidence(allConfidences);
					tweet.setHumanLabeled(humanLabeled);
				} else {
					System.err.println("[getClassifiedTweet] tweet without label: " + line);		        	
				}
			}
			//System.out.println("Parsed tweet: " + tweet.toString());

		} catch (ParseException ex) {
			Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("[getClassifiedTweet] Offending tweet: " + line);
			return null;
		}
		return tweet;
	}

	/*
	public ClassifiedTweet getClassifiedTweet(String line) {
		//System.out.println("Tweet to PARSE: " + line);
		ObjectMapper mapper = new ObjectMapper();
		ClassifiedTweet tweet = new ClassifiedTweet();
		try {
			Gson jsonObject = new GsonBuilder().serializeNulls().disableHtmlEscaping()
					.serializeSpecialFloatingPointValues()	//.setPrettyPrinting()
					.create();
			JsonParser parser = new JsonParser();
			JsonObject jsonObj = (JsonObject) parser.parse(line);


			if (jsonObj.get("id") != null) {
				tweet.setTweetID(jsonObj.get("id").getAsString());
			}

			if (jsonObj.get("text") != null) {
				tweet.setMessage(jsonObj.get("text").getAsString());
			}

			if (jsonObj.get("created_at") != null) {
				tweet.setCreatedAt(jsonObj.get("created_at").getAsString());
			}

			JsonObject jsonUserObj = null;
			if (jsonObj.get("user") != null) {				
				jsonUserObj = jsonObj.get("user").getAsJsonObject();
				if (jsonUserObj.get("id") != null) {
					tweet.setUserID(jsonUserObj.get("id").getAsString());
				}

				if (jsonUserObj.get("screen_name") != null) {
					tweet.setUserName(jsonUserObj.get("screen_name").getAsString());
					tweet.setTweetURL("https://twitter.com/" + tweet.getUserName() + "/status/" + tweet.getTweetID());
				}
				if (jsonUserObj.get("url") != null) {
					tweet.setUserURL(jsonUserObj.get("url").toString());
				}
			}

			JsonObject aidrObject = null;
			if (jsonObj.get("aidr") != null) {
				aidrObject = jsonObj.get("aidr").getAsJsonObject();
				if (aidrObject.get("crisis_name") != null) {
					tweet.setCrisisName(aidrObject.get("crisis_name").getAsString());
				}

				if (aidrObject.get("nominal_labels") != null) {
					//JSONArray nominalLabels = (JSONArray) aidrObject.get("nominal_labels");
					JsonArray nominalLabels = aidrObject.get("nominal_labels").getAsJsonArray();
					String allLabelNames = "";
					String allLabelDescriptinos = "";
					String allConfidences = "";
					String humanLabeled = "";
					for (int i = 0; i < nominalLabels.size(); i++) {
						//JSONObject label = (JSONObject) nominalLabels.get(i);
						JsonObject label = nominalLabels.get(i).getAsJsonObject();
						allLabelNames += label.get("label_name") + ";";
						allLabelDescriptinos += label.get("label_description") + ";";
						allConfidences += label.get("confidence") + ";";
						humanLabeled += label.get("from_human") + ";";

						// Added by koushik
						NominalLabel nLabel = new NominalLabel();
						nLabel.attibute_code = label.get("attribute_code").getAsString();
						nLabel.label_code = label.get("label_code") != null ? label.get("label_code").getAsString() : null;
						nLabel.confidence = Float.parseFloat(label.get("confidence").getAsString());

						nLabel.attribute_name = label.get("attribute_name").getAsString();
						nLabel.label_name = label.get("label_name").getAsString();
						nLabel.attribute_description = label.get("attribute_description").getAsString();
						nLabel.label_description = label.get("label_description").getAsString();
						nLabel.from_human = Boolean.parseBoolean(label.get("from_human").getAsString());

						tweet.nominal_labels.add(nLabel);
					}

					tweet.setLabelName(allLabelNames);
					tweet.setLabelDescription(allLabelDescriptinos);
					tweet.setConfidence(allConfidences);
					tweet.setHumanLabeled(humanLabeled);
				} else {
					System.err.println("[getClassifiedTweet] tweet without label: " + line);		        	
				}
			}
			//System.out.println("Parsed tweet: " + tweet.toString());

		} catch (Exception ex) {
			Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("[getClassifiedTweet] Offending tweet: " + line);
			return null;
		}
		return tweet;
	}
	 */
}
