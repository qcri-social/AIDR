package qa.qcri.aidr.trainer.api.entity.keychain;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/2/13
 * Time: 7:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentNominalLabelKey implements java.io.Serializable{
    //documentID, nominalLabelID
    private Long documentID;
    private Long nominalLabelID;
    public DocumentNominalLabelKey() { }

    public DocumentNominalLabelKey(Long documentID, Long nominalLabelID) {
        this.documentID = documentID;
        this.nominalLabelID = nominalLabelID;
    }

    public Long getNominalLabelID() {
        return nominalLabelID;
    }
    public void setNominalLabelID(Long nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    public Long getDocumentID() {
        return documentID;
    }

    public void setDocumentID(Long documentID) {
        this.documentID = documentID;
    }

    public boolean equals(Object key) {
        boolean result = true;
        if (!(key instanceof DocumentNominalLabelKey)) {return false;}

        Long otherDocumentID = ((DocumentNominalLabelKey)key).getDocumentID();
        Long otherNominalLabelID = ((DocumentNominalLabelKey)key).getNominalLabelID();

        if (nominalLabelID == null || otherNominalLabelID == null) {
            result = false;
        }else {
            result = nominalLabelID.equals(otherNominalLabelID);
        }

        if (documentID == null || otherDocumentID == null) {
            result = false;
        }else {
            result = documentID.equals(otherDocumentID);
        }

        return result;
    }

    public int hashCode() {
        int code = 0;
        if (documentID!=null) {code +=documentID;}
        if (nominalLabelID!=null) {code +=nominalLabelID;}
        return code;
    }
}
