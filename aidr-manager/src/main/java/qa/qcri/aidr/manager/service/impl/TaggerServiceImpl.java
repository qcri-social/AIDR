package qa.qcri.aidr.manager.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.common.code.JacksonWrapper;
import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;
import qa.qcri.aidr.manager.dto.ModelHistoryWrapper;
import qa.qcri.aidr.manager.dto.PingResponse;
import qa.qcri.aidr.manager.dto.TaggerAllCrisesResponse;
import qa.qcri.aidr.manager.dto.TaggerAllCrisesTypesResponse;
import qa.qcri.aidr.manager.dto.TaggerAttribute;
import qa.qcri.aidr.manager.dto.TaggerCrisesAttribute;
import qa.qcri.aidr.manager.dto.TaggerCrisis;
import qa.qcri.aidr.manager.dto.TaggerCrisisAttributesResponse;
import qa.qcri.aidr.manager.dto.TaggerCrisisExist;
import qa.qcri.aidr.manager.dto.TaggerCrisisModelsResponse;
import qa.qcri.aidr.manager.dto.TaggerCrisisRequest;
import qa.qcri.aidr.manager.dto.TaggerCrisisType;
import qa.qcri.aidr.manager.dto.TaggerLabel;
import qa.qcri.aidr.manager.dto.TaggerLabelRequest;
import qa.qcri.aidr.manager.dto.TaggerModel;
import qa.qcri.aidr.manager.dto.TaggerModelFamily;
import qa.qcri.aidr.manager.dto.TaggerModelLabelsResponse;
import qa.qcri.aidr.manager.dto.TaggerModelNominalLabel;
import qa.qcri.aidr.manager.dto.TaggerResponseWrapper;
import qa.qcri.aidr.manager.dto.TaggerStatusResponse;
import qa.qcri.aidr.manager.dto.TaggerUser;
import qa.qcri.aidr.manager.dto.TaggersForCodes;
import qa.qcri.aidr.manager.dto.TaggersForCollectionsRequest;
import qa.qcri.aidr.manager.dto.TaggersForCollectionsResponse;
import qa.qcri.aidr.manager.dto.TaskAnswer;
import qa.qcri.aidr.manager.dto.TrainingDataRequest;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.service.TaggerService;
import qa.qcri.aidr.manager.service.UserService;
import qa.qcri.aidr.manager.util.ManagerConfigurationProperty;
import qa.qcri.aidr.manager.util.ManagerConfigurator;

//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
@Service("taggerService")
public class TaggerServiceImpl implements TaggerService {

	private Logger logger = Logger.getLogger(TaggerServiceImpl.class);

	// @Autowired
	// private Client client;
	private static String taggerMainUrl;

	private static String crowdsourcingAPIMainUrl;

	private static String persisterMainUrl;

	private static String outputAPIMainUrl;
	
	@Autowired
	private UserService userService;

	TaggerServiceImpl() {
		taggerMainUrl = ManagerConfigurator.getInstance().getProperty(
				ManagerConfigurationProperty.TAGGER_MAIN_URL);

		crowdsourcingAPIMainUrl = ManagerConfigurator.getInstance()
				.getProperty(ManagerConfigurationProperty.CROWDSOURCING_API_MAIN_URL);

		persisterMainUrl = ManagerConfigurator.getInstance().getProperty(
				ManagerConfigurationProperty.PERSISTER_MAIN_URL);

		outputAPIMainUrl = ManagerConfigurator.getInstance().getProperty(
				ManagerConfigurationProperty.OUTPUT_MAIN_URL);

	}
	
	// new DTOs introduced. -Imran
	@Override
	public List<TaggerCrisisType> getAllCrisisTypes() throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			logger.info("Received request to fetch all crisisTypes");
			WebTarget webResource = client.target(taggerMainUrl
					+ "/crisisType/all");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			//logger.error("URL: " + taggerMainUrl + " " + jsonResponse);

			TaggerAllCrisesTypesResponse crisesTypesResponse = objectMapper
					.readValue(jsonResponse, TaggerAllCrisesTypesResponse.class);

			if (crisesTypesResponse.getCrisisTypes() != null) {
				logger.info("Tagger returned "
						+ crisesTypesResponse.getCrisisTypes().size()
						+ " crises types");
			}

			return crisesTypesResponse.getCrisisTypes();
		} catch (Exception e) {
			logger.error("Error while getting all crisis from Tagger", e);
			throw new AidrException(
					"Error while getting all crisis from Tagger", e);
		}
	}

	@Override
	public List<TaggerCrisis> getCrisesByUserId(Integer userId)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			WebTarget webResource = client.target(taggerMainUrl
					+ "/crisis?userID=" + userId);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerAllCrisesResponse taggerAllCrisesResponse = objectMapper
					.readValue(jsonResponse, TaggerAllCrisesResponse.class);

			if (taggerAllCrisesResponse.getCrisises() != null) {
				logger.info("Tagger returned "
						+ taggerAllCrisesResponse.getCrisises().size()
						+ " crisis for user");
			}

			return taggerAllCrisesResponse.getCrisises();
		} catch (Exception e) {
			logger.error("Exception while fetching crisis by userId: "+userId, e);
			throw new AidrException(
					"No collection is enabled for Tagger. Please enable tagger for one of your collections.",
					e);
		}
	}

	@Override
	public String createNewCrises(TaggerCrisisRequest crisis)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			CollectionDTO dto = crisis.toDTO();
			logger.info("Going to create new crisis: " + dto.getCode());
			WebTarget webResource = client.target(taggerMainUrl + "/crisis");
			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(
					Entity.json(dto),
					Response.class);

			return clientResponse.readEntity(String.class);
		} catch (Exception e) {
			logger.error("Error while creating new crisis: "+crisis.getName(), e);
			throw new AidrException(
					"Error while creating new crises in Tagger", e);
		}
	}

	// (6)
	@Override
	public java.util.Collection<TaggerAttribute> getAttributesForCrises(Integer crisisID,
			Integer userId) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// Rest call to Tagger
			WebTarget webResource = client.target(taggerMainUrl
					+ "/attribute/crisis/all?exceptCrisis=" + crisisID);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerCrisisAttributesResponse crisisAttributesResponse = objectMapper
					.readValue(jsonResponse,
							TaggerCrisisAttributesResponse.class);

			if (crisisAttributesResponse.getCrisisAttributes() != null) {
				logger.info("Tagger returned "
						+ crisisAttributesResponse.getCrisisAttributes().size()
						+ " attributes available for crises with ID "
						+ crisisID);
			} else {
				return Collections.emptyList();
			}

			return convertTaggerCrisesAttributeToDTO(
					crisisAttributesResponse.getCrisisAttributes(), userId);
		} catch (Exception e) {
			logger.error("Error while getting all attributes for crisis from Tagger for crisisID: "+crisisID, e);
			throw new AidrException(
					"Error while getting all attributes for crisis from Tagger",
					e);
		}
	}

	@Override
	public Map<String, Integer> countCollectionsClassifiers(
			List<String> collectionCodes) throws AidrException {

		try {
			Client client = ClientBuilder.newBuilder()
					.register(JacksonFeature.class).build();
			WebTarget webResource = client.target(taggerMainUrl
					+ "/crisis/crises");

			String input = "";

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			input = objectMapper.writeValueAsString(collectionCodes);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(input));
			String jsonResponse = clientResponse.readEntity(String.class);
			HashMap<String, Integer> rv = objectMapper.readValue(jsonResponse,
					HashMap.class);

			return rv;
		} catch (Exception e) {
			logger.error("Error while getting amount of classifiers by collection codes in Tagger", e);
			throw new AidrException(
					"Error while getting amount of classifiers by collection codes in Tagger",
					e);
		}
	}

	@Override
	public TaggerCrisisExist isCrisesExist(String code) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(taggerMainUrl
					+ "/crisis/code/" + code);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerCrisisExist crisisExist = objectMapper.readValue(
					jsonResponse, TaggerCrisisExist.class);

			if (crisisExist.getCrisisId() != null) {
				logger.info("Response from Tagger-API for Crises with the code "
						+ code+ ", found crisisID = "+ crisisExist.getCrisisId());
				return crisisExist;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error while checking if crisis exist in Tagger for collection: "+code, e);
			throw new AidrException(
					"Error while checking if crisis exist in Tagger", e);
		}
	}

	@Override
	public Integer isUserExistsByUsername(String userName) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			WebTarget webResource = client.target(taggerMainUrl + "/user/"
					+ userName);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			String jsonResponse = clientResponse.readEntity(String.class);

			UsersDTO user = objectMapper
					.readValue(jsonResponse, UsersDTO.class);

			TaggerUser taggerUser = new TaggerUser(user);

			if (taggerUser != null && taggerUser.getUserID() != null) {
				logger.info("User with the user name " + userName
						+ " already exist in Tagger and has ID: "
						+ taggerUser.getUserID());
				return taggerUser.getUserID();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error while checking if user: "+userName +"exist", e);
			throw new AidrException(
					"Error while checking if user exist in Tagger", e);
		}
	}

	@Override
	public Integer addNewUser(TaggerUser taggerUser) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			WebTarget webResource = client.target(taggerMainUrl + "/user");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper
					.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(
					Entity.json(objectMapper.writeValueAsString(taggerUser
							.toDTO())), Response.class);

			String jsonResponse = clientResponse.readEntity(String.class);

			UsersDTO dto = objectMapper.readValue(jsonResponse, UsersDTO.class);
			if (dto != null) {
				TaggerUser createdUser = new TaggerUser(dto);
				if (createdUser != null && createdUser.getUserID() != null) {
					logger.info("User with ID " + createdUser.getUserID()
							+ " was created in Tagger");
					return createdUser.getUserID();
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error while adding new user to Tagger", e);
			throw new AidrException("Error while adding new user to Tagger", e);
		}
	}

	@Override
	public Integer addAttributeToCrisis(TaggerModelFamily modelFamily)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			logger.info("Received add Attirbute request for crisis = "
					+ modelFamily.getCrisis().getCrisisID() + ", attribute = "
					+ modelFamily.getNominalAttribute().getNominalAttributeID());
			WebTarget webResource = client.target(taggerMainUrl
					+ "/modelfamily");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper
					.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(
					Entity.json(objectMapper.writeValueAsString(modelFamily
							.toDTO())), Response.class);

			String jsonResponse = clientResponse.readEntity(String.class);
			TaggerResponseWrapper responseWrapper = objectMapper.readValue(
					jsonResponse, TaggerResponseWrapper.class);
			if (responseWrapper != null) {
				Long modelFamilyIDLong = responseWrapper.getEntityID();
				Integer modelFamilyID = new Long(modelFamilyIDLong).intValue();
				if (modelFamilyID.intValue() > 0) {
					logger.info("Attribute was added to crises: "
							+ modelFamilyID);
					return modelFamilyID;
				} else {
					logger.info("Attribute was NOT added to crises: ");
					logger.info("Received message from tagger-api: "
							+ responseWrapper.getStatusCode() + "\n"
							+ responseWrapper.getMessage());
					return null;
				}
			} else {
				logger.info("Attribute was NOT added to crises: ");
				return null;
			}
		} catch (Exception e) {
			logger.error("Error while adding attribute to crises", e);
			throw new AidrException("Error while adding attribute to crises", e);
		}
	}

	@Override
	public TaggerCrisis getCrisesByCode(String code) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			logger.info("Received request for crisis : " + code);
			WebTarget webResource = client.target(taggerMainUrl
					+ "/crisis/by-code/" + code);
			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			String jsonResponse = clientResponse.readEntity(String.class);
			logger.info("received response: " + jsonResponse);
			CollectionDTO dto = null;
			TaggerResponseWrapper response = objectMapper.readValue(
					jsonResponse, TaggerResponseWrapper.class);
			if (response.getDataObject() != null) {
				dto = objectMapper.readValue(objectMapper
						.writeValueAsString(response.getDataObject()),
						CollectionDTO.class);
			}
			logger.info("deserialization result: " + dto);
			if (dto != null) {
				TaggerCrisis crisis = new TaggerCrisis(dto);
				if (crisis != null) {
					logger.info("Tagger returned crisis with code"
							+ crisis.getCode());
				}
				return crisis;
			}
			return null;
		} catch (Exception e) {
			logger.error("Error while getting crisis by code for collection: "+code, e);
			return null;
			// throw new
			// AidrException("Error while getting crisis by code from Tagger",
			// e);
		}
	}

	@Override
	public TaggerCrisis updateCode(TaggerCrisis crisis) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			CollectionDTO dto = crisis.toDTO();
			WebTarget webResource = client.target(taggerMainUrl + "/crisis");
			logger.info("Received update request for crisis = "
					+ crisis.getCode() + ", dto = " + dto.getCode());
			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).put(
					Entity.json(objectMapper.writeValueAsString(dto)),
					Response.class);

			String jsonResponse = clientResponse.readEntity(String.class);
			CollectionDTO updatedDTO = objectMapper.readValue(jsonResponse,
					CollectionDTO.class);
			TaggerCrisis updatedCrisis = new TaggerCrisis(updatedDTO);
			logger.info("Received response: " + updatedCrisis.getCode() + ", "
					+ updatedCrisis.getName() + ","
					+ updatedCrisis.getCrisisType().getCrisisTypeID());
			if (updatedCrisis != null) {
				logger.info("Crisis with id " + updatedCrisis.getCrisisID()
						+ " was updated in Tagger");
			}
			return crisis;
		} catch (Exception e) {
			logger.error("Error while updating crisis: "+crisis.getCode(), e);
			throw new AidrException(
					"Error while updating crisis", e);
		}
	}

	@Override
	public List<TaggerModel> getModelsForCrisis(Integer crisisID)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			int retrainingThreshold = getCurrentRetrainingThreshold();
			WebTarget webResource = client.target(taggerMainUrl
					+ "/model/crisis/" + new Long(crisisID));

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerCrisisModelsResponse crisisModelsResponse = objectMapper
					.readValue(jsonResponse, TaggerCrisisModelsResponse.class);
			logger.info("Tagger returned jsonResponse: " + jsonResponse);
			if (crisisModelsResponse.getModelWrapper() != null) {
				logger.info("Tagger returned "
						+ crisisModelsResponse.getModelWrapper().size()
						+ " models for crises with ID " + crisisID);
				List<TaggerModel> tempTaggerModel = new ArrayList<TaggerModel>();
				for (TaggerModel temp : crisisModelsResponse.getModelWrapper()) {

					TaggerModel tm = new TaggerModel();
					// System.out.println("reset0 : " + retrainingThreshold);
					tm.setRetrainingThreshold(retrainingThreshold);
					tm.setAttributeID(temp.getAttributeID());
					tm.setModelID(temp.getModelID());
					tm.setAttribute(temp.getAttribute());
					tm.setAuc(temp.getAuc());
					tm.setStatus(temp.getStatus());
					tm.setTrainingExamples(temp.getTrainingExamples());
					tm.setClassifiedDocuments(temp.getClassifiedDocuments());
					tm.setModelFamilyID(temp.getModelFamilyID());

					// System.out.println("reset : " +
					// tm.getRetrainingThreshold());
					tempTaggerModel.add(tm);
				}

				return tempTaggerModel;
			}
			return null;
		} catch (Exception e) {
			logger.error("Error while getting all models for crisisId:"+crisisID, e);
			throw new AidrException(
					"Error while getting all models for crisis from Tagger", e);
		}
	}

	// (1)
	@Override
	public TaggerAttribute createNewAttribute(TaggerAttribute attribute)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(taggerMainUrl + "/attribute");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper
					.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(
					Entity.json(objectMapper.writeValueAsString(attribute
							.toDTO())), Response.class);

			// String jsonResponse = clientResponse.getEntity(String.class);
			String jsonResponse = clientResponse.readEntity(String.class);
			NominalAttributeDTO dto = objectMapper.readValue(jsonResponse,
					NominalAttributeDTO.class);
			TaggerAttribute newAttribute = dto != null ? new TaggerAttribute(
					dto) : null;
			if (newAttribute != null) {
				logger.info("Attribute with ID "
						+ newAttribute.getNominalAttributeID()
						+ " was created in Tagger");
			}
			return newAttribute;
		} catch (Exception e) {
			logger.error("Error while creating new attribute", e);
			throw new AidrException(
					"Error while creating new attribute in Tagger", e);
		}
	}

	// (4)
	@Override
	public TaggerAttribute getAttributeInfo(Integer id) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// Rest call to Tagger
			logger.info("received request for nominal attribute id: " + id);
			WebTarget webResource = client.target(taggerMainUrl + "/attribute/"
					+ new Long(id));

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			String jsonResponse = clientResponse.readEntity(String.class);
			NominalAttributeDTO dto = objectMapper.readValue(jsonResponse,
					NominalAttributeDTO.class);
			TaggerAttribute response = dto != null ? new TaggerAttribute(dto)
					: null;
			logger.info("Received response: " + response != null ? response
					.getName() : "no attribute for id = " + id);
			if (response != null) {
				logger.info("Attribute with ID "
						+ response.getNominalAttributeID()
						+ " was retrieved from Tagger");
			}
			return response;
		} catch (Exception e) {
			logger.error("Error while getting attribute info for attribute: "+id, e);
			throw new AidrException(
					"Error while getting attribute from Tagger", e);
		}
	}

	@Override
	public TaggerLabel getLabelInfo(Integer id) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// Rest call to Tagger
			WebTarget webResource = client.target(taggerMainUrl + "/label/"
					+ id);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);
			NominalLabelDTO dto = objectMapper.readValue(jsonResponse,
					NominalLabelDTO.class);
			TaggerLabel response = new TaggerLabel(dto);
			if (response != null) {
				logger.info("Label with ID " + response.getNominalLabelID()
						+ " was retrieved from Tagger");
				return response;
			}
			return null;
		} catch (Exception e) {
			logger.error("Error while getting label info for labelId: "+id, e);
			throw new AidrException("Error while getting label from Tagger", e);
		}
	}

	// (3)
	@Override
	public boolean deleteAttribute(Integer id) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(taggerMainUrl + "/attribute/"
					+ id);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper
					.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).delete();
			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerStatusResponse response = objectMapper.readValue(
					jsonResponse, TaggerStatusResponse.class);
			if (response != null && response.getStatusCode() != null) {
				if ("SUCCESS".equals(response.getStatusCode())) {
					logger.info("Attribute with ID " + id
							+ " was deleted in Tagger");
					return true;
				} else {
					return false;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("Error while deleting a attribute for attributeId: "+id, e);
			throw new AidrException("Error while deleting attribute in Tagger",
					e);
		}
	}

	@Override
	public boolean deleteTrainingExample(Integer id) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			WebTarget webResource = client.target(taggerMainUrl
					+ "/document/removeTrainingExample/" + new Long(id));

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper
					.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).delete();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerStatusResponse response = objectMapper.readValue(
					jsonResponse, TaggerStatusResponse.class);
			if (response != null && response.getStatusCode() != null) {
				if ("SUCCESS".equals(response.getStatusCode())) {
					logger.info("Document with ID " + id
							+ " was deleted in Tagger");
					return true;
				} else {
					return false;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("Error while deleting document", e);
			throw new AidrException("Error while deleting document in Tagger",
					e);
		}
	}

	@Override
	public boolean removeAttributeFromCrises(Integer modelFamilyID)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			deletePybossaApp(modelFamilyID);
			WebTarget webResource = client.target(taggerMainUrl
					+ "/modelfamily/" + new Long(modelFamilyID));

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper
					.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).delete();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerStatusResponse response = objectMapper.readValue(
					jsonResponse, TaggerStatusResponse.class);
			if (response != null && response.getStatusCode() != null) {
				if ("SUCCESS".equals(response.getStatusCode())) {
					logger.info("Classifier was remove from crises by modelFamilyID: "
							+ modelFamilyID);
					return true;
				} else {
					return false;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("Error while removing classifier from crisis ", e);
			throw new AidrException(
					"Error while removing classifier from crisis in Tagger", e);
		}
	}

	// (2)
	@Override
	public TaggerAttribute updateAttribute(TaggerAttribute attribute)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(taggerMainUrl + "/attribute");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper
					.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).put(
					Entity.json(objectMapper.writeValueAsString(attribute
							.toDTO())), Response.class);

			String jsonResponse = clientResponse.readEntity(String.class);
			NominalAttributeDTO dto = objectMapper.readValue(jsonResponse,
					NominalAttributeDTO.class);
			TaggerAttribute updatedAttribute = new TaggerAttribute(dto);
			if (updatedAttribute != null) {
				logger.info("Attribute with id "
						+ updatedAttribute.getNominalAttributeID()
						+ " was updated in Tagger");
			} else {
				return null;
			}

			return attribute;
		} catch (Exception e) {
			logger.error("Error while updating attribute: "+attribute.getCode(), e);
			throw new AidrException("Error while updating attribute in Tagger",
					e);
		}
	}

	@Override
	public TaggerLabel updateLabel(TaggerLabelRequest label)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			// WebResource webResource = client.resource(taggerMainUrl +
			// "/label");
			WebTarget webResource = client.target(taggerMainUrl + "/label");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper
					.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON)
					.put(Entity.json(objectMapper.writeValueAsString(label
							.toDTO())), Response.class);

			String jsonResponse = clientResponse.readEntity(String.class);
			NominalLabelDTO dto = objectMapper.readValue(jsonResponse,
					NominalLabelDTO.class);
			TaggerLabel updatedLabel = new TaggerLabel(dto);
			if (updatedLabel != null) {
				logger.info("Label with id " + updatedLabel.getNominalLabelID()
						+ " was updated in Tagger");
			} else {
				return null;
			}

			return updatedLabel;
		} catch (Exception e) {
			logger.error("Error while updating label: "+label.getName(), e);
			throw new AidrException("Error while updating label in Tagger", e);
		}
	}

	@Override
	public TaggerLabel createNewLabel(TaggerLabelRequest label)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			WebTarget webResource = client.target(taggerMainUrl + "/label");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper
					.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON)
					.post(Entity.json(objectMapper.writeValueAsString(label
							.toDTO())), Response.class);

			String jsonResponse = clientResponse.readEntity(String.class);
			NominalLabelDTO dto = objectMapper.readValue(jsonResponse,
					NominalLabelDTO.class);
			TaggerLabel response = new TaggerLabel(dto);
			if (response != null) {
				logger.info("Label with ID " + response.getNominalLabelID()
						+ " was created in Tagger");
				return response;
			} else {
				logger.error("Error while creating new label: "+label.getName());
				throw new AidrException(
						"Error while creating new label in Tagger");
			}
		} catch (Exception e) {
			logger.error("Error while creating new label: "+label.getName(), e);
			throw new AidrException("Error while creating new label in Tagger",
					e);
		}
	}

	// (7)
	@Override
	public TaggerAttribute attributeExists(String code) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(taggerMainUrl
					+ "/attribute/code/" + code);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerAttribute attribute = objectMapper.readValue(jsonResponse,
					TaggerAttribute.class);
			if (attribute != null) {
				logger.info("Attribute with the code " + code
						+ " already exist in Tagger.");
				return attribute;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error while checking if attribute: "+code+" exist", e);
			throw new AidrException(
					"Error while checking if attribute exist in Tagger", e);
		}
	}

	@Override
	public List<TrainingDataDTO> getTrainingDataByModelIdAndCrisisId(
			Integer modelFamilyId, Integer crisisId, Integer start,
			Integer limit, String sortColumn, String sortDirection)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		logger.info("Received request for fetching training data for crisisID = "
				+ crisisId + "and modelFamilyId = " + modelFamilyId);
		try {
			WebTarget webResource = client.target(taggerMainUrl
					+ "/misc/getTrainingData?crisisID=" + new Long(crisisId)
					+ "&modelFamilyID=" + new Long(modelFamilyId)
					+ "&fromRecord=" + start + "&limit=" + limit
					+ "&sortColumn=" + sortColumn + "&sortDirection="
					+ sortDirection);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			// ClientResponse clientResponse =
			// webResource.type(MediaType.APPLICATION_JSON)
			// .accept(MediaType.APPLICATION_JSON)
			// .get(ClientResponse.class);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			// String jsonResponse = clientResponse.getEntity(String.class);
			String jsonResponse = clientResponse.readEntity(String.class);

			TrainingDataRequest trainingDataRequest = objectMapper.readValue(
					jsonResponse, TrainingDataRequest.class);
			if (trainingDataRequest != null
					&& trainingDataRequest.getTrainingData() != null) {
				logger.info("Tagger returned "
						+ trainingDataRequest.getTrainingData().size()
						+ " training data records for crisis with ID: "
						+ crisisId + " and family model with ID: "
						+ modelFamilyId);
				return trainingDataRequest.getTrainingData();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(crisisId + " Error while Getting training data for Crisis and Model", e);
			throw new AidrException(
					"Error while Getting training data for Crisis and Model.",
					e);
		}
	}

	@Override
	public String getAssignableTask(Integer id, String userName)
			throws AidrException {

		Integer taggerUserId = isUserExistsByUsername(userName);

		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// taskBufferNumber currently always 1
			int taskBufferNumber = 1;
			// WebResource webResource = client.resource(crowdsourcingAPIMainUrl
			// + "/taskbuffer/getassignabletask/" + userName + "/" + id + "/" +
			// taskBufferNumber);
			WebTarget webResource = client.target(crowdsourcingAPIMainUrl
					+ "/document/getassignabletask/" + userName + "/" + id
					+ "/" + taskBufferNumber);

			// ClientResponse clientResponse =
			// webResource.type(MediaType.APPLICATION_JSON)
			// .accept(MediaType.APPLICATION_JSON)
			// .get(ClientResponse.class);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			logger.info("getAssignableTask - clientResponse : "
					+ clientResponse);

			// String jsonResponse = clientResponse.getEntity(String.class);
			String jsonResponse = clientResponse.readEntity(String.class);

			return jsonResponse;
		} catch (Exception e) {
			logger.error("Error while getting Assignable Task in Tagger for user: "+userName, e);
			throw new AidrException(
					"Error while getting Assignable Task in Tagger", e);
		}
	}

	@Override
	public String getTemplateStatus(String code) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(crowdsourcingAPIMainUrl
					+ "/template/status/crisis/code/" + code);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			logger.info("getTemplateStatus - clientResponse : "
					+ clientResponse);

			String jsonResponse = clientResponse.readEntity(String.class);

			return jsonResponse;
		} catch (Exception e) {
			logger.error("Error while getting Template Status in Tagger for code:"+code, e);
			throw new AidrException(
					"Error while getting Template Status in Tagger", e);
		}
	}

	@Override
	public String skipTask(Integer id, String userName) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// WebResource webResource = client.resource(crowdsourcingAPIMainUrl
			// + "/taskassignment/revert/searchByDocUserName/" + userName + "/"
			// + id);
			WebTarget webResource = client.target(crowdsourcingAPIMainUrl
					+ "/taskassignment/revert/searchByDocUserName/" + userName
					+ "/" + id);

			// ClientResponse clientResponse =
			// webResource.type(MediaType.APPLICATION_JSON)
			// .accept(MediaType.APPLICATION_JSON)
			// .get(ClientResponse.class);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			logger.info("Skipping task: " + id + " for user = " + userName);
			logger.info("skipTask - clientResponse : " + clientResponse);

			// String jsonResponse = clientResponse.getEntity(String.class);
			String jsonResponse = clientResponse.readEntity(String.class);

			return jsonResponse;
		} catch (Exception e) {
			logger.error("Error while Skip Task operation for user: "+userName, e);
			throw new AidrException("Error while Skip Task operation", e);
		}
	}

	@Override
	public boolean saveTaskAnswer(List<TaskAnswer> taskAnswer)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(crowdsourcingAPIMainUrl
					+ "/taskanswer/save");
			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();

			logger.info("saveTaskAnswer - postData : "
					+ objectMapper.writeValueAsString(taskAnswer));

			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(
					Entity.json(objectMapper.writeValueAsString(taskAnswer)),
					Response.class);
			//logger.info("saveTaskAnswer - response status : "
				//	+ clientResponse.getStatus());

			return clientResponse.getStatus() == 204;
		} catch (Exception e) {
			logger.error("Error while saving TaskAnswer in AIDRCrowdsourcing", e);
			return true;
		}
	}

	@Override
	public String loadLatestTweets(String code, String constraints)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(outputAPIMainUrl
					+ "/crisis/fetch/channel/filter/" + code + "?count=1000");
			logger.info("Invoking: " + outputAPIMainUrl
					+ "/crisis/fetch/channel/filter/" + code + "?count=1000 constraints:" + constraints);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(constraints),
					Response.class);

			String jsonResponse = clientResponse.readEntity(String.class);
			if (jsonResponse != null
					&& (jsonResponse.startsWith("{") || jsonResponse
							.startsWith("["))) {
				return jsonResponse;
			} else {
				return "";
			}
		} catch (Exception e) {
			logger.error("Error while generating loading latest tweets for the collection: "+code, e);
			throw new AidrException(
					"Error while generating Tweet Ids link in taggerPersister",
					e);
		}
	}

	@Override
	public ModelHistoryWrapper getModelHistoryByModelFamilyID(Integer start,
			Integer limit, Integer id, String sortColumn, String sortDirection) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// WebResource webResource = client.resource(taggerMainUrl +
			// "/model/modelFamily/" + id
			// + "?start=" + start
			// + "&limit=" + limit);
			WebTarget webResource = client.target(taggerMainUrl
					+ "/model/modelFamily/" + id + "?start=" + start
					+ "&limit=" + limit+ "&sortColumn=" + sortColumn + "&sortDirection="
					+ sortDirection);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			// ClientResponse clientResponse =
			// webResource.type(MediaType.APPLICATION_JSON)
			// .accept(MediaType.APPLICATION_JSON)
			// .get(ClientResponse.class);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			// String jsonResponse = clientResponse.getEntity(String.class);
			String jsonResponse = clientResponse.readEntity(String.class);

			ModelHistoryWrapper modelHistoryWrapper = objectMapper.readValue(
					jsonResponse, ModelHistoryWrapper.class);
			return modelHistoryWrapper;
		} catch (Exception e) {
			logger.error("Error while Getting history records for Mode", e);
			throw new AidrException(
					"Error while Getting history records for Model.", e);
		}
	}

	@Override
	public List<TaggerModelNominalLabel> getAllLabelsForModel(Integer modelID,
			String crisisCode) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			//logger.info("received request for modelID = " + modelID);
			WebTarget webResource = client.target(taggerMainUrl
					+ "/modelNominalLabel/" + modelID + "/" + crisisCode);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerModelLabelsResponse modelLabelsResponse = objectMapper
					.readValue(jsonResponse, TaggerModelLabelsResponse.class);

			if (modelLabelsResponse.getModelNominalLabelsDTO() != null) {
				logger.info("Tagger returned "
						+ modelLabelsResponse.getModelNominalLabelsDTO().size()
						+ " labels for model with ID " + modelID);
				for (TaggerModelNominalLabel dto : modelLabelsResponse
						.getModelNominalLabelsDTO()) {
					logger.info("Training count for crisis = " + crisisCode
							+ ", label: " + dto.getNominalLabel().getName()
							+ " is = " + dto.getTrainingDocuments());
				}
			}

			return modelLabelsResponse.getModelNominalLabelsDTO();
		} catch (Exception e) {
			logger.error("Error while getting all labels for model from Tagger for the crisis: "+crisisCode, e);
			throw new AidrException(
					"Error while getting all labels for model from Tagger", e);
		}
	}

	@Override
	public String getRetainingThreshold() throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();

		try {

			WebTarget webResource = client.target(taggerMainUrl
					+ "/train/samplecountthreshold");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			// String jsonResponse = clientResponse.getEntity(String.class);
			String jsonResponse = clientResponse.readEntity(String.class);

			return jsonResponse;
		} catch (Exception e) {
			logger.error("Exception while getting training sampleCountThreshold", e);
			throw new AidrException("getRetainingThreshold : ", e);

		}

	}

	@Override
	public Map<String, Integer> getTaggersForCollections(
			List<String> collectionCodes) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			/**
			 * Rest call to Tagger
			 */
			// WebResource webResource = client.resource(taggerMainUrl +
			// "/modelfamily/taggers-by-codes");
			WebTarget webResource = client.target(taggerMainUrl
					+ "/modelfamily/taggers-by-codes");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();

			Response clientResponse = webResource
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(objectMapper
							.writeValueAsString(new TaggersForCollectionsRequest(
									collectionCodes))), Response.class);

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggersForCollectionsResponse taggersResponse = objectMapper
					.readValue(jsonResponse,
							TaggersForCollectionsResponse.class);
			if (taggersResponse != null
					&& !taggersResponse.getTaggersForCodes().isEmpty()) {
				Map<String, Integer> result = new HashMap<String, Integer>();
				for (TaggersForCodes taggerForCode : taggersResponse
						.getTaggersForCodes()) {
					result.put(taggerForCode.getCode(),
							taggerForCode.getCount());
				}
				return result;
			} else {
				return Collections.emptyMap();
			}
		} catch (Exception e) {
			logger.error("Error while getting taggers for collections", e);
			throw new AidrException("Error while getting taggers for collections", e);
		}
	}

	@Override
	public boolean pingTagger() throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(taggerMainUrl + "/misc/ping");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			PingResponse pingResponse = objectMapper.readValue(jsonResponse,
					PingResponse.class);
			if (pingResponse != null
					&& "RUNNING".equals(pingResponse.getStatus())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("Error while pinging tagger", e);
			throw new AidrException(
					"Error while pinging tagger.",e);
		}
	}

	@Override
	public boolean pingTrainer() throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(crowdsourcingAPIMainUrl
					+ "/util/ping/heartbeat");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			PingResponse pingResponse = objectMapper.readValue(jsonResponse,
					PingResponse.class);
			if (pingResponse != null && "200".equals(pingResponse.getStatus())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("Error while pinging trainer", e);
			throw new AidrException(
					"Error while pinging trainer.",e);
		}
	}

	@Override
	public boolean pingAIDROutput() throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(outputAPIMainUrl
					+ "/manage/ping");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			PingResponse pingResponse = objectMapper.readValue(jsonResponse,
					PingResponse.class);
			if (pingResponse != null
					&& "RUNNING".equals(pingResponse.getStatus())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("Error while pinging output", e);
			throw new AidrException(
					"Error while pinging output",
					e);
		}
	}

	@Override
	public boolean pingPersister() throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(persisterMainUrl
					+ "/persister/ping");

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			PingResponse pingResponse = objectMapper.readValue(jsonResponse,
					PingResponse.class);
			if (pingResponse != null
					&& "RUNNING".equals(pingResponse.getStatus())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("Error while pinging persister", e);
			throw new AidrException(
					"Error while pinging persister",e);
		}
	}

	// Added by koushik
	@Override
	public Map<String, Object> generateCSVLink(String code)
			throws AidrException {
		try {
			Client client = ClientBuilder.newBuilder()
					.register(JacksonFeature.class).build();
			WebTarget webResource = client.target(persisterMainUrl
					+ "/taggerPersister/genCSV?collectionCode=" + code
					+ "&exportLimit=100000");
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			Map<String, Object> jsonResponse = clientResponse.readEntity(Map.class);
			return jsonResponse;
			/*
			 * if (jsonResponse != null &&
			 * "http".equals(jsonResponse.substring(0, 4))) { return
			 * jsonResponse; } else { return ""; }
			 */
		} catch (Exception e) {
			logger.error("Error while generating CSV link in Persister for collection: "+code, e);
			throw new AidrException(
					"[generateCSVLink] Error while generating CSV link in Persister",
					e);
		}
	}

	// Added by koushik
	@Override
	public Map<String, Object> generateTweetIdsLink(String code)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		logger.info("[generateTweetIdsLink] Received request for code: "
				+ code);
		try {
			/*System.out.println("Invoked URL: " + persisterMainUrl
					+ "/taggerPersister/genTweetIds?collectionCode=" + code
					+ "&downloadLimited=true");*/
			WebTarget webResource = client.target(persisterMainUrl
					+ "/taggerPersister/genTweetIds?collectionCode=" + code
					+ "&downloadLimited=true");
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			Map<String, Object> jsonResponse = clientResponse.readEntity(Map.class);
			logger.info("Returning from func: " + jsonResponse);
			return jsonResponse;
			/*
			 * if (jsonResponse != null &&
			 * "http".equals(jsonResponse.substring(0, 4))) { return
			 * jsonResponse; } else { return ""; }
			 */
		} catch (Exception e) {
			logger.error("Error while generating Tweet Ids link in Persister for collection: "+code, e);
			throw new AidrException(
					"[generateTweetIdsLink] Error while generating Tweet Ids link in Persister",
					e);
		}
	}

	@Override
	public Map<String, Object> generateJSONLink(String code, String jsonType)
			throws AidrException {
		try {
			Client client = ClientBuilder.newBuilder()
					.register(JacksonFeature.class).build();
			WebTarget webResource = client.target(persisterMainUrl
					+ "/taggerPersister/genJson?collectionCode=" + code
					+ "&exportLimit=100000" + "&jsonType=" + jsonType);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			// String jsonResponse = clientResponse.readEntity(String.class);
			Map<String, Object> jsonResponse = clientResponse.readEntity(Map.class);
			return jsonResponse;
			/*
			 * if (jsonResponse != null &&
			 * "http".equals(jsonResponse.substring(0, 4))) { return
			 * jsonResponse; } else { return ""; }
			 */
		} catch (Exception e) {
			logger.error("Error while generating JSON download link in Persister for collection: "+code, e);
			throw new AidrException(
					"[generateJSONLink] Error while generating JSON download link in Persister",
					e);
		}
	}

	// Added by koushik
	@Override
	public Map<String, Object> generateJsonTweetIdsLink(String code,
			String jsonType) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(persisterMainUrl
					+ "/taggerPersister/genJsonTweetIds?collectionCode=" + code
					+ "&downloadLimited=true&" + "&jsonType=" + jsonType);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			Map<String, Object> jsonResponse = clientResponse
					.readEntity(Map.class);
			logger.info("Returning from func: " + jsonResponse);
			return jsonResponse;
		} catch (Exception e) {
			logger.error("Error while generating JSON Tweet Ids download link in Persister for collection: "+code, e);
			throw new AidrException(
					"[generateJsonTweetIdsLink] Error while generating JSON Tweet Ids download link in Persister",
					e);
		}
	}

	@Override
	public Map<String, Object> generateCSVFilteredLink(String code,
			String queryString, String userName, Integer count, boolean removeRetweet) throws AidrException {
		try {
			Client client = ClientBuilder.newBuilder()
					.register(JacksonFeature.class).build();
			
			
			String url = null;
			Response clientResponse = null;
				url = persisterMainUrl + "/taggerPersister/filter/genCSV?collectionCode=" + code + "&exportLimit="
						+ count + "&userName=" + userName + "&removeRetweet=" + removeRetweet;
				WebTarget webResource = client.target(url);
				clientResponse = webResource.request(MediaType.APPLICATION_JSON).post(Entity.json(queryString),
						Response.class);
			Map<String, Object> jsonResponse = clientResponse.readEntity(Map.class);
			return jsonResponse;
		} catch (Exception e) {
			logger.info("Error while generating csv filtered download link in Persister for collection: "+code, e);
			throw new AidrException(
					"[generateCSVFilteredLink] Error while generating csv filtered download link in Persister for collection",
					e);
		}
	}

	// Added by koushik
	@Override
	public Map<String, Object> generateTweetIdsFilteredLink(String code,
			String queryString, String userName) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		//System.out.println("[generateJsonTweetIdsLink] Received request for code: "+ code);
		try {
			logger.info("[generateTweetIdsLink] Invoked URL: "
					+ persisterMainUrl
					+ "/taggerPersister/filter/genTweetIds?collectionCode="
					+ code + "&downloadLimited=true&userName=" + userName);
			WebTarget webResource = client.target(persisterMainUrl
					+ "/taggerPersister/filter/genTweetIds?collectionCode="
					+ code + "&downloadLimited=true&userName=" + userName);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(queryString),
					Response.class);

			Map<String, Object> jsonResponse = clientResponse
					.readEntity(Map.class);
			//logger.info("Returning from func: " + jsonResponse);
			return jsonResponse;
		} catch (Exception e) {
			logger.error("Error while generating filtererd Tweet Ids download link in Persister for collection: "+code, e);
			throw new AidrException(
					"[generateTweetIdsFilteredLink] Error while generating filtererd Tweet Ids download link in Persister",
					e);
		}
	}
	
	@Override
	public Map<String, Object> generateTweetIdsOnlyFilteredLink(String code, String queryString, String userName, Integer count, Boolean removeRetweet) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			logger.info("[generateTweetIdsOnlyFilteredLink] Invoked URL: "
					+ persisterMainUrl
					+ "/taggerPersister/filter/genTweetIdsOnly?collectionCode="
					+ code + "&userName=" + userName + "&exportLimit=" + count + "&removeRetweet=" + removeRetweet);
			WebTarget webResource = client.target(persisterMainUrl
					+ "/taggerPersister/filter/genTweetIdsOnly?collectionCode="
					+ code + "&userName=" + userName + "&exportLimit=" + count + "&removeRetweet=" + removeRetweet);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(queryString),
					Response.class);

			Map<String, Object> jsonResponse = clientResponse.readEntity(Map.class);
			return jsonResponse;
		} catch (Exception e) {
			logger.error("Error while generating filtererd Tweet Ids Only download link in Persister for collection: "+code, e);
			throw new AidrException(
					"[generateTweetIdsOnlyFilteredLink] Error while generating filtererd Tweet Ids Only download link in Persister",
					e);
		}
	}
	

	@Override
	public Map<String, Object> generateJSONFilteredLink(String code,
			String queryString, String jsonType, String userName, Integer count, boolean removeRetweet)
			throws AidrException {
		try {
			Client client = ClientBuilder.newBuilder()
					.register(JacksonFeature.class).build();
			WebTarget webResource = client.target(persisterMainUrl
					+ "/taggerPersister/filter/genJson?collectionCode=" + code
					+ "&exportLimit=" + count + "&jsonType=" + jsonType
					+ "&userName=" + userName + "&removeRetweet=" + removeRetweet);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(queryString),
					Response.class);
			Map<String, Object> jsonResponse = clientResponse.readEntity(Map.class);
			return jsonResponse;
			/*
			 * if (jsonResponse != null &&
			 * "http".equals(jsonResponse.substring(0, 4))) { return
			 * jsonResponse; } else { return ""; }
			 */
		} catch (Exception e) {
			logger.error("Error while generating filtered JSON download link in Persister for collection: "+code, e);
			throw new AidrException(
					"[generateJSONFilteredLink] Error while generating filtered JSON download link in Persister",
					e);
		}
	}

	// Added by koushik
	@Override
	public Map<String, Object> generateJsonTweetIdsFilteredLink(String code,
			String queryString, String jsonType, String userName)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		//System.out.println("[generateJsonTweetIdsLink] Received request for code: "	+ code);
		try {
			logger.info("[generateJsonTweetIdsLink] Invoked URL: "
					+ persisterMainUrl
					+ "/taggerPersister/filter/genJsonTweetIds?collectionCode="
					+ code + "&downloadLimited=true&" + "&jsonType=" + jsonType
					+ "&userName=" + userName);
			WebTarget webResource = client.target(persisterMainUrl
					+ "/taggerPersister/filter/genJsonTweetIds?collectionCode="
					+ code + "&downloadLimited=true&" + "&jsonType=" + jsonType
					+ "&userName=" + userName);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(queryString),
					Response.class);

			Map<String, Object> jsonResponse = clientResponse
					.readEntity(Map.class);
			//logger.info("Returning from func: " + jsonResponse);
			return jsonResponse;
			/*
			 * if (jsonResponse != null &&
			 * "http".equals(jsonResponse.substring(0, 4))) { return
			 * jsonResponse; } else { return ""; }
			 */
		} catch (Exception e) {
			logger.error("Error while generating filtered JSON Tweet Ids download link in Persister for collection: "+code, e);
			throw new AidrException(
					"[generateJsonTweetIdsFilteredLink] Error while generating filtered JSON Tweet Ids download link in Persister",
					e);
		}
	}
	
	@Override
	public Map<String, Object> generateJsonTweetIdsOnlyFilteredLink(String code,
			String queryString, String jsonType, String userName, Integer exportLimit, Boolean removeRetweet)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		//System.out.println("[generateJsonTweetIdsLink] Received request for code: "	+ code);
		try {
			logger.info("[generateJsonTweetIdsLink] Invoked URL: "
					+ persisterMainUrl
					+ "/taggerPersister/filter/genJsonTweetIdsOnly?collectionCode="
					+ code + "&userName=" + userName + "&exportLimit=" + exportLimit + "&jsonType=" 
					+ jsonType + "&removeRetweet=" + removeRetweet);
			WebTarget webResource = client.target(persisterMainUrl
					+ "/taggerPersister/filter/genJsonTweetIdsOnly?collectionCode="
					+ code + "&userName=" + userName + "&exportLimit=" + exportLimit + "&jsonType=" 
					+ jsonType + "&removeRetweet=" + removeRetweet);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(queryString),
					Response.class);

			Map<String, Object> jsonResponse = clientResponse
					.readEntity(Map.class);
			return jsonResponse;
		} catch (Exception e) {
			logger.error("Error while generating filtered JSON Tweet Ids download link in Persister for collection: "+code, e);
			throw new AidrException(
					"[generateJsonTweetIdsFilteredLink] Error while generating filtered JSON Tweet Ids download link in Persister",
					e);
		}
	}

	private java.util.Collection<TaggerAttribute> convertTaggerCrisesAttributeToDTO(
			List<TaggerCrisesAttribute> attributes, Integer userId) {
		Map<Integer, TaggerAttribute> result = new HashMap<Integer, TaggerAttribute>();
		for (TaggerCrisesAttribute a : attributes) {
			if (!result.containsKey(a.getNominalAttributeID())) {
				if (!userId.equals(a.getUserID())
						&& !(new Integer(1)).equals(a.getUserID())) {
					continue;
				}
				TaggerUser user = new TaggerUser(a.getUserID());
				List<TaggerLabel> labels = new ArrayList<TaggerLabel>();
				TaggerLabel label = new TaggerLabel(a.getLabelName(),
						a.getLabelID());
				labels.add(label);
				TaggerAttribute taggerAttribute = new TaggerAttribute(
						a.getCode(), a.getDescription(), a.getName(),
						a.getNominalAttributeID(), user, labels);
				result.put(a.getNominalAttributeID(), taggerAttribute);
			} else {
				TaggerAttribute taggerAttribute = result.get(a
						.getNominalAttributeID());
				List<TaggerLabel> labels = taggerAttribute
						.getNominalLabelCollection();
				TaggerLabel label = new TaggerLabel(a.getLabelName(),
						a.getLabelID());
				labels.add(label);
			}
		}
		logger.info("Created attributes collection of size = " + result.size());
		//logger.info(result);
		return result.values();
	}

	private int getCurrentRetrainingThreshold() throws Exception {
		try {
			String retrainingThreshold = this.getRetainingThreshold();

			ObjectMapper mapper = JacksonWrapper.getObjectMapper();
			JsonFactory factory = mapper.getJsonFactory(); // since 2.1 use
															// mapper.getFactory()
															// instead
			JsonParser jp = factory.createJsonParser(retrainingThreshold);
			JsonNode actualObj = mapper.readTree(jp);

			JsonNode nameNode = actualObj.get("sampleCountThreshold");

			int sampleCountThreshold = Integer.parseInt(nameNode.asText());

			return sampleCountThreshold;
		} catch (Exception e) {
			logger.error("Exception while getting CurrentRetrainingThreshold", e);
			return 50;

		}
	}

	private void deletePybossaApp(Integer modelFamilyID) {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {

			//System.out.print("removeAttributeFromCrises: starting ......................................");
			WebTarget webResource = client.target(taggerMainUrl
					+ "/modelfamily/" + modelFamilyID);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper
					.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

			Response resp = webResource.request(MediaType.APPLICATION_JSON)
					.get();
			String jsonResp = resp.readEntity(String.class);
			TaggerModelFamily tm = objectMapper.readValue(jsonResp,
					TaggerModelFamily.class);
			if (tm != null && tm.getCrisis() != null
					&& tm.getNominalAttribute() != null) {
				String crisisCode = tm.getCrisis().getCode();
				String attributeCode = tm.getNominalAttribute().getCode();

				logger.info("crisisCode: " + crisisCode + " attributeCode: " + attributeCode);

				WebTarget webResp = client.target(crowdsourcingAPIMainUrl
						+ "/clientapp/delete/" + crisisCode + "/"
						+ attributeCode);

				Response clientResp = webResource.request(
						MediaType.APPLICATION_JSON).get();
				//logger.info("deactivated - clientResponse : " + clientResp);
			} else {
				logger.info("No modelfamily found for id = " + modelFamilyID);
			}
		} catch (Exception e) {
			logger.error("deactivated - deletePybossaApp : " + e);
		}
	}

	@Override
	public String getAttributesAndLabelsByCrisisId(Integer id)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// http://example.com:8084/AIDRTrainerAPI/rest/crisis/id/1234
			WebTarget webResource = client.target(crowdsourcingAPIMainUrl
					+ "/crisis/id/" + id);

			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			logger.info("getAssignableTask - clientResponse : "
					+ clientResponse);

			String jsonResponse = clientResponse.readEntity(String.class);

			return jsonResponse;
		} catch (Exception e) {
			logger.error("Error while getting all nominal attributes and their labels for a given crisisID: "+id, e);
			throw new AidrException(
					"Error while getting all nominal attributes and their labels for a given crisisID",
					e);
		}
	}

	@Override
	public int trashCollection(Collection collection) throws Exception {
		int retVal = 0;
		Long crisisID = -1L;
		//logger.info("[trashCollection] request received for collection: "
				//+ collection.getCode());
		// First clean up the aidr-predict database of documents
		try {
			Client client = ClientBuilder.newBuilder()
					.register(JacksonFeature.class).build();
			WebTarget webResource = client
					.target(taggerMainUrl + "/manage/collection/trash/crisis/" + collection.getCode());
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);
			//logger.info("[trashCollection] response from tagger-api: "
					//+ jsonResponse);
			if (jsonResponse != null && jsonResponse.contains("TRASHED")) {
				retVal = 1;
				crisisID = Long.parseLong(jsonResponse.substring(
						jsonResponse.indexOf(":") + 1,
						jsonResponse.indexOf("}")));
			} else {
				retVal = 0;
			}
		} catch (Exception e) {
			logger.error("Error while attempting trash  a collection: ", e);
			throw new AidrException(
					"Error while deleting a collection from aidr-predict database",
					e);
		}
		//logger.info("[trashCollection] result of cleaning aidr-predict: "
				//+ crisisID);
		if (retVal > 0 && crisisID < 0) {
			return 1; // crisis does not exist in aidr_predict table. Reason: no
						// classifier attached
		}
		if (retVal > 0 && crisisID > 0) {
			// Final DB task - cleanup the aidr-scheduler database of
			// micromapper tasks
			try {
				Client client = ClientBuilder.newBuilder()
						.register(JacksonFeature.class).build();
				WebTarget webResource = client.target(crowdsourcingAPIMainUrl
						+ "/clientapp/delete/crisis/" + crisisID);
				Response clientResponse = webResource.request(
						MediaType.APPLICATION_JSON).get();

				String jsonResponse = clientResponse.readEntity(String.class);
				//logger.info("[trashCollection] response from trainer-api: "
						//+ jsonResponse);
				if (jsonResponse != null
						&& jsonResponse.equalsIgnoreCase("{\"status\":200}")) {
					logger.info("[trashCollection] Success in trashing ");
					return 1;
				} else {
					return 0;
				}
			} catch (Exception e) {
				logger.error("Error while attempting trash REST call for aidr_scheduler for collection: ", e);
				throw new AidrException(
						"Error while attempting trash REST call for aidr_scheduler",
						e);
			}
		}
		return 0;
	}

	@Override
	public int untrashCollection(String collectionCode) throws Exception {
		//System.out.println("[untrashCollection] request received for collection: "+ collectionCode);
		try {
			Client client = ClientBuilder.newBuilder()
					.register(JacksonFeature.class).build();
			WebTarget webResource = client.target(taggerMainUrl
					+ "/manage/collection/untrash/crisis/" + collectionCode);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);
			//System.out.println("[untrashCollection] response from tagger-api: "
				//	+ jsonResponse);
			if (jsonResponse != null
					&& jsonResponse
							.equalsIgnoreCase("{\"status\": \"UNTRASHED\"}")) {
				logger.info("[trashCollection] Success in untrashing + "
						+ collectionCode);
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			logger.error("Error while attempting /untrash REST call for collection: "+collectionCode, e);
			throw new AidrException(
					"Error while attempting /untrash REST call", e);
		}
	}

	@Override
	public String loadLatestTweetsWithCount(String code, int count)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			String constraints = "{\"constraints\":[]}";
			WebTarget webResource = client.target(outputAPIMainUrl
					+ "/crisis/fetch/channel/filter/" + code + "?count="
					+ count);
			/*System.out.println("[loadLatestTweetsWithCount] Invoking: "
					+ outputAPIMainUrl + "/crisis/fetch/channel/filter/" + code
					+ "?count=" + count);
			System.out.println("[loadLatestTweetsWithCount] constraints: "
					+ constraints);*/
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(constraints),
					Response.class);

			String jsonResponse = clientResponse.readEntity(String.class);

			if (jsonResponse != null
					&& (jsonResponse.startsWith("{") || jsonResponse
							.startsWith("["))) {
				/*System.out
						.println("[loadLatestTweetsWithCount] jsonResponse for collection "
								+ code + ": " + jsonResponse);*/
				return jsonResponse;
			} else {
				/*System.out
						.println("[loadLatestTweetsWithCount] jsonResponse for collection "
								+ code + ": \"\"");*/
				return "";
			}
		} catch (Exception e) {
			logger.error("Error while loadLatestTweetsWithCount for collection: "+code, e);
			throw new AidrException("Error while loadLatestTweetsWithCount", e);
		}
	}

	@Override
	public TaggerResponseWrapper getHumanLabeledDocumentsByCrisisID(
			Long crisisID, Integer count) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// Rest call to Tagger
			WebTarget webResource = client.target(taggerMainUrl
					+ "/misc/humanLabeled/crisisID/" + crisisID + "?count="
					+ count);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerResponseWrapper dtoList = objectMapper.readValue(
					jsonResponse, TaggerResponseWrapper.class);
			logger.info("Number of human labeled documents returned by Tagger: "
					+ (dtoList.getHumanLabeledItems() != null ? dtoList
							.getHumanLabeledItems().size() : 0));

			return dtoList;
		} catch (Exception e) {
			logger.error("Error while getting all human labeled documents for crisisID = "
							+ crisisID + " from Tagger", e);
			throw new AidrException(
					"Error while getting all human labeled documents for crisisID = "
							+ crisisID + " from Tagger", e);
		}
	}

	@Override
	public TaggerResponseWrapper getHumanLabeledDocumentsByCrisisCode(
			String crisisCode, Integer count) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// Rest call to Tagger
			WebTarget webResource = client.target(taggerMainUrl
					+ "/misc/humanLabeled/crisisCode/" + crisisCode + "?count="
					+ count);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerResponseWrapper dtoList = objectMapper.readValue(
					jsonResponse, TaggerResponseWrapper.class);
			logger.info("Number of human labeled documents returned by Tagger: "
					+ (dtoList.getHumanLabeledItems() != null ? dtoList
							.getHumanLabeledItems().size() : 0));

			return dtoList;
		} catch (Exception e) {
			logger.error("Error while getting all human labeled documents for crisis code = "
							+ crisisCode + " from Tagger", e);
			throw new AidrException(
					"Error while getting all human labeled documents for crisis code = "
							+ crisisCode + " from Tagger", e);
		}
	}

	@Override
	public TaggerResponseWrapper getHumanLabeledDocumentsByCrisisIDUserID(
			Long crisisID, Long userID, Integer count) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// Rest call to Tagger
			WebTarget webResource = client.target(taggerMainUrl
					+ "/misc/humanLabeled/crisisID/" + crisisID + "/userID/"
					+ userID + "?count=" + count);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerResponseWrapper dtoList = objectMapper.readValue(
					jsonResponse, TaggerResponseWrapper.class);
			logger.info("Number of human labeled documents returned by Tagger: "
					+ (dtoList.getHumanLabeledItems() != null ? dtoList
							.getHumanLabeledItems().size() : 0));

			return dtoList;
		} catch (Exception e) {
			logger.error("Error while getting all human labeled documents for crisisID = "
							+ crisisID + ", userId = " + userID
							+ " from Tagger", e);
			throw new AidrException(
					"Error while getting all human labeled documents for crisisID = "
							+ crisisID + ", userId = " + userID
							+ " from Tagger", e);
		}
	}

	@Override
	public TaggerResponseWrapper getHumanLabeledDocumentsByCrisisIDUserName(
			Long crisisID, String userName, Integer count) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			// Rest call to Tagger
			WebTarget webResource = client.target(taggerMainUrl
					+ "/misc/humanLabeled/crisisID/" + crisisID + "/userName/"
					+ userName + "?count=" + count);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();

			String jsonResponse = clientResponse.readEntity(String.class);

			TaggerResponseWrapper dtoList = objectMapper.readValue(
					jsonResponse, TaggerResponseWrapper.class);
			logger.info("Number of human labeled documents returned by Tagger: "
					+ (dtoList.getHumanLabeledItems() != null ? dtoList
							.getHumanLabeledItems().size() : 0));

			return dtoList;
		} catch (Exception e) {
			logger.error("Error while getting all human labeled documents for crisisID = "
							+ crisisID + ", user name = " + userName
							+ " from Tagger", e);
			throw new AidrException(
					"Error while getting all human labeled documents for crisisID = "
							+ crisisID + ", user name = " + userName
							+ " from Tagger", e);
		}
	}

	@Override
	public Map<String, Object> downloadHumanLabeledDocumentsByCrisisUserName(
			String queryString, String crisisCode, String userName,
			Integer count, String fileType, String contentType)
			throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		
		String targetURL = taggerMainUrl
				+ "/misc/humanLabeled/download/crisis/" + crisisCode
				+ "/userName/" + userName + "?count=" + count
				+ "&fileType=" + fileType + "&contentType=" + contentType;
		
		logger.info("Going to invoke REST API: " + targetURL + " POST body: " + queryString);
		try {
			// Rest call to Tagger
			WebTarget webResource = client.target(targetURL);

			ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();
			objectMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).post(Entity.json(queryString),
					Response.class);
			String jsonResponse = clientResponse.readEntity(String.class);
			//logger.info("Response = " + jsonResponse);
			TaggerResponseWrapper response = objectMapper.readValue(
					jsonResponse, TaggerResponseWrapper.class);
			logger.info("Number of human labeled documents returned by Tagger: "
					+ response.getTotal());

			Map<String, Object> retVal = new HashMap<String, Object>();
			retVal.put("fileName", response.getMessage());
			retVal.put("total", response.getTotal());
			return retVal;
		} catch (Exception e) {
			logger.error("Error while getting download link for human labeled documents for crisis code = "
							+ crisisCode+ ", user name = "+ userName+ " from Tagger", e);
			throw new AidrException("Error while getting download link for human labeled documents for crisis code = "
					+ crisisCode+ ", user name = "+ userName+ " from Tagger", e);
		}
	}

	@Override
	public Map<String, Object> updateMicromapperEnabled(String code, Boolean isMicromapperEnabled) throws AidrException {
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		try {
			WebTarget webResource = client.target(taggerMainUrl
					+ "/crisis/update/micromapperEnabled/" + code +"/"+isMicromapperEnabled);
			Response clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			Map<String, Object> jsonResponse = clientResponse.readEntity(Map.class);
			return jsonResponse;
		} catch (Exception e) {
			logger.error("Exception while updating isMicromapperEnabled ", e);
			throw new AidrException("Exception while updating isMicromapperEnabled ",
					e);
		}
	}
	
	@Override
	public Boolean sendMailService(String subject, String body){
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		Response clientResponse = null;
		try {
			WebTarget webResource = client.target(taggerMainUrl
					+ "/misc/sendEmail");
			Form form = new Form();
			form.param("subject", subject);
			form.param("body", body);
			
			clientResponse = webResource.request().post(
					Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED),Response.class);
			if (clientResponse.getStatus() != 200) {
				logger.warn("Couldn't contact AIDRTaggerAPI for sending error message");
				return false;
			}
		} catch (Exception e) {
			logger.error("Error in contacting AIDRTaggerAPI: " + clientResponse);
			return false;
		}
		return true;
	}
	
	@Override
	public Long getLabelCount(Long collectionId) {
		
		Long labelCount = 0L;
		Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();
		Response clientResponse = null;
		try {
			WebTarget webResource = client.target(taggerMainUrl
					+ "/label/collection/" + collectionId);
			clientResponse = webResource.request(
					MediaType.APPLICATION_JSON).get();
			
			labelCount = clientResponse.readEntity(Long.class);
			
			if (clientResponse.getStatus() != 200) {
				logger.warn("Couldn't contact AIDRTaggerAPI for sending error message");
			}
		} catch (Exception e) {
			logger.error("Error in contacting AIDRTaggerAPI: " + clientResponse, e);
		}
		
		return labelCount;
	}
}
