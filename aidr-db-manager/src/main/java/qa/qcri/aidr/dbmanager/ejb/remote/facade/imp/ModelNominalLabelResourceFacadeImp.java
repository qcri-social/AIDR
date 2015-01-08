package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.ModelNominalLabelDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelNominalLabelResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelResourceFacade;
import qa.qcri.aidr.dbmanager.entities.model.Model;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.ModelNominalLabel;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;

@Stateless(name = "ModelNominalLabelResourceFacadeImp")
public class ModelNominalLabelResourceFacadeImp extends CoreDBServiceFacadeImp<ModelNominalLabel, Long> implements ModelNominalLabelResourceFacade {

	private Logger logger = Logger.getLogger("db-manager-log");

	@EJB
	private ModelResourceFacade modelEJB;

	@EJB
	private DocumentResourceFacade documentEJB;

	protected ModelNominalLabelResourceFacadeImp() {
		super(ModelNominalLabel.class);
	}

	@Override
	public List<ModelNominalLabelDTO> getAllModelNominalLabels() {
		List<ModelNominalLabelDTO> dtoList = new ArrayList<ModelNominalLabelDTO>();
		try {
			List<ModelNominalLabel> list = this.getAll();
			if (list != null) {
				System.out.println("Fetched list size: " + list.size());
				for (ModelNominalLabel m : list) {
					dtoList.add(new ModelNominalLabelDTO(m));
				}
			}
			return dtoList;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<ModelNominalLabelDTO> getAllModelNominalLabelsByModelID(Long modelID) {
		List<ModelNominalLabelDTO> dtoList = new ArrayList<ModelNominalLabelDTO>();;
		List<ModelNominalLabel> modelNominalLabelList = new ArrayList<ModelNominalLabel>();

		Model model = modelEJB.getById(modelID);
		if (model != null) {
			System.out.println("Model is not NULL in getAllModelNominalLabelsByModelID for modelID = "  + modelID);
			Hibernate.initialize(model.getModelFamily());

			try {
				Boolean modelStatus = model.getModelFamily().isIsActive(); //getting model status
				ModelFamily modelFamily = model.getModelFamily();
				System.out.println("*********** retrived modelFamilyID = " + modelFamily.getModelFamilyId());
				Hibernate.initialize(modelFamily.getNominalAttribute());
				Long nominalAttributeId = modelFamily.getNominalAttribute().getNominalAttributeId();
				//Long nominalAttributeId = model.getModelFamily().getNominalAttribute().getNominalAttributeId();

				modelNominalLabelList = this.getAllByCriteria(Restrictions.eq("id.modelId", modelID));
				System.out.println("modelNominalLabellist size = " + modelNominalLabelList.size());

				for (ModelNominalLabel labelEntity : modelNominalLabelList) {

					//Getting training examples for each label
					int trainingSet = 0;
					NominalLabel nominalLabel = labelEntity.getNominalLabel();

					if (nominalLabel != null && !nominalLabel.getNominalLabelCode().equalsIgnoreCase("null")) {
						try {
							List<DocumentDTO> docList = documentEJB.getDocumentCollectionWithNominalLabelData(nominalLabel.getNominalLabelId());
							for (DocumentDTO document : docList) {
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
					Hibernate.initialize(labelEntity.getModel());
					Hibernate.initialize(labelEntity.getNominalLabel());

					ModelNominalLabelDTO dto = new ModelNominalLabelDTO(labelEntity);
					dto.setModelStatus(modelStatus == true ? "RUNNING" : "NOT RUNNING");
					dto.setNominalAttributeId(nominalAttributeId);
					dto.setTrainingDocuments(trainingSet);
					dtoList.add(dto);
				}
			} catch (Exception e) {
				System.out.println("Exception occured in getAllModelNominalLabelsByModelID \n\n" );
				e.printStackTrace();
				return null;
			}
		}
		return dtoList;
	}

	@Override
	public void saveModelNominalLabel(ModelNominalLabelDTO modelNominalLabel) throws PropertyNotSetException {
		save(modelNominalLabel.toEntity());
	}

	@Override
	public ModelNominalLabelDTO addModelNominalLabel(ModelNominalLabelDTO modelNominalLabel)  {
		try {
			ModelNominalLabel modelLabel = modelNominalLabel.toEntity();
			em.persist(modelLabel);
			em.flush();
			em.refresh(modelLabel);
			return new ModelNominalLabelDTO(modelLabel); 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ModelNominalLabelDTO editModelNominalLabel(ModelNominalLabelDTO modelNominalLabel) throws PropertyNotSetException {
		System.out.println("Received request for: " + modelNominalLabel.getIdDTO().getNominalLabelId() + ":" + modelNominalLabel.getIdDTO().getModelId());
		try {
			ModelNominalLabel label = modelNominalLabel.toEntity();
			ModelNominalLabel oldLabel = getByCriteria(Restrictions.eq("id", modelNominalLabel.getIdDTO().toEntity()));
			if (oldLabel != null) {
				oldLabel = em.merge(label);
				return (oldLabel != null) ? new ModelNominalLabelDTO(oldLabel) : null;
			} else {
				throw new RuntimeException("Not found");
			}
		} catch (Exception e) {
			System.out.println("Exception in merging/updating nominalLabel: " + modelNominalLabel.getIdDTO().getNominalLabelId());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Integer deleteModelNominalLabel(ModelNominalLabelDTO modelNominalLabel) throws PropertyNotSetException {
		if (modelNominalLabel != null) {
			em.remove(modelNominalLabel.toEntity());
			return 1;
		}
		return 0;
	}

	@Override
	public ModelNominalLabelDTO getModelNominalLabelByID(Long nominalLabelID) throws PropertyNotSetException {
		ModelNominalLabel nb = this.getByCriteria(Restrictions.eq("id.nominalLabelId", nominalLabelID));
		return nb != null ? new ModelNominalLabelDTO(nb) : null;
	}

	@Override
	public Boolean isModelNominalLabelExists(Long nominalLabelID) throws PropertyNotSetException {
		return (getModelNominalLabelByID(nominalLabelID) != null) ? true : false;
	}

}
