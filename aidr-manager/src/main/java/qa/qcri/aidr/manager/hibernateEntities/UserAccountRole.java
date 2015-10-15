/**
 * 
 */
package qa.qcri.aidr.manager.hibernateEntities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author Latika
 *
 */
@Entity
@Table(name="account_role", uniqueConstraints=@UniqueConstraint(name="account_role_unique_key", columnNames={"account_id", "role_id"}))
public class UserAccountRole extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7176454786035441960L;

	@NotNull
	@ManyToOne
	private UserAccount account;
	
	@NotNull
	@ManyToOne
	private Role role;
	
	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
