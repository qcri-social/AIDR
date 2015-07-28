package qa.qcri.aidr.predict.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import org.apache.log4j.Logger;

import qa.qcri.aidr.predict.common.Event;

/**
 * OutputWorker maintains a persistent connection to a consumer of classified
 * documents.
 *
 * @author jrogstadius
 */
public class HttpOutputWorker {

    /*
     * This is a bit overly complicated, but Output workers need input data from
     * the client to be validly created (an ontology specification). Waiting for
     * the client data is a blocking operation, so the object creation is runs
     * in a separate thread.
     */
	private static Logger logger = Logger.getLogger(HttpOutputWorker.class);
	
    public static class WorkerFactory  implements Runnable {

        private Socket socket;

        public void initialize(Socket s) {
            socket = s;
        }

        @Override
        public void run() {
            OutputFilter o = getFilter(socket);
            if (o == null)
                return;

            HttpOutputWorker worker = new HttpOutputWorker(socket, o);
            HttpOutputWorkerIndex.getInstance().addWorker(o.crisisCode, worker);

            socket = null;
        }

        OutputFilter getFilter(Socket s) {

            /*
             * Example of a HTTP POST request sent from Google Chrome, with
             * three form fields at the bottom:
             * 
             * POST / HTTP/1.1 Host: localhost:4321 Connection: keep-alive
             * Content-Length: 53 Cache-Control: max-age=0 Accept:
             * text/html,application
             * /xhtml+xml,application/xml;q=0.9,*FRONTSLASH*;q=0.8 Origin: null
             * User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64)
             * AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.152
             * Safari/537.22 Content-Type: application/x-www-form-urlencoded
             * Accept-Encoding: gzip,deflate,sdch Accept-Language:
             * en-US,en;q=0.8 Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.3
             * 
             * username=jakob&password=pwd&ontology=3
             */

            logger.info("Waiting for POST data");

//            DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
//            conn.bind(ser.accept(), new BasicHttpParams());
//            HttpRequest request = conn.receiveRequestHeader();
//            conn.receiveRequestEntity((HttpEntityEnclosingRequest)request);
//            HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
//            System.out.println(EntityUtils.toString(entity));
//            
            BufferedReader in = null;

            try {
                in = new BufferedReader(new InputStreamReader(
                        s.getInputStream()));



            } catch (IOException e) {
                logger.error("IOException while trying to open input stream");
                return null;
            }

            Date t0 = new Date();
            try {
                while ((new Date().getTime() - t0.getTime() < 10000)) {
                    while (!in.ready()
                            && (new Date().getTime() - t0.getTime() < 2000)) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            logger.warn("Sleep interrupted while waiting for POST data");
                        }
                    }

                    String contentLengthKeyWord = "Content-Length";
                    String CRLF ="\r\n\r\n";

                    char[] cbuffer = new char[4000];
                    in.read(cbuffer);

                    String input = new String(cbuffer);

                    if(input.indexOf(contentLengthKeyWord) > -1){

                        int size = getContentSize(input);
                        int pos = input.indexOf(CRLF);
                        String strTemp = input.substring(pos);
                        if(size == strTemp.trim().length()){
                            input = strTemp;
                        }
                    }

                    logger.info("Recieved POST data: " + input);

                    return OutputFilter.fromJson(input);
                }
            } catch (IOException e) {
                logger.error("IOException while waiting for POST data");
            }

            logger.warn("Bad POST data.");
            return null;
        }
    }

    static int connectionID = 0;

    public final OutputFilter filter;
    public Event<Object> onConnectionClosed = new Event<Object>();

    private int connectionInstanceID;
    private Socket client;
    private PrintWriter clientStream = null;

    Integer pushCount = 0;

    private HttpOutputWorker(Socket s, OutputFilter o) {
        connectionInstanceID = connectionID++;
        filter = o;
        client = s;

        logger.info("Creating new OutputWorker (" + connectionInstanceID
                + ") for event " + o.crisisID);

        try {
            clientStream = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            logger.error("Could not get output stream for worker "
                    + connectionInstanceID);
        }
    }

    public void push(String doc) {
        if (!clientStream.checkError()) {
            logger.info("Writing to client: " + doc);
            clientStream.println(doc);
        }
        if (clientStream.checkError()) {
            // Inform subscribers that the connection has closed
            logger.info("Could not write to client, closing connection");
            close();
        }
    }

    boolean isClosed = false;

    public void close() {
        if (!client.isClosed())
            try {
                client.close();
            } catch (IOException e) {
                logger.error("IOException while closing client connection");
            }
        if (!isClosed)
            onConnectionClosed.fire(this, null);
        isClosed = true;
    }

    @Override
    protected void finalize() {
        close();
        onConnectionClosed.dispose();
    }

    private static int getContentSize(String input ) throws IOException {
        int size = -1;
        String contentLengthKeyWord = "Content-Length";
        int contentSizeStart = input.indexOf(contentLengthKeyWord) + ( contentLengthKeyWord.length() + 1);
        String fromContentSize = input.substring(contentSizeStart).trim();
        int contentSizeEnd = fromContentSize.indexOf("\r\n");
        String contentSize = fromContentSize.substring(0,contentSizeEnd);

        if(isNumericValue(contentSize)){
            size = Integer.parseInt(contentSize);
        }

        return size;
    }

    private static boolean isNumericValue(String value){
        try
        {
            Integer.parseInt(value);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;

    }
}
