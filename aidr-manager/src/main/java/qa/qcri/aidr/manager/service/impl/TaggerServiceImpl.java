package qa.qcri.aidr.manager.service.impl;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.util.JSONPObject;
import qa.qcri.aidr.manager.dto.*;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.service.TaggerService;

//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import org.glassfish.jersey.jackson.JacksonFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Service("taggerService")
public class TaggerServiceImpl implements TaggerService {

    private Logger logger = Logger.getLogger(getClass());

    //@Autowired
    //private Client client;

    @Value("${taggerMainUrl}")
    private String taggerMainUrl;

    @Value("${crowdsourcingAPIMainUrl}")
    private String crowdsourcingAPIMainUrl;

    @Value("${persisterMainUrl}")
    private String persisterMainUrl;

    @Value("${outputAPIMainUrl}")
    private String outputAPIMainUrl;

    @Override
    public List<TaggerCrisisType> getAllCrisisTypes() throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/crisisType/all");
        	WebTarget webResource = client.target(taggerMainUrl + "/crisisType/all");
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
        	//String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class); 


            TaggerAllCrisesTypesResponse crisesTypesResponse = objectMapper.readValue(jsonResponse, TaggerAllCrisesTypesResponse.class);
            if (crisesTypesResponse.getCrisisTypes() != null) {
                logger.info("Tagger returned " + crisesTypesResponse.getCrisisTypes().size() + " crises types");
            }

            return crisesTypesResponse.getCrisisTypes();
        } catch (Exception e) {
            throw new AidrException("Error while getting all crisis from Tagger", e);
        }
    }

    @Override
    public List<TaggerCrisis> getCrisesByUserId(Integer userId) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/crisis?userID=" + userId);
        	WebTarget webResource = client.target(taggerMainUrl + "/crisis?userID=" + userId);
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
        	//String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class); 

            TaggerAllCrisesResponse taggerAllCrisesResponse = objectMapper.readValue(jsonResponse, TaggerAllCrisesResponse.class);
            if (taggerAllCrisesResponse.getCrisises() != null) {
                logger.info("Tagger returned " + taggerAllCrisesResponse.getCrisises().size() + " crisis for user");
            }

            return taggerAllCrisesResponse.getCrisises();
        } catch (Exception e) {
            throw new AidrException("No collection is enabled for Tagger. Please enable tagger for one of your collections.", e);
        }
    }

    @Override
    public String createNewCrises(TaggerCrisisRequest crisis) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/crisis");
        	WebTarget webResource = client.target(taggerMainUrl + "/crisis");
            
            ObjectMapper objectMapper = new ObjectMapper();

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .post(ClientResponse.class, objectMapper.writeValueAsString(crisis));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(objectMapper.writeValueAsString(crisis)), Response.class);
            
            //return clientResponse.getEntity(String.class);
            return clientResponse.readEntity(String.class);
        } catch (Exception e) {
            throw new AidrException("Error while creating new crises in Tagger", e);
        }
    }

    @Override
    public Collection<TaggerAttribute> getAttributesForCrises(Integer crisisID, Integer userId) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/attribute/crisis/all?exceptCrisis=" + crisisID);
        	WebTarget webResource = client.target(taggerMainUrl + "/attribute/crisis/all?exceptCrisis=" + crisisID);
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
        	//String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class); 

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

    @Override
    public TaggerCrisisExist isCrisesExist(String code) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(taggerMainUrl + "/crisis/code/" + code);
        	WebTarget webResource = client.target(taggerMainUrl + "/crisis/code/" + code);
        	
            ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);
            
            
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

    @Override
    public Integer isUserExistsByUsername(String userName) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/user/" + userName);
        	WebTarget webResource = client.target(taggerMainUrl + "/user/" + userName);
        	
        	ObjectMapper objectMapper = new ObjectMapper();

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //         .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
            //String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);
        	
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

    @Override
    public Integer addNewUser(TaggerUser taggerUser) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/user");
        	WebTarget webResource = client.target(taggerMainUrl + "/user");
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .post(ClientResponse.class, objectMapper.writeValueAsString(taggerUser));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(objectMapper.writeValueAsString(taggerUser)), Response.class);
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);
            
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

    @Override
    public Integer addAttributeToCrisis(TaggerModelFamily modelFamily) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/modelfamily");
        	WebTarget webResource = client.target(taggerMainUrl + "/modelfamily");
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .post(ClientResponse.class, objectMapper.writeValueAsString(modelFamily));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(objectMapper.writeValueAsString(modelFamily)), Response.class);
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);
            
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

    @Override
    public TaggerCrisis getCrisesByCode(String code) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/crisis/by-code/" + code);
        	WebTarget webResource = client.target(taggerMainUrl + "/crisis/by-code/" + code);
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
            //String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);

            TaggerCrisis crisis = objectMapper.readValue(jsonResponse, TaggerCrisis.class);
            if (crisis != null) {
                logger.info("Tagger returned crisis with code" + crisis.getCode());
            }

            return crisis;
        } catch (Exception e) {
            throw new AidrException("Error while getting crisis by code from Tagger", e);
        }
    }

    @Override
    public TaggerCrisis updateCode(TaggerCrisis crisis) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/crisis");
        	WebTarget webResource = client.target(taggerMainUrl + "/crisis");
        	
            ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .put(ClientResponse.class, objectMapper.writeValueAsString(crisis));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.put(Entity.json(objectMapper.writeValueAsString(crisis)), Response.class);
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

            TaggerCrisis updatedCrisis = objectMapper.readValue(jsonResponse, TaggerCrisis.class);
            if (updatedCrisis != null) {
                logger.info("Crisis with id " + updatedCrisis.getCrisisID() + " was updated in Tagger");
            }

            return crisis;
        } catch (Exception e) {
            throw new AidrException("Error while getting crisis by code from Tagger", e);
        }
    }

    @Override
    public List<TaggerModel> getModelsForCrisis(Integer crisisID) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/model/crisis/" + crisisID);

            int retrainingThreshold = getCurrentRetrainingThreshold();
            WebTarget webResource = client.target(taggerMainUrl + "/model/crisis/" + crisisID);
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
        	//String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);

            TaggerCrisisModelsResponse crisisModelsResponse = objectMapper.readValue(jsonResponse, TaggerCrisisModelsResponse.class);
            if (crisisModelsResponse.getModelWrapper() != null) {
                logger.info("Tagger returned " + crisisModelsResponse.getModelWrapper().size() + " models for crises with ID " + crisisID);
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

                   // System.out.println("reset : " + tm.getRetrainingThreshold());
                    tempTaggerModel.add(tm);
                }

                return tempTaggerModel;
            }
            return null;
        } catch (Exception e) {
            throw new AidrException("Error while getting all models for crisis from Tagger", e);
        }
    }

    @Override
    public TaggerAttribute createNewAttribute(TaggerAttribute attribute) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/attribute");
        	WebTarget webResource = client.target(taggerMainUrl + "/attribute");
        	
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .post(ClientResponse.class, objectMapper.writeValueAsString(attribute));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(objectMapper.writeValueAsString(attribute)), Response.class);
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

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

    @Override
    public TaggerAttribute getAttributeInfo(Integer id) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/attribute/" + id);
            WebTarget webResource = client.target(taggerMainUrl + "/attribute/" + id);
            
            ObjectMapper objectMapper = new ObjectMapper();

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

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

    @Override
    public TaggerLabel getLabelInfo(Integer id) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/label/" + id);
        	WebTarget webResource = client.target(taggerMainUrl + "/label/" + id);
            
            ObjectMapper objectMapper = new ObjectMapper();

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);
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

    @Override
    public boolean deleteAttribute(Integer id) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/attribute/" + id);
        	WebTarget webResource = client.target(taggerMainUrl + "/attribute/" + id);
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .delete(ClientResponse.class);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).delete();
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

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

    @Override
    public boolean deleteTrainingExample(Integer id) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/document/removeTrainingExample/" + id);
        	WebTarget webResource = client.target(taggerMainUrl + "/document/removeTrainingExample/" + id);
            
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .delete(ClientResponse.class);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).delete();
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);
            
            TaggerStatusResponse response = objectMapper.readValue(jsonResponse, TaggerStatusResponse.class);
            if (response != null && response.getStatusCode() != null) {
                if ("SUCCESS".equals(response.getStatusCode())) {
                    logger.info("Document with ID " + id + " was deleted in Tagger");
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            throw new AidrException("Error while deleting document in Tagger", e);
        }
    }

    @Override
    public boolean removeAttributeFromCrises(Integer modelFamilyID) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            deletePybossaApp(modelFamilyID);
            //WebResource webResource = client.resource(taggerMainUrl + "/modelfamily/" + modelFamilyID);
        	WebTarget webResource = client.target(taggerMainUrl + "/modelfamily/" + modelFamilyID);
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .delete(ClientResponse.class);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).delete();
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);
            
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

    @Override
    public TaggerAttribute updateAttribute(TaggerAttribute attribute) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/attribute");
        	WebTarget webResource = client.target(taggerMainUrl + "/attribute");
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .put(ClientResponse.class, objectMapper.writeValueAsString(attribute));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.put(Entity.json(objectMapper.writeValueAsString(attribute)), Response.class);
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

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

    @Override
    public TaggerLabel updateLabel(TaggerLabelRequest label) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/label");
        	WebTarget webResource = client.target(taggerMainUrl + "/label");
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .put(ClientResponse.class, objectMapper.writeValueAsString(label));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.put(Entity.json(objectMapper.writeValueAsString(label)), Response.class);
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);
            
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

    @Override
    public TaggerLabel createNewLabel(TaggerLabelRequest label) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/label");
        	WebTarget webResource = client.target(taggerMainUrl + "/label");
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .post(ClientResponse.class, objectMapper.writeValueAsString(label));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(objectMapper.writeValueAsString(label)), Response.class);
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);
            
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

    @Override
    public TaggerAttribute attributeExists(String code) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(taggerMainUrl + "/attribute/code/" + code);
        	WebTarget webResource = client.target(taggerMainUrl + "/attribute/code/" + code);
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
        	//String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);

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

    @Override
    public List<TrainingDataDTO> getTrainingDataByModelIdAndCrisisId(Integer modelFamilyId,
                                                                     Integer crisisId,
                                                                     Integer start,
                                                                     Integer limit,
                                                                     String sortColumn,
                                                                     String sortDirection) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(taggerMainUrl + "/misc/getTrainingData?crisisID=" + crisisId
            //        + "&modelFamilyID=" + modelFamilyId
            //        + "&fromRecord=" + start
            //        + "&limit=" + limit
            //        + "&sortColumn=" + sortColumn
            //        + "&sortDirection=" + sortDirection);
        	WebTarget webResource = client.target(taggerMainUrl + "/misc/getTrainingData?crisisID=" + crisisId
                            + "&modelFamilyID=" + modelFamilyId
                            + "&fromRecord=" + start
                            + "&limit=" + limit
                            + "&sortColumn=" + sortColumn
                            + "&sortDirection=" + sortDirection);
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
        	//String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);

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
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
//            taskBufferNumber currently always 1
            int taskBufferNumber = 1;
            //WebResource webResource = client.resource(crowdsourcingAPIMainUrl + "/taskbuffer/getassignabletask/" + userName + "/" + id + "/" + taskBufferNumber);
            WebTarget webResource = client.target(crowdsourcingAPIMainUrl 
            		+ "/document/getassignabletask/"
            		+ userName + "/" + id + "/" + taskBufferNumber);
            
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            logger.info("getAssignableTask - clientResponse : " + clientResponse);
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

            return jsonResponse;
        } catch (Exception e) {
            throw new AidrException("Error while getting Assignable Task in Tagger", e);
        }
    }

    @Override
    public String getTemplateStatus(String code) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(crowdsourcingAPIMainUrl + "/template/status/crisis/code/" + code);
        	WebTarget webResource = client.target(crowdsourcingAPIMainUrl + "/template/status/crisis/code/" + code);
        	
        	//ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	logger.info("getTemplateStatus - clientResponse : " + clientResponse);
        	
            //String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);

            return jsonResponse;
        } catch (Exception e) {
            throw new AidrException("Error while getting Template Status in Tagger", e);
        }
    }

    @Override
    public String skipTask(Integer id, String userName) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(crowdsourcingAPIMainUrl + "/taskassignment/revert/searchByDocUserName/" + userName + "/" + id);
        	WebTarget webResource = client.target(crowdsourcingAPIMainUrl 
        			+ "/taskassignment/revert/searchByDocUserName/" + userName + "/" + id);
        	
        	//ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
            logger.info("skipTask - clientResponse : " + clientResponse);
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

            return jsonResponse;
        } catch (Exception e) {
            throw new AidrException("Error while Skip Task operation", e);
        }
    }

    @Override
    public boolean saveTaskAnswer(List<TaskAnswer> taskAnswer) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(crowdsourcingAPIMainUrl + "/taskanswer/save");
        	WebTarget webResource = client.target(crowdsourcingAPIMainUrl + "/taskanswer/save");
            ObjectMapper objectMapper = new ObjectMapper();

            logger.info("saveTaskAnswer - postData : " + objectMapper.writeValueAsString(taskAnswer));
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .post(ClientResponse.class, objectMapper.writeValueAsString(taskAnswer));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(objectMapper.writeValueAsString(taskAnswer)), Response.class);
            logger.info("saveTaskAnswer - response status : " + clientResponse.getStatus());

            return clientResponse.getStatus() == 204;
        } catch (Exception e) {
            throw new AidrException("Error while saving TaskAnswer in AIDRCrowdsourcing", e);
        }
    }

    @Override
    public String generateCSVLink(String code) throws AidrException {
    	Client client = ClientBuilder.newBuilder().build();
    	try {
            //WebResource webResource = client.resource(persisterMainUrl + "/taggerPersister/genCSV?collectionCode=" + code + "&exportLimit=100000");
        	WebTarget webResource = client.target(persisterMainUrl + 
        			"/taggerPersister/genCSV?collectionCode=" + code + "&exportLimit=100000");
        	
        	//ClientResponse clientResponse = webResource.type(MediaType.TEXT_PLAIN)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
        	//String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);

            if (jsonResponse != null && "http".equals(jsonResponse.substring(0, 4))) {
                return jsonResponse;
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new AidrException("Error while generating CSV link in taggerPersister", e);
        }
    }

    @Override
    public String generateTweetIdsLink(String code) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(persisterMainUrl + "/taggerPersister/genTweetIds?collectionCode=" + code);
        	WebTarget webResource = client.target(persisterMainUrl + "/taggerPersister/genTweetIds?collectionCode=" + code);
        	
        	//ClientResponse clientResponse = webResource.type(MediaType.TEXT_PLAIN)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            
        	//String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);


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
    public String loadLatestTweets(String code) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(outputAPIMainUrl + "/crisis/fetch/channel/" + code + "?count=1000");
        	WebTarget webResource = client.target(outputAPIMainUrl + "/crisis/fetch/channel/" + code + "?count=1000");
            
            //ClientResponse clientResponse = webResource.type(MediaType.TEXT_PLAIN)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
            //String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);

            if (jsonResponse != null && jsonResponse.startsWith("[")) {
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
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(taggerMainUrl + "/model/modelFamily/" + id
            //        + "?start=" + start
            //        + "&limit=" + limit);
            WebTarget webResource = client.target(taggerMainUrl + "/model/modelFamily/" + id
                    + "?start=" + start
                    + "&limit=" + limit);
            
            ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

            ModelHistoryWrapper modelHistoryWrapper = objectMapper.readValue(jsonResponse, ModelHistoryWrapper.class);
            return modelHistoryWrapper;
        } catch (Exception e) {
            throw new AidrException("Error while Getting history records for Model.", e);
        }
    }

    @Override
    public List<TaggerModelNominalLabel> getAllLabelsForModel(Integer modelID) throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/modelNominalLabel/" + modelID);
        	WebTarget webResource = client.target(taggerMainUrl + "/modelNominalLabel/" + modelID);
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
        	
        	//String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);

            TaggerModelLabelsResponse modelLabelsResponse = objectMapper.readValue(jsonResponse, TaggerModelLabelsResponse.class);
            if (modelLabelsResponse.getModelNominalLabelsDTO() != null) {
                logger.info("Tagger returned " + modelLabelsResponse.getModelNominalLabelsDTO().size() + " labels for model with ID " + modelID);
            }

            return modelLabelsResponse.getModelNominalLabelsDTO();
        } catch (Exception e) {
            throw new AidrException("Error while getting all labels for model from Tagger", e);
        }
    }

    @Override
    public String getRetainingThreshold() throws AidrException {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

        try {

            WebTarget webResource = client.target(taggerMainUrl + "/train/samplecountthreshold");

            ObjectMapper objectMapper = new ObjectMapper();
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();

            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

            return  jsonResponse;
        } catch (Exception e) {
            throw new AidrException("getRetainingThreshold : ", e);

        }

    }


    public Map<String, Integer> getTaggersForCollections(List<String> collectionCodes) throws AidrException {
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            /**
             * Rest call to Tagger
             */
            //WebResource webResource = client.resource(taggerMainUrl + "/modelfamily/taggers-by-codes");
        	WebTarget webResource = client.target(taggerMainUrl + "/modelfamily/taggers-by-codes");
        	
        	ObjectMapper objectMapper = new ObjectMapper();

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .post(ClientResponse.class, objectMapper.writeValueAsString(new TaggersForCollectionsRequest(collectionCodes)));
        	Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(objectMapper.writeValueAsString(new TaggersForCollectionsRequest(collectionCodes))), Response.class);
        	
            //String jsonResponse = clientResponse.getEntity(String.class);
        	String jsonResponse = clientResponse.readEntity(String.class);
        	
            TaggersForCollectionsResponse taggersResponse = objectMapper.readValue(jsonResponse, TaggersForCollectionsResponse.class);
            if (taggersResponse != null && !taggersResponse.getTaggersForCodes().isEmpty()) {
                Map<String, Integer> result = new HashMap<String, Integer>();
                for (TaggersForCodes taggerForCode : taggersResponse.getTaggersForCodes()){
                    result.put(taggerForCode.getCode(), taggerForCode.getCount());
                }
                return result;
            } else {
                return Collections.emptyMap();
            }
        } catch (Exception e) {
            throw new AidrException("Error while adding new user to Tagger", e);
        }
    }

    @Override
    public boolean pingTagger() throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(taggerMainUrl + "/misc/ping");
        	WebTarget webResource = client.target(taggerMainUrl + "/misc/ping");
        	
            ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

            PingResponse pingResponse = objectMapper.readValue(jsonResponse, PingResponse.class);
            if (pingResponse != null && "RUNNING".equals(pingResponse.getStatus())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new AidrException("Error while Getting training data for Crisis and Model.", e);
        }
    }

    @Override
    public boolean pingTrainer() throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(crowdsourcingAPIMainUrl + "/util/ping/heartbeat");
    		WebTarget webResource = client.target(crowdsourcingAPIMainUrl + "/util/ping/heartbeat");
    		
            ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            
            //String jsonResponse = clientResponse.getEntity(String.class);
            String jsonResponse = clientResponse.readEntity(String.class);

            PingResponse pingResponse = objectMapper.readValue(jsonResponse, PingResponse.class);
            if (pingResponse != null && "200".equals(pingResponse.getStatus())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new AidrException("Error while Getting training data for Crisis and Model.", e);
        }
    }

    @Override
    public boolean pingAIDROutput() throws AidrException{
    	Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    	try {
            //WebResource webResource = client.resource(outputAPIMainUrl + "/manage/ping");
        	WebTarget webResource = client.target(outputAPIMainUrl + "/manage/ping");
        	
            ObjectMapper objectMapper = new ObjectMapper();
            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
			Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            
            //String jsonResponse = clientResponse.getEntity(String.class);
			String jsonResponse = clientResponse.readEntity(String.class);
			
            PingResponse pingResponse = objectMapper.readValue(jsonResponse, PingResponse.class);
            if (pingResponse != null && "RUNNING".equals(pingResponse.getStatus())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new AidrException("Error while Getting training data for Crisis and Model.", e);
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

    private int getCurrentRetrainingThreshold() throws Exception{
        try{
            String retrainingThreshold = this.getRetainingThreshold();

            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getJsonFactory(); // since 2.1 use mapper.getFactory() instead
            JsonParser jp = factory.createJsonParser(retrainingThreshold);
            JsonNode actualObj = mapper.readTree(jp);

            JsonNode nameNode = actualObj.get("sampleCountThreshold");

            int sampleCountThreshold = Integer.parseInt(nameNode.asText()) ;

            return sampleCountThreshold;
        }
        catch(Exception e) {
            return 50;

        }
    }

    private void deletePybossaApp(Integer modelFamilyID){
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try{

            System.out.print("removeAttributeFromCrises: starting ......................................");
            //WebResource webResource = client.resource(taggerMainUrl + "/modelfamily/" + modelFamilyID);
            WebTarget webResource = client.target(taggerMainUrl + "/modelfamily/" + modelFamilyID);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .delete(ClientResponse.class);
            Response resp = webResource.request(MediaType.APPLICATION_JSON).get();
            String jsonResp = resp.readEntity(String.class);
            TaggerModelFamily tm = objectMapper.readValue(jsonResp, TaggerModelFamily.class);
            String crisisCode = tm.getCrisis().getCode();
            String attributeCode = tm.getNominalAttribute().getCode();

            System.out.print("crisisCode: " + crisisCode);
            System.out.print("attributeCode: " + attributeCode);

            WebTarget webResp = client.target(crowdsourcingAPIMainUrl+ "/clientapp/delete/" + crisisCode + "/" + attributeCode );

            //ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON)
            //        .accept(MediaType.APPLICATION_JSON)
            //        .get(ClientResponse.class);
            Response clientResp = webResource.request(MediaType.APPLICATION_JSON).get();
            logger.info("deactivated - clientResponse : " + clientResp);
        }
        catch(Exception e){
            logger.error("deactivated - deletePybossaApp : " + e);
        }

    }

}
