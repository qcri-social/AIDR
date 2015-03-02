package qa.qcri.aidr.manager.hibernateEntities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import qa.qcri.aidr.manager.util.CollectionStatus;
import qa.qcri.aidr.manager.util.JsonDateDeSerializer;
import qa.qcri.aidr.manager.util.JsonDateSerializer;

import javax.persistence.ManyToOne;

@Entity
@Table(name = "AIDR_COLLECTION_LOG")
@JsonIgnoreProperties(ignoreUnknown=true)
public class AidrCollectionLog implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4720813686204397970L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //@ManyToOne(targetEntity = AidrCollection.class, cascade = CascadeType.ALL)
    //@JoinColumn(name = "collection_id", referencedColumnName = "id")
    //private AidrCollection collection;
    private Integer collectionID;
    private Integer count;
    
    @Column(length = 1000, name = "track")
    private String track;
    
    @Column(length=1000, name="follow")
    private String follow;
    
    @Column(length=1000, name="geo")
    private String geo;
    
    private String langFilters;
    private Date startDate;
    private Date endDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getStartDate() {
        return startDate;
    }

    @JsonDeserialize(using = JsonDateDeSerializer.class)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndDate() {
        return endDate;
    }

    @JsonDeserialize(using = JsonDateDeSerializer.class)
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
    

//    public AidrCollection getCollection() {
//        return collection;
//    }
//
//    /**
//     * @param collection the collection to set
//     */
//    public void setCollection(AidrCollection collection) {
//        this.collection = collection;
//    }

    /**
     * @return the collectionID
     */
    public Integer getCollectionID() {
        return collectionID;
    }

    /**
     * @param collectionID the collectionID to set
     */
    public void setCollectionID(Integer collectionID) {
        this.collectionID = collectionID;
    }
}
