/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.predictui.facade.CrisisTypeResourceFacade;

/**
 *
 * @author Imran
 */
@Stateless
public class CrisisTypeResourceImp implements CrisisTypeResourceFacade {

	//@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	//private EntityManager em;
	
	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisTypeResourceFacade remoteCrisisTypeEJB;

	public List<CrisisTypeDTO> getCrisisTypes() {
		try {
			List<CrisisTypeDTO> dtoList = remoteCrisisTypeEJB.getAllCrisisTypes();
			return dtoList;
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		/*
		List<CrisisType> crisisList = new ArrayList<CrisisType>();
		List<CrisisTypeDTO> crisisTypeDTOList = new ArrayList<CrisisTypeDTO>();

		Query q = em.createNamedQuery("CrisisType.findAll", CrisisType.class);
		try {
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
		} catch (NoResultException e) {
			return null;
		}*/
	}

	public CrisisTypeDTO addCrisisType(CrisisTypeDTO crisisType) {
		System.out.println(crisisType.getName());
		try {
			return remoteCrisisTypeEJB.addCrisisType(crisisType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		//em.persist(crisisType);
		//return crisisType;
	}

	public CrisisTypeDTO getCrisisTypeByID(Long id) {
		try {
			return remoteCrisisTypeEJB.findCrisisTypeByID(id);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		/*
		Query query = em.createNamedQuery("CrisisType.findByCrisisTypeID", CrisisType.class);
		query.setParameter("crisisTypeID", id);
		return (CrisisType) query.getSingleResult();
		*/
	}

	public CrisisTypeDTO editCrisisType(CrisisTypeDTO crisis) {
		try {
			return remoteCrisisTypeEJB.editCrisisType(crisis);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		//CrisisType newCrisisType = em.merge(crisis);
		//return newCrisisType;
	}

	public void deleteCrisisType(Long id) {
		remoteCrisisTypeEJB.deleteCrisisType(id);
		
		/*
		CrisisType crisisType = em.find(CrisisType.class, id);
		if (crisisType != null) {
			em.remove(crisisType);
		}
		else{
			throw new RuntimeException();
		}*/
	}
}
