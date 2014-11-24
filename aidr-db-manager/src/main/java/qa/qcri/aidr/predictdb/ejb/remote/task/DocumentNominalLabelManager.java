package qa.qcri.aidr.predictdb.ejb.remote.task;

import qa.qcri.aidr.predictdb.dto.DocumentDTO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/24/14
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentNominalLabelManager {

    public void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel) throws Exception;
    public boolean foundDuplicateDocumentNominalLabel(DocumentNominalLabel documentNominalLabel) throws Exception;
    public List<DocumentDTO> getNominalLabelDocumentCollection(Integer nominalLabelID) throws Exception;

}
