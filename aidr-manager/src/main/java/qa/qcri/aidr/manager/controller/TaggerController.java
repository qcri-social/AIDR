package qa.qcri.aidr.manager.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.common.values.DownloadType;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;
import qa.qcri.aidr.manager.dto.CrisisRequest;
import qa.qcri.aidr.manager.dto.DateHistory;
import qa.qcri.aidr.manager.dto.ModelHistoryWrapper;
import qa.qcri.aidr.manager.dto.TaggerAttribute;
import qa.qcri.aidr.manager.dto.TaggerCrisis;
import qa.qcri.aidr.manager.dto.TaggerCrisisExist;
import qa.qcri.aidr.manager.dto.TaggerCrisisRequest;
import qa.qcri.aidr.manager.dto.TaggerCrisisType;
import qa.qcri.aidr.manager.dto.TaggerLabel;
import qa.qcri.aidr.manager.dto.TaggerLabelRequest;
import qa.qcri.aidr.manager.dto.TaggerModel;
import qa.qcri.aidr.manager.dto.TaggerModelFamily;
import qa.qcri.aidr.manager.dto.TaggerModelFamilyCollection;
import qa.qcri.aidr.manager.dto.TaggerResponseWrapper;
import qa.qcri.aidr.manager.dto.TaggerUser;
import qa.qcri.aidr.manager.dto.TaggerUserRequest;
import qa.qcri.aidr.manager.dto.TaskAnswer;
import qa.qcri.aidr.manager.dto.TaskAnswerRequest;
import qa.qcri.aidr.manager.dto.TaskInfo;
import qa.qcri.aidr.manager.dto.UpdateCrisisDTO;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.hibernateEntities.UserEntity;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.TaggerService;


@Controller
@RequestMapping("protected/tagger")
public class TaggerController extends BaseController {

	private Logger logger = Logger.getLogger(TaggerController.class);
	private ErrorLog elog = new ErrorLog();

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
		logger.info("Getting all CrisisTypes from Tagger");
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
			logger.info("userID = " + taggerUserId + ", name = " + userName);
			if (taggerUserId != null) {
				TaggerCrisisRequest crisis = transformCrisesRequestToTaggerCrises(crisisRequest, taggerUserId);
				logger.info("After transformation:, crisis = " + crisis.getCode() + ": " + crisis.getCrisisType() + ":" + crisis.getName());
				String response = taggerService.createNewCrises(crisis);
				logger.info("createNewCrises: " + response);
				if ("SUCCESS".equals(response)){
					logger.info("Returning : " + response);
					return getUIWrapper(true);
				} else {
					logger.info("Returning : " + response);
					return getUIWrapper(false, response);
				}
			} else {
				return getUIWrapper(false, "Unable to create new user in predict DB");
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
		logger.info("Add Attribute To Crises, received request for crisisID:" + crisesId + ", attributeID = " + attributeId + ", isActive = " + isActive);
		try {
			TaggerModelFamily modelFamily = transformCrisesIdAndAttributeIdToTaggerModelFamily(crisesId, attributeId, isActive);
			if (modelFamily != null) {
				logger.info("Created modelFamily: crisis = " + modelFamily.getCrisis().getCrisisID() + ", attributeId = " + modelFamily.getNominalAttribute().getNominalAttributeID());
			} else {
				logger.info("Something wrong, created modelFamily = null!!!");
			}
			Integer modelFamilyId = taggerService.addAttributeToCrisis(modelFamily);
			if (modelFamilyId != null) {
				logger.info("success in adding attribute to crisis for modelFamily : " + modelFamilyId);
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

			// update collection crisisType to keep data consistent
			AidrCollection collection = collectionService.findByCode(dto.getCode());
			collection.setCrisisType(dto.getCrisisTypeID());
			collectionService.update(collection);

			return getUIWrapper(updatedCrisis != null);
		}catch(Exception e){
			logger.error("Error while Updating Crisis in Tagger", e);
			return getUIWrapper(false);
		}
	}

	@RequestMapping(value = "/getModelsForCrisis.action", method = {RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> getModelsForCrisis(Integer id) {
		logger.info("Getting Attributes For Crisis ID " + id);
		try {
			List<TaggerModel> result = taggerService.getModelsForCrisis(id);
			logger.info("Fetched result data size = " + (result != null ? result.size() : null));
			if (result != null) {
				for (int i = 0;i < result.size();i++) {
					logger.info("looking at fetched model family: " + i);
					Map<String, Object> countResult = getTrainingDataCountByModelIdAndCrisisId(result.get(i).getModelFamilyID(), id);
					logger.info("fetched count: " + (countResult != null ? countResult.get("data") : null));
					if (countResult != null) {
						try {
							Integer value = (Integer) countResult.get("data");
							logger.info("cast long value: " + value);
							result.get(i).setTrainingExamples(value.longValue());
						} catch (Exception e) {
							logger.error(elog.toStringException(e));
						}
					}
					logger.info("For model family id: " + result.get(i).getModelFamilyID() + ", set human labeled count = " + result.get(i).getTrainingExamples());
				}
			}
			//return getUIWrapper(taggerService.getModelsForCrisis(id), true);
			return getUIWrapper(result, true);
		} catch (AidrException e) {
			logger.error(elog.toStringException(e));
			return getUIWrapper(false, e.getMessage());
		}
	}

	@RequestMapping(value = "/getRetrainThreshold.action", method = {RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> getRetrainThreshold() {
		logger.info("Getting Attributes For Crises");
		try {
			return getUIWrapper(taggerService.getRetainingThreshold(), true);
		} catch (AidrException e) {
			logger.error(e.getMessage(), e);
			return getUIWrapper(false, e.getMessage());
		}
	}

	@RequestMapping(value = "/getAllLabelsForModel.action", method = {RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> getAllLabelsForModel(Integer id, String code) {
		logger.info("Getting All Labels For Model = " + id + ", and crisis = " + code);
		try {
			return getUIWrapper(taggerService.getAllLabelsForModel(id, code), true);
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
			logger.info("For crisis ID " + crisisId + ", model family ID " + modelFamilyId + ", Returned response: " + response);
		} catch (AidrException e) {
			logger.error(e.getMessage(), e);
			return getUIWrapper(false, e.getMessage());
		}
		Integer total = 0;
		if (response != null) {
			total = response.get(0).getTotalRows();
		}
		logger.info("Returning for crisis ID " + crisisId + ", model family ID " + modelFamilyId + " total count = " + total + ", response data: " + response);
		return getUIWrapper(response, true, Long.valueOf(total), null);
	}

	@RequestMapping(value = "/getTrainingDataCountByModelIdAndCrisisId.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getTrainingDataCountByModelIdAndCrisisId(
			@RequestParam Integer modelFamilyId,
			@RequestParam Integer crisisId) {
		Integer start = 0;
		Integer limit = 20;
		List<TrainingDataDTO> response;
		logger.info("request received for crisis ID " + crisisId + ", model family ID " + modelFamilyId);
		try {
			response = taggerService.getTrainingDataByModelIdAndCrisisId(modelFamilyId, crisisId, start, limit, "", "");
			logger.info("received response back: " + response);
		} catch (AidrException e) {
			logger.error(elog.toStringException(e));
			return getUIWrapper(new Integer(0), false);
		}
		Integer total = 0;
		if (response != null && !response.isEmpty()) {
			try {
				total = response.get(0).getTotalRows();
			} catch (Exception e) {
				logger.error(elog.toStringException(e));
			}
		}
		logger.info("Returning for crisis ID " + crisisId + ", model family ID " + modelFamilyId + ", human labeled count = " + total);
		return getUIWrapper(total, true);
	}

	@RequestMapping(value = "/crisis-exists.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> crisisExists(@RequestParam String code) throws Exception {
		TaggerCrisisExist taggerCrisisExist = null;
		logger.info("Received request for crisis code: " + code);
		try {
			taggerCrisisExist = taggerService.isCrisesExist(code);
		} catch (AidrException e) {
			e.printStackTrace();
			return getUIWrapper(false);
		}
		if (taggerCrisisExist != null && taggerCrisisExist.getCrisisId() != null && taggerCrisisExist.getCrisisId() != 0){
			logger.info("Classifier already exists for the code: \"" + code + "\"");
			return getUIWrapper(true, true);
		} else {
			logger.info("Classifier doesn't exists yet for the code: \"" + code + "\"");
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

	@RequestMapping(value = "/loadLatestTweets.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> loadLatestTweets(@RequestParam String code, @RequestParam String constraints) throws Exception {
		String result = "";
		try {
			result = taggerService.loadLatestTweets(code, constraints);
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
			} else if ("persister".equals(service)) {
				result = taggerService.pingPersister();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		return getUIWrapper(result, true);
	}

	@RequestMapping(value = "/getAttributesAndLabelsByCrisisId.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getAttributesAndLabelsByCrisisId(@RequestParam Integer id) throws Exception {
		String result = "";
		try {
			result = taggerService.getAttributesAndLabelsByCrisisId(id);
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		return getUIWrapper(result,true);
	}

	// Added by koushik
	@RequestMapping(value = "/taggerGenerateCSVLink.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> generateCSVLink(@RequestParam String code) throws Exception {
		Map<String, Object> result = null;
		try {
			result = taggerService.generateCSVLink(code);
			if (result != null && result.get("url") != null) {
				return getUIWrapper(result.get("url"),true);
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
	}

	// Added by koushik
	@RequestMapping(value = "/taggerGenerateTweetIdsLink.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> generateTweetIdsLink(@RequestParam String code) throws Exception {
		System.out.println("[Controller generateTweetIdsLink] Received request for code: " + code);
		Map<String, Object> result = null;
		try {
			result = taggerService.generateTweetIdsLink(code);
			if (result != null && result.get("url") != null) {
				System.out.println("Returning success fo collection: " +  code + ", response: " + result);
				return getUIWrapper(result.get("url"),true, null, (String)result.get("message"));
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		//System.out.println("[Controller generateTweetIdsLink] Returning success: " + result);
		//return getUIWrapper(result,true);
	}

	private TaggerCrisisRequest transformCrisesRequestToTaggerCrises (CrisisRequest request, Integer taggerUserId) throws Exception{
		TaggerCrisisType crisisType = new TaggerCrisisType(request.getCrisisTypeID());
		TaggerUserRequest taggerUser = new TaggerUserRequest(taggerUserId);
		return new TaggerCrisisRequest(request.getCode(), request.getName(), crisisType, taggerUser);
	}

	private TaggerModelFamily transformCrisesIdAndAttributeIdToTaggerModelFamily (Integer crisesId, Integer attributeId, Boolean isActive) throws Exception{
		TaggerCrisis crisis = new TaggerCrisis(crisesId);
		TaggerAttribute nominalAttribute = new TaggerAttribute(attributeId);
		logger.info("crisis = " + crisis.getCrisisID() + ", attribute = " + nominalAttribute.getNominalAttributeID());
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
		taskInfo.setAttributeID(taskAnswerRequest.getAttributeID());

		taskAnswer.setDateHistory(dateHistory);
		taskAnswer.setInfo(taskInfo);

		result.add(taskAnswer);
		return result;
	}

	@RequestMapping(value = "/taggerGenerateJSONLink.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> generateJSONLink(@RequestParam String code,
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType) throws Exception {
		Map<String, Object> result = null;
		try {
			result = taggerService.generateJSONLink(code, jsonType);
			if (result != null && result.get("url") != null) {
				return getUIWrapper(result.get("url"),true);
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		//return getUIWrapper(result,true);
	}

	// Added by koushik
	@RequestMapping(value = "/taggerGenerateJsonTweetIdsLink.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> generateJsonTweetIdsLink(@RequestParam String code,
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType) throws Exception {
		System.out.println("[Controller generateTweetIdsLink] Received request for code: " + code);
		Map<String, Object> result = null;
		try {
			result = taggerService.generateJsonTweetIdsLink(code, jsonType);
			if (result != null && result.get("url") != null) {
				System.out.println("Returning success fo collection: " +  code + ", response: " + result);
				return getUIWrapper(result.get("url"),true, null, (String)result.get("message"));
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		//System.out.println("[Controller generateTweetIdsLink] Returning success: " + result);
		//return getUIWrapper(result,true);
	}


	// Added by koushik
	@RequestMapping(value = "/taggerGenerateCSVFilteredLink.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> generateCSVFilteredLink(@RequestParam String code,
			String queryString) throws Exception {
		Map<String, Object> result = null;
		try {
			String userName = getAuthenticatedUserName();
			if (null == userName) userName = "System";
			logger.info("Received request for download from user = " + userName);
			result = taggerService.generateCSVFilteredLink(code, queryString, userName);
			if (result != null && result.get("url") != null) {
				return getUIWrapper(result.get("url"),true);
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
	}

	// Added by koushik
	@RequestMapping(value = "/taggerGenerateTweetIdsFilteredLink.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> generateTweetIdsFilteredLink(@RequestParam String code,
			String queryString) throws Exception {
		System.out.println("[Controller generateTweetIdsLink] Received request for code: " + code);
		Map<String, Object> result = null;
		try {
			String userName = getAuthenticatedUserName();
			if (null == userName) userName = "System";
			
			result = taggerService.generateTweetIdsFilteredLink(code, queryString, userName);
			if (result != null && result.get("url") != null) {
				System.out.println("Returning success fo collection: " +  code + ", response: " + result);
				return getUIWrapper(result.get("url"),true, null, (String)result.get("message"));
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		//System.out.println("[Controller generateTweetIdsLink] Returning success: " + result);
		//return getUIWrapper(result,true);
	}


	@RequestMapping(value = "/taggerGenerateJSONFilteredLink.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> generateJSONFilteredLink(@RequestParam String code,
			String queryString,
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType) throws Exception {
		Map<String, Object> result = null;
		try {
			String userName = getAuthenticatedUserName();
			if (null == userName) userName = "System";
			
			result = taggerService.generateJSONFilteredLink(code, queryString, jsonType, userName);
			if (result != null && result.get("url") != null) {
				return getUIWrapper(result.get("url"),true);
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
		//return getUIWrapper(result,true);
	}

	// Added by koushik
	@RequestMapping(value = "/taggerGenerateJsonTweetIdsFilteredLink.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> generateJsonTweetIdsFilteredLink(@RequestParam String code,
			String queryString,
			@DefaultValue(DownloadType.TEXT_JSON) @QueryParam("jsonType") String jsonType) throws Exception {
		System.out.println("[Controller generateTweetIdsLink] Received request for code: " + code);
		Map<String, Object> result = null;
		try {
			String userName = getAuthenticatedUserName();
			if (null == userName) userName = "System";
			
			result = taggerService.generateJsonTweetIdsFilteredLink(code, queryString, jsonType, userName);
			if (result != null && result.get("url") != null) {
				System.out.println("Returning success fo collection: " +  code + ", response: " + result);
				return getUIWrapper(result.get("url"),true, null, (String)result.get("message"));
			} else {
				return getUIWrapper(false, "Something wrong - no file generated!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}
	}

	@RequestMapping(value = "/updateMobilePush.action", method = { RequestMethod.POST ,RequestMethod.GET })
	@ResponseBody
	public Map<String,Object> updateMobilePushStatus(AidrCollection collection) throws Exception {
		System.out.println("[updateMobilePushStatus: " + collection.getCode());
		String result = "";
		try {
			TaggerCrisis tagCrisis = taggerService.getCrisesByCode(collection.getCode());
			List<TaggerModelFamilyCollection> taggerModelFamilies =  tagCrisis.getModelFamilyCollection();
			/// all clientapp based on crisisID should be enable to push to mobile app.

		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "System is down or under maintenance. For further inquiries please contact admin.");
		}

		return getUIWrapper(result,true);
	}

	@RequestMapping(value = "/getHumanLabeledDocumentsByCrisisID.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getHumanLabeledDocumentsByCrisisID(Long crisisID, Integer count) throws Exception {
		try {
			TaggerResponseWrapper labeledDataList = taggerService.getHumanLabeledDocumentsByCrisisID(crisisID, count);
			return getUIWrapper(labeledDataList, true);
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "Error in fetching human labeled documents");
		}
	}

	@RequestMapping(value = "/getHumanLabeledDocumentsByCrisisCode.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getHumanLabeledDocumentsByCrisisCode(String crisisCode, Integer count) throws Exception {
		try {
			TaggerResponseWrapper labeledDataList = taggerService.getHumanLabeledDocumentsByCrisisCode(crisisCode, count);
			return getUIWrapper(labeledDataList, true);
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "Error in fetching human labeled documents");
		}
	}

	@RequestMapping(value = "/getHumanLabeledDocumentsByCrisisIDUserID.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getHumanLabeledDocumentsByCrisisIDUserID(Long crisisID, Long userID, Integer count) throws Exception {
		try {
			TaggerResponseWrapper labeledDataList = taggerService.getHumanLabeledDocumentsByCrisisIDUserID(crisisID, userID, count);
			return getUIWrapper(labeledDataList, true);
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "Error in fetching human labeled documents");
		}
	}

	@RequestMapping(value = "/getHumanLabeledDocumentsByCrisisIDUserName.action", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getHumanLabeledDocumentsByCrisisIDUserName(Long crisisID, String userName, Integer count) throws Exception {
		try {
			TaggerResponseWrapper labeledDataList = taggerService.getHumanLabeledDocumentsByCrisisIDUserName(crisisID, userName, count);
			return getUIWrapper(labeledDataList, true);
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "Error in fetching human labeled documents");
		}
	}

	@RequestMapping(value = "/downloadHumanLabeledDocuments.action", method = RequestMethod.POST)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ResponseBody
	public Map<String,Object> downloadHumanLabeledDocuments(String queryString, 
			@RequestParam(value = "crisisCode", required = true) String crisisCode, 
			@RequestParam(value = "count", defaultValue = "-1") Integer count,
			@RequestParam(value = "fileType", defaultValue = DownloadType.TEXT_JSON) String fileType, 
			@RequestParam(value = "contentType", defaultValue = DownloadType.FULL_TWEETS) String contentType) throws Exception {
		logger.info("Received request: crisisCode = " + crisisCode + ", count = " + count + ", fileType = " + fileType
				+ ", contentType = " + contentType + "\nquery String = " + queryString);
		try {
			String userName = getAuthenticatedUserName();
			if (null == userName) userName = "System";
			
			if (null == count) {
				count = -1;
			}
			Map<String, Object> downloadLink = taggerService.downloadHumanLabeledDocumentsByCrisisUserName(queryString, crisisCode, userName, count, fileType, contentType);
			if (downloadLink.get("fileName") != null && downloadLink.get("total") != null) {
				return getUIWrapper(downloadLink, true, new Long((Integer)downloadLink.get("total")), null);
			} else {
				return getUIWrapper(downloadLink, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "Error in getting download link for human labeled documents");
		}
	}
	
	@RequestMapping(value = "/updateMicromapperEnabled.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateMicromapperEnabled(@RequestParam Boolean isMicromapperEnabled, @RequestParam String code ) throws Exception {
		logger.info("In update micromapperEnabled for collection, code = " + code);
		Map<String, Object> result = null;
		try {
			result = taggerService.updateMicromapperEnabled(code, isMicromapperEnabled);
			if (result != null && result.get("isMicromapperEnabled") != null) {
				return getUIWrapper(null,true);
			} else {
				return getUIWrapper(false, "Something wrong while updating isMicromapperEnabled");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "Unable to update micromapperEnabled for collection, code = " + code);
		}
	}

	/**
	 * @param url
	 * @param mailType Type of mail (issues/suggestions)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendEmailService.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> sendEmail(@RequestParam String url, @RequestParam String mailType, @RequestParam String description) throws Exception {
		logger.info("In sending email");
		UserEntity userEntity = getAuthenticatedUser();
		userEntity.getUserName();
		String body = "User:-"+userEntity.getUserName() + "\nURL:-"+url +"\nRequestHeader:-"+description;
		String subject = mailType;
		Boolean result = false;
		try{
			result = taggerService.sendMailService(subject,body);
			if (result) {
				return getUIWrapper(null,true);
			} else {
				return getUIWrapper(false, "Sending Email Failed");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return getUIWrapper(false, "Sending Email Failed");
		}
	}
}