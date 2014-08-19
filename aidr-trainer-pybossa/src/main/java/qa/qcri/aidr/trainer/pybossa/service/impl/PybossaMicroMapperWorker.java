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
import qa.qcri.aidr.trainer.pybossa.format.impl.CVSRemoteFileFormatter;
import qa.qcri.aidr.trainer.pybossa.format.impl.GeoJsonOutputModel;
import qa.qcri.aidr.trainer.pybossa.format.impl.MicroMapperPybossaFormatter;
import qa.qcri.aidr.trainer.pybossa.format.impl.MicromapperInput;
import qa.qcri.aidr.trainer.pybossa.service.*;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;
import qa.qcri.aidr.trainer.pybossa.store.URLPrefixCode;
import qa.qcri.aidr.trainer.pybossa.store.UserAccount;
import qa.qcri.aidr.trainer.pybossa.util.DataFormatValidator;
import qa.qcri.aidr.trainer.pybossa.util.ErrorLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/18/13
 * Time: 1:19 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("pybossaMicroMapperWorker")
@Transactional(readOnly = false)
public class PybossaMicroMapperWorker implements MicroMapperWorker {

    protected static Logger logger = Logger.getLogger(PybossaMicroMapperWorker.class);
    private static ErrorLog elog = new ErrorLog();
    
    private Client client;
    private CVSRemoteFileFormatter cvsRemoteFileFormatter = new CVSRemoteFileFormatter();
    private int MAX_PENDING_QUEUE_SIZE = 50;
    private int MAX_IMPORT_PROCESS_QUEUE_SIZE = 10000;
    private String PYBOSSA_API_TASK_PUBLSIH_URL;

    private String PYBOSSA_API_TASK_RUN_BASE_URL;
    private String PYBOSSA_API_TASK_BASE_URL;

    private PybossaCommunicator pybossaCommunicator = new PybossaCommunicator();
    private JSONParser parser = new JSONParser();
    private MicroMapperPybossaFormatter pybossaFormatter = new MicroMapperPybossaFormatter();

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientAppService clientAppService;

    @Autowired
    private TaskQueueService taskQueueService;

    @Autowired
    private TaskLogService taskLogService;

    @Autowired
    private ClientAppSourceService clientAppSourceService;

    @Autowired
    private ClientAppResponseService clientAppResponseService;

    @Autowired
    private ReportTemplateService reportTemplateService;

    @Autowired
    private ReportProductService reportProductService;

    public void setClassVariable() throws Exception{
        client = clientService.findClientByCriteria("name", UserAccount.MIROMAPPER_USER_NAME);
        if(client != null){
            PYBOSSA_API_TASK_PUBLSIH_URL = client.getHostURL() + URLPrefixCode.TASK_PUBLISH + client.getHostAPIKey();
            PYBOSSA_API_TASK_BASE_URL  = client.getHostURL() + URLPrefixCode.TASK_INFO;
            PYBOSSA_API_TASK_RUN_BASE_URL =  client.getHostURL() + URLPrefixCode.TASKRUN_INFO;
            MAX_PENDING_QUEUE_SIZE = client.getQueueSize();
        }

    }

    @Override
    public void processTaskPublish() throws Exception{
        setClassVariable();
        if(client == null){
            return;
        }
        List<ClientApp> appList = clientAppService.getAllClientAppByClientID(client.getClientID() );

        if(appList.size() > 0){
            for(int i=0; i < appList.size(); i++){
                ClientApp currentClientApp =  appList.get(i);
                if(currentClientApp.getStatus().equals(StatusCodeType.MICROMAPPER_ONLY)){
                    List<ClientAppSource> datasources = clientAppSourceService.getClientAppSourceByStatus(currentClientApp.getClientAppID(),StatusCodeType.EXTERNAL_DATA_SOURCE_ACTIVE);

                    for(int j=0; j < datasources.size(); j++){
                        List<MicromapperInput> micromapperInputList = null;
                        String url = datasources.get(j).getSourceURL();
                        if(currentClientApp.getAppType() == StatusCodeType.APP_MAP){
                            micromapperInputList = cvsRemoteFileFormatter.getGeoClickerInputData(url);
                        }
                        else{
                            if(currentClientApp.getAppType() == StatusCodeType.APP_AERIAL){
                                micromapperInputList = cvsRemoteFileFormatter.getAerialClickerInputData(url);
                            }
                            else{
                                micromapperInputList = cvsRemoteFileFormatter.getClickerInputData(url);
                            }

                        }
                        if(micromapperInputList != null){
                            if(micromapperInputList.size() > 0) {
                                ClientAppSource source = datasources.get(j);
                                publishToPybossa(currentClientApp, micromapperInputList , source.getClientAppSourceID());
                                clientAppSourceService.updateClientAppSourceStatus(source.getClientAppSourceID(), StatusCodeType.EXTERNAL_DATA_SOURCE_USED);
                            }
                        }
                    }
                }
            }
        }

    }

    private void publishToPybossa(ClientApp currentClientApp, List<MicromapperInput> micromapperInputList, Long clientAppSourceID){
        try {
            List<String> aidr_data = pybossaFormatter.assemblePybossaTaskPublishForm(micromapperInputList, currentClientApp);

            for (String temp : aidr_data) {

                String response = pybossaCommunicator.sendPostGet(temp, PYBOSSA_API_TASK_PUBLSIH_URL) ;

                if(!response.startsWith("Exception")){
                    addToTaskQueue(response, currentClientApp.getClientAppID(), StatusCodeType.TASK_PUBLISHED, clientAppSourceID) ;
                }
                else{
                    addToTaskQueue(temp, currentClientApp.getClientAppID(), StatusCodeType.Task_NOT_PUBLISHED, clientAppSourceID) ;
                }
            }
            // data is consumed. need to mark as completed not to process anymore.
        } catch (Exception e) {
            logger.error("Exception for: " + clientAppSourceID);
            logger.error(elog.toStringException(e));
        }
    }

    private void addToTaskQueue(String inputData, Long clientAppID, Integer status, Long clientAppSourceID){

        logger.info("micromapper worker addToTaskQueue is called");

        try {
            Object obj = parser.parse(inputData);
            JSONObject jsonObject = (JSONObject) obj;

            Long taskID  = (Long)jsonObject.get("id");
            JSONObject info = (JSONObject)jsonObject.get("info");
            Long documentID = (Long)info.get("documentID");

            TaskQueue taskQueue = new TaskQueue(taskID, clientAppID, documentID, status);
            // mostly micromapper will have outside source. AIDR will have docID
            taskQueue.setClientAppSourceID(clientAppSourceID);

            taskQueueService.createTaskQueue(taskQueue);
            TaskLog taskLog = new TaskLog(taskQueue.getTaskQueueID(), taskQueue.getStatus());
            taskLogService.createTaskLog(taskLog);

        } catch (ParseException e) {
            logger.error("error for: " + inputData);
            logger.error(elog.toStringException(e));
        }
    }

    @Override
    public void processTaskImport() throws Exception{
        setClassVariable();
        if(client == null){
            return;
        }
        List<ClientApp> appList = clientAppService.getAllClientAppByClientID(client.getClientID() );
        List<GeoJsonOutputModel> geoJsonOutputModels =  new ArrayList<GeoJsonOutputModel>();
        Iterator itr= appList.iterator();
        while(itr.hasNext()){
            ClientApp clientApp = (ClientApp)itr.next();
            if(clientApp.getStatus().equals(StatusCodeType.MICROMAPPER_ONLY) || clientApp.getStatus().equals(StatusCodeType.AIDR_MICROMAPPER_BOTH)){
                List<TaskQueue> taskQueues =  taskQueueService.getTaskQueueByClientAppStatus(clientApp.getClientAppID(),StatusCodeType.TASK_PUBLISHED);
                if(taskQueues != null ){
                    int queueSize = MAX_IMPORT_PROCESS_QUEUE_SIZE;

                    if(taskQueues.size() < MAX_IMPORT_PROCESS_QUEUE_SIZE)
                    {
                        queueSize =  taskQueues.size();
                    }

                    for(int i=0; i < queueSize; i++){
                        TaskQueue taskQueue = taskQueues.get(i);
                        Long taskID =  taskQueue.getTaskID();
                        String taskQueryURL = PYBOSSA_API_TASK_BASE_URL + clientApp.getPlatformAppID() + "&id=" + taskID;
                        String inputData = pybossaCommunicator.sendGet(taskQueryURL);
                        try {

                            boolean isFound = pybossaFormatter.isTaskStatusCompleted(inputData);
                            if(isFound){
                                processTaskQueueImport(clientApp,taskQueue,taskID, geoJsonOutputModels) ;
                            }

                        } catch (Exception e) {
                            logger.error(elog.toStringException(e));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void processTaskExport() throws Exception {
        reportProductService.generateCVSReportForGeoClicker();
    }

    private void processTaskQueueImport(ClientApp clientApp,TaskQueue taskQueue, Long taskID, List<GeoJsonOutputModel> geoJsonOutputModels) throws Exception {
        String PYBOSSA_API_TASK_RUN = PYBOSSA_API_TASK_RUN_BASE_URL + clientApp.getPlatformAppID() + "&task_id=" + taskID;
        String importResult = pybossaCommunicator.sendGet(PYBOSSA_API_TASK_RUN) ;

        if(!importResult.isEmpty() && importResult.length() > StatusCodeType.RESPONSE_MIN_LENGTH  ){

            ClientAppAnswer clientAppAnswer = clientAppResponseService.getClientAppAnswer(clientApp.getClientAppID());

            if(clientApp.getAppType().equals(StatusCodeType.APP_VIDEO)){
                TaskQueueResponse taskQueueResponse = pybossaFormatter.getAnswerResponseForVideo(clientApp, importResult, parser, taskQueue.getTaskQueueID(), clientAppAnswer,reportTemplateService);
                clientAppResponseService.processTaskQueueResponse(taskQueueResponse);
            }
            else if(clientApp.getAppType().equals(StatusCodeType.APP_MAP))
            {
                TaskQueueResponse taskQueueResponse = pybossaFormatter.getAnswerResponseForGeo(importResult, parser, taskQueue.getTaskQueueID());
                clientAppResponseService.processTaskQueueResponse(taskQueueResponse);
            }
            else if(clientApp.getAppType().equals(StatusCodeType.APP_AERIAL))
            {
                TaskQueueResponse taskQueueResponse = pybossaFormatter.getAnswerResponseForAerial(importResult, parser, taskQueue.getTaskQueueID());
                clientAppResponseService.processTaskQueueResponse(taskQueueResponse);
            }
            else{
                TaskQueueResponse taskQueueResponse = pybossaFormatter.getAnswerResponse(clientApp, importResult, parser, taskQueue.getTaskQueueID(), clientAppAnswer, reportTemplateService);
                clientAppResponseService.processTaskQueueResponse(taskQueueResponse);
            }

            taskQueue.setStatus(StatusCodeType.TASK_LIFECYCLE_COMPLETED);
            updateTaskQueue(taskQueue);
        }
    }

    private void updateTaskQueue(TaskQueue taskQueue){
        taskQueueService.updateTaskQueue(taskQueue);
        TaskLog taskLog = new TaskLog(taskQueue.getTaskQueueID(), taskQueue.getStatus());
        taskLogService.createTaskLog(taskLog);
    }

}
