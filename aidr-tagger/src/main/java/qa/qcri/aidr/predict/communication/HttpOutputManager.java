package qa.qcri.aidr.predict.communication;

import java.io.IOException;
import java.net.ServerSocket;

import qa.qcri.aidr.predict.common.*;

/**
 * OutputManager listens for new connections on a specified port. New
 * OutputWorkers are created for each client that connects.
 * 
 * @author jrogstadius
 */
public class HttpOutputManager extends Loggable implements Runnable {

    static ServerSocket server;

    @Override
    public void run() {
        try {
            server = new ServerSocket(Config.HTTP_OUTPUT_PORT);
            log(LogLevel.INFO, "Listening for consumer connections on port "
                    + Config.HTTP_OUTPUT_PORT);

            while (true) {
                if (Thread.interrupted()) {
                    return;
                }

                try {
                    HttpOutputWorker.WorkerFactory factory = new HttpOutputWorker.WorkerFactory();
                    factory.initialize(server.accept());
                    Thread t = new Thread(factory);
                    t.start();
                } catch (IOException e) {
                    log(LogLevel.WARNING,
                            "Failed to establish connection with client on port "
                                    + Config.HTTP_OUTPUT_PORT);
                }
            }
        } catch (IOException e) {
            log(LogLevel.ERROR, "Could not listen on port "
                    + Config.HTTP_OUTPUT_PORT);
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
