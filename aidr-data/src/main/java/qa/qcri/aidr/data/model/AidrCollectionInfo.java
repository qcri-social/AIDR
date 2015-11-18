package qa.qcri.aidr.data.model;

import java.util.Date;

public class AidrCollectionInfo {

	private String name;

    private String code;

	private Long total_count;

    private Date startDate;

	private Date endDate;

    private String geo;

	private String curator;

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

	public Long getTotalCount() {
		return total_count;
	}
	
	public void setTotalCount(Long total_count) {
		this.total_count = total_count;
	}
	
    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }
    
	//@JsonSerialize(using = JsonDateSerializer.class)
    public Date getStartDate() {
        return startDate;
    }

   // @JsonDeserialize(using = JsonDateDeSerializer.class)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
	//@JsonSerialize(using = JsonDateSerializer.class)
    public Date getEndDate() {
        return endDate;
    }

   // @JsonDeserialize(using = JsonDateDeSerializer.class)
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public String getcurator() {
        return curator;
    }

    public void setcurator(String curator) {
        this.name = curator;
    }
    
}
