/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predict.data;

/**
 *
 * @author Imran
 */
public class AIDR implements java.io.Serializable{
    
    private String crisis_code;
    private String crisis_name;
    private String doctype;

    public AIDR() {
    }

    public AIDR(String code, String name, String docType) {
        this.crisis_code= code;
        this.crisis_name=name;
        this.doctype=docType;
        
    }

    /**
     * @return the crisis_code
     */
    public String getCrisis_code() {
        return crisis_code;
    }

    /**
     * @param crisis_code the crisis_code to set
     */
    public void setCrisis_code(String crisis_code) {
        this.crisis_code = crisis_code;
    }

    /**
     * @return the crisis_Name
     */
    public String getCrisis_name() {
        return crisis_name;
    }

    /**
     * @param crisis_Name the crisis_Name to set
     */
    public void setCrisis_name(String crisis_name) {
        this.crisis_name = crisis_name;
    }

    /**
     * @return the doctype
     */
    public String getDoctype() {
        return doctype;
    }

    /**
     * @param doctype the doctype to set
     */
    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }
    
    

    
    
}
