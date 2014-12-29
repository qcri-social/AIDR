package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;

public class TaggerLabel {

    private Integer nominalLabelID;

    private String name;

    private String nominalLabelCode;

    private String description;

    private int sequence = 100;

    private TaggerAttribute nominalAttribute;

    public TaggerLabel() {
    }

    public TaggerLabel(String name, Integer nominalLabelID) {
        this.name = name;
        this.nominalLabelID = nominalLabelID;
    }

    public TaggerLabel(String name, String nominalLabelCode, String description, TaggerAttribute nominalAttribute, int sequence) {
        this.name = name;
        this.nominalLabelCode = nominalLabelCode;
        this.description = description;
        this.nominalAttribute = nominalAttribute;
        this.sequence = sequence;
    }

    public TaggerLabel(NominalLabelDTO dto) throws Exception {
    	if (dto != null) {
    		this.setNominalLabelID(dto.getNominalLabelId().intValue());
    		this.setName(dto.getName());
    		this.setDescription(dto.getDescription());
    		this.setNominalLabelCode(dto.getNominalLabelCode());
                if (null != dto.getSequence())
    		this.setSequence(dto.getSequence());
    		this.setNominalAttribute(new TaggerAttribute(dto.getNominalAttributeDTO()));
    	}
    }
    
    public NominalLabelDTO toDTO() throws Exception {
    	NominalLabelDTO dto = new NominalLabelDTO();
    	dto.setNominalLabelId(new Long(this.getNominalLabelID()));
    	dto.setName(this.getName());
    	dto.setDescription(this.getDescription());
    	dto.setSequence(this.getSequence());
    	dto.setNominalAttributeDTO(this.getNominalAttribute().toDTO());
    	
    	return dto;
    }
    
    public Integer getNominalLabelID() {
        return nominalLabelID;
    }

    public void setNominalLabelID(Integer nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNominalLabelCode() {
        return nominalLabelCode;
    }

    public void setNominalLabelCode(String nominalLabelCode) {
        this.nominalLabelCode = nominalLabelCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaggerAttribute getNominalAttribute() {
        return nominalAttribute;
    }

    public void setNominalAttribute(TaggerAttribute nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
