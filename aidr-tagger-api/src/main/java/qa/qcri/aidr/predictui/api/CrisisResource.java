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
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
//import org.codehaus.jackson.map.ObjectMapper;




import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictui.dto.CrisisDTO;
import qa.qcri.aidr.predictui.dto.CrisisTypeDTO;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.util.Config;
import qa.qcri.aidr.predictui.util.ResponseWrapper;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/crisis")
@Stateless
public class CrisisResource {

	private Logger logger = Logger.getLogger(CrisisResource.class);
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
	public Response getCrisisByID(@PathParam("id") int id) {
		Crisis crisis = null;
		try {
			crisis = crisisLocalEJB.getCrisisByID((long) id);
			System.out.println("fetched crisis for id " + id + ": " + (crisis != null ? crisis.getCode() : "null"));
		} catch (RuntimeException e) {
			return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return Response.ok(mapper.writeValueAsString(crisis)).build();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/by-code/{code}")
	public Response getCrisisByCode(@PathParam("code") String crisisCode) {
		Crisis crisis = null;
		try {
			crisis = crisisLocalEJB.getCrisisByCode(crisisCode);
		} catch (RuntimeException e) {
			return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
		}
		CrisisDTO dto = transformCrisisToDto(crisis);
		return Response.ok(dto).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/code/{code}")
	public Response isCrisisExists(@PathParam("code") String crisisCode) {
		Integer crisisId = crisisLocalEJB.isCrisisExists(crisisCode);
		//        null value can not be correct deserialized
		if (crisisId == null){
			crisisId = 0;
		}
		//TODO: Following way of creating JSON should be changed through a proper and automatic way
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("crisisCode", crisisCode);
		result.put("crisisId", crisisId);
		//String response = "{\"crisisCode\":\"" + crisisCode + "\", \"crisisId\":\"" + crisisId + "\"}";
		return Response.ok(result.toString()).build();
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

		List<Crisis> crisisList = crisisLocalEJB.getAllCrisis();
		ResponseWrapper response = new ResponseWrapper();
		response.setMessage(Config.STATUS_CODE_SUCCESS);
		response.setCrisises(crisisList);
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("retreived crisis list: " + crisisList);
		try {
			System.out.println("Serialized response: " + mapper.writeValueAsString(response));
			//return Response.ok(response).build();
			return Response.ok(mapper.writeValueAsString(response)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseWrapper getAllCrisisByUserID(@QueryParam("userID") int userID) throws Exception {
		List<Crisis> crisisList = crisisLocalEJB.getAllCrisisByUserID(userID);
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
	public Response addCrisis(Crisis crisis) {
		try {
			// koushik: set default 
			crisis.setIsTrashed(false);
			crisisLocalEJB.addCrisis(crisis);
		} catch (RuntimeException e) {
			logger.error("Error while adding Crisis. Possible causes could be duplication of primary key, incomplete data, incompatible data format. For crisis: " + crisis.getCode());
			logger.error(elog.toStringException(e));
			return Response.ok("Error while adding Crisis. Possible causes could be duplication of primary key, incomplete data, incompatible data format.").build();
		}

		return Response.ok(Config.STATUS_CODE_SUCCESS).build();

	}

	@PUT
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editCrisis(Crisis crisis) {
		try {
			crisis = crisisLocalEJB.editCrisis(crisis);
		} catch (RuntimeException e) {
			return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
		}
		CrisisDTO dto = transformCrisisToDto(crisis);
		return Response.ok(dto).build();
	}

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

	@GET
	@Produces("application/json")
	@Path("/attributes/count/{crisisCode}")
	public Response getNominalAttributesCountForCrisis(@PathParam("crisisCode") String crisisCode) {
		Map<String, Integer> result = new HashMap<String, Integer>(1);;
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (crisisLocalEJB.isCrisisExists(crisisCode) != null) {
				List<String> crisisList = new ArrayList<String>();
				crisisList.add(crisisCode);
				result = crisisLocalEJB.countClassifiersByCrisisCodes(crisisList);
				System.out.println("retrieved result: " + result);
				if (result != null) {
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
