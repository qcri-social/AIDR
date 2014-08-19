package qa.qcri.aidr.trainer.api.util;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/28/14
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class GISUtil {
    //String urlBase = "http://nominatim.openstreetmap.org/search/";
    String urlBase = "http://scd1.qcri.org/nominatim/search/";
    String urlTail = "?format=json&addressdetails=1";

    // //http://scd1.qcri.org/nominatim/reverse?format=json&lat=16.63897422279177&lon=122.02808509999991&addressdetails=1
    String reverseURLBase = "http://scd1.qcri.org/nominatim/reverse?format=json&";
    String reverseURLTail = "&addressdetails=1&accept-language=en";
    JSONParser parser;
    Communicator communicator;
    
    private static Logger logger = Logger.getLogger(GISUtil.class);
    private static ErrorLog elog = new ErrorLog();
    
    public GISUtil(){
       this.parser = new JSONParser();
       this.communicator = new Communicator();
    }

    public String getDisplayName(String lat, String lon) throws Exception {
        String disName= null;
        if(!lat.isEmpty() && !lon.isEmpty()){
            String key = lat+"," + lon;
            String returnJson = communicator.sendGet(urlBase + key + urlTail);
            if(returnJson.trim().length() > 10){
                JSONArray jsonArray = (JSONArray) parser.parse(returnJson);

                Iterator itr= jsonArray.iterator();

                while(itr.hasNext()){

                    JSONObject featureJsonObj = (JSONObject)itr.next();
                    JSONObject info = (JSONObject)featureJsonObj.get("address");

                    String state = (String)info.get("state");
                    String city = (String)info.get("city");
                    String county = (String)info.get("county");
                    disName = (String)featureJsonObj.get("display_name");

                }

            }
        }
        return  disName;
    }

    public String getDisplayNameWithReverseLookUp(String key)  {
        String info= null;
        //lat=16.63897422279177&lon=122.0280850999999
        logger.info("key :" + key);
        if(!key.isEmpty() ){
            try{
                String returnJson = communicator.sendGet(this.reverseURLBase + key + this.reverseURLTail);
                logger.info("returnJson :" + returnJson);
                if(returnJson.trim().length() > 10){
                    JSONObject featureJsonObj = (JSONObject) parser.parse(returnJson);
                    //info =   (String)featureJsonObj.get("display_name");
                    logger.info("featureJsonObj : " + featureJsonObj.toJSONString());
                    JSONObject address =  (JSONObject) featureJsonObj.get("address");
                    info = (String) address.get("country");

                    if(address.get("state") != null){
                        info =  (String) address.get("state") + ", " +  info ;
                    }

                    logger.info("info : " + info);
                }
            }
            catch(Exception e){
                logger.error("getDisplayNameWithReverseLookUp exception for key: " + key);
                logger.error(elog.toStringException(e));
            }

        }
        return  info;
    }

}
