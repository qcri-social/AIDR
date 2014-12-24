/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

//import org.apache.log4j.Logger;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.predictui.entities.Users;
import qa.qcri.aidr.predictui.facade.UserResourceFacade;
import qa.qcri.aidr.predictui.util.ResponseWrapper;
import static qa.qcri.aidr.predictui.util.ConfigProperties.getProperty;

/**
 * REST Web Service
 *
 * @author Imran, Koushik
 */
@Path("/user")
@Stateless
public class UserResource {

    @Context
    private UriInfo context;
    @EJB
    private UserResourceFacade userLocalEJB;

    //private Logger logger = Logger.getLogger(UserResource.class.getName());
	private Logger logger = LoggerFactory.getLogger(UserResource.class);
	private ErrorLog elog = new ErrorLog();
    
    public UserResource() {
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(UsersDTO user) {

        UsersDTO createdUser = userLocalEJB.addUser(user);
        if (createdUser == null){
            return Response.ok("Error while creating new user.").build();
        } else {
        	System.out.println("Created new user with id = " + createdUser.getUserID() + ", name = " + createdUser.getName());
        }
        //return Response.ok(createdUser).build();
        ObjectMapper mapper = new ObjectMapper();
        try {
        	//System.out.println("serialized user data: " + mapper.writeValueAsString(createdUser));
        	return Response.ok(mapper.writeValueAsString(createdUser)).build();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return Response.ok().build();

    }
    
    @GET 
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUserByName(@PathParam("name") String userName) {

        UsersDTO user = userLocalEJB.getUserByName(userName);
        System.out.println("fetched user data: " + (user != null ? user.getUserID() : "null"));
        if (user == null){
//            return the same object but with empty id and role. By empty id we can see that user does not exist.
            user = new UsersDTO(null, userName, null);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
        	//System.out.println("serialized user data: " + mapper.writeValueAsString(user));
        	return Response.ok(mapper.writeValueAsString(user)).build();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return Response.ok().build();

    }
    
    @GET 
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UsersDTO findUserByID(@PathParam("id") Long id) {

        UsersDTO user = userLocalEJB.getUserByID(id);
        System.out.println("fetched user data: " + (user != null ? user.getUserID() : "null"));
        if (user == null){
//            return the same object but with empty id and role. By empty id we can see that user does not exist.
            user = new UsersDTO(id, "doesn't exist", null);
        }
        return user;

    }
    
    @GET 
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllUsers() {
        List<UsersDTO> users = userLocalEJB.getAllUsers();
        logger.info("fetched user data size: " + (users != null ? users.size(): "null"));
        ObjectMapper mapper = new ObjectMapper();
        try {
        	return Response.ok(mapper.writeValueAsString(users)).build();
        } catch (Exception e) {
        	logger.error("Error in getting all users");
        	logger.error(elog.toStringException(e));
        	return Response.ok(new ResponseWrapper(getProperty("STATUS_CODE_FAILED"), e.getCause().getCause().getMessage())).build();
        }
    }
}
