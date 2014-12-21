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
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelDTOWrapper;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelHistoryWrapper;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelWrapper;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.model.Model;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.ModelNominalLabel;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;

/**
 *
 * @author Imran
 */
@Stateless(name="ModelResourceFacadeImp")
public class ModelResourceFacadeImp extends CoreDBServiceFacadeImp<Model, Long> implements ModelResourceFacade {

	private static Logger logger = Logger.getLogger("db-manager-log");

	public ModelResourceFacadeImp() {
		super(Model.class);
	}

	public List<ModelDTO> getAllModels() throws PropertyNotSetException {
		List<ModelDTO> modelDTOList = new ArrayList<ModelDTO>();
		List<Model> modelList = getAll();
		System.out.println("Fetched models list size: " + modelList.size());
		for (Model model : modelList) {
			modelDTOList.add(new ModelDTO(model));
		}
		return modelDTOList;
	}

	public ModelDTO getModelByID(Long id) throws PropertyNotSetException {
		return new ModelDTO(getById(id));
	}

	/*
     Use this method to get number of models associated with a model family.
	 */
	@SuppressWarnings("unchecked")
	public Integer getModelCountByModelFamilyID(Long modelFamilyID) throws PropertyNotSetException {
		Criteria criteria = getCurrentSession().createCriteria(Model.class);
		criteria.add(Restrictions.eq("modelFamily.modelFamilyId", modelFamilyID));
		List<Model> modelList = criteria.list();
		return modelList != null ? Integer.valueOf(modelList.size()) : 0;
	}

	@SuppressWarnings("unchecked")
	public List<ModelHistoryWrapper> getModelByModelFamilyID(Long modelFamilyID, Integer start, Integer limit) throws PropertyNotSetException {
		List<ModelDTO> modelDTOList = new ArrayList<ModelDTO>();
		List<ModelHistoryWrapper> wrapperList = new ArrayList<ModelHistoryWrapper>();
		Criteria criteria = getCurrentSession().createCriteria(Model.class);
		criteria.add(Restrictions.eq("modelFamily.modelFamilyId", modelFamilyID));
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

	public List<ModelWrapper> getModelByCrisisID(Long crisisID) throws PropertyNotSetException{
		List<ModelWrapper> modelWrapperList = new ArrayList<ModelWrapper>();

		Crisis crisis = getEntityManager().find(Crisis.class, crisisID);
		List<ModelFamily> modelFamilyList = crisis.getModelFamilies();

		// for each modelFamily get all the models and take avg
		for (ModelFamily modelFamily : modelFamilyList) {
			List<Model> modelList = modelFamily.getModels();
			ModelWrapper modelWrapper = new ModelWrapper();
			modelWrapper.setModelFamilyID(modelFamily.getModelFamilyId());
			long classigiedElements = 0;
			double auc = 0.0;
			Long modelID = 0l;
			long trainingExamples = 0;

			// if size 0 we will get NaN for aucAverage
			if (modelList.size() > 0) {
				for (Model model : modelList) {
					if (model.isIsCurrentModel()) {
						auc = model.getAvgAuc();
						modelID = model.getModelId();

						//for each model get all the labels and sum over classifiedDocumentCount
						List<ModelNominalLabel> modelLabels = model.getModelNominalLabels();// .getModelNominalLabelCollection();
						long totalClassifiedDocuments = 0;
						for (ModelNominalLabel label : modelLabels) {
							totalClassifiedDocuments += label.getClassifiedDocumentCount();
						}
						classigiedElements = totalClassifiedDocuments;
					}
				}
			}
			//getting trainingCount
			trainingExamples = 0;
			NominalAttribute nominalAttribute = modelFamily.getNominalAttribute();
			List<NominalLabel> nominalLabelList = nominalAttribute.getNominalLabels();//.getNominalLabelCollection();
			for (NominalLabel label : nominalLabelList) {
				if (!(label.getNominalLabelCode().equalsIgnoreCase("null"))) {
					//Collection<Document> dc = label.getDocumentCollection();
					List<DocumentNominalLabel> documentNominalList = label.getDocumentNominalLabels();
					for (DocumentNominalLabel docNominalLabel : documentNominalList) {
						Document doc = docNominalLabel.getDocument();
						if (!doc.isIsEvaluationSet() && doc.isHasHumanLabels()) {
							trainingExamples++;
						}
					}
				}
			}
			modelWrapper.setTrainingExamples(trainingExamples);
			modelWrapper.setAttribute(modelFamily.getNominalAttribute().getName());
			modelWrapper.setAttributeID(modelFamily.getNominalAttribute().getNominalAttributeId());
			modelWrapper.setAuc(auc);
			modelWrapper.setClassifiedDocuments(classigiedElements);
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

}
