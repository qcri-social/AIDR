package qa.qcri.aidr.dbmanager.ejb.remote.facade;



import java.util.List;

import javax.ejb.Remote;

import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.common.exception.AidrException;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.task.Document;


@Remote
public interface DocumentResourceFacade extends CoreDBServiceFacade<Document, Long> {

	public DocumentDTO addDocument(DocumentDTO doc); 

	public DocumentDTO editDocument(DocumentDTO doc) throws PropertyNotSetException; 

	public Integer deleteDocument(DocumentDTO doc);

	public List<DocumentDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException;

	public DocumentDTO findDocumentByID(Long id) throws PropertyNotSetException;

	public DocumentDTO getDocumentWithAllFieldsByID(Long id) throws PropertyNotSetException;

	public boolean isDocumentExists(Long id) throws PropertyNotSetException;

	public List<DocumentDTO> getAllDocuments() throws PropertyNotSetException; 


	public void updateHasHumanLabel(DocumentDTO document);

	public int deleteNoLabelDocument(DocumentDTO document);
	public int deleteNoLabelDocument(List<DocumentDTO> collection);
	public int deleteUnassignedDocument(DocumentDTO document);
	public int deleteUnassignedDocumentCollection(List<DocumentDTO> collection) throws PropertyNotSetException;

	public int deleteStaleDocuments(String joinType, String joinTable, String joinColumn,
			String sortOrder, String[] orderBy,
			final String maxTaskAge, final String scanInterval);

	public List<DocumentDTO> getDocumentCollectionForNominalLabel(Criterion criterion) throws PropertyNotSetException;

	public List<DocumentDTO> findDocumentsByCrisisID(Long crisisId) throws PropertyNotSetException;
	
	public List<DocumentDTO> findLabeledDocumentsByCrisisID(Long crisisId) throws PropertyNotSetException;

	public List<DocumentDTO> findUnLabeledDocumentsByCrisisID(Long crisisId) throws PropertyNotSetException;

	public List<DocumentDTO> getDocumentCollectionWithNominalLabelData(Long nominalLabelID) throws Exception;

}
