package qa.qcri.aidr.predictui.util;

/**
 * 
 * @author Imran
 */
public class Config {

	//TODO: Replace with config file in text format

	public static final String STATUS_CODE_SUCCESS = "SUCCESS";
	public static final String STATUS_CODE_FAILED = "FAILED";

	//TODO : THIS PATH HAS TO BE UPDATED BASED ON aidr-tagger location
	public static final String AIDR_TAGGER_CONFIG_URL = "/home/imran/aidr_predict/config/config.txt";
	public static final String LOG_FILE_NAME = "AIDR-Predict-logs.txt";//

	//For deleting stale tasks from Document entity/table
	public static final String TASK_EXPIRY_AGE_LIMIT = "6h";
	public static final String TASK_BUFFER_SCAN_INTERVAL = "1h";

}
