/**
 *
 * Implements operations for managing the crisis_type table of the aidr_predict DB
 * 
 * @author Koushik
 */
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisTypeResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.misc.CrisisType;

@Stateless(name = "CrisisTypeResourceFacadeImp")
public class CrisisTypeResourceFacadeImp extends CoreDBServiceFacadeImp<CrisisType, Long> implements CrisisTypeResourceFacade {

	private Logger logger = Logger.getLogger("db-manager-log");

	public CrisisTypeResourceFacadeImp() {
		super(CrisisType.class);
	}

	@Override
	public List<CrisisTypeDTO> getAllCrisisTypes() throws PropertyNotSetException {
		List<CrisisTypeDTO> crisisTypeDTOList = new ArrayList<CrisisTypeDTO>();
		List<CrisisType> crisisTypeList = getAll();
		for (CrisisType cType : crisisTypeList) {
			Hibernate.initialize(cType.getCrisises());
			crisisTypeDTOList.add(new CrisisTypeDTO(cType));
		}
		return crisisTypeDTOList;
	}

	@Override
	public CrisisTypeDTO addCrisisType(CrisisTypeDTO crisisType) {
		try {
			CrisisType cType = crisisType.toEntity();
			em.persist(cType);
			em.flush();
			em.refresh(cType);
			return new CrisisTypeDTO(cType);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public CrisisTypeDTO editCrisisType(CrisisTypeDTO crisisType) throws PropertyNotSetException {
		System.out.println("Received edit request for: " + crisisType.getName() + ", " + crisisType.getCrisisTypeId());
		try {
			CrisisType cType = getById(crisisType.getCrisisTypeId());
			if (cType != null) {
				cType = em.merge(crisisType.toEntity());
				em.flush();
				em.refresh(cType);
				System.out.println("Updated crisisType: " + cType.getName() + ", " + cType.getCrisisTypeId());
				return cType != null ? new CrisisTypeDTO(cType) : null;
			} else {
				throw new RuntimeException("Not found");
			}
		} catch (Exception e) {
			System.out.println("Exception in merging/updating crisisType: " + crisisType.getCrisisTypeId());
			e.printStackTrace();	
		}
		return null;
	}

	@Override
	public Integer deleteCrisisType(Long id) {
		CrisisType crisisType = getById(id);
		if (crisisType != null) {
			this.delete(crisisType);
			return 1;
		} else {
			throw new RuntimeException("CrisisType requested to be deleted does not exist! id = " + id);
		}
	}

	@Override
	public List<CrisisTypeDTO> findByCriteria(String columnName, Object value) throws PropertyNotSetException {
		List<CrisisType> list = getAllByCriteria(Restrictions.eq(columnName, value));
		List<CrisisTypeDTO> dtoList = new ArrayList<CrisisTypeDTO>();
		if (list != null && !list.isEmpty()) {
			for (CrisisType c : list) {
				dtoList.add(new CrisisTypeDTO(c));
			}
		}
		return dtoList;
	}

	@Override
	public CrisisTypeDTO findCrisisTypeByID(Long id) throws PropertyNotSetException {
		CrisisType c = getById(id);
		if (c != null) {
			Hibernate.initialize(c.getCrisises());
		}
		return c != null ? new CrisisTypeDTO(c) : null;
	}

	@Override
	public boolean isCrisisTypeExists(Long id) throws PropertyNotSetException {
		CrisisType c = getById(id);
		return c != null ? true : false;
	}

	@Override
	public List<CrisisDTO> getAllCrisisForCrisisTypeID(Long id) throws PropertyNotSetException {
		List<CrisisDTO> dtoList = new ArrayList<CrisisDTO>();
		CrisisType crisisType = this.getById(id);
		if (crisisType != null) {
			Hibernate.initialize(crisisType.getCrisises());
			for (Crisis c : crisisType.getCrisises()) {
				dtoList.add(new CrisisDTO(c));
			}
		}
		return dtoList;
	}

}
