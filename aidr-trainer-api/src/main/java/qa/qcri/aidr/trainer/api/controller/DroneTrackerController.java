package qa.qcri.aidr.trainer.api.controller;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.service.DroneTrackerService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/23/14
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/drone")
@Component
public class DroneTrackerController {
    protected static Logger logger = Logger.getLogger("DroneTrackerController");

    @Autowired
    private DroneTrackerService droneTrackerService;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/getdrones")
    public JSONArray getAllDrones(){

        return droneTrackerService.getAllApprovedDroneGeoData();

    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/jsonp/getdrones")
    public String getJSONPAllDrones(){
        JSONArray jArrary = droneTrackerService.getAllApprovedDroneGeoData();
        String returnValue = "jsonp(" + jArrary.toJSONString() + ");";
        return returnValue;

    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/jsonp/drones/after/{droneID}")
    public String getJSONPDronesAfterID(@PathParam("droneID") Long droneID){
        String returnValue = null;
        System.out.println("droneID : " + droneID);
        if(droneID.equals(null)){
            JSONArray jArrary = droneTrackerService.getAllApprovedDroneGeoData();
            returnValue = "jsonp(" + jArrary.toJSONString() + ");";
        }
        else{
            JSONArray jArrary = droneTrackerService.getAllApprovedDroneGeoDataAfterID(droneID);
            returnValue = "jsonp(" + jArrary.toJSONString() + ");";
        }

        return returnValue;

    }

    @POST
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/add")
    public void addNewDrone(String data){
        System.out.println("addNewDrone : " + data);
        droneTrackerService.saveUserMappingRequest(data);
      //  return Response.status(CodeLookUp.APP_REQUEST_SUCCESS).entity(StatusCodeType.POST_COMPLETED).build();

    }

    @POST
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/jsonp/add")
    public void addJSONPNewDrone(String data){
        System.out.println("addJSONPNewDrone : " + data);
        String temp = data.replace("jsonp(","");
        temp.replace(");","");
        System.out.println("addJSONPNewDrone - temp : " + temp);
        droneTrackerService.saveUserMappingRequest(temp);
        //  return Response.status(CodeLookUp.APP_REQUEST_SUCCESS).entity(StatusCodeType.POST_COMPLETED).build();

    }

}
