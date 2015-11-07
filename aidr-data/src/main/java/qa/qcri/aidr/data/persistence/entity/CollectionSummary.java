package qa.qcri.aidr.data.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class CollectionSummary extends BaseEntity {

	private static final long serialVersionUID = 1L;

    private String name;

    private String code;

	private Long totalCount;
	
    private Date startDate;

	private Date endDate;
	
    private String geo;

	private String curator;
	
    public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
    
    public String getCurator() {
        return curator;
    }

    public void setCurator(String curator) {
        this.name = curator;
    }
	
}
