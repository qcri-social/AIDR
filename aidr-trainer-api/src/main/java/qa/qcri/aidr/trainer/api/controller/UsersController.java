package qa.qcri.aidr.trainer.api.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.dbmanager.entities.misc.Users;
import qa.qcri.aidr.trainer.api.service.UsersService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/20/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/users")
@Component
public class UsersController {
    protected static Logger logger = Logger.getLogger("UsersController");

    @Autowired
    private UsersService usersService;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path("/getuser/{username}")
    public Users getUserByName(@PathParam("username") String username){

        return  usersService.findUserByName(username) ;
    }

}
