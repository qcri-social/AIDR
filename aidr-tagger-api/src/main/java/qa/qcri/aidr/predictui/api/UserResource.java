/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.api;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import qa.qcri.aidr.predictui.entities.Users;
import qa.qcri.aidr.predictui.facade.UserResourceFacade;

/**
 * REST Web Service
 *
 * @author Imran
 */
@Path("/user")
@Stateless
public class UserResource {

    @Context
    private UriInfo context;
    @EJB
    private UserResourceFacade userLocalEJB;

    public UserResource() {
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(Users user) {

        Users createdUser = userLocalEJB.addUser(user);
        if (createdUser == null){
            return Response.ok("Error while creating new user.").build();
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

        Users user = userLocalEJB.getUserByName(userName);
        System.out.println("fetched user data: " + (user != null ? user.getUserID() : "null"));
        if (user == null){
//            return the same object but with empty id and role. By empty id we can see that user does not exist.
            user = new Users(null, userName, null);
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
    public Users findUserByID(@PathParam("id") String userName) {

        Users user = userLocalEJB.getUserByName(userName);
        System.out.println("fetched user data: " + (user != null ? user.getUserID() : "null"));
        if (user == null){
//            return the same object but with empty id and role. By empty id we can see that user does not exist.
            user = new Users(null, userName, null);
        }
        return user;

    }
}
