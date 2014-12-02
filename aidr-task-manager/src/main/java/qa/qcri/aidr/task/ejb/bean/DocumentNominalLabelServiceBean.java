package qa.qcri.aidr.task.ejb.bean;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.task.ejb.DocumentNominalLabelService;
import qa.qcri.aidr.task.entities.DocumentNominalLabel;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

/**
 * 
 * @author Koushik
 *
 */
@Stateless(name="DocumentNominalLabelServiceBean")
public class DocumentNominalLabelServiceBean extends AbstractTaskManagerServiceBean<DocumentNominalLabel, Long> implements DocumentNominalLabelService {

	private Logger logger = LoggerFactory.getLogger(DocumentNominalLabelServiceBean.class);
    protected DocumentNominalLabelServiceBean(){
        super(DocumentNominalLabel.class);
    }

    @Override
    public void saveDocumentNominalLabel(DocumentNominalLabel documentNominalLabel) {
        save(documentNominalLabel);
    }

    @Override
    public boolean foundDuplicate(DocumentNominalLabel documentNominalLabel) {
        Map<String, Long> attMap = new HashMap<String, Long>();
        attMap.put("documentID", documentNominalLabel.getDocumentID());
        attMap.put("nominalLabelID", documentNominalLabel.getNominalLabelID());

        DocumentNominalLabel obj =  getByCriterionID(Restrictions.allEq(attMap));

        if(obj != null)
            return true;

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
