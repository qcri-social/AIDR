package qa.qcri.aidr.task.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.task.entities.Document;

/**
 *
 * @author Koushik
 */
@Local
public interface DocumentFacade {
    
    public Document getDocumentByID(Long documentID);
    
    public List<Document> getAllDocumentsByCrisisID(Long crisisID);
    public List<Document> getAllLabeledDocumentsbyCrisisID(Long crisisID);
    public List<Document> getAllUnlabeledDocumentsbyCrisisID(Long crisisID);
    
    public int getAllDocumentsCountByCrisisID(Long crisisID);
    public int getAllLabeledDocumentsCountbyCrisisID(Long crisisID);
    public int getAllUnlabeledDocumentsCountbyCrisisID(Long crisisID);
    
    public void deleteDocument(Long documentID);
    public void deleteDocumentCollectionByCrisisID(Long crisisID);
    public void deleteDocumentCollection(List<Document> documentCollection);
    
    public void updateDocument(Document document);
    public void updateDocumentCollection(List<Document> documentCollection);
    
    public void saveDocument(Document document);
    public void saveDocumentCollection(List<Document> documentCollection);
    

}
