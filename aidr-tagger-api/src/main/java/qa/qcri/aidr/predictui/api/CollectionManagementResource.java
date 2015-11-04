package qa.qcri.aidr.predictui.api;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import qa.qcri.aidr.predictui.facade.CrisisManagementResourceFacade;

@Path("/manage/collection")
@Stateless
public class CollectionManagementResource {

	@Context
	private UriInfo context;

	@EJB
	private CrisisManagementResourceFacade crisisManagmentLocalEJB;

	//private static Logger logger = Logger.getLogger(CrisisManagementResource.class);
	private static Logger logger = Logger.getLogger(CollectionManagementResource.class);

	public CollectionManagementResource() {
	}

	@GET
	@Path("/trash/crisis/{crisisCode}")
	@Produces({"application/json"})
	public Response trashByCrisisCode(@PathParam("crisisCode") String crisisCode) {
		try {
			String response = crisisManagmentLocalEJB.trashByCrisisCode(crisisCode);
			return Response.ok(response).build();
		} catch (Exception e) {
			logger.error("Something went wrong in trashing attempt!");
			return Response.ok("{\"status\": \"FAILED\"}").build();
		}
	}

	@GET
	@Path("/untrash/crisis/{crisisCode}")
	@Produces({"application/json"})
	public Response untrashByCrisisCode(@PathParam("crisisCode") String crisisCode) {
		try {
			String response = crisisManagmentLocalEJB.untrashByCrisisCode(crisisCode);
			return Response.ok(response).build();
		} catch (Exception e) {
			logger.error("Something went wrong in trashing attempt!");
			return Response.ok("{\"status\": \"FAILED\"}").build();
		}
	}

}
