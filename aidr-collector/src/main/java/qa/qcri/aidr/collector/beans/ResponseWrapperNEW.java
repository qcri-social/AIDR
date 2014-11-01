/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.beans;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 *
 * @author Muhammad Imran
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseWrapper", propOrder = {
    "returnCode",
    "userMessage",
    "developerMessage",
    
 })
@XmlRootElement(name = "responseWrapper")
@JsonSerialize(include = Inclusion.NON_DEFAULT)
public class ResponseWrapperNEW implements Serializable{
    
    private String returnCode;
    private String userMessages;
    private String developerMessage;
    

    public ResponseWrapperNEW() {
    }

    /**
     * @return the returnCode
     */
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * @param returnCode the returnCode to set
     */
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * @return the userMessages
     */
    public String getUserMessages() {
        return userMessages;
    }

    /**
     * @param userMessages the userMessages to set
     */
    public void setUserMessages(String userMessages) {
        this.userMessages = userMessages;
    }

    /**
     * @return the developerMessage
     */
    public String getDeveloperMessage() {
        return developerMessage;
    }

    /**
     * @param developerMessage the developerMessage to set
     */
    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }
    
    

}
