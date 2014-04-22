package qa.qcri.aidr.task.entities;

import javax.persistence.*;



import java.io.Serializable;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity 	//@IdClass(DocumentNominalLabelKey.class)
@Table(catalog = "aidr_predict",name = "document_nominal_label")
public class DocumentNominalLabel implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    public  DocumentNominalLabel() {}
    public  DocumentNominalLabel(Long documentID, Long nominalLabelID){
        this.documentID = documentID;
        this.nominalLabelID = nominalLabelID;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    @Id
    @Column(name = "documentID")
    private Long documentID;

    
    //@Id
    @Column (name = "nominalLabelID", nullable = false)
    private Long nominalLabelID;

    @Column (name = "timestamp", nullable = false)
    private Date timestamp;


}
