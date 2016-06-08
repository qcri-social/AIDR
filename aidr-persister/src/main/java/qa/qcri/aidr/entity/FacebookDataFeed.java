package qa.qcri.aidr.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.JSONObject;

@Entity
@TypeDefs({ @TypeDef(name = "CustomJsonObject", typeClass = JSONObjectUserType.class) })
@Table(name="facebook_data_feed")
public class FacebookDataFeed extends BaseEntity {

	private static final long serialVersionUID = 5739273882912395843L;
	
	@Column(name="fb_id", length = 64)
    private String fb_id;
	
	@Column(name="code", length = 64)
    private String code;

	@Column(name="feed")
	@Type(type = "CustomJsonObject")
	private JSONObject feed;
	
	@Column(name="aidr")
	@Type(type = "CustomJsonObject")
	private JSONObject aidr;
	
	@Column(name="parent_type", length = 64)
    private String parentType;
	
	@Column(updatable = false, name = "feed_created_at")
	private Date feedCreatedAt;

	public String getFb_id() {
		return fb_id;
	}

	public void setFb_id(String fb_id) {
		this.fb_id = fb_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public JSONObject getFeed() {
		return feed;
	}

	public void setFeed(JSONObject feed) {
		this.feed = feed;
	}

	public JSONObject getAidr() {
		return aidr;
	}

	public void setAidr(JSONObject aidr) {
		this.aidr = aidr;
	}

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	public Date getFeedCreatedAt() {
		return feedCreatedAt;
	}

	public void setFeedCreatedAt(Date feedCreatedAt) {
		this.feedCreatedAt = feedCreatedAt;
	}
}
