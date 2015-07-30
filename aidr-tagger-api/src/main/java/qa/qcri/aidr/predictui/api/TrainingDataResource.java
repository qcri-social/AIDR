/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurationProperty;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurator;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/train")
@Stateless
public class TrainingDataResource {

    @Context
    private UriInfo context;
    @EJB
    private CrisisResourceFacade crisisLocalEJB;

    public TrainingDataResource() {
    }
    
    //private static Logger logger = Logger.getLogger(TrainingDataResource.class);
    private static Logger logger = Logger.getLogger(TrainingDataResource.class);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{crisisCode}/getItem")
    public Response getTweetToTag(@PathParam("crisisCode") Long crisisId) {
        CrisisDTO crisis = null;
        try {
            crisis = crisisLocalEJB.getCrisisByID(crisisId);
            return Response.ok(crisis).build();
        } catch (RuntimeException e) {
            logger.error("Error in getting tweet to tag for crisis: " + crisisId);
        	return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
        }
      
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/by-code/{code}")
    public Response getCrisisByCode(@PathParam("code") String crisisCode) {
        CrisisDTO crisis = null;
        try {
            crisis = crisisLocalEJB.getCrisisByCode(crisisCode);
            return Response.ok(crisis).build();
        } catch (RuntimeException e) {
            logger.error("Error in getting crisis by code: " + crisisCode);
        	return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
        }
  
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/code/{code}")
    public Response isCrisisExists(@PathParam("code") String crisisCode) {
        long crisisId = 0;
        CrisisDTO crisis = crisisLocalEJB.getCrisisByCode(crisisCode);
        if (crisis != null) {
            crisisId =  crisis.getCrisisID();
        }
        //TODO: Following way of creating JSON should be changed through a proper and automatic way
        String response = "{\"crisisCode\":\"" + crisisCode + "\", \"crisisId\":\"" + crisisId + "\"}";
        return Response.ok(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllCrisis() {
        List<CrisisDTO> crisisList = crisisLocalEJB.getAllCrisis();
        ResponseWrapper response = new ResponseWrapper();
        response.setMessage(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
        response.setCrisises(crisisList);
        return Response.ok(response).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseWrapper getAllCrisisByUserID(@QueryParam("userID") Long userID) throws Exception {
        List<CrisisDTO> crisisList = crisisLocalEJB.getAllCrisisByUserID(userID);
        ResponseWrapper response = new ResponseWrapper();
        if (crisisList == null) {
            response.setMessage("No crisis found associated with the given user id.");
            return response;
        }
        response.setCrisises(crisisList);
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCrisis(CrisisDTO crisis) {
        try {
            CrisisDTO newCrisis = crisisLocalEJB.addCrisis(crisis);
        } catch (RuntimeException e) {
        	logger.error("Error while adding crisis: " + crisis.getCode() + ". Possible causes could be duplication of primary key, incomplete data, incompatible data format.");
            return Response.ok("Error while adding Crisis. Possible causes could be duplication of primary key, incomplete data, incompatible data format.").build();
        }

        return Response.ok(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS)).build();

    }

    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editCrisis(CrisisDTO crisis) {
        try {
            CrisisDTO dto = crisisLocalEJB.editCrisis(crisis);
            return Response.ok(dto).build();
        } catch (RuntimeException e) {
            logger.error("Error in editing crisis: " + crisis.getCode());
        	return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
        }
    }

    /*
    private CrisisDTO transformCrisisToDto(Crisis c){
        CrisisTypeDTO typeDTO = null;
        if (c.getCrisisType() != null) {
            typeDTO = new CrisisTypeDTO(c.getCrisisType().getCrisisTypeID(), c.getCrisisType().getName());
        }
        CrisisDTO dto = new CrisisDTO();
        dto.setCode(c.getCode());
        dto.setName(c.getName());
        dto.setCrisisID(c.getCrisisID().intValue());
        dto.setCrisisType(typeDTO);
        return dto;
    }
	*/
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/samplecountthreshold")
    public Response getSampleCountThreshold(){

        Properties prop = new Properties();
        int sampleCountThreshold = 20;
        try {
            prop.load(new FileInputStream(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.AIDR_TAGGER_CONFIG_URL)));
            String value = prop.getProperty("sampleCountThreshold") ;

            if(value != null){
                sampleCountThreshold =   Integer.parseInt(prop
                        .getProperty("sampleCountThreshold"));
            }

        } catch (FileNotFoundException ex1) {
            logger.error("Couldn't create input stream for file" + TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.AIDR_TAGGER_CONFIG_URL));
            throw new RuntimeException(ex1);
        } catch (IOException ex2) {
        	logger.error("Couldn't load file" + TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.AIDR_TAGGER_CONFIG_URL));
            throw new RuntimeException(ex2);
		} catch (NumberFormatException ex3) {
			logger.error("Error in parsing sampleCountThreshold from: " + prop
                        .getProperty("sampleCountThreshold"));
			throw new RuntimeException(ex3);
		}

        String response = "{\"sampleCountThreshold\":\"" + sampleCountThreshold + "\"}";
        return Response.ok(response).build();
    }
}
