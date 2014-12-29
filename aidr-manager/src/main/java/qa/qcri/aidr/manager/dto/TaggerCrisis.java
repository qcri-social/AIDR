package qa.qcri.aidr.manager.dto;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;

public class TaggerCrisis {

	private String code;

	private String name;

	private TaggerCrisisType crisisType;

	private TaggerUser users;

	private Integer crisisID;

	private Boolean isTrashed;

	private List<TaggerModelFamilyCollection> modelFamilyCollection;

	public TaggerCrisis() {
		this.isTrashed = false;
	}

	public TaggerCrisis(Integer crisisID) {
		this.crisisID = crisisID;
		this.isTrashed = false;
	}

	public TaggerCrisis(String code, String name, TaggerCrisisType crisisType, TaggerUser users, Boolean isTrashed) {
		this.code = code;
		this.name = name;
		this.crisisType = crisisType;
		this.users = users;
		this.isTrashed = isTrashed;
	}

	public TaggerCrisis(CrisisDTO dto) throws Exception {
		if (dto != null) {
			this.setCode(dto.getCode());
			this.setName(dto.getName());
			this.setCrisisID(dto.getCrisisID().intValue());
			if (dto.getCrisisTypeDTO() != null) {
				this.setCrisisType(new TaggerCrisisType(dto.getCrisisTypeDTO()));
			}
			if (dto.getUsersDTO() != null) {
				this.setUsers(new TaggerUser(dto.getUsersDTO()));
			}
			this.setIsTrashed(dto.isIsTrashed());

			List<TaggerModelFamilyCollection> mfList = new ArrayList<TaggerModelFamilyCollection>();
			if (dto.getModelFamiliesDTO() != null) {
				for (ModelFamilyDTO mf: dto.getModelFamiliesDTO()) {
					mfList.add(new TaggerModelFamilyCollection(mf));
				}
				this.setModelFamilyCollection(mfList);
			}
		}
	}

	public CrisisDTO toDTO() throws Exception {
		CrisisDTO dto = new CrisisDTO();
		if (this.getCrisisID() != null) {
			dto.setCrisisID(new Long(this.getCrisisID()));
		}
		dto.setCode(this.getCode());
		dto.setName(this.getName());
		dto.setIsTrashed(this.getIsTrashed());
		if (this.getUsers() != null) {
			dto.setUsersDTO(this.getUsers().toDTO());
		}
		if (this.getCrisisType() != null) {
			dto.setCrisisTypeDTO(this.getCrisisType().toDTO());
		}

		List<ModelFamilyDTO> mfDTOList = new ArrayList<ModelFamilyDTO>();
		if (this.getModelFamilyCollection() != null) {
			for (TaggerModelFamilyCollection mf: this.getModelFamilyCollection()) {
				mfDTOList.add(mf.toDTO());
			}
			dto.setModelFamiliesDTO(mfDTOList);
		}
		return dto;
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

	public TaggerCrisisType getCrisisType() {
		return crisisType;
	}

	public void setCrisisType(TaggerCrisisType crisisType) {
		this.crisisType = crisisType;
	}

	public TaggerUser getUsers() {
		return users;
	}

	public void setUsers(TaggerUser users) {
		this.users = users;
	}

	public Integer getCrisisID() {
		return crisisID;
	}

	public void setCrisisID(Integer crisisID) {
		this.crisisID = crisisID;
	}

	public List<TaggerModelFamilyCollection> getModelFamilyCollection() {
		return modelFamilyCollection;
	}

	public void setModelFamilyCollection(List<TaggerModelFamilyCollection> modelFamilyCollection) {
		this.modelFamilyCollection = modelFamilyCollection;
	}

	public Boolean getIsTrashed() {
		return isTrashed;
	}

	public void setIsTrashed(Boolean isTrashed) {
		this.isTrashed = (isTrashed == null) ? false : isTrashed; 
	}

}