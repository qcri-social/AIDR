package qa.qcri.aidr.gis.util;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/22/13
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamConverter {

    public static String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }

            return writer.toString();
        } else {
            return "";
        }
    }
}

