package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;
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
@Table(name = "client_app_source")
public class ClientAppSource implements Serializable {
    private static final long serialVersionUID = -5527566248002296042L;

    @Id
    @GeneratedValue
    @Column (name = "id", nullable = false)
    private Long clientAppSourceID;

    @Column (name = "client_app_id", nullable = false)
    private Long clientAppID;

    @Column (name = "status", nullable = false)
    private Integer status;

    @Column (name = "source_url", nullable = false)
    private String sourceURL;

    @Column (name = "created", nullable = false)
    private Date created;

    public ClientAppSource(){}
    public ClientAppSource(Long clientAppID, Integer status, String sourceURL) {
        this.clientAppID = clientAppID;
        this.status = status;
        this.sourceURL = sourceURL;
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

    public Long getClientAppSourceID() {
        return clientAppSourceID;
    }

    public void setClientAppSourceID(Long clientAppSourceID) {
        this.clientAppSourceID = clientAppSourceID;
    }
}
