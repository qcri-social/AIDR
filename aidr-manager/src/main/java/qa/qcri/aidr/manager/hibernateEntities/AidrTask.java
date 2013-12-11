package qa.qcri.aidr.manager.hibernateEntities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import qa.qcri.aidr.manager.util.JsonDateDeSerializer;
import qa.qcri.aidr.manager.util.JsonDateSerializer;

@Entity
@Table(name="AIDR_TASK")
public class AidrTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7649724170375576404L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String query;
	@Column(name="start_date")
	private Date startDate;
	@Column(name="end_date")
	private Date endDate;
	private Integer count;
	
	@ManyToOne(targetEntity=AidrCollection.class)
	@JoinTable(name="TASK_COLLECTION_MAPPING",joinColumns = {
      @JoinColumn(name="taskId", unique = true)           
    },
    inverseJoinColumns = {
      @JoinColumn(name="collectionId")
    }     
    )
	private AidrCollection jobId;
	
	private String status;
	
	private String follow;
	
    @Column(name="from_geo")
	private String fromGeo;
    
	@Column(name="to_geo")
    private String toGeo;
	
    @Column(name="created_date")
    private Date createdDate;

	public String getFromGeo() {
		return fromGeo;
	}
	public void setFromGeo(String fromGeo) {
		this.fromGeo = fromGeo;
	}
	public String getToGeo() {
		return toGeo;
	}
	public void setToGeo(String toGeo) {
		this.toGeo = toGeo;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	@JsonSerialize(using= JsonDateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}
	
	@JsonDeserialize(using=JsonDateDeSerializer.class)
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonSerialize(using= JsonDateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public AidrCollection getJobId() {
		return jobId;
	}
	public void setJobId(AidrCollection jobId) {
		this.jobId = jobId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	
	
}
