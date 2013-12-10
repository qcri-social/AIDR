package qa.qcri.aidr.trainer.pybossa.impl;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import qa.qcri.aidr.trainer.pybossa.entity.*;
import qa.qcri.aidr.trainer.pybossa.service.ReportTemplateService;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;
import qa.qcri.aidr.trainer.pybossa.util.JsonSorter;
import qa.qcri.aidr.trainer.pybossa.util.StreamConverter;

import java.io.InputStream;
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
public class MicroMapperPybossaFormatter {
    protected static Logger logger = Logger.getLogger("service");
    public MicroMapperPybossaFormatter(){}

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

    public List<String> assemblePybossaTaskPublishForm( List<MicromapperInput> inputSources, ClientApp clientApp) throws Exception {

        List<String> outputFormatData = new ArrayList<String>();

        Iterator itr= inputSources.iterator();

        while(itr.hasNext()){
            MicromapperInput micromapperInput = (MicromapperInput)itr.next();
            JSONObject info = assemblePybossaInfoFormat(micromapperInput, clientApp) ;

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


    public String updateApp(ClientApp clientApp,JSONObject attribute, JSONArray labelModel) throws Exception {
        InputStream templateIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/template.html");
        String templateString = StreamConverter.convertStreamToString(templateIS) ;

        templateString = templateString.replace("TEMPLATE:SHORTNAME", clientApp.getShortName());
       // templateString = templateString.replace("TEMPLATE:NAME", clientApp.getName());
        //TEMPLATEFORATTRIBUTEAIDR
        String attributeDisplay = (String)attribute.get("name") ;
        attributeDisplay =  attributeDisplay +" " + (String)attribute.get("description") ;
        templateString = templateString.replace("TEMPLATE:FORATTRIBUTEAIDR", attributeDisplay);


        JSONArray sortedLabelModel = JsonSorter.sortJsonByKey(labelModel, "norminalLabelCode");
        StringBuffer displayLabel = new StringBuffer();
        Iterator itr= sortedLabelModel.iterator();
       // logger.debug("sortedLabelModel : " + sortedLabelModel);
        while(itr.hasNext()){

            JSONObject featureJsonObj = (JSONObject)itr.next();
            String labelName = (String)featureJsonObj.get("name");
            String lableCode = (String)featureJsonObj.get("norminalLabelCode");
            String description = (String)featureJsonObj.get("description");
            Long norminalLabelID = (Long) featureJsonObj.get("norminalLabelID");

            displayLabel.append("<label class='checkbox' name='nominalLabel'><strong>")  ;
            displayLabel.append("<input name='nominalLabel' type='checkbox' value=");
            displayLabel.append(labelName) ;
            displayLabel.append(">") ;
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

       // logger.debug("displayLabel : " + displayLabel.toString());

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
        app2.put("category_id", null);
        app2.put("owner_id", 1);

        //long_description
        return  app2.toJSONString();

    }


    private JSONObject assemblePybossaInfoFormat(MicromapperInput micromapperInput, ClientApp clientApp) throws Exception{

        Integer n_answers = clientApp.getClient().getDefaultTaskRunsPerTask();

        if(clientApp != null){
            n_answers = clientApp.getTaskRunsPerTask();
        }

        JSONObject pybossaData = new JSONObject();
        pybossaData.put("question","please tag it.");

        if(clientApp.getShortName().toLowerCase().contains("geo")){
            pybossaData = createGeoClickerInfo(pybossaData, micromapperInput);
        }
        else{
            pybossaData = createNonGeoClickerInfo(pybossaData, micromapperInput);
        }

        pybossaData.put("n_answers",n_answers);

        return pybossaData;
    }


    private JSONObject createNonGeoClickerInfo(JSONObject pybossaData, MicromapperInput micromapperInput ){

        pybossaData.put("author",micromapperInput.getAuthor());
        pybossaData.put("tweetid",micromapperInput.getTweetID());
        pybossaData.put("userID",micromapperInput.getAuthor());
        pybossaData.put("tweet",micromapperInput.getTweet());
        pybossaData.put("timestamp",micromapperInput.getCreated());
        pybossaData.put("lat",micromapperInput.getLat());
        pybossaData.put("lon",micromapperInput.getLng());
        pybossaData.put("url",micromapperInput.getUrl());
        pybossaData.put("imgurl",micromapperInput.getUrl());

        return pybossaData;

    }


    private JSONObject createGeoClickerInfo(JSONObject pybossaData, MicromapperInput micromapperInput ){

        pybossaData.put("author",micromapperInput.getAuthor());
        pybossaData.put("tweetid",micromapperInput.getTweetID());
        pybossaData.put("userID",micromapperInput.getAuthor());
        pybossaData.put("tweet",micromapperInput.getTweet());
        pybossaData.put("timestamp",micromapperInput.getCreated());
        pybossaData.put("lat",micromapperInput.getLat());
        pybossaData.put("lon",micromapperInput.getLng());
        pybossaData.put("url",micromapperInput.getUrl());
        pybossaData.put("imgurl",micromapperInput.getUrl());
        pybossaData.put("category",micromapperInput.getUrl());

        return pybossaData;
    }


    public boolean isTaskStatusCompleted(String data) throws Exception{
        /// will do later for importing process
        boolean isCompleted = false;
        if(!data.isEmpty() && data.length() > 10){
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);
            JSONArray jsonObject = (JSONArray) obj;

            Iterator itr= jsonObject.iterator();

            while(itr.hasNext()){
                JSONObject featureJsonObj = (JSONObject)itr.next();
                logger.debug("featureJsonObj : " +  featureJsonObj);
                String status = (String)featureJsonObj.get("state") ;
                logger.debug("status : "  + status);
                if(status.equalsIgnoreCase("completed"))
                {
                    isCompleted = true;
                }
            }

        }
        return isCompleted;
    }

    public List<String> processPybossaCompletedTask(String data) throws Exception{
        /// will do later for importing process
        if(!data.isEmpty() && data.length() > 10){
            List<String> completedTaskID = new ArrayList<String>();
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);
            JSONArray jsonObject = (JSONArray) obj;
            Iterator itr= jsonObject.iterator();

            while(itr.hasNext()){
                JSONObject featureJsonObj = (JSONObject)itr.next();
                Long id = (Long)featureJsonObj.get("id") ;
                String taskID =  id.toString();
                System.out.println(taskID);
                completedTaskID.add(taskID);
            }

            return completedTaskID;  //To change body of implemented methods use File | Settings | File Templates.
        }

        return null;
    }


    public TaskQueueResponse getAnswerResponse(String pybossaResult, JSONParser parser, Long taskQueueID, ClientAppAnswer clientAppAnswer, ReportTemplateService rtpService) throws Exception{
        JSONObject responseJSON = new JSONObject();

        JSONArray questionArrary =   (JSONArray) parser.parse(clientAppAnswer.getAnswer()) ;
        int questionSize =  questionArrary.size();
        int[] responses = new int[questionSize];
        String[] questions = new String[questionSize];

        for(int i=0; i< questionSize; i++){
            JSONObject obj = (JSONObject)questionArrary.get(i);
            questions[i] =   (String)obj.get("qa");
        }

        JSONArray array = (JSONArray) parser.parse(pybossaResult) ;

        Iterator itr= array.iterator();
        String answer = null;
        boolean isGeoClicker = false;
        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();

            JSONObject info = (JSONObject)featureJsonObj.get("info");
            Long taskID = (Long) featureJsonObj.get("id");
             // tweet
            if(info.get("category")!=null) {
                answer = (String)info.get("category");
            }
            // image
            if(info.get("damage")!=null) {
                answer = (String)info.get("damage");
            }
            // geo
            if(info.get("loc")!=null) {
                answer = (String)info.get("loc");
                isGeoClicker = true;
            }

            if(answer!=null && !isGeoClicker){
                for(int i=0; i < questions.length; i++ ){
                    if(questions[i].trim().equalsIgnoreCase(answer.trim())){
                        responses[i] = responses[i] + 1;
                        handleItemAboveCutOff(taskQueueID,responses[i], answer, info, clientAppAnswer, rtpService);
                    }

                }
            }

        }

        String taskInfo = "";
        String responseJsonString = "";
        if(!isGeoClicker){
            for(int i=0; i < questions.length; i++ ){
                responseJSON.put(questions[i], responses[i]);
            }
            responseJsonString = responseJSON.toJSONString();
        }
        else{
            // geo , well, map needs to handle result based on raidus
            responseJsonString = "geo";
            taskInfo = pybossaResult;
        }

        TaskQueueResponse taskQueueResponse = new TaskQueueResponse(taskQueueID, responseJsonString, taskInfo);
        return  taskQueueResponse;
    }

    private void handleItemAboveCutOff(Long taskQueueID,int responseCount, String answer, JSONObject info, ClientAppAnswer clientAppAnswer, ReportTemplateService reportTemplateService){
        // MAKE SURE TO MODIFY TEMPLATE HTML  Standize OUTPUT FORMAT
        if(responseCount >= clientAppAnswer.getVoteCutOff()){
            String tweetID = (String)info.get("tweetid");
            String tweet = (String)info.get("tweet");
            String author= (String)info.get("author");
            String lat= (String)info.get("lat");
            String lng= (String)info.get("lon");
            String url= (String)info.get("url");
            String created = (String)info.get("timestamp");
            Long taskID = (Long)info.get("taskID");

            //MicromapperOuput output = new MicromapperOuput(tweetID,tweet ,author,lat,lng,url,created, answer);
            //Long taskQueueID, Long taskID, String tweetID,String tweet,String author,String lat,String lng,String url,String created,String answer
            ReportTemplate template = new ReportTemplate(taskQueueID,taskID,tweetID,tweet,author,lat,lng,url,created, answer, StatusCodeType.TEMPLATE_IS_READY_FOR_EXPORT, clientAppAnswer.getClientAppID());
            reportTemplateService.saveReportItem(template);
            // save to output
        }
    }

}
