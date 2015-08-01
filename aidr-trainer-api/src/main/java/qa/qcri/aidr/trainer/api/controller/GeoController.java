package qa.qcri.aidr.trainer.api.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.trainer.api.service.GeoService;

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
    
    private static Logger logger = Logger.getLogger(GeoController.class);
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.WILDCARD})
    @Path("/JSON/geoMap/qdate/{lastupdated}")
    public String getMapGeoJSONBasedOnDate(@PathParam("lastupdated") String lastupdated) {
        ///System.out.print("updated : " + lastupdated);
        String requestedDate = null;
        String returnValue = "";


        try {
            Date queryDate = null;
            if(!lastupdated.isEmpty() && lastupdated!= null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //String dateInString = "2014-01-26 13:44:48";
                if(requestedDate != null){
                    try{
                        queryDate = sdf.parse(lastupdated);
                    }
                    catch(Exception e){
                    	logger.error("Exception while parsing date in getMapGeoJSONBasedOnDate",e);
                        queryDate = null;
                    }
                }
            }

            returnValue =  geoService.getGeoJsonOuputJSON(queryDate);

        } catch (Exception e) {
            logger.error("Exception while gettting MapGeoJSON Based On Date \t"+e.getStackTrace());
        }
        return returnValue;
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.WILDCARD})
    @Path("/JSON/geoMap")
    public String getMapGeoJSON() {
        String returnValue = "";
        try {

            returnValue =  geoService.getGeoJsonOuputJSON(null);

        } catch (Exception e) {
            logger.error("Exception while getting MapGeoJSON \t"+e.getStackTrace());
        }
        return returnValue;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.WILDCARD})
    @Path("/JSONP/geoMap/qdate/{lastupdated}")
    public String getMapGeoJSONPBasedOnDate(@PathParam("lastupdated") String lastupdated) {
        //System.out.print("updated : " + lastupdated);

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
            logger.error("Exception while getting MapGeoJSONP BasedOnDate \t"+e.getStackTrace());
        }

        return returnValue;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.WILDCARD})
    @Path("/JSONP/geoMap")
    public String getMapGeoJSONP() {

        String returnValue = "";
        try {

            returnValue =  geoService.getGeoJsonOuputJSONP(null);

        } catch (Exception e) {
            logger.error("Exception while getting MapGeoJSONP \t"+e.getStackTrace());
        }

        return returnValue;
    }



}
