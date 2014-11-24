package qa.qcri.aidr.predictdb.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class CrisisDTO implements Serializable {

	public CrisisDTO(){}

	public Long getCrisisID() {
		return crisisID;
	}

	public void setCrisisID(Long crisisID) {
		this.crisisID = crisisID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCrisisTypeID() {
		return crisisTypeID;
	}

	public void setCrisisTypeID(Long crisisTypeID) {
		this.crisisTypeID = crisisTypeID;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	@XmlElement
	private Long crisisID;

	@XmlElement
	private String name;

	@XmlElement
	private Long crisisTypeID;

	@XmlElement
	private String code;

	@XmlElement
	private Long userID;

}

