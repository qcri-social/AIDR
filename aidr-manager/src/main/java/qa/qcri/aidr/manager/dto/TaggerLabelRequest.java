package qa.qcri.aidr.manager.dto;


import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;

public class TaggerLabelRequest {

	private Integer nominalLabelID;

	private Integer nominalAttributeID;

	private String name;

	private String nominalLabelCode;

	private String description;

	public Integer getNominalLabelID() {
		return nominalLabelID;
	}

	public void setNominalLabelID(Integer nominalLabelID) {
		this.nominalLabelID = nominalLabelID;
	}

	public Integer getNominalAttributeID() {
		return nominalAttributeID;
	}

	public void setNominalAttributeID(Integer nominalAttributeID) {
		this.nominalAttributeID = nominalAttributeID;
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

	public NominalLabelDTO toDTO() throws Exception {
		NominalLabelDTO dto = new NominalLabelDTO();
		if (this.getNominalLabelID() != null) {
			dto.setNominalLabelId(new Long(this.getNominalLabelID()));
		}
		dto.setNominalLabelCode(this.getNominalLabelCode());
		dto.setName(this.getName());
		dto.setDescription(this.getDescription());
		
		// TODO: UI should send sequence number
		dto.setSequence(100);
		NominalAttributeDTO na = new NominalAttributeDTO();
		if (this.getNominalAttributeID() != null) {
			na.setNominalAttributeId(new Long(this.getNominalAttributeID()));
			dto.setNominalAttributeDTO(na);
		} else {
			dto.setNominalAttributeDTO(null);
		}
		return dto;
	}
}
