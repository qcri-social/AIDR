package qa.qcri.aidr.trainer.pybossa.store;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/21/13
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class PybossaConf {
    public static int DEFAULT_N_ANSWERS = 3;
    public static int DEFAULT_MAP_RAIDUS_CUT_OFF = 1;


    public static String VIDEO_CLICKER_RESPONSE_SEVERE = "severe";
    public static String VIDEO_CLICKER_RESPONSE_MILD = "mild";

    public static String GEOJSON_TYPE_POINT="point";
    public static String GEOJSON_TYPE_FEATURE_COLLECTION="FeatureCollection";


    public static String DEFAULT_TRAINER_FILE_PATH = "/mnt/data/trainer/";
    public static String DEFAULT_TRAINER_GEO_FILE_PATH = "/mnt/data/trainer/geo/";


    public static double ONE_MILE_RADIUS =  1609.34;
    public static double ONE_MILE_DISTANCE =  1;

    public static String TASK_STATUS_COMPLETED = "completed";

    public static String TASK_QUEUE_GEO_INFO_NOT_FOUND = "No Location Information";

    public static int DEFAULT_GEO_N_ANSWERS = 1;

    public static long DEFAULT_CATEGORY_ID = 3;

    public final static String BASE_URL = "https://twb.translationcenter.org/api/v1";
    //public final static String API_KEY = "niptz15xao0w";
    public final static String API_KEY = "jk26fh2yzwo4";
    public final static long TWB_TRANSLATE_DEADLINE = 3600000l; //in milli 1 hour
}
