package qa.qcri.aidr.task.ejb;

import qa.qcri.aidr.task.entities.DocumentNominalLabel;



/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentNominalLabelService {

    void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel);
    boolean foundDuplicate(DocumentNominalLabel documentNominalLabel);
}
