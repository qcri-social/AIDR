package qa.qcri.aidr.trainer.api.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImageTaskQueueDTO implements Serializable {

	private static final long serialVersionUID = -1204749826125824840L;

	private Long taskQueueId;

    private Long pybossaTaskId;
    
	private String imageUrl;
    
    private String imageText;
    
    private String category;
    
    private String latitude;
    
    private String longitude;
    
    private String location;
    
    private Long totalRows;

	public ImageTaskQueueDTO() {
	}

	public Long getTaskQueueId() {
		return taskQueueId;
	}

	public void setTaskQueueId(Long taskQueueId) {
		this.taskQueueId = taskQueueId;
	}

	public Long getPybossaTaskId() {
		return pybossaTaskId;
	}

	public void setPybossaTaskId(Long pybossaTaskId) {
		this.pybossaTaskId = pybossaTaskId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageText() {
		return imageText;
	}

	public void setImageText(String imageText) {
		this.imageText = imageText;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Long totalRows) {
		this.totalRows = totalRows;
	}

}
