package qa.qcri.aidr.manager.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "collection_log")
public class CollectionLog extends BaseEntity {

    private static final long serialVersionUID = 4720813686204397970L;

    public CollectionLog() {}
    
	public CollectionLog(Collection collection) {
		super();
		this.collectionId = collection.getId();
		this.count = collection.getCount();
		this.startDate = collection.getStartDate();
		this.endDate = collection.getEndDate();
		this.geo = collection.getGeo();
		this.track = collection.getTrack();
		this.langFilters = collection.getLangFilters();
		this.follow = collection.getFollow();
	}

    @Column(name="collection_id")
    private Long collectionId;
    
    private Integer count;
    
    @Column(length = 5000, name = "track")
    private String track;
    
    @Column(length=1000, name="follow")
    private String follow;
    
    @Column(length=1000, name="geo")
    private String geo;
    
    @Column(name="lang_filters")
    private String langFilters;
    
    @Column(name="start_date")
    private Date startDate;
    
    @Column(name="end_date")
    private Date endDate;
    
    @Column(name="updated_by")
    private Long updatedBy;

    public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    /**
     * @return the langFilter
     */
    public String getLangFilters() {
        return langFilters;
    }

    /**
     * @param langFilter the langFilter to set
     */
    public void setLangFilters(String langFilter) {
        this.langFilters = langFilter;
    }

    /**
     * @return the collectionId
     */
    public Long getCollectionId() {
        return collectionId;
    }

    /**
     * @param collectionId the collectionId to set
     */
    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }
}
