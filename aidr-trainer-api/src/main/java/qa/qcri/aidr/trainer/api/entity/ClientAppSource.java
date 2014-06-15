package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/18/13
 * Time: 2:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = "aidr_scheduler",name = "clientAppSource")
public class ClientAppSource implements Serializable {
    private static final long serialVersionUID = -5527566248002296042L;

    public ClientAppSource(){}

    public ClientAppSource(Long clientAppID) {
        this.clientAppID = clientAppID;

    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getClientAppID() {
        return clientAppID;
    }

    public void setClientAppID(Long clientAppID) {
        this.clientAppID = clientAppID;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ClientAppSource(Long clientAppID, Integer status, String sourceURL) {
        this.clientAppID = clientAppID;
        this.status = status;
        this.sourceURL = sourceURL;
    }

    @Id
    @Column (name = "clientAppID", nullable = false)
    private Long clientAppID;

    @Column (name = "status", nullable = false)
    private Integer status;

    @Column (name = "sourceURL", nullable = false)
    private String sourceURL;

    @Column (name = "created", nullable = false)
    private Date created;


}
