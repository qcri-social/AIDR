/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.net.UnknownHostException;

import qa.qcri.aidr.predictui.dto.ItemToLabelDTO;
import qa.qcri.aidr.predictui.dto.TrainingDataDTO;
import qa.qcri.aidr.predictui.facade.MiscResourceFacade;
import qa.qcri.aidr.predictui.util.Config;
import qa.qcri.aidr.predictui.util.ErrorLog;
import qa.qcri.aidr.predictui.util.ResponseWrapper;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/misc")
@Stateless
public class MiscResource {

	@Context
	private UriInfo context;
	@EJB
	private MiscResourceFacade miscEJB;

	public MiscResource() {
	}

	private static Logger logger = Logger.getLogger(MiscResource.class);
	private static ErrorLog elog = new ErrorLog();

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
			response.setTrainingData(trainingDataList);
		} catch (RuntimeException e) {
			logger.error("Error in getting training data for crisis: " + crisisID);
			logger.error(elog.toStringException(e));
			return Response.ok(new ResponseWrapper(Config.STATUS_CODE_FAILED, e.getCause().getCause().getMessage())).build();
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
		} catch (RuntimeException e) {
			logger.error("Exception in getting item to label for crisis: " + crisisID);
			logger.error(elog.toStringException(e));
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

}
