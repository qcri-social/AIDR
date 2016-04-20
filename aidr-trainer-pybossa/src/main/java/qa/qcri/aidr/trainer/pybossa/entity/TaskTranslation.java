package qa.qcri.aidr.trainer.pybossa.entity;

/**
 * Created by kamal on 3/22/15.
 */

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringEscapeUtils;


@Entity
@Table(name = "task_translation")
public class TaskTranslation implements Serializable{
	    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1486657246782287765L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique=true, nullable = false)
    private Long translationId;

    @Column(name = "task_id")
    private Long taskId;
    
    @Column(name = "client_app_id")
    private Long clientAppId;

    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "tweet_id", nullable = false)
    private String tweetID;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "lat", nullable = false)
    private String lat;

    @Column(name = "lon", nullable = false)
    private String lon;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "task_queue_id")
    private Long taskQueueID;

    @Column(name = "twb_order_id")
    private Long twbOrderId;
    
    @Column(name = "original_text")
    private String originalText;

    @Column(name = "translated_text")
    private String translatedText;
    
    @Column(name = "answer_code")
    private String answerCode;

    @Column(name = "created")
    private Date created;

    @Column(name = "status")
    private String status = STATUS_NEW;

    //change this to Enum?
    public static final String STATUS_NEW = "New";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_RECEIVED = "Received";
    public static final String STATUS_COMPLETE = "Complete";

    public TaskTranslation() {

    }

    public TaskTranslation(long clientAppId, long documentId,  String tweetID, String author, String originalText, String status) {
        this.clientAppId = clientAppId;
        this.tweetID = tweetID;
        this.author = (author==null)? "":author;
        this.originalText = originalText;
        this.status = status;
        this.documentId = documentId;
        this.lat = "";
        this.lon = "";
        this.url = "";
        this.setCreated(new Date());
        this.taskId = documentId;
        this.taskQueueID = documentId;
    }


    public TaskTranslation(Long taskId, Long clientAppId, String tweetID, String author, String lat, String lon, String url, Long taskQueueID, String originalText, String status) {
        this.taskId = taskId;
        this.clientAppId = clientAppId;
        this.tweetID = tweetID;
        this.author = (author==null)? "":author;
        this.lat = (lat==null)?"":lat;
        this.lon = (lon==null)?"":lon;
        this.url = (url==null)?"":url;
        this.taskQueueID = taskQueueID;
        this.originalText = originalText;
        this.status = status;
        this.setCreated(new Date());
    }


    public Long getTranslationId() {
		return translationId;
	}

	public void setTranslationId(Long translationId) {
		this.translationId = translationId;
	}

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
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
        return StringEscapeUtils.escapeCsv(originalText.replaceAll("[\r\n]", ""));
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

	public Long getClientAppId() {
		return clientAppId;
	}

	public void setClientAppId(Long clientAppId) {
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


    public Date getCreated() {
        return created;
    }

	public void setCreated(Date created) {
		this.created = created;
	}





}
