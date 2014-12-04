package qa.qcri.aidr.dbmanager.ejb.remote.facade;



import java.util.List;

import javax.ejb.Local;

import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.task.Document;


@Local
public interface DocumentResourceFacade extends CoreDBServiceFacade<Document, Long> {

    public void updateHasHumanLabel(DocumentDTO document);
    
    public int deleteNoLabelDocument(DocumentDTO document);
    public int deleteNoLabelDocument(List<DocumentDTO> collection);
    public int deleteUnassignedDocument(DocumentDTO document);
    public int deleteUnassignedDocumentCollection(List<DocumentDTO> collection) throws PropertyNotSetException;

    public int deleteStaleDocuments(String joinType, String joinTable, String joinColumn,
			 					    String sortOrder, String[] orderBy,
			 					    final String maxTaskAge, final String scanInterval);
    
    public List<DocumentDTO> getDocumentCollectionForNominalLabel(Criterion criterion) throws PropertyNotSetException;

	
}
