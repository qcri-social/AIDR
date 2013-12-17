package qa.qcri.aidr.manager.service.impl;

import qa.qcri.aidr.manager.dto.*;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.service.TaggerService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.util.*;

@Service("taggerService")
public class TaggerServiceImpl implements TaggerService {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private Client client;

    @Value("${taggerMainUrl}")
    private String taggerMainUrl;

    @Value("${crowdsourcingAPIMainUrl}")
    private String crowdsourcingAPIMainUrl;

    @Value("${persisterMainUrl}")
    private String persisterMainUrl;

    @Value("${taggerPersisterMainUrl}")
    private String taggerPersisterMainUrl;

    public List<TaggerCrisisType> getAllCrisisTypes() throws AidrException{
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/crisisType/all");
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerAllCrisesTypesResponse crisesTypesResponse = objectMapper.readValue(jsonResponse, TaggerAllCrisesTypesResponse.class);
            if (crisesTypesResponse.getCrisisTypes() != null) {
                logger.info("Tagger returned " + crisesTypesResponse.getCrisisTypes().size() + " crises types");
            }

            return crisesTypesResponse.getCrisisTypes();
        } catch (Exception e) {
            throw new AidrException("Error while getting all crisis from Tagger", e);
        }
    }

    public List<TaggerCrisis> getCrisesByUserId(Integer userId) throws AidrException{
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/crisis?userID=" + userId);
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerAllCrisesResponse taggerAllCrisesResponse = objectMapper.readValue(jsonResponse, TaggerAllCrisesResponse.class);
            if (taggerAllCrisesResponse.getCrisises() != null) {
                logger.info("Tagger returned " + taggerAllCrisesResponse.getCrisises().size() + " crisis for user");
            }

            return taggerAllCrisesResponse.getCrisises();
        } catch (Exception e) {
            throw new AidrException("No collection is enabled for Tagger. Please enable tagger for one of your collections.", e);
        }
    }


    public String createNewCrises(TaggerCrisisRequest crisis) throws AidrException {
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/crisis");
            ObjectMapper objectMapper = new ObjectMapper();

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, objectMapper.writeValueAsString(crisis));

            return clientResponse.getEntity(String.class);
        } catch (Exception e) {
            throw new AidrException("Error while creating new crises in Tagger", e);
        }
    }

    public Collection<TaggerAttribute> getAttributesForCrises(Integer crisisID, Integer userId) throws AidrException{
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/attribute/crisis/all?exceptCrisis=" + crisisID);
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerCrisisAttributesResponse crisisAttributesResponse = objectMapper.readValue(jsonResponse, TaggerCrisisAttributesResponse.class);
            if (crisisAttributesResponse.getCrisisAttributes() != null) {
                logger.info("Tagger returned " + crisisAttributesResponse.getCrisisAttributes().size() + " attributes available for crises with ID " + crisisID);
            } else {
                return Collections.emptyList();
            }

            return convertTaggerCrisesAttributeToDTO(crisisAttributesResponse.getCrisisAttributes(), userId);
        } catch (Exception e) {
            throw new AidrException("Error while getting all attributes for crisis from Tagger", e);
        }
    }

    public TaggerCrisisExist isCrisesExist(String code) throws AidrException{
        try {
            WebResource webResource = client.resource(taggerMainUrl + "/crisis/code/" + code);
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerCrisisExist crisisExist = objectMapper.readValue(jsonResponse, TaggerCrisisExist.class);
            if (crisisExist.getCrisisId() != null) {
                logger.info("Crises with the code " + code + " already exist in Tagger.");
                return crisisExist;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new AidrException("Error while checking if crisis exist in Tagger", e);
        }
    }


    public Integer isUserExistsByUsername(String userName) throws AidrException {
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/user/" + userName);
            ObjectMapper objectMapper = new ObjectMapper();

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);

            String jsonResponse = clientResponse.getEntity(String.class);
            TaggerUser taggerUser = objectMapper.readValue(jsonResponse, TaggerUser.class);
            if (taggerUser != null && taggerUser.getUserID() != null) {
                logger.info("User with the user name " + userName + " already exist in Tagger and has ID: " + taggerUser.getUserID());
                return taggerUser.getUserID();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new AidrException("Error while checking if user exist in Tagger", e);
        }
    }

    public Integer addNewUser(TaggerUser taggerUser) throws AidrException {
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/user");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, objectMapper.writeValueAsString(taggerUser));

            String jsonResponse = clientResponse.getEntity(String.class);
            TaggerUser createdUser = objectMapper.readValue(jsonResponse, TaggerUser.class);
            if (createdUser != null && createdUser.getUserID() != null) {
                logger.info("User with ID " + createdUser.getUserID() + " was created in Tagger");
                return createdUser.getUserID();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new AidrException("Error while adding new user to Tagger", e);
        }
    }

    public Integer addAttributeToCrisis(TaggerModelFamily modelFamily) throws AidrException {
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/modelfamily");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, objectMapper.writeValueAsString(modelFamily));

            String jsonResponse = clientResponse.getEntity(String.class);
            TaggerModelFamily createdModelFamily = objectMapper.readValue(jsonResponse, TaggerModelFamily.class);
            if (createdModelFamily != null && createdModelFamily.getModelFamilyID() != null) {
                logger.info("Attribute was added to crises");
                return createdModelFamily.getModelFamilyID();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new AidrException("Error while adding attribute to crises", e);
        }
    }

    public TaggerCrisis getCrisesByCode(String code) throws AidrException{
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/crisis/by-code/" + code);
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerCrisis crisis = objectMapper.readValue(jsonResponse, TaggerCrisis.class);
            if (crisis != null) {
                logger.info("Tagger returned crisis with code" + crisis.getCode());
            }

            return crisis;
        } catch (Exception e) {
            throw new AidrException("Error while getting crisis by code from Tagger", e);
        }
    }

    public TaggerCrisis updateCode(TaggerCrisis crisis) throws AidrException{
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/crisis");
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .put(ClientResponse.class, objectMapper.writeValueAsString(crisis));
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerCrisis updatedCrisis = objectMapper.readValue(jsonResponse, TaggerCrisis.class);
            if (updatedCrisis != null) {
                logger.info("Crisis with id " + updatedCrisis.getCrisisID() + " was updated in Tagger");
            }

            return crisis;
        } catch (Exception e) {
            throw new AidrException("Error while getting crisis by code from Tagger", e);
        }
    }

    public List<TaggerModel> getModelsForCrisis(Integer crisisID) throws AidrException{
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/model/crisis/" + crisisID);
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerCrisisModelsResponse crisisModelsResponse = objectMapper.readValue(jsonResponse, TaggerCrisisModelsResponse.class);
            if (crisisModelsResponse.getModelWrapper() != null) {
                logger.info("Tagger returned " + crisisModelsResponse.getModelWrapper().size() + " models for crises with ID " + crisisID);
                return crisisModelsResponse.getModelWrapper();
            }
            return null;
        } catch (Exception e) {
            throw new AidrException("Error while getting all models for crisis from Tagger", e);
        }
    }

    public TaggerAttribute createNewAttribute(TaggerAttribute attribute) throws AidrException {
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/attribute");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, objectMapper.writeValueAsString(attribute));

            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerAttribute response = objectMapper.readValue(jsonResponse, TaggerAttribute.class);
            if (response != null) {
                logger.info("Attribute with ID " + response.getNominalAttributeID() + " was created in Tagger");
                return response;
            }
            return null;
        } catch (Exception e) {
            throw new AidrException("Error while creating new attribute in Tagger", e);
        }
    }

    public TaggerAttribute getAttributeInfo(Integer id) throws AidrException {
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/attribute/" + id);
            ObjectMapper objectMapper = new ObjectMapper();

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerAttribute response = objectMapper.readValue(jsonResponse, TaggerAttribute.class);
            if (response != null) {
                logger.info("Attribute with ID " + response.getNominalAttributeID() + " was retrieved from Tagger");
                return response;
            }
            return null;
        } catch (Exception e) {
            throw new AidrException("Error while getting attribute from Tagger", e);
        }
    }

    public TaggerLabel getLabelInfo(Integer id) throws AidrException {
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/label/" + id);
            ObjectMapper objectMapper = new ObjectMapper();

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerLabel response = objectMapper.readValue(jsonResponse, TaggerLabel.class);
            if (response != null) {
                logger.info("Label with ID " + response.getNominalLabelID() + " was retrieved from Tagger");
                return response;
            }
            return null;
        } catch (Exception e) {
            throw new AidrException("Error while getting label from Tagger", e);
        }
    }

    public boolean deleteAttribute(Integer id) throws AidrException {
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/attribute/" + id);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .delete(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerStatusResponse response = objectMapper.readValue(jsonResponse, TaggerStatusResponse.class);
            if (response != null && response.getStatusCode() != null) {
                if ("SUCCESS".equals(response.getStatusCode())) {
                    logger.info("Attribute with ID " + id + " was deleted in Tagger");
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            throw new AidrException("Error while deleting attribute in Tagger", e);
        }
    }

    public boolean removeAttributeFromCrises(Integer modelFamilyID) throws AidrException {
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/modelfamily/" + modelFamilyID);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .delete(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerStatusResponse response = objectMapper.readValue(jsonResponse, TaggerStatusResponse.class);
            if (response != null && response.getStatusCode() != null) {
                if ("SUCCESS".equals(response.getStatusCode())) {
                    logger.info("Classifier was remove from crises by modelFamilyID: " + modelFamilyID);
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            throw new AidrException("Error while removing classifier from crisis in Tagger", e);
        }
    }

    public TaggerAttribute updateAttribute(TaggerAttribute attribute) throws AidrException{
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/attribute");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .put(ClientResponse.class, objectMapper.writeValueAsString(attribute));
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerAttribute updatedAttribute = objectMapper.readValue(jsonResponse, TaggerAttribute.class);
            if (updatedAttribute != null) {
                logger.info("Attribute with id " + updatedAttribute.getNominalAttributeID() + " was updated in Tagger");
            } else {
                return null;
            }

            return attribute;
        } catch (Exception e) {
            throw new AidrException("Error while updating attribute in Tagger", e);
        }
    }

    public TaggerLabel updateLabel(TaggerLabelRequest label) throws AidrException{
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/label");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .put(ClientResponse.class, objectMapper.writeValueAsString(label));
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerLabel updatedLabel = objectMapper.readValue(jsonResponse, TaggerLabel.class);
            if (updatedLabel != null) {
                logger.info("Label with id " + updatedLabel.getNominalLabelID() + " was updated in Tagger");
            } else {
                return null;
            }

            return updatedLabel;
        } catch (Exception e) {
            throw new AidrException("Error while updating label in Tagger", e);
        }
    }

    public TaggerLabel createNewLabel(TaggerLabelRequest label) throws AidrException {
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/label");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, objectMapper.writeValueAsString(label));

            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerLabel response = objectMapper.readValue(jsonResponse, TaggerLabel.class);
            if (response != null) {
                logger.info("Label with ID " + response.getNominalLabelID() + " was created in Tagger");
                return response;
            }
            return null;
        } catch (Exception e) {
            throw new AidrException("Error while creating new label in Tagger", e);
        }
    }

    public TaggerAttribute attributeExists(String code) throws AidrException{
        try {
            WebResource webResource = client.resource(taggerMainUrl + "/attribute/code/" + code);
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerAttribute attribute = objectMapper.readValue(jsonResponse, TaggerAttribute.class);
            if (attribute != null) {
                logger.info("Attribute with the code " + code + " already exist in Tagger.");
                return attribute;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new AidrException("Error while checking if attribute exist in Tagger", e);
        }
    }

    public List<TrainingDataDTO> getTrainingDataByModelIdAndCrisisId(Integer modelFamilyId,
                                                                     Integer crisisId,
                                                                     Integer start,
                                                                     Integer limit,
                                                                     String sortColumn,
                                                                     String sortDirection) throws AidrException{
        try {
            WebResource webResource = client.resource(taggerMainUrl + "/misc/getTrainingData?crisisID=" + crisisId
                    + "&modelFamilyID=" + modelFamilyId
                    + "&fromRecord=" + start
                    + "&limit=" + limit
                    + "&sortColumn=" + sortColumn
                    + "&sortDirection=" + sortDirection);
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TrainingDataRequest trainingDataRequest = objectMapper.readValue(jsonResponse, TrainingDataRequest.class);
            if (trainingDataRequest != null && trainingDataRequest.getTrainingData() != null) {
                logger.info("Tagger returned " + trainingDataRequest.getTrainingData().size() + " training data records for crises with ID: "
                        + crisisId + " and family model with ID: " + modelFamilyId);
                return trainingDataRequest.getTrainingData();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new AidrException("Error while Getting training data for Crisis and Model.", e);
        }
    }

    @Override
    public String getAssignableTask(Integer id, String userName) throws AidrException {
        try {
//            taskBufferNumber currently always 1
            int taskBufferNumber = 1;
            WebResource webResource = client.resource(crowdsourcingAPIMainUrl + "/taskbuffer/getassignabletask/" + userName + "/" + id + "/" + taskBufferNumber);
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            logger.info("getAssignableTask - clientResponse : " + clientResponse);
            String jsonResponse = clientResponse.getEntity(String.class);
            logger.info("getAssignableTask - jsonResponse : " + jsonResponse);

            return jsonResponse;
        } catch (Exception e) {
            throw new AidrException("Error while getting Assignable Task in Tagger", e);
        }
    }

    @Override
    public String getTemplateStatus(String code) throws AidrException {
        try {
            WebResource webResource = client.resource(crowdsourcingAPIMainUrl + "/template/status/crisis/code/" + code);
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            logger.info("getTemplateStatus - clientResponse : " + clientResponse);
            String jsonResponse = clientResponse.getEntity(String.class);
            logger.info("getTemplateStatus - jsonResponse : " + jsonResponse);

            return jsonResponse;
        } catch (Exception e) {
            throw new AidrException("Error while getting Template Status in Tagger", e);
        }
    }

    @Override
    public String skipTask(Integer id, String userName) throws AidrException {
        try {
            WebResource webResource = client.resource(crowdsourcingAPIMainUrl + "/taskassignment/revert/searchByDocUserName/" + userName + "/" + id);
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            logger.info("skipTask - clientResponse : " + clientResponse);
            String jsonResponse = clientResponse.getEntity(String.class);
            logger.info("skipTask - jsonResponse : " + jsonResponse);

            return jsonResponse;
        } catch (Exception e) {
            throw new AidrException("Error while Skip Task operation", e);
        }
    }

    public boolean saveTaskAnswer(List<TaskAnswer> taskAnswer) throws AidrException {
        try {
            WebResource webResource = client.resource(crowdsourcingAPIMainUrl + "/taskanswer/save");
            ObjectMapper objectMapper = new ObjectMapper();

            logger.info("saveTaskAnswer - postData : " + objectMapper.writeValueAsString(taskAnswer));
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, objectMapper.writeValueAsString(taskAnswer));

            logger.info("saveTaskAnswer - response status : " + clientResponse.getStatus());

            return clientResponse.getStatus() == 204;
        } catch (Exception e) {
            throw new AidrException("Error while saving TaskAnswer in AIDRCrowdsourcing", e);
        }
    }

    public String generateCSVLink(String code) throws AidrException {
        try {
            WebResource webResource = client.resource(taggerPersisterMainUrl + "/genCSV?collectionCode=" + code + "&exportLimit=100000");
            ClientResponse clientResponse = webResource.type(MediaType.TEXT_PLAIN)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            if (jsonResponse != null && "http".equals(jsonResponse.substring(0, 4))) {
                return jsonResponse;
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new AidrException("Error while generating CSV link in taggerPersister", e);
        }
    }

    public String generateTweetIdsLink(String code) throws AidrException {
        try {
            WebResource webResource = client.resource(taggerPersisterMainUrl + "/genTweetIds?collectionCode=" + code);
            ClientResponse clientResponse = webResource.type(MediaType.TEXT_PLAIN)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            if (jsonResponse != null && "http".equals(jsonResponse.substring(0, 4))) {
                return jsonResponse;
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new AidrException("Error while generating Tweet Ids link in taggerPersister", e);
        }
    }

    @Override
    public ModelHistoryWrapper getModelHistoryByModelFamilyID(Integer start, Integer limit, Integer id) throws AidrException {
        try {
            WebResource webResource = client.resource(taggerMainUrl + "/model/modelFamily/" + id
                    + "?start=" + start
                    + "&limit=" + limit);
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            ModelHistoryWrapper modelHistoryWrapper = objectMapper.readValue(jsonResponse, ModelHistoryWrapper.class);
            return modelHistoryWrapper;
        } catch (Exception e) {
            throw new AidrException("Error while Getting history records for Model.", e);
        }
    }

    public List<TaggerModelNominalLabel> getAllLabelsForModel(Integer modelID) throws AidrException{
        try {
            /**
             * Rest call to Tagger
             */
            WebResource webResource = client.resource(taggerMainUrl + "/modelNominalLabel/" + modelID);
            ObjectMapper objectMapper = new ObjectMapper();
            ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            String jsonResponse = clientResponse.getEntity(String.class);

            TaggerModelLabelsResponse modelLabelsResponse = objectMapper.readValue(jsonResponse, TaggerModelLabelsResponse.class);
            if (modelLabelsResponse.getModelNominalLabelsDTO() != null) {
                logger.info("Tagger returned " + modelLabelsResponse.getModelNominalLabelsDTO().size() + " labels for model with ID " + modelID);
            }

            return modelLabelsResponse.getModelNominalLabelsDTO();
        } catch (Exception e) {
            throw new AidrException("Error while getting all labels for model from Tagger", e);
        }
    }

    private Collection<TaggerAttribute> convertTaggerCrisesAttributeToDTO (List<TaggerCrisesAttribute> attributes, Integer userId) {
        Map<Integer, TaggerAttribute> result = new HashMap<Integer, TaggerAttribute>();
        for (TaggerCrisesAttribute a : attributes) {
            if(!result.containsKey(a.getNominalAttributeID())){
                if (!userId.equals(a.getUserID()) && !(new Integer(1)).equals(a.getUserID())){
                    continue;
                }
                TaggerUser user = new TaggerUser(a.getUserID());
                List<TaggerLabel> labels = new ArrayList<TaggerLabel>();
                TaggerLabel label = new TaggerLabel(a.getLabelName(), a.getLabelID());
                labels.add(label);
                TaggerAttribute taggerAttribute = new TaggerAttribute(a.getCode(), a.getDescription(), a.getName(), a.getNominalAttributeID(), user, labels);
                result.put(a.getNominalAttributeID(), taggerAttribute);
            } else {
                TaggerAttribute taggerAttribute = result.get(a.getNominalAttributeID());
                List<TaggerLabel> labels = taggerAttribute.getNominalLabelCollection();
                TaggerLabel label = new TaggerLabel(a.getLabelName(), a.getLabelID());
                labels.add(label);
            }
        }
        return result.values();
    }

}
