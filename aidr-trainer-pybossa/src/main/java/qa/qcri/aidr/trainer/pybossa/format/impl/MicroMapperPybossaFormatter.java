package qa.qcri.aidr.trainer.pybossa.format.impl;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.util.StringUtils;
import qa.qcri.aidr.trainer.pybossa.entity.*;
import qa.qcri.aidr.trainer.pybossa.service.ReportTemplateService;
import qa.qcri.aidr.trainer.pybossa.store.PybossaConf;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;
import qa.qcri.aidr.trainer.pybossa.util.DataFormatValidator;
import qa.qcri.aidr.trainer.pybossa.util.JsonSorter;
import qa.qcri.aidr.trainer.pybossa.util.LatLngUtils;
import qa.qcri.aidr.trainer.pybossa.util.StreamConverter;

import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/17/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class MicroMapperPybossaFormatter {
    protected static Logger logger = Logger.getLogger(MicroMapperPybossaFormatter.class);

    public MicroMapperPybossaFormatter(){}

    public TaskQueueResponse getAnswerResponse(ClientApp clientApp, String pybossaResult, JSONParser parser, Long taskQueueID, ClientAppAnswer clientAppAnswer, ReportTemplateService rtpService) throws Exception{
        if(clientAppAnswer == null){
            return null;
        }

        JSONObject responseJSON = new JSONObject();

        String[] questions = getQuestion( clientAppAnswer,  parser);
        int[] responses = new int[questions.length];

        JSONArray array = (JSONArray) parser.parse(pybossaResult) ;

        int cutOffSize =  getCutOffNumber(array.size(),  clientApp.getTaskRunsPerTask(), clientAppAnswer) ;
        Iterator itr= array.iterator();
        String answer = null;

        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();

            JSONObject info = (JSONObject)featureJsonObj.get("info");

            Long taskID = (Long) featureJsonObj.get("id");

            answer = this.getUserAnswer(featureJsonObj, clientApp);

            if(answer!=null && !clientApp.getAppType().equals(StatusCodeType.APP_MAP) ){
                for(int i=0; i < questions.length; i++ ){
                    if(questions[i].trim().equalsIgnoreCase(answer.trim())){
                        responses[i] = responses[i] + 1;
                        handleItemAboveCutOff(taskQueueID,responses[i], answer, info, clientAppAnswer, rtpService, cutOffSize);
                    }

                }
            }


        }

        String taskInfo = "";
        String responseJsonString = "";
        //if(!clientApp.getAppType().equals(StatusCodeType.APP_MAP)){
        for(int i=0; i < questions.length; i++ ){
            responseJSON.put(questions[i], responses[i]);
        }
        responseJsonString = responseJSON.toJSONString();
        //}

        TaskQueueResponse taskQueueResponse = new TaskQueueResponse(taskQueueID, responseJsonString, taskInfo);
        return  taskQueueResponse;
    }

    private int getCutOffNumber(int responseSize, int maxResponseSize, ClientAppAnswer clientAppAnswer){

        int cutOffSize =    clientAppAnswer.getVoteCutOff();

        if(responseSize > maxResponseSize){
            cutOffSize = (int)Math.round(maxResponseSize * 0.5);
        }

        return cutOffSize;
    }

    private String getUserAnswer(JSONObject featureJsonObj, ClientApp clientApp){

        String answer = null;
        JSONObject info = (JSONObject)featureJsonObj.get("info");
        // tweet
        if(info.get("category")!=null) {
            answer = (String)info.get("category");
        }
        // image , video
        if(info.get("damage")!=null && clientApp.getAppType() == StatusCodeType.APP_IMAGE) {
            answer = (String)info.get("damage");
        }



        // geo
        if(info.get("loc")!=null) {
            answer = (String)info.get("loc");
        }

        return answer;
    }

    private String[] getQuestion(ClientAppAnswer clientAppAnswer, JSONParser parser) throws ParseException {
        JSONArray questionArrary =   (JSONArray) parser.parse(clientAppAnswer.getAnswer()) ;
        int questionSize =  questionArrary.size();
        String[] questions = new String[questionSize];

        for(int i=0; i< questionSize; i++){
            JSONObject obj = (JSONObject)questionArrary.get(i);
            questions[i] =   (String)obj.get("qa");
        }

        return questions;
    }

    private void handleItemAboveCutOff(Long taskQueueID,int responseCount, String answer, JSONObject info, ClientAppAnswer clientAppAnswer, ReportTemplateService reportTemplateService, int cutOffSize){
        // MAKE SURE TO MODIFY TEMPLATE HTML  Standize OUTPUT FORMAT
        try{

            String tweetID ;
            String tweet;
            String author= "";
            String lat= "";
            String lng= "";
            String url= "";
            String created = "";
            Long taskID = taskQueueID;

            if(responseCount >= cutOffSize){

                Long tid = (Long)info.get("tweetid");
                tweetID = tid + "";
                if(info.get("tweet") == null){

                    tweet = (String)info.get("text");
                    author= "";
                    lat= "";
                    lng= "";
                    url= "";
                    created = "";
                }
                else{

                    tweet = (String)info.get("tweet");

                    if(info.get("author") != null){
                        author= (String)info.get("author");
                    }

                    if(info.get("lat") != null){
                        lat= (String)info.get("lat");
                    }

                    if(info.get("lon") != null){
                        lng= (String)info.get("lon");
                    }

                    if(info.get("url") != null){
                        url= (String)info.get("url");
                    }

                    created = (String)info.get("timestamp");
                    taskID = (Long)info.get("taskid");
                }

                if(taskQueueID!=null && taskID!=null && tweetID!=null && (tweet!=null && !tweet.isEmpty())){
                    if(!answer.equals("05_not_relevant") && !answer.equals("null") ) {
                        ReportTemplate template = new ReportTemplate(taskQueueID,taskID,tweetID,tweet,author,lat,lng,url,created, answer, StatusCodeType.TEMPLATE_IS_READY_FOR_EXPORT, clientAppAnswer.getClientAppID());
                        reportTemplateService.saveReportItem(template);
                    }
                }
            }
        }
        catch(Exception e){
             System.out.println("handleItemAboveCutOff exception");
             System.out.println("exception e :" + e.toString());
        }

    }


}
