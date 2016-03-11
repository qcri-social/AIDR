/**
 * The main class of the persister related to downloading of data from persisted collections - all REST APIs call this class methods
 * to generate CSV, JSON, TXT-JSON files with or without user specified filters.
 * 
 *  @author Imran, Koushik
 */
package qa.qcri.aidr.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
import org.apache.log4j.Logger;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvMapWriter;

import qa.qcri.aidr.common.filter.FilterQueryMatcher;
import qa.qcri.aidr.common.filter.JsonQueryList;
import qa.qcri.aidr.common.filter.NominalLabel;
import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentDTO;
import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentList;
import qa.qcri.aidr.io.FileSystemOperations;
import qa.qcri.aidr.io.ReadWriteCSV;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class JsonDeserializer {

	private static Logger logger = Logger.getLogger(JsonDeserializer.class.getName());
	private static final int BUFFER_SIZE = 10 * 1024 * 1024; // buffer size to use for buffered r/w
	private static final int LIST_BUFFER_SIZE = 50000;

	private static final String HUMAN_TAGGED_FILE_PREFIX = "-human_labeled_filtered-";
	private static final String ZIP_EXTENSION = ".zip";
	private static final String CSV_EXTENSION = ".csv";
	private static final String TWEETS_PREFIX = "_last_100k_tweets";
	private static final String TWEET_IDS_PREFIX = "_tweetIds_filtered";
	private static final String TWEET_IDS = "_tweetIds";
	private static final String CLASSIFIED = "Classified_";
	private static final String FILE_SEPARATOR = "/";
	private static final String STRING_SEPARATOR = "-";
	private static final String WITH_RETWEET = "with-retweet";
	private static final String WITHOUT_RETWEET = "without-retweet";
	private static final String VOLUME_STRING = "vol";
	private MD5HashGenerator MD5Hash;

	public JsonDeserializer() {
		MD5Hash = new MD5HashGenerator();
	}

	// This method generates tweetIds csv from all the jsons of a collection
	public Map<String, Object> generateJson2TweetIdsCSV(String collectionCode, boolean downloadLimited) {
		List<String> fileNames = FileSystemOperations.getAllJSONFileVolumes(collectionCode);
		List<Tweet> tweetsList = new ArrayList<Tweet>(LIST_BUFFER_SIZE);

		ICsvBeanWriter beanWriter = null;
		// String fileName = collectionCode + "_tweetIds" + ".csv";
		String fileNameforCSVGen = collectionCode + TWEET_IDS;
		String fileName = fileNameforCSVGen + CSV_EXTENSION;
		long lastCount = 0;
		long currentCount = 0;
		int totalCount = 0;
		try {
			ReadWriteCSV<CellProcessor> csv = new ReadWriteCSV<CellProcessor>();
			BufferedReader br = null;
			String fileToDelete = PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)	+ collectionCode + FILE_SEPARATOR + collectionCode 
					+ TWEET_IDS + CSV_EXTENSION;
			FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name

			for (String file : fileNames) {
				String fileLocation = PersisterConfigurator.getInstance().getProperty(
						PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
						+ collectionCode + FILE_SEPARATOR + file;
				try {
					br = new BufferedReader(new FileReader(fileLocation));
					String line;
					if (downloadLimited
							&& totalCount > Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
									PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
						break;
					}
					while ((line = br.readLine()) != null) {
						if (downloadLimited
								&& totalCount > Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
										PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
							tweetsList.clear();
							break;
						}
						Tweet tweet = getTweet(line);
						if (tweet != null) {
							if (tweetsList.size() < LIST_BUFFER_SIZE) { // after every 10k write to CSV file
								tweetsList.add(tweet);
							} else {
								int countToWrite;
								if (downloadLimited) {
									countToWrite = Math.min(
											tweetsList.size(),
											Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
													PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))
													- totalCount);
								} else {
									countToWrite = tweetsList.size();
								}
								if (countToWrite > 0) {
									beanWriter = csv.writeCollectorTweetIDSCSV(beanWriter, tweetsList.subList(0, countToWrite),
											collectionCode, fileName);
									totalCount += countToWrite;
									lastCount = currentCount;
									currentCount += tweetsList.size();
									logger.info(collectionCode + ": Writing_tweetIds: " + lastCount + " to " + currentCount);
								}
								tweetsList.clear();
								tweetsList.add(tweet);
							}
						}
					}
					br.close();
				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file = " + fileLocation);
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file = " + fileLocation);
				}
			} // end for
			int countToWrite = tweetsList.size();
			if (downloadLimited) {
				countToWrite = Math.min(
						tweetsList.size(),
						Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))
								- totalCount);
			}
			if (countToWrite > 0) {
				beanWriter = csv.writeCollectorTweetIDSCSV(beanWriter, tweetsList.subList(0, countToWrite), collectionCode, fileName);
				totalCount += countToWrite;
				tweetsList.clear();
			}
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
				}
			}
		}
		// beanWriter = csv.writeCollectorTweetIDSCSV(beanWriter, tweetsList,
		// collectionCode, collectionCode + "_tweetIds");
		tweetsList.clear();
		return ResultStatus.getUIWrapper("fileName", fileName, "count", totalCount);
	}

	public Map<String, Object> generateClassifiedJson2TweetIdsCSV(String collectionCode, final boolean downloadLimited) {

		ICsvMapWriter writer = null;
		String fileNameforCSVGen = CLASSIFIED + collectionCode + TWEET_IDS;
		String fileName = fileNameforCSVGen + CSV_EXTENSION;
		int totalCount = 0;
		try {
			List<String> fileNames = FileSystemOperations.getClassifiedFileVolumes(collectionCode);
			List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>(LIST_BUFFER_SIZE);

			ReadWriteCSV<CellProcessor> csv = new ReadWriteCSV<CellProcessor>();
			String[] runningHeader = null;
			BufferedReader br = null;
			String fileToDelete = PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode + FILE_SEPARATOR + CLASSIFIED + collectionCode + TWEET_IDS + CSV_EXTENSION;
			logger.info(collectionCode + ": Deleteing file : " + fileToDelete);
			FileSystemOperations.deleteFile(fileToDelete); // delete if there exists a csv file with same name

			long lastCount = 0;
			long currentCount = 0;
			for (String file : fileNames) {
				String fileLocation = PersisterConfigurator.getInstance().getProperty(
						PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
						+ collectionCode + FILE_SEPARATOR + file;
				logger.info(collectionCode + ": Reading file " + fileLocation);
				if (downloadLimited
						&& totalCount > Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
					break;
				}
				try {
					br = new BufferedReader(new FileReader(fileLocation));
					String line;
					while ((line = br.readLine()) != null) {
						if (downloadLimited
								&& totalCount >= Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
										PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
							tweetsList.clear();
							break;
						}
						// Otherwise process tweet
						ClassifiedTweet tweet = getClassifiedTweet(line);
						if (tweet != null && !tweet.getNominalLabels().isEmpty()) {
							if (tweetsList.size() < LIST_BUFFER_SIZE) {
								tweetsList.add(tweet);
							} else {
								int countToWrite;
								if (downloadLimited) {
									countToWrite = Math.min(
											tweetsList.size(),
											Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
													PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))
													- totalCount);
								} else {
									countToWrite = tweetsList.size();
								}
								if (countToWrite > 0) {
									if (0 == totalCount) {
										runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetIDCSVHeader,
												ReadWriteCSV.FIXED_CLASSIFIED_TWEET_ID_HEADER_SIZE, tweetsList.get(0));
									}
									writer = csv.writeClassifiedTweetIDsCSV(runningHeader, writer, tweetsList.subList(0, countToWrite),
											collectionCode, fileName);
									lastCount = currentCount;
									currentCount += countToWrite;
									logger.info(collectionCode + ": Writing_tweetIds: " + lastCount + " to " + currentCount);
									totalCount += countToWrite;
								}
								tweetsList.clear();
								tweetsList.add(tweet);
							}
						}
					}
					br.close();

				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file = " + fileLocation);
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file = " + fileLocation);
				}
			} // end for
			int countToWrite = tweetsList.size();
			if (downloadLimited) {
				countToWrite = Math.min(
						tweetsList.size(),
						Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))
								- totalCount);
			}
			if (countToWrite > 0) {
				if (0 == totalCount) {
					runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetIDCSVHeader,
							ReadWriteCSV.FIXED_CLASSIFIED_TWEET_ID_HEADER_SIZE, tweetsList.get(0));
				}
				writer = csv.writeClassifiedTweetIDsCSV(runningHeader, writer, tweetsList.subList(0, countToWrite), collectionCode,
						fileName);
				totalCount += countToWrite;
				tweetsList.clear();
			}

		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
				}
			}
		}
		return ResultStatus.getUIWrapper("fileName", fileName, "count", totalCount);
	}

	/**
	 * 
	 * @param collectionCode
	 * @param selectedLabels
	 *            list of user provided label names for filtering tweets
	 * @return JSON to CSV converted tweet IDs and classifiers filtered by user selected label
	 *         name
	 */
	public Map<String, Object> generateClassifiedJson2TweetIdsAndClassifiersCSVFiltered(final String collectionCode, final JsonQueryList queryList,
			final boolean downloadLimited, String userName) {
		ICsvMapWriter writer = null;
		// String fileNameforCSVGen = "Classified_" + collectionCode + "_tweetIds_filtered";
		// String fileName = fileNameforCSVGen + ".csv";
		String fileNameforCSVGen = null;

		try {
			fileNameforCSVGen = collectionCode + TWEET_IDS_PREFIX + STRING_SEPARATOR + MD5Hash.getMD5Hash(userName);
		} catch (Exception e) {
			fileNameforCSVGen = collectionCode + TWEET_IDS_PREFIX;
		}
		String fileName = fileNameforCSVGen + CSV_EXTENSION;
		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode;

		List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>(LIST_BUFFER_SIZE);
		String fileToDelete = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode + FILE_SEPARATOR + fileName;
		int totalCount = 0;
		try {
			// List<String> fileNames = FileSystemOperations.getClassifiedFileVolumes(collectionCode);
			List<String> fileNames = FileSystemOperations.getAllJSONFileVolumes(collectionCode);
			Collections.sort(fileNames);
			Collections.reverse(fileNames);

			ReadWriteCSV<CellProcessor> csv = new ReadWriteCSV<CellProcessor>(collectionCode);
			String[] runningHeader = null;
			// BufferedReader br = null;
			ReversedLinesFileReader br = null;

			logger.info(collectionCode + ": Deleteing file : " + fileToDelete + ZIP_EXTENSION);
			FileSystemOperations.deleteFile(fileToDelete + ZIP_EXTENSION); // delete if there exists a csv file with same name
			// writer = csv.writeClassifiedTweetIDsCSV(runningHeader, writer, tweetsList, collectionCode, fileName);

			// Added by koushik - first build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			if (queryList != null)
				tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

			for (String file : fileNames) {
				if (downloadLimited
						&& totalCount >= Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
					break;
				}
				String fileLocation = PersisterConfigurator.getInstance().getProperty(
						PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)	+ collectionCode + FILE_SEPARATOR + file;
				logger.info(collectionCode + ": Reading file " + fileLocation);
				try {
					// br = new BufferedReader(new FileReader(fileLocation));
					File f = new File(fileLocation);
					br = new ReversedLinesFileReader(f);
					String line;
					while ((line = br.readLine()) != null) {
						if (downloadLimited
								&& totalCount >= Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
										PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
							tweetsList.clear();
							break;
						}
						// Otherwise add to write buffer
						ClassifiedTweet tweet = getClassifiedTweet(line, collectionCode, Boolean.FALSE);
						if (0 == totalCount && runningHeader == null && writer == null) {
							runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetCSVHeader,
									ReadWriteCSV.FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweet);
							writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList, collectionCode, fileName, writer);
						}
						if (tweet != null && satisfiesFilter(queryList, tweetFilter, tweet)) {
							if (tweetsList.size() < LIST_BUFFER_SIZE
									&& tweetsList.size() < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
											PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
								tweetsList.add(tweet);
							} else {
								int countToWrite;
								if (downloadLimited) {
									countToWrite = Math.min(
											tweetsList.size(),
											Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
													PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))
													- totalCount);
								} else {
									countToWrite = tweetsList.size();
								}
								if (countToWrite > 0) {
									writer = csv.writeClassifiedTweetIDsCSV(runningHeader, writer, tweetsList.subList(0, countToWrite),
											collectionCode, fileName);
									totalCount += countToWrite;
								}
								tweetsList.clear();
								tweetsList.add(tweet);
							}
						}
					}

					br.close();

				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file = " + fileLocation);
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file = " + fileLocation);
				}
			} // end for
			int countToWrite = tweetsList.size();
			if (downloadLimited) {
				countToWrite = Math.min(
						tweetsList.size(),
						Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))
								- totalCount);
			}
			if (countToWrite > 0 && !tweetsList.isEmpty()) {
				if (0 == totalCount && runningHeader == null && writer == null) {
					runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetIDCSVHeader,
							ReadWriteCSV.FIXED_CLASSIFIED_TWEET_ID_HEADER_SIZE, tweetsList.get(0));
				}
				writer = csv.writeClassifiedTweetIDsCSV(runningHeader, writer, tweetsList.subList(0, countToWrite), collectionCode,
						fileName);
				totalCount += countToWrite;
				tweetsList.clear();
			}
			// In case if there wasn't any tweet. Just create an empty csv file.
			if (countToWrite == 0 && tweetsList.isEmpty() && 0 == totalCount && runningHeader == null && writer == null) {
				runningHeader = csv.resetClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetIDCSVHeader,
						ReadWriteCSV.FIXED_CLASSIFIED_TWEET_ID_HEADER_SIZE, 0);
				writer = csv.writeClassifiedTweetIDsCSV(runningHeader, writer, tweetsList, collectionCode, fileName);
			}
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
				}
			}
		}

		// beanWriter = csv.writeClassifiedTweetIDsCSV(beanWriter, tweetsList,
		// collectionCode, fileNameforCSVGen);
		tweetsList.clear();
		FileCompressor compressor = new FileCompressor(folderLocation, folderLocation, fileName);
		fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode
				+ FILE_SEPARATOR + compressor.zip();
		logger.info(collectionCode + ": Deleteing file : " + fileToDelete);
		FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name

		return ResultStatus.getUIWrapper("fileName", fileName, "count", totalCount);
	}
	
	/**
	 * 
	 * @param collectionCode
	 * @param selectedLabels
	 *            list of user provided label names for filtering tweets
	 * @return JSON to CSV converted tweet IDs filtered by user selected label
	 *         name
	 */
	public Map<String, Object> generateClassifiedJson2TweetIdsOnlyCSVFiltered(final String collectionCode, final Integer exportLimit, final JsonQueryList queryList,
			String userName, final boolean removeRetweet) {
		ICsvMapWriter writer = null;
		String fileNameforCSVGen = null;

		try {
			fileNameforCSVGen = collectionCode + TWEET_IDS_PREFIX + STRING_SEPARATOR + MD5Hash.getMD5Hash(userName);
		} catch (Exception e) {
			fileNameforCSVGen = collectionCode + TWEET_IDS_PREFIX;
		}
		String fileName = fileNameforCSVGen + CSV_EXTENSION;
		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode;

		List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>(LIST_BUFFER_SIZE);
		String fileToDelete = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode + FILE_SEPARATOR + fileName;
		int totalCount = 0;
		try {
			List<String> fileNames = FileSystemOperations.getAllJSONFileVolumes(collectionCode);
			Collections.sort(fileNames);
			Collections.reverse(fileNames);

			ReadWriteCSV<CellProcessor> csv = new ReadWriteCSV<CellProcessor>(collectionCode);
			String[] runningHeader = null;
			ReversedLinesFileReader br = null;

			logger.info(collectionCode + ": Deleteing file : " + fileToDelete + ZIP_EXTENSION);
			FileSystemOperations.deleteFile(fileToDelete + ZIP_EXTENSION); // delete if there exists a csv file with same name

			// Added by koushik - first build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			if (queryList != null)
				tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

			for (String file : fileNames) {
				if (totalCount >= exportLimit) {
					break;
				}
				String fileLocation = PersisterConfigurator.getInstance().getProperty(
						PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)	+ collectionCode + FILE_SEPARATOR + file;
				logger.info(collectionCode + ": Reading file " + fileLocation);
				try {
					// br = new BufferedReader(new FileReader(fileLocation));
					File f = new File(fileLocation);
					br = new ReversedLinesFileReader(f);
					String line;
					while ((line = br.readLine()) != null) {
						if (totalCount >= exportLimit) {
							tweetsList.clear();
							break;
						}
						// Otherwise add to write buffer
						ClassifiedTweet tweet = getClassifiedTweet(line, collectionCode, removeRetweet);
						if (0 == totalCount && runningHeader == null && writer == null) {
							runningHeader = new String[]{"tweetID"};
							writer = csv.writeClassifiedTweetIDsOnlyCSV(runningHeader, writer, tweetsList,
									collectionCode, fileName);
						}
						if (tweet != null && satisfiesFilter(queryList, tweetFilter, tweet)) {
							if (tweetsList.size() < LIST_BUFFER_SIZE
									&& tweetsList.size() < exportLimit) {
								tweetsList.add(tweet);
							} else {
								int countToWrite;
									countToWrite = Math.min(tweetsList.size(), exportLimit	- totalCount);
								if (countToWrite > 0) {
									writer = csv.writeClassifiedTweetIDsOnlyCSV(runningHeader, writer, tweetsList.subList(0, countToWrite),
											collectionCode, fileName);
									totalCount += countToWrite;
								}
								tweetsList.clear();
								tweetsList.add(tweet);
							}
						}
					}

					br.close();

				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file = " + fileLocation);
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file = " + fileLocation);
				}
			} // end for
			int countToWrite = tweetsList.size();
			countToWrite = Math.min(tweetsList.size(), exportLimit - totalCount);
			if (countToWrite > 0 && !tweetsList.isEmpty()) {
				if (0 == totalCount && runningHeader == null && writer == null) {
					runningHeader = new String[]{"tweetID"};
				}
				writer = csv.writeClassifiedTweetIDsOnlyCSV(runningHeader, writer, tweetsList.subList(0, countToWrite), collectionCode,
						fileName);
				totalCount += countToWrite;
				tweetsList.clear();
			}
			// In case if there wasn't any tweet. Just create an empty csv file.
			if (countToWrite == 0 && tweetsList.isEmpty() && 0 == totalCount && runningHeader == null && writer == null) {
				runningHeader = new String[]{"tweetID"};
				writer = csv.writeClassifiedTweetIDsOnlyCSV(runningHeader, writer, tweetsList, collectionCode, fileName);
			}
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
				}
			}
		}

		// beanWriter = csv.writeClassifiedTweetIDsCSV(beanWriter, tweetsList,
		// collectionCode, fileNameforCSVGen);
		tweetsList.clear();
		FileCompressor compressor = new FileCompressor(folderLocation, folderLocation, fileName);
		fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode
				+ FILE_SEPARATOR + compressor.zip();
		logger.info(collectionCode + ": Deleteing file : " + fileToDelete);
		FileSystemOperations.deleteFile(fileToDelete); // delete if there exist a csv file with same name

		return ResultStatus.getUIWrapper("fileName", fileName, "count", totalCount);
	}

	private final static String getDateTime() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd"); // yyyy-MM-dd_hh:mm:ss
		return df.format(new Date());
	}

	public String generateJSON2CSV_100K_BasedOnTweetCount(String collectionCode, int exportLimit) {
		BufferedReader br = null;
		//boolean isCSVGenerated = true;
		String fileName = "";
		ICsvBeanWriter beanWriter = null;

		try {

			String folderLocation = PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode;
			String fileNameforCSVGen = collectionCode + TWEETS_PREFIX;
			fileName = fileNameforCSVGen + CSV_EXTENSION;
			FileSystemOperations.deleteFile(PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode + FILE_SEPARATOR + fileName);

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();

			Arrays.sort(listOfFiles, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				}
			});

			List<Tweet> tweetsList = new ArrayList<Tweet>();
			ReadWriteCSV<CellProcessor> csv = new ReadWriteCSV<CellProcessor>();
			int currentSize = 0;

			createTweetList: {
				for (int i = 0; i < listOfFiles.length; i++) {
					File f = listOfFiles[i];
					String currentFileName = f.getName();
					if (currentFileName.endsWith(".json") && currentFileName.contains("vol")) {
						String line;
						logger.info(collectionCode + ": Reading file : " + f.getAbsolutePath());
						InputStream is = new FileInputStream(f.getAbsolutePath());
						br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
						while ((line = br.readLine()) != null) {
							try {
								Tweet tweet = getTweet(line);
								if (tweet != null) {
									if (tweetsList.size() < LIST_BUFFER_SIZE && currentSize <= exportLimit) {
										// write to arrayList
										tweetsList.add(tweet);
									} else {
										// write csv file
										int countToWrite = Math.min(tweetsList.size(), exportLimit - currentSize);
										if (countToWrite > 0) {
											beanWriter = csv.writeCollectorTweetsCSV(tweetsList.subList(0, countToWrite), collectionCode,
													fileName, beanWriter);
											currentSize += countToWrite;
										}
										// empty arraylist
										tweetsList.clear();
										tweetsList.add(tweet);
									}
									if (beanWriter != null) {
										if (currentSize >= exportLimit) {
											// write to the csv file
											break createTweetList;
										}
									}
								}
							} catch (Exception ex) {
								logger.error("Error while parsing the json" + ex);
							}
						}
						if (!tweetsList.isEmpty()) {
							beanWriter = csv.writeCollectorTweetsCSV(tweetsList, collectionCode, fileName, beanWriter);
							logger.info(collectionCode + ": final beanWriter  : " + beanWriter.getRowNumber());
							tweetsList.clear();
						}
						br.close();
					}
				}
			}
			int countToWrite = Math.min(tweetsList.size(), exportLimit - currentSize);
			if (countToWrite > 0) {
				beanWriter = csv.writeCollectorTweetsCSV(tweetsList.subList(0, countToWrite), collectionCode, fileName, beanWriter);
				tweetsList.clear();
			}
		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
			//isCSVGenerated = false;
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file");
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
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
			String folderLocation = PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode;
			String fileNameforCSVGen = CLASSIFIED + collectionCode + TWEETS_PREFIX;
			fileName = fileNameforCSVGen + CSV_EXTENSION;
			FileSystemOperations.deleteFile(folderLocation + FILE_SEPARATOR + fileName);

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();
			// to get only Tagger's files
			ArrayList<File> taggerFilesList = new ArrayList<File>();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (StringUtils.startsWith(listOfFiles[i].getName(), CLASSIFIED)
						&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) {
					taggerFilesList.add(listOfFiles[i]);
				}
			}

			Object[] objectsArray = taggerFilesList.toArray();
			File[] taggerFiles = Arrays.copyOf(objectsArray, objectsArray.length, File[].class);
			Arrays.sort(taggerFiles, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()); // koushik: changed sort order
				}
			});

			List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>(LIST_BUFFER_SIZE);
			ReadWriteCSV<CellProcessor> csv = new ReadWriteCSV<CellProcessor>(collectionCode);
			String[] runningHeader = null;
			int currentSize = 0;

			createTweetList: {
				for (int i = 0; i < taggerFiles.length; i++) {
					File f = taggerFiles[i];
					String currentFileName = f.getName();
					if (currentFileName.endsWith(".json")) {
						String line;
						logger.info(collectionCode + ": Reading file : " + f.getAbsolutePath());
						InputStream is = new FileInputStream(f.getAbsolutePath());
						br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
						while ((line = br.readLine()) != null) {
							ClassifiedTweet tweet = getClassifiedTweet(line);
							if (0 == currentSize && runningHeader == null && writer == null) {
								runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetCSVHeader,
										ReadWriteCSV.FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweet);
								writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList, collectionCode, fileName, writer);
							}
							if (tweet != null && !tweet.getNominalLabels().isEmpty()) {
								if (tweetsList.size() < LIST_BUFFER_SIZE) {
									tweetsList.add(tweet);
								} else {
									int countToWrite = Math.min(tweetsList.size(), exportLimit - currentSize);
									if (countToWrite > 0) {
										// buffer full, write to csv file
										writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList.subList(0, countToWrite),
												collectionCode, fileName, writer);
										currentSize += countToWrite;
									}
									tweetsList.clear();
									if (currentSize >= exportLimit) {
										break createTweetList;
									}
									// Otherwise add current tweet and continue
									tweetsList.add(tweet);
								}
							}
						}
						/*
						 * if (!tweetsList.isEmpty()) { writer =
						 * csv.writeClassifiedTweetsCSV(tweetsList,
						 * collectionCode, fileName, writer);
						 * tweetsList.clear(); }
						 */
						br.close();
					}
				}
			}
			int countToWrite = Math.min(tweetsList.size(), exportLimit - currentSize);
			if (countToWrite > 0) {
				if (0 == currentSize && runningHeader == null && writer == null) {
					runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetCSVHeader,
							ReadWriteCSV.FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweetsList.get(0));
				}
				writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList.subList(0, countToWrite), collectionCode, fileName, writer);
				tweetsList.clear();
			}
		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": File not found.");
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
				}
			}
		}
		return fileName;
	}

	/**
	 * 
	 * @param collectionCode
	 *            c
	 * @param exportLimit
	 * @param selectedLabels
	 *            list of user provided label names for filtering tweets
	 * @return JSON to CSV converted 100K tweets filtered by user selected label
	 *         name
	 */
	public Map<String, Object> taggerGenerateJSON2CSVBasedOnTweetCountFiltered(String collectionCode, int exportLimit,
			final JsonQueryList queryList, String userName, boolean removeRetweet) {
		// BufferedReader br = null;
		ReversedLinesFileReader br = null;
		int currentSize = 0;
		String fileName = "";
		ICsvMapWriter writer = null;
		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode;
		String fileNameforCSVGen = null;
		Long currentTimeStamp = System.currentTimeMillis();
		String exclusionPattern = VOLUME_STRING;
		
		fileNameforCSVGen = currentTimeStamp + STRING_SEPARATOR + exportLimit + STRING_SEPARATOR + (removeRetweet ? WITHOUT_RETWEET : WITH_RETWEET);
		try {
			fileNameforCSVGen = fileNameforCSVGen + STRING_SEPARATOR + MD5Hash.getMD5Hash(userName);
		} catch (Exception e) {
			logger.info("Issue in generating user hash for user : " + userName);
		}
		fileName = fileNameforCSVGen + CSV_EXTENSION;
		try {
			
			FileSystemOperations.deleteFiles(folderLocation, CSV_EXTENSION + ZIP_EXTENSION, exclusionPattern);

			logger.info("Deleting exsiting file: " + folderLocation + FILE_SEPARATOR + fileName + ZIP_EXTENSION);

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();
			// to get only Tagger's files
			ArrayList<File> taggerFilesList = new ArrayList<File>();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (StringUtils.startsWith(listOfFiles[i].getName(), (collectionCode + "_"))
						&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) {
					logger.info("Added to list, file: " + listOfFiles[i]);
					taggerFilesList.add(listOfFiles[i]);
				}
			}
			Object[] objectsArray = taggerFilesList.toArray();
			File[] taggerFiles = Arrays.copyOf(objectsArray, objectsArray.length, File[].class);

			Arrays.sort(taggerFiles, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				}
			});

			List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>(LIST_BUFFER_SIZE);
			ReadWriteCSV<CellProcessor> csv = new ReadWriteCSV<CellProcessor>(collectionCode);
			String[] runningHeader = null;
			// writer = csv.writeClassifiedTweetsCSV(runningHeader, null,
			// collectionCode, fileName, writer);

			// First build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			if (queryList != null)
				tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();
			//int j = 0;
			for (int i = taggerFiles.length - 1; i >= 0; i--) {
				File f = taggerFiles[i];
				String currentFileName = f.getName();
				if (currentFileName.endsWith(".json")) {
					String line;
					logger.info("Reading file : " + f.getAbsolutePath());
					// InputStream is = new
					// FileInputStream(f.getAbsolutePath());
					// br = new BufferedReader(new InputStreamReader(is,
					// Charset.forName("UTF-8")));
					br = new ReversedLinesFileReader(f);

					if (currentSize < exportLimit) {
						while ((line = br.readLine()) != null) {
							ClassifiedTweet tweet = getClassifiedTweet(line, collectionCode, removeRetweet);
							if (0 == currentSize && runningHeader == null && writer == null) {
								runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetCSVHeader,
										ReadWriteCSV.FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweet);
								writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList, collectionCode, fileName, writer);
							}
							// logger.info("Parsed tweet = " + tweet.getTweetID() + "," + tweet.getMessage());
							if (tweet != null && satisfiesFilter(queryList, tweetFilter, tweet)) {
								if (tweetsList.size() < LIST_BUFFER_SIZE && tweetsList.size() < exportLimit) {
									// Apply filter on tweet
									tweetsList.add(tweet);
								} else {
									// write-buffer full, write to csv file
									int countToWrite = Math.min(tweetsList.size(), exportLimit - currentSize);
									if (countToWrite > 0 && currentSize < exportLimit) {
										writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList.subList(0, countToWrite),
												collectionCode, fileName, writer);
										currentSize += countToWrite;
										logger.info("currentSize = " + currentSize + ", countToWrite = " + countToWrite);
										// logger.info(tweet.toJsonString());

										// clear contents from tweetsList buffer
										tweetsList.clear();
										countToWrite = 0;
										if (currentSize >= exportLimit) {
											break; // we are done
										} else {
											// Otherwise add the tweet to fresh buffer and continue
											tweetsList.add(tweet);
										}
									}
								}
							}
						} // end while
					} else {
						break;
					}
				}
				br.close();
				logger.info("Done processing file : " + f.getAbsolutePath());
			} // end for

			if (currentSize < exportLimit) {
				int countToWrite = Math.min(tweetsList.size(), exportLimit - currentSize);
				// logger.info("Outside for loop: currentSize = " + currentSize + ", countToWrite = " + countToWrite + " tweetsList size = " + tweetsList.size());
				if (countToWrite > 0 && tweetsList.size() >= countToWrite) {
					// logger.info("Outside loop, writing residual list: exportLimit = " + exportLimit + ", currentSize = " + currentSize + ", countToWrite = " + countToWrite);
					if (0 == currentSize && runningHeader == null && writer == null) {
						runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetCSVHeader,
								ReadWriteCSV.FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweetsList.get(0));
					}
					writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList.subList(0, countToWrite), collectionCode, fileName,
							writer);
					currentSize += countToWrite;
					tweetsList.clear();
				}
				// In case there wasn't any tweet. Just create an empty csv file.
				if (countToWrite == 0 && tweetsList.size() == 0 && 0 == currentSize && runningHeader == null && writer == null) {
					runningHeader = csv.resetClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetCSVHeader,
							ReadWriteCSV.FIXED_CLASSIFIED_TWEET_HEADER_SIZE, 0);
					writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList.subList(0, countToWrite), collectionCode, fileName,
							writer);
				}
				
				
			}
		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
				}
			}
		}

		FileCompressor compressor = new FileCompressor(folderLocation, folderLocation, fileName);
		String fileToDelete = fileName;
		
		fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode
				+ FILE_SEPARATOR + compressor.zip();
		
		FileSystemOperations.deleteFile(folderLocation + FILE_SEPARATOR + fileToDelete);
		logger.info("Deleted raw created file: " + folderLocation + FILE_SEPARATOR + fileNameforCSVGen + CSV_EXTENSION);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("fileName", fileName);
		resultMap.put("currentSize", currentSize);		
		return resultMap;
	}

	private Tweet getTweet(String line) {
		Tweet tweet = new Tweet();
		try {
			//Gson jsonObject = new GsonBuilder().serializeNulls().disableHtmlEscaping().serializeSpecialFloatingPointValues().create();
			JsonParser parser = new JsonParser();
			JsonObject jsonObj = (JsonObject) parser.parse(line);

			if (jsonObj.get("id_str") != null) {
				tweet.setTweetID(jsonObj.get("id_str").getAsString());
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
			logger.error("Exception while parsing the json to tweet" + ex);
			return null;
		}
		return tweet;
	}

	public ClassifiedTweet getClassifiedTweet(String line) {
		return getClassifiedTweet(line, null, Boolean.FALSE);
	}

	public ClassifiedTweet getClassifiedTweet(String line, String collectionCode, boolean removeRetweet) {

		ClassifiedTweet tweet = new ClassifiedTweet();
		try {
			StringReader reader = new StringReader(line.trim());
			JsonReader jsonReader = new JsonReader(reader);
			jsonReader.setLenient(true);
			//Gson jsonObject = new GsonBuilder().serializeNulls().disableHtmlEscaping().serializeSpecialFloatingPointValues().create();
			JsonParser parser = new JsonParser();
			JsonObject jsonObj = (JsonObject) parser.parse(jsonReader);

			if(removeRetweet && jsonObj.get("retweeted_status") != null) {
				return null;
			}
			if (jsonObj.get("id_str") != null) {
				tweet.setTweetID(jsonObj.get("id_str").getAsString());
			}

			if (jsonObj.get("text") != null) {
				tweet.setMessage(jsonObj.get("text").getAsString());
			}

			if (jsonObj.get("created_at") != null) {
				tweet.setCreatedAtString(jsonObj.get("created_at").getAsString());
				tweet.setCreateAt(new Date(tweet.getTimestamp()));
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
				if (aidrObject.get("crisis_code") != null) {
					tweet.setCrisisCode(aidrObject.get("crisis_code").getAsString());
				}
				if (aidrObject.has("nominal_labels") && (aidrObject.get("nominal_labels") != null)) {
					// JSONArray nominalLabels = (JSONArray)
					// aidrObject.get("nominal_labels");
					JsonArray nominalLabels = aidrObject.get("nominal_labels").getAsJsonArray();
					StringBuffer allAttributeNames = new StringBuffer();
					StringBuffer allAttributeCodes = new StringBuffer();
					StringBuffer allLabelNames = new StringBuffer();
					StringBuffer allLabelCodes = new StringBuffer();
					StringBuffer allLabelDescriptions = new StringBuffer();
					StringBuffer allConfidences = new StringBuffer();
					StringBuffer humanLabeled = new StringBuffer();
					for (int i = 0; i < nominalLabels.size(); i++) {
						// JSONObject label = (JSONObject) nominalLabels.get(i);
						JsonObject label = nominalLabels.get(i).getAsJsonObject();
						allAttributeNames.append(!label.get("attribute_name").isJsonNull() ? label.get("attribute_name").getAsString()
								: "null");
						allAttributeCodes.append(!label.get("attribute_code").isJsonNull() ? label.get("attribute_code").getAsString()
								: "null");
						allLabelNames.append(!label.get("label_name").isJsonNull() ? label.get("label_name").getAsString() : "null");
						allLabelCodes.append(!label.get("label_code").isJsonNull() ? label.get("label_code").getAsString() : "null");
						allLabelDescriptions.append(!label.get("label_description").isJsonNull() ? label.get("label_description")
								.getAsString() : "null");
						allConfidences.append(!label.get("confidence").isJsonNull() ? label.get("confidence").getAsFloat() : 0);
						humanLabeled.append(!label.get("from_human").isJsonNull() ? label.get("from_human").getAsBoolean() : false);

						// Added by koushik
						NominalLabel nLabel = new NominalLabel();
						nLabel.attribute_code = (label.has("attribute_code") && !label.get("attribute_code").isJsonNull()) ? label.get(
								"attribute_code").getAsString() : "null";
						nLabel.label_code = (label.has("label_code") && !label.get("label_code").isJsonNull()) ? label.get("label_code")
								.getAsString() : "null";
						nLabel.confidence = (label.has("confidence") && !label.get("confidence").isJsonNull()) ? Float.parseFloat(label
								.get("confidence").getAsString()) : 0;
						nLabel.attribute_name = (label.has("attribute_name") && !label.get("attribute_name").isJsonNull()) ? label.get(
								"attribute_name").getAsString() : "null";
						nLabel.label_name = (label.has("label_name") && !label.get("label_name").isJsonNull()) ? label.get("label_name")
								.getAsString() : "null";
						nLabel.attribute_description = (label.has("attribute_description") && !label.get("attribute_description")
								.isJsonNull()) ? label.get("attribute_description").getAsString() : "null";
						nLabel.label_description = (label.has("label_description") && !label.get("label_description").isJsonNull()) ? label
								.get("label_description").getAsString() : "null";
						nLabel.from_human = (label.has("from_human") && !label.get("from_human").isJsonNull()) ? Boolean.parseBoolean(label
								.get("from_human").getAsString()) : false;

						tweet.getNominalLabels().add(nLabel);

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
					tweet.createDummyNominalLabels(collectionCode);
				}
			} else {
				tweet.createDummyNominalLabels(collectionCode);
			}
			return tweet;
		} catch (Exception ex) {
			logger.error("Exception while parsing the json to classiffied tweet for the collection: " + collectionCode + "\t" + ex);
			return null;
		}
	}

	public String generateJSON2JSON_100K_BasedOnTweetCount(String collectionCode, DownloadJsonType jsonType) {
		BufferedReader br = null;
		String fileName = "";
		BufferedWriter beanWriter = null;
		String extension = DownloadJsonType.getSuffixString(jsonType);
		if (null == extension) {
			extension = DownloadJsonType.defaultSuffix();
		}
		boolean jsonObjectClosed = false;
		try {

			String folderLocation = PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode;
			String fileNameforJsonGen = collectionCode + TWEETS_PREFIX;
			fileName = fileNameforJsonGen + extension;
			FileSystemOperations.deleteFile(PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode + FILE_SEPARATOR + fileNameforJsonGen + extension);

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();

			Arrays.sort(listOfFiles, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				}
			});

			long currentSize = 0;
			StringBuffer outputFile = new StringBuffer().append(folderLocation).append(FILE_SEPARATOR).append(fileName);
			beanWriter = new BufferedWriter(new FileWriterWithEncoding(outputFile.toString(), "UTF-8"), BUFFER_SIZE);
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType)) {
				beanWriter.write("[");
			}
			boolean isDone = false;
			for (int i = 0; i < listOfFiles.length; i++) {
				File f = listOfFiles[i];
				String currentFileName = f.getName();
				if (currentFileName.endsWith(".json") && currentFileName.contains("vol")) {
					String line;
					logger.info("Reading file : " + f.getAbsolutePath());
					InputStream is = new FileInputStream(f.getAbsolutePath());
					br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
					while ((line = br.readLine()) != null) {
						try {
							if (currentSize <= Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
									PersisterConfigurationProperty.TWEETS_EXPORT_LIMIT))) {
								if (DownloadJsonType.JSON_OBJECT.equals(jsonType)
										&& currentSize < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
												PersisterConfigurationProperty.TWEETS_EXPORT_LIMIT)) && currentSize > 0) {
									beanWriter.write(", "); // do not append for last item
								}
								// write to file
								beanWriter.write(line);
								beanWriter.newLine();
								++currentSize;
							} else {
								if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
									beanWriter.write("]");
									jsonObjectClosed = true;
								}
								beanWriter.flush();
								isDone = true;
								break;
							}
						} catch (Exception ex) {
							logger.error("JSON file parsing exception" + ex);
						}
					} // end while
					br.close();
					if (isDone) {
						beanWriter.close();
						break;
					}
				}
			} // end for

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
		} finally {
			if (beanWriter != null) {
				try {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
						beanWriter.write("]");
						jsonObjectClosed = true;
					}
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
				}
			}
		}
		return fileName;
	}

	public Map<String, Object> generateJson2TweetIdsJson(String collectionCode, final boolean downloadLimited, DownloadJsonType jsonType) {
		BufferedReader br = null;
		String fileName = "";
		BufferedWriter beanWriter = null;
		int totalCount = 0;
		String extension = DownloadJsonType.getSuffixString(jsonType);
		if (null == extension) {
			extension = DownloadJsonType.defaultSuffix();
		}
		boolean jsonObjectClosed = false;
		try {

			String folderLocation = PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode;
			String fileNameforJsonGen = collectionCode + TWEET_IDS;
			fileName = fileNameforJsonGen + extension;
			FileSystemOperations.deleteFile(PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode + FILE_SEPARATOR + fileNameforJsonGen + extension);

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();

			Arrays.sort(listOfFiles, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				}
			});

			StringBuffer outputFile = new StringBuffer().append(folderLocation).append(FILE_SEPARATOR).append(fileName);
			beanWriter = new BufferedWriter(new FileWriterWithEncoding(outputFile.toString(), "UTF-8"), BUFFER_SIZE);
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType)) {
				beanWriter.write("[");
			}
			for (int i = 0; i < listOfFiles.length; i++) {
				File f = listOfFiles[i];
				String currentFileName = f.getName();
				if (currentFileName.endsWith(".json") && currentFileName.contains("vol")) {
					String line;
					logger.info("Reading file : " + f.getAbsolutePath());
					InputStream is = new FileInputStream(f.getAbsolutePath());
					br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
					if (downloadLimited
							&& totalCount > Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
									PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
						break;
					}
					while ((line = br.readLine()) != null) {
						if (downloadLimited
								&& totalCount > Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
										PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
							break;
						}
						try {
							Tweet tweet = getTweet(line);
							if (tweet != null && tweet.getTweetID() != null) {
								JSONObject obj = new JSONObject();
								obj.put("id", tweet.getTweetID());
								if (DownloadJsonType.JSON_OBJECT.equals(jsonType)
										&& totalCount < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
												PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT)) && totalCount > 0) {
									beanWriter.write(", "); // do not append for last item
								}
								// write to file
								beanWriter.write(obj.toJSONString());
								beanWriter.newLine();
								++totalCount;
							}
						} catch (Exception ex) {
							logger.error("JSON file parsing exception" + ex);
						}
					} // end while
					br.close();
				}
			} // end for
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
				beanWriter.write("]");
				jsonObjectClosed = true;
			}
			beanWriter.flush();
			beanWriter.close();

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
		} finally {
			if (beanWriter != null) {
				try {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
						beanWriter.write("]");
						jsonObjectClosed = true;
					}
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
				}
			}
		}
		return ResultStatus.getUIWrapper("fileName", fileName, "count", totalCount);
	}

	public String taggerGenerateJSON2JSON_100K_BasedOnTweetCount(String collectionCode, int exportLimit, DownloadJsonType jsonType) {
		BufferedReader br = null;
		String fileName = "";
		BufferedWriter beanWriter = null;

		String extension = DownloadJsonType.getSuffixString(jsonType);
		if (null == extension) {
			extension = DownloadJsonType.defaultSuffix();
		}
		boolean jsonObjectClosed = false;
		try {

			String folderLocation = PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode;
			logger.info("For collection: " + collectionCode + ", will create file from folder: " + folderLocation);
			String fileNameforJsonGen = CLASSIFIED + collectionCode + TWEETS_PREFIX;
			fileName = fileNameforJsonGen + extension;

			FileSystemOperations.deleteFile(folderLocation + FILE_SEPARATOR + fileNameforJsonGen + extension);
			logger.info("Deleted existing file: " + folderLocation + FILE_SEPARATOR + fileNameforJsonGen + extension);

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();
			// to get only Tagger's files
			ArrayList<File> taggerFilesList = new ArrayList<File>();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (StringUtils.startsWith(listOfFiles[i].getName(), CLASSIFIED)
						&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) {
					taggerFilesList.add(listOfFiles[i]);
				}
			}

			Object[] objectsArray = taggerFilesList.toArray();
			File[] taggerFiles = Arrays.copyOf(objectsArray, objectsArray.length, File[].class);
			Arrays.sort(taggerFiles, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()); // koushik: changed sort order?
				}
			});

			StringBuffer outputFile = new StringBuffer().append(folderLocation).append(FILE_SEPARATOR).append(fileName);
			beanWriter = new BufferedWriter(new FileWriterWithEncoding(outputFile.toString(), "UTF-8"), BUFFER_SIZE);
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType)) {
				beanWriter.write("[");
			}
			long currentSize = 0;
			boolean isDone = false;
			for (int i = 0; i < taggerFiles.length; i++) {
				File f = taggerFiles[i];
				String currentFileName = f.getName();
				if (currentFileName.endsWith(".json") && currentFileName.contains("vol")) {
					String line;
					logger.info("Reading file : " + f.getAbsolutePath());
					InputStream is = new FileInputStream(f.getAbsolutePath());
					br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

					while ((line = br.readLine()) != null) {
						try {
							if (currentSize < exportLimit
									&& currentSize < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
											PersisterConfigurationProperty.TWEETS_EXPORT_LIMIT))) {
								if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && currentSize > 0) {
									beanWriter.write(", ");
								}
								// write to file
								beanWriter.write(line);
								beanWriter.newLine();
								++currentSize;
							} else {
								if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
									beanWriter.write("]");
									jsonObjectClosed = true;
								}
								beanWriter.flush();
								isDone = true;
								break;
							}
						} catch (Exception ex) {
							logger.error("JSON file parsing exception" + ex);
						}
					} // end while
					br.close();
					if (isDone) {
						beanWriter.close();
						break;
					}
				}
			} // end for
		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
		} catch (NullPointerException ex) {
			logger.error(collectionCode + ": empty list of files to read");
		} finally {
			if (beanWriter != null) {
				try {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
						beanWriter.write("]");
						jsonObjectClosed = true;
					}
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
				}
			}
		}
		return fileName;
	}

	public Map<String, Object> generateClassifiedJson2TweetIdsJSON(String collectionCode, final boolean downloadLimited,
			DownloadJsonType jsonType) {
		String extension = DownloadJsonType.getSuffixString(jsonType);
		if (null == extension) {
			extension = DownloadJsonType.defaultSuffix();
		}
		boolean jsonObjectClosed = false;

		BufferedWriter beanWriter = null;
		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode;
		String fileNameforJsonGen = CLASSIFIED + collectionCode + TWEET_IDS;
		String fileName = fileNameforJsonGen + extension;
		int totalCount = 0;

		try {
			List<String> fileNames = FileSystemOperations.getClassifiedFileVolumes(collectionCode);

			BufferedReader br = null;
			String fileToDelete = PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode + FILE_SEPARATOR + CLASSIFIED + collectionCode + TWEET_IDS + extension;
			// System.out.println("Deleteing file : " + fileToDelete);
			logger.info("Deleteing file : " + fileToDelete);
			FileSystemOperations.deleteFile(fileToDelete); // delete if there exists a csv file with same name
			// System.out.println(fileNames);

			StringBuffer outputFile = new StringBuffer().append(folderLocation).append(FILE_SEPARATOR).append(fileName);
			beanWriter = new BufferedWriter(new FileWriterWithEncoding(outputFile.toString(), "UTF-8"), BUFFER_SIZE);
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType)) {
				beanWriter.write("[");
			}
			for (String file : fileNames) {
				if (downloadLimited
						&& totalCount > Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
					break;
				}
				String fileLocation = PersisterConfigurator.getInstance().getProperty(
						PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
						+ collectionCode + FILE_SEPARATOR + file;
				// logger.info("Reading file " + fileLocation);
				try {
					br = new BufferedReader(new FileReader(fileLocation));
					String line;
					while ((line = br.readLine()) != null) {
						if (downloadLimited
								&& totalCount >= Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
										PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT)))
							break;

						ClassifiedTweet tweet = getClassifiedTweet(line);
						if (tweet != null && tweet.getTweetID() != null) {
							if (!tweet.getNominalLabels().isEmpty()) {
								if (DownloadJsonType.JSON_OBJECT.equals(jsonType)
										&& totalCount < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
												PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT)) && totalCount > 0) {
									beanWriter.write(", ");
								}
								// write to file
								beanWriter.write(createJsonClassifiedTweetIDString(tweet));
								beanWriter.newLine();
							}
							++totalCount;
						}
					}
					br.close();

				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file");
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file read");
				}
			} // end for
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
				beanWriter.write("]");
				jsonObjectClosed = true;
			}
			beanWriter.flush();
			beanWriter.close();

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
		} finally {
			if (beanWriter != null) {
				try {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
						beanWriter.write("]");
						jsonObjectClosed = true;
					}
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
				}
			}
		}
		return ResultStatus.getUIWrapper("fileName", fileName, "count", totalCount);
	}

	public Map<String, Object> taggerGenerateJSON2JSONBasedOnTweetCountFiltered(String collectionCode, int exportLimit, JsonQueryList queryList,
			DownloadJsonType jsonType, String userName, boolean removeRetweet) {

		// BufferedReader br = null;
		ReversedLinesFileReader br = null;
		String fileName = "";
		long currentSize = 0;
		BufferedWriter beanWriter = null;
		String extension = DownloadJsonType.getSuffixString(jsonType);
		if (null == extension) {
			extension = DownloadJsonType.defaultSuffix();
		}
		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode + FILE_SEPARATOR;
		String fileNameforJsonGen = null;
		Long currentTimeStamp = System.currentTimeMillis();
		
		String exclusionPattern = HUMAN_TAGGED_FILE_PREFIX;
		fileNameforJsonGen = currentTimeStamp + STRING_SEPARATOR + exportLimit + STRING_SEPARATOR + (removeRetweet ? WITHOUT_RETWEET : WITH_RETWEET);
		try {
			fileNameforJsonGen = fileNameforJsonGen + STRING_SEPARATOR + MD5Hash.getMD5Hash(userName);
		} catch (Exception e) {
			logger.info("Issue in generating user hash for user : " + userName);
		}
		fileName = fileNameforJsonGen + extension;

		boolean jsonObjectClosed = false;
		try {
			FileSystemOperations.deleteFiles(folderLocation, extension + ZIP_EXTENSION, exclusionPattern);

			File folder = new File(folderLocation);
			File[] listOfFiles = folder.listFiles();
			// to get only Tagger's files
			ArrayList<File> taggerFilesList = new ArrayList<File>();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (StringUtils.startsWith(listOfFiles[i].getName(), (collectionCode + "_"))
						&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) {
					taggerFilesList.add(listOfFiles[i]);
					// logger.info("Added to list, file: " + listOfFiles[i]);
				}
			}

			Object[] objectsArray = taggerFilesList.toArray();
			File[] taggerFiles = Arrays.copyOf(objectsArray, objectsArray.length, File[].class);
			Arrays.sort(taggerFiles, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()); 
					// koushik: changed sort order to create list in ascending order of modified time
				}
			});

			StringBuffer outputFile = new StringBuffer().append(folderLocation).append(fileName);
			beanWriter = new BufferedWriter(new FileWriterWithEncoding(outputFile.toString(), "UTF-8"), BUFFER_SIZE);
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType)) {
				beanWriter.write("[");
			}
			// First build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			if (queryList != null)
				tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

			boolean isDone = false;
			for (int i = taggerFiles.length - 1; i >= 0; i--) {
				File f = taggerFiles[i];
				String currentFileName = f.getName();
				if (currentFileName.endsWith(".json") && currentFileName.contains("vol")) {
					String line;
					logger.info("Reading file : " + f.getAbsolutePath());
					// InputStream is = new
					// FileInputStream(f.getAbsolutePath());
					// br = new BufferedReader(new InputStreamReader(is,
					// Charset.forName("UTF-8")));
					br = new ReversedLinesFileReader(f);

					while ((line = br.readLine()) != null) {
						try {
							ClassifiedTweet tweet = getClassifiedTweet(line, null, removeRetweet);
							if(tweet != null) {
								if (currentSize < exportLimit) {
									// Apply filter on tweet
									if (satisfiesFilter(queryList, tweetFilter, tweet)) {
										if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && currentSize > 0) {
											beanWriter.write(", ");
										}
										// write to file
										//Decode unicode hex value to normal characters
										UnicodeUnescaper u = new UnicodeUnescaper();
										line =  u.translate(line);
										//Encoding angular brackets < > 
									    line = UnicodeEscaper.between(60,60).translate(line);
									    line = UnicodeEscaper.between(62,62).translate(line);
										beanWriter.write(line);
										beanWriter.newLine();
										++currentSize;
									}
								} else {
									if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
										beanWriter.write("]");
										jsonObjectClosed = true;
									}
									beanWriter.flush();
									isDone = true;
									break;
								}
							}
						} catch (Exception ex) {
							logger.error("JSON file parsing exception" + ex);
						}
					} // end while
					br.close();
					if (isDone) {
						beanWriter.newLine();
						beanWriter.close();
						break;
					}
				}
			} // end for
		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
		} catch (NullPointerException ex) {
			logger.error(collectionCode + ": empty list of files to read");
		} finally {
			if (beanWriter != null) {
				try {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
						beanWriter.write("]");
						beanWriter.newLine();
						jsonObjectClosed = true;
					}
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
				}
			}
		}
		
		FileCompressor compressor = new FileCompressor(folderLocation, folderLocation, fileName);
		String fileToDelete = fileName;
		fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode
				+ FILE_SEPARATOR + compressor.zip();
		FileSystemOperations.deleteFile(folderLocation + FILE_SEPARATOR + fileToDelete);
		logger.info("Deleted created raw file: " + folderLocation + FILE_SEPARATOR + fileNameforJsonGen + extension);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("fileName", fileName);
		resultMap.put("currentSize", currentSize);		
		return resultMap;
	}

	public Map<String, Object> generateClassifiedJson2TweetIdsJSONFiltered(String collectionCode, Boolean downloadLimited,
			JsonQueryList queryList, DownloadJsonType jsonType, String userName) {

		String extension = DownloadJsonType.getSuffixString(jsonType);
		if (null == extension) {
			extension = DownloadJsonType.defaultSuffix();
		}
		boolean jsonObjectClosed = false;

		BufferedWriter beanWriter = null;
		// String fileNameforJsonGen = "Classified_" + collectionCode +
		// "_tweetIds_filtered";
		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode + FILE_SEPARATOR;
		String fileNameforJsonGen = null;
		try {
			fileNameforJsonGen = collectionCode + TWEET_IDS_PREFIX + STRING_SEPARATOR + MD5Hash.getMD5Hash(userName);
		} catch (Exception e) {
			fileNameforJsonGen = collectionCode + TWEET_IDS_PREFIX;
		}

		String fileName = fileNameforJsonGen + extension;
		int totalCount = 0;

		try {
			List<String> fileNames = FileSystemOperations.getAllJSONFileVolumes(collectionCode);
			Collections.sort(fileNames);
			Collections.reverse(fileNames);

			// BufferedReader br = null;
			ReversedLinesFileReader br = null;

			String fileToDelete = PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode + FILE_SEPARATOR + fileName;
			// System.out.println("Deleteing file : " + fileToDelete + ".zip");
			logger.info("Deleteing file : " + fileToDelete + ZIP_EXTENSION);
			FileSystemOperations.deleteFile(fileToDelete + ZIP_EXTENSION); // delete if there exist a csv file with same name

			StringBuffer outputFile = new StringBuffer().append(folderLocation).append(fileName);
			beanWriter = new BufferedWriter(new FileWriterWithEncoding(outputFile.toString(), "UTF-8"), BUFFER_SIZE);
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType)) {
				beanWriter.write("[");
			}
			// First build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			if (queryList != null)
				tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

			for (String file : fileNames) {
				if (downloadLimited
						&& totalCount > Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
					break;
				}
				String fileLocation = PersisterConfigurator.getInstance().getProperty(
						PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
						+ collectionCode + FILE_SEPARATOR + file;
				logger.info("Reading file " + fileLocation);
				try {
					// br = new BufferedReader(new FileReader(fileLocation));
					File f = new File(fileLocation);
					br = new ReversedLinesFileReader(f);

					String line;
					while ((line = br.readLine()) != null) {
						if (downloadLimited
								&& totalCount >= Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
										PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
							break;
						}
						ClassifiedTweet tweet = getClassifiedTweet(line);
						if (tweet != null && tweet.getTweetID() != null) {
							// Apply filter on tweet
							if (satisfiesFilter(queryList, tweetFilter, tweet)) {
								if (DownloadJsonType.JSON_OBJECT.equals(jsonType)
										&& totalCount < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
												PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT)) && totalCount > 0) {
									beanWriter.write(", ");
								}
								// write to file
								beanWriter.write(createJsonClassifiedTweetIDString(tweet));
								beanWriter.newLine();
								++totalCount;
							}
						}
					}
					br.close();

				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file");
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file read");
				}
			} // end for
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
				beanWriter.write("]");
				jsonObjectClosed = true;
			}
			beanWriter.flush();
			beanWriter.close();

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
		} finally {
			if (beanWriter != null) {
				try {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
						beanWriter.write("]");
						jsonObjectClosed = true;
					}
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
				}
			}
		}
		FileCompressor compressor = new FileCompressor(folderLocation, folderLocation, fileName);
		String fileToDelete = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode + FILE_SEPARATOR + fileName;
		fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode
				+ FILE_SEPARATOR + compressor.zip();
		// System.out.println("Deleteing file : " + fileToDelete);
		logger.info("Deleteing file : " + fileToDelete);
		FileSystemOperations.deleteFile(fileToDelete); // delete if there exist
														// a csv file with same
														// name

		return ResultStatus.getUIWrapper("fileName", fileName, "count", totalCount);
	}
	
	
	public Map<String, Object> generateClassifiedJson2TweetIdsOnlyJSONFiltered(String collectionCode, Integer exportLimit, 
			JsonQueryList queryList, DownloadJsonType jsonType, String userName, Boolean removeRetweet) {

		String extension = DownloadJsonType.getSuffixString(jsonType);
		if (null == extension) {
			extension = DownloadJsonType.defaultSuffix();
		}
		boolean jsonObjectClosed = false;

		BufferedWriter beanWriter = null;
		// String fileNameforJsonGen = "Classified_" + collectionCode +
		// "_tweetIds_filtered";
		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode + FILE_SEPARATOR;
		String fileNameforJsonGen = null;
		try {
			fileNameforJsonGen = collectionCode + TWEET_IDS_PREFIX + STRING_SEPARATOR + MD5Hash.getMD5Hash(userName);
		} catch (Exception e) {
			fileNameforJsonGen = collectionCode + TWEET_IDS_PREFIX;
		}

		String fileName = fileNameforJsonGen + extension;
		int totalCount = 0;

		try {
			List<String> fileNames = FileSystemOperations.getAllJSONFileVolumes(collectionCode);
			Collections.sort(fileNames);
			Collections.reverse(fileNames);

			// BufferedReader br = null;
			ReversedLinesFileReader br = null;

			String fileToDelete = PersisterConfigurator.getInstance().getProperty(
					PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
					+ collectionCode + FILE_SEPARATOR + fileName;
			// System.out.println("Deleteing file : " + fileToDelete + ".zip");
			logger.info("Deleteing file : " + fileToDelete + ZIP_EXTENSION);
			FileSystemOperations.deleteFile(fileToDelete + ZIP_EXTENSION); // delete if there exist a csv file with same name

			StringBuffer outputFile = new StringBuffer().append(folderLocation).append(fileName);
			beanWriter = new BufferedWriter(new FileWriterWithEncoding(outputFile.toString(), "UTF-8"), BUFFER_SIZE);
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType)) {
				beanWriter.write("[");
			}
			// First build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			if (queryList != null)
				tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

			for (String file : fileNames) {
				if ( totalCount > exportLimit) {
					break;
				}
				String fileLocation = PersisterConfigurator.getInstance().getProperty(
						PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
						+ collectionCode + FILE_SEPARATOR + file;
				logger.info("Reading file " + fileLocation);
				try {
					// br = new BufferedReader(new FileReader(fileLocation));
					File f = new File(fileLocation);
					br = new ReversedLinesFileReader(f);

					String line;
					while ((line = br.readLine()) != null) {
						if (totalCount >= exportLimit) {
							break;
						}
						ClassifiedTweet tweet = getClassifiedTweet(line);
						if (tweet != null && tweet.getTweetID() != null) {
							// Apply filter on tweet
							if (satisfiesFilter(queryList, tweetFilter, tweet)) {
								if (DownloadJsonType.JSON_OBJECT.equals(jsonType)
										&& totalCount < exportLimit && totalCount > 0) {
									beanWriter.write(", ");
								}
								// write to file
								JSONObject obj = new JSONObject();
								obj.put("id", tweet.getTweetID());
								
								beanWriter.write(obj.toJSONString());
								beanWriter.newLine();
								++totalCount;
							}
						}
					}
					br.close();

				} catch (FileNotFoundException ex) {
					logger.error(collectionCode + ": couldn't find file");
				} catch (IOException ex) {
					logger.error(collectionCode + ": IO Exception for file read");
				}
			} // end for
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
				beanWriter.write("]");
				jsonObjectClosed = true;
			}
			beanWriter.flush();
			beanWriter.close();

		} catch (FileNotFoundException ex) {
			logger.error(collectionCode + ": couldn't find file");
		} catch (IOException ex) {
			logger.error(collectionCode + ": IO Exception for file read");
		} finally {
			if (beanWriter != null) {
				try {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
						beanWriter.write("]");
						jsonObjectClosed = true;
					}
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
				}
			}
		}
		FileCompressor compressor = new FileCompressor(folderLocation, folderLocation, fileName);
		String fileToDelete = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode + FILE_SEPARATOR + fileName;
		fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode
				+ FILE_SEPARATOR + compressor.zip();
		// System.out.println("Deleteing file : " + fileToDelete);
		logger.info("Deleteing file : " + fileToDelete);
		FileSystemOperations.deleteFile(fileToDelete); // delete if there exist
														// a csv file with same
														// name

		return ResultStatus.getUIWrapper("fileName", fileName, "count", totalCount);
	}

	public String createJsonClassifiedTweetIDString(ClassifiedTweet tweet) {
		JSONObject obj = new JSONObject();

		obj.put("id", tweet.getTweetID());
		obj.put("crisis_name", tweet.getCrisisName());
		if (tweet.getNominalLabels() != null) {
			List<NominalLabel> nbList = tweet.getNominalLabels();
			for (int i = 0; i < nbList.size(); i++) {
				NominalLabel nb = nbList.get(i);
				obj.put("attribute_name_" + i, nb.attribute_name);
				obj.put("attribute_code_" + i, nb.attribute_code);
				obj.put("label_name_" + i, nb.label_name);
				obj.put("label_description_" + i, nb.label_description);
				obj.put("label_code_" + i, nb.label_code);
				obj.put("confidence_" + i, nb.confidence);
				obj.put("humanLabeled_" + i, nb.from_human);
			}
		}
		return obj.toJSONString();
	}

	public boolean satisfiesFilter(final JsonQueryList queryList, final FilterQueryMatcher tweetFilter, final ClassifiedTweet tweet) {
		// Apply filter on tweet
		// logger.info("queryList = " + queryList + ", constraints = " +
		// (queryList != null ? queryList.getConstraints().isEmpty() : "null"));
		if (null == queryList || queryList.getConstraints().isEmpty()) {
			// logger.info("No filtering");
			return true; // no filtering
		} else {
			if (!tweet.getNominalLabels().isEmpty()) {
				return tweetFilter.getMatcherResult(tweet); // satisfies filter
			}
		}
		return false;
	}

	public String generateClassifiedList2CSV_100K_BasedOnTweetCountFiltered(String collectionCode, int exportLimit,
			JsonQueryList queryList, HumanLabeledDocumentList labeledItems, String userName) {
		ICsvMapWriter writer = null;
		String fileName = null;
		String fileNameforCSVGen = null;

		try {
			fileNameforCSVGen = collectionCode + HUMAN_TAGGED_FILE_PREFIX + MD5Hash.getMD5Hash(userName) + CSV_EXTENSION;
			fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL)
					+ FILE_SEPARATOR + collectionCode + FILE_SEPARATOR + fileNameforCSVGen;
		} catch (Exception e) {
			logger.error("Exception while generating MD5Hash for user: " + userName);
			return null;
		}

		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode;

		FileSystemOperations.deleteFile(folderLocation + FILE_SEPARATOR + fileNameforCSVGen + ZIP_EXTENSION);
		logger.info("Deleted existing file: " + folderLocation + FILE_SEPARATOR + fileNameforCSVGen + ZIP_EXTENSION);
		try {
			List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>(LIST_BUFFER_SIZE);
			ReadWriteCSV<CellProcessor> csv = new ReadWriteCSV<CellProcessor>(collectionCode);
			String[] runningHeader = null;
			int currentSize = 0;
			// writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList,
			// collectionCode, fileNameforCSVGen, writer);

			// First build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			if (queryList != null)
				tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

			for (HumanLabeledDocumentDTO dto : labeledItems.getItems()) {
				ClassifiedTweet tweet = new ClassifiedTweet();
				tweet.toClassifiedTweetFromLabeledDoc(dto, collectionCode);
				
				if (0 == currentSize && runningHeader == null && writer == null) {
					runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetCSVHeader,
							ReadWriteCSV.FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweet);
					writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList, collectionCode, fileNameforCSVGen, writer);
				}
				// logger.info("Parsed tweet = " + tweet.toJsonString());
				if (tweet != null && satisfiesFilter(queryList, tweetFilter, tweet)) {
					if (tweetsList.size() < exportLimit && tweetsList.size() < LIST_BUFFER_SIZE) {
						// Apply filter on tweet
						tweetsList.add(tweet);
					} else {
						// write buffer full, write to csv file
						int countToWrite = Math.min(tweetsList.size(), exportLimit - currentSize);
						if (countToWrite > 0) {
							// System.out.println("exportLimit = " + exportLimit
							// + ", currentSize = " + currentSize +
							// ", countToWrite = " + countToWrite);
							writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList.subList(0, countToWrite), collectionCode,
									fileNameforCSVGen, writer);
							currentSize += countToWrite;
							logger.info("currentSize = " + currentSize + ", countToWrite = " + countToWrite);
						}
						// clear contents from tweetsList buffer
						tweetsList.clear();
						if (currentSize >= exportLimit) {
							break; // we are done
						}
						// Otherwise add the tweet to fresh buffer and continue
						tweetsList.add(tweet);
					}
				}
			}

			int countToWrite = Math.min(tweetsList.size(), exportLimit - currentSize);
			// logger.info("Outside for loop: currentSize = " + currentSize +
			// ", countToWrite = " + countToWrite + " tweetsList size = " +
			// tweetsList.size());
			if (countToWrite > 0) {
				// System.out.println("exportLimit = " + exportLimit +
				// ", currentSize = " + currentSize + ", countToWrite = " +
				// countToWrite);
				if (0 == currentSize && runningHeader == null && writer == null) {
					runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetCSVHeader,
							ReadWriteCSV.FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweetsList.get(0));
				}
				writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList.subList(0, countToWrite), collectionCode,
						fileNameforCSVGen, writer);
				tweetsList.clear();
			}
		} catch (Exception e) {
			logger.error("Exception in generateClassifiedList2CSV_100K_BasedOnTweetCountFiltered", e);
			return null;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("Exception in generateClassifiedList2CSV_100K_BasedOnTweetCountFiltered", e);
					return null;
				}
			}
		}
		// Compress generated file and send the compressed file link
		FileCompressor compressor = new FileCompressor(folderLocation, folderLocation, fileNameforCSVGen);
		fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode
				+ FILE_SEPARATOR + compressor.zip();
		FileSystemOperations.deleteFile(folderLocation + FILE_SEPARATOR + fileNameforCSVGen);
		logger.info("Deleted raw file post compression: " + fileNameforCSVGen);
		return fileName;
	}

	public Map<String, Object> generateClassifiedList2TweetIdsCSVFiltered(String collectionCode, JsonQueryList queryList,
			Boolean downloadLimited, HumanLabeledDocumentList labeledItems, String userName) {
		ICsvMapWriter writer = null;
		String fileName = null;
		String fileNameforCSVGen = null;

		try {
			fileNameforCSVGen = collectionCode + HUMAN_TAGGED_FILE_PREFIX + "tweetIds-" + MD5Hash.getMD5Hash(userName) + CSV_EXTENSION;
			fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL)
					+ FILE_SEPARATOR + collectionCode + FILE_SEPARATOR + fileNameforCSVGen;
		} catch (Exception e) {
			logger.error("Exception while generating MD5Hash for user: " + userName);
			return null;
		}
		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode;

		int totalCount = 0;
		List<ClassifiedTweet> tweetsList = new ArrayList<ClassifiedTweet>(LIST_BUFFER_SIZE);
		try {
			ReadWriteCSV<CellProcessor> csv = new ReadWriteCSV<CellProcessor>(collectionCode);
			String[] runningHeader = null;

			FileSystemOperations.deleteFile(folderLocation + FILE_SEPARATOR + fileNameforCSVGen + ZIP_EXTENSION);
			logger.info(collectionCode + ": Deleteing file : " + fileNameforCSVGen + ZIP_EXTENSION);

			// Added by koushik - first build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			if (queryList != null)
				tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

			// writer = csv.writeClassifiedTweetIDsCSV(runningHeader, writer,
			// tweetsList, collectionCode, fileNameforCSVGen);

			for (HumanLabeledDocumentDTO dto : labeledItems.getItems()) {
				ClassifiedTweet tweet = new ClassifiedTweet();
				tweet.toClassifiedTweetFromLabeledDoc(dto, collectionCode);
				if (0 == totalCount && runningHeader == null && writer == null) {
					runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetCSVHeader,
							ReadWriteCSV.FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweet);
					writer = csv.writeClassifiedTweetsCSV(runningHeader, tweetsList, collectionCode, fileNameforCSVGen, writer);
				}
				if (downloadLimited
						&& totalCount >= Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
					tweetsList.clear();
					break;
				}
				if (tweet != null && satisfiesFilter(queryList, tweetFilter, tweet)) {
					if (tweetsList.size() < LIST_BUFFER_SIZE
							&& tweetsList.size() < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
									PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
						tweetsList.add(tweet);
					} else {
						int countToWrite;
						if (downloadLimited) {
							countToWrite = Math.min(
									tweetsList.size(),
									Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
											PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))
											- totalCount);
						} else {
							countToWrite = tweetsList.size();
						}
						if (countToWrite > 0) {
							writer = csv.writeClassifiedTweetIDsCSV(runningHeader, writer, tweetsList.subList(0, countToWrite),
									collectionCode, fileNameforCSVGen);
							totalCount += countToWrite;
						}
						tweetsList.clear();
						tweetsList.add(tweet);
					}
				}
			} // end for
			int countToWrite = tweetsList.size();
			if (downloadLimited) {
				countToWrite = Math.min(
						tweetsList.size(),
						Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))
								- totalCount);
			}
			if (countToWrite > 0 && !tweetsList.isEmpty()) {
				if (0 == totalCount && runningHeader == null && writer == null) {
					runningHeader = csv.setClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetIDCSVHeader,
							ReadWriteCSV.FIXED_CLASSIFIED_TWEET_ID_HEADER_SIZE, tweetsList.get(0));
				}
				writer = csv.writeClassifiedTweetIDsCSV(runningHeader, writer, tweetsList.subList(0, countToWrite), collectionCode,
						fileNameforCSVGen);
				totalCount += countToWrite;
				tweetsList.clear();
			}
		} catch (Exception e) {
			logger.error("Exception in generateClassifiedList2TweetIdsCSVFiltered");
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for csv file write ");
				}
			}
		}

		// beanWriter = csv.writeClassifiedTweetIDsCSV(beanWriter, tweetsList,
		// collectionCode, fileNameforCSVGen);
		tweetsList.clear();
		FileCompressor compressor = new FileCompressor(folderLocation, folderLocation, fileNameforCSVGen);
		fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode
				+ FILE_SEPARATOR + compressor.zip();
		FileSystemOperations.deleteFile(folderLocation + FILE_SEPARATOR + fileNameforCSVGen);
		// System.out.println("Deleted raw file post compression: " +
		// fileNameforCSVGen);
		logger.info("Deleted raw file post compression: " + fileNameforCSVGen);
		return ResultStatus.getUIWrapper("fileName", fileName, "count", totalCount);
	}

	public String generateClassifiedList2JSON_100K_BasedOnTweetCountFiltered(String collectionCode, int exportLimit,
			JsonQueryList queryList, DownloadJsonType jsonType, HumanLabeledDocumentList labeledItems, String userName) {
		String fileName = null;
		String fileNameforGen = null;

		String extension = DownloadJsonType.getSuffixString(jsonType);
		if (null == extension) {
			extension = DownloadJsonType.defaultSuffix();
		}

		// If everything ok, then finally generate the fileName
		try {
			fileNameforGen = collectionCode + HUMAN_TAGGED_FILE_PREFIX + MD5Hash.getMD5Hash(userName) + extension;
			fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL)
					+ FILE_SEPARATOR + collectionCode + FILE_SEPARATOR + fileNameforGen;
		} catch (Exception e) {
			logger.error("Exception while generating MD5Hash for user: " + userName);
			return null;
		}

		BufferedWriter beanWriter = null;

		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode;
		boolean jsonObjectClosed = false;
		try {
			FileSystemOperations.deleteFile(folderLocation + FILE_SEPARATOR + fileNameforGen + ZIP_EXTENSION);
			logger.info("Deleted existing file: " + folderLocation + FILE_SEPARATOR + fileNameforGen + ZIP_EXTENSION);

			StringBuffer outputFile = new StringBuffer().append(folderLocation).append(FILE_SEPARATOR).append(fileNameforGen);
			beanWriter = new BufferedWriter(new FileWriterWithEncoding(outputFile.toString(), "UTF-8"), BUFFER_SIZE);

			if (DownloadJsonType.JSON_OBJECT.equals(jsonType)) {
				beanWriter.write("[");
			}
			// First build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			if (queryList != null)
				tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

			long currentSize = 0;
			boolean isDone = false;
			for (HumanLabeledDocumentDTO dto : labeledItems.getItems()) {
				ClassifiedTweet tweet = new ClassifiedTweet();
				tweet.toClassifiedTweetFromLabeledDoc(dto, collectionCode);
				if (currentSize < exportLimit
						&& currentSize < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.TWEETS_EXPORT_LIMIT))) {
					// Apply filter on tweet
					if (satisfiesFilter(queryList, tweetFilter, tweet)) {
						if (DownloadJsonType.JSON_OBJECT.equals(jsonType)
								&& currentSize < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
										PersisterConfigurationProperty.TWEETS_EXPORT_LIMIT)) && currentSize > 0) {
							beanWriter.write(", ");
						}
						// write to file
						String jsonString = tweet.toJsonString();
						beanWriter.write(jsonString);
						beanWriter.newLine();
						++currentSize;
					}
					// System.out.println("currentSize  : " + currentSize);
				} else {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
						beanWriter.write("]");
						jsonObjectClosed = true;
					}
					beanWriter.flush();
					isDone = true;
					break;
				}
				if (isDone) {
					beanWriter.newLine();
					beanWriter.close();
					break;
				}
			} // end for
		} catch (Exception ex) {
			logger.error(collectionCode + ": empty list of files to read");
		} finally {
			if (beanWriter != null) {
				try {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
						beanWriter.write("]");
						beanWriter.newLine();
						jsonObjectClosed = true;
					}
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
				}
			}
		}
		FileCompressor compressor = new FileCompressor(folderLocation, folderLocation, fileNameforGen);
		fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode
				+ FILE_SEPARATOR + compressor.zip();
		FileSystemOperations.deleteFile(folderLocation + FILE_SEPARATOR + fileNameforGen);
		// System.out.println("Deleted raw file post compression: " +
		// fileNameforGen);
		logger.info("Deleted raw file post compression: " + fileNameforGen);
		return fileName;

	}

	public Map<String, Object> generateClassifiedList2TweetIdsJSONFiltered(String collectionCode, Boolean downloadLimited,
			JsonQueryList queryList, DownloadJsonType jsonType, HumanLabeledDocumentList labeledItems, String userName) {

		String fileName = null;
		String fileNameforGen = null;
		String extension = DownloadJsonType.getSuffixString(jsonType);
		if (null == extension) {
			extension = DownloadJsonType.defaultSuffix();
		}

		boolean jsonObjectClosed = false;
		BufferedWriter beanWriter = null;

		String folderLocation = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode;
		// If everything ok, then finally generate the fileName
		try {
			fileNameforGen = collectionCode + HUMAN_TAGGED_FILE_PREFIX + "tweetIds-" + MD5Hash.getMD5Hash(userName) + extension;
			fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL)
					+ FILE_SEPARATOR + collectionCode + FILE_SEPARATOR + fileNameforGen;
		} catch (Exception e) {
			logger.error("Error while generating MD5Hash for the user: " + userName);
			return null;
		}
		String fileToDelete = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH)
				+ collectionCode + FILE_SEPARATOR + fileNameforGen;
		int totalCount = 0;
		try {
			// System.out.println("Deleteing file : " + fileToDelete + ".zip");
			logger.info("Deleteing file : " + fileToDelete + ZIP_EXTENSION);
			FileSystemOperations.deleteFile(fileToDelete + ZIP_EXTENSION); // delete
																			// if
																			// there
																			// exist
																			// a
																			// file
																			// with
																			// same
																			// name

			StringBuffer outputFile = new StringBuffer().append(folderLocation).append(FILE_SEPARATOR).append(fileNameforGen);
			beanWriter = new BufferedWriter(new FileWriterWithEncoding(outputFile.toString(), "UTF-8"), BUFFER_SIZE);
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType)) {
				beanWriter.write("[");
			}
			// First build the FilterQueryMatcher
			FilterQueryMatcher tweetFilter = new FilterQueryMatcher();
			if (queryList != null)
				tweetFilter.queryList.setConstraints(queryList);
			tweetFilter.buildMatcherArray();

			for (HumanLabeledDocumentDTO dto : labeledItems.getItems()) {
				if (downloadLimited
						&& totalCount > Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
								PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT))) {
					break;
				}
				ClassifiedTweet tweet = new ClassifiedTweet();
				tweet.toClassifiedTweetFromLabeledDoc(dto, collectionCode);

				// Apply filter on tweet
				if (satisfiesFilter(queryList, tweetFilter, tweet)) {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType)
							&& totalCount < Integer.parseInt(PersisterConfigurator.getInstance().getProperty(
									PersisterConfigurationProperty.DEFAULT_TWEETID_VOLUME_LIMIT)) && totalCount > 0) {
						beanWriter.write(", ");
					}
					// write to file
					beanWriter.write(createJsonClassifiedTweetIDString(tweet));
					beanWriter.newLine();
					++totalCount;
				}

			} // end for
			if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
				beanWriter.write("]");
				jsonObjectClosed = true;
			}
			beanWriter.flush();
			beanWriter.close();
		} catch (Exception ex) {
			logger.error(collectionCode + ": IO Exception for file read");
		} finally {
			if (beanWriter != null) {
				try {
					if (DownloadJsonType.JSON_OBJECT.equals(jsonType) && !jsonObjectClosed) {
						beanWriter.write("]");
						jsonObjectClosed = true;
					}
					beanWriter.close();
				} catch (IOException ex) {
					logger.error(collectionCode + ": IOException for JSON file write ");
				}
			}
		}
		FileCompressor compressor = new FileCompressor(folderLocation, folderLocation, fileNameforGen);
		fileName = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.PERSISTER_DOWNLOAD_URL) + collectionCode
				+ FILE_SEPARATOR + compressor.zip();
		FileSystemOperations.deleteFile(fileToDelete); // delete if there exist
														// a file with same name
		// System.out.println("Deleted raw file post compression: " +
		// fileToDelete);
		logger.info("Deleted raw file post compression: " + fileToDelete);
		return ResultStatus.getUIWrapper("fileName", fileName, "count", totalCount);
	}
}
