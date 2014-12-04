package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import javax.ejb.Remote;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;

@Remote
public interface DocumentNominalLabelResourceFacade extends CoreDBServiceFacade<DocumentNominalLabel, Long> {
	
	public void saveDocumentNominalLabel(DocumentNominalLabelDTO documentNominalLabel) throws PropertyNotSetException;
	public boolean foundDuplicate(DocumentNominalLabelDTO documentNominalLabel);
}
