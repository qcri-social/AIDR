package qa.qcri.aidr.trainer.pybossa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/18/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostURL() {
        return hostURL;
    }

    public void setHostURL(String hostURL) {
        this.hostURL = hostURL;
    }

    public String getHostAPIKey() {
        return hostAPIKey;
    }

    public void setHostAPIKey(String hostAPIKey) {
        this.hostAPIKey = hostAPIKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Long getAidrUserID() {
        return aidrUserID;
    }

    public void setAidrUserID(Long aidrUserID) {
        this.aidrUserID = aidrUserID;
    }

    public Set<ClientApp> getClientAppSet() {
        return clientAppSet;
    }

    public void setClientAppSet(Set<ClientApp> clientAppSet) {
        this.clientAppSet = clientAppSet;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getAidrHostURL() {
        return aidrHostURL;
    }

    public void setAidrHostURL(String aidrHostURL) {
        this.aidrHostURL = aidrHostURL;
    }

    public int getDefaultTaskRunsPerTask() {
        return defaultTaskRunsPerTask;
    }

    public void setDefaultTaskRunsPerTask(int defaultTaskRunsPerTask) {
        this.defaultTaskRunsPerTask = defaultTaskRunsPerTask;
    }


    @Id
    @Column(name = "clientID")
    private Long clientID;

    @Column (name = "aidrUserID", nullable = false)
    private Long aidrUserID;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "hostURL", nullable = false)
    private String hostURL;

    @Column (name = "hostAPIKey", nullable = false)
    private String hostAPIKey;

    @Column (name = "description", nullable = false)
    private String description;

    @Column (name = "queueSize", nullable = false)
    private Integer queueSize;

    @Column (name = "aidrHostURL", nullable = false)
    private String aidrHostURL;

    @Column (name = "created", nullable = false)
    private Date created;

    @Column (name = "defaultTaskRunsPerTask", nullable = false)
    private int defaultTaskRunsPerTask;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="clientID")
    private Set<ClientApp> clientAppSet;
}
