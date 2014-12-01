/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;

import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisTypeResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.CrisisType;

@Stateless
public class CrisisTypeResourceFacadeImp extends CoreDBServiceFacadeImp<CrisisType, Long> implements CrisisTypeResourceFacade {

	private Logger logger = Logger.getLogger("db-manager-log");

	@Override
	public List<CrisisTypeDTO> getCrisisTypes() throws PropertyNotSetException {
		List<CrisisTypeDTO> crisisTypeDTOList = new ArrayList<CrisisTypeDTO>();
		List<CrisisType> crisisTypeList = getAll();
		for (CrisisType cType: crisisTypeList) {
			crisisTypeDTOList.add(new CrisisTypeDTO(cType));
		}
		return crisisTypeDTOList;
	}

	@Override
	public CrisisTypeDTO addCrisisType(CrisisTypeDTO crisisType) throws PropertyNotSetException {
		em.persist(crisisType.toEntity());
		return crisisType;
	}

	@Override
	public CrisisTypeDTO getCrisisTypeByID(Long id) throws PropertyNotSetException {
		CrisisType cType = getById(id);
		if (cType != null) {
			return new CrisisTypeDTO(cType);
		}
		return null;
	}

	@Override
	public CrisisTypeDTO editCrisisType(CrisisTypeDTO crisisType) throws PropertyNotSetException {
		CrisisType newCrisisType = em.merge(crisisType.toEntity());
		return new CrisisTypeDTO(newCrisisType);
	}

	@Override
	public void deleteCrisisType(Long id) {
		CrisisType crisisType = getById(id);
		if (crisisType != null) {
			this.delete(crisisType);
		}
		else {
			throw new RuntimeException("Crisis requested to be deleted does not exist! id = " + id);
		}
	}
}
