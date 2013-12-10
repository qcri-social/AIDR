package qa.qcri.aidr.trainer.api.template;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/31/13
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrisisApplicationListModel {

    private Long nominalAttributeID;
    private String name;
    private String url;
    private Integer remaining;
    private Integer totaltaskNumber;

    public CrisisApplicationListModel(Long nominalAttributeID, String name , String url, Integer remaining, Integer totaltaskNumber){
        this.nominalAttributeID = nominalAttributeID;
        this.name = name;
        this.url = url;
        this.remaining = remaining;
        this.totaltaskNumber = totaltaskNumber;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public Integer getTotaltaskNumber() {
        return totaltaskNumber;
    }

    public void setTotaltaskNumber(Integer totaltaskNumber) {
        this.totaltaskNumber = totaltaskNumber;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNominalAttributeID() {
        return nominalAttributeID;
    }

    public void setNominalAttributeID(Long nominalAttributeID) {
        this.nominalAttributeID = nominalAttributeID;
    }


}
