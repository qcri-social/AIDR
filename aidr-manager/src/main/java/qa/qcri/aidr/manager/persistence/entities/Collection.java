package qa.qcri.aidr.manager.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import qa.qcri.aidr.manager.util.CollectionStatus;
import qa.qcri.aidr.manager.util.CollectionType;

@Entity
@Table(name = "collection")
public class Collection extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(length = 64, unique = true)
    private String code;

    @Column(length = 255, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable=false)
    private UserAccount owner;

    private Integer count;

    private CollectionStatus status;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @Column(name="publicly_listed")
    private boolean publiclyListed;
    
    @Column(length = 1024, name="last_document")
    private String lastDocument;
    
    @Column(name="classifier_enabled")
    private boolean classifierEnabled;
    
    @ManyToOne
    @JoinColumn(name="crisis_type")
    private CrisisType crisisType;
    
    @Enumerated(EnumType.STRING)
    private CollectionType provider;

    @Column(name="duration_hours")
    private Integer durationHours;

    @Column(name="trashed")
    private boolean trashed;

    @Column(length = 5000, name = "track")
    private String track;

    @Column(length = 1000, name = "follow")
    private String follow;

    @Column(length = 1000, name = "geo")
    private String geo;
    
    @Column(name="geo_r")
    private String geoR;

    @Column(length = 1000, name = "purpose")
    private String purpose;
    
    @Column(name="lang_filters")
    private String langFilters;
    
    @Column(name="micromappers_enabled")
    private boolean micromappersEnabled;

    @ManyToOne
    @JoinColumn(name="classifier_enabled_by")
    private UserAccount classifierEnabledBy;
    
    @Column(name="usage_type")
    private String usageType;
    
    @Column(name="save_media_enabled", columnDefinition="bit default 0")
    private boolean saveMediaEnabled;
    
    @Transient
    private boolean isSourceOutage = false;

    public boolean isPubliclyListed() {
		return publiclyListed;
	}

	public void setPubliclyListed(boolean publiclyListed) {
		this.publiclyListed = publiclyListed;
	}

	public CrisisType getCrisisType() {
		return crisisType;
	}

	public void setCrisisType(CrisisType crisisType) {
		this.crisisType = crisisType;
	}

	public UserAccount getClassifierEnabledBy() {
		return classifierEnabledBy;
	}

	public void setClassifierEnabledBy(UserAccount classifierEnabledBy) {
		this.classifierEnabledBy = classifierEnabledBy;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getFollow() {
		return follow;
	}

	public void setFollow(String follow) {
		this.follow = follow;
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

	public String getLangFilters() {
		return langFilters;
	}

	public void setLangFilters(String langFilters) {
		this.langFilters = langFilters;
	}

	public boolean isMicromappersEnabled() {
		return micromappersEnabled;
	}

	public void setMicromappersEnabled(boolean micromappersEnabled) {
		this.micromappersEnabled = micromappersEnabled;
	}
	
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

	public UserAccount getOwner() {
		return owner;
	}

	public void setOwner(UserAccount owner) {
		this.owner = owner;
	}

	public Integer getCount() {
		return count;
	}

	public Integer getDurationHours() {
		return durationHours;
	}

	public void setDurationHours(Integer durationHours) {
		this.durationHours = durationHours;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public CollectionStatus getStatus() {
		return status;
	}

	public void setStatus(CollectionStatus status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLastDocument() {
		return lastDocument;
	}

	public void setLastDocument(String lastDocument) {
		this.lastDocument = lastDocument;
	}

	public boolean isClassifierEnabled() {
		return classifierEnabled;
	}

	public void setClassifierEnabled(boolean classifierEnabled) {
		this.classifierEnabled = classifierEnabled;
	}


	public CollectionType getProvider() {
		return provider;
	}

	public void setProvider(CollectionType provider) {
		this.provider = provider;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}	

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public boolean isSourceOutage() {
		return isSourceOutage;
	}

	public void setSourceOutage(boolean isSourceOutage) {
		this.isSourceOutage = isSourceOutage;
	}

	public boolean isSaveMediaEnabled() {
		return saveMediaEnabled;
	}

	public void setSaveMediaEnabled(boolean saveMediaEnabled) {
		this.saveMediaEnabled = saveMediaEnabled;
	}
	
}