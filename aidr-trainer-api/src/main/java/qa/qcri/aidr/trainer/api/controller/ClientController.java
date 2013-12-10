package qa.qcri.aidr.trainer.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.trainer.api.entity.Client;
import qa.qcri.aidr.trainer.api.service.ClientService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/client")
@Component
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/id/{clientid}")
    public Client getCrisisByID(@PathParam("clientid") Long clientid){
        return clientService.findClientbyID("clientID", clientid) ;
    }


}
