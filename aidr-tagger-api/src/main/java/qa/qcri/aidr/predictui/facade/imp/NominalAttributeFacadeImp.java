/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.predictui.facade.*;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.NominalAttributeResourceFacade;
import qa.qcri.aidr.predictui.entities.NominalAttribute;

/**
 *
 * @author Imran
 */
@Stateless
public class NominalAttributeFacadeImp implements NominalAttributeFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;
    
    @EJB
    NominalAttributeResourceFacade nominalAttributeRemoteEJB;

    public List<NominalAttributeDTO> getAllAttributes() throws PropertyNotSetException {
        return nominalAttributeRemoteEJB.getAllAttributes();
    }

    public List<qa.qcri.aidr.dbmanager.dto.CrisisAttributesDTO> getAllAttributesExceptCrisis(Long crisisID) throws PropertyNotSetException{
       return nominalAttributeRemoteEJB.getAllAttributesExceptCrisis(crisisID);
    }

    public boolean addAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException{
        return nominalAttributeRemoteEJB.addAttribute(attribute);
    }

    public NominalAttributeDTO editAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException{
        return nominalAttributeRemoteEJB.editAttribute(attribute);

    }

    public NominalAttributeDTO getAttributeByID(Long attributeID) throws PropertyNotSetException{
        return nominalAttributeRemoteEJB.getAttributeByID(attributeID);
    }

    public boolean deleteAttribute(Long attributeID) throws PropertyNotSetException{
        return nominalAttributeRemoteEJB.deleteAttribute(attributeID);
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
