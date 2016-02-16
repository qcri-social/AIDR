package qa.qcri.aidr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.JSONObject;

@Entity
@TypeDefs({ @TypeDef(name = "CustomJsonObject", typeClass = JSONObjectUserType.class) })
@Table(name="data_feed")
public class DataFeed  extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5739273882912395843L;
	
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
	
	@Column(name="place")
	@Type(type = "CustomJsonObject")
	private JSONObject place;

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

	public JSONObject getGeo() {
		return geo;
	}

	public void setGeo(JSONObject geo) {
		this.geo = geo;
	}

	public JSONObject getPlace() {
		return place;
	}

	public void setPlace(JSONObject place) {
		this.place = place;
	}

}
