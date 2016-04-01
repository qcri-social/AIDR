package qa.qcri.aidr.trainer.api.controller;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.trainer.api.service.DocumentService;
import qa.qcri.aidr.trainer.api.template.TaskBufferJsonModel;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/4/14
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/document")
@RestController
public class DocumentController {

	protected static Logger logger = Logger.getLogger("DocumentController");

    @Autowired
    private DocumentService documentService;

    @RequestMapping("/getbatchtaskbuffer/{userName}/{crisisID}/{maxresult}")
    public List<DocumentDTO> getAllTaskBufferToAssign(
            @PathVariable("userName") String userName,
            @PathVariable("crisisID") String crisisID,
            @PathVariable("maxresult") String maxresult){

    	logger.info("Request for batch task fetch, crisisID = " + crisisID + ", user = " + userName + ", count = " + maxresult);
        return documentService.getDocumentForTask(new Long(crisisID), Integer.valueOf(maxresult), userName);

    }

    @RequestMapping("/getassignabletask/{userName}/{crisisID}/{maxresult}")
    public List<TaskBufferJsonModel> getOneTaskBufferToAssign(@PathVariable("crisisID") String crisisID,
                                                              @PathVariable("userName") String userName,
                                                              @PathVariable("maxresult") String maxresult){

        DocumentDTO document = null;
        Long id = new Long(crisisID);
        logger.info("Going to fetch internal training document for crisisID = " + crisisID + ", userName = " + userName + ", count = " + maxresult);
        if(userName != null){
            List<DocumentDTO> documents =  documentService.getDocumentForOneTask(id,Integer.valueOf(maxresult),userName );
            if(documents!= null){
                if(documents.size() > 0){
                    document = documents.get(0);
                    logger.info("Fetched document for internal tagging task: " + document.getDocumentID() + ", for crisisID = " + document.getCrisisDTO().getCrisisID());
                }
            }	

        }
        List<TaskBufferJsonModel> jsonData = documentService.findOneDocumentForTaskByCririsID(document, id);
        for (int i = 0;i < jsonData.size();i++) {
        	logger.info("To be returned json Data, documentID = " + jsonData.get(i).getDocumentID() + ", for crisisID = " + jsonData.get(i).getCrisisID() );
        }
        return jsonData;
    }
    
    @RequestMapping(value = "/import/training-set", method={RequestMethod.POST})
    public String importTrainingData(@RequestParam @NotNull Long targetCollectionId,
    		@RequestParam Long sourceCollectionId, @RequestParam Long attributeId) {
    	
    	if(targetCollectionId == null || sourceCollectionId == null || attributeId == null
    			|| targetCollectionId == 0 || sourceCollectionId == 0 || attributeId == 0
    			|| targetCollectionId == -1 || sourceCollectionId == -1 || attributeId == -1) {
    		return "FAILURE";
    	}
    	documentService.importTrainingData(targetCollectionId, sourceCollectionId, attributeId);
    	return "SUCCESS";
    }
}
