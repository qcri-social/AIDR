package qa.qcri.aidr.trainer.api.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.service.GeoService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 1/19/14
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("/geo")
@Component
public class GeoController {

    @Autowired
    GeoService geoService;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/JSONP/geoMap/qdate/{lastupdated}")
    public String getMapGeoJSONPBasedOnDate(@PathParam("lastupdated") String lastupdated) {
        System.out.print("updated : " + lastupdated);

        String returnValue = "";
        try {
            Date queryDate = null;
            if(!lastupdated.isEmpty() && lastupdated!= null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //String dateInString = "2014-01-26 13:44:48";
                queryDate = sdf.parse(lastupdated);
            }

            returnValue =  geoService.getGeoJsonOuputJSONP(queryDate);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return returnValue;
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/JSONP/geoMap")
    public String getMapGeoJSONP() {

        String returnValue = "";
        try {

            returnValue =  geoService.getGeoJsonOuputJSONP(null);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return returnValue;
    }

}
