package qa.qcri.aidr.trainer.api.service.impl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

    @Autowired
    CustomUITemplateDao customUITemplateDao;

    @Autowired
    CrisisService crisisService;

    @Autowired
    ClientAppService clientAppService;

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

        List<CustomUITemplate> cList ;
        if(customUIType== CodeLookUp.CLASSIFIER_WELCOME_PAGE){
            cList =  customUITemplateDao.getTemplateByAttributeAndType(crisisID,attributeID, StatusCodeType.CUSTOM_UI_UPDATE_REQUEST, customUIType);
            if(cList.size() > 0){
                ClientApp clientApp = clientAppService.getClientAppByCrisisAndAttribute(crisisID,  attributeID);
                CustomUITemplate c = cList.get(0);
                String welcomePage = buildWelcomePage(c.getTemplateValue());
                //ClientApp clientApp, int customUIType, String updateTemplateValue
                String jsonData = this.assembleToPybossaFormat(clientApp, customUIType, welcomePage);
                this.sendToPybossa(jsonData, clientApp );
            }

        }

        if(customUIType== CodeLookUp.CLASSIFIER_TUTORIAL_ONE || customUIType== CodeLookUp.CLASSIFIER_TUTORIAL_TWO){
            cList =  customUITemplateDao.getTemplateByAttribute( crisisID,  attributeID);
            if(cList.size() > 0){
                ClientApp clientApp = clientAppService.getClientAppByCrisisAndAttribute(crisisID,  attributeID);
                String tutorialOne = "";
                String tutorialTwo = "";
                for(CustomUITemplate c : cList){
                    if(c.getTemplateType().equals(CodeLookUp.CLASSIFIER_TUTORIAL_ONE) ){
                        tutorialOne = c.getTemplateValue();
                    }

                    if(c.getTemplateType().equals(CodeLookUp.CLASSIFIER_TUTORIAL_TWO) ){
                        tutorialTwo = c.getTemplateValue();
                    }
                }

               String tutorialPage =  buildTutorialTemplate(clientApp, tutorialOne, tutorialTwo);
               String jsonData = this.assembleToPybossaFormat(clientApp,customUIType,  tutorialPage);
               this.sendToPybossa(jsonData, clientApp );
            }
        }

        if(customUIType== CodeLookUp.CLASSIFIER_SKIN){
            cList =  customUITemplateDao.getTemplateByAttribute( crisisID,  attributeID);
            if(cList.size() > 0){
                ClientApp clientApp = clientAppService.getClientAppByCrisisAndAttribute(crisisID,  attributeID);
                Set<ModelFamily> families = crisisService.findByCrisisID(crisisID).getModelFamilySet();
                for(ModelFamily family : families){
                    if(family.getNominalAttributeID().equals(attributeID)){
                        NominalAttribute nom = family.getNominalAttribute();
                        String skinUpdate = buildAppSkin(clientApp, nom, skinType);
                        String jsonData = this.assembleToPybossaFormat(clientApp,customUIType,  skinUpdate);
                        this.sendToPybossa(jsonData, clientApp );
                    }
                }


            }
        }


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
        while(itr.hasNext()){

            NominalLabel featureJsonObj = (NominalLabel) itr.next();
            String labelName = featureJsonObj.getName()  ;
            String lableCode = featureJsonObj.getNorminalLabelCode() ;
            String description = featureJsonObj.getDescription();
            Long norminalLabelID = featureJsonObj.getNorminalLabelID();

            if(skinType == CodeLookUp.IPHONE_SKIN) {
                    displayLabel.append("<<li id=")  ;
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

    public String assembleToPybossaFormat(ClientApp clientApp, int customUIType, String updateTemplateValue){
        JSONObject app = new JSONObject();

        if(CodeLookUp.CLASSIFIER_SKIN.equals(customUIType))  {
            app.put("task_presenter", updateTemplateValue);
        }

        if(customUIType== CodeLookUp.CLASSIFIER_TUTORIAL_ONE || customUIType== CodeLookUp.CLASSIFIER_TUTORIAL_TWO){
            app.put("tutorial", updateTemplateValue);
        }

        JSONObject app2 = new JSONObject();

        if(app.size() > 0){
            app2.put("info", app );
        }

        if(CodeLookUp.CLASSIFIER_WELCOME_PAGE.equals(customUIType))  {
            app2.put("long_description", updateTemplateValue);
        }


        app2.put("name", clientApp.getName());
        app2.put("short_name", clientApp.getShortName());
        app2.put("id", clientApp.getPlatformAppID());

        //long_description
        return  app2.toJSONString();
    }

    private void sendToPybossa(String jsonData,ClientApp clientApp){
        Communicator pybossaCommunicator = new Communicator();
        String url = clientApp.getClient().getHostURL()  + "/app/" + clientApp.getPlatformAppID() + "?api_key=" + clientApp.getClient().getHostAPIKey();

        int responseCode = pybossaCommunicator.sendPut(jsonData, url);

    }

}
