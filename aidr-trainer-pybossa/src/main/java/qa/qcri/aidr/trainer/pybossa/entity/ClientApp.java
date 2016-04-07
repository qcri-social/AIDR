package qa.qcri.aidr.trainer.pybossa.entity;

import qa.qcri.aidr.trainer.pybossa.store.LookupCode;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/18/13
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "client_app")
public class ClientApp implements Serializable {
    private static final long serialVersionUID = -5527566248002296042L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long clientAppID;

    @Column (name = "client_id", nullable = false)
    private Long clientID;

    @Column (name = "crisis_id", nullable = false)
    private Long crisisID;

    @Column (name = "nominal_attribute_id", nullable = false)
    private Long nominalAttributeID;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "description", nullable = false)
    private String description;

    @Column (name = "platform_app_id", nullable = false)
    private Long platformAppID;

    @Column (name = "short_name", nullable = false)
    private String shortName;

    @Column (name = "task_run_per_task", nullable = false)
    private Integer taskRunsPerTask;

    @Column (name = "quorum", nullable = false)
    private Integer quorum;

    @Column (name = "icon_url", nullable = true)
    private String iconURL;

    @Column (name = "status", nullable = false)
    private Integer status;

    @Column (name = "created", nullable = false)
    private Date created;

    @Column (name = "app_type", nullable = false)
    private Integer appType;

    @Column (name = "tc_project_id", nullable = true)
    private Long tcProjectId;


    @ManyToOne
    @JoinColumn(name="client_id" ,nullable = false, insertable = false, updatable = false)
    private Client client;


    public ClientApp(){
    }

    public ClientApp(Long clientID, Long crisisID, String name, String description, Long platformAppID, String shortName, Long nominalAttributeID, int taskRunsPerTask, int appType){

        this.clientID = clientID;
        this.crisisID = crisisID;
        this.name = name;
        this.description = description;
        this.platformAppID =  platformAppID;
        this.shortName = shortName;
        this.nominalAttributeID = nominalAttributeID;
        this.quorum = LookupCode.AIDR_ONLY;
        this.status = LookupCode.AIDR_ONLY;
        this.taskRunsPerTask = taskRunsPerTask;
        this.appType = appType;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getClientAppID() {
        return clientAppID;
    }

    public void setClientAppID(Long clientAppID) {
        this.clientAppID = clientAppID;
    }


    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPlatformAppID() {
        return platformAppID;
    }

    public void setPlatformAppID(Long platformAppID) {
        this.platformAppID = platformAppID;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getTaskRunsPerTask() {
        return taskRunsPerTask;
    }

    public void setTaskRunsPerTask(Integer taskRunsPerTask) {
        this.taskRunsPerTask = taskRunsPerTask;
    }

    public Integer getQuorum() {
        return quorum;
    }

    public void setQuorum(Integer quorum) {
        this.quorum = quorum;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getNominalAttributeID() {
        return nominalAttributeID;
    }

    public void setNominalAttributeID(Long nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }

    public Long getTcProjectId() { return tcProjectId;}
}
