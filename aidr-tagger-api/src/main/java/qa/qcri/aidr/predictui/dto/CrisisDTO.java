package qa.qcri.aidr.predictui.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class CrisisDTO {
	
	@XmlElement
    private String code;
	
	@XmlElement
    private String name;

	@XmlElement
    private CrisisTypeDTO crisisType;

	@XmlElement
    private Integer crisisID;

    public CrisisDTO() {
    }

    public CrisisDTO(Integer crisisID) {
        this.crisisID = crisisID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CrisisTypeDTO getCrisisType() {
        return crisisType;
    }

    public void setCrisisType(CrisisTypeDTO crisisType) {
        this.crisisType = crisisType;
    }

    public Integer getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Integer crisisID) {
        this.crisisID = crisisID;
    }

}