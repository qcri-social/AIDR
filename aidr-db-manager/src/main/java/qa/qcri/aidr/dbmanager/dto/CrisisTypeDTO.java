package qa.qcri.aidr.dbmanager.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.entities.misc.Collection;
import qa.qcri.aidr.dbmanager.entities.misc.CrisisType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class CrisisTypeDTO implements java.io.Serializable {

    private static final long serialVersionUID = 8074463052776843105L;
    private static final Logger logger = Logger.getLogger("db-manager-log");

    @XmlElement
    private Long crisisTypeId;

    @XmlElement
    private String name;

    @XmlElement
    private List<CollectionDTO> crisisesDTO = null;

    @XmlElement
    private int numberOfCrisisAssociated;

    public CrisisTypeDTO() {
    }

    public CrisisTypeDTO(String name) {
        this.name = name;
    }

    public CrisisTypeDTO(Long crisisTypeId, String name) {
        this.setCrisisTypeId(crisisTypeId);
        this.setName(name);
    }

    public CrisisTypeDTO(Long crisisTypeId, String name, List<CollectionDTO> crisisesDTO) {
        this.setCrisisTypeId(crisisTypeId);
        this.setName(name);
        this.setCrisisesDTO(crisisesDTO);
    }

    public CrisisTypeDTO(CrisisType crisisType) {
        if (crisisType != null) {
            this.setCrisisTypeId(crisisType.getCrisisTypeId());
            this.setName(crisisType.getName());
        } else {
            logger.error("Entity = null in constructor");
        }
    }
    
    public Long getCrisisTypeId() {
        return this.crisisTypeId;
    }

    public void setCrisisTypeId(Long crisisTypeId) {
        this.crisisTypeId = crisisTypeId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CollectionDTO> getCrisisesDTO() {
        return this.crisisesDTO;
    }

    public void setCrisisesDTO(List<CollectionDTO> crisisesDTO) {
        this.crisisesDTO = crisisesDTO;
    }

    private List<CollectionDTO> toCrisisDTOList(List<Collection> list) throws PropertyNotSetException {
        if (list != null) {
            List<CollectionDTO> crisisesDTO = new ArrayList<CollectionDTO>();
            for (Collection c : list) {
                crisisesDTO.add(new CollectionDTO(c));
            }
            return crisisesDTO;
        }
        return null;
    }

    private List<Collection> toCrisisList(List<CollectionDTO> list) throws PropertyNotSetException {
        if (list != null) {
            List<Collection> crisises = new ArrayList<Collection>();
            for (CollectionDTO dto : list) {
                crisises.add(dto.toEntity());
            }
            return crisises;
        }
        return null;
    }

    public CrisisType toEntity() {
        CrisisType cType = new CrisisType(this.getName());
        if (this.getCrisisTypeId() != null) {
            cType.setCrisisTypeId(this.getCrisisTypeId());
        }
        cType.setName(this.getName());

        return cType;
    }

    /**
     * @return the numberOfCrisisAssociated
     */
    public int getNumberOfCrisisAssociated() {
        return numberOfCrisisAssociated;
    }

    /**
     * @param numberOfCrisisAssociated the numberOfCrisisAssociated to set
     */
    public void setNumberOfCrisisAssociated(int numberOfCrisisAssociated) {
        this.numberOfCrisisAssociated = numberOfCrisisAssociated;
    }
}
