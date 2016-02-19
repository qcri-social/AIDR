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
 * Date: 10/20/13
 * Time: 1:10 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "client_app_answer")
public class ClientAppAnswer implements Serializable {
    private static final long serialVersionUID = -5527566248002296042L;

    @Id
    @Column(name = "client_app_id")
    private Long clientAppID;

    @Column (name = "answer", nullable = false)
    private String answer;

    @Column (name = "created", nullable = false)
    private Date created;

    public ClientAppAnswer(){}

    public ClientAppAnswer(Long clientAppID, String answer){
        this.clientAppID = clientAppID;
        this.answer = answer;

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
