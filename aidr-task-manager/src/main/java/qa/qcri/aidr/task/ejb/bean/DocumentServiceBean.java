package qa.qcri.aidr.task.ejb.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.task.ejb.DocumentService;
import qa.qcri.aidr.task.entities.Document;

/**
 * 
 * @author Koushik
 *
 */
@Stateless(name="DocumentServiceBean")
public class DocumentServiceBean extends AbstractTaskManagerServiceBean<Document, Long> implements DocumentService {

	public DocumentServiceBean() {
		super(Document.class);
	}

	@Override
	public void updateHasHumanLabel(Document document) {
		Document doc = (Document) getByCriteria(Restrictions.eq("documentID", document.getDocumentID()));
		if (doc != null) {
			doc.setHasHumanLabels(true);
			save(doc);
		}
	}


	@Override
	public int deleteNoLabelDocument(Document document) {
		System.out.println("[deleteNoLabelDocument]) Received request for : " + document.getDocumentID());
		int deleteCount = 0;
		if (!document.isHasHumanLabels()) {
			try {
				//delete(document);
				String hql = "DELETE from document WHERE documentID = :documentID";
				Session session = getCurrentSession();
				Query collectionDeleteQuery = session.createSQLQuery(hql);
				try {
					//Transaction tx = (session.getTransaction() != null) ? session.getTransaction(): session.beginTransaction();
					collectionDeleteQuery.setParameter("documentID", document.getDocumentID());
					deleteCount = collectionDeleteQuery.executeUpdate();
					//tx.commit();
			
					System.out.println("[deleteNoLabelDocument]) deleted count = " + deleteCount);
				} catch (Exception e) {
					System.err.println("[deleteNoLabelDocument] deletion query failed");
					e.printStackTrace();
					return 0;
				}
				System.out.println("[deleteNoLabelDocument]) deletion success, deleted count = " + deleteCount);
				session.flush();
				return 1;
				
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("[deleteNoLabelDocument] Deletion query failed");
				return 0;
			}
		}
		System.out.println("[deleteNoLabelDocument]) Document has label. Not deleting.");
		return 0;
	}



	@Override
	public int deleteNoLabelDocument(List<Document> collection) {
		int deleteCount = 0;
		if (!collection.isEmpty()) {
			List<Long> deleteList = new ArrayList<Long>();
			for (Document doc : collection) {
				deleteList.add(doc.getDocumentID());
				System.out.println("Attempt to delete: " + doc.getDocumentID());
			}
			String hql = "DELETE from document WHERE (documentID IN (:documentID) "
					+ " AND !hasHumanLabels)";
			Session session = getCurrentSession();
			Query collectionDeleteQuery = session.createSQLQuery(hql);
			try {
				Transaction tx = session.beginTransaction();
				collectionDeleteQuery.setParameterList("documentID", deleteList);
				deleteCount = collectionDeleteQuery.executeUpdate();
				tx.commit();
				System.out.println("[deleteNoLabelDocument]) deleted count = " + deleteCount);
			} catch (Exception e) {
				System.err.println("[deleteNoLabelDocument] Collection deletion query failed");
				e.printStackTrace();
			}
		}
		return deleteCount;
	}


	@Override
	public int deleteUnassignedDocument(Document document) {
		String hql = "DELETE from Document WHERE (documentID = :documentID AND "
				+ " documentID NOT IN (SELECT documentID FROM TaskAssignment))";
		if (!document.isHasHumanLabels()) {
			try {
				Query query = getCurrentSession().createQuery(hql);
				query.setParameter("documentID", document.getDocumentID());
				int result = query.executeUpdate();
				return result;
			} catch (Exception e) {
				System.err.println("[deleteUnAssignedTask] Deletion query failed");
				e.printStackTrace();
				return 0;
			}
		}
		return 0;
	}

	@Override
	public int deleteUnassignedDocumentCollection(List<Document> collection) {
		int deleteCount = 0;
		if (!collection.isEmpty()) {
			List<Long> deleteList = new ArrayList<Long>();
			List<Boolean> labelList = new ArrayList<Boolean>();
			for (Document doc : collection) {
				if (!doc.isHasHumanLabels()) deleteList.add(doc.getDocumentID());
				labelList.add(false);
			}
			String hql = "DELETE from Document WHERE (documentID IN (:documentID) "
					+ " AND documentID NOT IN (SELECT documentID FROM TaskAssignment) ";
					//+ " AND hasHumanLabels = :hasHumanLabels)";
			Session session = getCurrentSession();
			Query collectionDeleteQuery = session.createQuery(hql);
			try {
				collectionDeleteQuery.setParameterList("documentID", deleteList);
				//collectionDeleteQuery.setParameterList("hasHumanLabels", labelList);
				Transaction tx = session.beginTransaction();
				deleteCount = collectionDeleteQuery.executeUpdate();
				tx.commit();
			} catch (Exception e) {
				System.err.println("[deleteTask] Collection deletion query failed");
				e.printStackTrace();
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
		try {
			deleteCount = deleteQuery.executeUpdate();
			System.out.println("[deleteStaleDocuments] number of deleted records = " + deleteCount);
		} catch (Exception e) {
			System.err.println("[deleteStaleDocuments] Exception in executing SQL delete stale docs query");
			e.printStackTrace();
		}
		return deleteCount;
	}


	/**
	 * 
	 * @param timeString
	 * @return duration in milliseconds. Negative indicates an invalid parse result
	 */

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
}

