package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.dbmanager.dto.taggerapi.ItemToLabelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.task.Document;

@Remote
public interface MiscResourceFacade extends CoreDBServiceFacade<Document, Long>{
	
	// MiscResource from tagger-api
	public List<TrainingDataDTO> getTraningDataByCrisisAndAttribute(Long crisisID, Long modelFamilyID, int fromRecord, int limit, 
																	String sortColumn, String sortDirection);
	
	public ItemToLabelDTO getItemToLabel(Long crisisID, Long modelFamilyID);
	
}
