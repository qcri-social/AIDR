package qa.qcri.aidr.task.ejb.bean;

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

@Stateless(name="DocumentServiceBean")
public class DocumentServiceBean extends AbstractTaskManagerServiceBean<Document, Long> implements DocumentService {
	
	//@PersistenceContext(unitName = "qa.qcri.aidr.taskmanager-EJBS") 
	//private EntityManager em;
	
	public DocumentServiceBean() {
		super(Document.class);
	}
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void updateHasHumanLabel(Document document) {
		Document doc = getByCriteria(Restrictions.eq("documentID", document.getDocumentID()));
		if (doc != null) {
			doc.setHasHumanLabels(true);
			save(doc);
		}
	}

	@Override
	public int deleteDocument(Document document) {
		String hql = "delete from Document where documentID = :documentID AND !hasHumanLabels";
		try {
			Query query = getCurrentSession().createQuery(hql);
			query.setLong("documentID", document.getDocumentID());
			query.executeUpdate();
		} catch (Exception e) {
			System.err.println("[deleteDocument] Deletion query failed");
			return 0;
		}
		return 1;
	}


	@Override
	public int deleteDocument(List<Document> collection) {
		int deleteCount = 0;
		if (!collection.isEmpty()) {
			List<Long> deleteList = new ArrayList<Long>();
			for (Document doc : collection) {
				deleteList.add(doc.getDocumentID());
			}
			String hql = "DELETE from Document WHERE (documentID IN (:documentID) "
					+ " && !hasHumanLabels)";
			Session session = getCurrentSession();
			Query collectionDeleteQuery = session.createQuery(hql);
			try {
				Transaction tx = session.beginTransaction();
				collectionDeleteQuery.setParameterList("documentID", deleteList);
				deleteCount = collectionDeleteQuery.executeUpdate();
				tx.commit();
			} catch (Exception e) {
				System.err.println("[deleteTask] Collection deletion query failed");
			}
		}
		return deleteCount;
	}
	
	@Override
	public int deleteUnassignedDocument(Document document) {
		String hql = "DELETE from Document WHERE (documentID = :documentID && "
				+ " documentID NOT IN (SELECT documentID FROM TaskAssignment) "
				+ " && !hasHumanLabels)";
		try {
			Query query = getCurrentSession().createQuery(hql);
			query.setLong("documentID", document.getDocumentID());
			query.executeUpdate();
		} catch (Exception e) {
			System.err.println("[deleteUnAssignedTask] Deletion query failed");
			return 0;
		}
		return 1;
	}

	@Override
	public int deleteUnassignedDocumentCollection(List<Document> collection) {
		int deleteCount = 0;
		if (!collection.isEmpty()) {
			List<Long> deleteList = new ArrayList<Long>();
			for (Document doc : collection) {
				deleteList.add(doc.getDocumentID());
			}
			String hql = "DELETE from Document WHERE (documentID IN (:documentID) "
					+ " documentID NOT IN (SELECT documentID FROM TaskAssignment) "
					+ " && !hasHumanLabels)";
			Session session = getCurrentSession();
			Query collectionDeleteQuery = session.createQuery(hql);
			try {
				collectionDeleteQuery.setParameterList("documentID", deleteList);
				Transaction tx = session.beginTransaction();
				deleteCount = collectionDeleteQuery.executeUpdate();
				tx.commit();
			} catch (Exception e) {
				System.err.println("[deleteTask] Collection deletion query failed");
			}
		}
		return deleteCount;
	}
	
	@Override
	public int deleteStaleDocuments(String joinType, String joinTable, String joinColumn,
								    String sortOrder, String[] orderBy, 
								    final String maxTaskAge, final String scanInterval) {
		
		int deleteCount = 0;
		Session session = getCurrentSession();
		StringBuffer hql = new StringBuffer("DELETE d FROM Document d ");
        if (joinType.equalsIgnoreCase("LEFT_JOIN")) {
        	hql.append(" LEFT JOIN ").append(joinTable).append(" t ");
        } else if (joinType.equalsIgnoreCase("JOIN")) {
        	hql.append(" JOIN ").append(joinTable).append(" t ");
        }
        hql.append(" ON d.documentID = t.documentID WHERE ")
		   .append("(!d.hasHumanLabels && t.documentID IS NULL && TIMESTAMPDIFF(")
		   .append(getMetric(scanInterval))
		   .append(", d.receivedAt, now()) > :task_expiry_age)");
        
        Query deleteQuery = session.createQuery(hql.toString());
        deleteQuery.setParameter("task_expiry_age", Integer.parseInt(getTimeValue(maxTaskAge)));
        try {
        	deleteCount = deleteQuery.executeUpdate();
			System.out.println("[deleteStaleDocuments] number of deleted no answer records = " + deleteCount);
		} catch (Exception e) {
			System.err.println("[deleteStaleDocuments] Exception in executing SQL delete no answer docs query");
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

