package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.predictui.facade.*;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.Document;

/**
 *
 * @author Imran
 */
@Stateless
public class DocumentFacadeImp implements DocumentFacade{
    
    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
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
    public void removeTrainingExample(Long documentID) {
        Document document = em.find(Document.class, documentID);
        if (document != null) {
            document = em.merge(document);
            document.setHasHumanLabels(false);
            document.setNominalLabelCollection(null);
        }
    }

	@Override
	public List<Document> getAllUnlabeledDocumentbyCrisisID(Crisis crisis) {
		Query query = em.createNamedQuery("Document.findByCrisisID", Document.class);
        query.setParameter("crisisID", crisis.getCrisisID());
        query.setParameter("hasHumanLabels", false);
        List<Document> documentList = query.getResultList();
        
        return documentList;
	}
    
}
