package qa.qcri.aidr.utils;

/**
 * Contains common config constants used throughout the system. 
 * 
 * @author Imran
 */
public class Config {

	//TODO: Replace with config file in text format

	public static final String FETCHER_CHANNEL = "FetcherChannel.";
	public static final String COLLECTION_CHANNEL = "CollectionChannel.";
	public static final String TAGGER_CHANNEL = "aidr_predict.";

	public static final String DEFAULT_PERSISTER = "default_persister";
	public static final String DEFAULT_PERSISTER_CODE = "default_code";
	public static final String DEFAULT_PERSISTER_FILE_PATH = "/var/www/aidr/data/persister/";		// default location
    //public static final String DEFAULT_PERSISTER_FILE_PATH = "/export/sc/aidr/data/persister/";	// for local VM
	//public static final String DEFAULT_PERSISTER_FILE_PATH = "/sc/projects/aidr/data/persister/";	// for azure VM

	public static final int DEFAULT_FILE_VOLUMN_LIMIT = 100000; // 100K limit imposed by Twitter
	public static final int TWEETS_EXPORT_LIMIT_100K = 100000;
	public static final int DEFAULT_FILE_WRITER_BUFFER_SIZE = 1024;//10485760;		// 10MB buffer size
	
	public static final int DEFAULT_TWEETID_VOLUME_LIMIT = 1000000;		// max. number of tweets downloadable from AIDR website
	public static final int INPUT_PORT = 4320;
	public static final int OUTPUT_PORT = 4321;

	public static final String REDIS_HOST = "localhost";
	//public static final String REDIS_HOST = "78.100.128.137";

	public static final int REDIS_PORT = 6379;		// default is 6379 - koushik
	
	public static final String LOG_FILE_NAME = "AIDR_Persister_Log.txt";//"errorlog.txt";

	//public static final String SCD1_URL = "http://scd1.qcri.org/aidr/data/persister/";	// scd1
	public static final String SCD1_URL = "http://localhost/aidr/data/persister/";	// koushik
	//public static final String SCD1_URL = "http://aidr-dev.qcri.org/aidr/data/persister/";	// for local VM
	//public static final String SCD1_URL = "http://aidr-prod.qcri.org/data/persister/";	// for azure VM
	
	// Related to Load shedding
	public static final int PERSISTER_LOAD_LIMIT = 6000;			// messages/interval
	public static final int PERSISTER_LOAD_CHECK_INTERVAL_MINUTES = 1;		// in minutes

	// Related to Tweet Download limit
	public static final String managerUrl = "http://localhost:8080/AIDRFetchManager";
	public static final String TWEET_DOWNLOAD_LIMIT_MSG = "The size of the collection is larger than " + TWEETS_EXPORT_LIMIT_100K + ". To download the full tweet IDs collection, please contact the AIDR admin at aidr@noreply.github.com";
}
