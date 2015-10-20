/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.ModelNominalLabelDTO;
import qa.qcri.aidr.predictui.facade.ModelNominalLabelFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskManagerRemote;

//import qa.qcri.aidr.predictui.dto.ModelNominalLabelDTO;


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

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelNominalLabelResourceFacade remoteModelNominalLabelEJB;
	
	//@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	//private EntityManager em;

	public List<ModelNominalLabelDTO> getAllModelNominalLabels() {
		List<ModelNominalLabelDTO> modelNominalLabelList = remoteModelNominalLabelEJB.getAllModelNominalLabels();
		
		return modelNominalLabelList;
		
		/*
		Query query = em.createNamedQuery("ModelNominalLabel.findAll", ModelNominalLabel.class);
		try {
			List<ModelNominalLabel> modelNominalLabelList = query.getResultList();
			return modelNominalLabelList;
		} catch (NoResultException e) {
			return null;
		}
		*/

	}

	public List<ModelNominalLabelDTO> getAllModelNominalLabelsByModelID(Long modelID, String crisisCode) {
		List<ModelNominalLabelDTO> modelNominalLabelDTOList = remoteModelNominalLabelEJB.getAllModelNominalLabelsByModelID(modelID, crisisCode);
		return modelNominalLabelDTOList;
		
		/*
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
		*/
	}

	@Override
	public void deleteByModel(Long modelID) {
		remoteModelNominalLabelEJB.deleteModelNominalLabelByModelID(modelID);
	}
}
