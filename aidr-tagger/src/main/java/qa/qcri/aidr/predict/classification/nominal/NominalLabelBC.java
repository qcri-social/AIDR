package qa.qcri.aidr.predict.classification.nominal;

import java.io.Serializable;

import qa.qcri.aidr.predict.classification.DocumentLabel;

/**
 * A multi-valued nominal label for a DocumentSet.
 * 
 * @author jrogstadius
 */
public class NominalLabelBC implements Serializable, DocumentLabel {

    private static final long serialVersionUID = 1L;

    private boolean isHumanLabel;
    private long sourceID;
    private int attributeID;
    private int nominalLabelID;
    private double confidence;
    private boolean isNullLabel;

    public boolean isHumanLabel() {
        return isHumanLabel;
    }
    
    public void setHumanLabel(boolean isHumanLabel) {
        this.isHumanLabel = isHumanLabel;
    }

    public long getSourceID() {
        return sourceID;
    }

    public void setSourceID(long sourceID) {
        this.sourceID = sourceID;
    }

    public boolean getIsNullLabel() {
        return this.isNullLabel;
    }

    public void setIsNullLabel(boolean isNullLabel) {
        this.isNullLabel = isNullLabel;
    }

    public int getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    public int getNominalLabelID() {
        return nominalLabelID;
    }

    public void setNominalLabelID(int nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String toString() {
        return "{" + attributeID + "," + nominalLabelID + "," + confidence + "}";
    }

    public NominalLabelBC(long sourceID, int attributeID, int nominalLabelID,
            double confidence) {
        this.sourceID = sourceID;
        this.attributeID = attributeID;
        this.nominalLabelID = nominalLabelID;
        this.confidence = confidence;
    }
    
    // Added by koushik - to handle empty nominal_labels array
    public NominalLabelBC() {
    	this.sourceID = 0;
    	this.attributeID = 0;
    	this.nominalLabelID = 0;
    	this.confidence = 0.0;
    	this.isHumanLabel = false;
    }
}
