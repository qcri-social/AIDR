/**
 * 
 */
package qa.qcri.aidr.manager.hibernateEntities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import qa.qcri.aidr.manager.RoleType;

/**
 * @author Latika
 *
 */
@Entity
@Table(name="user_role")//, uniqueConstraints=@UniqueConstraint(name="user_role_unique_key", columnNames={"user_id", "role_type"}))
public class UserRole extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7176454786035441960L;

	@NotNull
	@ManyToOne
	private UserEntity user;
	
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name="role_type")
	private RoleType roleType;

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
}
