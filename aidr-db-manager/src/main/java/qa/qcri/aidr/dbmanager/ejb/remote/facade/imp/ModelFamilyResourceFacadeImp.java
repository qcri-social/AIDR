/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TaggersForCodes;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFamilyResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;


/**
 *
 * @author Imran
 */
@Stateless(name="ModelFamilyResourceFacadeImp")
public class ModelFamilyResourceFacadeImp extends CoreDBServiceFacadeImp<ModelFamily, Long> implements ModelFamilyResourceFacade {

    private static Logger logger = Logger.getLogger("db-manager-log");

    public ModelFamilyResourceFacadeImp() {
		super(ModelFamily.class);
	}
    
    public List<ModelFamilyDTO> getAllModelFamilies() throws PropertyNotSetException {
        List<ModelFamilyDTO> modelFamilyDTOList = new ArrayList<ModelFamilyDTO>();
        List<ModelFamily> modelFamilyList = getAll();
        for (ModelFamily modelFamily : modelFamilyList) {
            modelFamilyDTOList.add(new ModelFamilyDTO(modelFamily));
        }
        return modelFamilyDTOList; //returns empty list if no data is found in the database
    }

    public List<ModelFamilyDTO> getAllModelFamiliesByCrisis(Long crisisID) throws PropertyNotSetException {
        List<ModelFamilyDTO> modelFamilyDTOList = new ArrayList<ModelFamilyDTO>();
        Crisis crisis = getEntityManager().find(Crisis.class, crisisID);
        List<ModelFamily> modelFamilyList = crisis.getModelFamilies();
        for (ModelFamily modelFamily : modelFamilyList) {
            Hibernate.initialize(modelFamily.getModels());
            Hibernate.initialize(modelFamily.getNominalAttribute());
        	modelFamilyDTOList.add(new ModelFamilyDTO(modelFamily));
        }
        return modelFamilyDTOList; //returns empty list if no data is found in the database
    }

    public ModelFamilyDTO getModelFamilyByID(Long id) throws PropertyNotSetException {
        return new ModelFamilyDTO(getById(id));
    }

    public boolean addCrisisAttribute(ModelFamilyDTO modelFamily) throws PropertyNotSetException {
        try {
            getEntityManager().persist(modelFamily.toEntity());
        } catch (HibernateException he) {
            logger.error("Hibernate exception on adding ModelFamily.\n" + he.getStackTrace());
            return false;
        }
        return true;
    }

    public boolean deleteModelFamily(Long modelFamilyID) throws PropertyNotSetException {
        ModelFamily modelFamily = getEntityManager().find(ModelFamily.class, modelFamilyID);
        if (modelFamily != null) {
            try {
                getEntityManager().remove(modelFamily);
            } catch (HibernateException he) {
                logger.error("Hibernate exception on deleting ModelFamily using ID=" + modelFamilyID + he.getStackTrace());
                return false; // hibernate delete operation failed. Details in the logs.
            }
            return true; // successfully deleted.
        } else {
            return false; // delete operation failed becuase no modelfamily is found against given ID.
        }
    }

	public List<TaggersForCodes> getTaggersByCodes(final List<String> codes) {
		List<qa.qcri.aidr.dbmanager.dto.taggerapi.TaggersForCodes> result = new ArrayList<TaggersForCodes>();

		String sql = "select c.code as code, " +
				" count(mf.modelFamilyID) as modelsCount " +
				" from model_family mf " +
				" right outer join crisis c on c.crisisID = mf.crisisID " +
				" where c.code in :codes " +
				" group by mf.crisisID ";

		Query query = em.createNativeQuery(sql);
		query.setParameter("codes", codes);
		List<Object[]> rows = null;
		try {
			rows = query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
		for (Object[] row : rows) {
			TaggersForCodes taggersForCodes = new TaggersForCodes();
			taggersForCodes.setCode((String) row[0]);
			taggersForCodes.setCount((BigInteger) row[1]);
			result.add(taggersForCodes);
		}

		return result;
	}
}
