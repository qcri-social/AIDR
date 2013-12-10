/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import qa.qcri.aidr.predictui.facade.*;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.predictui.dto.CrisisAttributesDTO;
import qa.qcri.aidr.predictui.entities.NominalAttribute;
import qa.qcri.aidr.predictui.entities.NominalLabel;

/**
 *
 * @author Imran
 */
@Stateless
public class NominalAttributeFacadeImp implements NominalAttributeFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    public List<NominalAttribute> getAllAttributes() {
        List<NominalAttribute> attributesList = null;
        try {
            Query query = em.createNamedQuery("NominalAttribute.findAll", NominalAttribute.class);
            attributesList = query.getResultList();
            for (NominalAttribute attribute : attributesList) {
                try {
                    Query labelQuery = em.createNamedQuery("NominalLabel.findByNominalAttribute", NominalLabel.class);
                    labelQuery.setParameter("nominalAttribute", attribute);
                    attribute.setNominalLabelCollection(labelQuery.getResultList());
                } catch (NoResultException e) {
                    attribute.setNominalLabelCollection(null);
                }
            }
        } catch (NoResultException e) {
            return null;
        }

        return attributesList;

    }

    public List<CrisisAttributesDTO> getAllAttributesExceptCrisis(int crisisID) {
        List<CrisisAttributesDTO> attributesList = new ArrayList();
        String sql = "SELECT na.nominalAttributeID, na.userID, na.name, na.description, na.code, "
                + " nl.nominalLabelID, nl.name AS lblName FROM nominal_attribute na \n"
                + " JOIN nominal_label nl ON na.nominalAttributeID = nl.nominalAttributeID \n"
                + " LEFT JOIN model_family mf ON na.nominalAttributeID = mf.nominalAttributeID \n"
                + " AND mf.crisisID = :crisisID WHERE mf.crisisID IS NULL";
        try {
            Query query = em.createNativeQuery(sql);
            query.setParameter("crisisID", crisisID);
            List<Object[]> rows = query.getResultList();
            CrisisAttributesDTO attribute;
            for (Object[] row : rows) {
                attribute = new CrisisAttributesDTO();
                attribute.setNominalAttributeID(((Integer) row[0]).intValue());
                attribute.setUserID(((Integer) row[1]).intValue());
                attribute.setName((String) row[2]);
                attribute.setDescription((String) row[3]);
                attribute.setCode(((String) row[4]));
                attribute.setLabelID(((Integer) row[5]).intValue());
                attribute.setLabelName(((String) row[6]));
                attributesList.add(attribute);
            }
            return attributesList;
        } catch (NoResultException e) {
            return null;
        }
    }

    public NominalAttribute addAttribute(NominalAttribute attribute) {
        em.persist(attribute);
        return attribute;
    }

    public NominalAttribute editAttribute(NominalAttribute attribute) {
        if (attribute == null) {
            throw new RuntimeException("Missing data values");
        }
        NominalAttribute attFound = em.find(NominalAttribute.class, attribute.getNominalAttributeID());
        if (attFound != null) {
            em.merge(attribute);
            return attribute;
        } else {
            throw new RuntimeException("Can't edit");
        }

    }

    public NominalAttribute getAttributeByID(int attributeID) {
        NominalAttribute attribute = null;
        try {
            Query query = em.createNamedQuery("NominalAttribute.findByNominalAttributeID", NominalAttribute.class);
            query.setParameter("nominalAttributeID", attributeID);
            attribute = (NominalAttribute) query.getSingleResult();
            try {
                Query labelQuery = em.createNamedQuery("NominalLabel.findByNominalAttribute", NominalLabel.class);
                labelQuery.setParameter("nominalAttribute", attribute);
                attribute.setNominalLabelCollection(labelQuery.getResultList());
            } catch (NoResultException e) {
                attribute.setNominalLabelCollection(null);
            }

        } catch (NoResultException e) {
            return null;
        }
        return attribute;
    }

    @Override
    public void deleteAttribute(int attributeID) {
        NominalAttribute attribute = em.find(NominalAttribute.class, attributeID);
        if (attribute != null) {
            em.remove(attribute);
        }
    }

    @Override
    public Integer isAttributeExists(String attributeCode) {
        try {
            Query query = em.createNamedQuery("NominalAttribute.findByCode", NominalAttribute.class);
            query.setParameter("code", attributeCode);

            if (query.getSingleResult() != null) {
                NominalAttribute attribute = (NominalAttribute) query.getSingleResult();
                return attribute.getNominalAttributeID();
            }
        } catch (NoResultException e) {
            return null;
        }
        return null;
    }
}
