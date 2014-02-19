package qa.qcri.aidr.output.stream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ChunkedOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/test/chunk")
public class TestChunkedOutput {
	
	// Related to Async Thread management
	//public static ExecutorService executorServicePool;

	// Debugging
	private static Logger logger = LoggerFactory.getLogger(TestChunkedOutput.class);
	private static int sendCount = 0;
	//private ContainerResponse responseContext;
	
	/////////////////////////////////////////////////////////////////////////////

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public ChunkedOutput<String> getChunkedResponse() throws Throwable {
		final ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);
        new Thread() {
            public void run() {
                logger.info("Received GET request...");
            	try {
                    String chunk = getNextString();
                    while (chunk != null) {
                        if (!output.isClosed()) {
                        	if (!output.isClosed()) output.write(chunk);
                        	if (!output.isClosed()) output.write("\n");
                        }
                        else {
                        	logger.info("Possible client disconnect...");
                        	break;
                        }
                        try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
                        chunk = getNextString();
                        logger.info("received from generator: " + chunk);
                    }
                } catch (Exception e) {
                    // IOException thrown when writing the
                    // chunks of response: should be handled
                	logger.info("Error in write attempt");
                } 
            	logger.info("Done with async thread - joining with master now");
            	for (int i = 0;i < 10;i++) {
            		System.out.println("value = " + i);
            	}
            }
        }.start();
        logger.info("End of async main thread");
        return output;
    }
 
    protected String getNextString() {
    	if (TestChunkedOutput.sendCount < 10) {
    		StringBuilder str = new StringBuilder();
    		str.append("{").append("\"count\":").append(Integer.toString(TestChunkedOutput.sendCount)).append("}");
    		logger.info("returning to caller = " + str);
    		++TestChunkedOutput.sendCount;
    		return str.toString();
    	}
    	sendCount = 0;
    	return null;
    }
}
