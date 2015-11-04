/**
 * 
 */
package qa.qcri.aidr.manager.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import qa.qcri.aidr.manager.hibernateEntities.BaseEntity;
import qa.qcri.aidr.manager.hibernateEntities.UserAccount;

/**
 * @author Latika
 *
 */
@Entity
@Table(name="collection_collaborator", uniqueConstraints = @UniqueConstraint(name="collection_collaborator_unique_key", columnNames={"collection_id", "account_id"}))
public class CollectionCollaborator extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6164545572667938026L;

	@ManyToOne
	private Collection collection;
	
	@ManyToOne
	private UserAccount account;
	public Collection getCollection() {
		return collection;
	}
	public void setCollection(Collection collection) {
		this.collection = collection;
	}
	public UserAccount getAccount() {
		return account;
	}
	public void setAccount(UserAccount account) {
		this.account = account;
	}
	
}
