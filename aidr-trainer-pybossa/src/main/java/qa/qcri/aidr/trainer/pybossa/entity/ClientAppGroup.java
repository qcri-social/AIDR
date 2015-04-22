package qa.qcri.aidr.trainer.pybossa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/16/15
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = "aidr_scheduler",name = "clientApp")
public class ClientAppGroup implements Serializable
{
    private static final long serialVersionUID = -5527566248002296042L;

    @Id
    @GeneratedValue
    @Column(name = "groupID")
    private Long groupID;

    @Column (name = "crisisID", nullable = true)
    private Long crisisID;

    @Column (name = "created", nullable = false)
    private Date created;

    public ClientAppGroup() {
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public Long getCrisisID() {
        return crisisID;
    }

    public void setCrisisID(Long crisisID) {
        this.crisisID = crisisID;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
