package qa.qcri.aidr.predict.common;

/**
 * Helper class for event handling.
 * 
 * @author jrogstadius
 */
public class EventArgs<T> {
    public Object sender;
    public T result;

    public EventArgs(Object sender, T args) {
        this.sender = sender;
        this.result = args;
    }
}
