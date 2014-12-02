package qa.qcri.aidr.predictui.api;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.predictui.api.CrisisResource;

@Path("/test")
@Stateless
public class TestDBManagerResource {
	private Logger logger = LoggerFactory.getLogger(CrisisResource.class);

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisResourceFacade remoteCrisisEJB;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisis/{id}")
	public Response getCrisisByID(long id) {
		try {
			CrisisDTO dto = remoteCrisisEJB.getCrisisByID(id);
			if (dto != null) 
			{
				return Response.ok(dto).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching crisis from remote EJB for id = " + id);
			logger.error("stacktrace: ", e);
			e.printStackTrace();
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned for crisis ID = " + id).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisis/all")
	public Response getAllCrisis() {
		try {
			List<CrisisDTO> dtoList = remoteCrisisEJB.getAllCrisis();
			System.out.println("Returned successfully from remote EJB call!");
			if (dtoList != null) 
			{
				return Response.ok(dtoList).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching crisis from remote EJB");
			logger.error("stacktrace: ", e);
			e.printStackTrace();
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned").build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisis/update/{id}")
	public Response updateCrisisByID(long id) {
		try {
			CrisisDTO dto = remoteCrisisEJB.getCrisisByID(id);
			if (dto != null) 
			{
				dto.setIsTrashed(true);
				CrisisDTO newDTO = remoteCrisisEJB.editCrisis(dto);
				return Response.ok(newDTO).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in updating crisis from remote EJB: " + id);
			logger.error("stacktrace: ", e);
			e.printStackTrace();
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned for: " + id).build();
	}
}
