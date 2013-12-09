package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.DocumentNominalLabel;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentNominalLabelDao {

    void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel);
    boolean foundDuplicate(DocumentNominalLabel documentNominalLabel);
}
