package qa.qcri.aidr.trainer.api.service.impl;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.CustomUITemplateDao;
import qa.qcri.aidr.trainer.api.entity.*;
import qa.qcri.aidr.trainer.api.service.ClientAppService;
import qa.qcri.aidr.trainer.api.service.CrisisService;
import qa.qcri.aidr.trainer.api.service.CustomUITemplateService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.NominalAttributeJsonModel;
import qa.qcri.aidr.trainer.api.template.NominalLabelJsonModel;
import qa.qcri.aidr.trainer.api.util.Communicator;
import qa.qcri.aidr.trainer.api.util.DataSorterUtil;
import qa.qcri.aidr.trainer.api.util.StreamConverter;

import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/2/14
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("customUITemplateService")
@Transactional(readOnly = true)
public class CustomUITemplateServiceImpl implements CustomUITemplateService {
    protected static Logger logger = Logger.getLogger("service");

    @Autowired
    CustomUITemplateDao customUITemplateDao;

    @Autowired
    CrisisService crisisService;

    @Autowired
    ClientAppService clientAppService;

    @Override
    public List<CustomUITemplate> getCustomTemplateSkinType(Long crisisID){
        //
        return  customUITemplateDao.getTemplateByCrisisWithType(crisisID, CodeLookUp.CLASSIFIER_SKIN);

    }


    @Override
    public List<CustomUITemplate> getCustomTemplateByCrisis(Long crisisID){
    //
        return customUITemplateDao.getTemplateByCrisis(crisisID) ;
    }

    @Override
    public List<CustomUITemplate> getCustomTemplateForLandingPage(Long crisisID){
        List<CustomUITemplate> templates =  customUITemplateDao.getTemplateByCrisis(crisisID) ;
        List<CustomUITemplate> tempList = new ArrayList<CustomUITemplate>();

        for(CustomUITemplate template : templates){
            Integer tempType = template.getTemplateType();

            if(tempType.equals(CodeLookUp.PUBLIC_LANDING_PAGE_TOP) || tempType.equals(CodeLookUp.PUBLIC_LANDING_PAGE_BOTTOM) || tempType.equals(CodeLookUp.CURATOR_NAME))
            {
                tempList.add(template) ;
            }
        }

        return tempList ;
    }

    @Override
    public void updateCustomTemplateByCrisis(Long crisisID, int customUIType) {

        List<CustomUITemplate> customUITemplateList =  customUITemplateDao.getTemplateByCrisis(crisisID);
        if(customUITemplateList.size() > 0){
            List<ClientApp> clientApps = clientAppService.getAllClientAppByCrisisID(crisisID);
            for(ClientApp app :clientApps){
                long attributeID = app.getNominalAttributeID();

            }
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateCustomTemplateByAttribute(Long crisisID, Long attributeID, int customUIType, int skinType) throws Exception {
        logger.debug("updateCustomTemplateByAttribute");
        logger.debug("crisisID : " + crisisID);
        logger.debug("attributeID" + attributeID);
        logger.debug("customUIType" + customUIType);
        logger.debug("skinType" + skinType);

        List<CustomUITemplate> cList ;
        if(customUIType== CodeLookUp.CLASSIFIER_WELCOME_PAGE){
            logger.debug("CLASSIFIER_WELCOME_PAGE");
            cList =  customUITemplateDao.getTemplateByAttributeAndType(crisisID,attributeID, StatusCodeType.CUSTOM_UI_UPDATE_REQUEST, customUIType);
            if(cList.size() > 0){
                ClientApp clientApp = clientAppService.getClientAppByCrisisAndAttribute(crisisID,  attributeID);
                CustomUITemplate c = cList.get(0);
                String longDescString = buildWelcomePage(c.getTemplateValue());
                //ClientApp clientApp, int customUIType, String updateTemplateValue
                String jsonData = this.assembleTPybossaJson(clientApp, CodeLookUp.WELCOMPAGE_UPDATE, longDescString);
                this.sendToPybossa(jsonData, clientApp );
            }

        }

        if(customUIType== CodeLookUp.CLASSIFIER_TUTORIAL_ONE || customUIType== CodeLookUp.CLASSIFIER_TUTORIAL_TWO){
            logger.debug("CLASSIFIER_TUTORIAL");
            cList =  customUITemplateDao.getTemplateByAttribute( crisisID,  attributeID);
            ClientApp clientApp = clientAppService.getClientAppByCrisisAndAttribute(crisisID,  attributeID);
            if(cList.size() > 0){
                String tutorialOne = null ;
                String tutorialTwo  = null;
                for(CustomUITemplate c : cList){
                    if(c.getTemplateType().equals(CodeLookUp.CLASSIFIER_TUTORIAL_ONE) ){
                        tutorialOne = c.getTemplateValue();
                    }

                    if(c.getTemplateType().equals(CodeLookUp.CLASSIFIER_TUTORIAL_TWO) ){
                        tutorialTwo = c.getTemplateValue();
                    }
                }
               String tutorialPage;
               if(tutorialOne == null ){
                   tutorialOne = this.buildDefaultTutorialPartOne(clientApp);
               }

               if(tutorialTwo == null ){
                   tutorialTwo = this.buildDefaultTutorialPartTwo(clientApp);
               }

               tutorialPage =  buildTutorialTemplate(clientApp, tutorialOne, tutorialTwo);
               logger.debug("CLASSIFIER_TUTORIAL - Context : " + tutorialPage);
               String jsonData = this.assembleTPybossaJson(clientApp,CodeLookUp.TUTORIAL,  tutorialPage);
               this.sendToPybossa(jsonData, clientApp );

            }
            else{
               String defaultTutorialPage = this.buildDefaultTutorialTemplate(clientApp);
               logger.debug("CLASSIFIER_TUTORIAL - Context : " + defaultTutorialPage);
               String jsonData = this.assembleTPybossaJson(clientApp,CodeLookUp.TUTORIAL,  defaultTutorialPage);
               this.sendToPybossa(jsonData, clientApp );
            }
        }

        if(customUIType== CodeLookUp.CLASSIFIER_SKIN){
            logger.debug("CLASSIFIER_SKIN");
            List<ClientApp> apps = clientAppService.getAllClientAppByCrisisID(crisisID);
            logger.debug("skim. app count: " + apps.size());
            for(ClientApp clientApp : apps){
                logger.debug("skim. clientApp: " + clientApp.getClientAppID());
                //ClientApp clientApp = clientAppService.getClientAppByCrisisAndAttribute(crisisID,  attributeID);
                Set<ModelFamily> families = crisisService.findByCrisisID(crisisID).getModelFamilySet();
                logger.debug("skim. families: " + families.size());
                for(ModelFamily family : families){
                    logger.debug("skim. families: " + family.getNominalAttributeID());
                    logger.debug("skim. clientApp: " + clientApp.getNominalAttributeID());
                    if(family.getNominalAttributeID().equals(clientApp.getNominalAttributeID())){
                        NominalAttribute nom = family.getNominalAttribute();
                        String skinUpdate = buildAppSkin(clientApp, nom, skinType);
                        String jsonData = this.assembleTPybossaJson(clientApp,CodeLookUp.TASK_PRESENTER,  skinUpdate);
                        this.sendToPybossa(jsonData, clientApp );
                    }
                }
            }
        }
    }


    @Override
    public String assembleTPybossaJson(ClientApp clientApp, String key, String value) throws Exception{

        String getInfo = this.getAppInfo(clientApp);
        JSONParser parser = new JSONParser();

        JSONObject data = (JSONObject) parser.parse(getInfo);


        String created = (String)data.get("created");

        JSONObject info  = (JSONObject)data.get("info");
        String long_description = (String)data.get("long_description");
        String tutorial = (String)info.get("tutorial");
        String task_presenter = (String)info.get("task_presenter");

        if(key.equalsIgnoreCase(CodeLookUp.WELCOMPAGE_UPDATE)){
            long_description = value;
        }

        if(key.equalsIgnoreCase(CodeLookUp.TUTORIAL)){
            tutorial = value;
        }

        if(key.equalsIgnoreCase(CodeLookUp.TASK_PRESENTER)){
            task_presenter = value;
        }

        JSONObject app = new JSONObject();

        app.put("task_presenter", task_presenter);

        app.put("tutorial", tutorial);
        app.put("thumbnail", "http://i.imgur.com/lgZAWIc.png");

        JSONObject app2 = new JSONObject();
        app2.put("info", app );

        app2.put("long_description", long_description);
        app2.put("name", clientApp.getName());
        app2.put("short_name", clientApp.getShortName());
        app2.put("description", clientApp.getShortName());
        app2.put("id", clientApp.getPlatformAppID());
        app2.put("time_limit", 0);
        app2.put("long_tasks", 0);
        app2.put("created", created);
        app2.put("calibration_frac", 0);
        app2.put("bolt_course_id", 0);
        app2.put("link", "<link rel='self' title='app' href='http://localhost:5000/api/app/2'/>");
        app2.put("allow_anonymous_contributors", true);
        app2.put("time_estimate", 0);
        app2.put("hidden", 0);
        if(data.get("category_id")!=null){
            long category_id = (Long)data.get("category_id");
            app2.put("category_id", category_id);
        }
        //app2.put("owner_id", 1);

        return  app2.toJSONString();
    }

    public String buildTutorialTemplate(ClientApp clientApp, String partOne, String partTwo) throws Exception{

        InputStream tutorialIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/tutorial.html");
        String tutorialString = StreamConverter.convertStreamToString(tutorialIS) ;

        tutorialString = tutorialString.replace("TEMPLATE:SHORTNAME", clientApp.getShortName());
        tutorialString = tutorialString.replace("TEMPLATE:NAME", clientApp.getName());
        tutorialString = tutorialString.replace("TEMPLATE:PARTONE", partOne);
        tutorialString = tutorialString.replace("TEMPLATE:PARTTWO", partTwo);


        return tutorialString;
    }

    public String buildWelcomePage(String templateValue) throws Exception{
        InputStream longDescIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/long_description.html");
        String longDescString = StreamConverter.convertStreamToString(longDescIS) ;
        longDescString = longDescString.replace("TEMPLATE:DESCRIPTION", templateValue);

        return longDescString;
    }

    public String buildAppSkin(ClientApp clientApp, NominalAttribute attribute, int skinType) throws Exception {
        InputStream templateIS = null;
        if(skinType == CodeLookUp.DEFAULT_SKIN){
            templateIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/template.html");
        }
        else{
            templateIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("html/iPhoneTemplate.html");
        }

        String templateString = StreamConverter.convertStreamToString(templateIS) ;

        templateString = templateString.replace("TEMPLATE:SHORTNAME", clientApp.getShortName());

        String attributeDisplay = attribute.getName();

        attributeDisplay =  attributeDisplay +" " + attribute.getDescription();
        templateString = templateString.replace("TEMPLATE:FORATTRIBUTEAIDR", attributeDisplay);

        Set<NominalLabel> nominalLabels =   attribute.getNominalLabelSet();
        SortedMap nominalLabelMap = DataSorterUtil.sortNominalLabelByCode(attribute.getNominalLabelSet());
        String labelString = buildLabelList(nominalLabelMap, skinType);

        templateString = templateString.replace("TEMPLATE:FORLABELSFROMAIDR", labelString);


        return  templateString;

    }

    public String buildLabelList(SortedMap mapData, int skinType){

        StringBuffer displayLabel = new StringBuffer();
        Iterator itr= mapData.entrySet().iterator();

        Set<String> numbers = mapData.keySet();

        for (String number : numbers) {
            //System.out.println(mapData.get(number));
            NominalLabel featureJsonObj =(NominalLabel) mapData.get(number);

            String labelName = featureJsonObj.getName()  ;
            String lableCode = featureJsonObj.getNorminalLabelCode() ;
            String description = featureJsonObj.getDescription();
            Long norminalLabelID = featureJsonObj.getNorminalLabelID();

            if(skinType == CodeLookUp.IPHONE_SKIN) {
                    displayLabel.append("<li id='")  ;
                    displayLabel.append(lableCode) ;
                    displayLabel.append("'>") ;
                    displayLabel.append(labelName) ;
                    displayLabel.append("</li>")  ;
            }
            else{

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

        }


        return displayLabel.toString();
    }

    public String buildDefaultTutorialPartOne(ClientApp clientApp) throws  Exception{
        long crisisID = clientApp.getCrisisID();

        CrisisJsonModel crisisJsonModel = crisisService.findByOptimizedCrisisID(crisisID);


        StringBuffer partOne = new StringBuffer();
        partOne.append("<b>Hi!</b>  Many thanks for volunteering your time as a Digital Humanitarian, in order to learn more about ");
        partOne.append(crisisJsonModel.getName());
        partOne.append(".");
        partOne.append(" Critical information is often shared on Twitter in real time, which is where you come in.");

        return partOne.toString();
    }

    public String buildDefaultTutorialPartTwo(ClientApp clientApp) throws  Exception{
        long crisisID = clientApp.getCrisisID();
        long attributeID = clientApp.getNominalAttributeID();
        CrisisJsonModel crisisJsonModel = crisisService.findByOptimizedCrisisID(crisisID);
        Set<NominalAttributeJsonModel> nominalAttributes = crisisJsonModel.getNominalAttributeJsonModelSet();
        Set<NominalLabelJsonModel> labelJsonModelSet = null;

        for(NominalAttributeJsonModel a : nominalAttributes){
            if(a.getNominalAttributeID().equals(attributeID)) {
                labelJsonModelSet = a.getNominalLabelJsonModelSet();
            }
        }

        StringBuffer partTwo = new StringBuffer();
        partTwo.append("Being a Digital Humanitarian is as easy and fast as a click of the mouse. ");
        partTwo.append("If you want to keep track of your progress and points, make sure to login! ");
        partTwo.append("This Clicker will simply load a tweet and ask you to click on the category that best describes the tweet.<br/>");

        if(labelJsonModelSet != null ){
            partTwo.append("<table>");
            for(NominalLabelJsonModel labelJsonModel : labelJsonModelSet) {
                partTwo.append("<tr><td>");
                partTwo.append("<b>" + labelJsonModel.getName() + "</b></td>") ;
                if(!labelJsonModel.getDescription().isEmpty()){
                    partTwo.append("<td>: " + labelJsonModel.getDescription() + "</td>") ;
                }
                partTwo.append("</tr>");
            }
            partTwo.append("</table>");

        }
        partTwo.append("<br/><br/>");
        partTwo.append("Note that these tweets come directly from twitter and may on rare occasions include disturbing content. Only start clicking if you understand this and still wish to volunteer.");
        partTwo.append("<br/><br/>");
        partTwo.append("Thank you!");

        return partTwo.toString();

    }

    private String buildDefaultTutorialTemplate(ClientApp clientApp) throws  Exception{

        long crisisID = clientApp.getCrisisID();
        long attributeID = clientApp.getNominalAttributeID();
        CrisisJsonModel crisisJsonModel = crisisService.findByOptimizedCrisisID(crisisID);
        Set<NominalAttributeJsonModel> nominalAttributes = crisisJsonModel.getNominalAttributeJsonModelSet();
        Set<NominalLabelJsonModel> labelJsonModelSet = null;

        for(NominalAttributeJsonModel a : nominalAttributes){
            if(a.getNominalAttributeID().equals(attributeID)) {
                labelJsonModelSet = a.getNominalLabelJsonModelSet();
            }
        }


        StringBuffer partOne = new StringBuffer();
        partOne.append("<b>Hi!</b>  Many thanks for volunteering your time as a Digital Humanitarian, in order to learn more about ");
        partOne.append(crisisJsonModel.getName());
        partOne.append(".");
        partOne.append(" Critical information is often shared on Twitter in real time, which is where you come in.");

        StringBuffer partTwo = new StringBuffer();
        partTwo.append("Being a Digital Humanitarian is as easy and fast as a click of the mouse. ");
        partTwo.append("If you want to keep track of your progress and points, make sure to login! ");
        partTwo.append("This Clicker will simply load a tweet and ask you to click on the category that best describes the tweet.<br/>");

        if(labelJsonModelSet != null ){
            partTwo.append("<table>");
            for(NominalLabelJsonModel labelJsonModel : labelJsonModelSet) {
                partTwo.append("<tr><td>");
                partTwo.append("<b>" + labelJsonModel.getName() + "</b></td>") ;
                if(!labelJsonModel.getDescription().isEmpty()){
                    partTwo.append("<td>: " + labelJsonModel.getDescription() + "</td>") ;
                }
                partTwo.append("</tr>");
            }
            partTwo.append("</table>");

        }
        partTwo.append("<br/><br/>");
        partTwo.append("Note that these tweets come directly from twitter and may on rare occasions include disturbing content. Only start clicking if you understand this and still wish to volunteer.");
        partTwo.append("<br/><br/>");
        partTwo.append("Thank you!");

        return buildTutorialTemplate(clientApp, partOne.toString(), partTwo.toString());
    }

    private String getAppInfo(ClientApp clientApp){
        Communicator pybossaCommunicator = new Communicator();
        String url = clientApp.getClient().getHostURL()  + "/app/" + clientApp.getPlatformAppID();

        String response = pybossaCommunicator.sendGet(url);

        return response;
    }

    private void sendToPybossa(String jsonData,ClientApp clientApp){
        logger.info("sendToPybossa jsonData : " + jsonData);
        Communicator pybossaCommunicator = new Communicator();
        String url = clientApp.getClient().getHostURL()  + "/app/" + clientApp.getPlatformAppID() + "?api_key=" + clientApp.getClient().getHostAPIKey();
        logger.info("sendToPybossa url:" + url);
        int responseCode = pybossaCommunicator.sendPut(jsonData, url);
        logger.info("sendToPybossa response:" + responseCode);
    }


}
