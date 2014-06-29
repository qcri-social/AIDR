package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/23/14
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = "aidr_scheduler",name = "droneTracker")
public class DroneTracker  implements Serializable {
    private static final long serialVersionUID = -5527566248002296042L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column (name = "geojson", nullable = false)
    private String geojson;

    @Column (name = "videoURL", nullable = false)
    private String videoURL;

    @Column (name = "displayName", nullable = false)
    private String displayName;

    @Column (name = "status", nullable = false)
    private int status;

    @Column (name = "created", nullable = false)
    private Date created;

    public DroneTracker(){}

    public DroneTracker(String geoJson,String videoURL,String displayName){
        this.geojson = geoJson;
        this.videoURL = videoURL;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getGeojson() {
        return geojson;
    }

    public void setGeojson(String geojson) {
        this.geojson = geojson;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
