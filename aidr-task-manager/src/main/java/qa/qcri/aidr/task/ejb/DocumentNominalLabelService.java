package qa.qcri.aidr.task.ejb;

import java.util.List;

import javax.ejb.Local;
import qa.qcri.aidr.task.entities.DocumentNominalLabel;



@Local
public interface DocumentNominalLabelService extends AbstractTaskManagerService<DocumentNominalLabel, Long> {

    public void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel);
    public boolean foundDuplicate(DocumentNominalLabel documentNominalLabel);
}
