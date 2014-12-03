/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.dto.ModelDTOWrapper;
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
public class ModelResourceFacadeImp extends CoreDBServiceFacadeImp<Model, Long> implements ModelResourceFacade {

    public List<ModelDTO> getAllModels() throws PropertyNotSetException {
        List<ModelDTO> modelDTOList = new ArrayList<ModelDTO>();
        List<Model> modelList = getAll();
        if (modelList != null) {
            for (Model model : modelList) {
                modelDTOList.add(new ModelDTO(model));
            }
        }
        return modelDTOList;
    }

    public ModelDTO getModelByID(Long id) throws PropertyNotSetException {
        return new ModelDTO(getById(id));
    }

    /*
     Use this method to get number of models associated with a model family.
     */
    public Integer getModelCountByModelFamilyID(Long modelFamilyID) throws PropertyNotSetException {
        Criteria criteria = getCurrentSession().createCriteria(Model.class);
        criteria.add(Restrictions.eq("modelFamilyID", modelFamilyID));
        List<Model> modelList = criteria.list();
        return modelList != null ? Integer.valueOf(modelList.size()) : 0;
    }

    public List<ModelDTO> getModelByModelFamilyID(Integer modelFamilyID, Integer start, Integer limit) throws PropertyNotSetException {
        List<ModelDTO> modelDTOList = new ArrayList<ModelDTO>();
        Criteria criteria = getCurrentSession().createCriteria(Model.class);
        criteria.add(Restrictions.eq("modelFamilyID", modelFamilyID));
        List<Model> modelList = criteria.list();
        if (modelList != null) {
            for (Model model : modelList) {
                modelDTOList.add(new ModelDTO(model));
            }
        }
        return modelDTOList;
    }

    public List<ModelDTOWrapper> getModelByCrisisID(Long crisisID) {
        List<ModelDTOWrapper> modelDTOWrapperList = new ArrayList<ModelDTOWrapper>();

        Crisis crisis = getEntityManager().find(Crisis.class, crisisID);
        List<ModelFamily> modelFamilyList = crisis.getModelFamilies();

        // for each modelFamily get all the models and take avg
        for (ModelFamily modelFamily : modelFamilyList) {
            List<Model> modelList = modelFamily.getModels();
            ModelDTOWrapper modelDTOWrapper = new ModelDTOWrapper();
            modelDTOWrapper.setModelFamilyID(modelFamily.getModelFamilyId());
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

            modelDTOWrapper.setTrainingExamples(trainingExamples);
            modelDTOWrapper.setAttribute(modelFamily.getNominalAttribute().getName());
            modelDTOWrapper.setAttributeID(modelFamily.getNominalAttribute().getNominalAttributeId());
            modelDTOWrapper.setAuc(auc);
            modelDTOWrapper.setClassifiedDocuments(classigiedElements);
            String status = "";
            if (modelFamily.isIsActive()) {
                status = "Active";
            } else {
                status = "Inactive";
            }
            modelDTOWrapper.setStatus(status);
            modelDTOWrapper.setModelID(modelID);

            modelDTOWrapperList.add(modelDTOWrapper);
        }
        return modelDTOWrapperList;
    }

}
