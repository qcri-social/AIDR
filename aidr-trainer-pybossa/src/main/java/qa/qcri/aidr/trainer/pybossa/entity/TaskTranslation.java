package qa.qcri.aidr.trainer.pybossa.entity;

/**
 * Created by kamal on 3/22/15.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "taskTranslation")
public class TaskTranslation {
	    
    @Id
    @GeneratedValue
    @Column(name = "translationID", unique=true, nullable = false)
    private Long translationId;

    @Column(name = "taskID")
    private Long taskId;
    
    @Column(name = "clientAppID")
    private String clientAppId;

    @Column(name = "tweetID", nullable = false)
    private String tweetID;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "lat", nullable = false)
    private String lat;

    @Column(name = "lon", nullable = false)
    private String lon;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "taskQueueID")
    private Long taskQueueID;

    @Column(name = "twbOrderID")
    private Long twbOrderId;
    
    @Column(name = "originalText")
    private String originalText;

    @Column(name = "translatedText")
    private String translatedText;
    
    @Column(name = "answerCode")
    private String answerCode;

    @Column(name = "created", nullable = false)
    private String created;

    @Column(name = "status")
    private String status = STATUS_NEW;

    //change this to Enum?
    public static final String STATUS_NEW = "New";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_RECEIVED = "Received";
    public static final String STATUS_COMPLETE = "Complete";

    public TaskTranslation() {

    }

    public TaskTranslation(Long taskId, String clientAppId, String tweetID, String author, String lat, String lon, String url, Long taskQueueID, String originalText, String status) {
        this.taskId = taskId;
        this.clientAppId = clientAppId;
        this.tweetID = tweetID;
        this.author = author;
        this.lat = lat;
        this.lon = lon;
        this.url = url;
        this.taskQueueID = taskQueueID;
        this.originalText = originalText;
        this.status = status;
    }


    public Long getTranslationId() {
		return translationId;
	}

	public void setTranslationId(Long translationId) {
		this.translationId = translationId;
	}


	public Long getTwbOrderId() {
		return twbOrderId;
	}

	public void setTwbOrderId(Long twbOrderId) {
		this.twbOrderId = twbOrderId;
	}

	public String getOriginalText() {
		return originalText;
	}

    public String getCSVFormattedOriginalText() {
        return originalText.replaceAll("[\r\n]", "");
    }

	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}

	public String getTranslatedText() {
		return translatedText;
	}

	public void setTranslatedText(String translatedText) {
		this.translatedText = translatedText;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getClientAppId() {
		return clientAppId;
	}

	public void setClientAppId(String clientAppId) {
		this.clientAppId = clientAppId;
	}

	public String getAnswerCode() {
		return answerCode;
	}

	public void setAnswerCode(String answerCode) {
		this.answerCode = answerCode;
	}


    public String getTweetID() {
        return tweetID;
    }

    public void setTweetID(String tweetID) {
        this.tweetID = tweetID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTaskQueueID() {
        return taskQueueID;
    }

    public void setTaskQueueID(Long taskQueueID) {
        this.taskQueueID = taskQueueID;
    }


    public String getCreated() {
        return created;
    }





}
