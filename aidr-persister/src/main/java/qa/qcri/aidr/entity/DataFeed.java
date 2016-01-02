package qa.qcri.aidr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.JSONObject;

/*import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.JSONObject;
*/

@Entity
@TypeDefs({ @TypeDef(name = "CustomJsonObject", typeClass = JSONObjectUserType.class) })
@Table(name="data_feed")
public class DataFeed  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5739273882912395843L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="code", length = 64)
    private String code;
	
	@Column(name="source", length = 45)
    private String source;
	
	@Column(name="feed")
	@Type(type = "CustomJsonObject")
	private JSONObject feed;
	
	@Column(name="aidr")
	@Type(type = "CustomJsonObject")
	private JSONObject aidr;
	
	@Column(name="geo")
	@Type(type = "CustomJsonObject")
	private JSONObject geo;
	
	@Column(updatable = false, nullable=false, name = "created_at")
	private Date createdAt;
	
	@Column(nullable=false, name = "updated_at")
	private Date updatedAt;
	
	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getSource() {
		return source;
	}

	public JSONObject getFeed() {
		return feed;
	}

	public JSONObject getAidr() {
		return aidr;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setFeed(JSONObject feed) {
		this.feed = feed;
	}

	public void setAidr(JSONObject aidr) {
		this.aidr = aidr;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public JSONObject getGeo() {
		return geo;
	}

	public void setGeo(JSONObject geo) {
		this.geo = geo;
	}

}
