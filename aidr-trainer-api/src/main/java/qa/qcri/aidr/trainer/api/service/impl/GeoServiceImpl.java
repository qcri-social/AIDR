package qa.qcri.aidr.trainer.api.service.impl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.ReportTemplateDao;
import qa.qcri.aidr.trainer.api.dao.TaskQueueResponseDao;
import qa.qcri.aidr.trainer.api.entity.ClientApp;
import qa.qcri.aidr.trainer.api.entity.ReportTemplate;
import qa.qcri.aidr.trainer.api.entity.TaskQueue;
import qa.qcri.aidr.trainer.api.entity.TaskQueueResponse;
import qa.qcri.aidr.trainer.api.service.ClientAppService;
import qa.qcri.aidr.trainer.api.service.GeoService;
import qa.qcri.aidr.trainer.api.service.TaskQueueService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;
import qa.qcri.aidr.trainer.api.template.GeoJsonOutputModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 1/18/14
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("geoService")
@Transactional(readOnly = true)
public class GeoServiceImpl implements GeoService {

    @Autowired
    ClientAppService clientAppService;

    @Autowired
    TaskQueueService taskQueueService;

    @Autowired
    ReportTemplateDao reportTemplateDao;

    @Autowired
    TaskQueueResponseDao taskQueueResponseDao;

    private JSONParser parser = new JSONParser();

    @Override
    public List<GeoJsonOutputModel> getGeoJsonOutput() throws Exception{
        List<GeoJsonOutputModel> geoJsonOutputModels =  new ArrayList<GeoJsonOutputModel>();
        List<ClientApp> clientApps =  clientAppService.findClientAppByAppType("appType",CodeLookUp.APP_MAP)  ;

        for(int i=0; i < clientApps.size(); i++){

            ClientApp clientApp = clientApps.get(i);
            List<TaskQueue> taskQueues = taskQueueService.getTaskQueueByClientAppStatus(clientApp.getClientAppID(), StatusCodeType.TASK_LIFECYCLE_COMPLETED);
            geoJsonOutputModels = processTaskQueue(taskQueues, geoJsonOutputModels, null);

        }

        return geoJsonOutputModels;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getGeoJsonOuputJSONP(Date updated) throws Exception {
        List<GeoJsonOutputModel> geoJsonOutputModels =  new ArrayList<GeoJsonOutputModel>();
        List<ClientApp> clientApps =  clientAppService.findClientAppByAppType("appType",CodeLookUp.APP_MAP)  ;

        for(int i=0; i < clientApps.size(); i++){

            ClientApp clientApp = clientApps.get(i);
            List<TaskQueue> taskQueues = taskQueueService.getTaskQueueByClientAppStatus(clientApp.getClientAppID(), StatusCodeType.TASK_LIFECYCLE_COMPLETED);
            geoJsonOutputModels = processTaskQueue(taskQueues, geoJsonOutputModels, updated);

        }

        JSONArray jsonArray = new JSONArray();
        for(GeoJsonOutputModel item : geoJsonOutputModels) {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("info", (JSONObject)parser.parse(item.getGeoJsonInfo()));

            jsonObject.put("features", (JSONObject)parser.parse(item.getGeoJson()));
            jsonArray.add(jsonObject);
        }

        String returnValue = "jsonp(" +  jsonArray.toJSONString() + ");";

        return returnValue;
    }


    private List<GeoJsonOutputModel> processTaskQueue(List<TaskQueue> taskQueues, List<GeoJsonOutputModel> geoJsonOutputModels, Date updated) throws ParseException {
        boolean isItOkToAdd = true;
        for (TaskQueue element : taskQueues) {
            isItOkToAdd = true;

            List<TaskQueueResponse> taskQueueResponses =  taskQueueResponseDao.getTaskQueueResponseByTaskQueueIDBasedOnLastUpdate(element.getTaskQueueID(), updated);

            if(taskQueueResponses.size() > 0){

                if(taskQueueResponses.get(0).getTaskInfo() != null){
                    if(!isEmptyGeoJson(taskQueueResponses.get(0).getResponse()) ){

                        List<ReportTemplate>templateList =  reportTemplateDao.getReportTemplateSearchBy("tweetID", taskQueueResponses.get(0).getTaskInfo());

                        if(taskQueueResponses.size() > 0 && templateList.size() > 0 ){
                            GeoJsonOutputModel model = new GeoJsonOutputModel(templateList.get(0), taskQueueResponses.get(0));
                            if(model.getGeoJson().contains("No Location Information")){
                                JSONArray newFeatures = new JSONArray();
                                JSONArray features  = (JSONArray)parser.parse(model.getGeoJson()) ;
                                for(int i =0; i < features.size(); i++){
                                    if(!features.get(i).equals("No Location Information")){
                                        newFeatures.add(features.get(i));
                                    }
                                }
                                if(newFeatures.size() > 0){
                                    model.setGeoJson(newFeatures.toJSONString());
                                }
                                else{
                                    isItOkToAdd = false;
                                }

                            }
                            if(isItOkToAdd){
                                geoJsonOutputModels.add( model );
                            }
                        }
                    }
                }
            }
        }

        return geoJsonOutputModels;
    }


    private boolean isEmptyGeoJson(String jsonString){
        boolean isEmpty = false;
        //geometry
        try{
            JSONObject geometry  = (JSONObject)parser.parse(jsonString) ;

            if(geometry.size() == 0){
                isEmpty = true;
            }

            JSONObject a = (JSONObject)geometry.get("geometry");

            if(a.size() == 0){
                isEmpty = true;
            }
        }
        catch(Exception e){
            isEmpty = true;
        }
        return isEmpty;
    }
}
