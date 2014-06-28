package qa.qcri.aidr.trainer.pybossa.service;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import qa.qcri.aidr.trainer.pybossa.entity.TaskLog;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;
import qa.qcri.aidr.trainer.pybossa.util.DataFormatValidator;
import qa.qcri.aidr.trainer.pybossa.util.LatLngUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/29/13
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class ClientAppRunWorkerTest {

   // @Autowired
   // private ClientAppRunWorker clientAppRunWorker;

    @Test
    public void testProcessTaskPublish() throws Exception {
       // clientAppRunWorker.processTaskRunImport();
        //clientAppRunWorker.processTaskPublish();

    }


    public void testGeoCalculation() throws Exception {

        //121.07551516934, 14.298073550000003
        double lat1 = 14.298073550000003;
        double lon1 = 121.07551516934;
        //120.9370878, 14.327082399999979
        double lat2 = 14.327082399999979;
        double lon2 = 120.9370878;
        //121.07551516934, 14.298073550000003
        double lat3 = 14.298073550000003;
        double lon3 = 121.07551516934;


        // new york
        //double lat1 = 40.7143528;
        //double lon1 = -74.0059731;
        // chicago
        //double lat2 = 41.8781136;
        //double lon2 = -87.6297982;
        // Atlanta
        //double lat3 = 33.7489954;
        //double lon3 = -84.3879824;

        double results[] = new double[2];

        LatLngUtils.geoMidPointFor3Points(lat1, lon1,lat2,  lon2, lat3,  lon3, results);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coordinates", Arrays.toString(results));
        jsonObject.put("type", "Point");

        JSONObject geoResponse = new JSONObject();
        geoResponse.put("geometry", jsonObject);


        System.out.println(geoResponse.toJSONString());

        double[] distance = new double[3];
        double[] distanceInMile= new double[1];

        double lon = results[0];
        double lat = results[1];


        LatLngUtils.computeDistanceInMile(lat, lon, lat1, lon1, distanceInMile);
        distance[0] = distanceInMile[0];

        LatLngUtils.computeDistanceInMile(lat, lon, lat2, lon2, distanceInMile);
        distance[1] = distanceInMile[0];

        LatLngUtils.computeDistanceInMile(lat, lon, lat3, lon3, distanceInMile);
        distance[2] = distanceInMile[0];

        Arrays.sort(distance) ;

        double maxDistance = distance[2];

        System.out.println("maxDistance : " + maxDistance);
    }
}
