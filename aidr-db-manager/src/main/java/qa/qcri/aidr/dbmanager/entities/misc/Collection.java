/**
 * Implements the entity corresponding to the collection table in the aidr_predict DB
 * 
 * @author Koushik
 */
package qa.qcri.aidr.dbmanager.entities.misc;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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

import org.hibernate.Hibernate;

import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.task.Document;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "collection")
public class Collection implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7692349620189189978L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	@Column(name = "id", unique = true, nullable = false)
	private Long crisisId;

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
    
    @Column(name="micromappers_enabled")
    private boolean isMicromapperEnabled;
    
    @Column(name="usage_type")
    private String usageType;

    @ManyToOne
    @JoinColumn(name="classifier_enabled_by")
	private Users users;
	
	@Column(name = "trashed", nullable = false)
	private boolean isTrashed;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "collection")
	@JsonManagedReference
	private List<Document> documents;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "collection")
	@JsonManagedReference
	private List<ModelFamily> modelFamilies;

	
	@Column(updatable = false, nullable=false, name = "created_at")
	private Timestamp createdAt;
	
	@Column(nullable=false, name = "updated_at")
	private Timestamp updatedAt;
	
    private String provider;
    
	private Integer status;
	
    @Column(name="save_media_enabled", columnDefinition="bit default 0")
    private boolean saveMediaEnabled;
	
	@PreUpdate
	protected void onUpdate() {
		updatedAt = new Timestamp(System.currentTimeMillis());
	}
	
	@PrePersist
    public void onCreate() {
		updatedAt = new Timestamp(System.currentTimeMillis());
		createdAt = updatedAt;
    }

	
	public Collection(Users users, CrisisType crisisType, String name, String code,
			boolean isTrashed, boolean isMicromapperEnabled) {
		this.users = users;
		this.crisisType = crisisType;
		this.name = name;
		this.code = code;
		this.isTrashed = isTrashed;
		this.isMicromapperEnabled = isMicromapperEnabled;
	}

	public Collection(Long crisisID, String name, String code, boolean isTrashed){
        this.crisisId = crisisID;
        this.name = name;
        this.code = code;
        this.isTrashed = isTrashed;
    }
	
	public Collection(Users users, CrisisType crisisType, String name, String code,
			boolean isTrashed, boolean isMicromapperEnabled, List<Document> documents,
			List<ModelFamily> modelFamilies) {
		this.users = users;
		this.crisisType = crisisType;
		this.name = name;
		this.code = code;
		this.isTrashed = isTrashed;
		this.isMicromapperEnabled = isMicromapperEnabled;
		this.documents = documents;
		this.modelFamilies = modelFamilies;
	}


	public Collection() {
		// TODO Auto-generated constructor stub
	}

	public Long getCrisisId() {
		return this.crisisId;
	}

	public void setCrisisId(Long crisisId) {
		this.crisisId = crisisId;
	}

	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
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
	
	public List<Document> getDocuments() {
		return this.documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public List<ModelFamily> getModelFamilies() {
		return this.modelFamilies;
	}

	public void setModelFamilies(List<ModelFamily> modelFamilies) {
		this.modelFamilies = modelFamilies;
	}
	
	public boolean hasUsers() {
		return Hibernate.isInitialized(this.users);
	}
	
	public boolean hasCrisisType() {
		return Hibernate.isInitialized(this.crisisType);
	}
	
	public boolean hasDocuments() {
		//return ((PersistentList) this.documents).wasInitialized();
		return Hibernate.isInitialized(this.documents);
	}
	
	public boolean hasModelFamilies() {
		//return ((PersistentList) this.modelFamilies).wasInitialized();
		return Hibernate.isInitialized(this.modelFamilies);
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

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public boolean isSaveMediaEnabled() {
		return saveMediaEnabled;
	}

	public void setSaveMediaEnabled(boolean saveMediaEnabled) {
		this.saveMediaEnabled = saveMediaEnabled;
	}

}
