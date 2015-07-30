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
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurationProperty;
import qa.qcri.aidr.predictui.util.TaggerAPIConfigurator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

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
		CrisisDTO crisis = null;
		try {
			crisis = crisisLocalEJB.getCrisisByID(id);
			System.out.println("fetched crisis for id " + id + ": " + (crisis != null ? crisis.getCode() : "null"));
		} catch (RuntimeException e) {
			return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return Response.ok(mapper.writeValueAsString(crisis)).build();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/by-code/{code}")
	public Response getCrisisByCode(@PathParam("code") String crisisCode) {
		CrisisDTO crisis = null;
		ResponseWrapper response = new ResponseWrapper();
		try {
			crisis = crisisLocalEJB.getCrisisByCode(crisisCode);
			System.out.println("For code = " + crisisCode + ", Returning crisis: " + (crisis != null ? crisis.getCode() : "null"));
			if (crisis != null) {
				response.setDataObject(crisis);
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
			return Response.ok(response).build();
		}
		
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/code/{code}")
	public Response isCrisisExists(@PathParam("code") String crisisCode) {
		CrisisDTO crisis = crisisLocalEJB.getCrisisByCode(crisisCode);
		long crisisId = 0;
		//        null value can not be correct deserialized
		if (crisis == null){
			crisisId = 0;
		} else {
				crisisId = crisis.getCrisisID();
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
	public Response getCrisesByCodes(List<String> codes) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap<String, Integer> classifiersNumbers = crisisLocalEJB.countClassifiersByCrisisCodes(codes);
			String rv = objectMapper.writeValueAsString(classifiersNumbers);

			return Response.ok(rv).build();
		} catch (IOException e) {
			logger.error("Error while getting numbers of classifiers by crisis codes:");
			for (String c: codes) {
				logger.error("for code: " + c);
			}
			return Response.ok("Error while getting numbers of classifiers by crisis codes.").build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
	public Response getAllCrisis() {
		System.out.println("Received request");

		List<CrisisDTO> crisisList = crisisLocalEJB.getAllCrisis();
		ResponseWrapper response = new ResponseWrapper();
		response.setMessage(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS));
		response.setCrisises(crisisList);
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("retreived crisis list: " + crisisList);
		try {
			System.out.println("Serialized response: " + mapper.writeValueAsString(response));
			//return Response.ok(response).build();
			return Response.ok(mapper.writeValueAsString(response)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok(new ResponseWrapper(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_FAILED), e.getCause().getCause().getMessage())).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseWrapper getAllCrisisByUserID(@QueryParam("userID") Long userID) throws Exception {
		List<CrisisDTO> crisisList = crisisLocalEJB.getAllCrisisByUserID(userID);
		System.out.println("list of crisis for userID " + userID + ": " + (crisisList != null ? crisisList.size() : 0));
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
			System.out.println("Received request to add crisis: " + crisis.getCode());
			if (crisisLocalEJB.isCrisisExists(crisis.getCode())) {
				logger.warn("Crisis with code = " + crisis.getCode() + " already has at least one classifier attached!");
				return Response.ok(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS)).build();
			}
			// Otherwise, add the new crisis to aidr_predict database
			crisis.setIsTrashed(false);
			CrisisDTO newCrisis = crisisLocalEJB.addCrisis(crisis);
			System.out.println("Added crisis successfully: id = " + newCrisis.getCrisisID() + ", " + newCrisis.getCode());
			return Response.ok(TaggerAPIConfigurator.getInstance().getProperty(TaggerAPIConfigurationProperty.STATUS_CODE_SUCCESS)).build();
		} catch (RuntimeException e) {
			logger.error("Error while adding Crisis. Possible causes could be duplication of primary key, incomplete data, incompatible data format. For crisis: " + crisis.getCode(), e);
			return Response.ok("Error while adding Crisis. Possible causes could be duplication of primary key, incomplete data, incompatible data format.").build();
		}
	}

	@PUT
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editCrisis(CrisisDTO crisis) {
		try {
			System.out.println("Received request to edit crisis: " + crisis.getCode());
			CrisisDTO updatedCrisis = crisisLocalEJB.editCrisis(crisis);
			return Response.ok(updatedCrisis).build();
		} catch (RuntimeException e) {
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
				System.out.println("retrieved result: " + retVal);
				if (retVal != null) {
					result.put("count", retVal.get(crisisCode) != null ? retVal.get(crisisCode) : 0);
					System.out.println("Generated response: " + result);
					return Response.ok(mapper.writeValueAsString(result)).build();
				}
			}
			result.put("count", 0);
			System.out.println("Response string from tagger-api on classifier count: " + mapper.writeValueAsString(result));
			return Response.ok(mapper.writeValueAsString(result)).build();
		} catch (Exception e) {
			e.printStackTrace();
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
			CrisisDTO crisisDTO = crisisLocalEJB.getCrisisByCode(crisisCode);
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
