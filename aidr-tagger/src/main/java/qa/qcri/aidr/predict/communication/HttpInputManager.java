package qa.qcri.aidr.predict.communication;

import java.io.IOException;
import java.net.ServerSocket;
import qa.qcri.aidr.predict.common.*;

/**
 * InputManager listens for new connections on a specified port. New
 * InputWorkers are created for each client that connects.
 * 
 * @author jrogstadius
 */
public class HttpInputManager extends Loggable implements Runnable {

    static ServerSocket server;

    @Override
    public void run() {
        try {
            server = new ServerSocket(Config.HTTP_INPUT_PORT);
            log(LogLevel.INFO, "Listening for provider connections on port "
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
                    log(LogLevel.WARNING,
                            "Failed to establish connection with client on port "
                                    + Config.HTTP_INPUT_PORT);
                }
            }
        } catch (IOException e) {
            log(LogLevel.ERROR, "Could not listen on port "
                    + Config.HTTP_INPUT_PORT);
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
            log("Could not close socket", e);
        }
    }
}
