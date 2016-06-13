package qa.qcri.aidr.manager.dto;

public class CollectionUpdateInfo {
	
	private String code;
	private String name;
	private String follow;
	private String provider;
	private String crisisType;
	private String track;
	private String langFilters;
	private String durationHours;
	private String geo;
	private String geoR;
	private int fetchInterval;
	private int fetchFrom;
	
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
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getCrisisType() {
		return crisisType;
	}
	public void setCrisisType(String crisisType) {
		this.crisisType = crisisType;
	}
	public String getTrack() {
		return track;
	}
	public void setTrack(String track) {
		this.track = track;
	}
	public String getLangFilters() {
		return langFilters;
	}
	public void setLangFilters(String langFilters) {
		this.langFilters = langFilters;
	}
	public String getDurationHours() {
		return durationHours;
	}
	public void setDurationHours(String durationHours) {
		this.durationHours = durationHours;
	}
	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
	}
	public String getGeoR() {
		return geoR;
	}
	public void setGeoR(String geoR) {
		this.geoR = geoR;
	}
	public int getFetchInterval() {
		return fetchInterval;
	}
	public void setFetchInterval(int fetchInterval) {
		this.fetchInterval = fetchInterval;
	}
	public int getFetchFrom() {
		return fetchFrom;
	}
	public void setFetchFrom(int fetchFrom) {
		this.fetchFrom = fetchFrom;
	}
}
