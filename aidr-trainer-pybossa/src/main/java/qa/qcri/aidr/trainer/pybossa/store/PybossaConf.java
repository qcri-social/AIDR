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
    //http://scd1.qcri.org/aidr/data/trainer/
    public static String DEFAULT_TRAINER_FILE_PATH = "/var/www/aidr/data/trainer/";
    //public static String DEFAULT_TRAINER_FILE_PATH = "/var/www/micromappers/data/";
}
