package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentNominalLabelDTO implements Serializable {

	public  DocumentNominalLabelDTO() {
		this.timestamp = new java.sql.Timestamp(java.util.Calendar.getInstance().getTimeInMillis());
	}

	public  DocumentNominalLabelDTO(Long documentID, Long nominalLabelID){
		this.documentID = documentID;
		this.nominalLabelID = nominalLabelID;
		this.timestamp = new java.sql.Timestamp(java.util.Calendar.getInstance().getTimeInMillis());
	}

	public  DocumentNominalLabelDTO(Long documentID, Long nominalLabelID, Long userID){
		this.documentID = documentID;
		this.nominalLabelID = nominalLabelID;
		this.userID = userID;
		this.timestamp = new java.sql.Timestamp(java.util.Calendar.getInstance().getTimeInMillis());
	}

	public Long getDocumentID() {
		return documentID;
	}

	public void setDocumentID(Long documentID) {
		this.documentID = documentID;
	}

	public Long getNominalLabelID() {
		return nominalLabelID;
	}

	public void setNominalLabelID(Long nominalLabelID) {
		this.nominalLabelID = nominalLabelID;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}


	@XmlElement
	private Long documentID;


	@XmlElement
	private Long nominalLabelID;

	@XmlElement
	private Long userID;

	@XmlElement
	private Date timestamp;

}
