package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.pybossa.entity.Client;
import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;
import qa.qcri.aidr.trainer.pybossa.entity.TaskLog;
import qa.qcri.aidr.trainer.pybossa.entity.TaskQueue;
import qa.qcri.aidr.trainer.pybossa.format.impl.PybossaFormatter;
import qa.qcri.aidr.trainer.pybossa.service.*;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;
import qa.qcri.aidr.trainer.pybossa.store.URLPrefixCode;
import qa.qcri.aidr.trainer.pybossa.store.UserAccount;
import qa.qcri.aidr.trainer.pybossa.util.DataFormatValidator;
import qa.qcri.aidr.trainer.pybossa.util.DateTimeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service("pybossaWorker")
@Transactional(readOnly = false)
public class PybossaWorker implements ClientAppRunWorker {

    protected static Logger logger = Logger.getLogger("service");

    @Autowired
    private ClientAppService clientAppService;

    @Autowired
    private TaskQueueService taskQueueService;

    @Autowired
    private TaskLogService taskLogService;

    private Client client;
    private int MAX_PENDING_QUEUE_SIZE = StatusCodeType.MAX_PENDING_QUEUE_SIZE;
    private String PYBOSSA_API_TASK_PUBLSIH_URL;
    private String AIDR_API_URL;
    private String AIDR_TASK_ANSWER_URL;
    private String PYBOSSA_API_TASK_RUN_BASE_URL;
    private String PYBOSSA_API_TASK_BASE_URL;
    private String AIDR_ASSIGNED_TASK_CLEAN_UP;
    private String PYBOSSA_API_APP_DELETE_URL;
    private String PYBOSSA_TASK_DELETE_URL;

    private PybossaCommunicator pybossaCommunicator = new PybossaCommunicator();
    private JSONParser parser = new JSONParser();
    private PybossaFormatter pybossaFormatter = new PybossaFormatter();


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
            AIDR_API_URL =  client.getAidrHostURL() + URLPrefixCode.ASSINGN_TASK + UserAccount.SYSTEM_USER_NAME + "/";
            PYBOSSA_API_TASK_PUBLSIH_URL = client.getHostURL() + URLPrefixCode.TASK_PUBLISH + client.getHostAPIKey();
            AIDR_TASK_ANSWER_URL = client.getAidrHostURL() + URLPrefixCode.TASK_ANSWER_SAVE;
            PYBOSSA_API_TASK_BASE_URL  = client.getHostURL() + URLPrefixCode.TASK_INFO;
            PYBOSSA_API_TASK_RUN_BASE_URL =  client.getHostURL() + URLPrefixCode.TASKRUN_INFO;
            MAX_PENDING_QUEUE_SIZE = client.getQueueSize();
            ///AIDR_ASSIGNED_TASK_CLEAN_UP = client.getAidrHostURL() + URLPrefixCode.ASSIGN_TASK_CLEANUP;
            PYBOSSA_API_APP_DELETE_URL = client.getHostURL() + URLPrefixCode.PYBOSAA_APP ;
            PYBOSSA_TASK_DELETE_URL = client.getHostURL() + URLPrefixCode.PYBOSSA_TASK_DELETE;
        }

    }

    @Override
    public void processTaskRunImport() throws Exception{


        List<ClientApp> clientAppList = clientAppService.findClientAppByStatus(StatusCodeType.AIDR_ONLY);
        Iterator itr= clientAppList.iterator();

        while(itr.hasNext()){
            ClientApp clientApp = (ClientApp)itr.next();
            setClassVariable(clientApp.getClient());
            processTaskRunPerClientAppImport(clientApp);
        }

    }

    @Override
    public void processTaskPublish() throws Exception{
       // setClassVariable();

        // do some clean up
        List<ClientApp> crisisID = clientAppService.getAllCrisis();

        for (int i = 0; i < crisisID.size(); i++) {
            Object obj = crisisID.get(i);
            Long id = (Long)obj;
            if(id!=null){
                deactivateClientApp(id);
                List<ClientApp> appList = clientAppService.getAllClientAppByCrisisIDAndStatus(id , StatusCodeType.AIDR_ONLY);
               // System.out.println("processTaskPublish : crisisID - " + id);
               // System.out.println("processTaskPublish : appList - " + appList.size());
                if(appList.size() > 0){
                    setClassVariable(appList.get(0).getClient());
                    int pushTaskNumber = calculateMinNumber(appList);
                    //System.out.println("processTaskPublish : pushTaskNumber - " + pushTaskNumber);
                    if( pushTaskNumber > 0 ){
                        String inputData = pybossaCommunicator.sendGet(AIDR_API_URL + id + "/" +pushTaskNumber);
                        //System.out.println(AIDR_API_URL + id + "/" +pushTaskNumber);
                        //System.out.println("inputData  " + inputData);
                        if(DataFormatValidator.isValidateJson(inputData)){
                            try {
                                for (int index = 0; index < appList.size() ; index++){
                                    ClientApp currentClientApp = appList.get(index);
                                        List<String> aidr_data = pybossaFormatter.assemblePybossaTaskPublishForm(inputData, currentClientApp);
                                        for (String temp : aidr_data) {
                                           // System.out.println("*************************************************");
                                           // System.out.println(temp);
                                            String response = pybossaCommunicator.sendPostGet(temp, PYBOSSA_API_TASK_PUBLSIH_URL) ;
                                           // System.out.println(PYBOSSA_API_TASK_PUBLSIH_URL + "\n");
                                           // System.out.println(response + "\n");
                                            if(!response.startsWith("Exception") && !response.contains("exception_cls")){
                                                addToTaskQueue(response, currentClientApp.getClientAppID(), StatusCodeType.TASK_PUBLISHED) ;
                                            }
                                            else{
                                                addToTaskQueue(temp, currentClientApp.getClientAppID(), StatusCodeType.Task_NOT_PUBLISHED) ;
                                            }
                                            //System.out.println("*************************************************");

                                        }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                    }
                }
                appList.clear();
            }

        }
    }

    public void processTaskRunPerClientAppImport(ClientApp clientApp){
        //http://crowdcrafting.org/api/task?app_id=749&limit=10&state=completed
        //http://localhost:5000/api/task?app_id=176&id=6109
        List<TaskQueue> taskQueues =  taskQueueService.getTaskQueueByClientAppStatus(clientApp.getClientAppID(),StatusCodeType.TASK_PUBLISHED);
        if(taskQueues != null ){
            for(int i=0; i < taskQueues.size(); i++){
                TaskQueue taskQueue = taskQueues.get(i);
                Long taskID =  taskQueue.getTaskID();
                String taskQueryURL = PYBOSSA_API_TASK_BASE_URL + clientApp.getPlatformAppID() + "&id=" + taskID;
                String inputData = pybossaCommunicator.sendGet(taskQueryURL);

                try {
                    boolean isFound = pybossaFormatter.isTaskStatusCompleted(inputData);

                    if(isFound){
                        processTaskQueueImport(clientApp, taskQueue, taskID);
                    }
                    else{
                        long diffHours = DateTimeConverter.getHourDifference(taskQueue.getCreated(), null);
                        if(diffHours >= StatusCodeType.TASK_CLEANUP_CUT_OFF_HOUR){
                            String returnValue = this.removeAbandonedTask(taskID, taskQueue.getTaskQueueID());
                            if(returnValue.equalsIgnoreCase("Exception")){
                                taskQueue.setStatus(StatusCodeType.TASK_ABANDONED);
                                taskQueueService.updateTaskQueue(taskQueue);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
     }

    private void processTaskQueueImport(ClientApp clientApp, TaskQueue taskQueue, Long taskID) throws Exception {
        String PYBOSSA_API_TASK_RUN = PYBOSSA_API_TASK_RUN_BASE_URL + clientApp.getPlatformAppID() + "&task_id=" + taskID;
        String importResult = pybossaCommunicator.sendGet(PYBOSSA_API_TASK_RUN) ;

       // System.out.println("importResult: " + importResult);

        if(DataFormatValidator.isValidateJson(importResult)){
            List<TaskLog> taskLogList = taskLogService.getTaskLog(taskQueue.getTaskQueueID());
            String pybossaResult = pybossaFormatter.getTaskLogDateHistory(taskLogList,importResult, parser, clientApp.getNominalAttributeID());
            int responseCode = pybossaCommunicator.sendPost(pybossaResult, AIDR_TASK_ANSWER_URL);

            if(responseCode ==StatusCodeType.HTTP_OK ||responseCode ==StatusCodeType.HTTP_OK_NO_CONTENT ){
                taskQueue.setStatus(StatusCodeType.TASK_LIFECYCLE_COMPLETED);
                updateTaskQueue(taskQueue);
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

                TaskQueue taskQueue = new TaskQueue(taskID, clientAppID, documentID, status);
                taskQueueService.createTaskQueue(taskQueue);
                TaskLog taskLog = new TaskLog(taskQueue.getTaskQueueID(), taskQueue.getStatus());
                taskLogService.createTaskLog(taskLog);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateTaskQueue(TaskQueue taskQueue){
        taskQueueService.updateTaskQueue(taskQueue);
        TaskLog taskLog = new TaskLog(taskQueue.getTaskQueueID(), taskQueue.getStatus());
        taskLogService.createTaskLog(taskLog);
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

    private int calculateMinNumber(List<ClientApp> clientAppList){
        int min = MAX_PENDING_QUEUE_SIZE;
        for (int i = 0; i < clientAppList.size(); i++) {
            ClientApp obj = clientAppList.get(i);
            int currentPendingTask =  taskQueueService.getCountTaskQeueByStatusAndClientApp(obj.getClientAppID(), StatusCodeType.AIDR_ONLY);
           // System.out.println("currentPendingTask : " + currentPendingTask);
           // System.out.println("MAX_PENDING_QUEUE_SIZE : " + MAX_PENDING_QUEUE_SIZE);
            int numQueue = MAX_PENDING_QUEUE_SIZE - currentPendingTask;
            if(numQueue < 0) {
                    min =  0;
            }
            else{
                min =  numQueue;
            }
        }
        return min;
    }

    private void deactivateClientApp(Long crisisID) throws Exception {
        List<ClientApp> clientAppList = clientAppService.getAllClientAppByCrisisIDAndStatus(crisisID , StatusCodeType.CLIENT_APP_INACTIVE_REQUEST);

        for (int i = 0; i < clientAppList.size(); i++) {
            ClientApp currentClientApp = clientAppList.get(i);
            setClassVariable(currentClientApp.getClient());
            if(currentClientApp.getStatus().equals(StatusCodeType.CLIENT_APP_INACTIVE_REQUEST)){
                String deleteURL = PYBOSSA_API_APP_DELETE_URL + currentClientApp.getPlatformAppID()+ URLPrefixCode.PYBOSSA_APP_UPDATE_KEY + client.getHostAPIKey();
                String returnValue = pybossaCommunicator.deleteGet(deleteURL);
                clientAppService.updateClientAppStatus(currentClientApp, StatusCodeType.CLIENT_APP_DISABLED);
            }

        }
    }

    public void doCleanAbandonedTask() throws Exception{
        List<TaskQueue> taskQueues =  taskQueueService.getTaskQueueByStatus("status",StatusCodeType.TASK_ABANDONED);
        for(int i = 0; i < taskQueues.size(); i++){
            String returnValue = removeAbandonedTask(taskQueues.get(i).getTaskID(), taskQueues.get(i).getTaskQueueID());
        }
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

}
