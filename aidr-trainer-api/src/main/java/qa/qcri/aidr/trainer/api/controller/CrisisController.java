package qa.qcri.aidr.trainer.api.controller;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
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
@RequestMapping("/crisis")
@RestController
public class CrisisController {
	
	protected static Logger logger = Logger.getLogger(CrisisController.class);
	
	@Autowired
	private CrisisService crisisService;

	@RequestMapping("/id/{crisisid}")
	public CrisisJsonModel getCrisisByID(@PathVariable("crisisid") Long crisisid){
		logger.info("received request for crisisId = " + crisisid);
		try {
			return  crisisService.findByOptimizedCrisisID(crisisid);
		} catch (Exception e) {
			logger.error("Error in getting crisisID for crisis ID = " + crisisid,e);
		}
		return null;
	}

	@RequestMapping("/getallactive")
	public List getAllActiveCrisis(){
		return crisisService.findAllActiveCrisis(); 
	}

	@RequestMapping("/get/active")
	public List<CollectionDTO> getActiveCrisis(){
		return crisisService.findActiveCrisisInfo();
	}

	@RequestMapping("/getnominalAttribute")
	public List<CrisisNominalAttributeModel> getAllActiveCrisisNominalAttribute(){
		return  crisisService.getAllActiveCrisisNominalAttribute();
	}

	@RequestMapping("/getnominalLabels/{crisisid}/{nominalAttributeID}")
	public String getAllActiveCrisisNominalAttribute(@PathVariable("crisisid") Long crisisID, @PathVariable("nominalAttributeID") Long nominalAttributeID){
		JSONArray labelJsonArrary = new JSONArray();

		Set<NominalLabelDTO> nominalLabels =  crisisService.getNominalLabelByCrisisID(crisisID, nominalAttributeID) ;
		if(nominalLabels != null){
			for (NominalLabelDTO o : nominalLabels) {
				JSONObject qa = new JSONObject();
				qa.put("qa", o.getNominalLabelCode());

				labelJsonArrary.add(qa);
			}
		}
		return labelJsonArrary.toJSONString();
	}

}
