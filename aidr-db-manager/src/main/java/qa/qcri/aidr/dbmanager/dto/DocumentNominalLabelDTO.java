package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;

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
		this.setIdDTO(new DocumentNominalLabelIdDTO(doc.getId()));
		this.setDocumentDTO(new DocumentDTO(doc.getDocument()));
		this.setNominalLabelDTO(new NominalLabelDTO(doc.getNominalLabel()));
		this.setTimestamp(doc.getTimestamp());
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

	public NominalLabelDTO getNominalLabelDTO() throws PropertyNotSetException {
		if (this.nominalLabelDTO != null) {
			return this.nominalLabelDTO;
		} else {
			throw new PropertyNotSetException();
		}
	}

	public void setNominalLabelDTO(NominalLabelDTO nominalLabelDTO) {
		if (nominalLabelDTO != null) {
			this.nominalLabelDTO = nominalLabelDTO;
		} else {
			throw new IllegalArgumentException("Argument cannot be null!");
		}
	}

	public DocumentDTO getDocumentDTO() throws PropertyNotSetException {
		if (this.documentDTO != null) {
			return this.documentDTO;
		} else {
			throw new PropertyNotSetException();
		}
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
		doc.setDocument(this.getDocumentDTO().toEntity());
		doc.setNominalLabel(this.getNominalLabelDTO().toEntity());
		doc.setTimestamp(this.getTimestamp());
		return doc;
	}
}
