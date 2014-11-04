package qa.qcri.aidr.common.log;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/3/14
 * Time: 9:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class LogMessage {
    public static final String NEWLINE = "\n";
    private String description;
    private int eventCode;
    private String id;
    private Collection<?> items;
    private String methodName;
    private Throwable throwable;
    private StackTraceElement[] trace;


    public LogMessage(String id, int eventCode, String methodName, String description, StackTraceElement[] trace,
                      Collection<?> items) {
        this.id = id;
        this.eventCode = eventCode;
        this.methodName = methodName;
        this.description = description;
        this.trace = trace;
        this.items = items;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<?> getItems() {
        return items;
    }

    public void setItems(Collection<?> items) {
        this.items = items;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public StackTraceElement[] getTrace() {
        return trace;
    }

    public void setTrace(StackTraceElement[] trace) {
        this.trace = trace;
    }


}