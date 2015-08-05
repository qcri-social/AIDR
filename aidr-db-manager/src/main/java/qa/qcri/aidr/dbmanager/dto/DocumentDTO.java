package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Crisis;
import qa.qcri.aidr.dbmanager.entities.misc.CrisisType;
import qa.qcri.aidr.dbmanager.entities.misc.Users;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class DocumentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6833511192868020105L;
	private static final Logger logger = Logger.getLogger("db-manager-log");

	@XmlElement
	private Long documentID;

	@XmlElement
	private boolean hasHumanLabels;

	@XmlElement
	@JsonBackReference
	private CrisisDTO crisisDTO = null;

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

	@XmlElement
	private NominalLabelDTO nominalLabelDTO = null;
	
	public DocumentDTO(){}

	public DocumentDTO(Long documentID, boolean hasHumanLabels){
		this.setDocumentID(documentID);
		this.setHasHumanLabels(hasHumanLabels);
	}

	public DocumentDTO(CrisisDTO crisis, boolean isEvaluationSet,
			boolean hasHumanLabels, Double valueAsTrainingSample,
			Date receivedAt, String language, String doctype, String data) {
		this.setCrisisDTO(crisis);
		this.setIsEvaluationSet(isEvaluationSet);
		this.setHasHumanLabels(hasHumanLabels);
		this.setValueAsTrainingSample(valueAsTrainingSample);
		this.setReceivedAt(receivedAt);
		this.setLanguage(language);
		this.setDoctype(doctype);
		this.setData(data);
	}

	public DocumentDTO(CrisisDTO crisis, boolean isEvaluationSet,
			boolean hasHumanLabels, Double valueAsTrainingSample,
			Date receivedAt, String language, String doctype, String data,
			String wordFeatures, String geoFeatures, List<TaskAssignmentDTO> taskAssignments,
			List<DocumentNominalLabelDTO> documentNominalLabels) {
		this.setCrisisDTO(crisis);
		this.setIsEvaluationSet(isEvaluationSet);
		this.setHasHumanLabels(hasHumanLabels);
		this.setValueAsTrainingSample(valueAsTrainingSample);
		this.setReceivedAt(receivedAt);
		this.setLanguage(language);
		this.setDoctype(doctype);
		this.setData(data);
		
		this.setWordFeatures(wordFeatures);
		this.setGeoFeatures(geoFeatures);
		this.setTaskAssignmentDTO(taskAssignments);
		this.setDocumentNominalLabelsDTO(documentNominalLabels);
	}
	
	
	public DocumentDTO(Document doc) throws PropertyNotSetException {
		if (doc != null) {
			if (doc.getDocumentId() != null) {
				this.setDocumentID(doc.getDocumentId());
			}
			this.setHasHumanLabels(doc.isHasHumanLabels());
			if (doc.hasCrisis()) {
				Crisis c = new Crisis(doc.getCrisis().getUsers(), doc.getCrisis().getCrisisType(), doc.getCrisis().getName(), doc.getCrisis().getCode(),
										doc.getCrisis().isIsTrashed(), doc.getCrisis().isIsMicromapperEnabled());
				c.setCrisisId(doc.getCrisis().getCrisisId());
				this.setCrisisDTO(new CrisisDTO(c));
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
		} else {
			logger.error("Entity = null in constructor");
		}
	}


	public Long getDocumentID() {
		return documentID;
	}

	public void setDocumentID(Long documentID) {
			this.documentID = documentID;
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
		this.crisisDTO = crisisDTO;
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
		this.taskAssignmentsDTO = taskAssignmentsDTO;
	}

	public List<DocumentNominalLabelDTO> getDocumentNominalLabelsDTO() {
		return this.documentNominalLabelsDTO;
	}

	public void setDocumentNominalLabelsDTO(List<DocumentNominalLabelDTO> documentNominalLabelsDTO) {
		this.documentNominalLabelsDTO = documentNominalLabelsDTO;
	}
	
	public NominalLabelDTO getNominalLabelDTO() {
		return this.nominalLabelDTO;
	}
	
	public void setNominalLabelDTO(NominalLabelDTO nominalLabelDTO) {
		this.nominalLabelDTO = nominalLabelDTO;
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

	private List<TaskAssignmentDTO> toTaskAssignmentDTOList(List<TaskAssignment> list) throws PropertyNotSetException {
		if (list != null) {
			List<TaskAssignmentDTO> dtoList = new ArrayList<TaskAssignmentDTO>();
			for (TaskAssignment d: list) {
				dtoList.add(new TaskAssignmentDTO(d));
			}
			return dtoList;
		}
		return null;
	}


	private List<TaskAssignment> toTaskAssignmentList(List<TaskAssignmentDTO> list) throws PropertyNotSetException {
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
			doc.setDocumentId(this.getDocumentID());
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
