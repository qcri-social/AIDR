package qa.qcri.aidr.trainer.api.template;

import org.json.simple.JSONObject;
import qa.qcri.aidr.trainer.api.entity.ReportTemplate;
import qa.qcri.aidr.trainer.api.entity.TaskQueueResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 1/18/14
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoJsonOutputModel {

    private String geoJsonInfo;
    private String geoJson;

    public GeoJsonOutputModel( ReportTemplate reportTemplate, TaskQueueResponse taskQueueResponse){

        this.geoJson= taskQueueResponse.getResponse();
        this.setGeoJsonInfo(reportTemplate, taskQueueResponse);
    }

    public String getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(String geoJson) {
        this.geoJson = geoJson;
    }

    public void setGeoJsonInfo(ReportTemplate reportTemplate, TaskQueueResponse taskQueueResponse) {
        JSONObject js = new JSONObject();

        js.put("tweet", reportTemplate.getTweet());
        js.put("author", reportTemplate.getAuthor());
        js.put("url", reportTemplate.getUrl());
        js.put("created", reportTemplate.getCreated());
        js.put("answer", reportTemplate.getAnswer());
        js.put("tweetID", reportTemplate.getTweetID());
        js.put("updated", taskQueueResponse.getCreated().toString());

       this.geoJsonInfo = js.toJSONString();
    }


    public String getGeoJsonInfo(){
        return geoJsonInfo;
    }

}
