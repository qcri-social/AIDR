package qa.qcri.aidr.task.ejb;



import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import qa.qcri.aidr.task.entities.Document;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/1/13
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface DocumentService extends AbstractTaskManagerService<Document, Long>{

	public EntityManager getEntityManager(); 
    public void updateHasHumanLabel(Document document);
    
    public int deleteDocument(Document document);
    public int deleteDocument(List<Document> collection);
    public int deleteUnassignedDocument(Document document);
    public int deleteUnassignedDocumentCollection(List<Document> collection);
    public int deleteStaleDocuments(String joinType, String joinTable, String joinColumn,
			 					    String sortOrder, String[] orderBy,
			 					    final String maxTaskAge, final String scanInterval);
}
