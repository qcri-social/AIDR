package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class DocumentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6833511192868020105L;

	@XmlElement
	private Long documentID;

	@XmlElement
	private boolean hasHumanLabels;

	@XmlElement
	private CrisisDTO crisisDTO;

	@XmlElement
	private boolean isEvaluationSet;

	@XmlElement
	private Double valueAsTrainingSample;

	@XmlElement
	private Date receivedAt;

	@XmlElement
	private String language;

	@XmlElement
	private String doctype;

	@XmlElement
	private String data;

	@XmlElement
	private String wordFeatures;

	@XmlElement
	private String geoFeatures;

	@XmlElement
	private List<TaskAssignmentDTO> taskAssignmentsDTO = null;

	@XmlElement
	private List<DocumentNominalLabelDTO> documentNominalLabelsDTO = null;

	public DocumentDTO(){}

	public DocumentDTO(Long documentID, boolean hasHumanLabels){
		this.setDocumentID(documentID);
		this.setHasHumanLabels(hasHumanLabels);
	}

	public DocumentDTO(Document doc) throws PropertyNotSetException {
		this.setDocumentID(doc.getDocumentId());
		this.setHasHumanLabels(doc.isHasHumanLabels());
		if (doc.getCrisis() != null) {
			this.setCrisisDTO(new CrisisDTO(doc.getCrisis()));
		} else {
			throw new PropertyNotSetException("Crisis ID not set in document id: " + doc.getDocumentId());
		}
		this.setDoctype(doc.getDoctype());
		this.setGeoFeatures(doc.getGeoFeatures());
		this.setIsEvaluationSet(doc.isIsEvaluationSet());
		this.setLanguage(doc.getLanguage());
		this.setReceivedAt(doc.getReceivedAt());
		this.setData(doc.getData());
		this.setValueAsTrainingSample(doc.getValueAsTrainingSample());
		this.setWordFeatures(doc.getWordFeatures());

		// now the optional fields
		if (doc.hasDocumentNominalLabels()) {
			this.setDocumentNominalLabelsDTO(this.toDocumentNominalLabelDTOList(doc.getDocumentNominalLabels()));
		}
		if (doc.hasTaskAssignments()) {
			this.setTaskAssignmentDTO(this.toTaskAssignmentDTOList(doc.getTaskAssignments()));
		}

	}


	public Long getDocumentID() {
		return documentID;
	}

	public void setDocumentID(Long documentID) {
		if (documentID != null) {
			this.documentID = documentID;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	public boolean getHasHumanLabels() {
		return hasHumanLabels;
	}

	public void setHasHumanLabels(boolean hasHumanLabels) {
		this.hasHumanLabels = hasHumanLabels;
	}

	public String getGeoFeatures() {
		return geoFeatures;
	}

	public void setGeoFeatures(String geoFeatures) {
		this.geoFeatures = geoFeatures;
	}

	public CrisisDTO getCrisisDTO() {
		return this.crisisDTO;
	}

	public void setCrisisDTO(CrisisDTO crisisDTO) {
		if (crisisDTO != null) {
			this.crisisDTO = crisisDTO;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	public boolean getIsEvaluationSet() {
		return isEvaluationSet;
	}

	public void setIsEvaluationSet(boolean evaluationSet) {
		isEvaluationSet = evaluationSet;
	}

	public Double getValueAsTrainingSample() {
		return valueAsTrainingSample;
	}

	public void setValueAsTrainingSample(Double valueAsTrainingSample) {
		this.valueAsTrainingSample = valueAsTrainingSample;
	}

	public Date getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(Date receivedAt) {
		this.receivedAt = receivedAt;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getWordFeatures() {
		return wordFeatures;
	}

	public void setWordFeatures(String wordFeatures) {
		this.wordFeatures = wordFeatures;
	}

	public List<TaskAssignmentDTO> getTaskAssignmentsDTO() {
		return taskAssignmentsDTO;
	}

	public void setTaskAssignmentDTO(List<TaskAssignmentDTO> taskAssignmentsDTO) {
		if (taskAssignmentsDTO != null) {
			this.taskAssignmentsDTO = taskAssignmentsDTO;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	public List<DocumentNominalLabelDTO> getDocumentNominalLabelsDTO() {
		return this.documentNominalLabelsDTO;
	}

	public void setDocumentNominalLabelsDTO(List<DocumentNominalLabelDTO> documentNominalLabelsDTO) {
		if (documentNominalLabelsDTO != null) {
			this.documentNominalLabelsDTO = documentNominalLabelsDTO;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	private List<DocumentNominalLabelDTO> toDocumentNominalLabelDTOList(List<DocumentNominalLabel> list) throws PropertyNotSetException {
		if (list != null) {
			List<DocumentNominalLabelDTO> dtoList = new ArrayList<DocumentNominalLabelDTO>();
			for (DocumentNominalLabel d: list) {
				dtoList.add(new DocumentNominalLabelDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<DocumentNominalLabel> toDocumentNominalLabelList(List<DocumentNominalLabelDTO> list) throws PropertyNotSetException {
		if (list != null) {
			List<DocumentNominalLabel> eList = new ArrayList<DocumentNominalLabel>();
			for (DocumentNominalLabelDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	} 

	private List<TaskAssignmentDTO> toTaskAssignmentDTOList(List<TaskAssignment> list) {
		if (list != null) {
			List<TaskAssignmentDTO> dtoList = new ArrayList<TaskAssignmentDTO>();
			for (TaskAssignment d: list) {
				dtoList.add(new TaskAssignmentDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<TaskAssignment> toTaskAssignmentList(List<TaskAssignmentDTO> list) {
		if (list != null) {
			List<TaskAssignment> eList = new ArrayList<TaskAssignment>();
			for (TaskAssignmentDTO dto: list) {
				eList.add(dto.toEntity());
			}
			return eList;
		}
		return null;
	}

	public Document toEntity() throws PropertyNotSetException {
		Document doc = new Document(this.getCrisisDTO().toEntity(), isEvaluationSet, hasHumanLabels, 
				valueAsTrainingSample, receivedAt, language, doctype, data, wordFeatures, geoFeatures, null, null);
		if (this.documentID != null) {
			doc.setDocumentId(getDocumentID());
		}
		if (this.getDocumentNominalLabelsDTO() != null) {
			doc.setDocumentNominalLabels(this.toDocumentNominalLabelList(getDocumentNominalLabelsDTO()));
		}
		if (this.getTaskAssignmentsDTO() != null) {
			doc.setTaskAssignments(this.toTaskAssignmentList(getTaskAssignmentsDTO()));
		}
		return doc;
	}
}
