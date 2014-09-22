package qa.qcri.aidr.task.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author Koushik
 */
@Entity
@Table(catalog = "aidr_predict",name = "users")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Users implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "userID")
    private Long userID;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "role", nullable = false)
    private String role;

}
