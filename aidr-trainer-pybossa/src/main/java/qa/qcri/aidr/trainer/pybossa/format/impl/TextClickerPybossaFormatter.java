package qa.qcri.aidr.trainer.pybossa.format.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import qa.qcri.aidr.trainer.pybossa.entity.*;
import qa.qcri.aidr.trainer.pybossa.service.ReportTemplateService;
import qa.qcri.aidr.trainer.pybossa.service.TranslationService;
import qa.qcri.aidr.trainer.pybossa.store.LookupCode;
import qa.qcri.aidr.trainer.pybossa.util.DataFormatValidator;
import qa.qcri.aidr.trainer.pybossa.util.JsonSorter;
import qa.qcri.aidr.trainer.pybossa.util.StreamConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/17/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextClickerPybossaFormatter {

    private boolean translateRequired = false;

    @Autowired
    TranslationService translationService;

    public TextClickerPybossaFormatter(){}

    public boolean getTranslateRequired() {
        return translateRequired;
    }

    public void setTranslateRequired(boolean translateRequired) {
        this.translateRequired = translateRequired;
    }

    public String assmeblePybossaAppCreationForm(String name, String shortName, String description) throws Exception{

        JSONObject app = new JSONObject();

        app.put("name", name);
        app.put("short_name", shortName);
        app.put("description", description);

        return app.toJSONString();
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

    public String buildTaskOutputForAIDR(Long taskQueueId, List<TaskLog> taskLogList, String pybossaResult, JSONParser parser, ClientApp clientApp, ClientAppAnswer clientAppAnswer) throws Exception{

        JSONArray outJson = new JSONArray();

        JSONArray array = (JSONArray) parser.parse(pybossaResult) ;

        if(array.size() > 0){
            JSONObject oneFeatureJsonObj = (JSONObject) array.get(0);
            String finalAnswer = this.getAnswerResponse(clientApp,pybossaResult,parser,clientAppAnswer, taskQueueId);

            System.out.println("finalAnswer : " + finalAnswer);

            if(finalAnswer != null) {
                JSONObject infoJson =  this.buildInfoJson( (JSONObject)oneFeatureJsonObj.get("info"), finalAnswer, clientApp );

                oneFeatureJsonObj.put("info", infoJson);


                outJson.add(oneFeatureJsonObj);

                return outJson.toJSONString();
            }


        }

        return null  ;
    }


    ///////////////////////////////////////////////////////////////////////

    private JSONObject buildInfoJson(JSONObject infoJson,  String finalAnswer, ClientApp clientApp){

        JSONObject obj = new JSONObject();
        obj.put("documentID", infoJson.get("documentID"));
        obj.put("category", finalAnswer);
        obj.put("aidrID", infoJson.get("aidrID"));
        obj.put("crisisID", clientApp.getCrisisID());
        obj.put("attributeID", clientApp.getNominalAttributeID());

        return obj;
    }

    public String getAnswerResponse(ClientApp clientApp, String pybossaResult, JSONParser parser, ClientAppAnswer clientAppAnswer, Long taskQueueID) throws Exception{

        String[] questions = getQuestion( clientAppAnswer,  parser);
        int[] responses = new int[questions.length];

        JSONArray array = (JSONArray) parser.parse(pybossaResult) ;

        Iterator itr= array.iterator();

        String answer = null;
        JSONObject finalInfo = null;

        int cutoffSize = this.getCutOffNumber(array.size(), clientApp.getTaskRunsPerTask(), clientAppAnswer)  ;

        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();
            JSONObject info = (JSONObject)featureJsonObj.get("info");
            finalInfo = info;

            answer = this.getUserAnswer(featureJsonObj);

            if(answer != null){
                for(int i=0; i < questions.length; i++ ){
                    if(questions[i].trim().equalsIgnoreCase(answer.trim())){
                        responses[i] = responses[i] + 1;
                    }
                }
            }

        }


        String finalAnswer = null;

        for(int i=0; i < questions.length; i++ ){
            if(responses[i] >= cutoffSize){
                finalAnswer =  questions[i];
                if(finalAnswer.equalsIgnoreCase(LookupCode.ANSWER_NOT_ENGLISH)){
                    handleTranslationItem(taskQueueID, answer, finalInfo, clientAppAnswer, cutoffSize);
                }
            }
        }

        return  finalAnswer;
    }

    private void handleTranslationItem(Long taskQueueID,String answer, JSONObject info, ClientAppAnswer clientAppAnswer, int cutOffSize){

        try{
            String tweetID = String.valueOf(info.get("tweetid"));
            String tweet = (String)info.get("tweet");
            if(tweet == null){
                tweet = (String)info.get("text");
            }
            String author= (String)info.get("author");
            String lat= (String)info.get("lat");
            String lng= (String)info.get("lon");
            String url= (String)info.get("url");
            String created = (String)info.get("timestamp");

            Long taskID;
            if(info.get("taskid") == null){
                taskID =  taskQueueID;
            }
            else{
                taskID = (Long)info.get("taskid");
            }


            if(taskQueueID!=null && taskID!=null && tweetID!=null && (tweet!=null && !tweet.isEmpty())) {
                System.out.println("handleTranslationItem :" + taskQueueID);
                this.setTranslateRequired(true);
                createTaskTranslation(taskID, tweetID, tweet, author, lat, lng, url, taskQueueID, created, clientAppAnswer);

            }
        }
        catch(Exception e){
            System.out.println("handleTranslationItem- exception :" + e.getMessage());
            this.setTranslateRequired(false);
        }

    }

    private void createTaskTranslation(Long taskID, String tweetID, String tweet, String author, String lat, String lon, String url, Long taskQueueID, String created, ClientAppAnswer clientAppAnswer){

        TaskTranslation extTrans = translationService.findByTaskId(taskID);
        if (extTrans != null ) {
            return;
        }

        System.out.println("createTaskTranslation is called : " + taskQueueID);

        TaskTranslation translation = new TaskTranslation(taskID, clientAppAnswer.getClientAppID(), tweetID, author, lat, lon, url, taskQueueID, tweet, TaskTranslation.STATUS_NEW);
        translationService.createTranslation(translation);


    }

    public TaskQueueResponse getTaskQueueResponse(ClientApp clientApp, String pybossaResult, JSONParser parser, Long taskQueueID, ClientAppAnswer clientAppAnswer, ReportTemplateService rtpService) throws Exception{
        System.out.println(" getTaskQueueResponse : taskQueueID " +  taskQueueID);
        if(clientAppAnswer == null){
            return null;
        }

        JSONObject responseJSON = new JSONObject();


        String[] correctAnswers = getQuestion(clientAppAnswer, parser);

        String[] activeAnswers = this.getActiveAnswerKey( clientAppAnswer,  parser);
        int[] responses = new int[correctAnswers.length];
        JSONArray array = (JSONArray) parser.parse(pybossaResult) ;

        int cutOffSize =  getCutOffNumber(array.size(),  clientApp.getTaskRunsPerTask(), clientAppAnswer) ;

        Iterator itr= array.iterator();
        String answer = null;
        boolean foundCutoffItem = false;
        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();

            JSONObject info = (JSONObject)featureJsonObj.get("info");

            Long taskID = (Long) featureJsonObj.get("id");

            answer = this.getUserAnswer(featureJsonObj);

            System.out.println("answer :" + answer);

            if(answer!=null && !clientApp.getAppType().equals(LookupCode.APP_MAP) ){
                for(int i=0; i < correctAnswers.length; i++ ){
                    if(correctAnswers[i].trim().equalsIgnoreCase(answer.trim())){
                        responses[i] = responses[i] + 1;
                        foundCutoffItem = handleItemAboveCutOff(taskQueueID,responses[i], answer, info, clientAppAnswer, rtpService, cutOffSize, activeAnswers);
                    }
                }
            }


        }

        String taskInfo = "";
        String responseJsonString = "";

        for(int i=0; i < correctAnswers.length; i++ ){
            responseJSON.put(correctAnswers[i], responses[i]);
        }
        responseJsonString = responseJSON.toJSONString();


        TaskQueueResponse taskQueueResponse = new TaskQueueResponse(taskQueueID, responseJsonString, taskInfo);
        return  taskQueueResponse;
    }

    private boolean handleItemAboveCutOff(Long taskQueueID,int responseCount, String answer, JSONObject info, ClientAppAnswer clientAppAnswer, ReportTemplateService reportTemplateService, int cutOffSize, String[] activeAnswers){
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

                for(int a=0; a < activeAnswers.length; a++){
                    if(activeAnswers[a].equalsIgnoreCase(answer)){
                        if(taskQueueID!=null && taskID!=null && tweetID!=null && (tweet!=null && !tweet.isEmpty())){
                            ReportTemplate template = new ReportTemplate(taskQueueID,taskID,tweetID,tweet,author,lat,lng,url,created, answer, LookupCode.TEMPLATE_IS_READY_FOR_EXPORT, clientAppAnswer.getClientAppID());
                            reportTemplateService.saveReportItem(template);
                            processed = true;
                        }
                    }
                }

            }
        }
        catch(Exception e){
            System.out.println("handleItemAboveCutOff exception");
            System.out.println("exception e :" + e.toString());
        }
        return processed;
    }

    private String getUserAnswer(JSONObject featureJsonObj){
        String answer = null;
        JSONObject info = (JSONObject)featureJsonObj.get("info");

        if(info.get("category")!=null) {
            answer = (String)info.get("category");
        }

        return answer;
    }

    private String[] getQuestion(ClientAppAnswer clientAppAnswer, JSONParser parser) throws ParseException {
        String answerKey =   clientAppAnswer.getAnswer();
        System.out.println("getQuestion : " + answerKey);
        JSONArray questionArrary =   (JSONArray) parser.parse(answerKey) ;
        int questionSize =  questionArrary.size();
        String[] questions = new String[questionSize];

        for(int i=0; i< questionSize; i++){
            JSONObject obj = (JSONObject)questionArrary.get(i);
            questions[i] =   (String)obj.get("qa");
        }

        return questions;
    }

    private String[] getActiveAnswerKey(ClientAppAnswer clientAppAnswer, JSONParser parser) throws ParseException {

        String answerKey =   clientAppAnswer.getActiveAnswerKey();
        System.out.println("getActiveAnswerKey : " + answerKey);
        if(answerKey== null){
            answerKey =   clientAppAnswer.getAnswer();
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

    public int getCutOffNumber(int responseSize, int maxResponseSize, ClientAppAnswer clientAppAnswer){

        int cutOffSize =    clientAppAnswer.getVoteCutOff();

        if(responseSize > maxResponseSize){
            cutOffSize = (int)Math.round(maxResponseSize * 0.5);
        }

        return cutOffSize;
    }

    //////////////////////////////////////////////////////////////////////////////////////////

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
            tasks.put("project_id", clientApp.getPlatformAppID());
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

        for(int i = indexStart; i < indexEnd; i++){
            JSONObject featureJsonObj = (JSONObject)jsonObject.get(i);
            JSONObject info = assemblePybossaInfoFormat(featureJsonObj, parser, clientApp) ;

            JSONObject tasks = new JSONObject();

            tasks.put("info", info);
            tasks.put("n_answers", clientApp.getTaskRunsPerTask());
            tasks.put("quorum", clientApp.getQuorum());
            tasks.put("calibration", new Integer(0));
            tasks.put("project_id", clientApp.getPlatformAppID());
            tasks.put("priority_0", new Integer(0));

            outputFormatData.add(tasks.toJSONString());

        }

        return outputFormatData;
    }

    public void publicTaskTranslationTaskPublishForm(String inputData, ClientApp clientApp, int indexStart, int indexEnd) throws Exception{
        try{
            List<TaskTranslation> outputFormatData = new ArrayList<TaskTranslation>();
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(inputData);

            JSONArray jsonObject = (JSONArray) obj;

            for(int i = indexStart; i < indexEnd; i++){
                JSONObject featureJsonObj = (JSONObject)jsonObject.get(i);
                JSONObject data = (JSONObject) parser.parse((String) featureJsonObj.get("data"));

                Long documentID =  (Long)featureJsonObj.get("documentID");
                JSONObject usr =  (JSONObject)data.get("user");
                String userName = (String)usr.get("name");
                String tweetTxt = (String)data.get("text");
                String tweetID =  String.valueOf(data.get("id"));

                TaskTranslation task = new TaskTranslation(clientApp.getClientAppID(),documentID, tweetID, userName, tweetTxt, TaskTranslation.STATUS_NEW);
                //outputFormatData.add(task);
                translationService.createTranslation(task);

            }
        }
        catch(Exception e){
            System.out.println("publicTaskTranslationTaskPublishForm : " + e);
        }


       // return outputFormatData;
    }

    public String generateClientAppTemplateLabel(JSONArray labelModel){
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

        return displayLabel.toString();

    }

    public String updateApp(ClientApp clientApp,JSONObject attribute, JSONArray labelModel, Long categoryID) throws Exception {
        InputStream templateIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/template.html");
        String templateString = StreamConverter.convertStreamToString(templateIS) ;

        templateString = templateString.replace("TEMPLATE:SHORTNAME", clientApp.getShortName());

        String attributeDisplay = (String)attribute.get("name") ;

        attributeDisplay =  attributeDisplay +" " + (String)attribute.get("description") ;
        templateString = templateString.replace("TEMPLATE:FORATTRIBUTEAIDR", attributeDisplay);

        templateString = templateString.replace("TEMPLATE:FORLABELSFROMAIDR", this.generateClientAppTemplateLabel(labelModel) );

        InputStream tutorialIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/tutorial.html");
        String tutorialString = StreamConverter.convertStreamToString(tutorialIS) ;

        tutorialString = tutorialString.replace("TEMPLATE:SHORTNAME", clientApp.getShortName());
        tutorialString = tutorialString.replace("TEMPLATE:NAME", clientApp.getName());

        InputStream longDescIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/long_description.html");
        String longDescString = StreamConverter.convertStreamToString(longDescIS) ;

        JSONObject appInfo = new JSONObject();

        appInfo.put("task_presenter", templateString);

        appInfo.put("tutorial", tutorialString);
        appInfo.put("thumbnail", "http://i.imgur.com/lgZAWIc.png");

        JSONObject app = new JSONObject();
        app.put("info", appInfo );

        app.put("long_description", longDescString);
        app.put("name", clientApp.getName());
        app.put("short_name", clientApp.getShortName());
        app.put("description", clientApp.getShortName());
        app.put("allow_anonymous_contributors", true);
        app.put("hidden", 0);
        app.put("category_id", categoryID);
        app.put("featured", false);

        return  app.toJSONString();

    }

    private JSONObject assemblePybossaInfoFormat(JSONObject featureJsonObj, JSONParser parser, ClientApp clientApp) throws Exception{

        JSONObject data = (JSONObject) parser.parse((String) featureJsonObj.get("data"));

        Long documentID =  (Long)featureJsonObj.get("documentID");
        Long crisisID =   clientApp.getCrisisID();
        JSONObject usr =  (JSONObject)data.get("user");
        String userName = (String)usr.get("name");
        Long userID = (Long)usr.get("id");
        String tweetTxt = (String)data.get("text");
        String createdAt = (String)data.get("created_at");
        Long tweetID =  (Long)data.get("id");

        JSONObject pybossaData = new JSONObject();
        pybossaData.put("question","please tag it.");
        pybossaData.put("userName",userName);
        pybossaData.put("tweetid",tweetID);
        pybossaData.put("userID",userID.toString());
        pybossaData.put("text",tweetTxt);
        pybossaData.put("createdAt",createdAt);
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

    public void setTranslationService(TranslationService service) {
        translationService = service;
    }

}
