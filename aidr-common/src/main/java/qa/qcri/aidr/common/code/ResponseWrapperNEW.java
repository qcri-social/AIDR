package qa.qcri.aidr.common.code;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * A response sent to the front-end of the application.
 *
 * @author Muhammad Imran
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseWrapper", propOrder = {
    "returnCode",
    "userMessages",
    "developerMessage",
    
 })
@XmlRootElement(name = "responseWrapper")
@JsonSerialize(include = Inclusion.NON_DEFAULT)
public class ResponseWrapperNEW implements Serializable {

	/**
	 * A serial versionID for this Serializable object.
	 */
	private static final long serialVersionUID = 7835885665823356986L;

	/**
	 * A return code to give to the front-end.
	 */
	private String returnCode;

	/**
	 * A message meant to be seen by the end user.
	 */
	private String userMessages;

	/**
	 * A message meant to be seen by an application developer, in case of error/warning
	 */
	private String developerMessage;

	/**
	 * Create an empty response.
	 */
	public ResponseWrapperNEW() {
	}

	/**
	 * Gets the return code
	 * 
	 * @return the return code
	 */
	public String getReturnCode() {
		return returnCode;
	}

	/**
	 * Sets the return code
	 * 
	 * @param returnCode the return code
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * Gets the message to be shown to the end user
	 * 
	 * @return the userMessages
	 */
	public String getUserMessages() {
		return userMessages;
	}

	/**
	 * Sets the message to be shown to the end user
	 * 
	 * @param userMessages the message
	 */
	public void setUserMessages(String userMessages) {
		this.userMessages = userMessages;
	}

	/**
	 * Gets the message to be shown to the application developer, in case of error/warning
	 * 
	 * @return the developerMessage
	 */
	public String getDeveloperMessage() {
		return developerMessage;
	}

	/**
	 * Sets the message to be shown to the application developer, in case of error/warning
	 * 
	 * @param developerMessage the message
	 */
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
}