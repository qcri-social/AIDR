package qa.qcri.aidr.trainer.api.controller;

import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qa.qcri.aidr.trainer.api.store.CodeLookUp;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 2/3/14
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/util")
@RestController
public class ServiceController {

	@RequestMapping("/ping/heartbeat")
    public Response getHeartbeat() {
        String returnValue = "{\"status\":200}";
        return Response.status(CodeLookUp.APP_STATUS_ALIVE).entity(returnValue).build();
    }
}
