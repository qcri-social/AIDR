package qa.qcri.aidr.predictui.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;


/**
 *
 * @author Imran
 */
@Local
public interface DocumentFacade {
    
    public List<DocumentDTO> getAllDocuments();
    public DocumentDTO getDocumentByID(Long id);
    public List<DocumentDTO> getAllLabeledDocumentbyCrisisID(Long crisisID, Long attributeID);
    public List<DocumentDTO> getAllUnlabeledDocumentbyCrisisID(Long crisisID);
    public int deleteDocument(Long documentID);
    public void removeTrainingExample(Long documentID);

}
