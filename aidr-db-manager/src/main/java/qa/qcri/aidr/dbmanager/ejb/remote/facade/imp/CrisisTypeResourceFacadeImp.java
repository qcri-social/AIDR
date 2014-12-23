/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    public CrisisTypeDTO addCrisisType(CrisisTypeDTO crisisType) throws PropertyNotSetException {
        em.persist(crisisType.toEntity());
        return findByCriteria("name", crisisType.getName()).get(0);
    }

    @Override
    public CrisisTypeDTO editCrisisType(CrisisTypeDTO crisisType) throws PropertyNotSetException {
        CrisisType newCrisisType = em.merge(crisisType.toEntity());
        return new CrisisTypeDTO(newCrisisType);
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
        if (list != null) {
            for (CrisisType c : list) {
                dtoList.add(new CrisisTypeDTO(c));
            }
        }
        return dtoList;
    }

    @Override
    public CrisisTypeDTO findCrisisTypeByID(Long id) throws PropertyNotSetException {
        CrisisType c = getById(id);
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
