package qa.qcri.aidr.manager.controller;

import qa.qcri.aidr.manager.dto.*;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.TaggerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("protected/tagger")
public class TaggerController extends BaseController {

    private Logger logger = Logger.getLogger(TaggerController.class);

    @Autowired
    private TaggerService taggerService;

    @Autowired
    private CollectionService collectionService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(value = "/getAllCrisisTypes.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getAllCrisis() {
        logger.info("Getting all Crisis from Tagger");
        try {
            return getUIWrapper(taggerService.getAllCrisisTypes(), true);
        } catch (AidrException e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/getCrisesByUserId.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getCrisesByUserId() {
        logger.info("Getting crises from Tagger by User");
        try {
            String userName = getAuthenticatedUserName();
            Integer taggerUserId = taggerService.isUserExistsByUsername(userName);
            if (taggerUserId == null) {
                TaggerUser taggerUser = new TaggerUser(userName, "normal");
                taggerUserId = taggerService.addNewUser(taggerUser);
            }
            if (taggerUserId != null) {
                return getUIWrapper(taggerService.getCrisesByUserId(taggerUserId), true);
            } else {
                return getUIWrapper(false, "Error while getting all crisis for user in Tagger");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/createCrises.action", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> createCrises(CrisisRequest crisisRequest) {
        logger.info("Creating new crises in Tagger");
        try {
            String userName = getAuthenticatedUserName();
            Integer taggerUserId = taggerService.isUserExistsByUsername(userName);
            if (taggerUserId == null) {
                TaggerUser taggerUser = new TaggerUser(userName, "normal");
                taggerUserId = taggerService.addNewUser(taggerUser);
            }

            TaggerCrisisRequest crisis = transformCrisesRequestToTaggerCrises(crisisRequest, taggerUserId);
            String response = taggerService.createNewCrises(crisis);
            if ("SUCCESS".equals(response)){
                return getUIWrapper(true);
            } else {
                return getUIWrapper(false, response);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/getAttributesForCrises.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getAttributesForCrises(Integer id) {
        logger.info("Getting Attributes For Crises");
        try {
            String userName = getAuthenticatedUserName();
            Integer taggerUserId = taggerService.isUserExistsByUsername(userName);
            return getUIWrapper(taggerService.getAttributesForCrises(id, taggerUserId), true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/addAttributeToCrisis.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> addAttributeToCrisis(Integer crisesId, Integer attributeId, Boolean isActive) {
        logger.info("Add Attribute To Crises");
        try {
            TaggerModelFamily modelFamily = transformCrisesIdAndAttributeIdToTaggerModelFamily(crisesId, attributeId, isActive);
            Integer modelFamilyId = taggerService.addAttributeToCrisis(modelFamily);
            if (modelFamilyId != null) {
                return getUIWrapper(modelFamilyId, true);
            } else {
                return getUIWrapper("Error while adding attribute to crises", false);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/updateCrisis.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> update(UpdateCrisisDTO dto) throws Exception {
        logger.info("Updating Crisis in Tagger having id " + dto.getCrisisID());
        TaggerCrisis crisis = transformCrisisDTOToCrisis(dto);
        try{
            TaggerCrisis updatedCrisis = taggerService.updateCode(crisis);
            return getUIWrapper(updatedCrisis != null);
        }catch(Exception e){
            logger.error("Error while Updating Crisis in Tagger", e);
            return getUIWrapper(false);
        }
    }

    @RequestMapping(value = "/getModelsForCrisis.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getModelsForCrisis(Integer id) {
        logger.info("Getting Attributes For Crises");
        try {
            return getUIWrapper(taggerService.getModelsForCrisis(id), true);
        } catch (AidrException e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/getAllLabelsForModel.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getAllLabelsForModel(Integer id) {
        logger.info("Getting All Labels For Model");
        try {
            return getUIWrapper(taggerService.getAllLabelsForModel(id), true);
        } catch (AidrException e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/createAttribute.action", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> createAttribute(TaggerAttribute attribute) {
        logger.info("Creating new attribute in Tagger");
        try {
            String userName = getAuthenticatedUserName();
            Integer taggerUserId = taggerService.isUserExistsByUsername(userName);
            TaggerUser taggerUser = new TaggerUser(taggerUserId);
            attribute.setUsers(taggerUser);

            TaggerAttribute response = taggerService.createNewAttribute(attribute);
            if (response != null){
                return getUIWrapper(response, true);
            } else {
                return getUIWrapper(false, "Error while creating new attribute in Tagger");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/getAttributeInfo.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getAttributeInfo(@RequestParam Integer id) {
        logger.info("Get attribute by Id");
        try {
            TaggerAttribute response = taggerService.getAttributeInfo(id);
            if (response != null){
                return getUIWrapper(response, true);
            } else {
                return getUIWrapper(false, "Error while getting attribute from Tagger");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/deleteAttribute.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> deleteAttribute(@RequestParam Integer id) {
        logger.info("Delete attribute by Id");
        try {
            boolean success = taggerService.deleteAttribute(id);
            if (success){
                return getUIWrapper(true, "Attribute was successful deleted");
            } else {
                return getUIWrapper(false, "Error while deleting attribute in Tagger");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/removeAttributeFromCrises.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> removeAttributeFromCrises(@RequestParam Integer id) {
        logger.info("Remove classifier from crises by modelFamilyID");
        try {
            boolean success = taggerService.removeAttributeFromCrises(id);
            if (success){
                return getUIWrapper(true, "Classifier was successful removed from crisis");
            } else {
                return getUIWrapper(false, "Error while remove classifier from crises in Tagger");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/updateAttribute.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateAttribute(@RequestParam Integer attributeID, @RequestParam String attributeName) throws Exception {
        logger.info("Updating Attribute in Tagger having id " + attributeID);
        try{
            TaggerAttribute response = taggerService.getAttributeInfo(attributeID);
            TaggerAttribute updatedAttribute;
            if (response != null && attributeName != null){
                response.setName(attributeName);
                response.setNominalLabelCollection(null);
                updatedAttribute = taggerService.updateAttribute(response);
            } else {
                return getUIWrapper(false, "Error while updating attribute in Tagger");
            }
            return getUIWrapper(updatedAttribute != null);
        }catch(Exception e){
            logger.error("Error while updating attribute in Tagger", e);
            return getUIWrapper(false);
        }
    }

    @RequestMapping(value = "/updateLabel.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateLabel(@RequestParam Integer labelID,
                                          @RequestParam String labelName,
                                          @RequestParam String labelDescription,
                                          @RequestParam Integer attributeID) throws Exception {
        logger.info("Updating Label in Tagger having id " + labelID);
        try{
            TaggerLabel response = taggerService.getLabelInfo(labelID);
            TaggerLabel updatedLabel;
            if (response != null && (labelName != null || labelDescription != null)){
                TaggerLabelRequest dto = new TaggerLabelRequest();
                dto.setName(labelName);
                dto.setDescription(labelDescription);
                dto.setNominalAttributeID(attributeID);
                dto.setNominalLabelCode(response.getNominalLabelCode());
                dto.setNominalLabelID(response.getNominalLabelID());
                updatedLabel = taggerService.updateLabel(dto);
            } else {
                return getUIWrapper(false, "Error while updating label in Tagger");
            }
            return getUIWrapper(updatedLabel != null);
        }catch(Exception e){
            logger.error("Error while updating label in Tagger", e);
            return getUIWrapper(false);
        }
    }

    @RequestMapping(value = "/createLabel.action", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> createLabel(TaggerLabelRequest labelRequest) {
        logger.info("Creating new label in Tagger");
        try {
            TaggerLabel response = taggerService.createNewLabel(labelRequest);
            if (response != null){
                return getUIWrapper(response, true);
            } else {
                return getUIWrapper(false, "Error while creating new label in Tagger");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/attribute-exists.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> attributeExists(@RequestParam String code) throws Exception {

        TaggerAttribute attribute = taggerService.attributeExists(code);
        if (attribute != null && attribute.getNominalAttributeID() != null && attribute.getNominalAttributeID() != 0){
            return getUIWrapper(true, true);
        } else {
            return getUIWrapper(false, true);
        }
    }

    @RequestMapping(value = "/getTrainingDataByModelIdAndCrisisId.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getTrainingDataByModelIdAndCrisisId(@RequestParam Integer start,
                                                                   @RequestParam Integer limit,
                                                                   @RequestParam Integer modelFamilyId,
                                                                   @RequestParam Integer crisisId,
                                                                   @RequestParam (value = "sortColumn", required = false, defaultValue = "") String sortColumn,
                                                                   @RequestParam (value = "sortDirection", required = false, defaultValue = "") String sortDirection) throws Exception {
        if (modelFamilyId == null || crisisId == null ) {
            logger.error("Error while Getting training data for Crisis and Model. Model ID or Crisis ID is empty");
            return getUIWrapper(false);
        }
        start = (start != null) ? start : 0;
        limit = (limit != null) ? limit : 20;
        List<TrainingDataDTO> response;
        try {
            response = taggerService.getTrainingDataByModelIdAndCrisisId(modelFamilyId, crisisId, start, limit, sortColumn, sortDirection);
        } catch (AidrException e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
        Integer total = 0;
        if (response != null) {
            total = response.get(0).getTotalRows();
        }
        return getUIWrapper(response, true, Long.valueOf(total), null);
    }

    @RequestMapping(value = "/crisis-exists.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> crisisExists(@RequestParam String code) throws Exception {
        TaggerCrisisExist taggerCrisisExist = null;
        try {
            taggerCrisisExist = taggerService.isCrisesExist(code);
        } catch (AidrException e) {
            e.printStackTrace();
            return getUIWrapper(false);
        }
        if (taggerCrisisExist != null && taggerCrisisExist.getCrisisId() != null && taggerCrisisExist.getCrisisId() != 0){
            return getUIWrapper(true, true);
        } else {
            return getUIWrapper(false, true);
        }
    }

    @RequestMapping(value = "/getAssignableTask.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getAssignableTask(@RequestParam Integer id) throws Exception {
        try {
            logger.info("Get Assignable Task is started with crisis id: " + id);

            String userName = getAuthenticatedUserName();
            String sVar = taggerService.getAssignableTask(id, userName);

            logger.info("sVar : " + sVar);
            return getUIWrapper(sVar, true);
        } catch (AidrException e) {
            e.printStackTrace();
            return getUIWrapper(e.getMessage(), false);
        }
    }

    @RequestMapping(value = "/skipTask.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> skipTask(@RequestParam Integer id) throws Exception {
        try {
            logger.info("Skip Task for document id: " + id);

            String userName = getAuthenticatedUserName();
            String sVar = taggerService.skipTask(id, userName);

            logger.info("sVar : " + sVar);
            return getUIWrapper(sVar, true);
        } catch (AidrException e) {
            e.printStackTrace();
            return getUIWrapper(e.getMessage(), false);
        }
    }

    @RequestMapping(value = "/getTemplateStatus.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getTemplateStatus(@RequestParam String code) throws Exception {
        try {
            logger.info("Get Template Status for crisis with code: " + code);

            String sVar = taggerService.getTemplateStatus(code);

            logger.info("sVar : " + sVar);
            return getUIWrapper(sVar, true);
        } catch (AidrException e) {
            e.printStackTrace();
            return getUIWrapper(e.getMessage(), false);
        }
    }

    @RequestMapping(value = "/saveTaskAnswer.action", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveTaskAnswer(TaskAnswerRequest taskAnswerRequest) {
        logger.info("Saving TaskAnswer in AIDRCrowdsourcing");
        try {
            String userName = getAuthenticatedUserName();
            Integer taggerUserId = taggerService.isUserExistsByUsername(userName);
            if (taggerUserId == null) {
                logger.error("Saving TaskAnswer - can not find Tagger user by User name");
                return getUIWrapper(false, "Saving TaskAnswer - can not find Tagger user by User name");
            }
            List<TaskAnswer> taskAnswer = transformTaskAnswerRequestToTaskAnswer(taskAnswerRequest, taggerUserId);

            boolean result = taggerService.saveTaskAnswer(taskAnswer);
            return getUIWrapper(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/taggerGenerateCSVLink.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> taggerGenerateCSVLink(@RequestParam String code) throws Exception {
        String result = "";
        try {
            result = taggerService.generateCSVLink(code);
        } catch (Exception e) {
            e.printStackTrace();
            return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
        }
        return getUIWrapper(result,true);
    }

    @RequestMapping(value = "/taggerGenerateTweetIdsLink.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> taggerGenerateTweetIdsLink(@RequestParam String code) throws Exception {
        String result = "";
        try {
            result = taggerService.generateTweetIdsLink(code);
        } catch (Exception e) {
            e.printStackTrace();
            return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
        }
        return getUIWrapper(result,true);
    }

    @RequestMapping(value = "/modelHistory.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getModelHistoryByModelFamilyID(@RequestParam Integer start, @RequestParam Integer limit, @RequestParam Integer id) throws Exception {
        if (id == null) {
            logger.error("Error while Getting history records for Model by ModelFamilyId. ModelFamilyId is empty");
            return getUIWrapper(false);
        }

        start = (start != null) ? start : 0;
        limit = (limit != null) ? limit : 50;

        try {
            ModelHistoryWrapper result = taggerService.getModelHistoryByModelFamilyID(start, limit, id);
            if (result != null) {
                logger.info("Tagger returned " + result.getTotal() + " history records for model with modelFamilyID: " + id);
                return getUIWrapper(result.getModelHistoryWrapper(), result.getTotal());
            } else {
                return getUIWrapper(Collections.emptyList(), 0L);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
        }
    }

    @RequestMapping(value = "/deleteTrainingExample.action", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> deleteTrainingExample(@RequestParam Integer id) {
        logger.info("Delete Training Example by Id");
        try {
            boolean success = taggerService.deleteTrainingExample(id);
            if (success){
                return getUIWrapper(true, "Training Example was successful deleted");
            } else {
                return getUIWrapper(false, "Error while deleting Training Example in Tagger");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return getUIWrapper(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/pingService.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> pingService(@RequestParam String service) throws Exception {
        boolean result = false;
        try {
            if ("tagger".equals(service)) {
                result = taggerService.pingTagger();
            } else if ("collector".equals(service)) {
                result = collectionService.pingCollector();
            } else if ("trainer".equals(service)) {
                result = taggerService.pingTrainer();
            } else if ("AIDROutput".equals(service)) {
                result = taggerService.pingAIDROutput();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
        }
        return getUIWrapper(result, true);
    }

    private TaggerCrisisRequest transformCrisesRequestToTaggerCrises (CrisisRequest request, Integer taggerUserId) throws Exception{
        TaggerCrisisType crisisType = new TaggerCrisisType(request.getCrisisTypeID());
        TaggerUserRequest taggerUser = new TaggerUserRequest(taggerUserId);
        return new TaggerCrisisRequest(request.getCode(), request.getName(), crisisType, taggerUser);
    }

    private TaggerModelFamily transformCrisesIdAndAttributeIdToTaggerModelFamily (Integer crisesId, Integer attributeId, Boolean isActive) throws Exception{
        TaggerCrisis crisis = new TaggerCrisis(crisesId);
        TaggerAttribute nominalAttribute = new TaggerAttribute(attributeId);
        return new TaggerModelFamily(crisis, nominalAttribute, isActive);
    }

    private TaggerCrisis transformCrisisDTOToCrisis (UpdateCrisisDTO dto) throws Exception{
        TaggerCrisis crisis = new TaggerCrisis(dto.getCrisisID());
        if (dto.getCrisisTypeID() != null && dto.getCrisisTypeName() != null) {
            TaggerCrisisType crisisType = new TaggerCrisisType(dto.getCrisisTypeID(), dto.getCrisisTypeName());
            crisis.setCrisisType(crisisType);
        }
        return crisis;
    }

    private List<TaskAnswer> transformTaskAnswerRequestToTaskAnswer (TaskAnswerRequest taskAnswerRequest, Integer taggerUserId) {
        List<TaskAnswer> result = new ArrayList<TaskAnswer>();
        TaskAnswer taskAnswer = new TaskAnswer();
        taskAnswer.setUser_id(taggerUserId);

        DateHistory dateHistory = new DateHistory();
        dateHistory.setTaskcreated(taskAnswerRequest.getTaskcreated());
        dateHistory.setTaskcompleted(taskAnswerRequest.getTaskcompleted());
        dateHistory.setTaskpresented(taskAnswerRequest.getTaskcreated());
        dateHistory.setTaskpulled(taskAnswerRequest.getTaskcompleted());

        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setDocumentID(taskAnswerRequest.getDocumentID());
        taskInfo.setText("");
        taskInfo.setCategory(taskAnswerRequest.getCategory());
        taskInfo.setAidrID(taggerUserId);
        taskInfo.setTweetid("");
        taskInfo.setCrisisID(taskAnswerRequest.getCrisisID());

        taskAnswer.setDateHistory(dateHistory);
        taskAnswer.setInfo(taskInfo);

        result.add(taskAnswer);
        return result;
    }

}