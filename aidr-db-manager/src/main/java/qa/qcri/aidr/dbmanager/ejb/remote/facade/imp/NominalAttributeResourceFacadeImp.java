/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.NominalAttributeResourceFacade;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;
import qa.qcri.aidr.dbmanager.entities.task.Document;

/**
 *
 * @author Imran
 */
@Stateless(name = "NominalAttributeResourceFacadeImp")
public class NominalAttributeResourceFacadeImp extends CoreDBServiceFacadeImp<NominalAttribute, Long> implements NominalAttributeResourceFacade {

    private static Logger logger = Logger.getLogger("db-manager-log");

    public NominalAttributeResourceFacadeImp() {
		super(NominalAttribute.class);
	}
    
    public boolean addAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException {
        try {
            save(attribute.toEntity());
        } catch (HibernateException he) {
            logger.error("Hibernate exception on save Nominal Attribute. \n" + he.getStackTrace());
            return false;
        }
        return true;
    }

    public NominalAttributeDTO editAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException {
        if (attribute == null) {
            throw new RuntimeException("Missing data values");
        }
        NominalAttribute nominalAttributeDB = getEntityManager().find(NominalAttribute.class, attribute.getNominalAttributeId());
        if (nominalAttributeDB != null) {
            getEntityManager().merge(attribute);
            return attribute;
        } else {
            throw new RuntimeException("Can't edit");
        }
    }

    public boolean deleteAttribute(Long attributeID) throws PropertyNotSetException {
        NominalAttribute attribute = getEntityManager().find(NominalAttribute.class, attributeID);
        if (attribute != null) {
            try {
                getEntityManager().remove(attribute);
            } catch (HibernateException he) {
                logger.error("Error occured while removing Attribute with ID = " + attributeID + "\n" + he.getStackTrace());
                return false; // exception occured
            }
            return true; //successfully deleted
        } else {
            return false; // attribute does not exist with the provided id.
        }
    }

    public NominalAttributeDTO getAttributeByID(Long attributeID) throws PropertyNotSetException {
        NominalAttribute nominalAttribute = getById(attributeID);
        nominalAttribute.getNominalLabels(); //loading labels too
        return new NominalAttributeDTO(nominalAttribute);
    }

    public List<NominalAttributeDTO> getAllAttributes() throws PropertyNotSetException {
        List<NominalAttributeDTO> nominalAttributeDTOList = new ArrayList<NominalAttributeDTO>();
        List<NominalAttribute> nominalAttributeList = getAll();
        for (NominalAttribute nominalAttribute : nominalAttributeList) {
            nominalAttributeDTOList.add(new NominalAttributeDTO(nominalAttribute));
        }
        return nominalAttributeDTOList;
    }

    public Long isAttributeExists(String attributeCode) throws PropertyNotSetException {
        Criteria criteria = getCurrentSession().createCriteria(NominalAttribute.class);
        criteria.add(Restrictions.eq("code", attributeCode));
        NominalAttribute nominalAttribute = (NominalAttribute)criteria.uniqueResult();
        return nominalAttribute != null ? nominalAttribute.getNominalAttributeId() : null;
    }

}
