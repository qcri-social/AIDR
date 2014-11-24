package qa.qcri.aidr.predictdb.ejb.local.task;

import java.util.List;

import javax.ejb.Local;


@Local
public interface DocumentNominalLabelService extends AbstractTaskManagerService<DocumentNominalLabel, Long> {

    public void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel);
    public boolean foundDuplicate(DocumentNominalLabel documentNominalLabel);
}
