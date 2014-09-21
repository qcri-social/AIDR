/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictui.util.ResponseWrapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.apache.log4j.Logger;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.predictui.dto.CrisisAttributesDTO;
//import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.NominalAttribute;
//import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.facade.NominalAttributeFacade;
import qa.qcri.aidr.predictui.util.Config;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/attribute")
@Stateless
public class NominalAttributeResource {

	@Context
	private UriInfo context;
	@EJB
	private NominalAttributeFacade attributeLocalEJB;

	public NominalAttributeResource() {
	}

	//private static Logger logger = Logger.getLogger(NominalAttributeResource.class);
	private static Logger logger = LoggerFactory.getLogger(NominalAttributeResource.class);
	private static ErrorLog elog = new ErrorLog();

	@GET
	@Produces("application/json")
	@Path("/all")
	public Response getAllNominalAttributes() {
		List<NominalAttribute> attributeList = attributeLocalEJB.getAllAttributes();
		ResponseWrapper response = new ResponseWrapper();
		response.setNominalAttributes(attributeList);
		return Response.ok(response).build();
	}

	@GET
	@Produces("application/json")
	@Path("/crisis/all")
	public Response getAllNominalAttributesWithCrisis(@QueryParam("exceptCrisis") int crisisID) {
		List<CrisisAttributesDTO> attributeList = attributeLocalEJB.getAllAttributesExceptCrisis(crisisID);
		ResponseWrapper response = new ResponseWrapper();
		if (attributeList.isEmpty() || attributeList == null){
			response.setMessage("No attribute left.");
			return Response.ok(response).build();
		}
		response.setCrisisAttributes(attributeList);
		return Response.ok(response).build();
	}

	

	@GET
	@Produces("application/json")
	@Path("{attributeID}")
	public Response getAttributesnLabelByAttrID(@PathParam("attributeID") int attributeID) {
		NominalAttribute attribute = attributeLocalEJB.getAttributeByID(attributeID);
		ResponseWrapper response = new ResponseWrapper();
		if (attribute != null){
			return Response.ok(attribute).build();
		}
		response.setMessage("no attribute found with the given id.");
		return Response.ok(response).build();
	}

	@POST
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAttribute(NominalAttribute attribute){
		NominalAttribute attrib =  attributeLocalEJB.addAttribute(attribute);
		return Response.ok(attrib).build();
	}

	@PUT
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editAttribute(NominalAttribute attribute) {
		try {
			attribute = attributeLocalEJB.editAttribute(attribute);
		} catch (RuntimeException e) {
			logger.error("failed to edit attribute: " + attribute.getCode());
			logger.error(elog.toStringException(e));
			return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
		}
		return Response.ok(attribute).build();
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAttribute(@PathParam("id") int id) {
		try {
			attributeLocalEJB.deleteAttribute(id);
		} catch (RuntimeException e) {
			logger.error("failed to delete attribute: " + id);
			logger.error(elog.toStringException(e));
			return Response.ok(
					new ResponseWrapper(Config.STATUS_CODE_FAILED,
							"Error while deleting Attribute.")).build();
		}
		return Response.ok(new ResponseWrapper(Config.STATUS_CODE_SUCCESS)).build();
	}

	@GET
	@Produces("application/json")
	@Path("/code/{code}")
	public Response isAttributeExists(@PathParam("code") String code) {
		Integer attributeID = attributeLocalEJB.isAttributeExists(code);
		if (attributeID == null){
			attributeID = 0;
		}
		String response = "{\"code\":\"" + code + "\", \"nominalAttributeID\":\"" + attributeID + "\"}";
		return Response.ok(response).build();
	}
}
