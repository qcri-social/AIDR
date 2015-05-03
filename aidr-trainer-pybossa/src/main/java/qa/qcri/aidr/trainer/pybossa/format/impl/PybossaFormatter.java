package qa.qcri.aidr.trainer.pybossa.format.impl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import qa.qcri.aidr.trainer.pybossa.entity.*;
import qa.qcri.aidr.trainer.pybossa.service.ClientAppResponseService;
import qa.qcri.aidr.trainer.pybossa.service.ReportTemplateService;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;
import qa.qcri.aidr.trainer.pybossa.util.DataFormatValidator;
import qa.qcri.aidr.trainer.pybossa.util.DateTimeConverter;
import qa.qcri.aidr.trainer.pybossa.util.JsonSorter;
import qa.qcri.aidr.trainer.pybossa.util.StreamConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/17/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class PybossaFormatter {
   // protected static Logger logger = Logger.getLogger("service");
    public PybossaFormatter(){}

    public String assmeblePybossaAppCreationForm(String name, String shortName, String description) throws Exception{

        JSONObject app = new JSONObject();

        app.put("name", name);
        app.put("short_name", shortName);
        app.put("description", description);

        return app.toJSONString();
    }

    public Long getDocumentID(String jsonApp, JSONParser parser) throws Exception{
        Long documentID = null;
        JSONArray array = (JSONArray) parser.parse(jsonApp);
        Iterator itr= array.iterator();

        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();
            JSONObject info = (JSONObject)featureJsonObj.get("info");
            documentID = (Long)info.get("documentID");
        }

        return documentID;

    }

    public Long getAppID(String jsonApp, JSONParser parser) throws Exception{
        Long appID = null;
        JSONArray array = (JSONArray) parser.parse(jsonApp);
        Iterator itr= array.iterator();

        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();
            appID = (Long)featureJsonObj.get("id");
        }

        return appID;
    }

    public String getTaskLogDateHistory(List<TaskLog> taskLogList, String pybossaResult, JSONParser parser, ClientApp clientApp, ClientAppAnswer clientAppAnswer) throws Exception{
        //JSONObject aModified = new JSONObject();
        JSONArray outJson = new JSONArray();
        JSONObject dateJSON = new JSONObject();

        for(int i=0; i < taskLogList.size(); i++){
            TaskLog taskLog = taskLogList.get(i);
            switch (taskLog.getStatus()) {
                case 1: dateJSON.put("taskcreated", taskLog.getCreated().toString());
                        dateJSON.put("taskpulled", taskLog.getCreated().toString());
                    break;
            }
            dateJSON.put("taskpulled", DateTimeConverter.reformattedCurrentDate());
        }

        JSONArray array = (JSONArray) parser.parse(pybossaResult) ;

        if(array.size() > 0){
            JSONObject oneFeatureJsonObj = (JSONObject) array.get(0);

            String taskPresented = (String)oneFeatureJsonObj.get("created");

            String taskCompleted = (String)oneFeatureJsonObj.get("finish_time");

            dateJSON.put("taskpresented",taskPresented) ;
            dateJSON.put("taskcompleted",taskCompleted) ;


            oneFeatureJsonObj.put("dateHistory",dateJSON) ;

            String finalAnswer = this.getAnswerResponse(clientApp,pybossaResult,parser,clientAppAnswer);
            if(finalAnswer != null) {
                Long attributeID = clientApp.getNominalAttributeID();
                JSONObject infoJson =  this.buildInfoJson( (JSONObject)oneFeatureJsonObj.get("info"), finalAnswer, attributeID );

                oneFeatureJsonObj.put("info", infoJson);


                outJson.add(oneFeatureJsonObj);

                return outJson.toJSONString();
            }


        }

        return null  ;
    }


    ///////////////////////////////////////////////////////////////////////

    private JSONObject buildInfoJson(JSONObject infoJson,  String finalAnswer, Long attributeID){
        // TEMP UPDATE.
        JSONObject obj = new JSONObject();
        obj.put("documentID", infoJson.get("documentID"));
        obj.put("category", finalAnswer);
        obj.put("aidrID", infoJson.get("aidrID"));
        obj.put("crisisID", 314);
        obj.put("attributeID", attributeID);
        return obj;
    }

    public String getAnswerResponse(ClientApp clientApp, String pybossaResult, JSONParser parser, ClientAppAnswer clientAppAnswer) throws Exception{
        JSONObject responseJSON = new JSONObject();

        String[] questions = getQuestion( clientAppAnswer,  parser);
        int[] responses = new int[questions.length];

        JSONArray array = (JSONArray) parser.parse(pybossaResult) ;

        Iterator itr= array.iterator();
        String answer = null;

        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();
            answer = this.getUserAnswer(featureJsonObj, clientApp);
            for(int i=0; i < questions.length; i++ ){
                if(questions[i].trim().equalsIgnoreCase(answer.trim())){
                    responses[i] = responses[i] + 1;
                }
            }
        }

        String finalAnswer = null;

        int cutOffValue = this.getCutOffNumber(array.size(), clientApp.getTaskRunsPerTask(), clientAppAnswer);

        for(int i=0; i < questions.length; i++ ){
            if(responses[i] >= cutOffValue){
                finalAnswer =  questions[i];
            }
        }

        return  finalAnswer;
    }

    public TaskQueueResponse getTaskQueueResponse(ClientApp clientApp, String pybossaResult, JSONParser parser, Long taskQueueID, ClientAppAnswer clientAppAnswer, ReportTemplateService rtpService) throws Exception{
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
        boolean foundCutoffItem = false;
        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();

            JSONObject info = (JSONObject)featureJsonObj.get("info");

            Long taskID = (Long) featureJsonObj.get("id");

            answer = this.getUserAnswer(featureJsonObj, clientApp);

            if(answer!=null && !clientApp.getAppType().equals(StatusCodeType.APP_MAP) ){
                for(int i=0; i < questions.length; i++ ){
                    if(questions[i].trim().equalsIgnoreCase(answer.trim())){
                        responses[i] = responses[i] + 1;
                        foundCutoffItem = handleItemAboveCutOff(taskQueueID,responses[i], answer, info, clientAppAnswer, rtpService, cutOffSize);
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

    private boolean handleItemAboveCutOff(Long taskQueueID,int responseCount, String answer, JSONObject info, ClientAppAnswer clientAppAnswer, ReportTemplateService reportTemplateService, int cutOffSize){
        // MAKE SURE TO MODIFY TEMPLATE HTML  Standize OUTPUT FORMAT
        boolean processed = false;
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
                      ReportTemplate template = new ReportTemplate(taskQueueID,taskID,tweetID,tweet,author,lat,lng,url,created, answer, StatusCodeType.TEMPLATE_IS_READY_FOR_EXPORT, clientAppAnswer.getClientAppID());
                      reportTemplateService.saveReportItem(template);
                      processed = true;
                }
            }
        }
        catch(Exception e){
            System.out.println("handleItemAboveCutOff exception");
            System.out.println("exception e :" + e.toString());
        }
        return processed;
    }

    private String getUserAnswer(JSONObject featureJsonObj, ClientApp clientApp){
        String answer = null;
        JSONObject info = (JSONObject)featureJsonObj.get("info");

        if(info.get("category")!=null) {
            answer = (String)info.get("category");
        }

        return answer;
    }

    private String[] getQuestion(ClientAppAnswer clientAppAnswer, JSONParser parser) throws ParseException {
        String answerKey =   clientAppAnswer.getActiveAnswerKey();
        if(answerKey == null){
            answerKey = clientAppAnswer.getAnswer();
        }

        JSONArray questionArrary =   (JSONArray) parser.parse(answerKey) ;
        int questionSize =  questionArrary.size();
        String[] questions = new String[questionSize];

        for(int i=0; i< questionSize; i++){
            JSONObject obj = (JSONObject)questionArrary.get(i);
            questions[i] =   (String)obj.get("qa");
        }

        return questions;
    }


    //////////////////////////////////////////////////////////////////////////////////////////
    public int getCutOffNumber(int responseSize, int maxResponseSize, ClientAppAnswer clientAppAnswer){

        int cutOffSize =    clientAppAnswer.getVoteCutOff();

        if(responseSize > maxResponseSize){
            cutOffSize = (int)Math.round(maxResponseSize * 0.5);
        }

        return cutOffSize;
    }

    public List<String> assemblePybossaTaskPublishForm(String inputData, ClientApp clientApp) throws Exception {

        List<String> outputFormatData = new ArrayList<String>();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(inputData);

        JSONArray jsonObject = (JSONArray) obj;
        Iterator itr= jsonObject.iterator();

        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();
            JSONObject info = assemblePybossaInfoFormat(featureJsonObj, parser, clientApp) ;

            JSONObject tasks = new JSONObject();

            tasks.put("info", info);
            tasks.put("n_answers", clientApp.getTaskRunsPerTask());
            tasks.put("quorum", clientApp.getQuorum());
            tasks.put("calibration", new Integer(0));
            tasks.put("app_id", clientApp.getPlatformAppID());
            tasks.put("priority_0", new Integer(0));

            outputFormatData.add(tasks.toJSONString());

            //System.out.println(featureJsonObj.toString());
        }

        return outputFormatData;
    }

    public List<String> assemblePybossaTaskPublishFormWithIndex(String inputData, ClientApp clientApp, int indexStart, int indexEnd) throws Exception {

        List<String> outputFormatData = new ArrayList<String>();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(inputData);

        JSONArray jsonObject = (JSONArray) obj;
        Iterator itr= jsonObject.iterator();

        for(int i = indexStart; i < indexEnd; i++){
            JSONObject featureJsonObj = (JSONObject)jsonObject.get(i);
            JSONObject info = assemblePybossaInfoFormat(featureJsonObj, parser, clientApp) ;

            JSONObject tasks = new JSONObject();

            tasks.put("info", info);
            tasks.put("n_answers", clientApp.getTaskRunsPerTask());
            tasks.put("quorum", clientApp.getQuorum());
            tasks.put("calibration", new Integer(0));
            tasks.put("app_id", clientApp.getPlatformAppID());
            tasks.put("priority_0", new Integer(0));

            outputFormatData.add(tasks.toJSONString());

        }

        return outputFormatData;
    }


    public String updateApp(ClientApp clientApp,JSONObject attribute, JSONArray labelModel, Long categoryID) throws Exception {
        InputStream templateIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/template.html");
        String templateString = StreamConverter.convertStreamToString(templateIS) ;

        templateString = templateString.replace("TEMPLATE:SHORTNAME", clientApp.getShortName());
       // templateString = templateString.replace("TEMPLATE:NAME", clientApp.getName());
        //TEMPLATEFORATTRIBUTEAIDR
        String attributeDisplay = (String)attribute.get("name") ;
       // String attributeCode = (String)attribute.get("code");

        attributeDisplay =  attributeDisplay +" " + (String)attribute.get("description") ;
        templateString = templateString.replace("TEMPLATE:FORATTRIBUTEAIDR", attributeDisplay);


        JSONArray sortedLabelModel = JsonSorter.sortJsonByKey(labelModel, "norminalLabelCode");
        StringBuffer displayLabel = new StringBuffer();
        Iterator itr= sortedLabelModel.iterator();
        //logger.debug("sortedLabelModel : " + sortedLabelModel);
        while(itr.hasNext()){

            JSONObject featureJsonObj = (JSONObject)itr.next();
            String labelName = (String)featureJsonObj.get("name");
            String lableCode = (String)featureJsonObj.get("norminalLabelCode");
            String description = (String)featureJsonObj.get("description");
            Long norminalLabelID = (Long) featureJsonObj.get("norminalLabelID");

            displayLabel.append("<label class='radio' name='nominalLabel'><strong>")  ;
            displayLabel.append("<input name='nominalLabel' type='radio' value='");
            displayLabel.append(lableCode) ;
            displayLabel.append("'>") ;
            displayLabel.append(labelName) ;
            displayLabel.append("</strong>")  ;
            if(!description.isEmpty()){
                displayLabel.append("&nbsp;&nbsp;")  ;
                displayLabel.append("<font color='#999999' size=-1>")  ;
                displayLabel.append(description) ;
                displayLabel.append("</font>")  ;
            }
            displayLabel.append("</label>")  ;
        }

        //logger.debug("displayLabel : " + displayLabel.toString());

        templateString = templateString.replace("TEMPLATE:FORLABELSFROMAIDR", displayLabel.toString());

        InputStream tutorialIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/tutorial.html");
        String tutorialString = StreamConverter.convertStreamToString(tutorialIS) ;

        tutorialString = tutorialString.replace("TEMPLATE:SHORTNAME", clientApp.getShortName());
        tutorialString = tutorialString.replace("TEMPLATE:NAME", clientApp.getName());

        InputStream longDescIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/long_description.html");
        String longDescString = StreamConverter.convertStreamToString(longDescIS) ;

        JSONObject app = new JSONObject();

        app.put("task_presenter", templateString);

        app.put("tutorial", tutorialString);
        app.put("thumbnail", "http://i.imgur.com/lgZAWIc.png");

        JSONObject app2 = new JSONObject();
        app2.put("info", app );

        app2.put("long_description", longDescString);
        app2.put("name", clientApp.getName());
        app2.put("short_name", clientApp.getShortName());
        app2.put("description", clientApp.getShortName());
        app2.put("id", clientApp.getPlatformAppID());
        app2.put("time_limit", 0);
        app2.put("long_tasks", 0);
        app2.put("created", "" + new Date().toString()+"");
        app2.put("calibration_frac", 0);
        app2.put("bolt_course_id", 0);
        app2.put("link", "<link rel='self' title='app' href='http://localhost:5000/api/app/2'/>");
        app2.put("allow_anonymous_contributors", true);
        app2.put("time_estimate", 0);
        app2.put("hidden", 0);
       // app2.put("category_id", categoryID);
      //  app2.put("owner_id", 1);

        //long_description
        return  app2.toJSONString();

    }

    private JSONObject assemblePybossaInfoFormat(JSONObject featureJsonObj, JSONParser parser, ClientApp clientApp) throws Exception{

        String attributeInfo = (String)featureJsonObj.get("attributeInfo");
        JSONObject data = (JSONObject) parser.parse((String)featureJsonObj.get("data"));

        Long documentID =  (Long)featureJsonObj.get("documentID");
        Long crisisID =  (Long)featureJsonObj.get("crisisID");

        JSONObject usr =  (JSONObject)data.get("user");
        String userName = (String)usr.get("name");
        Long userID = (Long)usr.get("id");
        String tweetTxt = (String)data.get("text");
        String createdAt = (String)data.get("created_at");
        Long tweetID =  (Long)data.get("id");


        Integer n_answers = 1;
        if(clientApp != null){
            n_answers = clientApp.getTaskRunsPerTask();
        }


        JSONObject pybossaData = new JSONObject();
        pybossaData.put("question","please tag it.");
        pybossaData.put("userName",userName);
        pybossaData.put("tweetid",tweetID);
        pybossaData.put("userID",userID.toString());
        pybossaData.put("text",tweetTxt);
        pybossaData.put("createdAt",createdAt);
        pybossaData.put("n_answers",n_answers);
        //pybossaData.put("attributeInfo",attributeInfo);
        pybossaData.put("documentID",documentID);
        pybossaData.put("crisisID",crisisID);
        pybossaData.put("aidrID",clientApp.getClient().getAidrUserID());

        return pybossaData;
    }

    public boolean isTaskStatusCompleted(String data) throws Exception{
        /// will do later for importing process
        boolean isCompleted = false;
        if(DataFormatValidator.isValidateJson(data)){
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);
            JSONArray jsonObject = (JSONArray) obj;

            Iterator itr= jsonObject.iterator();

            while(itr.hasNext()){
                JSONObject featureJsonObj = (JSONObject)itr.next();
                //logger.debug("featureJsonObj : " +  featureJsonObj);
                String status = (String)featureJsonObj.get("state") ;
                //logger.debug("status : "  + status);
                if(status.equalsIgnoreCase("completed"))
                {
                    isCompleted = true;
                }
            }

        }
        return isCompleted;
    }

}
