/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.predictui.facade.*;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import qa.qcri.aidr.predictui.dto.ModelNominalLabelDTO;
import qa.qcri.aidr.predictui.entities.Document;
import qa.qcri.aidr.predictui.entities.Model;
import qa.qcri.aidr.predictui.entities.ModelNominalLabel;
import qa.qcri.aidr.predictui.entities.NominalLabel;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;

import java.util.Collection;
import java.util.ArrayList;

/**
 *
 * @author Imran
 * 
 * Koushik: added try/catch
 */
@Stateless
public class ModelNominalLabelImp implements ModelNominalLabelFacade {

	@EJB
	private TaskManagerRemote<DocumentDTO, Long> taskManager;

	@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	private EntityManager em;

	public List<ModelNominalLabel> getAllModelNominalLabels() {
		Query query = em.createNamedQuery("ModelNominalLabel.findAll", ModelNominalLabel.class);
		try {
			List<ModelNominalLabel> modelNominalLabelList = query.getResultList();
			return modelNominalLabelList;
		} catch (NoResultException e) {
			return null;
		}

	}

	public List<ModelNominalLabelDTO> getAllModelNominalLabelsByModelID(int modelID) {
		List<ModelNominalLabel> modelNominalLabelList = null;
		List<ModelNominalLabelDTO> modelNominalLabelDTOList = new ArrayList<ModelNominalLabelDTO>();
		Model model = em.find(Model.class, modelID);
		if (model != null) {
			Query query = em.createNamedQuery("ModelNominalLabel.findByModel", ModelNominalLabel.class);
			query.setParameter("model", model);
			try {
				modelNominalLabelList = query.getResultList();
			} catch (NoResultException e) {
				return null;
			}
			//if (modelNominalLabelList.isEmpty()){
			//    return null;
			//}
			Boolean modelStatus = model.getModelFamily().getIsActive();
			Integer nominalAttributeId = model.getModelFamily().getNominalAttribute().getNominalAttributeID();
			for (ModelNominalLabel labelEntity : modelNominalLabelList) {

				//Getting training examples for each label
				int trainingSet = 0;
				NominalLabel nominalLabel = labelEntity.getNominalLabel();
				//Collection<Document> docList = nominalLabel.getDocumentCollection();
				
				Collection<Document> docList = null;
				if (nominalLabel != null && !nominalLabel.getNominalLabelCode().equalsIgnoreCase("null")) {
					try {
					List<DocumentDTO> dtoList = taskManager.getNominalLabelDocumentCollection(new Long(nominalLabel.getNominalLabelID()));
						docList = Document.toLocalDocumentList(dtoList);
						for (Document document : docList) {
							if (!(document.getIsEvaluationSet())) {
								trainingSet++;
							}
						}
					} catch (PropertyNotSetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
				mnlDTO.setNominalAttributeId(nominalAttributeId);

				modelNominalLabelDTOList.add(mnlDTO);
			}

		}
		return modelNominalLabelDTOList;
	}
}
