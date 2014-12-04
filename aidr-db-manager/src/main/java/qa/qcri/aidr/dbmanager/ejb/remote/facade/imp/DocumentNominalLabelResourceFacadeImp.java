package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentNominalLabelResourceFacade;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;

/**
 * @author Koushik
 */

@Stateless(name="DocumentNominalLabelResourceFacadeImp")
public class DocumentNominalLabelResourceFacadeImp 
			extends CoreDBServiceFacadeImp<DocumentNominalLabel, Long> implements DocumentNominalLabelResourceFacade {

	private Logger logger = LoggerFactory.getLogger("db-manager-log");
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
        attMap.put("documentID", documentNominalLabel.getDocumentDTO().getDocumentID());
        attMap.put("nominalLabelID", documentNominalLabel.getNominalLabelDTO().getNominalLabelId());

        DocumentNominalLabel obj =  getByCriterionID(Restrictions.allEq(attMap));

        if(obj != null)
            return true;

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

