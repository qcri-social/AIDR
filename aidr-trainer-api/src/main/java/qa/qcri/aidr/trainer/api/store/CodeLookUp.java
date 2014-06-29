package qa.qcri.aidr.trainer.api.store;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 1/18/14
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class CodeLookUp {

    public static Integer APP_MULTIPLE_CHOICE = 1;
    public static Integer APP_IMAGE = 2;
    public static Integer APP_VIDEO = 3;
    public static Integer APP_MAP = 4;
    public static Integer APP_AERIAL = 5;

    public static String APP_MULTIPLE_CHOICE_NAME = "Text Clicker";
    public static String APP_IMAGE_NAME = "Image Clicker";
    public static String APP_VIDEO_NAME = "Video Clicker";
    public static String APP_MAP_NAME = "Geo Clicker";
    public static String APP_AERIAL_NAME = "Aerial Clicker";


    public static Integer APP_STATUS_ALIVE = 200;
    public static Integer APP_SERVICE_COMPLETED = 200;

    public static Integer APP_SERVICE_SUCCESSFUL = 200;
    public static Integer APP_SERVICE_REQUST_FOUND_DUPLICATION_ENTRY = 500;

    public static Integer APP_REQUEST_SUCCESS = 200;

    public static Integer PUBLIC_LANDING_PAGE_TOP = 1;
    public static Integer PUBLIC_LANDING_PAGE_BOTTOM = 2;
    public static Integer CURATOR_NAME = 6;


    // pybossa upate
    public static Integer CLASSIFIER_WELCOME_PAGE = 3;
    public static Integer CLASSIFIER_TUTORIAL_ONE = 4;
    public static Integer CLASSIFIER_TUTORIAL_TWO = 5;
    public static Integer CLASSIFIER_SKIN = 7;


    public static Integer DEFAULT_SKIN = 0;
    public static Integer IPHONE_SKIN = 1;


    public static String WELCOMPAGE_UPDATE = "long_description";
    public static String TUTORIAL = "tutorial";
    public static String TASK_PRESENTER = "task_presenter";

    public static Integer DOCUMENT_REMAINING_COUNT = 20;


    public static Integer MAX_APP_HOLD_PERIOD_DAY = 7;

}
