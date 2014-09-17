package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Date;

import qa.qcri.aidr.trainer.api.entity.keychain.DocumentNominalLabelKey;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity @IdClass(DocumentNominalLabelKey.class)
@Table(catalog = "aidr_predict",name = "document_nominal_label")
@XmlRootElement
public class DocumentNominalLabel implements Serializable {

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

    @XmlElement
    @Id
    @Column(name = "documentID")
    private Long documentID;

    @XmlElement
    @Id
    @Column (name = "nominalLabelID", nullable = false)
    private Long nominalLabelID;
    
    @XmlElement
    @Id
    @Column (name = "userID", nullable = false)
    private Long userID;

    @XmlElement
    @Column (name = "timestamp", nullable = false)
    private Date timestamp;


}
