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
@Table(name = "clientAppEvent")
public class ClientAppEvent implements Serializable {
    private static final long serialVersionUID = -5527566248002296042L;

    @Id
    @GeneratedValue
    @Column(name = "clientAppEventID")
    private Long clientAppEventID;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "clinetAppID", nullable = false)
    private Long clinetAppID;

    @Column (name = "sequence", nullable = false)
    private Integer sequence;

    @Column (name = "eventID", nullable = false)
    private Long eventID;

    @Column (name = "created", nullable = true)
    private Date created;


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

    public Long getClinetAppID() {
        return clinetAppID;
    }

    public void setClinetAppID(Long clinetAppID) {
        this.clinetAppID = clinetAppID;
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
