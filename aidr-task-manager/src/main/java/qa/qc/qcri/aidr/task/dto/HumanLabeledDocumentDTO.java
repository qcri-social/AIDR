package qa.qc.qcri.aidr.task.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;

@XmlRootElement
public class HumanLabeledDocumentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement private DocumentDTO doc;
	
	@XmlElement private DocumentNominalLabelDTO labelData;
	
	public HumanLabeledDocumentDTO() {
	}
	
	public HumanLabeledDocumentDTO(DocumentDTO doc, DocumentNominalLabelDTO labelData) throws Exception {
		if (doc != null) {
			this.setDoc(doc);
		} else {
			throw new PropertyNotSetException("label Data can't be null in constructor");
		}
		if (labelData != null) {
			this.setLabelData(labelData);
		} else {
			throw new PropertyNotSetException("label Data can't be null in constructor");
		}
	}
	
	public DocumentDTO getDoc() {
		return this.doc;
	}
	
	public void setDoc(DocumentDTO doc) {
		this.doc = doc;
	}
	
	public DocumentNominalLabelDTO getLabelData() {
		return this.labelData;
	}
	
	public void setLabelData(DocumentNominalLabelDTO labelData) {
		this.labelData = labelData;
	}
}
