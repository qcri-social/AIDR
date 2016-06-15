/**
 * Implements operations for managing the model table of the aidr_predict DB
 * 
 * @author Koushik
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelHistoryWrapper;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelWrapper;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.CollectionResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.MiscResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Collection;
import qa.qcri.aidr.dbmanager.entities.model.Model;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.ModelNominalLabel;
import qa.qcri.aidr.util.NativeQueryUtil;


@Stateless(name="ModelResourceFacadeImp")
public class ModelResourceFacadeImp extends CoreDBServiceFacadeImp<Model, Long> implements ModelResourceFacade {

	private static Logger logger = Logger.getLogger("db-manager-log");

	@EJB
	private CollectionResourceFacade collectionResourceFacade;

	@EJB
	private MiscResourceFacade miscResourceFacade;
	
	public ModelResourceFacadeImp() {
		super(Model.class);
	}

	@Override
	public List<ModelDTO> getAllModels() throws PropertyNotSetException {
		List<ModelDTO> modelDTOList = new ArrayList<ModelDTO>();
		List<Model> modelList = getAll();
		logger.info("Fetched models list size: " + modelList.size());
		for (Model model : modelList) {
			modelDTOList.add(new ModelDTO(model));
		}
		return modelDTOList;
	}

	@Override
	public ModelDTO getModelByID(Long id) throws PropertyNotSetException {
		return new ModelDTO(getById(id));
	}

	/*
     Use this method to get number of models associated with a model family.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Integer getModelCountByModelFamilyID(Long modelFamilyID) throws PropertyNotSetException {
		Criteria criteria = getCurrentSession().createCriteria(Model.class);
		criteria.add(Restrictions.eq("modelFamily.modelFamilyId", modelFamilyID));
		List<Model> modelList = criteria.list();
		return modelList != null ? Integer.valueOf(modelList.size()) : 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ModelHistoryWrapper> getModelByModelFamilyID(Long modelFamilyID, Integer start, Integer limit) throws PropertyNotSetException {
		return getModelByModelFamilyID(modelFamilyID, start, limit, "trainingTime", "DESC");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ModelHistoryWrapper> getModelByModelFamilyID(Long modelFamilyID, Integer start, Integer limit, 
			String sortColumn, String sortDirection) throws PropertyNotSetException {
		List<ModelDTO> modelDTOList = new ArrayList<ModelDTO>();
		List<ModelHistoryWrapper> wrapperList = new ArrayList<ModelHistoryWrapper>();
		
		Criteria criteria = getCurrentSession().createCriteria(Model.class);
		criteria.add(Restrictions.eq("modelFamily.modelFamilyId", modelFamilyID));
	
		if(sortColumn.isEmpty()){
			sortColumn = "trainingTime";
		}
		if(sortDirection.isEmpty()){
			sortDirection = "DESC";
			criteria.addOrder(Order.desc(sortColumn));
		}
		else{
			if(sortDirection.equalsIgnoreCase("ASC")){
				criteria.addOrder(Order.asc(sortColumn));
			}
			else if (sortDirection.equalsIgnoreCase("DESC")){
				criteria.addOrder(Order.desc(sortColumn));
			}
		}
		
		criteria.setFirstResult(start);
		criteria.setMaxResults(limit);
		List<Model> modelList = criteria.list();
		for (Model model : modelList) {
			modelDTOList.add(new ModelDTO(model));
			ModelHistoryWrapper w = new ModelHistoryWrapper();
			w.setModelID(model.getModelId());
			w.setAvgAuc(model.getAvgAuc());
			w.setAvgPrecision(model.getAvgPrecision());
			w.setAvgRecall(model.getAvgRecall());
			w.setTrainingCount(model.getTrainingCount());
			w.setTrainingTime(model.getTrainingTime());
			wrapperList.add(w);
		}
		return wrapperList;
	}

	@Override
	public List<ModelWrapper> getModelByCrisisID(Long crisisID) throws PropertyNotSetException{
		List<ModelWrapper> modelWrapperList = new ArrayList<ModelWrapper>();

		Collection collection = getEntityManager().find(Collection.class, crisisID);
		getEntityManager().find(Collection.class, crisisID);
		Hibernate.initialize(collection.getModelFamilies());
		List<ModelFamily> modelFamilyList = collection.getModelFamilies();

		// for each modelFamily get all the models and take avg
		for (ModelFamily modelFamily : modelFamilyList) {
			Hibernate.initialize(modelFamily.getModels());
			List<Model> modelList = modelFamily.getModels();
			ModelWrapper modelWrapper = new ModelWrapper();
			modelWrapper.setModelFamilyID(modelFamily.getModelFamilyId());
			long classifiedElements = 0;
			double auc = 0.0;
			Long modelID = 0l;
			long trainingExamples = 0;

			// if size 0 we will get NaN for aucAverage
			if (modelList!=null && modelList.size() > 0) {
				for (Model model : modelList) {
					if (model.isIsCurrentModel()) {
						auc = model.getAvgAuc();
						modelID = model.getModelId();

						//for each model get all the labels and sum over classifiedDocumentCount
						Hibernate.initialize(model.getModelNominalLabels());
						long totalClassifiedDocuments = 0;
						for (ModelNominalLabel label : model.getModelNominalLabels()) {
							totalClassifiedDocuments += label.getClassifiedDocumentCount();
						}
						classifiedElements = totalClassifiedDocuments;
					}
				}
			}
			modelWrapper.setTrainingExamples(trainingExamples);
			modelWrapper.setAttribute(modelFamily.getNominalAttribute().getName());
			modelWrapper.setAttributeID(modelFamily.getNominalAttribute().getNominalAttributeId());
			modelWrapper.setAuc(auc);
			modelWrapper.setClassifiedDocuments(classifiedElements);
			String status = "";
			if (modelFamily.isIsActive()) {
				status = "Active";
			} else {
				status = "Inactive";
			}
			modelWrapper.setStatus(status);
			modelWrapper.setModelID(modelID);

			modelWrapperList.add(modelWrapper);
		}
		return modelWrapperList;
	}

	@Override
	public boolean deleteModel(Long modelID) {
		Model model = getEntityManager().find(Model.class, modelID);
		if (model != null) {
			try {
				getEntityManager().remove(model);
			} catch (HibernateException he) {
				logger.error("Hibernate exception on deleting Model using ID=" + model + he.getStackTrace());
				return false; // hibernate delete operation failed. Details in the logs.
			}
			return true; // successfully deleted.
		} else {
			return false; // delete operation failed becuase no modelfamily is found against given ID.
		}
	}
}
