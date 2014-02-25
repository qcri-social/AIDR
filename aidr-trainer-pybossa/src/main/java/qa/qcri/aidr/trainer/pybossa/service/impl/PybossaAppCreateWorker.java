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
import qa.qcri.aidr.trainer.pybossa.format.impl.PybossaFormatter;
import qa.qcri.aidr.trainer.pybossa.service.ClientAppCreateWorker;
import qa.qcri.aidr.trainer.pybossa.service.ClientAppService;
import qa.qcri.aidr.trainer.pybossa.service.ClientService;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;
import qa.qcri.aidr.trainer.pybossa.store.URLPrefixCode;
import qa.qcri.aidr.trainer.pybossa.store.UserAccount;

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

    protected static Logger logger = Logger.getLogger("service");

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientAppService clientAppService;


    private Client client;
    private String AIDR_ALL_ACTIVE_CRISIS_URL;
    private String PYBOSSA_API_APP_CREATE_URL ;
    private String AIDR_GET_CRISIS_URL;
    private String PYBOSSA_API_APP_UPDATE_BASE_URL;
    private String PYBOSSA_API_CATEGORY_BASE_URL;

    private PybossaCommunicator pybossaCommunicator = new PybossaCommunicator();
    private JSONParser parser = new JSONParser();
    private PybossaFormatter pybossaFormatter = new PybossaFormatter();


    public void setClassVariable() throws Exception{
        client = clientService.findClientByCriteria("name", UserAccount.SYSTEM_USER_NAME);
        if(client != null){
            AIDR_ALL_ACTIVE_CRISIS_URL = client.getAidrHostURL() + URLPrefixCode.AIDR_ACTIVE_NOMINAL_ATTRIBUTE ;
            AIDR_GET_CRISIS_URL = client.getAidrHostURL() + URLPrefixCode.AIDR_CRISIS_INFO ;
            PYBOSSA_API_APP_UPDATE_BASE_URL = client.getHostURL()  + URLPrefixCode.PYBOSAA_APP;
            PYBOSSA_API_APP_CREATE_URL = client.getHostURL()  + URLPrefixCode.PYBOSSA_APP_KEY + client.getHostAPIKey();
            PYBOSSA_API_CATEGORY_BASE_URL = client.getHostURL() + URLPrefixCode.PYBOSSA_CATEGORY;
        }
    }

    @Override
    public void doCreateApp() throws Exception{
        setClassVariable();
        if(client != null){


            List<ClientApp> appList = clientAppService.getAllClientAppByClientID(client.getClientID());
            String crisisSet = pybossaCommunicator.sendGet(AIDR_ALL_ACTIVE_CRISIS_URL);

            if(crisisSet != null ){

                JSONArray array = (JSONArray) parser.parse(crisisSet);

                for(int i = 0 ; i < array.size(); i++){
                    JSONObject jsonObject = (JSONObject)array.get(i);

                    Long cririsID = (Long)jsonObject.get("cririsID");
                    Long attID = (Long)jsonObject.get("nominalAttributeID");

                    if(!findDuplicate(appList, cririsID, attID)){
                        // AIDR_GET_CRISIS_URL = AIDR_GET_CRISIS_URL + id;
                        String cririsInfo = pybossaCommunicator.sendGet(AIDR_GET_CRISIS_URL + cririsID);
                        if(!cririsInfo.isEmpty()){
                            JSONObject crisisJson = (JSONObject) parser.parse(cririsInfo);
                            if(crisisJson.get("nominalAttributeJsonModelSet") != null ){
                                String nominalModel = crisisJson.get("nominalAttributeJsonModelSet").toString();
                                if(!nominalModel.isEmpty() && nominalModel.length() > StatusCodeType.RESPONSE_MIN_LENGTH){
                                    String name = (String)crisisJson.get("name");
                                    Long crisisID = (Long)crisisJson.get("crisisID");
                                    String code = (String)crisisJson.get("code");
                                    String description = name + "(" + crisisID + ")";
                                    JSONArray attArray = (JSONArray)crisisJson.get("nominalAttributeJsonModelSet");
                                    Iterator itr= attArray.iterator();
                                    while(itr.hasNext()){
                                        JSONObject featureJsonObj = (JSONObject)itr.next();
                                        Long nominalAttributeID = (Long)featureJsonObj.get("nominalAttributeID");
                                        processAppCreation(featureJsonObj, nominalAttributeID,crisisID,attID,code, name, description);
                                    }
                                }
                            }
                        }
                    }
                }
            }



        }

    }

    private void processAppCreation(JSONObject featureJsonObj, Long nominalAttributeID,Long crisisID, Long attID, String code,String name,String description) throws Exception {
        String PYBOSSA_APP_INFO_URL = client.getHostURL()  + URLPrefixCode.PYBOSSA_SHORT_NAME;
        boolean updateInfo = false;
        if(nominalAttributeID.equals(attID)){
            String appcode = code + "_" + (String)featureJsonObj.get("code");
            String appname = name + ": " +  (String)featureJsonObj.get("name") ;
            ClientApp localClientApp = clientAppService.findClientAppByCriteria("shortName",appcode);

            if(localClientApp == null){
                String data = pybossaFormatter.assmeblePybossaAppCreationForm(appname,appcode, description);
                int responseCode = pybossaCommunicator.sendPost(data, PYBOSSA_API_APP_CREATE_URL);
                if(responseCode == StatusCodeType.HTTP_OK){
                    updateInfo = true;
                }
                else{
                    if(responseCode == StatusCodeType.HTTP_OK_DUPLICATE_INFO_FOUND){
                        // logger.info("duplicate app found : " + responseCode);
                    }
                }
            }
            else{
                updateInfo = true;
            }


            if(updateInfo){
                JSONArray labelModel = (JSONArray) featureJsonObj.get("nominalLabelJsonModelSet");
                String appInfo = pybossaCommunicator.sendGet(PYBOSSA_APP_INFO_URL + appcode);
                Long appID = pybossaFormatter.getAppID(appInfo, parser);
                if(localClientApp == null){
                    localClientApp = createClientAppInstance(crisisID, appname,description,appID,appcode,nominalAttributeID);
                }
                doAppUpdate(localClientApp, appInfo, featureJsonObj, labelModel, code, name);
            }
        }
    }

    @Override
    public void doAppUpdate(ClientApp clientApp, String appInfoJson,JSONObject attribute, JSONArray labelModel, String crisisCode, String crisisName) throws Exception{
        //  String PYBOSSA_API_APP_UPDATE = ClientDummy.HOST_URL  + "/app/" + ClientDummy.APP_ID +"?api_key=" + ClientDummy.HOST_APIKey;
        //Client cl = clientApp.getClient();
        Long categoryID = this.getCategoryID(crisisName, crisisCode) ;

        String PYBOSSA_API_APP_UPDATE_URL = PYBOSSA_API_APP_UPDATE_BASE_URL + clientApp.getPlatformAppID() + URLPrefixCode.PYBOSSA_APP_UPDATE_KEY + client.getHostAPIKey();
        String data = pybossaFormatter.updateApp(clientApp,attribute, labelModel, categoryID);
       // System.out.print("out : " + data);
        int responseCode = pybossaCommunicator.sendPut(data, PYBOSSA_API_APP_UPDATE_URL);
        //System.out.print("responseCode: " + responseCode);
    }

    @Override
    public void doAppTemplateUpdate(ClientApp clientApp, Long nominalAttributeID) throws Exception {
        Long crisisID = clientApp.getCrisisID();
        if(client == null){
            setClassVariable();
        }
        String crisisInfo = pybossaCommunicator.sendGet(AIDR_GET_CRISIS_URL + crisisID);
        if(!crisisInfo.isEmpty()){
            JSONObject crisisJson = (JSONObject) parser.parse(crisisInfo);
            if(crisisJson.get("nominalAttributeJsonModelSet") != null ){
                String nominalModel = crisisJson.get("nominalAttributeJsonModelSet").toString();
                if(!nominalModel.isEmpty() && nominalModel.length() > StatusCodeType.RESPONSE_MIN_LENGTH){
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


    private ClientApp createClientAppInstance(Long crisisID, String appname, String description, Long appID, String appcode, Long nominalAttributeID){
        ClientApp clApp = new ClientApp(client.getClientID(),crisisID, appname,description,appID,appcode,nominalAttributeID, client.getDefaultTaskRunsPerTask(), StatusCodeType.APP_MULTIPLE_CHOICE);
        clientAppService.createClientApp(clApp);
        return clApp;

    }

    private boolean findDuplicate(List<ClientApp> clientApp, Long crisisID, Long nominalAttributeID ){

        boolean found = false;
        Iterator<ClientApp> iterator = clientApp.iterator();

        while (iterator.hasNext()){
            ClientApp cm =  (ClientApp)iterator.next();
            if(cm.getCrisisID().equals(crisisID)){
                if(cm.getNominalAttributeID().equals(nominalAttributeID)){
                    found = true;
                }
            }
        }

        return found;
    }

    private Long getCategoryID(String crisisName,  String crisisCode) throws Exception {
        Long categoryID = null;
        String PYBOSSA_API_CATEGORY_FIND_URL = PYBOSSA_API_CATEGORY_BASE_URL+ URLPrefixCode.PYBOSSA_CATEGORY_SHORT_NAME +  crisisCode;
        String responseData = pybossaCommunicator.sendGet(PYBOSSA_API_CATEGORY_FIND_URL);

        if(responseData.trim().length() > StatusCodeType.RESPONSE_MIN_LENGTH) {
            categoryID = pybossaFormatter.getCategoryID(responseData,  parser, true);
        }
        else{
            String PYBOSSA_API_CATEGORY_CREATE_URL =  PYBOSSA_API_CATEGORY_BASE_URL + URLPrefixCode.PYBOSSA_APP_UPDATE_KEY + client.getHostAPIKey();
            String newCategoryData = pybossaFormatter.getCatagoryDataSet(crisisName, crisisCode);
            responseData = pybossaCommunicator.sendPostGet(newCategoryData, PYBOSSA_API_CATEGORY_CREATE_URL);
            categoryID = pybossaFormatter.getCategoryID(responseData,  parser, false);
          // create and get
         //
        }
        return categoryID;
    }
}
