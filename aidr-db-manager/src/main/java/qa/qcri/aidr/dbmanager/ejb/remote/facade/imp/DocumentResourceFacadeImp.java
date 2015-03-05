package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.AidrException;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.NominalLabelResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;

/**
 * 
 * @author Koushik
 *
 */
@Stateless(name="DocumentResourceFacadeImp")
public class DocumentResourceFacadeImp extends CoreDBServiceFacadeImp<Document, Long> implements DocumentResourceFacade  {

	private Logger logger = Logger.getLogger("db-manager-log");
	private ErrorLog elog = new ErrorLog();

	@EJB
	CrisisResourceFacade crisisEJB;

	@EJB
	NominalLabelResourceFacade nominalLabelEJB;

	public DocumentResourceFacadeImp() {
		super(Document.class);
	}

	@Override
	public void updateHasHumanLabel(DocumentDTO document) {
		Document doc = (Document) getByCriteria(Restrictions.eq("documentId", document.getDocumentID()));
		if (doc != null) {
			doc.setHasHumanLabels(true);
			update(doc);
		}
	}

	@Override
	public List<DocumentDTO> getDocumentCollectionForNominalLabel(Criterion criterion) throws PropertyNotSetException {
		String aliasTable = "documentNominalLabels";
		String aliasTableKey = "documentNominalLabels.id";
		String[] orderBy = {"documentId"};

		List<Document> fetchedList = getByCriteriaWithInnerJoinByOrder(criterion, "ASC", orderBy, null, aliasTable, Restrictions.isNotEmpty(aliasTableKey));
		if (fetchedList != null && !fetchedList.isEmpty()) {
			List<DocumentDTO> dtoList = new ArrayList<DocumentDTO>();
			for (Document doc: fetchedList) {
				dtoList.add(new DocumentDTO(doc));
			}
			return dtoList;
		}
		return null;
	}

	@Override
	public int deleteNoLabelDocument(DocumentDTO document) {
		if (null == document) { 
			return 0;
		}

		logger.info("Received request for : " + document.getDocumentID());
		int deleteCount = 0;

		if (!document.getHasHumanLabels()) {
			try {
				//delete(document);
				String hql = "DELETE from document WHERE documentID = :documentID AND !hasHumanLabels";
				Session session = getCurrentSession();
				Query collectionDeleteQuery = session.createSQLQuery(hql);
				try {
					collectionDeleteQuery.setParameter("documentID", document.getDocumentID());
					Transaction tx = session.beginTransaction();
					deleteCount = collectionDeleteQuery.executeUpdate();
					tx.commit();
					logger.info("deleted count = " + deleteCount);
				} catch (Exception e) {
					logger.error("deletion query failed, document: " + document.getDocumentID());
					logger.error(elog.toStringException(e));
					return 0;
				}
				logger.info("deletion success, deleted count = " + deleteCount);
				session.flush();
				return 1;

			} catch (Exception e) {
				logger.error("Deletion query failed");
				logger.error(elog.toStringException(e));
				return 0;
			}
		}
		logger.info("Document has label. Not deleting.");
		return 0;
	}



	@Override
	public int deleteNoLabelDocument(List<DocumentDTO> collection) {
		int deleteCount = 0;
		if (collection != null && !collection.isEmpty()) {
			List<Long>documentIDList = new ArrayList<Long>();
			for (DocumentDTO d: collection) {
				documentIDList.add(d.getDocumentID());
			}
			String hql = "DELETE from document WHERE (documentID IN (:documentID) "
					+ " AND !hasHumanLabels)";
			Session session = getCurrentSession();
			Query collectionDeleteQuery = session.createSQLQuery(hql);
			try {
				Transaction tx = session.beginTransaction();
				collectionDeleteQuery.setParameterList("documentID", documentIDList);
				deleteCount = collectionDeleteQuery.executeUpdate();
				tx.commit();
				logger.info("deleted count = " + deleteCount);
			} catch (Exception e) {
				logger.error("Collection deletion query failed");
				logger.error(elog.toStringException(e));
			}
		}
		return deleteCount;
	}


	@Override
	public int deleteUnassignedDocument(DocumentDTO document) {
		String hql = "DELETE from Document WHERE (documentID = :documentID AND "
				+ " documentID NOT IN (SELECT documentID FROM TaskAssignment)"
				+ " AND !hasHumanLabels)";
		if (!document.getHasHumanLabels()) {
			try {
				Query query = getCurrentSession().createQuery(hql);
				query.setParameter("documentID", document.getDocumentID());
				int result = query.executeUpdate();
				return result;
			} catch (Exception e) {
				logger.error("Deletion query failed");
				logger.error(elog.toStringException(e));
				return 0;
			}
		}
		return 0;
	}

	@Override
	public int deleteUnassignedDocumentCollection(List<DocumentDTO> collection) throws PropertyNotSetException {
		int deleteCount = 0;
		if (collection != null && !collection.isEmpty()) {
			List<Long>documentIDList = new ArrayList<Long>();
			for (DocumentDTO d: collection) {
				documentIDList.add(d.getDocumentID());
				logger.debug("To delete document: {" + d.getCrisisDTO().getCrisisID() + ", " + d.getDocumentID() + ", " + d.getHasHumanLabels() + "}");
			}
			logger.info("Size of docList to delete: " + documentIDList.size());
			String hql = "DELETE from Document WHERE (documentID IN (:documentID) "
					+ " AND documentID NOT IN (SELECT documentID FROM TaskAssignment)"
					+ " AND !hasHumanLabels)";
			Session session = getCurrentSession();
			Query collectionDeleteQuery = session.createQuery(hql);
			try {
				collectionDeleteQuery.setParameterList("documentID", documentIDList);
				Transaction tx = session.beginTransaction();
				deleteCount = collectionDeleteQuery.executeUpdate();
				tx.commit();
			} catch (Exception e) {
				logger.error("Collection deletion query failed");
				logger.error(elog.toStringException(e));
			}
		}
		return deleteCount;
	}

	/**
	 * Query very specific to deleting stale tasks only
	 */
	@Override
	public int deleteStaleDocuments(String joinType, String joinTable, String joinColumn,
			String sortOrder, String[] orderBy, 
			final String maxTaskAge, final String scanInterval) {

		logger.info("received request: " + joinType + ", " + joinTable + ", " 
				+ joinColumn + ", " + maxTaskAge + ", " + scanInterval);
		System.out.println("[deleteStaleDocuments] received request: " + joinType + ", " + joinTable + ", " 
				+ joinColumn + ", " + maxTaskAge + ", " + scanInterval);

		int deleteCount = 0;
		Session session = getCurrentSession();
		StringBuffer hql = new StringBuffer("DELETE d FROM aidr_predict.document d ");
		if (joinType.equalsIgnoreCase("LEFT JOIN") || joinType.equalsIgnoreCase("LEFT_JOIN")) {
			hql.append(" LEFT JOIN ").append(joinTable).append(" t ");
			hql.append(" ON d.").append(joinColumn).append(" = t.").append(joinColumn)
			.append(" WHERE ")
			.append("(!d.hasHumanLabels AND t.documentID IS NULL AND TIMESTAMPDIFF(")
			.append(getMetric(scanInterval))
			.append(", d.receivedAt, now()) > ");
		} else if (joinType.equalsIgnoreCase("JOIN")) {
			hql.append(" JOIN ").append(joinTable).append(" t ");
			hql.append(" ON d.").append(joinColumn).append(" = t.").append(joinColumn)
			.append(" WHERE ")
			.append("(!d.hasHumanLabels && TIMESTAMPDIFF(")
			.append(getMetric(scanInterval))
			.append(", t.assignedAt, now()) > ");
		}
		hql.append(" :task_expiry_age) ");

		if (orderBy != null) {
			hql.append(" ORDER BY ");
			for (int i = 0; i< orderBy.length - 1; i++) {
				hql.append(orderBy[i]).append(", ");
			}
			hql.append(orderBy[orderBy.length-1]).append(" ");
			if (sortOrder != null) {
				hql.append(sortOrder.toUpperCase()).append(" ; ");
			}
		}

		Query deleteQuery = session.createSQLQuery(hql.toString());
		deleteQuery.setParameter("task_expiry_age", Integer.parseInt(getTimeValue(maxTaskAge)));
		System.out.println("Constructed query: " + deleteQuery.getQueryString());
		logger.info("Constructed query: " + deleteQuery.getQueryString());
		try {
			deleteCount = deleteQuery.executeUpdate();
			System.out.println("[deleteStaleDocuments] number of deleted records = " + deleteCount);
			logger.info("[deleteStaleDocuments] number of deleted records = " + deleteCount);
		} catch (Exception e) {
			logger.error("Exception in executing SQL delete stale docs query");
			logger.error(elog.toStringException(e));
		}
		return deleteCount;
	}

	/**
	 * 
	 * @param timeString
	 * @return duration in milliseconds. Negative indicates an invalid parse result
	 */

	@SuppressWarnings("unused")
	private long parseTime(final String timeString) {
		long duration = -1;		
		assert timeString != null;
		float value = Float.parseFloat(timeString.substring(0, timeString.length()-1));
		if (value > 0) {
			String suffix = timeString.substring(timeString.length() - 1, timeString.length());
			if (suffix != null) {
				if (suffix.equalsIgnoreCase("s"))
					duration = Math.round(value * 1000);
				else if (suffix.equalsIgnoreCase("m"))
					duration = Math.round(value * 60 * 1000) ;
				else if (suffix.equalsIgnoreCase("h"))
					duration = Math.round(value * 60 * 60 * 1000);
				else if (suffix.equalsIgnoreCase("d"))
					duration = Math.round(value * 60 * 60 * 24 * 1000);
				else
					duration = Math.round(value * 60 * 1000);		// default is minutes
			}
			else
				duration = Math.round(value * 60 * 1000);		// default is minutes
		}
		return duration;
	}


	private String getTimeValue(final String timeString) {
		assert timeString != null;
		return timeString.substring(0, timeString.length()-1);
	}


	private String getMetric(final String timeString) {
		assert timeString != null;
		String metric = "HOUR";			// default
		String suffix = timeString.substring(timeString.length() - 1, timeString.length());
		if (suffix != null) {
			if (suffix.equalsIgnoreCase("s"))
				metric = "SECOND"; 
			else if (suffix.equalsIgnoreCase("m"))
				metric = "MINUTE";
			else if (suffix.equalsIgnoreCase("h"))
				metric = "HOUR";
			else if (suffix.equalsIgnoreCase("d"))
				metric = "DAY";
		}
		return metric;
	}

	@Override
	public DocumentDTO addDocument(DocumentDTO doc) {
		try {
			Document d = doc.toEntity();
			em.persist(d);
			em.flush();
			em.refresh(d);
			return new DocumentDTO(d);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public DocumentDTO editDocument(DocumentDTO doc) throws PropertyNotSetException {
		System.out.println("Received request for: " + doc.getDocumentID() + ", " + doc.getCrisisDTO().getCode());
		try {
			Document d = doc.toEntity();
			Document oldDoc = getById(d.getDocumentId()); 
			if (oldDoc != null) {
				oldDoc = em.merge(d);
				return oldDoc != null ? new DocumentDTO(oldDoc) : null;
			} else {
				throw new RuntimeException("Not found");
			}
		} catch (Exception e) {
			System.out.println("Exception in merging/updating document: " + doc.getDocumentID());
			e.printStackTrace();	
		}
		return null;
	}

	@Override
	public Integer deleteDocument(DocumentDTO doc) {
		try {
			em.remove(doc.toEntity()); 
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	@Override
	public List<DocumentDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException {
		List<Document> list = getAllByCriteria(Restrictions.eq(columnName,value));
		List<DocumentDTO> dtoList = new ArrayList<DocumentDTO>();
		if (list != null && !list.isEmpty()) {
			for (Document c: list) {
				dtoList.add(new DocumentDTO(c));
			}
		}
		return dtoList;
	}

	@Override
	public DocumentDTO findDocumentByID(Long id) throws PropertyNotSetException {
		Document doc = getById(id);
		if (doc != null) {
			DocumentDTO dto = new DocumentDTO(doc);
			return dto;
		} else {
			return null;
		}
	}

	@Override
	public List<DocumentDTO> findDocumentsByCrisisID(Long crisisId) throws PropertyNotSetException {
		List<DocumentDTO> dtoList = findByCriteria("crisis.crisisId", crisisId);
		return dtoList;
	}

	@Override
	public DocumentDTO getDocumentWithAllFieldsByID(Long id) throws PropertyNotSetException {
		Document doc = getById(id);
		if (doc != null) {
			Hibernate.initialize(doc.getCrisis());
			Hibernate.initialize(doc.getDocumentNominalLabels());
			Hibernate.initialize(doc.getTaskAssignments());

			DocumentDTO dto = new DocumentDTO(doc);
			return dto;
		} else {
			return null;
		}
	}

	@Override
	public boolean isDocumentExists(Long id) throws PropertyNotSetException {
		DocumentDTO dto = findDocumentByID(id); 
		return dto != null ? true : false;
	}

	@Override
	public List<DocumentDTO> getAllDocuments() throws PropertyNotSetException {
		System.out.println("Received request for fetching all Documents!!!");
		List<DocumentDTO> dtoList = new ArrayList<DocumentDTO>();
		List<Document> list = getAll();
		if (list != null && !list.isEmpty()) {
			for (Document doc : list) {
				//System.out.println("Converting to DTO Document: " + doc.getDocumentId() + ", " + doc.getCrisis().getCode() + ", " + doc.isHasHumanLabels());
				DocumentDTO dto = new DocumentDTO(doc);
				dtoList.add(dto);
			}
		}
		System.out.println("Done creating DTO list, size = " + dtoList.size());
		return dtoList;
	}

	@Override
	public List<DocumentDTO> findLabeledDocumentsByCrisisID(Long crisisId) throws PropertyNotSetException {
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis.crisisId",crisisId))
				.add(Restrictions.eq("hasHumanLabels", true));
		List<DocumentDTO> dtoList = new ArrayList<DocumentDTO>();
		List<Document> list = this.getAllByCriteria(criterion);
		if (list != null && !list.isEmpty()) {
			for (Document doc : list) {
				//System.out.println("Converting to DTO Document: " + doc.getDocumentId() + ", " + doc.getCrisis().getCode() + ", " + doc.isHasHumanLabels());
				DocumentDTO dto = new DocumentDTO(doc);
				dtoList.add(dto);
			}
		}
		System.out.println("Done creating DTO list, size = " + dtoList.size());
		return dtoList;
	}

	@Override
	public List<DocumentDTO> findUnLabeledDocumentsByCrisisID(Long crisisId) throws PropertyNotSetException {
		Criterion criterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis.crisisId",crisisId))
				.add(Restrictions.eq("hasHumanLabels", false));
		List<DocumentDTO> dtoList = new ArrayList<DocumentDTO>();
		List<Document> list = this.getAllByCriteria(criterion);
		if (list != null && !list.isEmpty()) {
			for (Document doc : list) {
				//System.out.println("Converting to DTO Document: " + doc.getDocumentId() + ", " + doc.getCrisis().getCode() + ", " + doc.isHasHumanLabels());
				DocumentDTO dto = new DocumentDTO(doc);
				dtoList.add(dto);
			}
		}
		System.out.println("Done creating DTO list, size = " + dtoList.size());
		return dtoList;
	}

	@Override
	public List<DocumentDTO> getDocumentCollectionWithNominalLabelData(Long nominalLabelID) throws Exception {
		List<DocumentDTO> dtoList = new ArrayList<DocumentDTO>();
		if (nominalLabelID != null) {
			String aliasTable = "documentNominalLabels";
			String aliasTableKeyField = "documentNominalLabels.id.nominalLabelId";
			String[] orderBy = {"documentId"};

			Criterion criterion = Restrictions.eq("hasHumanLabels", true);
			Criterion aliasCriterion =  Restrictions.eq(aliasTableKeyField, nominalLabelID);
			try {
				List<Document> docList = this.getByCriteriaWithInnerJoinByOrder(criterion, "DESC", orderBy, null, aliasTable, aliasCriterion);
				logger.debug("docList = " + docList);
				if (docList != null && !docList.isEmpty()) {
					logger.info("Fetched size = " + docList.size());
					NominalLabelDTO nominalLabel = nominalLabelEJB.getNominalLabelByID(nominalLabelID);
					for (Document doc: docList) {
						DocumentDTO dto = new DocumentDTO(doc);
						dto.setNominalLabelDTO(nominalLabel);
						dtoList.add(dto);	
					}
					System.out.println("Done creating DTO list, size = " + dtoList.size());
				}
			} catch (Exception e) {
				throw new Exception();
			}
		}
		return dtoList;
	}

}