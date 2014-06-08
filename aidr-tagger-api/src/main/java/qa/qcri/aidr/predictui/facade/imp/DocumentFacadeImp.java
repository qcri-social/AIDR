package qa.qcri.aidr.predictui.facade.imp;

//import qa.qcri.aidr.predictui.facade.*;
import qa.qcri.aidr.predictui.util.TaskManagerEntityMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


import org.codehaus.jackson.type.TypeReference;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.Document;
import qa.qcri.aidr.predictui.facade.DocumentFacade;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;

/**
 *
 * @author koushik
 */
@Stateless
public class DocumentFacadeImp implements DocumentFacade{
    
    //@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    //private EntityManager em;
    
    @EJB
	private TaskManagerRemote<qa.qcri.aidr.task.entities.Document, Long> taskManager;


    public List<Document> getAllDocuments() {
        //Query query = em.createNamedQuery("Document.findAll", Document.class);
    	//List<Document> documentList = query.getResultList();
    	
    	TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
    	String jsonString  = taskManager.getAllTasks();
        
    	List<Document> documentList = mapper.deSerializeList(jsonString, new TypeReference<List<Document>>() {});
        return documentList;
        
    }

    public Document getDocumentByID(long id) {
        //Query query = em.createNamedQuery("Document.findByDocumentID", Document.class);
        //query.setParameter("documentID", id);
        //Document document = (Document)query.getSingleResult();
        
    	TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
    	String jsonString  = taskManager.getTaskById(id);
    	Document document = mapper.deSerialize(jsonString, Document.class);
    	return document;
    }

    public List<Document> getAllLabeledDocumentbyCrisisID(long crisisID, long attributeID) {
        //Query query = em.createNamedQuery("Document.findByCrisisID", Document.class);
        //query.setParameter("crisisID", crisisID);
        //query.setParameter("attributeID", attributeID);
        //List<Document> documentList = query.getResultList();
    	
    	TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
    	Criterion criterion = Restrictions.eq("hasHumanLabels", true);
    	String jsonString  = taskManager.getTaskCollectionByCriterion(crisisID, null, criterion);
    	List<Document> documentList = mapper.deSerializeList(jsonString, new TypeReference<List<Document>>() {});
        
        return documentList;
    }

    @Override
    public void deleteDocument(Long documentID) {
        /*
    	Document document = em.find(Document.class, documentID);
        if (document != null) {
            em.remove(document);
        }*/
    	taskManager.deleteTaskById(documentID);
    }

    @Override
    public void removeTrainingExample(Long documentID) {
        /*
    	Document document = em.find(Document.class, documentID);
        if (document != null) {
            document = em.merge(document);
            document.setHasHumanLabels(false);
            document.setNominalLabelCollection(null);
        }
        */
    	Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("setHasHumanLabels", new Boolean(false).toString());
    	paramMap.put("setNominalLabelCollection", null);
    	qa.qcri.aidr.task.entities.Document newDoc = taskManager.setTaskParameter(qa.qcri.aidr.task.entities.Document.class, documentID, paramMap);
    }

	@Override
	public List<Document> getAllUnlabeledDocumentbyCrisisID(Crisis crisis) {
		//Query query = em.createNamedQuery("Document.findByCrisisID", Document.class);
        //query.setParameter("crisisID", crisis.getCrisisID());
        //query.setParameter("hasHumanLabels", false);
        //List<Document> documentList = query.getResultList();
        
		TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
    	Criterion criterion = Restrictions.eq("hasHumanLabels", false);
    	String jsonString  = taskManager.getTaskCollectionByCriterion(crisis.getCrisisID(), null, criterion);
    	List<Document> documentList = mapper.deSerializeList(jsonString, new TypeReference<List<Document>>() {});
        
        return documentList;
	}
    
}
