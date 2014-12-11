// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.dbmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.model.Model;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.ModelNominalLabel;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 436159208494993271L;

	@XmlElement
	private Long modelId;

	@XmlElement
	private Long modelFamilyId;

	@XmlElement
	private double avgPrecision;

	@XmlElement
	private double avgRecall;

	@XmlElement
	private double avgAuc;

	@XmlElement
	private int trainingCount;

	@XmlElement
	private Date trainingTime;

	@XmlElement
	private boolean isCurrentModel;

	@XmlElement
	private List<ModelNominalLabelDTO> modelNominalLabelsDTO = null;

	@XmlElement
	private ModelFamilyDTO modelFamilyDTO;

	public ModelDTO() {
	}

	public ModelDTO(Model model) throws PropertyNotSetException {
		setModelId(model.getModelId());
		if (model.hasModelFamily()) {
			ModelFamily mf = new ModelFamily(model.getModelFamily().getNominalAttribute(), model.getModelFamily().getCrisis(),
					model.getModelFamily().isIsActive());
			mf.setModelFamilyId(model.getModelFamily().getModelFamilyId());
			this.setModelFamilyDTO(new ModelFamilyDTO(mf));
		}
		setAvgPrecision(model.getAvgPrecision());
		setAvgRecall(model.getAvgRecall());
		setAvgAuc(model.getAvgAuc());
		setTrainingCount(model.getTrainingCount());
		setTrainingTime(model.getTrainingTime());
		setIsCurrentModel(model.isIsCurrentModel());
		if (model.hasModelNominalLabel()) {
			setModelNominalLabelsDTO(this.toModelNominalLabelDTOList(model.getModelNominalLabels()));
		}
	}

	public ModelDTO(ModelFamilyDTO modelFamilyDTO, double avgPrecision, double avgRecall,
			double avgAuc, int trainingCount, Date trainingTime,
			boolean isCurrentModel) {
		setModelFamilyDTO(modelFamilyDTO);
		setAvgPrecision(avgPrecision);
		setAvgRecall(avgRecall);
		setAvgAuc(avgAuc);
		setTrainingCount(trainingCount);
		setTrainingTime(trainingTime);
		setIsCurrentModel(isCurrentModel);
	}

	public Model toEntity() throws PropertyNotSetException {
		Model model = new Model();
		if (this.getModelId() != null) {
			model.setModelId(this.getModelId());
		}
		if (this.getModelFamilyDTO() != null) {
			model.setModelFamily(getModelFamilyDTO().toEntity());
		}
		model.setAvgPrecision(getAvgPrecision());
		model.setAvgRecall(getAvgRecall());
		model.setAvgAuc(getAvgAuc());
		model.setTrainingCount(getTrainingCount());
		model.setTrainingTime(getTrainingTime());
		model.setIsCurrentModel(isIsCurrentModel());

		if (this.modelNominalLabelsDTO != null) {
			model.setModelNominalLabels(this.toModelNominalLabelList(this.getModelNominalLabelsDTO()));
		}
		return model;
	}

	public Long getModelId() throws PropertyNotSetException {
		if (this.modelId != null) {
			return this.modelId;
		} else {
			throw new PropertyNotSetException();
		}
	}

	public void setModelId(Long modelId) {
		if (modelId == null) {
			throw new IllegalArgumentException("modelID cannot be null");
		} else if (modelId.longValue() <= 0) {
			throw new IllegalArgumentException("modelID cannot be zero or a negative number");
		} else {
			this.modelId = modelId;
		}
	}

	public Long getModelFamilyId() {
		return this.modelFamilyId;
	}

	public void setModelFamilyId(Long modelFamilyId) {
		if (modelFamilyId == null) {
			throw new IllegalArgumentException("modelFamilyID cannot be null");
		} else if (modelFamilyId <= 0) {
			throw new IllegalArgumentException("modelFamilyID cannot be zero or a negative number");
		} else {
			this.modelFamilyId = modelFamilyId;
		}

	}

	public double getAvgPrecision() {
		return this.avgPrecision;
	}

	public void setAvgPrecision(Double avgPrecision) {
		if (avgPrecision == null) {
			throw new IllegalArgumentException("Average percision cannot be null");
		} else if (avgPrecision.doubleValue() < 0) {
			throw new IllegalArgumentException("Average percision cannot be a negative number");
		} else {
			this.avgPrecision = avgPrecision;
		}

	}

	public double getAvgRecall() {
		return this.avgRecall;
	}

	public void setAvgRecall(Double avgRecall) {
		if (avgRecall == null) {
			throw new IllegalArgumentException("Average recall cannot be null");
		} else if (avgRecall.doubleValue() < 0) {
			throw new IllegalArgumentException("Average recall cannot be a negative number");
		} else {
			this.avgRecall = avgRecall;
		}
	}

	public double getAvgAuc() {
		return this.avgAuc;
	}

	public void setAvgAuc(Double avgAuc) {
		if (avgAuc == null) {
			throw new IllegalArgumentException("Average AUC cannot be null");
		} else if (avgAuc.doubleValue() < 0) {
			throw new IllegalArgumentException("Average AUC cannot be a negative number");
		} else {
			this.avgAuc = avgAuc;
		}

	}

	public int getTrainingCount() {
		return this.trainingCount;
	}

	public void setTrainingCount(Integer trainingCount) {
		if (trainingCount == null) {
			throw new IllegalArgumentException("Average training cannot be null");
		} else if (trainingCount < 0) {
			throw new IllegalArgumentException("Average training cannot be a negative number");
		} else {
			this.trainingCount = trainingCount;
		}
	}

	public Date getTrainingTime() {
		return this.trainingTime;
	}

	public void setTrainingTime(Date trainingTime) {
		if (trainingTime == null) {
			throw new IllegalArgumentException("Training time cannot be null");
		} else {
			this.trainingTime = trainingTime;
		}

	}

	public boolean isIsCurrentModel() {
		return this.isCurrentModel;
	}

	public void setIsCurrentModel(Boolean isCurrentModel) {
		this.isCurrentModel = isCurrentModel;
	}

	public List<ModelNominalLabelDTO> getModelNominalLabelsDTO() {
		return this.modelNominalLabelsDTO;
	}

	public void setModelNominalLabelsDTO(List<ModelNominalLabelDTO> modelNominalLabelsDTO) {
		this.modelNominalLabelsDTO = modelNominalLabelsDTO;
	}

	public ModelFamilyDTO getModelFamilyDTO() throws PropertyNotSetException {
		return modelFamilyDTO;
	}

	public void setModelFamilyDTO(ModelFamilyDTO modelFamilyDTO) {
		this.modelFamilyDTO = modelFamilyDTO;
	}

	private List<ModelNominalLabelDTO> toModelNominalLabelDTOList(List<ModelNominalLabel> list) throws PropertyNotSetException {
		if (list != null) {
			List<ModelNominalLabelDTO> dtoList = new ArrayList<ModelNominalLabelDTO>();
			for (ModelNominalLabel d: list) {
				dtoList.add(new ModelNominalLabelDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<ModelNominalLabel> toModelNominalLabelList(List<ModelNominalLabelDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<ModelNominalLabel> eList = new ArrayList<ModelNominalLabel>();
			for (ModelNominalLabelDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	}

}
