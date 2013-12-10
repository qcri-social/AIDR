/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.predictui.dto.NominalLabelDTO;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.NominalAttribute;
import qa.qcri.aidr.predictui.entities.NominalLabel;
import qa.qcri.aidr.predictui.facade.NominalLabelResourceFacade;

/**
 *
 * @author Imran
 */
@Stateless
public class NominalLabelResourceImp implements NominalLabelResourceFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    public NominalLabel addNominalLabel(NominalLabelDTO label) {

        Query attributeQuery = em.createNamedQuery("NominalAttribute.findByNominalAttributeID", NominalAttribute.class);
        attributeQuery.setParameter("nominalAttributeID", label.getNominalAttributeID());
        NominalAttribute dbAtt = (NominalAttribute) attributeQuery.getSingleResult();

        NominalLabel labelDB = new NominalLabel();
        labelDB.setDescription(label.getDescription());
        labelDB.setName(label.getName());
        labelDB.setNominalAttribute(dbAtt);
        labelDB.setNominalLabelCode(label.getNominalLabelCode());

        em.persist(labelDB);
        return labelDB;
    }

    public NominalLabel getNominalLabelByID(int id) {
        NominalLabel label = null;
        try {
            Query query = em.createNamedQuery("NominalLabel.findByNominalLabelID", NominalLabel.class);
            query.setParameter("nominalLabelID", id);

            label = (NominalLabel) query.getSingleResult();

        } catch (NoResultException e) {
            return null;
        }

        return label;
    }

    @Override
    public NominalLabel editNominalLabel(NominalLabelDTO label) {
        if (label == null) {
            return null;
        }
        NominalLabel labelToDB = new NominalLabel();
        try {
            
            Query attributeQuery = em.createNamedQuery("NominalAttribute.findByNominalAttributeID", NominalAttribute.class);
        attributeQuery.setParameter("nominalAttributeID", label.getNominalAttributeID());
        NominalAttribute dbAtt = (NominalAttribute) attributeQuery.getSingleResult();

        
        labelToDB.setDescription(label.getDescription());
        labelToDB.setName(label.getName());
        labelToDB.setNominalAttribute(dbAtt);
        labelToDB.setNominalLabelCode(label.getNominalLabelCode());
        labelToDB.setNominalLabelID(label.getNominalLabelID());
            
            NominalLabel dbLabel = em.find(NominalLabel.class, labelToDB.getNominalLabelID());
            if (dbLabel != null) {
                em.merge(labelToDB);
                return labelToDB;
            }
        } catch (NoResultException e) {
            return null;
        }
        return labelToDB;

    }

    public List<NominalLabel> getAllNominalLabel() {
        List<NominalLabel> labelList = new ArrayList<NominalLabel>();
        Query q = em.createNamedQuery("NominalLabel.findAll", NominalLabel.class);
        labelList = q.getResultList();
        return labelList;
    }

    @Override
    public void deleteNominalLabel(int labelID) {
        NominalLabel label = em.find(NominalLabel.class, labelID);
        if (label != null) {
            em.remove(label);
        }
    }
}
