package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelIdDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;

@Remote
public interface DocumentNominalLabelResourceFacade extends CoreDBServiceFacade<DocumentNominalLabel, Long> {
	public DocumentNominalLabelDTO addDocument(DocumentNominalLabelDTO doc) throws PropertyNotSetException; 

	public DocumentNominalLabelDTO editDocument(DocumentNominalLabelDTO doc) throws PropertyNotSetException; 

	public Integer deleteDocument(DocumentNominalLabelDTO doc);

	public List<DocumentNominalLabelDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException;

	public DocumentNominalLabelDTO findDocumentByPrimaryKey(DocumentNominalLabelIdDTO id) throws PropertyNotSetException;
	
	public DocumentNominalLabelDTO findDocumentByID(Long id) throws PropertyNotSetException;

	public boolean isDocumentExists(DocumentNominalLabelIdDTO id) throws PropertyNotSetException;

	public List<DocumentNominalLabelDTO> getAllDocuments() throws PropertyNotSetException; 

	
	public void saveDocumentNominalLabel(DocumentNominalLabelDTO documentNominalLabel) throws PropertyNotSetException;
	public boolean foundDuplicate(DocumentNominalLabelDTO documentNominalLabel);
}
