package qa.qcri.aidr.collector.utils;

/**
 * Contains common config constants used throughout the system. 
 * 
 * @author Imran
 */
public class Config {

	//TODO: Replace with config file in text format
        
        public static final String STATUS_CODE_COLLECTION_INITIALIZING = "INITIALIZING";
        public static final String STATUS_CODE_COLLECTION_RUNNING = "RUNNING";
        public static final String STATUS_CODE_COLLECTION_ERROR = "FATAL-ERROR";
        public static final String STATUS_CODE_COLLECTION_RUNNING_WARNING = "RUNNING-WARNNING";
        public static final String STATUS_CODE_COLLECTION_NOTFOUND = "NOT-FOUND";
        public static final String STATUS_CODE_COLLECTION_STOPPED = "STOPPED";
        
        public static final String LANGUAGE_ALLOWED_ALL="ANY";
        
        // gf 3 way
        //public static final String FETCHER_REST_URI="http://localhost:8080/AIDRFetcher/webresources/fetcher";
        public static final String FETCHER_REST_URI="http://localhost:8084/AIDRCollector/webresources/";		// scd1
        //public static final String FETCHER_REST_URI="http://localhost:8080/AIDRCollector/webresources/";		// koushik
        
        // Default persister related parameters
        public static final boolean DEFAULT_PERSISTANCE_MODE = true;
        //public static final String DEFAULT_PERSISTER_FILE_LOCATION = "/var/www/aidr/data/persister/";		// for scd1
        public static final String DEFAULT_PERSISTER_FILE_LOCATION = "/sc/projects/aidr/data/persister/";		// for azure VM
        public static final String PERSISTER_REST_URI="http://localhost:8084/AIDRPersister/webresources/";	// scd1
        //public static final String PERSISTER_REST_URI="http://localhost:8080/AIDRPersister/webresources/";		// koushik
    
        // Redis paramters
        public static final int INPUT_PORT = 4320;
        public static final int OUTPUT_PORT = 4321;
        public static final String REDIS_HOST = "localhost";
        public static final String FETCHER_CHANNEL = "FetcherChannel";
        public static final String TAGGER_CHANNEL = "aidr_predict";
        public static final int FETCHER_REDIS_COUNTER_UPDATE_THRESHOLD = 5; // Specifies when items counter should be updated. Here 10 means after every 10 items
        
        // AIDR-FETCHER logging
        public static final String LOG_FILE_NAME = "AIDR_Fetcher_LOG.txt";//"errorlog.txt";
	
        // Related to Load shedding
        public static final int PERSISTER_LOAD_LIMIT = 6000;			// messages/interval
        public static final int PERSISTER_LOAD_CHECK_INTERVAL_MINUTES = 1;		// in minutes
}
