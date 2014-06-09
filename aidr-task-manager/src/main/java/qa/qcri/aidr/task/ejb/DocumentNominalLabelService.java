package qa.qcri.aidr.task.ejb;

import javax.ejb.Local;


import qa.qcri.aidr.task.entities.DocumentNominalLabel;



@Local
public interface DocumentNominalLabelService extends AbstractTaskManagerService<DocumentNominalLabel, Long> {

    void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel);
    boolean foundDuplicate(DocumentNominalLabel documentNominalLabel);
}
