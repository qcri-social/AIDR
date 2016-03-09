/**
 * This class provides an implementation to create a CSV file containing tweets with variable number of classifiers. 
 * 
 * @author Imran
 * @param <CellProcessors>
 */

package qa.qcri.aidr.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.encoder.DefaultCsvEncoder;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import qa.qcri.aidr.common.filter.NominalLabel;
import qa.qcri.aidr.utils.ClassifiedTweet;
import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;
import qa.qcri.aidr.utils.Tweet;


public class ReadWriteCSV<CellProcessors> {

	private static Logger logger = Logger.getLogger(ReadWriteCSV.class);

	private String collectionCode = null; 

	public static final String[] ClassifiedTweetCSVHeader = new String[]{"tweetID", "message","userID", "userName", "userURL", "createdAt", "tweetURL", "crisisName"}; 
	public static final String[] ClassifiedTweetIDCSVHeader = new String[]{"tweetID", "crisisName"};

	public static final int FIXED_CLASSIFIED_TWEET_HEADER_SIZE = ClassifiedTweetCSVHeader.length;
	public static final int FIXED_CLASSIFIED_TWEET_ID_HEADER_SIZE = ClassifiedTweetIDCSVHeader.length;
	public static final int VARIABLE_HEADER_SIZE = 7;	// number of variable header elements per classifier
	private static final int DEFAULT_CLASSIFIER_COUNT = 1;

	private static int countWritten = 0;
	
	public ReadWriteCSV(String collectionCode) {
		this.collectionCode = collectionCode;
	}

	public ReadWriteCSV() {
		this(null);
	}

	private static CellProcessor[] getProcessors4TweetIDSCCSV() {

		final CellProcessor[] processors = new CellProcessor[]{
				//new UniqueHashCode(), // tweetID (must be unique)
				new NotNull()	// tweetID (must be unique)
		};


		return processors;
	}

	private static CellProcessor[] getCollectorTweetsProcessors() {

		final CellProcessor[] processors = new CellProcessor[]{
				//new UniqueHashCode(), // tweetID (must be unique)
				//new NotNull(),	// tweetID (must be unique)
				new Optional(),		// data shows that sometimes tweetID CAN be null!
				new Optional(), // message
				//new FmtDate("dd/MM/yyyy"), // birthDate
				new Optional(), // userID
				new Optional(), // userName
				//new Optional(new FmtBool("Y", "N")), // isRT
				new Optional(), // userURL
				new Optional(), // createdAt
				new Optional() // tweet permanent URL
		};


		return processors;
	}

	private static CellProcessor[] getClassifiedTweetVariableProcessors(final int count) {
		CellProcessor[] processors = new CellProcessor[count];
		for (int i = 0;i < count;i++) {
			processors[i] = new Optional();
		}
		return processors;
	}

	private static CellProcessor[] getProcessors4ClassifiedCCSV() {

		final CellProcessor[] processors = new CellProcessor[]{
				//new UniqueHashCode(), // tweetID (must be unique)
				//new NotNull(),	// tweetID (must be unique)
				new Optional(),	// tweetID - data shows that sometimes tweetID CAN be null!
				new Optional(), // message
				new Optional(), // userID
				new Optional(), // userName
				new Optional(), // userURL
				new Optional(), // createdAt
				new NotNull(),	// crisis name
				new Optional(),	// attribute name
				new Optional(),	// attribute code
				new Optional(), // label name
				new Optional(), // label description
				new Optional(), // label code
				new Optional(), // confidence
				new Optional() // humanLabeled
		};
		return processors;
	}

	private static CellProcessor[] getProcessors4ClassifiedTweetIDSCCSV() {

		final CellProcessor[] processors = new CellProcessor[]{
				//new UniqueHashCode(), // tweetID (must be unique)
				new NotNull(),	// tweetID (must be unique): sometimes CAN be null!
				new NotNull(), 	// crisis name
				new Optional(),	// attribute name
				new Optional(), // attribute code
				new Optional(), // label name
				new Optional(), // label description
				new Optional(),	// label code
				new Optional(), // confidence
				new Optional(), // humanLabeled
		};
		return processors;
	}

	public ICsvBeanWriter getCSVBeanWriter(String fileToWrite) {
		try {
			return new CsvBeanWriter(new FileWriter(fileToWrite, true),
					new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE)
			.useEncoder(new DefaultCsvEncoder())
			.build() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error in creating CSV Bean writer!");
			logger.error("Exception",e);
		}
		return null;
	}

	public ICsvMapWriter getCSVMapWriter(String fileToWrite) {
		try {
			return new CsvMapWriter(new FileWriterWithEncoding(fileToWrite,"UTF-8", true),
					new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE)
			.useEncoder(new DefaultCsvEncoder())
			.build() );
		} catch (IOException e) {
			logger.error("Error in creating CSV Bean writer!"+e);
		}
		return null;
	}

	public ICsvBeanWriter writeCollectorTweetIDSCSV(ICsvBeanWriter beanWriter, List<Tweet> tweetsList, String collectionDIR, String fileName) {
		try {
			// the header elements are used to map the bean values to each column (names must match)
			//final String[] header = new String[]{"tweetID", "message","userID", "userName", "userURL", "createdAt", "tweetURL"};
			//final CellProcessor[] processors = getProcessors();

			// koushik: shouldn't we be writing only the tweetIDs?
			final String[] header = new String[]{"tweetID"};

			final CellProcessor[] processors = getProcessors4TweetIDSCCSV();

			String persisterDIR = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH);
			//fileName = StringUtils.substringBefore(fileName, ".json"); //removing .json extension
			String fileToWrite = persisterDIR + collectionDIR + "/" + fileName;
			logger.info(collectionDIR + ": Writing CSV file : " + fileToWrite);

			if (null == beanWriter) { 
				beanWriter = getCSVBeanWriter(fileToWrite);
				// write the header
				beanWriter.writeHeader(header);
			}

			for (final Tweet tweet : tweetsList) {
				try {
					if (tweet.getTweetID() != null) {
						beanWriter.write(tweet, header, processors);
					}
				} catch (SuperCsvCellProcessorException e) {
					logger.error(collectionDIR + ": SuperCSV error");
				}
			}

		} catch (IOException ex) {
			logger.error(collectionDIR + ": IO Exception occured");
		} 
		//return fileName+".csv";
		return beanWriter;
	}

	public ICsvMapWriter writeClassifiedTweetIDsCSV(String[] runningHeader, ICsvMapWriter mapWriter, final List<ClassifiedTweet> tweetsList, String collectionDIR, String fileName) {
		// the header elements are used to map the bean values to each column (names must match)
		String[] header = ClassifiedTweetIDCSVHeader;
		//String[] runningHeader = null;
		try {
			if (null == mapWriter) {
				String persisterDIR = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH);
				String fileToWrite = persisterDIR + collectionDIR + "/" + fileName;
				logger.info(collectionDIR + ": Writing CSV file : " + fileToWrite);
				mapWriter = getCSVMapWriter(fileToWrite);

				// First write the header
				if (runningHeader != null) mapWriter.writeHeader(runningHeader);
				countWritten = 0;
			}
		} catch (Exception ex) {
			logger.error(collectionDIR + ": Exception occured when creating a mapWriter instance");
			logger.error("Exception",ex);
			return null;
		}

		// Now write to CSV file using CsvMapWriter
		for (final ClassifiedTweet tweet : tweetsList) {
			try {
				//logger.info("Current header length :: Actual number of cells needed: " + runningHeader.length + "::" + getClassifedTweetHeaderSize(FIXED_CLASSIFIED_TWEET_ID_HEADER_SIZE, tweet.getNominalLabels().size()));
				if (runningHeader.length < getClassifedTweetHeaderSize(FIXED_CLASSIFIED_TWEET_ID_HEADER_SIZE, tweet.getNominalLabels().size())) {
					// reallocate header
					runningHeader = resetClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetIDCSVHeader, ReadWriteCSV.FIXED_CLASSIFIED_TWEET_ID_HEADER_SIZE, tweet.getNominalLabels().size());

					logger.info("Reallocated running header. After reallocation, Current header length :: Actual number of cells needed: " 
							+ runningHeader.length + "::" + getClassifedTweetHeaderSize(FIXED_CLASSIFIED_TWEET_ID_HEADER_SIZE, tweet.getNominalLabels().size()));
				}
				final Map<String, Object> tweetToWrite = createClassifiedTweetIDCsvMap(runningHeader, tweet);
				final CellProcessor[] processors = getClassifiedTweetVariableProcessors(runningHeader.length);
				mapWriter.write(tweetToWrite, runningHeader, processors);
				++countWritten;
			} catch (SuperCsvCellProcessorException e) {
				logger.error(collectionDIR + ": SuperCSV error. Offending tweet: " + tweet.getTweetID());
			} catch (IOException e) {
				logger.error(collectionDIR + "IOException in writing tweet: " + tweet.getTweetID());
			}
		}
		return mapWriter;
	}

	public ICsvMapWriter writeClassifiedTweetIDsOnlyCSV(String[] runningHeader, ICsvMapWriter mapWriter, final List<ClassifiedTweet> tweetsList, String collectionDIR, String fileName) {
		// the header elements are used to map the bean values to each column (names must match)
		//String[] runningHeader = null;
		try {
			if (null == mapWriter) {
				String persisterDIR = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH);
				String fileToWrite = persisterDIR + collectionDIR + "/" + fileName;
				logger.info(collectionDIR + ": Writing CSV file : " + fileToWrite);
				mapWriter = getCSVMapWriter(fileToWrite);

				// First write the header
				if (runningHeader != null) mapWriter.writeHeader(runningHeader);
				countWritten = 0;
			}
		} catch (Exception ex) {
			logger.error(collectionDIR + ": Exception occured when creating a mapWriter instance");
			logger.error("Exception",ex);
			return null;
		}

		// Now write to CSV file using CsvMapWriter
		for (final ClassifiedTweet tweet : tweetsList) {
			try {
				final Map<String, Object> tweetToWrite = new HashMap<String, Object>();
				tweetToWrite.put(runningHeader[0], tweet.getTweetID());
				final CellProcessor[] processors = getClassifiedTweetVariableProcessors(runningHeader.length);
				mapWriter.write(tweetToWrite, runningHeader, processors);
				++countWritten;
			} catch (SuperCsvCellProcessorException e) {
				logger.error(collectionDIR + ": SuperCSV error. Offending tweet: " + tweet.getTweetID());
			} catch (IOException e) {
				logger.error(collectionDIR + "IOException in writing tweet: " + tweet.getTweetID());
			}
		}
		return mapWriter;
	}
	
	
	public ICsvBeanWriter writeCollectorTweetsCSV(List<Tweet> tweetsList, String collectionDIR, String fileName, ICsvBeanWriter beanWriter) {

		try {
			final String[] header = new String[]{"tweetID", "message","userID", "userName", "userURL", "createdAt", "tweetURL"};
			final CellProcessor[] processors = getCollectorTweetsProcessors();

			if(null == beanWriter){
				String persisterDIR = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH);
				//fileName = StringUtils.substringBefore(fileName, ".json"); //removing .json extension
				String fileToWrite = persisterDIR + collectionDIR + "/" + fileName;
				logger.info(collectionDIR + ": Writing CSV file : " + fileToWrite);
				beanWriter = getCSVBeanWriter(fileToWrite);
				beanWriter.writeHeader(header);
			}

			for (final Tweet tweet : tweetsList) {
				try {
					beanWriter.write(tweet, header, processors);
				} catch (SuperCsvCellProcessorException e) {
					logger.error(collectionDIR + ": SuperCSV error");
				}
			}

		} catch (IOException ex) {
			logger.error(collectionDIR + ": IO Exception occured");
		}
		return beanWriter;
	}


	public ICsvMapWriter writeClassifiedTweetsCSV(String[] runningHeader, List<ClassifiedTweet> tweetsList, String collectionDIR, String fileName, ICsvMapWriter mapWriter) {
		String[] header = ClassifiedTweetCSVHeader;
		try {
			if (null == mapWriter) {
				String persisterDIR = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH);
				String fileToWrite = persisterDIR + collectionDIR + "/" + fileName;
				logger.info(collectionDIR + ": Writing CSV file : " + fileToWrite);
				mapWriter = getCSVMapWriter(fileToWrite);

				// First write the header
				if (runningHeader != null) mapWriter.writeHeader(runningHeader);
				countWritten = 0;
			}		
		} catch (Exception ex) {
			logger.error(collectionDIR + ": Exception occured when creating a mapWriter instance");
			logger.error("Exception",ex);
			return null;
		}

		// Now write to CSV file using CsvMapWriter
		logger.info("Received length of tweets List to write = " + tweetsList.size());
		for (final ClassifiedTweet tweet : tweetsList) {
			try {
				//logger.info("Current header length :: Actual number of cells needed: " + runningHeader.length + "::" + getClassifedTweetHeaderSize(FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweet.getNominalLabels().size()));
				if (runningHeader.length < getClassifedTweetHeaderSize(FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweet.getNominalLabels().size())) {
					// reallocate header
					runningHeader = resetClassifiedTweetHeader(ReadWriteCSV.ClassifiedTweetCSVHeader, ReadWriteCSV.FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweet.getNominalLabels().size());

					logger.info("Reallocated running header. After reallocation, Current header length :: Actual number of cells needed: " 
							+ runningHeader.length + "::" + getClassifedTweetHeaderSize(FIXED_CLASSIFIED_TWEET_HEADER_SIZE, tweet.getNominalLabels().size()));
				}
				final Map<String, Object> tweetToWrite = createClassifiedTweetCsvMap(runningHeader, tweet);
				final CellProcessor[] processors = getClassifiedTweetVariableProcessors(runningHeader.length);
				//logger.info("Going to write: " + tweetToWrite);
				mapWriter.write(tweetToWrite, runningHeader, processors);
				++countWritten;

			} catch (SuperCsvCellProcessorException e) {
				logger.error(collectionDIR + ": SuperCSV error. Offending tweet: " + tweet.getTweetID());
				logger.error("Exception",e);
			} catch (IOException e) {
				logger.error(collectionDIR + "IOException in writing tweet: " + tweet.getTweetID());
			}
		}
		logger.info("Actual number of tweets written so far: " + countWritten);
		return mapWriter;
	}

	public String[] resetClassifiedTweetHeader(String[] header, int fixedHeaderSize, int numberOfClassifiers) {
		String[] fullHeader = new String[getClassifedTweetHeaderSize(fixedHeaderSize, numberOfClassifiers)];
		for (int i = 0;i < header.length;i++) {
			fullHeader[i] = header[i];
		}
		int endPoint = header.length;
		for (int i = 0;i < numberOfClassifiers;i++) {
			fullHeader[endPoint] = new String("attributeName_" + (i+1));
			fullHeader[endPoint+1] = new String("attributeCode_" + (i+1));
			fullHeader[endPoint+2] = new String("labelName_" + (i+1));
			fullHeader[endPoint + 3] = new String("labelDescription_" + (i+1));
			fullHeader[endPoint + 4] = new String("labelCode_" + (i+1));
			fullHeader[endPoint+5] = new String("confidence_" + (i+1));
			fullHeader[endPoint+6] = new String("humanLabeled_" + (i+1));
			endPoint += VARIABLE_HEADER_SIZE;
		}
		logger.info("Number of classifiers = " + numberOfClassifiers + ", headerSize = " + fullHeader.length);
		return fullHeader;
	}

	public String[] setClassifiedTweetHeader(String[] header, int fixedHeaderSize, ClassifiedTweet tweet) {

		int numberOfClassifiers = 0;
		Map<String, Integer> classifierCount = getClassifierCountForCrisis(this.collectionCode);
		if (classifierCount.containsKey("count") && classifierCount.get("count") == -1) {
			// estimate based on current 'tweet'
			numberOfClassifiers = getClassiferCountFromTweet(tweet);
			logger.info("Estimated classifier count based on first tweet = " + numberOfClassifiers);
		} else {
			// set as per obtained value
			numberOfClassifiers = classifierCount.get("count");
			logger.info("Number of classifier count based on tagger-API data = " + numberOfClassifiers);
		}
		String[] fullHeader = new String[getClassifedTweetHeaderSize(fixedHeaderSize, numberOfClassifiers)];
		for (int i = 0;i < header.length;i++) {
			fullHeader[i] = header[i];
		}
		int endPoint = header.length;
		for (int i = 0;i < numberOfClassifiers;i++) {
			fullHeader[endPoint] = new String("attributeName_" + (i+1));
			fullHeader[endPoint+1] = new String("attributeCode_" + (i+1));
			fullHeader[endPoint+2] = new String("labelName_" + (i+1));
			fullHeader[endPoint + 3] = new String("labelDescription_" + (i+1));
			fullHeader[endPoint + 4] = new String("labelCode_" + (i+1));
			fullHeader[endPoint+5] = new String("confidence_" + (i+1));
			fullHeader[endPoint+6] = new String("humanLabeled_" + (i+1));
			endPoint += VARIABLE_HEADER_SIZE;
		}
		logger.info("Number of classifiers = " + numberOfClassifiers + ", headerSize = " + fullHeader.length);
		return fullHeader;
	}


	private Map<String, Integer> getClassifierCountForCrisis(String collectionCode) {
		Map<String, Integer> jsonResponse = null;
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			WebTarget webResource = client.target(PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.TAGGER_MAIN_URL) + "/crisis/attributes/count/" + collectionCode);
			Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
			jsonResponse = clientResponse.readEntity(Map.class); 
			logger.info("Tagger API returned: " + jsonResponse);
			if (jsonResponse.containsKey("count") && jsonResponse.get("count") != null) {
				if (jsonResponse.get("count") > 0) {
					return jsonResponse;
				} else {
					jsonResponse.put("count", DEFAULT_CLASSIFIER_COUNT);
				}
			} else {
				jsonResponse = new HashMap<String, Integer>();
				jsonResponse.put("count", -1);
			}
		} catch (Exception e) {
			logger.info("Unable to get classifiers count from Tagger API, will try based on first read tweet, jsonResponse = " + jsonResponse);
			jsonResponse = new HashMap<String, Integer>();
			jsonResponse.put("count", -1);
		}
		return jsonResponse;
	}

	public int getClassifedTweetHeaderSize(int fixedHeaderSize, ClassifiedTweet tweet) {
		int numberOfClassifiers = 0;
		if (tweet.getNominalLabels() != null) {
			numberOfClassifiers = tweet.getNominalLabels().size();
			logger.info("From nominal_labels size = " + tweet.getNominalLabels().size());
		} else {
			numberOfClassifiers = DEFAULT_CLASSIFIER_COUNT;
			logger.info("From default value = " + DEFAULT_CLASSIFIER_COUNT);
		}
		return (fixedHeaderSize + numberOfClassifiers * VARIABLE_HEADER_SIZE);		// number of nominal_label elements

	}

	public int getClassifedTweetHeaderSize(int fixedHeaderSize, int numberOfClassifiers) {
		return (fixedHeaderSize + numberOfClassifiers * VARIABLE_HEADER_SIZE);		// number of nominal_label elements
	}

	public int getClassiferCountFromTweet(ClassifiedTweet tweet) {
		int numberOfClassifiers = 0;
		if (tweet.getNominalLabels() != null) {
			numberOfClassifiers = tweet.getNominalLabels().size();
			logger.info("From nominal_labels size = " + tweet.getNominalLabels().size());
		} else {
			numberOfClassifiers = DEFAULT_CLASSIFIER_COUNT;
			logger.info("From default value = " + DEFAULT_CLASSIFIER_COUNT);
		}
		return numberOfClassifiers; 	// number of nominal_labels
	}

	private Map<String, Object> createClassifiedTweetCsvMap(String[] header, ClassifiedTweet tweet) {
		Map<String, Object> tweetToWrite = new HashMap<String, Object>();
		int i = 0;	
		tweetToWrite.put(header[i], tweet.getTweetID());
		tweetToWrite.put(header[i+1], tweet.getMessage());
		tweetToWrite.put(header[i+2], tweet.getUserID());
		tweetToWrite.put(header[i+3], tweet.getUserName());
		tweetToWrite.put(header[i+4], tweet.getUserURL());
		tweetToWrite.put(header[i+5], tweet.getCreatedAt());
		tweetToWrite.put(header[i+6], tweet.getTweetURL());
		tweetToWrite.put(header[i+7], tweet.getCrisisName());
		i = i + ClassifiedTweetCSVHeader.length;
		if (tweet.getNominalLabels() != null) {
			//logger.info("[createClassifiedTweetCsvMap] tweet toString :" + tweet.toString());
			//logger.info("[createClassifiedTweetCsvMap] tweet getNominalLabels size :" + tweet.getNominalLabels().size());
			tweetToWrite = writeVariableAttributeData(header, i, tweetToWrite, tweet);
		}
		return tweetToWrite;
	}

	private Map<String, Object> writeVariableAttributeData(final String[] header, final int startIndex, Map<String, Object> tweetToWrite, final ClassifiedTweet tweet) {
		int i = startIndex;
		//logger.info("[writeVariableAttributeData] tweet getNominalLabels size :" + tweet.getNominalLabels().size());        
		//logger.info("[writeVariableAttributeData] startIndex :" + i);
		for (int j = 0;j < tweet.getNominalLabels().size();j++) {
			try{
				NominalLabel nLabel = tweet.getNominalLabels().get(j);
				if (nLabel != null) {
					//logger.info("[writeVariableAttributeData] nLabel attribute_name :" + nLabel.attribute_name);
					tweetToWrite.put(header[i], nLabel.attribute_name);
					tweetToWrite.put(header[i+1], nLabel.attribute_code);
					tweetToWrite.put(header[i+2], nLabel.label_name);
					tweetToWrite.put(header[i+3], nLabel.label_description);
					tweetToWrite.put(header[i+4], nLabel.label_code);
					tweetToWrite.put(header[i+5], nLabel.confidence);
					tweetToWrite.put(header[i+6], nLabel.from_human);
					i += VARIABLE_HEADER_SIZE;
				}
			}
			catch(Exception e){
				logger.error("[writeVariableAttributeData] excpetion : " + e.getMessage() + " - " + e.getStackTrace());
			}
		}
		return tweetToWrite;
	}

	private Map<String, Object> createClassifiedTweetIDCsvMap(String[] header, ClassifiedTweet tweet) {
		Map<String, Object> tweetToWrite = new HashMap<String, Object>();
		int i = 0;	
		tweetToWrite.put(header[i], tweet.getTweetID());
		tweetToWrite.put(header[i+1], tweet.getCrisisName());
		i = ClassifiedTweetIDCSVHeader.length;
		if (tweet.getNominalLabels() != null) {
			tweetToWrite = writeVariableAttributeData(header, i, tweetToWrite, tweet);
		}
		return tweetToWrite;
	}
}
