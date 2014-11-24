package qa.qcri.aidr.predictdb.ejb.remote.task;

import qa.qcri.aidr.predictdb.dto.DocumentDTO;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/24/14
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentManager {
    public DocumentDTO getDocumentById(Long id);
}
