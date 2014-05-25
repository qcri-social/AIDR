package qa.qcri.aidr.utils;

/**
 * Contains common config constants used throughout the system. 
 * 
 * @author Imran
 */
public class Config {

	//TODO: Replace with config file in text format

	public static final String FETCHER_CHANNEL = "FetcherChannel.";
	public static final String TAGGER_CHANNEL = "aidr_predict.";

	public static final String DEFAULT_PERSISTER = "default_persister";
	public static final String DEFAULT_PERSISTER_CODE = "default_code";
	public static final String DEFAULT_PERSISTER_FILE_PATH = "/var/www/aidr/data/persister/";
	//public static final String DEFAULT_PERSISTER_FILE_PATH = "/var/data/aidr/persister/";

	public static final int DEFAULT_FILE_VOLUMN_LIMIT = 100000; // 100K limit imposed by Twitter
	public static final int TWEETS_EXPORT_LIMIT_100K = 100000;
	public static final int DEFAULT_FILE_WRITER_BUFFER_SIZE = 500;
	public static final int INPUT_PORT = 4320;
	public static final int OUTPUT_PORT = 4321;

	public static final String REDIS_HOST = "localhost";
	//public static final String REDIS_HOST = "78.100.128.137";

	public static final int REDIS_PORT = 6379;		// default is 6379 - koushik
	
	public static final String LOG_FILE_NAME = "AIDR_Persister_Log.txt";//"errorlog.txt";

	public static final String SCD1_URL = "http://scd1.qcri.org/aidr/data/persister/";	// scd1
	//public static final String SCD1_URL = "http://localhost/aidr/data/persister/";		// koushik

}
