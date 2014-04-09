package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.DocumentDao;
import qa.qcri.aidr.trainer.api.entity.Document;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/1/13
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DocumentDaoImpl extends AbstractDaoImpl<Document, String> implements DocumentDao {

    protected DocumentDaoImpl(){
        super(Document.class);
    }

    @Override
    public void updateHasHumanLabel(Document document) {
        Document cDoc = findByCriterionID(Restrictions.eq("documentID", document.getDocumentID()));
        cDoc.setHasHumanLabels(true);

        saveOrUpdate(cDoc);

    }

    @Override
    public Document findDocument(Long documentID) {
        Document cDoc = findByCriterionID(Restrictions.eq("documentID", documentID));

        return cDoc;
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Document> findDocumentForTask(Long crisisID, int requestNumber) {

        String[] orderby = {"valueAsTrainingSample","documentID"};
        String aliasTable = "taskAssignment";
        String aliasTableKey = "taskAssignment.documentID";

        Criterion criterion = Restrictions.conjunction()
                .add(Restrictions.eq("crisisID",crisisID))
                .add(Restrictions.eq("hasHumanLabels",false))
               ;

        Criterion aliasCriterion =  (Restrictions.isNull(aliasTableKey));


        return findByCriteriaWithAliasByOrder(criterion,orderby,requestNumber, aliasTable, aliasCriterion);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getAvailableTaskDocumentCount(Long crisisID) {

        String[] orderby = {"valueAsTrainingSample","documentID"};
        String aliasTable = "taskAssignment";
        String aliasTableKey = "taskAssignment.documentID";

        Criterion criterion = Restrictions.conjunction()
                .add(Restrictions.eq("crisisID",crisisID))
                .add(Restrictions.eq("hasHumanLabels",false))
                ;

        Criterion aliasCriterion =  (Restrictions.isNull(aliasTableKey));

        List<Document>  documents = findByCriteriaWithAliasByOrder(criterion,orderby,null, aliasTable, aliasCriterion);

        return documents.size();  //To change body of implemented methods use File | Settings | File Templates.
    }
}
