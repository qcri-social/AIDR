/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predict.classification.nominal;

/**
 *
 * @author Imran
 */
public class ModelNominalLabelPerformance {
    
    private int nominalLabelID;
    private double precision;
    private double recall;
    private double auc;
    private int classifiedDocumentCount;

    public ModelNominalLabelPerformance() {
    }

    
    public ModelNominalLabelPerformance(int nominalLabelID, double precision, double recall, double auc, int classifiedDocumentCount) {
        this.nominalLabelID = nominalLabelID;
        this.precision = precision;
        this.recall = recall;
        this.auc = auc;
        this.classifiedDocumentCount = classifiedDocumentCount;
    }

    
    
    /**
     * @return the nominalLabelID
     */
    public int getNominalLabelID() {
        return nominalLabelID;
    }

    /**
     * @param nominalLabelID the nominalLabelID to set
     */
    public void setNominalLabelCode(int nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    /**
     * @return the precision
     */
    public double getPrecision() {
        return precision;
    }

    /**
     * @param precision the precision to set
     */
    public void setPrecision(double precision) {
        this.precision = precision;
    }

    /**
     * @return the recall
     */
    public double getRecall() {
        return recall;
    }

    /**
     * @param recall the recall to set
     */
    public void setRecall(double recall) {
        this.recall = recall;
    }

    /**
     * @return the auc
     */
    public double getAuc() {
        return auc;
    }

    /**
     * @param auc the auc to set
     */
    public void setAuc(double auc) {
        this.auc = auc;
    }

    /**
     * @return the classifiedDocumentCount
     */
    public int getClassifiedDocumentCount() {
        return classifiedDocumentCount;
    }

    /**
     * @param classifiedDocumentCount the classifiedDocumentCount to set
     */
    public void setClassifiedDocumentCount(int classifiedDocumentCount) {
        this.classifiedDocumentCount = classifiedDocumentCount;
    }
    
    
    
    
    
}
