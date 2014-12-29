package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.model.ModelNominalLabel;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttributeDependentLabel;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentNominalLabelDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 731958369334372038L;

	@XmlElement
	private DocumentNominalLabelIdDTO idDTO;

	@XmlElement
	private Date timestamp;

	@XmlElement
	private NominalLabelDTO nominalLabelDTO;

	@XmlElement
	private DocumentDTO documentDTO;

	public  DocumentNominalLabelDTO() {}

	public  DocumentNominalLabelDTO(DocumentNominalLabel doc) throws PropertyNotSetException {
		if (doc != null) {
			this.setIdDTO(new DocumentNominalLabelIdDTO(doc.getId()));
			if (doc.hasDocument()) {
				Document d = new Document(doc.getDocument().getCrisis(), doc.getDocument().isIsEvaluationSet(),
						doc.getDocument().isHasHumanLabels(), doc.getDocument().getValueAsTrainingSample(),
						doc.getDocument().getReceivedAt(), doc.getDocument().getLanguage(), doc.getDocument().getDoctype(), doc.getDocument().getData());

				d.setWordFeatures(doc.getDocument().getWordFeatures());
				d.setGeoFeatures(doc.getDocument().getGeoFeatures()); 
				d.setDocumentId(doc.getDocument().getDocumentId());
				this.setDocumentDTO(new DocumentDTO(d));
			}
			if (doc.hasNominalLabel()) {
				NominalLabel nb = new NominalLabel(doc.getNominalLabel().getNominalAttribute(),
						doc.getNominalLabel().getNominalLabelCode(), doc.getNominalLabel().getName(), doc.getNominalLabel().getDescription(),
						doc.getNominalLabel().getSequence());
				nb.setNominalLabelId(doc.getNominalLabel().getNominalLabelId());
				this.setNominalLabelDTO(new NominalLabelDTO(nb));
			}
			this.setTimestamp(doc.getTimestamp());
		}
	}

	public DocumentNominalLabelDTO(DocumentNominalLabelIdDTO idDTO,
			NominalLabelDTO nominalLabelDTO, DocumentDTO documentDTO) throws PropertyNotSetException {
		this.setIdDTO(idDTO);
		this.setNominalLabelDTO(nominalLabelDTO);
		this.setDocumentDTO(documentDTO);
	}

	public DocumentNominalLabelIdDTO getIdDTO() {
		return this.idDTO;
	}

	public void setIdDTO(DocumentNominalLabelIdDTO idDTO) {
		if (idDTO != null) {
			this.idDTO = idDTO;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public NominalLabelDTO getNominalLabelDTO() {
		return this.nominalLabelDTO;
	}

	public void setNominalLabelDTO(NominalLabelDTO nominalLabelDTO) {
		if (nominalLabelDTO != null) {
			this.nominalLabelDTO = nominalLabelDTO;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	public DocumentDTO getDocumentDTO() {
		return this.documentDTO;
	}

	public void setDocumentDTO(DocumentDTO documentDTO) {
		if (documentDTO != null) {
			this.documentDTO = documentDTO;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	public DocumentNominalLabel toEntity() throws PropertyNotSetException {
		DocumentNominalLabel doc = new DocumentNominalLabel();
		if (this.idDTO != null) {
			doc.setId(this.getIdDTO().toEntity());
		}
		if (this.getDocumentDTO() != null) {
			doc.setDocument(this.getDocumentDTO().toEntity());
		}
		if (this.getNominalLabelDTO() != null) {
			doc.setNominalLabel(this.getNominalLabelDTO().toEntity());
		}
		doc.setTimestamp(this.getTimestamp());
		return doc;
	}
}
