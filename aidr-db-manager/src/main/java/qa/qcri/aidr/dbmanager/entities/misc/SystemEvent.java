package qa.qcri.aidr.dbmanager.entities.misc;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

@Entity
@Table(name = "system_event", catalog = "aidr_predict")
public class SystemEvent implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6475156575527664114L;
	private Logger logger = Logger.getLogger(SystemEvent.class);

	public SystemEvent(String severity, String module, String description) {
		this(severity,module,null,description);	
	}
	
	public SystemEvent(String severity, String module, String code, String description) {
		this(severity,module,code,description,false);	
	}
	
	public SystemEvent(String severity, String module, String description, Boolean emailSent) {
		this(severity,module,null,description,emailSent);	
	}
	
	public SystemEvent(String severity, String module, String code,
			String description, Boolean emailSent) {
		this(new Date(System.currentTimeMillis()),severity,module,code,description,emailSent);	
	}
	
	public SystemEvent(Date receivedAt, String severity, String module,
			String description) {
		this(receivedAt,severity,module,null,description);	
	}
	
	public SystemEvent(Date receivedAt, String severity, String module, String code,
			String description) {
		this(receivedAt,severity,module,code,description,false);	
	}

	public SystemEvent(Date receivedAt, String severity, String module, String code,
			String description, Boolean emailSent) {
		super();
		logger.info("date:" + receivedAt + " modeule:" + module +" emailSent?" + emailSent);
		this.receivedAt = receivedAt;
		this.severity = severity;
		this.module = module;
		this.code = code;
		this.description = description;
		this.emailSent = emailSent;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "eventID", unique = true, nullable = false)
	private Long eventID;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "receivedAt", nullable = false, length = 19)
	private Date receivedAt;
	
	@Column(name = "severity", nullable = false, length = 20)
	private String severity;
	
	@Column(name = "module", nullable = false, length = 50)
	private String module;
	
	@Column(name = "code", length = 50)
	private String code;
	
	@Column(name = "description", nullable = false, length = 65535, columnDefinition="Text")
	private String description;
	
	@Column(name = "emailSent")
	private Boolean emailSent;

}
