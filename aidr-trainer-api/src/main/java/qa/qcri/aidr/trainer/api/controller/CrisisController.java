package qa.qcri.aidr.trainer.api.controller;

import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.dbmanager.entities.misc.Collection;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;
import qa.qcri.aidr.trainer.api.service.CrisisService;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.CrisisNominalAttributeModel;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/crisis")
@Component
public class CrisisController {
	
	protected static Logger logger = Logger.getLogger(CrisisController.class);
	
	@Autowired
	private CrisisService crisisService;

	@GET
	@Produces( MediaType.APPLICATION_JSON )
	@Path("/id/{crisisid}")
	public CrisisJsonModel getCrisisByID(@PathParam("crisisid") Long crisisid){
		logger.info("received request for crisisId = " + crisisid);
		try {
			return  crisisService.findByOptimizedCrisisID(crisisid);
		} catch (Exception e) {
			logger.error("Error in getting crisisID for crisis ID = " + crisisid,e);
		}
		return null;
	}

	@GET
	@Produces( MediaType.APPLICATION_JSON )
	@Path("/getallactive")
	public List getAllActiveCrisis(){
		return  crisisService.findAllActiveCrisis();
	}

	@GET
	@Produces( MediaType.APPLICATION_JSON )
	@Path("/get/active")
	public List<Collection> getActiveCrisis(){
		return  crisisService.findActiveCrisisInfo();
	}

	@GET
	@Produces( MediaType.APPLICATION_JSON )
	@Path("/getnominalAttribute")
	public List<CrisisNominalAttributeModel> getAllActiveCrisisNominalAttribute(){
		return  crisisService.getAllActiveCrisisNominalAttribute();
	}

	@GET
	@Produces( MediaType.APPLICATION_JSON )
	@Path("/getnominalLabels/{crisisid}/{nominalAttributeID}")
	public String getAllActiveCrisisNominalAttribute(@PathParam("crisisid") Long crisisID, @PathParam("nominalAttributeID") Long nominalAttributeID){
		JSONArray labelJsonArrary = new JSONArray();

		Set<NominalLabel> nominalLabels =  crisisService.getNominalLabelByCrisisID(crisisID, nominalAttributeID) ;
		if(nominalLabels != null){
			for (NominalLabel o : nominalLabels) {
				JSONObject qa = new JSONObject();
				qa.put("qa", o.getNominalLabelCode());

				labelJsonArrary.add(qa);
			}
		}
		return labelJsonArrary.toJSONString();
	}

}
