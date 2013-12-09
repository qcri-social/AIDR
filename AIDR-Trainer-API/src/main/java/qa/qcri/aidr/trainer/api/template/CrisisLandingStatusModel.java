package qa.qcri.aidr.trainer.api.template;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/5/13
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrisisLandingStatusModel {

    public CrisisLandingStatusModel(String url, String status, String message){
        this.url = url;
        this.status = status;
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    private String url;
    private String message;
    private String status;

}
