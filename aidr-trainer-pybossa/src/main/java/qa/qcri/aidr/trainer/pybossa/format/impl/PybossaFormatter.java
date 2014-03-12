package qa.qcri.aidr.trainer.pybossa.format.impl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;
import qa.qcri.aidr.trainer.pybossa.entity.TaskLog;
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

    public String getTaskLogDateHistory(List<TaskLog> taskLogList, String pybossaResult, JSONParser parser) throws Exception{
        //JSONObject aModified = new JSONObject();
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

        Iterator itr= array.iterator();

        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();

            String taskPresented = (String)featureJsonObj.get("created");
          //  taskPresented = DateTimeConverter.utcToDefault(taskPresented);

            String taskCompleted = (String)featureJsonObj.get("finish_time");
          //  taskCompleted = DateTimeConverter.utcToDefault(taskCompleted);

            dateJSON.put("taskpresented",taskPresented) ;
            dateJSON.put("taskcompleted",taskCompleted) ;

            featureJsonObj.put("dateHistory",dateJSON) ;
        }

        return  array.toJSONString();
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
        app2.put("category_id", categoryID);
        app2.put("owner_id", 1);

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

    public Long getCategoryID(String data, JSONParser parser, boolean isJsonArray) throws Exception{
        Long categoryID = null;
        if(isJsonArray){
            JSONArray array = (JSONArray) parser.parse(data);
            Iterator itr= array.iterator();

            while(itr.hasNext()){
                JSONObject featureJsonObj = (JSONObject)itr.next();
                categoryID = (Long)featureJsonObj.get("id");
            }
        }
        else{
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            categoryID = (Long)jsonObject.get("id");
        }
        return categoryID;
    }

    public String getCatagoryDataSet(String attributeName,  String attributeCode){
        JSONObject categoryJSON = new JSONObject();
        categoryJSON.put("name", attributeName) ;
        categoryJSON.put("short_name", attributeCode) ;
        categoryJSON.put("description", attributeName);

        return categoryJSON.toJSONString();
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

    private void assembleToAIDRImportFormat(String data) throws Exception{
        System.out.println(data);
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(data);
        JSONArray jsonObject = (JSONArray) obj;
        Iterator itr= jsonObject.iterator();
        while(itr.hasNext()){
            JSONObject featureJsonObj = (JSONObject)itr.next();

            JSONObject info = (JSONObject) parser.parse((String)featureJsonObj.get("info"));

            String userName = (String)featureJsonObj.get("user_id");
            Long userID = (Long)featureJsonObj.get("user_id");
            String answer = (String)info.get("category");

            Long crisisID = (Long)info.get("crisisID");
            Long documentID = (Long)info.get("documentID");
            String attributeInfo =    (String)info.get("attributeInfo");

            //'[{"attributeID":15,"labelID":"56"},
            //{"attributeID":17,"labelID":"57"},
            //{"attributeID":18,"labelID":"58"},
            //{"attributeID":20,"labelID":"12"}
            //]'
            //outputFormatData.add(processPybossaOutputFormat(pybossaData));

            System.out.println(featureJsonObj.toString());
        }
    }

}
