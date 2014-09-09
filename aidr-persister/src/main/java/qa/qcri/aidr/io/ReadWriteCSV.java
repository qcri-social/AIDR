/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
//import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.io.ICsvWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.encoder.DefaultCsvEncoder;
import org.supercsv.exception.SuperCsvCellProcessorException;

import qa.qcri.aidr.logging.ErrorLog;
import qa.qcri.aidr.persister.filter.NominalLabel;
import qa.qcri.aidr.utils.Config;
import qa.qcri.aidr.utils.Tweet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import qa.qcri.aidr.utils.ClassifiedTweet;

/**
 *
 * @author Imran
 * @param <CellProcessors>
 */
public class ReadWriteCSV<CellProcessors> {

	//private static final int BUFFER_SIZE = 10 * 1024 * 1024;
	private static Logger logger = Logger.getLogger(ReadWriteCSV.class);
	private static ErrorLog elog = new ErrorLog();

	public static final int VARIABLE_HEADER_SIZE = 7;	// number of variable header elements per classifier

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
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	public ICsvMapWriter getCSVMapWriter(String fileToWrite) {
		try {
			return new CsvMapWriter(new FileWriter(fileToWrite, true),
					new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE)
			.useEncoder(new DefaultCsvEncoder())
			.build() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error in creating CSV Bean writer!");
			logger.error(elog.toStringException(e));
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

			String persisterDIR = Config.DEFAULT_PERSISTER_FILE_PATH;
			fileName = StringUtils.substringBefore(fileName, ".json"); //removing .json extension
			String fileToWrite = persisterDIR + collectionDIR + "/" + fileName + ".csv";
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
					//Logger.getLogger(ReadWriteCSV.class.getName()).log(Level.SEVERE, "[writeCollectorTweetIDSCSV] SuperCSV error");
					logger.error(collectionDIR + ": SuperCSV error");
					//e.printStackTrace();
				}
			}

		} catch (IOException ex) {
			//Logger.getLogger(ReadWriteCSV.class.getName()).log(Level.SEVERE, "[writeCollectorTweetIDSCSV] IO Exception occured");
			logger.error(collectionDIR + ": IO Exception occured");
			//ex.printStackTrace();
		} 
		//return fileName+".csv";
		return beanWriter;
	}

	public ICsvMapWriter writeClassifiedTweetIDsCSV(ICsvMapWriter mapWriter, final List<ClassifiedTweet> tweetsList, String collectionDIR, String fileName) {
		// the header elements are used to map the bean values to each column (names must match)
		String[] header = new String[]{"tweetID", "crisisName"};
		try {
			if (null == mapWriter) {
				String persisterDIR = Config.DEFAULT_PERSISTER_FILE_PATH;
				fileName = StringUtils.substringBefore(fileName, ".json"); //removing .json extension
				String fileToWrite = persisterDIR + collectionDIR + "/output/" + fileName + ".csv";
				logger.info(collectionDIR + ": Writing CSV file : " + fileToWrite);
				mapWriter = getCSVMapWriter(fileToWrite);

				// Determine the headers
				String[] runningHeader = null;
				if (!tweetsList.isEmpty()) {
					runningHeader  = setClassifiedTweetHeader(header, header.length, tweetsList.get(0));
				}
				// First write the header
				mapWriter.writeHeader(runningHeader);
			}
		} catch (Exception ex) {
			logger.error(collectionDIR + ": Exception occured when creating a mapWriter instance");
			logger.error(elog.toStringException(ex));
			return null;
		}

		// Now write to CSV file using CsvMapWriter
		// int count = 0;
		for (final ClassifiedTweet tweet : tweetsList) {
			String[] runningHeader  = setClassifiedTweetHeader(header, header.length, tweet);
			try {
				final Map<String, Object> tweetToWrite = createClassifiedTweetIDCsvMap(runningHeader, tweet);
				final CellProcessor[] processors = getClassifiedTweetVariableProcessors(runningHeader.length);
				/*
				if (count == 0) {
					System.out.print("Running Header: " + mapWriter.getLineNumber());
					for (int j = 0;j < runningHeader.length;j++) {
						System.out.print(runningHeader[j] + ", ");
					}
					System.out.print("Tweet received : " + tweet.getTweetID() + "," 
							+ tweet.getMessage() + "," + tweet.getCrisisName() + ",");

					for (int j = 0;j < tweet.getNominalLabels().size();j++) {
						NominalLabel nb = tweet.getNominalLabels().get(j);
						System.out.print("Attribute DETAILS: " 
								+ nb.attribute_name + "," + nb.attribute_code + ","
								+ nb.label_name + "," + nb.label_description + "," 
								+ nb.label_code + "," + nb.confidence + "," 
								+ nb.from_human);
					}
					System.out.println("Tweet to Write: " + tweetToWrite);
				}
				++count;*/
				mapWriter.write(tweetToWrite, runningHeader, processors);
			} catch (SuperCsvCellProcessorException e) {
				//logger.error(collectionDIR + ": SuperCSV error. Offending tweet: " + tweet.getTweetID());
				//logger.error(elog.toStringException(e));
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
				String persisterDIR = Config.DEFAULT_PERSISTER_FILE_PATH;
				fileName = StringUtils.substringBefore(fileName, ".json"); //removing .json extension
				String fileToWrite = persisterDIR + collectionDIR + "/" + fileName + ".csv";
				logger.info(collectionDIR + ": Writing CSV file : " + fileToWrite);
				//beanWriter = new CsvBeanWriter(new FileWriter(fileToWrite, true),
				//        CsvPreference.EXCEL_PREFERENCE);
				beanWriter = getCSVBeanWriter(fileToWrite);
				beanWriter.writeHeader(header);
			}

			for (final Tweet tweet : tweetsList) {
				try {
					beanWriter.write(tweet, header, processors);
				} catch (SuperCsvCellProcessorException e) {
					logger.error(collectionDIR + ": SuperCSV error");
					//e.printStackTrace();
				}
			}

		} catch (IOException ex) {
			logger.error(collectionDIR + ": IO Exception occured");
			//ex.printStackTrace();
		}
		return beanWriter;
	}


	public ICsvMapWriter writeClassifiedTweetsCSV(List<ClassifiedTweet> tweetsList, String collectionDIR, String fileName, ICsvMapWriter mapWriter) {
		String[] header = new String[]{"tweetID", "message","userID", "userName", "userURL", "createdAt", "tweetURL", "crisisName"}; 
		//System.out.println("mapWriter = " + mapWriter);
		try {
			if (null == mapWriter) {
				String persisterDIR = Config.DEFAULT_PERSISTER_FILE_PATH;
				fileName = StringUtils.substringBefore(fileName, ".json"); //removing .json extension
				String fileToWrite = persisterDIR + collectionDIR + "/output/" + fileName + ".csv";
				logger.info(collectionDIR + ": Writing CSV file : " + fileToWrite);
				mapWriter = getCSVMapWriter(fileToWrite);

				// Determine the headers
				String[] runningHeader = null;
				if (!tweetsList.isEmpty()) {
					runningHeader  = setClassifiedTweetHeader(header, header.length, tweetsList.get(0));
				}
				// First write the header
				mapWriter.writeHeader(runningHeader);
				/*
				System.out.print("Initiating Running Header: " + mapWriter.getLineNumber());
				for (int j = 0;j < runningHeader.length;j++) {
					System.out.print(runningHeader[j] + ", ");
				}*/
			}
		} catch (Exception ex) {
			logger.error(collectionDIR + ": Exception occured when creating a mapWriter instance");
			logger.error(elog.toStringException(ex));
			return null;
		}

		// Now write to CSV file using CsvMapWriter
		//int count = 0;
		for (final ClassifiedTweet tweet : tweetsList) {
			String[] runningHeader  = setClassifiedTweetHeader(header, header.length, tweet);
			try {
				final Map<String, Object> tweetToWrite = createClassifiedTweetCsvMap(runningHeader, tweet);
				final CellProcessor[] processors = getClassifiedTweetVariableProcessors(runningHeader.length);
				mapWriter.write(tweetToWrite, runningHeader, processors);
				/*
				if (count == 0) {
					System.out.print("Running Header: " + mapWriter.getLineNumber());
					for (int j = 0;j < runningHeader.length;j++) {
						System.out.print(runningHeader[j] + ", ");
					}
					System.out.print("Tweet received : " + tweet.getTweetID() + "," 
							+ tweet.getMessage() + "," + tweet.getCrisisName() + ",");

					for (int j = 0;j < tweet.getNominalLabels().size();j++) {
						NominalLabel nb = tweet.getNominalLabels().get(j);
						System.out.print("Attribute DETAILS: " 
								+ nb.attribute_name + "," + nb.attribute_code + ","
								+ nb.label_name + "," + nb.label_description + "," 
								+ nb.label_code + "," + nb.confidence + "," 
								+ nb.from_human);
					}
					System.out.println("Tweet to Write: " + tweetToWrite);
				}
				++count;*/
			} catch (SuperCsvCellProcessorException e) {
				//logger.error(collectionDIR + ": SuperCSV error. Offending tweet: " + tweet.getTweetID());
				//logger.error(elog.toStringException(e));
			} catch (IOException e) {
				logger.error(collectionDIR + "IOException in writing tweet: " + tweet.getTweetID());
			}
		}
		//System.out.println("Tweets written so far: " + count);
		return mapWriter;
	}

	public String[] setClassifiedTweetHeader(String[] header, int fixedHeaderSize, ClassifiedTweet tweet) {

		String[] fullHeader = new String[getClassifedTweetHeaderSize(fixedHeaderSize, tweet)];
		for (int i = 0;i < header.length;i++) {
			fullHeader[i] = header[i];
		}

		if (tweet.getNominalLabels() != null) {
			int numberOfClassifiers = tweet.getNominalLabels().size();
			int endPoint = header.length;
			for (int i = 0;i < numberOfClassifiers;i++) {
				fullHeader[endPoint] = new String("atributeName_" + (i+1));
				fullHeader[endPoint+1] = new String("atributeCode_" + (i+1));
				fullHeader[endPoint+2] = new String("labelName_" + (i+1));
				fullHeader[endPoint + 3] = new String("labelDescription_" + (i+1));
				fullHeader[endPoint + 4] = new String("labelCode_" + (i+1));
				fullHeader[endPoint+5] = new String("confidence_" + (i+1));
				fullHeader[endPoint+6] = new String("humanLabeled_" + (i+1));
				endPoint += VARIABLE_HEADER_SIZE;
			}
		}
		return fullHeader;
	}


	public int getClassifedTweetHeaderSize(int fixedHeaderSize, ClassifiedTweet tweet) {
		int numberOfClassifiers = 0;
		if (tweet.getNominalLabels() != null) {
			numberOfClassifiers = tweet.getNominalLabels().size();
		} 
		return (fixedHeaderSize + numberOfClassifiers * VARIABLE_HEADER_SIZE);		// number of nominal_label elements

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
		i = i + 8;
		if (tweet.getNominalLabels() != null) {
			tweetToWrite = writeVariableAttributeData(header, i, tweetToWrite, tweet);
		}
		return tweetToWrite;
	}
	
	private Map<String, Object> writeVariableAttributeData(final String[] header, final int startIndex, Map<String, Object> tweetToWrite, final ClassifiedTweet tweet) {
		int i = startIndex;
		for (int j = 0;j < tweet.getNominalLabels().size();j++) {
			NominalLabel nLabel = tweet.getNominalLabels().get(j);
			tweetToWrite.put(header[i], nLabel.attribute_name);
			tweetToWrite.put(header[i+1], nLabel.attribute_code);
			tweetToWrite.put(header[i+2], nLabel.label_name);
			tweetToWrite.put(header[i+3], nLabel.label_description);
			tweetToWrite.put(header[i+4], nLabel.label_code);
			tweetToWrite.put(header[i+5], nLabel.confidence);
			tweetToWrite.put(header[i+6], nLabel.from_human);
			i += VARIABLE_HEADER_SIZE;
		}
		return tweetToWrite;
	}

	private Map<String, Object> createClassifiedTweetIDCsvMap(String[] header, ClassifiedTweet tweet) {
		Map<String, Object> tweetToWrite = new HashMap<String, Object>();
		int i = 0;	
		tweetToWrite.put(header[i], tweet.getTweetID());
		tweetToWrite.put(header[i+1], tweet.getCrisisName());
		i = 2;
		if (tweet.getNominalLabels() != null) {
			tweetToWrite = writeVariableAttributeData(header, i, tweetToWrite, tweet);
		}
		return tweetToWrite;
	}
}
