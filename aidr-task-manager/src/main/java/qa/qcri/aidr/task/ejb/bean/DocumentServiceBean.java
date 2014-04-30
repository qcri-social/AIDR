package qa.qcri.aidr.task.ejb.bean;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.task.ejb.DocumentService;
import qa.qcri.aidr.task.entities.Document;

@Stateless(name="DocumentServiceBean")
public class DocumentServiceBean extends AbstractTaskManagerServiceBean<Document, String> implements DocumentService {
	
	@PersistenceContext(unitName = "qa.qcri.aidr.taskmanager-EJBS") 
	private EntityManager em;
	
	public DocumentServiceBean() {
		super(Document.class);
	}
	
	@Override
	public void updateHasHumanLabel(Document document) {
		Document doc = getByCriteria(Restrictions.eq("documentID", document.getDocumentID()));
		if (doc != null) {
			doc.setHasHumanLabels(true);
			save(doc);
		}
	}

}
