package qa.qcri.aidr.trainer.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.DocumentDao;
import qa.qcri.aidr.trainer.api.entity.Document;
import qa.qcri.aidr.trainer.api.service.DocumentService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/1/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("documentService")
@Transactional(readOnly = true)
public class DocumentServiceImpl implements DocumentService {

    protected static Logger logger = Logger.getLogger("service");

    @Autowired
    private DocumentDao documentDao;


    @Override
    @Transactional(readOnly = false)
    public void updateHasHumanLabel(Long documentID, boolean value) {
       // logger.debug("documentID : " + documentID) ;

        Document document = new Document(documentID, true);
       // logger.debug("document : " + document) ;
        if(document != null ) {
            documentDao.updateHasHumanLabel(document);
        }
    }

    @Override
    public Document findDocument(Long documentID) {
        return documentDao.findDocument(documentID);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
