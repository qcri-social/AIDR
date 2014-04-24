package qa.qcri.aidr.task.facade.imp;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.task.entities.Document;
import qa.qcri.aidr.task.facade.DocumentFacade;


/**
 *
 * @author Imran
 */
@Stateless
public class DocumentFacadeImp implements DocumentFacade{
    
    @PersistenceContext(unitName = "qa.qcri.aidr.taskmanager-EJBS") org.hibernate.Session session;
    @PersistenceUnit(unitName = "qa.qcri.aidr.taskmanager-EJBS") SessionFactory sessionFactory;
    private EntityManager em;
	
    public List<Document> getAllDocuments() {
    	Query query = em.createNamedQuery("Document.findAll", Document.class);
        List<Document> documentList = query.getResultList();
        return documentList;
        
    }

    public Document getDocumentByID(long id) {
        Query query = em.createNamedQuery("Document.findByDocumentID", Document.class);
        query.setParameter("documentID", id);
        Document document = (Document)query.getSingleResult();
        return document;
    }

    public List<Document> getAllLabeledDocumentbyCrisisID(long crisisID, long attributeID) {
        Query query = em.createNamedQuery("Document.findByCrisisID", Document.class);
        query.setParameter("crisisID", crisisID);
        query.setParameter("attributeID", attributeID);
        List<Document> documentList = query.getResultList();
        
        
        return documentList;
    }

    @Override
    public void deleteDocument(Long documentID) {
        Document document = em.find(Document.class, documentID);
        if (document != null) {
            em.remove(document);
        }
    }


	@Override
	public List<Document> getAllLabeledDocumentsbyCrisisID(Long crisisID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Document> getAllUnlabeledDocumentsbyCrisisID(Long crisisID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDocumentCollectionByCrisisID(Long crisisID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDocumentCollection(List<Document> documentCollection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Document getDocumentByID(Long documentID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Document> getAllDocumentsByCrisisID(Long crisisID) {
		Session session = (Session) em.getDelegate();
		Criteria criteria = session.createCriteria(Document.class);
        criteria.add(Restrictions.eq("crisisID", crisisID));
        return (List<Document>) criteria.list();
	
		
		//CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        //CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        //Root<Document> doc = criteriaQuery.from(Document.class);
        //criteriaQuery.where(criteriaBuilder.equal(, y))
        //criteriaQuery(Restrictions.eq("crisisID", crisisID));
	}

	@Override
	public int getAllDocumentsCountByCrisisID(Long crisisID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAllLabeledDocumentsCountbyCrisisID(Long crisisID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAllUnlabeledDocumentsCountbyCrisisID(Long crisisID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateDocument(Document document) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDocumentCollection(List<Document> documentCollection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDocument(Document document) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDocumentCollection(List<Document> documentCollection) {
		// TODO Auto-generated method stub
		
	}
    
}
