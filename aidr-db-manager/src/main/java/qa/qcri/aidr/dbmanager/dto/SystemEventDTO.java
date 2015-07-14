package qa.qcri.aidr.dbmanager.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.SystemEvent;

@XmlRootElement
public class SystemEventDTO implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5569837731283802124L;
	
	@XmlElement private Integer eventID;
	@XmlElement private Date receivedAt;
	@XmlElement private String severity;
	@XmlElement private String module;
	@XmlElement private String code;
	@XmlElement private String description;
	
	public SystemEventDTO(String severity, String module, String description) {
		this(severity,module,null,description);				
	}
	
	public SystemEventDTO(String severity, String module, String code, String description) {
		super();
		this.severity = severity;
		this.module = module;
		this.description = description;
		this.code = code;
		this.receivedAt = new Date(System.currentTimeMillis());						
	}
	
	public SystemEvent toEntity() throws PropertyNotSetException {
		SystemEvent sys = new SystemEvent(this.receivedAt,this.severity,this.module,this.code,this.description);
		return sys;		
	}
	
	public Integer getEventID() {
		return eventID;
	}
	public void setEventID(Integer eventID) {
		this.eventID = eventID;
	}
	public Date getReceivedAt() {
		return receivedAt;
	}
	public void setReceivedAt(Date receivedAt) {
		this.receivedAt = receivedAt;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
