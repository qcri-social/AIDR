package qa.qcri.aidr.trainer.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "image_task_queue")
public class ImageTaskQueue implements Serializable {

	private static final long serialVersionUID = 8313504282875796991L;

	@Id
    @GeneratedValue
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "task_queue_id")
    private TaskQueue taskQueue;

    @Column (name = "pybossa_task_id", nullable = false)
    private Long pybossaTaskId;
    
    @Column(name="image_url", columnDefinition="text")
	private String imageUrl;
    
    @Column(name="image_text", columnDefinition="text")
    private String imageText;
    
    @Column(name="category", length = 100)
    private String category;
    
    @Column(name="lat", length = 150)
    private String latitude;
    
    @Column(name="lon", length = 150)
    private String longitude;
    
    @Column(name="location", length = 250)
    private String location;

    public ImageTaskQueue(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TaskQueue getTaskQueue() {
		return taskQueue;
	}

	public void setTaskQueue(TaskQueue taskQueue) {
		this.taskQueue = taskQueue;
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

}
