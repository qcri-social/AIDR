/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.predictui.facade.*;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.predictui.dto.ModelNominalLabelDTO;
import qa.qcri.aidr.predictui.entities.Document;
import qa.qcri.aidr.predictui.entities.Model;
import qa.qcri.aidr.predictui.entities.ModelNominalLabel;
import qa.qcri.aidr.predictui.entities.NominalLabel;
import java.util.Collection;
import java.util.ArrayList;

/**
 *
 * @author Imran
 */
@Stateless
public class ModelNominalLabelImp implements ModelNominalLabelFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    public List<ModelNominalLabel> getAllModelNominalLabels() {
        Query query = em.createNamedQuery("ModelNominalLabel.findAll", ModelNominalLabel.class);
        List<ModelNominalLabel> modelNominalLabelList = query.getResultList();
        return modelNominalLabelList;

    }

    public List<ModelNominalLabelDTO> getAllModelNominalLabelsByModelID(int modelID) {
        List<ModelNominalLabel> modelNominalLabelList = null;
        List<ModelNominalLabelDTO> modelNominalLabelDTOList = new ArrayList<ModelNominalLabelDTO>();
        Model model = em.find(Model.class, modelID);
        if (model != null) {
            Query query = em.createNamedQuery("ModelNominalLabel.findByModel", ModelNominalLabel.class);
            query.setParameter("model", model);
            modelNominalLabelList = query.getResultList();
            if (modelNominalLabelList.isEmpty()){
                return null;
            }
            Boolean modelStatus = model.getModelFamily().getIsActive();
            for (ModelNominalLabel labelEntity : modelNominalLabelList) {

                //Getting training examples for each label
                int trainingSet = 0;
                    NominalLabel nominalLabel = labelEntity.getNominalLabel();
                    Collection<Document> docList = nominalLabel.getDocumentCollection();
                    for (Document document : docList) {
                        if (!(document.getIsEvaluationSet())) {
                            trainingSet++;
                        }
                    }
                // Deep copying modelNominalLabel to ModelNominalLabelDTO
                ModelNominalLabelDTO mnlDTO = new ModelNominalLabelDTO();
                mnlDTO.setClassifiedDocumentCount(labelEntity.getClassifiedDocumentCount());
                mnlDTO.setLabelAuc(labelEntity.getLabelAuc());
                mnlDTO.setLabelPrecision(labelEntity.getLabelPrecision());
                mnlDTO.setLabelRecall(labelEntity.getLabelRecall());
                mnlDTO.setModel(labelEntity.getModel());
                mnlDTO.setModelNominalLabelPK(labelEntity.getModelNominalLabelPK());
                mnlDTO.setNominalLabel(labelEntity.getNominalLabel());
                mnlDTO.setTrainingDocuments(trainingSet);
                mnlDTO.setModelStatus(modelStatus==true ? "RUNNING" : "NOT RUNNING");
                
                modelNominalLabelDTOList.add(mnlDTO);
            }

        }
        return modelNominalLabelDTOList;
    }
}
