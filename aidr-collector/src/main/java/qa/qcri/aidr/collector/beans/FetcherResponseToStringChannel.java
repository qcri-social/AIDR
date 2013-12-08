/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.beans;

/**
 *
 * @author Imran
 */
public class FetcherResponseToStringChannel {
    
    private AIDR aidr;
    

    public FetcherResponseToStringChannel() {
        this.aidr= new AIDR();
        
    }

    public FetcherResponseToStringChannel(AIDR aidr) {
        this.aidr = aidr;
        
    }

    /**
     * @return the aidr
     */
    public AIDR getAidr() {
        return aidr;
    }

    /**
     * @param aidr the aidr to set
     */
    public void setAidr(AIDR aidr) {
        this.aidr = aidr;
    }

   
  
    
    
}
