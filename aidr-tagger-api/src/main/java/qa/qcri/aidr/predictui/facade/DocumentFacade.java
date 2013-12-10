/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;
import qa.qcri.aidr.predictui.entities.Document;

/**
 *
 * @author Imran
 */
@Local
public interface DocumentFacade {
    
    public List<Document> getAllDocuments();
    public Document getDocumentByID(long id);
    public List<Document> getAllLabeledDocumentbyCrisisID(long crisisID, long attributeID);
    
}
