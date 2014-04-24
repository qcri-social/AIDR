package qa.qcri.aidr.task.dao;



import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.task.entities.Document;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/1/13
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Local
public interface DocumentService extends AbstractTaskManagerService<Document, String>{

    public void updateHasHumanLabel(Document document);
}
