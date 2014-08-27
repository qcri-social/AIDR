/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.utils;


import net.minidev.json.JSONObject;

import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvMapWriter;

import qa.qcri.aidr.persister.filter.JsonQueryList;
import qa.qcri.aidr.persister.filter.FilterQueryMatcher;
import qa.qcri.aidr.persister.filter.NominalLabel;
import qa.qcri.aidr.utils.Config;

import java.io.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import qa.qcri.aidr.utils.Tweet;
import qa.qcri.aidr.io.FileSystemOperations;
import qa.qcri.aidr.io.ReadWriteCSV;
import qa.qcri.aidr.logging.ErrorLog;

import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 *
 * @author Imran
 */
public class JsonDeserializer {

	private static Logger logger = Logger.getLogger(JsonDeserializer.class.getName());
	private static ErrorLog elog = new ErrorLog();

	private static final int BUFFER_SIZE = 10 * 1024 * 1024;	// buffer size to use for buffered r/w
	private static final int LIST_BUFFER_SIZE = 50000; 


	//This method generates tweetIds csv from all the jsons of a collection
	public String generateJson2TweetIdsCSV(String collectionCode, boolean downloadLimited) {
		List<String> fileNames = FileSystemOperations.getAllJSONFileVolumes(collectionCode);
		List<Tweet> tweetsList = new ArrayList<Tweet>(LIST_BUFFER_SIZE);

		ICsvBeanWriter beanWriter = null;
		//String fileName = collectionCode + "_tweetIds" + ".csv";		
		String fileNameforCSVGen = collectionCode + "_tweetIds";
		String fileName = fileNameforCSVGen + ".csv";
		long lastCount = 0;
		long currentCount = 0;
		try {
			ReadWriteCSV csv = new ReadWriteCSV();
			BufferedReader br = null;
			String fileToDelete = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/" + collectionCode + "_tweetIds.csv";
			FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name

			int totalCount = 0;
			for (String file : fileNames) {
				String fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/" + file;
				try {
					br = new BufferedReader(new FileReader(fileLocation));
					String line;
					while ((line = br.readLine()) != null) {
						if (downloadLimited && totalCount < Config.DEFAULT_TWEETID_VOLUME_LIMIT) {
							break;
						}
						Tweet tweet = getTweet(line);
						if (tweet != null) {
							if (tweetsList.size() < LIST_BUFFER_SIZE) { //after every 10k write to CSV file
								tweetsList.add(tweet);
							} else {
								beanWriter = csv.writeCollectorTweetIDSCSV(beanWriter, tweetsList, collectionCode, fileNameforCSVGen);
								lastCount = currentCount;
								currentCount += tweetsList.size();
								logger.info(collectionCode + ": Writing_tweetIds: " + lastCount + " to " + currentCount);
								tweetsList.clear();
								tweetsList.add(tweet);	
							}
							++totalCount;
						}
					}
					br.close();
				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file = " + fileLocation);
					logger.error(elog.toStringException(ex));
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file = " + fileLocation);
					logger.error(elog.toStringException(ex));
				} 
			}
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
					logger.error(elog.toStringException(ex));
				}
			}
		}
		//beanWriter = csv.writeCollectorTweetIDSCSV(beanWriter, tweetsList, collectionCode, collectionCode + "_tweetIds");
		tweetsList.clear();

		return fileName;
	}

	public String generateClassifiedJson2TweetIdsCSV(String collectionCode, final boolean downloadLimited) {

		ICsvMapWriter writer = null;
		String fileNameforCSVGen = "Classified_" + collectionCode + "_tweetIds";
		String fileName = fileNameforCSVGen + ".csv";

		try {
			List<String> fileNames = FileSystemOperations.getClassifiedFileVolumes(collectionCode);
			List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>(LIST_BUFFER_SIZE);

			ReadWriteCSV csv = new ReadWriteCSV();
			BufferedReader br = null;
			String fileToDelete = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + "Classified_" + collectionCode + "_tweetIds.csv";
			logger.info(collectionCode + ": Deleteing file : " + fileToDelete);
			FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name
			//logger.info(fileNames);

			long lastCount = 0;
			long currentCount = 0;
			int totalCount = 0;
			for (String file : fileNames) {
				String fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + file;
				logger.info(collectionCode + ": Reading file " + fileLocation);
				try {
					br = new BufferedReader(new FileReader(fileLocation));
					String line;
					while ((line = br.readLine()) != null) {
						if (downloadLimited && totalCount < Config.DEFAULT_TWEETID_VOLUME_LIMIT) {
							break;
						}
						ClassifiedTweet tweet = getClassifiedTweet(line);
						if (tweet != null) {

							if (tweetsList.size() < LIST_BUFFER_SIZE) { //after every 10k write to CSV file
								if (!tweet.getNominalLabels().isEmpty()) {
									tweetsList.add(tweet);

								}
							} else {
								writer = csv.writeClassifiedTweetIDsCSV(writer, tweetsList, collectionCode, fileNameforCSVGen);
								lastCount = currentCount;
								currentCount += tweetsList.size();
								logger.info(collectionCode + ": Writing_tweetIds: " + lastCount + " to " + currentCount);
								tweetsList.clear();
								tweetsList.add(tweet);
							}
							++totalCount;
						}
					}
					br.close();

				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file = " + fileLocation);
					logger.error(elog.toStringException(ex));
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file = " + fileLocation);
					logger.error(elog.toStringException(ex));
				} 
				//beanWriter = csv.writeClassifiedTweetIDsCSV(beanWriter, tempList, collectionCode, fileNameforCSVGen);
			}	// end for 
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
					logger.error(elog.toStringException(ex));
				}
			}
		}

		return fileName;
	}

	/**
	 * 
	 * @param collectionCode
	 * @param selectedLabels list of user provided label names for filtering tweets
	 * @return JSON to CSV converted tweet IDs filtered by user selected label name
	 */
	public String generateClassifiedJson2TweetIdsCSVFiltered(final String collectionCode, 
			final JsonQueryList queryList, final boolean downloadLimited) {
		ICsvMapWriter writer = null;
		String fileNameforCSVGen = "Classified_" + collectionCode + "_tweetIds_filtered";
		String fileName = fileNameforCSVGen + ".csv";
		List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>(LIST_BUFFER_SIZE);

		try {
			List<String> fileNames = FileSystemOperations.getClassifiedFileVolumes(collectionCode);

			ReadWriteCSV csv = new ReadWriteCSV();
			BufferedReader br = null;
			String fileToDelete = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + "Classified_" + collectionCode + "_tweetIds_filtered.csv";
			logger.info(collectionCode + ": Deleteing file : " + fileToDelete);
			FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name

			// Added by koushik - first build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

			int totalCount = 0;
			for (String file : fileNames) {
				if (downloadLimited && totalCount < Config.DEFAULT_TWEETID_VOLUME_LIMIT) {
					break;
				}
				String fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + file;
				logger.info(collectionCode + ": Reading file " + fileLocation);
				try {
					br = new BufferedReader(new FileReader(fileLocation));
					String line;
					while ((line = br.readLine()) != null) {
						if (downloadLimited && totalCount < Config.DEFAULT_TWEETID_VOLUME_LIMIT) {
							break;
						}
						ClassifiedTweet tweet = getClassifiedTweet(line);
						if (tweet != null) {
							// Apply filter on tweet
							/*
						if (!tweet.getNominalLabels().isEmpty() && tweetFilter.getMatcherResult(tweet)) {
							tweetsList.add(tweet);		
						}*/
							if (tweetsList.size() < LIST_BUFFER_SIZE) { //after every 10k write to CSV file
								// Apply filter on tweet
								if (!tweet.getNominalLabels().isEmpty() && tweetFilter.getMatcherResult(tweet)) {
									tweetsList.add(tweet);		// Question: WHY DUPLICATE ADDITION? 
								}
							} else {
								writer = csv.writeClassifiedTweetIDsCSV(writer, tweetsList, collectionCode, fileNameforCSVGen);
								tweetsList.clear();
								tweetsList.add(tweet);
							}
							++totalCount;
						}
					}

					br.close();

				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file = " + fileLocation);
					logger.error(elog.toStringException(ex));
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file = " + fileLocation);
					logger.error(elog.toStringException(ex));
				} 		
			}
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
					logger.error(elog.toStringException(ex));
				}
			}
		}

		//beanWriter = csv.writeClassifiedTweetIDsCSV(beanWriter, tweetsList, collectionCode, fileNameforCSVGen);
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

			List<Tweet> tweetsList = new ArrayList<Tweet>();
			ReadWriteCSV csv = new ReadWriteCSV();
			long currentSize = 0;

			createTweetList:
			{
				for (int i = 0; i < listOfFiles.length; i++) {
					File f = listOfFiles[i];
					String currentFileName = f.getName();
					if (currentFileName.endsWith(".json")
							&& currentFileName.contains("vol")) {
						String line;
						logger.info(collectionCode + ": Reading file : " + f.getAbsolutePath());
						InputStream is = new FileInputStream(f.getAbsolutePath());
						br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
						while ((line = br.readLine()) != null) {
							try {
								Tweet tweet = getTweet(line);
								if (tweet != null) {
									if (tweetsList.size() < LIST_BUFFER_SIZE && currentSize <= Config.TWEETS_EXPORT_LIMIT_100K) {
										//write to arrayList
										tweetsList.add(tweet);
										currentSize++;
										//logger.info("currentSize  : " + currentSize);
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
								}
							} catch (Exception ex) {
								//Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, "JSON file parsing exception at line {0}", lineNumber);
							}
						}
						beanWriter = csv.writeCollectorTweetsCSV(tweetsList, collectionCode, fileNameforCSVGen, beanWriter);
						logger.info(collectionCode + ": final beanWriter  : " + beanWriter.getRowNumber());
						tweetsList.clear();
						br.close();
					}
				}
			}
			//fileName = csv.writeCollectorTweetIDSCSV(tweetsList, collectionCode, fileNameforCSVGen);

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
			logger.error(elog.toStringException(ex));
			isCSVGenerated = false;
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file");
			logger.error(elog.toStringException(ex));
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
					logger.error(elog.toStringException(ex));
				}
			}
		}
		return fileName;
	}

	public String taggerGenerateJSON2CSV_100K_BasedOnTweetCount(String collectionCode, int exportLimit) {
		BufferedReader br = null;
		String fileName = "";
		ICsvMapWriter writer = null;

		try {

			String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output";
			String fileNameforCSVGen = "Classified_" + collectionCode + "_last_100k_tweets";
			fileName = fileNameforCSVGen + ".csv";
			FileSystemOperations.deleteFile(folderLocation + "/" + fileNameforCSVGen + ".csv");

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();
			// to get only Tagger's files
			ArrayList<File> taggerFilesList = new ArrayList<File>();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (StringUtils.startsWith(listOfFiles[i].getName(), "Classified_")
						&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) {
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

			List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>();
			ReadWriteCSV csv = new ReadWriteCSV();
			long currentSize = 0;

			createTweetList:
			{
				for (int i = 0; i < taggerFiles.length; i++) {
					File f = taggerFiles[i];
					String currentFileName = f.getName();
					if (currentFileName.endsWith(".json")) {
						String line;
						logger.info(collectionCode + ": Reading file : " + f.getAbsolutePath());
						InputStream is = new FileInputStream(f.getAbsolutePath());
						br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
						while (((line = br.readLine()) != null)
								&& (currentSize < exportLimit)) {
							ClassifiedTweet tweet = getClassifiedTweet(line);
							if (tweet != null) {
								if (tweetsList.size() < LIST_BUFFER_SIZE && currentSize <= exportLimit) {
									//write to arrayList
									if (!tweet.getNominalLabels().isEmpty()) {
										tweetsList.add(tweet);
										//System.out.println("Added TWEET: " + tweet.getLabelName() + ", " + tweet.getConfidence());
										currentSize++;
									}
								} else {

									//write csv file
									writer = csv.writeClassifiedTweetsCSV(tweetsList, collectionCode, fileNameforCSVGen, writer);
									// empty arraylist
									tweetsList.clear();

								}
							}
						}
						//writer = csv.writeClassifiedTweetsCSV(tweetsList, collectionCode, fileNameforCSVGen, writer);
						tweetsList.clear();
						br.close();
					}
				}
			}
			//fileName = csv.writeCollectorTweetIDSCSV(tweetsList, collectionCode, fileNameforCSVGen);

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": File not found.");
			logger.error(elog.toStringException(ex));
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
			logger.error(elog.toStringException(ex));
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
					logger.error(elog.toStringException(ex));
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
		ICsvMapWriter writer = null;

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
				if (StringUtils.startsWith(listOfFiles[i].getName(), "Classified_")
						&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) {
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
			long currentSize = 0;

			createTweetList:
			{
				//First build the FilterQueryMatcher
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
								if (tweetsList.size() < LIST_BUFFER_SIZE && currentSize <= exportLimit) {
									// Apply filter on tweet
									if (!tweet.getNominalLabels().isEmpty() && tweetFilter.getMatcherResult(tweet)) { 
										//write to arrayList
										tweetsList.add(tweet);
										//System.out.println("filtered TWEET with label: " + tweet.getLabelName() + ", " + tweet.getConfidence());
										currentSize++;
									}
								} else {

									//write csv file
									writer = csv.writeClassifiedTweetsCSV(tweetsList, collectionCode, fileNameforCSVGen, writer);
									// empty arraylist
									tweetsList.clear();

								}
								if (writer != null) {
									if (currentSize >= exportLimit) {
										break createTweetList;
									}
								}
							}
						}
						writer = csv.writeClassifiedTweetsCSV(tweetsList, collectionCode, fileNameforCSVGen,writer);
						tweetsList.clear();
						br.close();
					}
				}
			}
			//fileName = csv.writeCollectorTweetIDSCSV(tweetsList, collectionCode, fileNameforCSVGen);

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
			logger.error(elog.toStringException(ex));
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
			logger.error(elog.toStringException(ex));
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
					logger.error(elog.toStringException(ex));
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
				if (StringUtils.startsWith(listOfFiles[i].getName(), "Classified_")
						&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) {
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
			logger.error(collectionCode + ": couldn't find file");
			logger.error(elog.toStringException(ex));
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
			logger.error(elog.toStringException(ex));
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
				if (StringUtils.startsWith(listOfFiles[i].getName(), "Classified_")
						&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) {
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
			logger.error(collectionCode + ": couldn't find file");
			logger.error(elog.toStringException(ex));
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
			logger.error(elog.toStringException(ex));
		} 
		return tweetsList;
	}


	public static void main(String[] args) {
		JsonDeserializer jc = new JsonDeserializer();
		String fileName = jc.generateClassifiedJson2TweetIdsCSV("2014-04-chile_earthquake_2014", false);
		//String fileName = jc.taggerGenerateJSON2CSV_100K_BasedOnTweetCount("2014-04-chile_earthquake_2014", 10);
		//String fileName = jc.generateJson2TweetIdsCSV("prism_nsa");
		System.out.println("File name: " + fileName);

		//testFilterAPIs();
		//jc.generateJson2TweetIdsCSV("syria_en");
		//FileSystemOperations.deleteFile("CandFlood2013_20130922_vol-1.json.csv");
	}

	/*
	private Tweet getTweet(String line) throws ParseException {
		Tweet tweet = new Tweet();
		try {
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
		} catch (ParseException ex) {
			Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("[getTweet] Offending tweet: " + line);
			return null;
		}

		return tweet;
	}
	 */

	private Tweet getTweet(String line) {
		Tweet tweet = new Tweet();
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
		} catch (Exception ex) {
			//Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
			//System.out.println("[getTweet] Offending tweet: " + line);
			return null;
		}
		return tweet;
	}


	// getClassifiedTweet method using net.minidev.json library for parsing JSON string
	/*
	public ClassifiedTweet getClassifiedTweet(String line) {
		//System.out.println("Tweet to PARSE: " + line);
		ClassifiedTweet tweet = new ClassifiedTweet();
		try {
			if (line != null && !line.isEmpty()) {
				//System.out.println("[getClassifiedTweet] input tweet: " + line);
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
					if (jsonObj.get("user") instanceof JSONObject) {
						jsonUserObj = (JSONObject) jsonObj.get("user");
					} else if (jsonObj.get("user") instanceof JSONArray) {
						JSONArray temp = (JSONArray) jsonObj.get("user");
						jsonUserObj = (JSONObject) temp.get(0);
					}
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
							nLabel.attribute_code = label.get("attribute_code") != null ? label.get("attribute_code").toString() : null;
							nLabel.label_code = label.get("label_code") != null ? label.get("label_code").toString() : null;
							try {
								float conf = 0;
								if (label.get("confidence") instanceof Float) { 
									conf = (float) label.get("confidence");
								} else if (label.get("confidence") instanceof Integer) {
									Integer temp = (Integer) label.get("confidence");
									conf = temp.floatValue();
									//System.out.println("confidence as Integer!!!");
								} 
								nLabel.confidence = conf;
							} catch (Exception ex) {
								ex.printStackTrace();
								//System.err.println("Error in parsing float confidence field");
								nLabel.confidence = 1;
							}

							nLabel.attribute_name = label.get("attribute_name") != null ? label.get("attribute_name").toString() : null;
							nLabel.label_name = label.get("label_name") != null ? label.get("label_name").toString() : null;
							nLabel.attribute_description = label.get("attribute_description") != null ? label.get("attribute_description").toString() : null;
							nLabel.label_description = label.get("label_description") != null ? label.get("label_description").toString() : null;
							try {	
								nLabel.from_human = (label.get("from_human") != null) ? (boolean) label.get("from_human") : false;
							} catch (Exception ex) {
								System.err.println("Error in parsing from_human field");
								ex.printStackTrace();
								nLabel.from_human = false;
							}
							tweet.nominal_labels.add(nLabel);
						}

						tweet.setLabelName(allLabelNames);
						tweet.setLabelDescription(allLabelDescriptinos);
						tweet.setConfidence(allConfidences);
						tweet.setHumanLabeled(humanLabeled);
					} else {
						//System.err.println("[getClassifiedTweet] tweet without label: " + line);		        	
					}
				}
				//System.out.println("Parsed tweet: " + tweet.toString());
				return tweet;
			} else {
				System.err.println("Input line: " + line);
				return null;
			}
		} catch (ParseException ex) {
			Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("[getClassifiedTweet] Offending tweet: " + line);
			System.out.println("[getClassifiedTweet] Returning null");
			return null;
		}
	}
	 */


	// getClassifiedTweet method using Gson library for parsing JSON string
	public ClassifiedTweet getClassifiedTweet(String line) {
		//System.out.println("Tweet to PARSE: " + line);

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
					StringBuffer allAttributeNames = new StringBuffer();
					StringBuffer allAttributeCodes = new StringBuffer();
					StringBuffer allLabelNames = new StringBuffer();
					StringBuffer allLabelCodes = new StringBuffer();
					StringBuffer allLabelDescriptions = new StringBuffer();
					StringBuffer allConfidences = new StringBuffer();
					StringBuffer humanLabeled = new StringBuffer();
					for (int i = 0; i < nominalLabels.size(); i++) {
						//JSONObject label = (JSONObject) nominalLabels.get(i);
						JsonObject label = nominalLabels.get(i).getAsJsonObject();
						allAttributeNames.append(label.get("attribute_name"));
						allAttributeCodes.append(label.get("attribute_code"));
						allLabelNames.append(label.get("label_name"));
						allLabelCodes.append(label.get("label_code"));
						allLabelDescriptions.append(label.get("label_description"));
						allConfidences.append(label.get("confidence"));
						humanLabeled.append(label.get("from_human"));

						// Added by koushik
						NominalLabel nLabel = new NominalLabel();
						nLabel.attribute_code = label.get("attribute_code").getAsString();
						nLabel.label_code = label.get("label_code") != null ? label.get("label_code").getAsString() : null;
						nLabel.confidence = Float.parseFloat(label.get("confidence").getAsString());

						nLabel.attribute_name = label.get("attribute_name").getAsString();
						nLabel.label_name = label.get("label_name").getAsString();
						nLabel.attribute_description = label.get("attribute_description").getAsString();
						nLabel.label_description = label.get("label_description").getAsString();
						nLabel.from_human = Boolean.parseBoolean(label.get("from_human").getAsString());

						tweet.nominal_labels.add(nLabel);

						// remove the ugly ';' from end-of-list
						if (i < nominalLabels.size() - 1) {
							allAttributeNames.append(";");
							allAttributeCodes.append(";");
							allLabelNames.append(";");
							allLabelDescriptions.append(";");
							allConfidences.append(";");
							humanLabeled.append(";");
						}
					}

					tweet.setAttributeName_1(allAttributeNames.toString());
					tweet.setAttributeCode_1(allAttributeCodes.toString());
					tweet.setLabelName_1(allLabelNames.toString());
					tweet.setLabelDescription_1(allLabelDescriptions.toString());
					tweet.setConfidence_1(allConfidences.toString());
					tweet.setHumanLabeled_1(humanLabeled.toString());
				} else {
					//logger.warn("tweet without label: " + line);		        	
				}
			}
			//logger.warn("Parsed tweet: " + tweet.toString());

		} catch (Exception ex) {
			//Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
			//logger.warn("Offending tweet: " + line);
			return null;
		}
		return tweet;
	}

	public String generateJSON2JSON_100K_BasedOnTweetCount(String collectionCode) {
		BufferedReader br = null;
		String fileName = "";
		BufferedWriter beanWriter = null;

		try {

			String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode;
			String fileNameforJsonGen = collectionCode + "_last_100k_tweets";
			fileName = fileNameforJsonGen + ".json";
			FileSystemOperations.deleteFile(Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/" + fileNameforJsonGen + ".json");

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();

			Arrays.sort(listOfFiles, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				}
			});

			long currentSize = 0;
			StringBuffer outputFile = new StringBuffer().append(folderLocation)
					.append("/")
					.append(fileName);
			beanWriter = new BufferedWriter(new FileWriter(outputFile.toString()), BUFFER_SIZE);

			boolean isDone = false;
			for (int i = 0; i < listOfFiles.length; i++) {
				File f = listOfFiles[i];
				String currentFileName = f.getName();
				if (currentFileName.endsWith(".json")
						&& currentFileName.contains("vol")) {
					String line;
					logger.info("Reading file : " + f.getAbsolutePath());
					InputStream is = new FileInputStream(f.getAbsolutePath());
					br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
					while ((line = br.readLine()) != null) {
						try {
							if (currentSize <= Config.TWEETS_EXPORT_LIMIT_100K) {
								//write to file
								beanWriter.write(line);
								beanWriter.newLine();
								++currentSize;
								// System.out.println("currentSize  : " + currentSize);
							} else {
								beanWriter.flush();
								isDone = true;
								break;
							}
						} catch (Exception ex) {
							//Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, "JSON file parsing exception at line {0}", lineNumber);
						}
					}	// end while
					br.close();
					if (isDone) {
						beanWriter.close();
						break;
					}
				}
			}	// end for		

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
			logger.error(elog.toStringException(ex));
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
			logger.error(elog.toStringException(ex));
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
					logger.error(elog.toStringException(ex));
				}
			}
		}
		return fileName;
	}

	public String generateJson2TweetIdsJson(String collectionCode, final boolean downloadLimited) {
		BufferedReader br = null;
		String fileName = "";
		BufferedWriter beanWriter = null;

		try {

			String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode;
			String fileNameforJsonGen = collectionCode + "_tweetIds";
			fileName = fileNameforJsonGen + ".json";
			FileSystemOperations.deleteFile(Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/" + fileNameforJsonGen + ".json");

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();

			Arrays.sort(listOfFiles, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				}
			});

			StringBuffer outputFile = new StringBuffer().append(folderLocation)
					.append("/")
					.append(fileName);
			beanWriter = new BufferedWriter(new FileWriter(outputFile.toString()), BUFFER_SIZE);
			int totalCount = 0;
			for (int i = 0; i < listOfFiles.length; i++) {
				File f = listOfFiles[i];
				String currentFileName = f.getName();
				if (currentFileName.endsWith(".json") 
						&& currentFileName.contains("vol")) {
					String line;
					logger.info("Reading file : " + f.getAbsolutePath());
					InputStream is = new FileInputStream(f.getAbsolutePath());
					br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
					while ((line = br.readLine()) != null) {
						if (downloadLimited && totalCount < Config.DEFAULT_TWEETID_VOLUME_LIMIT) {
							break;
						}
						try {
							Tweet tweet = getTweet(line);
							if (tweet != null && tweet.getTweetID() != null) {
								StringBuffer jsonString = new StringBuffer().append("{")
										.append("\"id\":")
										.append(tweet.getTweetID())
										.append("}");
								//write to file
								beanWriter.write(jsonString.toString());
								beanWriter.newLine();
								++totalCount;
							}
						} catch (Exception ex) {
							//Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, "JSON file parsing exception at line {0}", lineNumber);
						}
					}	// end while
					br.close();
				}
			}	// end for		
			beanWriter.flush();
			beanWriter.close();

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
			logger.error(elog.toStringException(ex));
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
			logger.error(elog.toStringException(ex));
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
					logger.error(elog.toStringException(ex));
				}
			}
		}
		return fileName;
	}

	public String taggerGenerateJSON2JSON_100K_BasedOnTweetCount(String collectionCode, int exportLimit) {
		BufferedReader br = null;
		String fileName = "";
		BufferedWriter beanWriter = null;

		try {

			String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output";
			String fileNameforJsonGen = "Classified_" + collectionCode + "_last_100k_tweets";
			fileName = fileNameforJsonGen + ".json";

			FileSystemOperations.deleteFile(folderLocation + "/" + fileNameforJsonGen + ".json");
			logger.info("Deleted existing file: " + folderLocation + "/" + fileNameforJsonGen + ".json");

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();
			// to get only Tagger's files
			ArrayList<File> taggerFilesList = new ArrayList();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (StringUtils.startsWith(listOfFiles[i].getName(), "Classified_")
						&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) {
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

			StringBuffer outputFile = new StringBuffer().append(folderLocation)
					.append("/")
					.append(fileName);
			beanWriter = new BufferedWriter(new FileWriter(outputFile.toString()), BUFFER_SIZE);

			long currentSize = 0;
			boolean isDone = false;
			for (int i = 0; i < taggerFiles.length; i++) {
				File f = taggerFiles[i];
				String currentFileName = f.getName();
				if (currentFileName.endsWith(".json") 
						&& currentFileName.contains("vol")) {
					String line;
					logger.info("Reading file : " + f.getAbsolutePath());
					InputStream is = new FileInputStream(f.getAbsolutePath());
					br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
					while ((line = br.readLine()) != null) {
						try {
							if (currentSize <= Config.TWEETS_EXPORT_LIMIT_100K) {
								//write to file
								beanWriter.write(line);
								beanWriter.newLine();
								++currentSize;
								// System.out.println("currentSize  : " + currentSize);
							} else {
								beanWriter.flush();
								isDone = true;
								break;
							}
						} catch (Exception ex) {
							//Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, "JSON file parsing exception at line {0}", lineNumber);
						}
					}	// end while						
					br.close();
					if (isDone) {
						beanWriter.close();
						break;
					}
				}
			}	// end for	
		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
			logger.error(elog.toStringException(ex));
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
			logger.error(elog.toStringException(ex));
		} catch (NullPointerException ex) {
			logger.error(collectionCode + ": empty list of files to read");
			logger.info(elog.toStringException(ex));
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
					logger.error(elog.toStringException(ex));
				}
			}
		}
		return fileName;
	}

	public String generateClassifiedJson2TweetIdsJSON(String collectionCode, final boolean downloadLimited) {
		BufferedWriter beanWriter = null;
		String folderLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output";
		String fileNameforJsonGen = "Classified_" + collectionCode + "_tweetIds";
		String fileName = fileNameforJsonGen + ".json";

		try {
			List<String> fileNames = FileSystemOperations.getClassifiedFileVolumes(collectionCode);

			BufferedReader br = null;
			String fileToDelete = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + "Classified_" + collectionCode + "_tweetIds.json";
			System.out.println("Deleteing file : " + fileToDelete);
			FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name
			//System.out.println(fileNames);

			StringBuffer outputFile = new StringBuffer().append(folderLocation)
					.append("/")
					.append(fileName);
			beanWriter = new BufferedWriter(new FileWriter(outputFile.toString()), BUFFER_SIZE);
			int totalCount = 0;
			for (String file : fileNames) {
				if (downloadLimited && totalCount < Config.DEFAULT_TWEETID_VOLUME_LIMIT) {
					break;
				}
				String fileLocation = Config.DEFAULT_PERSISTER_FILE_PATH + collectionCode + "/output/" + file;
				logger.info("Reading file " + fileLocation);
				try {
					br = new BufferedReader(new FileReader(fileLocation)); 
					String line;
					while ((line = br.readLine()) != null) {
						if (downloadLimited && totalCount < Config.DEFAULT_TWEETID_VOLUME_LIMIT) {
							break;
						}
						ClassifiedTweet tweet = getClassifiedTweet(line);
						if (tweet != null && tweet.getTweetID() != null) {
							if (!tweet.getNominalLabels().isEmpty()) {
								//write to file
								beanWriter.write(createJsonClassifiedTweetIDString(tweet));
								beanWriter.newLine();
							}
							++totalCount;
						}  
					}
					br.close();

				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file");
					logger.error(elog.toStringException(ex));
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file read");
					logger.error(elog.toStringException(ex));
				} 
			}	// end for 
			beanWriter.flush();
			beanWriter.close();

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
			logger.error(elog.toStringException(ex));
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
			logger.error(elog.toStringException(ex));
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
					logger.error(elog.toStringException(ex));
				}
			}
		}
		return fileName;
	}

	/*
	public String createJsonTweetIDString(Tweet tweet) {
		try {
			Gson jsonObject = new GsonBuilder().serializeNulls().disableHtmlEscaping()
					.serializeSpecialFloatingPointValues()
					.create();
			JsonParser parser = new JsonParser();
			String jsonString = jsonObject.toJson(tweet);
			return jsonString;
		} catch (Exception ex) {
			//Logger.getLogger(JsonDeserializer.class.getName()).log(Level.SEVERE, null, ex);
			//System.out.println("[createJsonTweetString] Offending tweet: " + line);
			return null;
		}
	}
	 */

	public String createJsonClassifiedTweetIDString(ClassifiedTweet tweet) {
		JSONObject obj = new JSONObject();

		obj.put("id", tweet.getTweetID());
		obj.put("attribute_name", tweet.getAttributeName_1());
		obj.put("attribute_code", tweet.getAttributeCode_1());
		obj.put("label_name", tweet.getLabelName_1());
		obj.put("label_description", tweet.getLabelDescription_1());
		obj.put("label_code", tweet.getLabelCode_1());
		obj.put("confidence", tweet.getConfidence_1());
		obj.put("humanLabeled", tweet.getHumanLabeled_1());

		return obj.toJSONString();

		/*
		StringBuffer jsonString = new StringBuffer().append("{")
				.append("\"id\":")
				.append(tweet.getTweetID()).append(",")
				.append("\"attribute_name\":")
				.append(tweet.getAttributeName_1()).append(",")
				.append("\"attribute_code\":")
				.append(tweet.getAttributeCode_1()).append(",")
				.append("\"label_name\":")
				.append(tweet.getLabelName_1()).append(",")
				.append("\"label_description\":")
				.append(tweet.getLabelDescription_1()).append(",")
				.append("\"confidence\":")
				.append(tweet.getConfidence_1())
				.append("}");

		return jsonString.toString();
		 */
	}

}
