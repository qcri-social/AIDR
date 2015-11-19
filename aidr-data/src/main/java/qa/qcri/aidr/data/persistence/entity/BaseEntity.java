package qa.qcri.aidr.data.persistence.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * @author Latika
 *
 */
@MappedSuperclass
public class BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9015978098898586703L;
	@Id @GeneratedValue(strategy=GenerationType.AUTO) 
	protected Long id;
	
	@Column(updatable = false, nullable=false, name = "created_at")
	private Timestamp createdAt;
	
	@Column(nullable=false, name = "updated_at")
	private Timestamp updatedAt;
	
	@PreUpdate
	protected void onUpdate() {
		updatedAt = new Timestamp(System.currentTimeMillis());
	}
	
	@PrePersist
    public void onCreate() {
		updatedAt = new Timestamp(System.currentTimeMillis());
		createdAt = updatedAt;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}
