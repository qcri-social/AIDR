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
	//public static final String AIDR_TAGGER_CONFIG_URL = "/home/local/QCRI/mimran/aidr/tagger/config/config.txt";
	public static final String AIDR_TAGGER_CONFIG_URL = "/home/mimran/aidr/aidr-tagger/config/config.txt";		// for aidr-prod server
	public static final String LOG_FILE_NAME = "aidr-tagger-logs.txt";//

	//For deleting stale tasks from Document entity/table
	public static final String TASK_EXPIRY_AGE_LIMIT = "6h";
	public static final String TASK_BUFFER_SCAN_INTERVAL = "1h";

    public static final int PUBLIC_LANDING_PAGE_TOP = 1;
    public static final int PUBLIC_LANDING_PAGE_BOTTOM = 2;
    public static final int CLASSIFIER_DESCRIPTION_PAGE = 3;
    public static final int CLASSIFIER_TUTORIAL_ONE = 4;
    public static final int CLASSIFIER_TUTORIAL_TWO = 5;
    public static final int CUSTOM_CURATOR = 6;
    public static final int CLASSIFIER_SKIN = 7;


}
