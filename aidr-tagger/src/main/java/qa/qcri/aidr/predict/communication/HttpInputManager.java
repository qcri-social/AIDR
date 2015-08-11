package qa.qcri.aidr.predict.communication;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.log4j.Logger;

import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;

/**
 * InputManager listens for new connections on a specified port. New
 * InputWorkers are created for each client that connects.
 * 
 * @author jrogstadius
 */
public class HttpInputManager implements Runnable {

    static ServerSocket server;
    
    private static Logger logger = Logger.getLogger(HttpInputManager.class);
    
    @Override
    public void run() {
        try {
			server = new ServerSocket(Integer.parseInt(TaggerConfigurator
					.getInstance().getProperty(
							TaggerConfigurationProperty.HTTP_INPUT_PORT)));
			logger.info("Listening for provider connections on port "
					+ Integer
							.parseInt(TaggerConfigurator
									.getInstance()
									.getProperty(
											TaggerConfigurationProperty.HTTP_INPUT_PORT)));

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
                                    + Integer.parseInt(TaggerConfigurator
                        					.getInstance().getProperty(
                        							TaggerConfigurationProperty.HTTP_INPUT_PORT)));
                }
            }
        } catch (IOException e) {
            logger.warn("Could not listen on port "
                    + Integer.parseInt(TaggerConfigurator
        					.getInstance().getProperty(
        							TaggerConfigurationProperty.HTTP_INPUT_PORT)));
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
            logger.warn("Could not close socket");
        }
    }
}
