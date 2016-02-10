package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 1/18/14
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "report_template")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReportTemplate implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;


    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long reportTemplateID;

    @Column(name = "task_queue_id")
    private Long taskQueueID;

    @Column(name = "client_app_id")
    private Long clientAppID;

    @Column(name = "task_id", nullable = false)
    private Long taskID;

    @Column(name = "tweet_id", nullable = false)
    private String tweetID;

    @Column(name = "tweet", nullable = false)
    private String tweet;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "lat", nullable = false)
    private String lat;

    @Column(name = "lon", nullable = false)
    private String lon;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "created", nullable = false)
    private String created;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "status", nullable = false)
    private Integer status;

    public ReportTemplate(){}

    public Long getClientAppID() {
        return clientAppID;
    }

    public void setClientAppID(Long clientAppID) {
        this.clientAppID = clientAppID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getReportTemplateID() {
        return reportTemplateID;
    }

    public void setReportTemplateID(Long reportTemplateID) {
        this.reportTemplateID = reportTemplateID;
    }

    public Long getTaskQueueID() {
        return taskQueueID;
    }

    public void setTaskQueueID(Long taskQueueID) {
        this.taskQueueID = taskQueueID;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public String getTweetID() {
        return tweetID;
    }

    public void setTweetID(String tweetID) {
        this.tweetID = tweetID;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
