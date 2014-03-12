package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.Document;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/1/13
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentDao extends AbstractDao<Document, String> {

    void updateHasHumanLabel(Document document);
    Document findDocument(Long documentID);
    List<Document> findDocumentForTask(Long crisisID, int requestNumber);
}
