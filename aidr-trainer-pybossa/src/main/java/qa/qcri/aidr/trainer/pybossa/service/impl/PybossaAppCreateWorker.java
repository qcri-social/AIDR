package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.trainer.pybossa.entity.Client;
import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;
import qa.qcri.aidr.trainer.pybossa.entity.ClientAppAnswer;
import qa.qcri.aidr.trainer.pybossa.entity.TaskQueue;
import qa.qcri.aidr.trainer.pybossa.format.impl.TextClickerPybossaFormatter;
import qa.qcri.aidr.trainer.pybossa.service.*;
import qa.qcri.aidr.trainer.pybossa.store.PybossaConf;
import qa.qcri.aidr.trainer.pybossa.store.LookupCode;
import qa.qcri.aidr.trainer.pybossa.store.URLPrefixCode;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */

@Service("pybossaAppCreateWorker")
@Transactional(readOnly = false)
public class PybossaAppCreateWorker implements ClientAppCreateWorker {

    protected static Logger logger = Logger.getLogger(PybossaAppCreateWorker.class);
    
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientAppService clientAppService;

    @Autowired
    private TaskQueueService taskQueueService;

    @Autowired
    private ClientAppResponseService clientAppResponseService;

    private Client client;
    private String AIDR_ALL_ACTIVE_CRISIS_URL;
    private String PYBOSSA_API_APP_CREATE_URL ;
    private String AIDR_GET_CRISIS_URL;
    private String PYBOSSA_API_APP_UPDATE_BASE_URL;
    private String AIDR_NOMINAL_ATTRIBUTE_LABEL_URL;


    private PybossaCommunicator pybossaCommunicator = new PybossaCommunicator();
    private JSONParser parser = new JSONParser();
    private TextClickerPybossaFormatter textClickerFormat = new TextClickerPybossaFormatter();

    @Transactional(readOnly = true)
    public void setClassVariable(Client theClient) throws Exception{
        boolean resetVariable = false;

        if(client != null){
            if(!client.getClientID().equals(theClient.getClientID())){
                client = theClient;
                resetVariable = true;
            }
        }
        else{
            theClient = clientService.findClientByCriteria("name",LookupCode.SYSTEM_USER_NAME);
            client = theClient;
            resetVariable = true;
        }

        if(resetVariable){
            AIDR_ALL_ACTIVE_CRISIS_URL = client.getAidrHostURL() + URLPrefixCode.AIDR_ACTIVE_NOMINAL_ATTRIBUTE ;
            AIDR_GET_CRISIS_URL = client.getAidrHostURL() + URLPrefixCode.AIDR_CRISIS_INFO ;
            PYBOSSA_API_APP_UPDATE_BASE_URL = client.getHostURL()  + URLPrefixCode.PYBOSAA_APP;
            PYBOSSA_API_APP_CREATE_URL = client.getHostURL()  + URLPrefixCode.PYBOSSA_APP_KEY + client.getHostAPIKey();
            AIDR_NOMINAL_ATTRIBUTE_LABEL_URL = client.getAidrHostURL() + URLPrefixCode.AIDR_NOMINAL_ATTRIBUTE_LABEL;
        }

    }

    @Override
    public void doCreateApp() throws Exception{
        System.out.println("doCreateApp : Start : " + new Date());
        try{
            setClassVariable(client);

            if(client != null){

                List<ClientApp> appList = clientAppService.getAllClientAppByClientID(client.getClientID());
                String crisisSet = pybossaCommunicator.sendGet(AIDR_ALL_ACTIVE_CRISIS_URL);

                if(crisisSet != null && !crisisSet.isEmpty() ){
                    JSONArray array = (JSONArray) parser.parse(crisisSet);

                    for(int i = 0 ; i < array.size(); i++){
                        JSONObject jsonObject = (JSONObject)array.get(i);
                        Long cririsID = (Long)jsonObject.get("cririsID");
                        Long attID = (Long)jsonObject.get("nominalAttributeID");
                        if(!this.findDuplicate(appList, cririsID, attID) ){
                            // AIDR_GET_CRISIS_URL = AIDR_GET_CRISIS_URL + id;
                            String cririsInfo = pybossaCommunicator.sendGet(AIDR_GET_CRISIS_URL + cririsID);
                            if(!cririsInfo.isEmpty()){
                                JSONObject crisisJson = (JSONObject) parser.parse(cririsInfo);
                                if(crisisJson.get("nominalAttributeJsonModelSet") != null ){
                                    String nominalModel = crisisJson.get("nominalAttributeJsonModelSet").toString();
                                    if(!nominalModel.isEmpty() && nominalModel.length() > LookupCode.RESPONSE_MIN_LENGTH){
                                        String name = (String)crisisJson.get("name");
                                        Long crisisID = (Long)crisisJson.get("crisisID");
                                        String code = (String)crisisJson.get("code");
                                        String description = name + "(" + crisisID + ")";
                                        JSONArray attArray = (JSONArray)crisisJson.get("nominalAttributeJsonModelSet");
                                        Iterator itr= attArray.iterator();
                                        while(itr.hasNext()){
                                            JSONObject featureJsonObj = (JSONObject)itr.next();
                                            Long nominalAttributeID = (Long)featureJsonObj.get("nominalAttributeID");
                                            this.processAppCreation(featureJsonObj, nominalAttributeID,crisisID,attID,code, name, description);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e){
            logger.error("creation exception : " + e);
        }


    }

    private void processAppCreation(JSONObject featureJsonObj, Long nominalAttributeID,Long crisisID, Long attID, String code,String name,String description) throws Exception {
        String PYBOSSA_APP_INFO_URL = client.getHostURL()  + URLPrefixCode.PYBOSSA_SHORT_NAME;
        boolean updateInfo = false;
        boolean duplicateFound = false;

        if(nominalAttributeID.equals(attID)){
            String appcode = code + "_" + (String)featureJsonObj.get("code");
            String appname = name + ": " +  (String)featureJsonObj.get("name") ;
            ClientApp localClientApp = clientAppService.findClientAppByCriteria("shortName",appcode);

            if(localClientApp == null){
                String data = textClickerFormat.assmeblePybossaAppCreationForm(appname,appcode, description);
                int responseCode = pybossaCommunicator.sendPost(data, PYBOSSA_API_APP_CREATE_URL);
                if(responseCode == LookupCode.HTTP_OK){
                    updateInfo = true;
                }
                else{
                    if(responseCode == LookupCode.HTTP_OK_DUPLICATE_INFO_FOUND){
                         logger.info("duplicate app found : " + responseCode);
                         duplicateFound = true;
                    }
                }
            }
            else{
                updateInfo = true;
            }

            if(duplicateFound) {
                String appInfoDuplicate = pybossaCommunicator.sendGet(PYBOSSA_APP_INFO_URL + appcode);
                Long appIDDuplicate = textClickerFormat.getAppID(appInfoDuplicate, parser);
                this.createClientAppInstance(crisisID, appname,description,appIDDuplicate,appcode,nominalAttributeID);
                return;
            }

            if(updateInfo){
                JSONArray labelModel = (JSONArray) featureJsonObj.get("nominalLabelJsonModelSet");
                String appInfo = pybossaCommunicator.sendGet(PYBOSSA_APP_INFO_URL + appcode);
                Long appID = textClickerFormat.getAppID(appInfo, parser);
                if(localClientApp == null){
                    localClientApp = this.createClientAppInstance(crisisID, appname,description,appID,appcode,nominalAttributeID);
                }
                this.doAppUpdate(localClientApp, appInfo, featureJsonObj, labelModel, code, name);
            }
        }
    }

    @Override
    public void doAppUpdate(ClientApp clientApp, String appInfoJson,JSONObject attribute, JSONArray labelModel, String crisisCode, String crisisName) throws Exception{

        String PYBOSSA_API_APP_UPDATE_URL = PYBOSSA_API_APP_UPDATE_BASE_URL + clientApp.getPlatformAppID() + URLPrefixCode.PYBOSSA_APP_UPDATE_KEY + client.getHostAPIKey();
        String data = textClickerFormat.updateApp(clientApp, attribute, labelModel, PybossaConf.DEFAULT_CATEGORY_ID);

        int responseCode = pybossaCommunicator.sendPut(data, PYBOSSA_API_APP_UPDATE_URL);

        ClientAppAnswer clientAppAnswer = clientAppResponseService.getClientAppAnswer(clientApp.getClientAppID());

        if(clientAppAnswer == null){
            int cutOffValue = LookupCode.MAX_VOTE_CUT_OFF_VALUE;
            String AIDR_NOMINAL_ATTRIBUTE_LABEL_URL_PER_APP = AIDR_NOMINAL_ATTRIBUTE_LABEL_URL + clientApp.getCrisisID() + "/" + clientApp.getNominalAttributeID();
            String answerSet = pybossaCommunicator.sendGet(AIDR_NOMINAL_ATTRIBUTE_LABEL_URL_PER_APP) ;

            if(clientApp.getTaskRunsPerTask() < LookupCode.MAX_VOTE_CUT_OFF_VALUE){
                cutOffValue = LookupCode.MIN_VOTE_CUT_OFF_VALUE;
            }

            clientAppResponseService.saveClientAppAnswer(clientApp.getClientAppID(), answerSet, cutOffValue);
        }


    }

    @Override
    public void doAppTemplateUpdate(ClientApp clientApp, Long nominalAttributeID) throws Exception {
        Long crisisID = clientApp.getCrisisID();
        if(client == null){
            setClassVariable(client);
        }
        String crisisInfo = pybossaCommunicator.sendGet(AIDR_GET_CRISIS_URL + crisisID);
        if(!crisisInfo.isEmpty()){
            JSONObject crisisJson = (JSONObject) parser.parse(crisisInfo);
            if(crisisJson.get("nominalAttributeJsonModelSet") != null ){
                String nominalModel = crisisJson.get("nominalAttributeJsonModelSet").toString();
                if(!nominalModel.isEmpty() && nominalModel.length() > LookupCode.RESPONSE_MIN_LENGTH){
                    String name = (String)crisisJson.get("name");
                    String code = (String)crisisJson.get("code");
                    String description = name + "(" + crisisID + ")";
                    JSONArray attArray = (JSONArray)crisisJson.get("nominalAttributeJsonModelSet");
                    Iterator itr= attArray.iterator();

                    while(itr.hasNext()){
                        JSONObject featureJsonObj = (JSONObject)itr.next();
                        Long nominalAttID = (Long)featureJsonObj.get("nominalAttributeID");

                        if(nominalAttributeID.equals(nominalAttID)){
                            processAppCreation(featureJsonObj, nominalAttributeID,crisisID,nominalAttID,code, name, description);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void doAppDelete() throws Exception {
        System.out.println("doAppDelete : Start : " + new Date());
        List<ClientApp> clientAppList = clientAppService.findClientAppByStatus(LookupCode.CLIENT_APP_INACTIVE_REQUEST) ;
        for (int i = 0; i < clientAppList.size(); i++) {
            ClientApp currentClientApp = clientAppList.get(i);
            setClassVariable(currentClientApp.getClient());
            this.doMarkTaskAbandoned(currentClientApp);
            clientAppService.updateClientAppStatus(currentClientApp, LookupCode.CLIENT_APP_DISABLED);
            doCleanAbandonedTask();
        }
    }

    public void doMarkTaskAbandoned(ClientApp currentClientApp) throws Exception{
        List<TaskQueue> taskQueues =  taskQueueService.getTaskQueueByClientAppStatus(currentClientApp.getClientAppID(), LookupCode.TASK_PUBLISHED);

        for(int i = 0; i < taskQueues.size(); i++){
            TaskQueue taskToBeRemoved = taskQueues.get(i);
            taskToBeRemoved.setStatus(LookupCode.TASK_ABANDONED)  ;
            taskQueueService.createTaskQueue(taskToBeRemoved);
        }

    }

    public void doCleanAbandonedTask() throws Exception{
        List<TaskQueue> taskQueues =  taskQueueService.getTaskQueueByStatus("status", LookupCode.TASK_ABANDONED);
        for(int i = 0; i < taskQueues.size(); i++){
            TaskQueue taskToBeRemoved = taskQueues.get(i);
            taskToBeRemoved.setStatus(LookupCode.TASK_ABANDONED)  ;
            taskQueueService.createTaskQueue(taskToBeRemoved);
        }
    }

    private ClientApp createClientAppInstance(Long crisisID, String appname, String description, Long appID, String appcode, Long nominalAttributeID){
        ClientApp clApp = new ClientApp(client.getClientID(),crisisID, appname,description,appID,appcode,nominalAttributeID, client.getDefaultTaskRunsPerTask(), LookupCode.APP_MULTIPLE_CHOICE);
        clientAppService.createClientApp(clApp);
        return clApp;

    }

    private boolean findDuplicate(List<ClientApp> clientAppList, Long crisisID, Long nominalAttributeID ){

        boolean found = false;
        Iterator<ClientApp> iterator = clientAppList.iterator();

        while (iterator.hasNext()){
            Object o = iterator.next();
            ClientApp cm =  (ClientApp)o;
            if(cm.getCrisisID().equals(crisisID)){
                if(cm.getNominalAttributeID().equals(nominalAttributeID)){
                    found = true;
                }
            }
        }

        return found;
    }

}
