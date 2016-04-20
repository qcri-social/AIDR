package qa.qcri.aidr.trainer.pybossa.util;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 2/12/14
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataFormatValidator {
	
	protected static Logger logger = Logger.getLogger(DataFormatValidator.class);
	
    public static boolean isValidateJson(String inputDataString)
    {
        boolean isVaildJsonObject = true;
        try{
            new JSONParser().parse(inputDataString);
        }
        catch(Exception e){
            isVaildJsonObject = false;
            logger.warn("Invalid JSON : " + inputDataString);
        }

        return isVaildJsonObject;
    }

    public static boolean isEmptyGeoJson(String jsonString){
        boolean isEmpty = false;
        //geometry
        JSONParser parser = new JSONParser();
        try{
            JSONObject geometry  = (JSONObject)parser.parse(jsonString) ;

            if(geometry.size() == 0){
                isEmpty = true;
            }

            JSONObject a = (JSONObject)geometry.get("geometry");

            if(a.size() == 0){
                isEmpty = true;
            }
        }
        catch(Exception e){
            isEmpty = true;
        }
        return isEmpty;
    }
}
