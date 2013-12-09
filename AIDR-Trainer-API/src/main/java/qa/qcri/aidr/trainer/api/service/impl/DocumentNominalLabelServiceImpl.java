package qa.qcri.aidr.trainer.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.DocumentNominalLabelDao;
import qa.qcri.aidr.trainer.api.entity.DocumentNominalLabel;
import qa.qcri.aidr.trainer.api.service.DocumentNominalLabelService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/29/13
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("documentNominalLabelService")
@Transactional(readOnly = true)
public class DocumentNominalLabelServiceImpl implements DocumentNominalLabelService {

    @Autowired
    private DocumentNominalLabelDao documentNominalLabelDao;

    @Override
    public void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel) {
        documentNominalLabelDao.saveDocumentNominalLabel(documentNominalLabel);
    }

    @Override
    public boolean foundDuplicateEntry(DocumentNominalLabel documentNominalLabel) {
        return documentNominalLabelDao.foundDuplicate(documentNominalLabel);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
