package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.trainer.api.entity.Document;
import qa.qcri.aidr.trainer.api.template.TaskBufferJsonModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/1/13
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentService {

    void updateHasHumanLabel(Long documentID, boolean value);
    DocumentDTO findDocument(Long documentID);
    List<DocumentDTO> getDocumentForTask(Long crisisID, int count, String userName);
    List<TaskBufferJsonModel> findOneDocumentForTaskByCririsID(DocumentDTO document, Long crisisID);
    public void addToOneTaskAssignmentWithUserName(Long documentID, String userName) ;
    public void addToOneTaskAssignment(Long documentID, Long userID) ;
    //public List<Document> getAvailableDocument(long crisisID, int maxresult);
    public List<DocumentDTO> getAvailableDocument(Long crisisID, Integer maxresult);
    public List<DocumentDTO> getDocumentForOneTask(Long crisisID, int count, String userName);
}
