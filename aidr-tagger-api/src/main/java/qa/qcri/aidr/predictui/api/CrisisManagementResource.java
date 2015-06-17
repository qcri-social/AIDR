package qa.qcri.aidr.predictui.api;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.Document;
import qa.qcri.aidr.predictui.entities.ModelFamily;
import qa.qcri.aidr.predictui.facade.CrisisManagementResourceFacade;
import qa.qcri.aidr.predictui.facade.CrisisResourceFacade;
import qa.qcri.aidr.predictui.facade.DocumentFacade;
import qa.qcri.aidr.predictui.facade.ModelFamilyFacade;

import qa.qcri.aidr.task.ejb.TaskManagerRemote;

@Path("/manage/collection")
@Stateless
public class CrisisManagementResource {

	@Context
	private UriInfo context;

	@EJB
	private CrisisManagementResourceFacade crisisManagmentLocalEJB;

	//private static Logger logger = Logger.getLogger(CrisisManagementResource.class);
	private static Logger logger = Logger.getLogger(CrisisManagementResource.class);
	private static ErrorLog elog = new ErrorLog();

	public CrisisManagementResource() {
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
			logger.error(elog.toStringException(e));
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
			logger.error(elog.toStringException(e));
			return Response.ok("{\"status\": \"FAILED\"}").build();
		}
	}

}
