package qa.qcri.aidr.trainer.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


@Entity
@Table(catalog = "aidr_predict", name = "collection")
public class Collection implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	@Column(name = "id", unique = true, nullable = false)
	private Long crisisID;

	@Column(length = 64, unique = true)
    private String code;

    @Column(length = 255, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private Users owner;

    private Integer count;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @Column(name="publicly_listed")
    private boolean publiclyListed;
    
    @Column(name="last_document")
    private String lastDocument;
    
    @Column(name="classifier_enabled")
    private boolean classifierEnabled;
    
    @ManyToOne
    @JoinColumn(name="crisis_type")
    private CrisisType crisisType;
    
    @Column(name="duration_hours")
    private Integer durationHours;

    @Column(length = 5000, name = "track")
    private String track;

    @Column(length = 1000, name = "follow")
    private String follow;

    @Column(length = 1000, name = "geo")
    private String geo;
    
    @Column(name="geo_r")
    private String geoR;

    @Column(name="lang_filters")
    private String langFilters;

    @ManyToOne
    @JoinColumn(name="classifier_enabled_by")
	private Users classifierEnabledBy;
	
	@Column(name = "trashed", nullable = false)
	private boolean isTrashed;
	
	@Column(updatable = false, nullable=false, name = "created_at")
	private Timestamp createdAt;
	
	@Column(nullable=false, name = "updated_at")
	private Timestamp updatedAt;

    @Column (name = "micromappers_enabled", nullable = false)
    private Boolean isMicromapperEnabled;
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="crisisID")
    private Set<ModelFamily> modelFamilySet;

    private String provider;
	private Integer status;
	
    public Boolean getIsMicromapperEnabled() {
		return isMicromapperEnabled;
	}

	public void setIsMicromapperEnabled(Boolean isMicromapperEnabled) {
		this.isMicromapperEnabled = isMicromapperEnabled;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = new Timestamp(System.currentTimeMillis());
	}
	
	@PrePersist
    public void onCreate() {
		updatedAt = new Timestamp(System.currentTimeMillis());
		createdAt = updatedAt;
    }

	public Collection(){}

    public Collection(Long crisisID, String name, String code, boolean isTrashed){
        this.crisisID = crisisID;
        this.name = name;
        this.code = code;
        this.isTrashed = isTrashed;
    }
    
    public Collection(Long crisisID, String name, String code, boolean isTrashed, boolean isMicromapperEnabled){
        this.crisisID = crisisID;
        this.name = name;
        this.code = code;
        this.isTrashed = isTrashed;
        this.isMicromapperEnabled = isMicromapperEnabled;
    }

	public Users getClassifierEnabledBy() {
		return this.classifierEnabledBy;
	}

	public void setClassifierEnabledBy(Users classifierEnabledBy) {
		this.classifierEnabledBy = classifierEnabledBy;
	}

	
	public CrisisType getCrisisType() {
		return this.crisisType;
	}

	public void setCrisisType(CrisisType crisisType) {
		this.crisisType = crisisType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Users getOwner() {
		return owner;
	}

	public void setOwner(Users owner) {
		this.owner = owner;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
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

	public boolean isPubliclyListed() {
		return publiclyListed;
	}

	public void setPubliclyListed(boolean publiclyListed) {
		this.publiclyListed = publiclyListed;
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

	public Integer getDurationHours() {
		return durationHours;
	}

	public void setDurationHours(Integer durationHours) {
		this.durationHours = durationHours;
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

	public boolean isMicromapperEnabled() {
		return isMicromapperEnabled;
	}

	public void setMicromapperEnabled(boolean isMicromapperEnabled) {
		this.isMicromapperEnabled = isMicromapperEnabled;
	}

	public boolean isTrashed() {
		return isTrashed;
	}

	public void setTrashed(boolean isTrashed) {
		this.isTrashed = isTrashed;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isIsTrashed() {
		return this.isTrashed;
	}

	public void setIsTrashed(boolean isTrashed) {
		this.isTrashed = isTrashed;
	}
	
	public boolean isIsMicromapperEnabled() {
		return isMicromapperEnabled;
	}

	public void setIsMicromapperEnabled(boolean isMicromapperEnabled) {
		this.isMicromapperEnabled = isMicromapperEnabled;
	}
	
    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
    }

    public Boolean getTrashed() {
        return isTrashed;
    }

    public void setTrashed(Boolean trashed) {
        isTrashed = trashed;
    }

    public Set<ModelFamily> getModelFamilySet() {
        return modelFamilySet;
    }

    public void setModelFamilySet(Set<ModelFamily> modelFamilySet) {
        this.modelFamilySet = modelFamilySet;
    }
    
    public Boolean getMicromapperEnabled() {
		return isMicromapperEnabled;
	}

	public void setMicromapperEnabled(Boolean isMicromapperEnabled) {
		this.isMicromapperEnabled = isMicromapperEnabled;
	}

}

