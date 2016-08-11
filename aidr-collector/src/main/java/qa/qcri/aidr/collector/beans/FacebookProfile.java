package qa.qcri.aidr.collector.beans;


public class FacebookProfile{
	
	private String id;
	private String link;
	private Integer fans;
	private String name;
	private String imageUrl;
	private FacebookEntityType type;
	
	public String getId() {
		return id;
	}
	public String getLink() {
		return link;
	}
	public Integer getFans() {
		return fans;
	}
	public String getName() {
		return name;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public void setFans(Integer fans) {
		this.fans = fans;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the type
	 */
	public FacebookEntityType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(FacebookEntityType type) {
		this.type = type;
	}
	
}
