package qa.qcri.aidr.trainer.pybossa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 12/4/13
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "client_app_event")
public class ClientAppEvent implements Serializable {
    private static final long serialVersionUID = -5527566248002296042L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long clientAppEventID;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "client_app_id", nullable = false)
    private Long clientAppID;

    @Column (name = "sequence", nullable = false)
    private Integer sequence;

    @Column (name = "event_id", nullable = false)
    private Long eventID;

    @Column (name = "created", nullable = true)
    private Date created;

    public Long getClientAppID() {
        return clientAppID;
    }

    public void setClientAppID(Long clientAppID) {
        this.clientAppID = clientAppID;
    }

    public Long getClientAppEventID() {
        return clientAppEventID;
    }

    public void setClientAppEventID(Long clientAppEventID) {
        this.clientAppEventID = clientAppEventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }


}
