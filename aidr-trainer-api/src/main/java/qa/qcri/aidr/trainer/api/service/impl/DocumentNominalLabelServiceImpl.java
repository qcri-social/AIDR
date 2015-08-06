package qa.qcri.aidr.trainer.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelIdDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentNominalLabelResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.NominalLabelResourceFacade;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;
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
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class DocumentNominalLabelServiceImpl implements DocumentNominalLabelService {
	
	private static Logger logger=Logger.getLogger(DocumentNominalLabelServiceImpl.class);

	//@Autowired
	//private DocumentNominalLabelDao documentNominalLabelDao;

	@Autowired TaskManagerRemote<DocumentNominalLabelDTO, Long> taskManager;

	@Autowired DocumentNominalLabelResourceFacade remoteDocumentNominalLabelEJB;	

	@Autowired NominalLabelResourceFacade remoteNominalLabelEJB;

	@Autowired DocumentResourceFacade remoteDocumentEJB;

	@Override
	public void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel) {
		//documentNominalLabelDao.saveDocumentNominalLabel(documentNominalLabel);
		DocumentNominalLabelIdDTO id = new DocumentNominalLabelIdDTO(documentNominalLabel.getDocumentID(), documentNominalLabel.getNominalLabelID(), documentNominalLabel.getUserID());
		try {
			DocumentDTO doc = remoteDocumentEJB.findDocumentByID(documentNominalLabel.getDocumentID());
			NominalLabelDTO nb = remoteNominalLabelEJB.getNominalLabelByID(documentNominalLabel.getNominalLabelID());

			DocumentNominalLabelDTO dto = new DocumentNominalLabelDTO(id, nb, doc);
			taskManager.saveDocumentNominalLabel(dto);
		} catch (Exception e) {
			logger.error("Exception while saving documentNominalLabel",e);
		}
	}

	@Override
	public boolean foundDuplicateEntry(DocumentNominalLabel documentNominalLabel) {
		//return documentNominalLabelDao.foundDuplicate(documentNominalLabel);  //To change body of implemented methods use File | Settings | File Templates.
		try {
			DocumentNominalLabelIdDTO id = new DocumentNominalLabelIdDTO(documentNominalLabel.getDocumentID(), documentNominalLabel.getNominalLabelID(), documentNominalLabel.getUserID());
			return remoteDocumentNominalLabelEJB.isDocumentExists(id);
		} catch (Exception e) {
			logger.error("Exception while finding Duplicate Entry for documentNominalLabel",e);
			return false;
		}
	}
}
