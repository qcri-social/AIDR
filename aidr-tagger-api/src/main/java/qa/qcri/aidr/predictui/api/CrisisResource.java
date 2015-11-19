/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurationProperty;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/crisis")
@Stateless
public class CrisisResource {

	//private Logger logger = Logger.getLogger(CrisisResource.class.getName());
	private Logger logger = Logger.getLogger(CrisisResource.class);

	@Context
	private UriInfo context;
	@EJB
	private CrisisResourceFacade crisisLocalEJB;

	public CrisisResource() {
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getCrisisByID(@PathParam("id") Long id) {
		CollectionDTO collection = null;
		try {
			collection = crisisLocalEJB.getCrisisByID(id);
			System.out.println("fetched collection for id " + id + ": " + (collection != null ? collection.getCode() : "null"));
		} catch (RuntimeException e) {
			logger.error("Error in getCrisisById for collection id : " + id);
			return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return Response.ok(mapper.writeValueAsString(collection)).build();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Error in Json processing.");
			return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/by-code/{code}")
	public Response getCrisisByCode(@PathParam("code") String crisisCode) {
		CollectionDTO collection = null;
		ResponseWrapper response = new ResponseWrapper();
		try {
			collection = crisisLocalEJB.getCrisisByCode(crisisCode);
			logger.info("For code = " + crisisCode + ", Returning crisis: " + (collection != null ? collection.getCode() : "null"));
			if (collection != null) {
				response.setDataObject(collection);
				response.setTotal(1);
				response.setStatusCode(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
				return Response.ok(response).build();
			} else {
				response.setDataObject(null);
				response.setTotal(0);
				response.setStatusCode(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
				return Response.ok(response).build();
			}
		} catch (RuntimeException e) {
			response.setDataObject(null);
			response.setTotal(0);
			response.setStatusCode(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED));
			response.setMessage(e.getCause().getCause().getMessage());
			logger.error("Error in getCrisisByCode for code : " + crisisCode);
			return Response.ok(response).build();
		}
		
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/code/{code}")
	public Response isCrisisExists(@PathParam("code") String crisisCode) {
		CollectionDTO collection = crisisLocalEJB.getCrisisByCode(crisisCode);
		long crisisId = 0;
		//        null value can not be correct deserialized
		if (collection == null){
			crisisId = 0;
		} else {
				crisisId = collection.getCrisisID();
		}
			
        Gson gson = new Gson();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("crisisCode", crisisCode);
		result.put("crisisId", crisisId);

		return Response.ok(gson.toJson(result)).build();
	}

	@POST
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	@Path("/crises")
	public Response getCrisesByCodes(String codes) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonArr = (JsonArray)jsonParser.parse(codes);
            ArrayList<String> jsonObjList = new Gson().fromJson(jsonArr, ArrayList.class);
			HashMap<String, Integer> classifiersNumbers = crisisLocalEJB.countClassifiersByCrisisCodes(jsonObjList);
			String rv = objectMapper.writeValueAsString(classifiersNumbers);

			return Response.ok(classifiersNumbers).build();
		} catch (IOException e) {
			logger.error("Error while getting numbers of classifiers by crisis codes:", e);
			return Response.ok("Error while getting numbers of classifiers by crisis codes.").build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public Response getAllCrisis() {

		List<CollectionDTO> crisisList = crisisLocalEJB.getAllCrisis();
		ResponseWrapper response = new ResponseWrapper();
		response.setMessage(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
		response.setCrisises(crisisList);
		
		ObjectMapper mapper = new ObjectMapper();
		logger.info("retreived crisis list: " + crisisList);
		try {
			return Response.ok(mapper.writeValueAsString(response)).build();
		} catch (Exception e) {
			logger.error("Error in getAllCrisis.");
			return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseWrapper getAllCrisisByUserID(@QueryParam("userID") Long userID) throws Exception {
		List<CollectionDTO> crisisList = crisisLocalEJB.getAllCrisisByUserID(userID);
		logger.info("list of crisis for userID " + userID + ": " + (crisisList != null ? crisisList.size() : 0));
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
	public Response addCrisis(CollectionDTO crisis) {
		try {
			logger.info("Received request to add crisis: " + crisis.getCode());
			if (crisisLocalEJB.isCrisisExists(crisis.getCode())) {
				logger.warn("Crisis with code = " + crisis.getCode() + " already has at least one classifier attached!");
				return Response.ok(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS)).build();
			}
			// Otherwise, add the new crisis to aidr_predict database
			crisis.setIsTrashed(false);
			CollectionDTO newCollection = crisisLocalEJB.addCrisis(crisis);
			logger.info("Added crisis successfully: id = " + newCollection.getCrisisID() + ", " + newCollection.getCode());
			return Response.ok(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS)).build();
		} catch (RuntimeException e) {
			logger.error("Error while adding Crisis. Possible causes could be duplication of primary key, incomplete data, incompatible data format. For crisis: " + crisis.getCode(), e);
			return Response.ok("Error while adding Crisis. Possible causes could be duplication of primary key, incomplete data, incompatible data format.").build();
		}
	}

	@PUT
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editCrisis(CollectionDTO collection) {
		try {
			logger.info("Received request to edit crisis: " + collection.getCode());
			CollectionDTO updatedCollection = crisisLocalEJB.editCrisis(collection);
			return Response.ok(updatedCollection).build();
		} catch (RuntimeException e) {
			logger.error("Error in editCrisis for crisis code : " + collection.getCode());
			return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
		}
		
	}
	
	@GET
	@Produces("application/json")
	@Path("/attributes/count/{crisisCode}")
	public Response getNominalAttributesCountForCrisis(@PathParam("crisisCode") String crisisCode) {
		Map<String, Integer> result = new HashMap<String, Integer>(1);
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (crisisLocalEJB.isCrisisExists(crisisCode) != null) {
				List<String> crisisList = new ArrayList<String>();
				crisisList.add(crisisCode);
				Map<String, Integer> retVal = crisisLocalEJB.countClassifiersByCrisisCodes(crisisList);
				if (retVal != null) {
					result.put("count", retVal.get(crisisCode) != null ? retVal.get(crisisCode) : 0);
					logger.info("Generated response: " + result);
					return Response.ok(mapper.writeValueAsString(result)).build();
				}
			}
			result.put("count", 0);
			return Response.ok(mapper.writeValueAsString(result)).build();
		} catch (Exception e) {
			logger.error("Error in getNominalAttributesCountForCrisis for crisis code : " + crisisCode);
			result.put("count", -1);
			return Response.ok(result).build();
		}
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCrisis(@PathParam("id") Long id) throws PropertyNotSetException {
		int crisisDeleted = crisisLocalEJB.deleteCrisis(id);
		return crisisDeleted == 1 ? Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS))).build() : Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED))).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update/micromapperEnabled/{crisisCode}/{isMicromapperEnabled}")
	public Response updateMicromapperEnabledByCrisisCode(@PathParam("crisisCode") String crisisCode, 
			@PathParam("isMicromapperEnabled") Boolean isMicromapperEnabled ) {
		Map<String, Boolean> result = new HashMap<String, Boolean>(1);
		ObjectMapper mapper = new ObjectMapper();
		try {
			CollectionDTO crisisDTO = crisisLocalEJB.getCrisisByCode(crisisCode);
			crisisDTO.setIsMicromapperEnabled(isMicromapperEnabled);
			
			crisisDTO = crisisLocalEJB.editCrisis(crisisDTO);
			if(crisisDTO.isIsMicromapperEnabled()==isMicromapperEnabled){
				result.put("isMicromapperEnabled", isMicromapperEnabled);
			}
			return Response.ok(mapper.writeValueAsString(result)).build();
		} catch (Exception e) {
			logger.error("Exception while updating isMicromapperEnabled " + e.getMessage());
			return Response.ok("{\"status\": \"false\"}").build();
		}
	}
}
