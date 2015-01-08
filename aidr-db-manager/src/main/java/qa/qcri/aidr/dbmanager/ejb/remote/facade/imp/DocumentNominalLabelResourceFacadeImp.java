package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelIdDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentNominalLabelResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabelId;

/**
 * @author Koushik
 */

@Stateless(name="DocumentNominalLabelResourceFacadeImp")
public class DocumentNominalLabelResourceFacadeImp 
extends CoreDBServiceFacadeImp<DocumentNominalLabel, Long> implements DocumentNominalLabelResourceFacade {

	private Logger logger = LoggerFactory.getLogger("db-manager-log");

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
		Session session = getCurrentSession();
		Transaction tx = session.beginTransaction();
		DocumentNominalLabel d = doc.toEntity();
		try {
			em.persist(d);
			em.flush();
			em.refresh(d);
			System.out.println("Success in saving to document_nominal_label table, doc = " + doc.getIdDTO().getDocumentId());
		} catch (Exception e) {
			System.err.println("Error in saving document nominal label for document = " + doc.getIdDTO().getDocumentId());
			e.printStackTrace();
			tx.rollback();
			return null;
		}
		Document labeledDoc = null;
		try {
			labeledDoc = em.find(Document.class, d.getId().getDocumentId());
			labeledDoc.setHasHumanLabels(true);
			em.merge(labeledDoc);
			em.flush();
			if (!tx.wasCommitted()) tx.commit();
			System.out.println("Success in updating hashumanLabels field in document table, doc = " + labeledDoc.getDocumentId());
			return new DocumentNominalLabelDTO(d);
		} catch (Exception e) {
			System.err.println("Error in updating hasHumanLabel field of labeled document = " + labeledDoc.getDocumentId() +", rolling back transaction (delete from document_nominal_label)...");
			tx.rollback();
			return null;
		}
	}

	@Override
	public DocumentNominalLabelDTO editDocument(DocumentNominalLabelDTO doc) throws PropertyNotSetException {
		System.out.println("Received request for: " + doc.getDocumentDTO().getDocumentID() + ", " + doc.getDocumentDTO().getCrisisDTO().getCode());
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
			System.out.println("Exception in merging/updating document: " + doc.getIdDTO().getDocumentId());
			e.printStackTrace();	
		}
		return null;
	}

	@Override
	public Integer deleteDocument(DocumentNominalLabelDTO doc) {
		try {
			em.remove(doc.toEntity()); 
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	@Override
	public List<DocumentNominalLabelDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException {
		List<DocumentNominalLabel> list = getAllByCriteria(Restrictions.eq(columnName,value));
		List<DocumentNominalLabelDTO> dtoList = new ArrayList<DocumentNominalLabelDTO>();
		if (list != null) {
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
		System.out.println("Received request for fetching all Documents!!!");
		List<DocumentNominalLabelDTO> dtoList = new ArrayList<DocumentNominalLabelDTO>();
		List<DocumentNominalLabel> list = getAll();
		if (list != null) {
			for (DocumentNominalLabel doc : list) {
				//System.out.println("Converting to DTO Document: " + doc.getDocumentId() + ", " + doc.getCrisis().getCode() + ", " + doc.isHasHumanLabels());
				DocumentNominalLabelDTO dto = new DocumentNominalLabelDTO(doc);
				dtoList.add(dto);
			}
		}
		System.out.println("Done creating DTO list, size = " + dtoList.size());
		return dtoList;
	}

	@Override
	public DocumentNominalLabelDTO findLabeledDocumentByID(Long id) throws PropertyNotSetException {
		List<DocumentNominalLabelDTO> dtoList = findByCriteria("id.documentId", id.longValue()); 
		return (dtoList != null && !dtoList.isEmpty()) ? dtoList.get(0) : null;
	}

	@Override
	public List<DocumentNominalLabelDTO> getLabeledDocumentCollectionForNominalLabel(Integer nominalLabelID) throws PropertyNotSetException {

		List<DocumentNominalLabelDTO> fetchedList = findByCriteria("id.nominalLabelId", new Long(nominalLabelID).longValue());
		return fetchedList;
	}
}

