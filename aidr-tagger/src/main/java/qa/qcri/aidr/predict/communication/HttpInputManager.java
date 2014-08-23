package qa.qcri.aidr.predict.communication;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.log4j.Logger;

import qa.qcri.aidr.predict.common.*;

/**
 * InputManager listens for new connections on a specified port. New
 * InputWorkers are created for each client that connects.
 * 
 * @author jrogstadius
 */
public class HttpInputManager extends Loggable implements Runnable {

    static ServerSocket server;
    
    private static Logger logger = Logger.getLogger(HttpInputManager.class);
    private static ErrorLog elog = new ErrorLog();
    
    @Override
    public void run() {
        try {
            server = new ServerSocket(Config.HTTP_INPUT_PORT);
            logger.info("Listening for provider connections on port "
                    + Config.HTTP_INPUT_PORT);

            while (true) {
                if (Thread.interrupted()) {
                    return;
                }

                try {
                    HttpInputWorker conn = new HttpInputWorker(server.accept());
                    Thread t = new Thread(conn);
                    t.start();
                } catch (IOException e) {
                    logger.warn("Failed to establish connection with client on port "
                                    + Config.HTTP_INPUT_PORT);
                }
            }
        } catch (IOException e) {
            logger.error("Could not listen on port "
                    + Config.HTTP_INPUT_PORT);
            logger.error(elog.toStringException(e));
        } finally {

            finalize();
        }

    }

    @Override
    protected void finalize() {
        // Objects created in run method are finalized when
        // program terminates and thread exits
        try {
            server.close();
        } catch (IOException e) {
            logger.error("Could not close socket");
            logger.error(elog.toStringException(e));
        }
    }
}
