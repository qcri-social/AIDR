package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.entity.DocumentNominalLabel;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/29/13
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentNominalLabelService {

    void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel);
    boolean foundDuplicateEntry(DocumentNominalLabel documentNominalLabel);
}
