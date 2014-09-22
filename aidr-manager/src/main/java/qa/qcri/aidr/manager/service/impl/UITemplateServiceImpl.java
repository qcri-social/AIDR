package qa.qcri.aidr.manager.service.impl;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.common.code.JacksonWrapper;
import qa.qcri.aidr.manager.dto.UITemplateRequest;
import qa.qcri.aidr.manager.dto.UITemplateResponse;
import qa.qcri.aidr.manager.exception.AidrException;
import qa.qcri.aidr.manager.service.UITemplateService;
import qa.qcri.aidr.manager.util.CustomUITemplateLookup;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/23/14
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("uitemplateService")
public class UITemplateServiceImpl implements UITemplateService {

    private Logger logger = Logger.getLogger(getClass());

    @Value("${taggerMainUrl}")
    private String taggerMainUrl;

    @Value("${crowdsourcingAPIMainUrl}")
    private String crowdsourcingAPIMainUrl;

    @Value("${persisterMainUrl}")
    private String persisterMainUrl;

    @Value("${outputAPIMainUrl}")
    private String outputAPIMainUrl;


    @Override
    public UITemplateRequest updateTemplate(UITemplateRequest uiTemplateRequest) throws AidrException {
        //  return null;  //To change body of implemented methods use File | Settings | File Templates.
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {

            WebTarget webResource = client.target(taggerMainUrl + "/customuitemplate");
            ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();

            logger.info("updateTemplate - updateTemplate : " + objectMapper.writeValueAsString(uiTemplateRequest));

            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(objectMapper.writeValueAsString(uiTemplateRequest)), Response.class);


            String jsonResponse = clientResponse.readEntity(String.class);
            System.out.println("jsonResponse: " + jsonResponse);
            UITemplateRequest response = objectMapper.readValue(jsonResponse, UITemplateRequest.class);
            if (response != null) {

                if(uiTemplateRequest.getTemplateType().equals(CustomUITemplateLookup.CLASSIFIER_WELCOME_PAGE)){
                    webResource = client.target(crowdsourcingAPIMainUrl + "/customUI/welcome/update");
                    Response clientResponse2 = webResource.request(MediaType.APPLICATION_JSON)
                            .post(Entity.json(objectMapper.writeValueAsString(uiTemplateRequest)), Response.class);
                }

                if(uiTemplateRequest.getTemplateType().equals(CustomUITemplateLookup.CLASSIFIER_SKIN)){
                    webResource = client.target(crowdsourcingAPIMainUrl + "/customUI/skin/update");
                    Response clientResponse2 = webResource.request(MediaType.APPLICATION_JSON)
                            .post(Entity.json(objectMapper.writeValueAsString(uiTemplateRequest)), Response.class);
                }

                if(uiTemplateRequest.getTemplateType().equals(CustomUITemplateLookup.CLASSIFIER_TUTORIAL_ONE) ||
                        uiTemplateRequest.getTemplateType().equals(CustomUITemplateLookup.CLASSIFIER_TUTORIAL_ONE) ){
                    webResource = client.target(crowdsourcingAPIMainUrl + "/customUI/tutorial/update");
                    Response clientResponse2 = webResource.request(MediaType.APPLICATION_JSON)
                            .post(Entity.json(objectMapper.writeValueAsString(uiTemplateRequest)), Response.class);
                }


                logger.info("updateTemplate - updateTemplate with ID " + response + " was created in Tagger");
                return response;
            }
            return null;
        } catch (Exception e) {
            throw new AidrException("Error while creating new template in Tagger", e);
        }

    }

    @Override
    public String getTemplatesByCrisisID(long crisisID) throws AidrException {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {
            System.out.println("getPublicLandingPageTemplate: " + crisisID);
            System.out.println("url: " + taggerMainUrl + "/customuitemplate/crisisID/" + crisisID);

            WebTarget webResource = client.target(taggerMainUrl + "/customuitemplate/crisisID/" + crisisID);
            ObjectMapper objectMapper = JacksonWrapper.getObjectMapper();

            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            String jsonResponse = clientResponse.readEntity(String.class);
            //System.out.println("jsonResponse: " + jsonResponse);

            if (jsonResponse != null) {
                logger.info("getPublicLandingPageTemplate");
                return jsonResponse;
            }
            return null;
        } catch (Exception e) {
            throw new AidrException("Error while creating new template in Tagger", e);
        }
    }

    @Override
    public String getCrisisChildrenElement(Integer id) throws AidrException {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {
             WebTarget webResource = client.target(crowdsourcingAPIMainUrl
                    + "/crisis/id/" + id);

            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            logger.info("getCrisisChildrenElement - clientResponse : " + clientResponse);

            String jsonResponse = clientResponse.readEntity(String.class);

            return jsonResponse;
        } catch (Exception e) {
            throw new AidrException("Error while getting CrisisChildrenElement in UITemplate", e);
        }
    }


}
