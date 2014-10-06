import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Test class for data consumption over HTTP. Accepts a filter specification as
 * the first command line argument; either an interger (crisis ID) or a
 * json-formatted track filter (see communication standard for format).
 * 
 * @author jrogstadius
 */
public class HttpConsumerClientTest {

    static Socket socket;
    static BufferedReader serverIn;
    static PrintWriter serverOut;

     static String host = "localhost";
    static int port = 8766;//Config.HTTP_OUTPUT_PORT;
    static String filter = "{\"crisis_id\":37}";

    // static String filter =
    // "{\"crisis_id\":1, \"filters\": [ {\"type\":\"attribute\", \"attribute_id\":1 },{\"type\":\"attribute\", \"attribute_id\":2 },{\"type\":\"attribute\", \"attribute_id\":3 } ]}";

    public static void main(String[] args) throws UnknownHostException,
            IOException {
        if (args.length == 1) {
            int crisisID;
            try {
                crisisID = Integer.parseInt(args[0]);
                filter = "{\"crisis_id\":" + crisisID + "}";
            } catch (NumberFormatException ex) {
                filter = args[0];
            }
        }
        System.out.println("ClientTest");
        filter = "{\"crisis_id\":37}";
        listenSocket();
    }

    public static void listenSocket() throws UnknownHostException, IOException {
        // Create socket connection
        socket = new Socket(host, port);
        System.out.println("connected");
        serverOut = new PrintWriter(socket.getOutputStream(), true);
        serverIn = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        
        serverOut.println(filter);
        
        getServerOutput();

        socket.close();
    }

    public static void getUserInput() {

        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (!(line = scanner.nextLine()).equals("exit")) {
            serverOut.println(line);
        }

        scanner.close();
    }

    public static void getServerOutput() {

        String line;
        try {
            while ((line = serverIn.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
