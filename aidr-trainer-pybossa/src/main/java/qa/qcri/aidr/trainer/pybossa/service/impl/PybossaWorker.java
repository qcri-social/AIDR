package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.trainer.pybossa.entity.*;
import qa.qcri.aidr.trainer.pybossa.format.impl.TextClickerPybossaFormatter;
import qa.qcri.aidr.trainer.pybossa.service.*;
import qa.qcri.aidr.trainer.pybossa.store.LookupCode;
import qa.qcri.aidr.trainer.pybossa.store.URLPrefixCode;
import qa.qcri.aidr.trainer.pybossa.util.DataFormatValidator;
import qa.qcri.aidr.trainer.pybossa.util.DateTimeConverter;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("pybossaWorker")
@Transactional(readOnly = false)
public class PybossaWorker implements ClientAppRunWorker {

    protected static Logger logger = Logger.getLogger(PybossaWorker.class);
    
    @Autowired
    private ClientAppService clientAppService;

    @Autowired
    private TaskQueueService taskQueueService;

    @Autowired
    private TaskLogService taskLogService;

    @Autowired
    private ClientAppResponseService clientAppResponseService;

    @Autowired
    private ReportTemplateService reportTemplateService;


    private Client client;
    private int MAX_PENDING_QUEUE_SIZE = LookupCode.MAX_PENDING_QUEUE_SIZE;
    private String PYBOSSA_API_TASK_PUBLSIH_URL;
    private String AIDR_API_URL;
    private String AIDR_TASK_ANSWER_URL;
    private String AIDR_NOMINAL_ATTRIBUTE_LABEL_URL;
    private String PYBOSSA_API_TASK_RUN_BASE_URL;
    private String PYBOSSA_API_TASK_BASE_URL;
    private String AIDR_ASSIGNED_TASK_CLEAN_UP_URL;

    private String PYBOSSA_TASK_DELETE_URL;

    private PybossaCommunicator pybossaCommunicator = new PybossaCommunicator();
    private JSONParser parser = new JSONParser();
    private TextClickerPybossaFormatter pybossaFormatter = new TextClickerPybossaFormatter();

    @Autowired
    private TranslationService translationService;

    public void setClassVariable(Client theClient) throws Exception{
        boolean resetVariable = false;

        if(client != null){
            if(!client.getClientID().equals(theClient.getClientID())){
                client = theClient;
                resetVariable = true;
            }
        }
        else{
            client = theClient;
            resetVariable = true;
        }

        if(resetVariable){
            AIDR_API_URL =  client.getAidrHostURL() + URLPrefixCode.ASSINGN_TASK + LookupCode.SYSTEM_USER_NAME + "/";
            AIDR_ASSIGNED_TASK_CLEAN_UP_URL = client.getAidrHostURL()  + URLPrefixCode.AIDR_TASKASSIGNMENT_REVERT + LookupCode.SYSTEM_USER_NAME + "/";
            PYBOSSA_API_TASK_PUBLSIH_URL = client.getHostURL() + URLPrefixCode.TASK_PUBLISH + client.getHostAPIKey();
            AIDR_TASK_ANSWER_URL = client.getAidrHostURL() + URLPrefixCode.TASK_ANSWER_SAVE;
            PYBOSSA_API_TASK_BASE_URL  = client.getHostURL() + URLPrefixCode.TASK_INFO;
            PYBOSSA_API_TASK_RUN_BASE_URL =  client.getHostURL() + URLPrefixCode.TASKRUN_INFO;
            MAX_PENDING_QUEUE_SIZE = client.getQueueSize();

            PYBOSSA_TASK_DELETE_URL = client.getHostURL() + URLPrefixCode.PYBOSSA_TASK_DELETE;

            AIDR_NOMINAL_ATTRIBUTE_LABEL_URL = client.getAidrHostURL() + URLPrefixCode.AIDR_NOMINAL_ATTRIBUTE_LABEL;

        }

    }

    @Override
    public void processTaskRunImport() throws Exception{
        System.out.println("processTaskRunImport : Start : " + new Date());

        List<ClientApp> clientAppList = clientAppService.findClientAppByStatus(LookupCode.AIDR_ONLY);
        Iterator itr= clientAppList.iterator();

        pybossaFormatter.setTranslationService(translationService);

        while(itr.hasNext()){
            ClientApp clientApp = (ClientApp)itr.next();
            this.setClassVariable(clientApp.getClient());
            this.processTaskRunPerClientAppImport(clientApp);

            if(clientApp.getTcProjectId() != null ){
                this.processTranslations(clientApp);
            }
        }
    }

    @Override
    public void processTaskPublish() throws Exception{
        System.out.println("processTaskPublish : Start : " + new Date());
        List<ClientApp> appList = clientAppService.findClientAppByStatus(LookupCode.AIDR_ONLY)  ;

        if(appList != null) {
	        for (int index = 0; index < appList.size() ; index++){
	
	            this.setClassVariable(appList.get(index).getClient());
	
	            int pushTaskNumber = calculateMinNumber(appList.get(index));
	
	            if( pushTaskNumber > 0 ){
	
	                String api = AIDR_API_URL + appList.get(index).getCrisisID() + "/" + pushTaskNumber;
	
	                logger.warn("Send request :: " + api);
	                String inputData = pybossaCommunicator.sendGet(api);
	
	                logger.warn("Input data :: " + inputData);
	                if(DataFormatValidator.isValidateJson(inputData)){
	                    try {
	                        processNonGroupPushing(appList.get(index), inputData, pushTaskNumber)  ;
	
	                    } catch (Exception e) {
	                        logger.error("error in publishing data", e);
	                    }
	                }
	            }
	        }
        }


    }

    private void processTranslations(ClientApp clientApp) throws Exception {
        translationService.processTranslations(clientApp);
    }


    public void processNonGroupPushing(ClientApp currentClientApp, String inputData, int pushTaskNumber){
        try{
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(inputData);
            JSONArray jsonObject = (JSONArray) obj;
            int inputDataSize =  jsonObject.size();

            int itemsPerApp = inputDataSize;
            int itemIndexStart = 0;
            int itemIndexEnd = itemsPerApp;


            if(itemIndexEnd > inputDataSize){
                itemIndexEnd  = inputDataSize;
            }
            if((itemIndexStart < itemIndexEnd) && (itemIndexEnd <= inputDataSize)) {

                // if translate is required, let's go directly
                if(currentClientApp.getTcProjectId() != null){
                    pybossaFormatter.setTranslationService(translationService);
                    pybossaFormatter.publicTaskTranslationTaskPublishForm(inputData, currentClientApp, itemIndexStart, itemIndexEnd);
                }
                else{
                    List<String> aidr_data = pybossaFormatter.assemblePybossaTaskPublishFormWithIndex(inputData, currentClientApp, itemIndexStart, itemIndexEnd);
                    int itemLoopEnd = itemIndexEnd - itemIndexStart;
                    for(int i = 0; i < itemLoopEnd; i++){
                        String temp = aidr_data.get(i);
                        String response = pybossaCommunicator.sendPostGet(temp, PYBOSSA_API_TASK_PUBLSIH_URL) ;
                        if(!response.startsWith("Exception") && !response.contains("exception_cls")){
                            addToTaskQueue(response, currentClientApp.getClientAppID(), LookupCode.TASK_PUBLISHED) ;
                        }
                        else{
                            addToTaskQueue(temp, currentClientApp.getClientAppID(), LookupCode.Task_NOT_PUBLISHED) ;
                        }
                    }
                    itemIndexStart = itemIndexEnd;
                    itemIndexEnd = itemIndexEnd + itemsPerApp;
                    if(itemIndexEnd > inputDataSize && itemIndexStart < inputDataSize){
                        itemIndexEnd = itemIndexStart + (inputDataSize - itemIndexStart);
                    }
                }

            }

        }
        catch(Exception e){
            logger.error(e.getMessage());
        }

    }

    public void processTaskRunPerClientAppImport(ClientApp clientApp){
        //http://crowdcrafting.org/api/task?app_id=749&limit=10&state=completed
        //http://localhost:5000/api/task?app_id=176&id=6109
        List<TaskQueue> taskQueues =  taskQueueService.getTaskQueueByClientAppStatus(clientApp.getClientAppID(), LookupCode.TASK_PUBLISHED);

        if(taskQueues != null ){
            int max_loop_size = taskQueues.size();

            for(int i=0; i < max_loop_size; i++){
                TaskQueue taskQueue = taskQueues.get(i);
                //if(!this.isExpiredTaskQueue(taskQueue)){
                    Long taskID =  taskQueue.getTaskID();
                    //System.out.println("taskID :" + taskID);
                    String taskQueryURL = PYBOSSA_API_TASK_BASE_URL + clientApp.getPlatformAppID() + "&id=" + taskID;
                    String inputData = pybossaCommunicator.sendGet(taskQueryURL);

                    try {
                        boolean isFound = pybossaFormatter.isTaskStatusCompleted(inputData);

                        if(isFound){

                            this.processTaskQueueImport(clientApp, taskQueue, taskID);
                        }

                    } catch (Exception e) {
                        System.out.println("Error for processTaskRunPerClientAppImport: " + clientApp.getShortName());
                        System.out.println("Error for processTaskRunPerClientAppImport: taskID" + taskID);

                    }
               // }
            }
        }
     }

    private boolean isExpiredTaskQueue(TaskQueue taskQueue){
        boolean isExpired = false;
        try{
            long diffHours = DateTimeConverter.getHourDifference(taskQueue.getCreated(), null);
            if(diffHours >= LookupCode.TASK_CLEANUP_CUT_OFF_HOUR){
                String returnValue = this.removeAbandonedTask(taskQueue.getTaskID(), taskQueue.getTaskQueueID());
                isExpired = true;
                if(returnValue.equalsIgnoreCase("Exception")){
                    taskQueue.setStatus(LookupCode.TASK_ABANDONED);
                    taskQueueService.updateTaskQueue(taskQueue);
                }

            }
        }
        catch(Exception e){
            logger.error("isExpiredTaskQueue : " + e.getMessage());
        }
        return isExpired;
    }

    private void processTaskQueueImport(ClientApp clientApp, TaskQueue taskQueue, Long taskID) throws Exception {
        String PYBOSSA_API_TASK_RUN = PYBOSSA_API_TASK_RUN_BASE_URL + clientApp.getPlatformAppID() + "&task_id=" + taskID;
        String importResult = pybossaCommunicator.sendGet(PYBOSSA_API_TASK_RUN) ;

        JSONArray array = (JSONArray) parser.parse(importResult) ;

        ClientAppAnswer clientAppAnswer = clientAppResponseService.getClientAppAnswer(clientApp.getClientAppID());

        if(clientAppAnswer == null){
            int cutOffValue = LookupCode.MAX_VOTE_CUT_OFF_VALUE;
            String AIDR_NOMINAL_ATTRIBUTE_LABEL_URL_PER_APP = AIDR_NOMINAL_ATTRIBUTE_LABEL_URL + clientApp.getCrisisID() + "/" + clientApp.getNominalAttributeID();
            String answerSet = pybossaCommunicator.sendGet(AIDR_NOMINAL_ATTRIBUTE_LABEL_URL_PER_APP) ;

            if(clientApp.getTaskRunsPerTask() < LookupCode.MAX_VOTE_CUT_OFF_VALUE){
                cutOffValue = LookupCode.MIN_VOTE_CUT_OFF_VALUE;
            }

            clientAppResponseService.saveClientAppAnswer(clientApp.getClientAppID(), answerSet, cutOffValue);
            clientAppAnswer = clientAppResponseService.getClientAppAnswer(clientApp.getClientAppID());
        }

        String pybossaResult = importResult;

        if(DataFormatValidator.isValidateJson(importResult)){
            List<TaskLog> taskLogList = taskLogService.getTaskLog(taskQueue.getTaskQueueID());

            pybossaResult = pybossaFormatter.buildTaskOutputForAIDR(taskQueue.getTaskQueueID(), taskLogList, importResult, parser, clientApp, clientAppAnswer);

            int responseCode =  LookupCode.HTTP_OK;

            if(pybossaResult != null && !pybossaFormatter.getTranslateRequired()){
                responseCode = pybossaCommunicator.sendPost(pybossaResult, AIDR_TASK_ANSWER_URL);
            }

            if(responseCode == LookupCode.HTTP_OK ||responseCode == LookupCode.HTTP_OK_NO_CONTENT || pybossaFormatter.getTranslateRequired() ){
                //System.out.println("update taskQueue : " + responseCode);
                TaskQueueResponse taskQueueResponse = pybossaFormatter.getTaskQueueResponse(clientApp, importResult, parser, taskQueue.getTaskQueueID(), clientAppAnswer, reportTemplateService);

                if(pybossaFormatter.getTranslateRequired()){
                    pybossaFormatter.setTranslateRequired(false);
                    taskQueueResponse.setTaskInfo(pybossaResult);
                }
                taskQueue.setStatus(LookupCode.TASK_LIFECYCLE_COMPLETED);
                this.updateTaskQueue(taskQueue);
                clientAppResponseService.processTaskQueueResponse(taskQueueResponse);

            }

        }
    }

    private void addToTaskQueue(String inputData, Long clientAppID, Integer status){

        try {
                Object obj = parser.parse(inputData);

                JSONObject jsonObject = (JSONObject) obj;

                Long taskID  = (Long)jsonObject.get("id");
                JSONObject info = (JSONObject)jsonObject.get("info");
                Long documentID = (Long)info.get("documentID");

                if(status.equals(LookupCode.Task_NOT_PUBLISHED)){
                    pybossaCommunicator.sendGet(AIDR_ASSIGNED_TASK_CLEAN_UP_URL+ documentID) ;
                }
                else{
                    TaskQueue taskQueue = new TaskQueue(taskID, clientAppID, documentID, status);
                    taskQueueService.createTaskQueue(taskQueue);
                    TaskLog taskLog = new TaskLog(taskQueue.getTaskQueueID(), taskQueue.getStatus());
                    taskLogService.createTaskLog(taskLog);
                }

        } catch (ParseException e) {
            logger.error("Error parsing: " + inputData);
            logger.error(e.getMessage());
        }
    }

    private void updateTaskQueue(TaskQueue taskQueue){
        taskQueueService.updateTaskQueue(taskQueue);
        TaskLog taskLog = new TaskLog(taskQueue.getTaskQueueID(), taskQueue.getStatus());
        taskLogService.createTaskLog(taskLog);
    }


    private int calculateMinNumber(ClientApp obj){
        int min = MAX_PENDING_QUEUE_SIZE;

        if(obj.getTcProjectId()== null){
            int currentPendingTask =  taskQueueService.getCountTaskQeueByStatusAndClientApp(obj.getClientAppID(), LookupCode.AIDR_ONLY);
            int numQueue = MAX_PENDING_QUEUE_SIZE - currentPendingTask;
            if(numQueue < 0) {
                min =  0;
            }
            else{
                min =  numQueue;
            }
        }
        else{
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            if(dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.WEDNESDAY || dayOfWeek == Calendar.FRIDAY ){
                min = 1000 ;
                List<TaskTranslation> taskTranslations = translationService.findAllTranslationsByClientAppIdAndStatus(obj.getClientAppID(), TaskTranslation.STATUS_IN_PROGRESS, min);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                String rightNow = sdf.format(new Date());
                String recordDate = sdf.format(taskTranslations.get(0).getCreated())   ;
                if(rightNow.equalsIgnoreCase(recordDate)){
                    min = 0;
                }

            } else {
                min = 0;
            }
        }

        return min;
    }

    public String removeAbandonedTask(long taskID, long taskQueueID) throws Exception {
        String deleteTaskURL =  PYBOSSA_TASK_DELETE_URL + taskID + URLPrefixCode.PYBOSSA_APP_UPDATE_KEY + client.getHostAPIKey();
        //System.out.println(deleteTaskURL);
        String returnValue = pybossaCommunicator.deleteGet(deleteTaskURL);

        if(!returnValue.equalsIgnoreCase("Exception")) {
            taskLogService.deleteAbandonedTaskLog(taskQueueID);
            taskQueueService.deleteAbandonedTaskQueue(taskQueueID);
        }
        return returnValue;
    }

    private TaskQueue getTaskQueue(Long clientAppID, Long documentID){
        TaskQueue taskQueue = null;

        List<TaskQueue> queSet = taskQueueService.getTaskQueueByDocument(clientAppID, documentID);
        if(queSet!=null){
            if(queSet.size() > 0){
                taskQueue =  queSet.get(0);
            }
        }

        return taskQueue;
    }

}
