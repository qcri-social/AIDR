package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CrisisAttributesDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.NominalAttributeResourceFacade;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;

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

	public NominalAttributeDTO addAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException {
		try {
			NominalAttribute e = attribute.toEntity();
			NominalAttribute savedEntity = save(e);
			if (savedEntity != null) {
				return new NominalAttributeDTO(savedEntity);
			} else {
				return null;
			}
		} catch (HibernateException he) {
			logger.error("Hibernate exception on save Nominal Attribute. \n" + he.getStackTrace());
			return null;
		}
	}

	public NominalAttributeDTO editAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException {
		try {
			if (attribute == null) {
				throw new RuntimeException("Missing data values");
			}
			NominalAttribute nominalAttributeDB = getEntityManager().find(NominalAttribute.class, attribute.getNominalAttributeId());
			if (nominalAttributeDB != null) {
				NominalAttribute mergedEntity = (NominalAttribute) merge(attribute.toEntity());
				return new NominalAttributeDTO(mergedEntity);
			} else {
				return null;
			}
		} catch (HibernateException he) {
			logger.error("Hibernate exception on editing Nominal Attribute. \n" + he.getStackTrace());
			return null;
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
		Hibernate.initialize(nominalAttribute.getNominalLabels()); //loading labels too
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

	//TODO: Native query used in this method should be translated into a criteria query.
	@SuppressWarnings("unchecked")
	public List<CrisisAttributesDTO> getAllAttributesExceptCrisis(Long crisisID) throws PropertyNotSetException {
		List<CrisisAttributesDTO> attributesList = new ArrayList<>();
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
			return attributesList;
		}
	}

}
