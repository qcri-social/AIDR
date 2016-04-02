/**
 * Implements operations for managing the document_nominal_label table of the aidr_predict DB
 * 
 *  @author Koushik
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelIdDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentNominalLabelResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;


@Stateless(name="DocumentNominalLabelResourceFacadeImp")
public class DocumentNominalLabelResourceFacadeImp 
extends CoreDBServiceFacadeImp<DocumentNominalLabel, Long> implements DocumentNominalLabelResourceFacade {

	private Logger logger = Logger.getLogger("db-manager-log");

	@EJB
	DocumentResourceFacade documentEJB;

	protected DocumentNominalLabelResourceFacadeImp(){
		super(DocumentNominalLabel.class);
	}

	@Override
	public void saveDocumentNominalLabel(DocumentNominalLabelDTO documentNominalLabel) throws PropertyNotSetException {
		save(documentNominalLabel.toEntity());
	}

	@Override
	public boolean foundDuplicate(DocumentNominalLabelDTO documentNominalLabel) {
		Map<String, Long> attMap = new HashMap<String, Long>();
		attMap.put("id.documentId", documentNominalLabel.getDocumentDTO().getDocumentID());
		attMap.put("id.nominalLabelId", documentNominalLabel.getNominalLabelDTO().getNominalLabelId());

		DocumentNominalLabel obj =  getByCriterionID(Restrictions.allEq(attMap));

		if(obj != null) {
			return true;
		}
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public DocumentNominalLabelDTO addDocument(DocumentNominalLabelDTO doc) throws PropertyNotSetException {
		DocumentNominalLabel d = doc.toEntity();
		try {
			em.persist(d);
			em.flush();
			em.refresh(d);
			logger.debug("Success in saving to document_nominal_label table, doc = " + doc.getIdDTO().getDocumentId());
		} catch (Exception e) {
			logger.error("Error in saving document nominal label for document = " + doc.getIdDTO().getDocumentId(), e);
			return null;
		}
		Document labeledDoc = null;
		try {
			labeledDoc = em.find(Document.class, d.getId().getDocumentId());
			labeledDoc.setHasHumanLabels(true);
			em.merge(labeledDoc);
			em.flush();
			logger.debug("Success in updating hashumanLabels field in document table, doc = " + labeledDoc.getDocumentId());
			return new DocumentNominalLabelDTO(d);
		} catch (Exception e) {
			logger.error("Error in updating hasHumanLabel field of labeled document = " + labeledDoc.getDocumentId() +", rolling back transaction (delete from document_nominal_label)...");
			return null;
		}
	}

	@Override
	public DocumentNominalLabelDTO editDocument(DocumentNominalLabelDTO doc) throws PropertyNotSetException {
		try {
			DocumentNominalLabel d = doc.toEntity();
			DocumentNominalLabel oldDoc = getById(d.getId().getDocumentId()); 
			if (oldDoc != null) {
				oldDoc = em.merge(d);
				return (oldDoc != null) ? new DocumentNominalLabelDTO(oldDoc) : null;
			} else {
				throw new RuntimeException("Not found");
			}
		} catch (Exception e) {
			logger.error("Exception in merging/updating document: " + doc.getIdDTO().getDocumentId(), e);
		}
		return null;
	}

	@Override
	public Integer deleteDocument(DocumentNominalLabelDTO doc) {
		try {
			DocumentNominalLabel managed = em.merge(doc.toEntity());
			em.remove(managed); 
		} catch (Exception e) {
			logger.warn("Warning! Couldn't delete document nominal label with id : " + doc.getIdDTO());
			return 0;
		}
		return 1;
	}

	@Override
	public List<DocumentNominalLabelDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException {
		List<DocumentNominalLabel> list = getAllByCriteria(Restrictions.eq(columnName,value));
		List<DocumentNominalLabelDTO> dtoList = new ArrayList<DocumentNominalLabelDTO>();
		if (list != null && !list.isEmpty()) {
			for (DocumentNominalLabel c: list) {
				dtoList.add(new DocumentNominalLabelDTO(c));
			}
		}
		return dtoList;
	}

	@Override
	public DocumentNominalLabelDTO findDocumentByPrimaryKey(DocumentNominalLabelIdDTO id) throws PropertyNotSetException {
		List<DocumentNominalLabelDTO> docList = findByCriteria("id", id.toEntity());
		return (docList != null && !docList.isEmpty()) ? docList.get(0) : null;
	}

	@Override
	public boolean isDocumentExists(DocumentNominalLabelIdDTO id) throws PropertyNotSetException {
		List<DocumentNominalLabelDTO> docList = findByCriteria("id", id.toEntity());
		return (docList != null && !docList.isEmpty()) ? true : false;
	}

	@Override
	public boolean isDocumentExists(Long id) throws PropertyNotSetException {
		List<DocumentNominalLabelDTO> docList = findByCriteria("id.documentId", id.longValue());
		return (docList != null && !docList.isEmpty()) ? true : false;
	}

	@Override
	public List<DocumentNominalLabelDTO> getAllDocuments() throws PropertyNotSetException {
		logger.info("Received request for fetching all Documents!!!");
		List<DocumentNominalLabelDTO> dtoList = new ArrayList<DocumentNominalLabelDTO>();
		List<DocumentNominalLabel> list = getAll();
		if (list != null && !list.isEmpty()) {
			for (DocumentNominalLabel doc : list) {
				DocumentNominalLabelDTO dto = new DocumentNominalLabelDTO(doc);
				dtoList.add(dto);
			}
		}
		logger.info("Done creating DTO list, size = " + dtoList.size());
		return dtoList;
	}

	@Override
	public DocumentNominalLabelDTO findLabeledDocumentByID(Long id) throws PropertyNotSetException {
		List<DocumentNominalLabelDTO> dtoList = findByCriteria("id.documentId", id.longValue()); 
		return (dtoList != null && !dtoList.isEmpty()) ? dtoList.get(0) : null;
	}
	
	@Override
	public List<DocumentNominalLabelDTO> findLabeledDocumentListByID(Long id) throws PropertyNotSetException {
		List<DocumentNominalLabelDTO> dtoList = findByCriteria("id.documentId", id.longValue()); 
		return (dtoList != null && !dtoList.isEmpty()) ? dtoList : null;
	}

	@Override
	public List<DocumentNominalLabelDTO> getLabeledDocumentCollectionForNominalLabel(Integer nominalLabelID) throws PropertyNotSetException {

		List<DocumentNominalLabelDTO> fetchedList = findByCriteria("id.nominalLabelId", new Long(nominalLabelID).longValue());
		return fetchedList;
	}

	@Override
	public void deleteDocumentNominalLabelByNominalLabel(Long nominalLabelID) throws PropertyNotSetException {
		
		List<DocumentNominalLabelDTO> fetchedList = findByCriteria("id.nominalLabelId", new Long(nominalLabelID).longValue());
		for (DocumentNominalLabelDTO documentNominalLabelDTO : fetchedList) {
			deleteDocument(documentNominalLabelDTO);
		}
	}
}

