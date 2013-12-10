/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.predictui.dto.CrisisTypeDTO;
import qa.qcri.aidr.predictui.entities.CrisisType;
import qa.qcri.aidr.predictui.facade.CrisisTypeResourceFacade;

/**
 *
 * @author Imran
 */
@Stateless
public class CrisisTypeResourceImp implements CrisisTypeResourceFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    public List<CrisisTypeDTO> getCrisisTypes() {
        List<CrisisType> crisisList = new ArrayList<CrisisType>();
        List<CrisisTypeDTO> crisisTypeDTOList = new ArrayList<CrisisTypeDTO>();
        Query q = em.createNamedQuery("CrisisType.findAll", CrisisType.class);
        crisisList = q.getResultList();
        for (CrisisType cType: crisisList){
            int crisisAssociated = cType.getCrisisCollection().size();
            CrisisTypeDTO ctDTO = new CrisisTypeDTO();
            ctDTO.setCrisisTypeID(cType.getCrisisTypeID());
            ctDTO.setName(cType.getName());
            ctDTO.setNumberOfCrisisAssociated(crisisAssociated);
            
            crisisTypeDTOList.add(ctDTO);
        }
        
        return crisisTypeDTOList;
    }

    public CrisisType addCrisisType(CrisisType crisisType) {
        System.out.println(crisisType.getName());
        em.persist(crisisType);
        return crisisType;
    }

    public CrisisType getCrisisTypeByID(int id) {
        Query query = em.createNamedQuery("CrisisType.findByCrisisTypeID", CrisisType.class);
        query.setParameter("crisisTypeID", id);
        return (CrisisType) query.getSingleResult();
    }

    public CrisisType editCrisisType(CrisisType crisis) {
        CrisisType newCrisisType = em.merge(crisis);
        return newCrisisType;
    }

    public void deleteCrisisType(int id) {
        CrisisType crisisType = em.find(CrisisType.class, id);
        if (crisisType != null) {
            em.remove(crisisType);
        }
        else{
            throw new RuntimeException();
        }
    }
}
