/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.beans;


/**
 * @author Imran
 * This class is used to send response to the clients. 
 */
public class ResponseWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6615942283542314788L;
	protected String statusCode;
	protected String message;
	protected boolean isSourceOutage;
	protected Object dataObject;

	public ResponseWrapper() {
	}

	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public Object getDataObject() {
		return dataObject;
	}

	public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSourceOutage() {
		return isSourceOutage;
	}

	public void setSourceOutage(boolean isSourceOutage) {
		this.isSourceOutage = isSourceOutage;
	}

}
