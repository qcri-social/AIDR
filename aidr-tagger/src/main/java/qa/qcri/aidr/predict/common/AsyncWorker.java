package qa.qcri.aidr.predict.common;

import org.apache.log4j.Logger;

/**
 * Helper class for running asynchronous operations.
 * 
 * @author jrogstadius
 * @param <T>
 *            Input type for the worker function.
 */
public class AsyncWorker<T> implements Runnable {
    public Function<T> function;
    public T functionArg;

    public Event<Object> onCompleted = new Event<Object>();
    
    private static Logger logger = Logger.getLogger(AsyncWorker.class);
    
    public AsyncWorker(Function<T> function, T data) {
        this.function = function;
        this.functionArg = data;
    }

    public void run() {
        try {
            function.execute(functionArg);
            onCompleted.fire(this, null);
        } catch (Exception e) {
            logger.error("Exception in AsyncTask", e);
        }
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }
}
