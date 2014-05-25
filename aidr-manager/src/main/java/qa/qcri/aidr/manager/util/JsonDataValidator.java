package qa.qcri.aidr.manager.util;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 5/12/14
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonDataValidator {

    public static boolean isValidJSON(String json) {
        boolean valid = false;
        try {
            final JsonParser parser = new ObjectMapper().getJsonFactory()
                    .createJsonParser(json);
            while (parser.nextToken() != null) {
                String fieldname = parser.getCurrentName();
                System.out.println("fieldname: " + fieldname);
            }
            valid = true;
        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return valid;
    }

    public static boolean isValidCeaJSON(String json) {
        boolean valid = false;
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;

            if(jsonObject.get("code") == null){
                return false;
            }

            if(jsonObject.get("geo")== null){
                return false;
            }

            if(jsonObject.get("durationInHours") == null ){
                return false;
            }

            if(jsonObject.get("account") == null ){
                return false;
            }

            if(jsonObject.get("shareWithAccounts") == null ){
                return false;
            }

            valid = true;

        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return valid;
    }
}
