/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.List;

import qa.qcri.aidr.common.util.EmailClient;
import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentDTO;
import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentList;
import qa.qcri.aidr.dbmanager.dto.taggerapi.HumanLabeledDocumentListWrapper;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ItemToLabelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;
import qa.qcri.aidr.predictui.facade.MiscResourceFacade;
import qa.qcri.aidr.predictui.facade.SystemEventFacade;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurationProperty;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

/**
 * REST Web Service
 *
 * @author Imran, Koushik
 */
@Path("/misc")
@Stateless
public class MiscResource {

	@Context
	private UriInfo context;
	@EJB
	private MiscResourceFacade miscEJB;
	@EJB
	private SystemEventFacade systemEventEJB;

	public MiscResource() {
	}

	//private static Logger logger = Logger.getLogger(MiscResource.class);
	private static Logger logger = Logger.getLogger(MiscResource.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTrainingData")
	public Response getTrainingDataByCrisisAndAttribute(@QueryParam("crisisID") int crisisID,
			@QueryParam("modelFamilyID") int modelFamilyID,
			@DefaultValue("0") @QueryParam("fromRecord") int fromRecord,
			@DefaultValue("100") @QueryParam("limit") int limit,
			@DefaultValue("") @QueryParam("sortColumn") String sortColumn,
			@DefaultValue("") @QueryParam("sortDirection") String sortDirection) {
		logger.info("received crisisID :" + crisisID);
		logger.info("received modelFID :" + modelFamilyID);
		ResponseWrapper response = new ResponseWrapper();
		try {
			List<TrainingDataDTO> trainingDataList = miscEJB.getTraningDataByCrisisAndAttribute((long) crisisID, modelFamilyID, fromRecord, limit, sortColumn, sortDirection);
			logger.info("Returning result size for crisisID = " + crisisID + " and modelFamilyId = " + modelFamilyID + ", training data count = " + (trainingDataList != null ? trainingDataList.size() : 0)); 
			response.setTrainingData(trainingDataList);
		} catch (RuntimeException e) {
			logger.error("Error in getting training data for crisis: " + crisisID);
			logger.error("Exception", e);
			return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
		}
		return Response.ok(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getItem")
	public Response getItemToLabel(@QueryParam("crisisID") int crisisID, @QueryParam("attributeID") int attributeID) {
		ItemToLabelDTO item = new ItemToLabelDTO();
		try {
			item = miscEJB.getItemToLabel((long) crisisID, attributeID);
			logger.info("Found item to label = " + (item != null ? item.getItemID() : "NONE!"));
		} catch (RuntimeException e) {
			logger.error("Exception in getting item to label for crisis: " + crisisID);
			logger.error("Exception", e);
		}
		return Response.ok(item).build();
	}

	@GET
	@Produces("application/json")
	@Path("/ping")
	public Response ping() {
		String response = "{\"application\":\"aidr-tagger-api\", \"status\":\"RUNNING\"}";
		return Response.ok(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/humanLabeled/crisisID/{crisisID}")
	public Response getHumanLabeledDocumentsByCrisisID(@PathParam("crisisID") Long crisisID, @QueryParam("count") Integer count) {
		if (null == crisisID) {
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "crisisID can't be null")).build();
		}
		try {
			List<HumanLabeledDocumentDTO> dtoList = miscEJB.getHumanLabeledDocumentsByCrisisID(crisisID, count);
			System.out.println("REST call will return dto List size = " + (dtoList != null ? dtoList.size() : "null"));
			if (dtoList != null) {
				ResponseWrapper response = new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
				response.setItems(dtoList);
				response.setTotal(dtoList.size());
				return Response.ok(response).build();
			} else {
				return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS), "Found 0 human labeled documents")).build();
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "Exception in fetching human labeled documents")).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/humanLabeled/crisisCode/{crisisCode}")
	public Response getHumanLabeledDocumentsByCrisisCode(@PathParam("crisisCode") String crisisCode, @QueryParam("count") Integer count) {
		if (null == crisisCode) {
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "crisis code can't be null")).build();
		}
		try {
			List<HumanLabeledDocumentDTO> dtoList = miscEJB.getHumanLabeledDocumentsByCrisisCode(crisisCode, count);
			System.out.println("REST call will return dto List size = " + (dtoList != null ? dtoList.size() : "null"));
			if (dtoList != null) {
				ResponseWrapper response = new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
				response.setItems(dtoList);
				response.setTotal(dtoList.size());
				return Response.ok(response).build();
			} else {
				return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS), "Found 0 human labeled documents")).build();
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "Exception in fetching human labeled documents")).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/humanLabeled/crisisID/{crisisID}/userID/{userID}")
	public Response getHumanLabeledDocumentsByCrisisIDUserID(@PathParam("crisisID") Long crisisID, @PathParam("userID") Long userID, 
			@QueryParam("count") Integer count) {
		if (null == crisisID || null == userID) {
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "crisisID or userID can't be null")).build();
		}
		try {
			List<HumanLabeledDocumentDTO> dtoList = miscEJB.getHumanLabeledDocumentsByCrisisIDUserID(crisisID, userID, count);
			System.out.println("REST call will return dto List size = " + (dtoList != null ? dtoList.size() : "null"));
			if (dtoList != null) {
				ResponseWrapper response = new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
				response.setItems(dtoList);
				response.setTotal(dtoList.size());
				return Response.ok(response).build();
			} else {
				return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS), "Found 0 human labeled documents")).build();
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "Exception in fetching human labeled documents")).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/humanLabeled/crisisID/{crisisID}/userName/{userName}")
	public Response getHumanLabeledDocumentsByCrisisIDUserName(@PathParam("crisisID") Long crisisID, @PathParam("userName") String userName, 
			@QueryParam("count") Integer count,
			@DefaultValue("CSV") @QueryParam("fileType") String fileType,
			@DefaultValue("full") @QueryParam("fileType") String contentType) {
		if (null == crisisID || null == userName) {
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "crisisID or user name can't be null")).build();
		}
		try {
			List<HumanLabeledDocumentDTO> dtoList = miscEJB.getHumanLabeledDocumentsByCrisisIDUserName(crisisID, userName, count);
			System.out.println("REST call will return dto List size = " + (dtoList != null ? dtoList.size() : "null"));
			if (dtoList != null) {
				ResponseWrapper response = new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
				response.setItems(dtoList);
				response.setTotal(dtoList.size());
				return Response.ok(response).build();
			} else {
				return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS), "Found 0 human labeled documents")).build();
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "Exception in fetching human labeled documents")).build();
		}
	}
	
	/*
	 * Note: userName in the path parameter refers to the username who is trying to access the data.
	 * It is not used for filtering the labeled items - but for generating the downloadable fileName in the persister.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/humanLabeled/download/crisis/{crisisCode}/userName/{userName}")
	public Response downloadHumanLabeledDocumentsByCrisisIDUserName(String queryString,
			@PathParam("crisisCode") String crisisCode, @PathParam("userName") String userName, 
			@QueryParam("count") Integer count,
			@DefaultValue("CSV") @QueryParam("fileType") String fileType,
			@DefaultValue("full") @QueryParam("contentType") String contentType) {
		if (null == crisisCode || null == userName) {
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "crisisID or user name can't be null")).build();
		}
		try {
			List<HumanLabeledDocumentDTO> dtoList = miscEJB.getHumanLabeledDocumentsByCrisisCode(crisisCode, count);
			System.out.println("REST call will return dto List size = " + (dtoList != null ? dtoList.size() : "null"));
			if (dtoList != null) {
				ResponseWrapper response = new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
				response.setTotal(dtoList.size());
				HumanLabeledDocumentList list = new HumanLabeledDocumentList(dtoList);
				response.setMessage(miscEJB.downloadItems(list, queryString, dtoList.get(0).getDoc().getCrisisDTO().getCode(), 
															userName, count, fileType, contentType));
				return Response.ok(response).build();
			} else {
				return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS), "Found 0 human labeled documents")).build();
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return Response.ok(
					new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), "Exception in fetching human labeled documents")).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/sendErrorEmail")
	public Response sendErrorEmail(@FormParam("code") String code, @FormParam("description") String description) throws Exception {
		Boolean emailSent = true;
		try {
			EmailClient.sendErrorMail(code,description);
		} catch (Exception e) {
			logger.error("Unable to send email");
			logger.error(e.getMessage());
			emailSent = false;
		}
		try
		{
			systemEventEJB.insertSystemEvent("ERROR", "Collector", code, description, emailSent);
		}
		catch (Exception e) {
			return Response.serverError().build();
		}
		return Response.ok().build();
	}
}
