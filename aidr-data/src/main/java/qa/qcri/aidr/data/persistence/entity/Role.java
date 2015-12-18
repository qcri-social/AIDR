package qa.qcri.aidr.data.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import qa.qcri.aidr.data.RoleType;

@Entity
@Table(name="role")
public class Role extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	@Column(name="level", nullable=false, unique=true)
	private RoleType roleType;

	private String description;
	
	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
