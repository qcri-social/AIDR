package qa.qcri.aidr.common.log;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/9/14
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */

public class LogError extends Error implements ItemInterface
{
    private LogThrowable logThrowable;


    public LogError() {
        super();
        logThrowable = new LogThrowable(this, null, 0);
        logThrowable.log();
    }

    public LogError(String message) {
        super(message);
        logThrowable = new LogThrowable(this, null, 0);
        logThrowable.log();
    }

    public LogError(String message, int errorCode) {
        super(message);
        logThrowable = new LogThrowable(this, null, errorCode);
        logThrowable.log();
    }

    public LogError(Throwable theThrowable) {
        super(
                theThrowable.getClass().getName() +
                        ((theThrowable.getMessage() == null || theThrowable.getMessage().length() == 0)
                                ? ""
                                : (": " + theThrowable.getMessage())
                        )
        );

        logThrowable = new LogThrowable(this, theThrowable, 0);
        logThrowable.log();
    }

    public LogError(Throwable theThrowable, int errorCode) {
        super(
                theThrowable.getClass().getName() +
                        ((theThrowable.getMessage() == null || theThrowable.getMessage().length() == 0)
                                ? ""
                                : (": " + theThrowable.getMessage())
                        )
        );

        logThrowable = new LogThrowable(this, theThrowable, errorCode);
        logThrowable.log();
    }

    public void addDetailsItem(String label, Object value) {
        logThrowable.addDetailsItem(label, value);
    }

    public void addItem(String label, boolean value) {
        logThrowable.addItem(label, value);
    }

    public void addItem(String label, char value) {
        logThrowable.addItem(label, value);
    }

    public void addItem(String label, byte value) {
        logThrowable.addItem(label, value);
    }

    public void addItem(String label, short value) {
        logThrowable.addItem(label, value);
    }

    public void addItem(String label, int value) {
        logThrowable.addItem(label, value);
    }

    public void addItem(String label, long value) {
        logThrowable.addItem(label, value);
    }

    public void addItem(String label, float value) {
        logThrowable.addItem(label, value);
    }

    public void addItem(String label, double value) {
        logThrowable.addItem(label, value);
    }

    public void addItem(String label, Object value) {
        logThrowable.addItem(label, value);
    }

    public String getDescription() {
        return logThrowable.getDescription();
    }

    public int getErrorCode() {
        return logThrowable.getErrorCode();
    }

    public String stackTraceToString() {
        return logThrowable.stackTraceToString();
    }

    public void log() {
        logThrowable.log();
    }

}