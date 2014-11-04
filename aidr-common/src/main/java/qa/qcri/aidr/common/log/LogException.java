package qa.qcri.aidr.common.log;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/3/14
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class LogException extends Exception implements ItemInterface {


    private LogThrowable loggedThrowable;

    public LogException() {
        super();

        loggedThrowable = new LogThrowable(this, null, 0, ItemType.ERROR, false);
        loggedThrowable.log();
    }

    public LogException(String message) {
        this(message, 0, ItemType.ERROR, false);
    }

    public LogException(String message, boolean isRetryable) {
        this(message, 0, ItemType.ERROR, isRetryable);
    }

    public LogException(String message, int errorCode, ItemType severity, boolean isRetryable) {
        super(message);
        loggedThrowable = new LogThrowable(this, null, errorCode, severity, isRetryable);
        loggedThrowable.log();
    }

    public LogException(Throwable throwable) {
        this(throwable, 0, ItemType.ERROR, false);
    }

    public LogException(Throwable throwable, int errorCode, ItemType severity, boolean retryable) {
        super(
                throwable.getClass().getName() + (throwable.getMessage().isEmpty() ? "" : (": " + throwable.getMessage())),
                throwable
        );

        loggedThrowable = new LogThrowable(this, throwable, errorCode, severity, retryable);
        loggedThrowable.log();
    }

    @Override
    public void addDetailsItem(String label, Object value) {
        loggedThrowable.addDetailsItem(label, value);
    }

    @Override
    public void addItem(String label, boolean value) {
        loggedThrowable.addItem(label, value);
    }

    @Override
    public void addItem(String label, char value) {
        loggedThrowable.addItem(label, value);
    }

    @Override
    public void addItem(String label, byte value) {
        loggedThrowable.addItem(label, value);
    }

    @Override
    public void addItem(String label, short value) {
        loggedThrowable.addItem(label, value);
    }

    @Override
    public void addItem(String label, int value) {
        loggedThrowable.addItem(label, value);
    }

    @Override
    public void addItem(String label, long value) {
        loggedThrowable.addItem(label, value);
    }

    @Override
    public void addItem(String label, float value) {
        loggedThrowable.addItem(label, value);
    }

    @Override
    public void addItem(String label, double value) {
        loggedThrowable.addItem(label, value);
    }

    @Override
    public void addItem(String label, Object value) {
        loggedThrowable.addItem(label, value);
    }

    public void addItem(String label, Object value, boolean asDetailsItem) {
        if (asDetailsItem)
            loggedThrowable.addDetailsItem(label, value);
        else
            loggedThrowable.addItem(label, value);
    }

    @Override
    public String getDescription() {
        return loggedThrowable.getDescription();
    }

    @Override
    public int getErrorCode() {
        return loggedThrowable.getErrorCode();
    }

    public String getFullName() {
        return loggedThrowable.getFullName();
    }

    @Override
    public ItemType getSeverity() {
        return loggedThrowable.getSeverity();
    }

    @Override
    public String stackTraceToString() {
        return loggedThrowable.stackTraceToString();
    }

    @Override
    public boolean isRetryable() {
        return loggedThrowable.isRetryable();
    }

    @Override
    public void log() {
        loggedThrowable.log();
    }

    public final LogThrowable getLoggedThrowable() {
        return loggedThrowable;
    }

    public final void setLoggedThrowable(LogThrowable lt) {
        loggedThrowable = lt;
    }

}
