package qa.qcri.aidr.trainer.pybossa.store;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/3/13
 * Time: 9:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusCodeType {

    public static Integer Task_NOT_PUBLISHED = 0;
    public static Integer TASK_PUBLISHED = 1;
    public static Integer TASK_LIFECYCLE_COMPLETED = 2;
    public static Integer TASK_EXPORTED = 3;
    public static Integer TASK_ABANDONED = 4;

    public static Integer MAX_PENDING_QUEUE_SIZE = 50;

    public static String TASK_COMMIT_SUCCESS = "success";


    public static Integer HTTP_OK = 200;
    public static Integer HTTP_OK_NO_CONTENT = 204;
    public static Integer HTTP_OK_DUPLICATE_INFO_FOUND = 415 ;

    public static Integer RESPONSE_MIN_LENGTH = 10;

    public static Integer CLIENT_APP_PENDING = 0;
    public static Integer MICROMAPPER_ONLY = 2;
    public static Integer AIDR_ONLY = 1;
    public static Integer CLIENT_APP_INACTIVE_REQUEST = 3;
    public static Integer CLIENT_APP_DISABLED = 4;


    public static Integer EXTERNAL_DATA_SOURCE_USED = 2;
    public static Integer EXTERNAL_DATA_SOURCE_ACTIVE = 1;
    public static Integer EXTERNAL_DATA_SOURCE_UPLOADED = 0;



    public static Integer TEMPLATE_IS_READY_FOR_EXPORT = 0;
    public static Integer TEMPLATE_EXPORTED = 1;

    public static Integer APP_MULTIPLE_CHOICE = 1;
    public static Integer APP_IMAGE = 2;
    public static Integer APP_VIDEO = 3;
    public static Integer APP_MAP = 4;

    public static long TASK_CLEANUP_CUT_OFF_HOUR = 12;

    public static Integer MIN_VOTE_CUT_OFF_VALUE = 1;
    public static Integer MAX_VOTE_CUT_OFF_VALUE = 2;

}
