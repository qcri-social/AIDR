package qa.qcri.aidr.trainer.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.trainer.api.dao.DocumentNominalLabelDao;
import qa.qcri.aidr.trainer.api.entity.DocumentNominalLabel;
import qa.qcri.aidr.trainer.api.service.DocumentNominalLabelService;
import qa.qcri.aidr.trainer.api.util.TaskManagerEntityMapper;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/29/13
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("documentNominalLabelService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class DocumentNominalLabelServiceImpl implements DocumentNominalLabelService {

    //@Autowired
    //private DocumentNominalLabelDao documentNominalLabelDao;
    
    @Autowired TaskManagerRemote<qa.qcri.aidr.task.entities.Document, Long> taskManager;
    
    @Override
    public void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel) {
        //documentNominalLabelDao.saveDocumentNominalLabel(documentNominalLabel);
    	TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
    	qa.qcri.aidr.task.entities.DocumentNominalLabel doc = mapper.reverseTransformDocumentNominalLabel(documentNominalLabel);
    	taskManager.saveDocumentNominalLabel(doc);
    }

    @Override
    public boolean foundDuplicateEntry(DocumentNominalLabel documentNominalLabel) {
        //return documentNominalLabelDao.foundDuplicate(documentNominalLabel);  //To change body of implemented methods use File | Settings | File Templates.
    	TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
    	qa.qcri.aidr.task.entities.DocumentNominalLabel doc = mapper.reverseTransformDocumentNominalLabel(documentNominalLabel);
    	return taskManager.foundDuplicateDocumentNominalLabel(doc);
    }
}
