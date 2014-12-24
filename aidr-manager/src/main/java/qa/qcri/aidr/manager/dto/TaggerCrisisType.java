package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;

public class TaggerCrisisType {

	private Integer crisisTypeId;

	private String name;

	private int numberOfCrisisAssociated;

	public TaggerCrisisType() {
	}

	public TaggerCrisisType(Integer crisisTypeId) {
		this.crisisTypeId = crisisTypeId;
	}

	public TaggerCrisisType(Integer crisisTypeId, String name) {
		this.crisisTypeId = crisisTypeId;
		this.name = name;
	}

	public TaggerCrisisType(CrisisTypeDTO dto) throws Exception {
		if (dto != null) {
			this.setCrisisTypeId(dto.getCrisisTypeId().intValue());
			this.setName(dto.getName());
			this.setNumberOfCrisisAssociated(dto.getNumberOfCrisisAssociated());
		}
	}

	public CrisisTypeDTO toDTO() throws Exception {
		CrisisTypeDTO dto = new CrisisTypeDTO(new Long(this.getCrisisTypeId()), this.getName());
		dto.setNumberOfCrisisAssociated(this.getNumberOfCrisisAssociated());
		return dto;
	}

	public Integer getCrisisTypeId() {
		return crisisTypeId;
	}

	public void setCrisisTypeId(Integer crisisTypeId) {
		this.crisisTypeId = crisisTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfCrisisAssociated() {
		return numberOfCrisisAssociated;
	}

	public void setNumberOfCrisisAssociated(int numberOfCrisisAssociated) {
		this.numberOfCrisisAssociated = numberOfCrisisAssociated;
	}


}
