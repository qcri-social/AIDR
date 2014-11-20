package qa.qcri.aidr.predict.common;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * Abstract helper class for event handling based on the subscriber pattern.
 * 
 * @param <T>
 *            The data type used to pass information from the sender to the
 *            listener.
 * @author jrogstadius
 */
public class Event<T>  {

	private static Logger logger = Logger.getLogger(Event.class);
	
    private boolean disposed = false;

    private Vector<Function<EventArgs<T>>> listeners;

    public void subscribe(Function<EventArgs<T>> callback) {
        checkDisposed();

        if (listeners == null)
            listeners = new Vector<Function<EventArgs<T>>>();

        if (!listeners.contains(callback))
            listeners.add(callback);
    }

    public void unsubscribe(Function<EventArgs<T>> callback) {
        if (listeners == null)
            return;

        listeners.remove(callback);
    }

    public void fire(Object source, T result) {
        checkDisposed();

        if (listeners != null) {
            for (Function<EventArgs<T>> callback : listeners) {
                try {
                    callback.execute(new EventArgs<T>(source, result));
                } catch (Exception e) {
                    logger.error("Exception when firing event", e);
                }
            }
        }
    }

    public void dispose() {
        listeners.clear();
        disposed = true;
    }

    private void checkDisposed() {
        if (disposed)
            throw new RuntimeException("Object has been disposed.");
    }
}
