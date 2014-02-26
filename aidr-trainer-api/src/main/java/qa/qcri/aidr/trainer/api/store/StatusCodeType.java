package qa.qcri.aidr.trainer.api.store;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/1/13
 * Time: 8:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class StatusCodeType {

    public static Integer TASK_BUFFER_STATUS_AVAILABLE = 0;
    public static Integer TASK_BUFFER_STATUS_ASSIGNED = 1;

    public static String TASK_COMMIT_SUCCESS = "success";

    public static String CRISIS_PYBOSSA_SERVICE_READY =  "ready";
    public static String CRISIS_PYBOSSA_SERVICE_NOT_READY =  "not_ready";

    public static Integer Task_NOT_PUBLISHED = 0;
    public static Integer TASK_PUBLISHED = 1;
    public static Integer TASK_LIFECYCLE_COMPLETED = 2;
    public static Integer TASK_EXPORTED = 3;

    public static Integer MICROMAPPER_ONLY = 2;
    public static Integer AIDR_ONLY = 1;
    public static Integer CLIENT_APP_INACTIVE = 3;



}
