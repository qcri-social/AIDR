package qa.qcri.aidr.trainer.pybossa.store;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/20/13
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class URLPrefixCode {
    public static String ASSINGN_TASK = "/document/getbatchtaskbuffer/";
    public static String TASK_PUBLISH = "/task?api_key=";
    public static String TASK_ANSWER_SAVE = "/taskanswer/save";

    public static String TASK_INFO = "/task?project_id=";
    public static String TASKRUN_INFO = "/taskrun?project_id=";



    public static String AIDR_ACTIVE_NOMINAL_ATTRIBUTE ="/crisis/getnominalAttribute";
    public static String AIDR_CRISIS_INFO="/crisis/id/";
    public static String AIDR_NOMINAL_ATTRIBUTE_LABEL = "/crisis/getnominalLabels/";
    public static String AIDR_TASKASSIGNMENT_REVERT= "/taskassignment/revert/name/";

    public static String PYBOSAA_APP = "/project/";
    public static String PYBOSSA_APP_KEY ="/project?api_key=";
    public static String PYBOSSA_SHORT_NAME="/project?short_name=";


    public static String PYBOSSA_APP_UPDATE_KEY = "?api_key=";

    public static String PYBOSSA_TASK_DELETE = "/task/";

    public static String MM_GEO_SOURCE_PATH = "http://aidr-prod.qcri.org/data/trainer/";

    public static String MICROMAPPER_API_SOURCE_SAVE_URL = "http://gis.micromappers.org/MMAPI/rest/source/save";

}
