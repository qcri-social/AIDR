/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.util.ResponseWrapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static qa.qcri.aidr.predictui.util.ConfigProperties.getProperty;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/crisis")
@Stateless
public class CrisisResource {

	//private Logger logger = Logger.getLogger(CrisisResource.class.getName());
	private Logger logger = LoggerFactory.getLogger(CrisisResource.class);
	private ErrorLog elog = new ErrorLog();

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
			return Response.ok(new ResponseWrapper(getProperty("STATUS_CODE_FAILED"), e.getCause().getCause().getMessage())).build();
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return Response.ok(mapper.writeValueAsString(crisis)).build();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.ok(new ResponseWrapper(getProperty("STATUS_CODE_FAILED"), e.getCause().getCause().getMessage())).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/by-code/{code}")
	public Response getCrisisByCode(@PathParam("code") String crisisCode) {
		CrisisDTO crisis = null;
		try {
			crisis = crisisLocalEJB.getCrisisByCode(crisisCode);
			System.out.println("Returning crisis: " + crisis.getCode());
		} catch (RuntimeException e) {
			return Response.ok(new ResponseWrapper(getProperty("STATUS_CODE_FAILED"), e.getCause().getCause().getMessage())).build();
		}
		return Response.ok(crisis).build();
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
			logger.error(elog.toStringException(e));
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
		response.setMessage(getProperty("STATUS_CODE_SUCCESS"));
		response.setCrisises(crisisList);
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("retreived crisis list: " + crisisList);
		try {
			System.out.println("Serialized response: " + mapper.writeValueAsString(response));
			//return Response.ok(response).build();
			return Response.ok(mapper.writeValueAsString(response)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok(new ResponseWrapper(getProperty("STATUS_CODE_FAILED"), e.getCause().getCause().getMessage())).build();
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
				return Response.ok(getProperty("STATUS_CODE_SUCCESS")).build();
			}
			// Otherwise, add the new crisis to aidr_predict database
			crisis.setIsTrashed(false);
			CrisisDTO newCrisis = crisisLocalEJB.addCrisis(crisis);
			System.out.println("Added crisis successfully: id = " + newCrisis.getCrisisID() + ", " + newCrisis.getCode());
			return Response.ok(getProperty("STATUS_CODE_SUCCESS")).build();
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error("Error while adding Crisis. Possible causes could be duplication of primary key, incomplete data, incompatible data format. For crisis: " + crisis.getCode());
			logger.error(elog.toStringException(e));
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
			return Response.ok(new ResponseWrapper(getProperty("STATUS_CODE_FAILED"), e.getCause().getCause().getMessage())).build();
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
					result.put("count", retVal.get(crisisCode));
					return Response.ok(mapper.writeValueAsString(result)).build();
				}
			}
			result.put("count", 0);
			return Response.ok(mapper.writeValueAsString(result)).build();
		} catch (Exception e) {
			e.printStackTrace();
			result.put("count", -1);
			return Response.ok(result.toString()).build();
		}
	}
}
