package qa.qcri.aidr.common.log;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/3/14
 * Time: 8:23 AM
 */

public interface ItemInterface {

    void addDetailsItem(String label, Object value);

    void addItem(String label, boolean value);

    void addItem(String label, char value);

    void addItem(String label, byte value);

    void addItem(String label, short value);

    void addItem(String label, int value);

    void addItem(String label, long value);

    void addItem(String label, float value);

    void addItem(String label, double value);

    void addItem(String label, Object value);

    String getDescription();

    int getErrorCode();

    String stackTraceToString();

    void log();

}
