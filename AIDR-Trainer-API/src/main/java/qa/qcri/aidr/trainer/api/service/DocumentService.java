package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.entity.Document;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/1/13
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentService {

    void updateHasHumanLabel(Long documentID, boolean value);
    Document findDocument(Long documentID);
}
