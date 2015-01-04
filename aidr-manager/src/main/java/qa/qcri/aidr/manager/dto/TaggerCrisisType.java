package qa.qcri.aidr.manager.dto;

import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;

public class TaggerCrisisType {

	private Integer crisisTypeID;

	private String name;

	private int numberOfCrisisAssociated;

	public TaggerCrisisType() {
	}

	public TaggerCrisisType(Integer crisisTypeId) {
		this.crisisTypeID = crisisTypeId;
	}

	public TaggerCrisisType(Integer crisisTypeId, String name) {
		this.crisisTypeID = crisisTypeId;
		this.name = name;
	}

	public TaggerCrisisType(CrisisTypeDTO dto) throws Exception {
		if (dto != null) {
			this.setCrisisTypeID(dto.getCrisisTypeId().intValue());
			this.setName(dto.getName());
			this.setNumberOfCrisisAssociated(dto.getNumberOfCrisisAssociated());
		}
	}

	public CrisisTypeDTO toDTO() throws Exception {
		CrisisTypeDTO dto = new CrisisTypeDTO(new Long(this.getCrisisTypeID()), this.getName());
		dto.setNumberOfCrisisAssociated(this.getNumberOfCrisisAssociated());
		return dto;
	}

	public Integer getCrisisTypeID() {
		return crisisTypeID;
	}

	public void setCrisisTypeID(Integer crisisTypeId) {
		this.crisisTypeID = crisisTypeId;
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
