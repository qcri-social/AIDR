package qa.qcri.aidr.task.ejb;



import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import org.hibernate.criterion.Criterion;

import qa.qcri.aidr.task.entities.Document;


@Local
public interface DocumentService extends AbstractTaskManagerService<Document, Long>{

    public void updateHasHumanLabel(Document document);
    
    public int deleteNoLabelDocument(Document document);
    public int deleteNoLabelDocument(List<Document> collection);
    public int deleteUnassignedDocument(Document document);
    public int deleteUnassignedDocumentCollection(List<Document> collection);
    public int deleteStaleDocuments(String joinType, String joinTable, String joinColumn,
			 					    String sortOrder, String[] orderBy,
			 					    final String maxTaskAge, final String scanInterval);
    
    public List<qa.qcri.aidr.task.entities.Document> getDocumentCollectionForNominalLabel(Criterion criterion);

}
