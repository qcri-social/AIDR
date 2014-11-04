package qa.qcri.aidr.common.log;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/3/14
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class LogItem {

    private String	label;
    private Object	value;


    public LogItem(String label, Object value) {
        this.label = label;
        this.value = value;
    }

    public LogItem(String label, boolean value) {
        this(label, Boolean.valueOf(value));
    }


    public LogItem(String label, byte value) {
        this(label, Byte.valueOf(value));
    }


    public LogItem(String label, char value) {
        this(label, new Character(value));
    }

    public LogItem(String label, double value) {
        this(label, new Double(value));
    }


    public LogItem(String label, float value) {
        this(label, new Float(value));
    }


    public LogItem(String label, int value) {
        this(label, Integer.valueOf(value));
    }


    public LogItem(String label, long value) {
        this(label, Long.valueOf(value));
    }


    public LogItem(String label, short value) {
        this(label, Short.valueOf(value));
    }

    public String toString() {
        return (label + "=" + value);
    }
}
