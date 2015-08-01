package qa.qcri.aidr.trainer.api.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelIdDTO;

/**
 * Created by: koushik
 */

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocumentNominalLabel implements Serializable {
	
	private static Logger logger=Logger.getLogger(DocumentNominalLabel.class);

    private static final long serialVersionUID = -5527566248002296042L;

    public  DocumentNominalLabel() {}
    public  DocumentNominalLabel(Long documentID, Long nominalLabelID, Long userID){
        this.documentID = documentID;
        this.nominalLabelID = nominalLabelID;
        this.userID = userID;
        this.timestamp = new Date();
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

	public static DocumentNominalLabel toLocalDocumentNominalLabel(DocumentNominalLabelDTO doc) {
		if (doc != null) {
			try {
				DocumentNominalLabel nominalDoc = new DocumentNominalLabel(doc.getIdDTO().getDocumentId(), doc.getIdDTO().getNominalLabelId(), doc.getIdDTO().getUserId());
				return nominalDoc;
			} catch (PropertyNotSetException e) {
				logger.error("Exception while parsing DocumentNominalLabelDTO to LocalDocumentNominalLabel",e);
			}
		}
		return null;
	}
	
	public static DocumentNominalLabelDTO toDocumentNominalLabelDTO(DocumentNominalLabel doc) {
		if (doc != null) {
			DocumentNominalLabelIdDTO idDTO = new DocumentNominalLabelIdDTO(doc.getDocumentID(), doc.getNominalLabelID(), doc.getUserID());
			DocumentNominalLabelDTO nominalDoc = new DocumentNominalLabelDTO();
			nominalDoc.setIdDTO(idDTO);
			return nominalDoc;
		}
		return null;
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
