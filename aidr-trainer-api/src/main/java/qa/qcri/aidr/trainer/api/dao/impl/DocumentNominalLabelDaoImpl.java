package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.DocumentNominalLabelDao;
import qa.qcri.aidr.trainer.api.entity.DocumentNominalLabel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DocumentNominalLabelDaoImpl extends AbstractDaoImpl<DocumentNominalLabel, String> implements DocumentNominalLabelDao {

    protected DocumentNominalLabelDaoImpl(){
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

        DocumentNominalLabel obj =  findByCriterionID(Restrictions.allEq(attMap));

        if(obj != null)
            return true;

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
