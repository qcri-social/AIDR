package qa.qcri.aidr.predictui.api;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import qa.qcri.aidr.dbmanager.dto.CrisisDTO;
import qa.qcri.aidr.dbmanager.dto.CrisisTypeDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.UsersDTO;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;


@Path("/test")
@Stateless
public class TestDBManagerResource {
	private Logger logger = Logger.getLogger("db-manager-log");

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisResourceFacade remoteCrisisEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade remoteDocumentEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.UsersResourceFacade remoteUsersEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentNominalLabelResourceFacade remoteDocumentNominalLabelEJB;		

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.CrisisTypeResourceFacade remoteCrisisTypeEJB;

	@EJB
	private TaskManagerRemote<DocumentDTO, Long> taskManager;


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisisType/{id}")
	public Response getCrisisTypeByID(@PathParam("id") long id) {
		try {
			CrisisTypeDTO dto = remoteCrisisTypeEJB.findCrisisTypeByID(id);
			logger.info("Returned successfully from remote EJB call!");
			if (dto != null) 
			{
				return Response.ok(dto).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching crisisType from remote EJB: " + id);
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisisType/all")
	public Response getAllCrisisTypes() {
		try {
			List<CrisisTypeDTO> dtoList = remoteCrisisTypeEJB.getAllCrisisTypes();
			if (dtoList != null) 
			{
				return Response.ok(dtoList).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching crisisType list from remote EJB ");
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisisType/crisis/{id}")
	public Response getAllCrisisForCrisisType(@PathParam("id") long id) {
		try {
			List<CrisisDTO> dtoList = remoteCrisisTypeEJB.getAllCrisisForCrisisTypeID(id);
			if (dtoList != null) 
			{
				return Response.ok(dtoList).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching crisis list for crisisType from remote EJB: " + id);
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned").build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisis/{id}")
	public Response getCrisisByID(@PathParam("id") long id) {
		try {
			CrisisDTO dto = remoteCrisisEJB.findCrisisByID(id);
			if (dto != null) 
			{
				return Response.ok(dto).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching crisis from remote EJB for id = " + id);
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned for crisis ID = " + id).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisis/fulldata/{id}")
	public Response getCrisisWithAllParamsByID(@PathParam("id") long id) {
		try {
			CrisisDTO dto = remoteCrisisEJB.getCrisisWithAllFieldsByID(id);
			if (dto != null) 
			{
				return Response.ok(dto).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching crisis from remote EJB for id = " + id);
			logger.error("stacktrace: ", e);
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
			if (dtoList != null) 
			{
				return Response.ok(dtoList).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching crisis from remote EJB");
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisis/update/{id}")
	public Response updateCrisisByID(@PathParam("id") long id, @QueryParam("value") boolean value) {
		try {
			CrisisDTO dto = remoteCrisisEJB.findCrisisByID(id);
			if (dto != null) 
			{
				logger.info("Fetched crisis: " + dto.getCrisisID() + ", " + dto.getCrisisID());
				dto.setIsTrashed(value);
				CrisisDTO newDTO = remoteCrisisEJB.editCrisis(dto);
				return Response.ok(newDTO).build();
			} 
		} catch (Exception e) {
			logger.error("Exception in updating crisis from remote EJB: " + id);
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned for: " + id).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/document/{id}")
	public Response getDocumentByID(@PathParam("id") long id) {
		try {
			DocumentDTO dto = remoteDocumentEJB.findDocumentByID(id);
			if (dto != null) 
			{
				return Response.ok(dto).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching document from remote EJB for id = " + id);
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned for document ID = " + id).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/document/crisis/{id}")
	public Response getDocumentsByCrisisID(@PathParam("id") long id) {
		try {
			List<DocumentDTO> dtoList = remoteDocumentEJB.findDocumentsByCrisisID(id);
			if (dtoList != null) 
			{
				return Response.ok(dtoList).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching document from remote EJB for id = " + id);
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned for document ID = " + id).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/document/labeled/{id}")
	public Response getLabeledDocumentsByCrisisID(@PathParam("id") long id) {
		try {
			List<DocumentDTO> dtoList = remoteDocumentEJB.findLabeledDocumentsByCrisisID(id);
			if (dtoList != null) 
			{
				return Response.ok(dtoList).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching document from remote EJB for id = " + id);
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned for document ID = " + id).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/document/fulldata/{id}")
	public Response getWithAllFieldsDocument(@PathParam("id") long id) {
		try {
			DocumentDTO dto = remoteDocumentEJB.getDocumentWithAllFieldsByID(id);
			if (dto != null) 
			{
				return Response.ok(dto).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching document from remote EJB for id = " + id);
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned for document ID = " + id).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user/{id}")
	public Response getUserByID(@PathParam("id") long id) {
		try {
			UsersDTO dto = remoteUsersEJB.getUserById(id);
			if (dto != null) 
			{
				return Response.ok(dto).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching user from remote EJB for id = " + id);
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned for document ID = " + id).build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/dn/{id}")
	public Response getDocumentNominalLabelByID(@PathParam("id") long id) {
		try {
			DocumentNominalLabelDTO dto = remoteDocumentNominalLabelEJB.findLabeledDocumentByID(id);
			if (dto != null) 
			{
				return Response.ok(dto).build();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in fetching document_nominal_label from remote EJB for id = " + id);
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Null object returned for document ID = " + id).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisisType/adddel")
	public Response TestAddDeleteCrisisType() {
		try {
			CrisisTypeDTO crisisType = new CrisisTypeDTO("test_db-manager_crisisType");
			CrisisTypeDTO dto = remoteCrisisTypeEJB.addCrisisType(crisisType);
			if (dto != null) 
			{
				logger.info("Add crisis successful: " + dto.getName() + ":" + dto.getCrisisTypeId());
				Integer ret = remoteCrisisTypeEJB.deleteCrisisType(dto.getCrisisTypeId());
				if (ret != null && ret.intValue() == 1) return Response.ok("CrisisType Add-Delete test successful" + dto).build();
			} 
		} catch (Exception e) {
			logger.error("Error in /crisisType/addde.", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("CrisisType add-Delete test failed").build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/crisis/adddel")
	public Response TestAddDeleteCrisis() {
		try {
			CrisisTypeDTO crisisType = remoteCrisisTypeEJB.getAllCrisisTypes().get(0);
			UsersDTO user = remoteUsersEJB.getUserById(9L);
			CrisisDTO newCrisis = new CrisisDTO("testDBManagerCrisis", "test_db-manager_crisis", false, false, crisisType, user);
			CrisisDTO dto = remoteCrisisEJB.addCrisis(newCrisis);
			if (dto != null) 
			{
				logger.info("Add crisis successful: " + dto.getCrisisID() + ":" + dto.getName() + ":" + dto.getCode() 
						+ ":" + dto.getCrisisTypeDTO().getName() + ":" + dto.getCrisisTypeDTO().getCrisisTypeId());
				Integer ret = remoteCrisisEJB.deleteCrisis(dto);
				if (ret != null && ret.intValue() == 1) return Response.ok("Crisis Add-Delete test successful" + dto).build();
			} 
		} catch (Exception e) {
			logger.error("Error in /crisis/adddel.", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Crisis add-Delete test failed").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/document/adddel")
	public Response TestAddDeleteDocument() {
		try {
			CrisisDTO crisis = remoteCrisisEJB.findActiveCrisis().get(0);
			DocumentDTO newDoc = new DocumentDTO(crisis, false, false, 0.7, new Date(), "en", "twitter", "This is a test data for db-manager test");
			DocumentDTO dto = remoteDocumentEJB.addDocument(newDoc);
			if (dto != null) 
			{
				logger.info("Add document successful: " + dto.getDocumentID() + ":" + dto.getCrisisDTO().getCode() 
						+ ":" + dto.getCrisisDTO().getCrisisID());
				Integer ret = remoteDocumentEJB.deleteDocument(dto);
				if (ret != null && ret.intValue() == 1) return Response.ok("Document Add-Delete test successful" + dto).build();
			} 
		} catch (Exception e) {
			logger.error("Error in /document/adddel." e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("Document add-Delete test failed").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user/adddel")
	public Response TestAddDeleteUser() {
		try {
			UsersDTO newUser = new UsersDTO("testUserDBManager", "normal");
			UsersDTO dto = remoteUsersEJB.addUser(newUser);
			if (dto != null) 
			{
				logger.info("Add user successful: " + dto.getUserID() + ":" + dto.getName() + ":" + dto.getRole());
				Integer ret = remoteUsersEJB.deleteUser(dto.getUserID());	
				if (ret != null && ret.intValue() == 1) return Response.ok("User Add-Delete test successful" + dto).build();
			} 
		} catch (Exception e) {
			logger.error("stacktrace: ", e);
			return Response.ok("Exception: " + e).build();
		}
		return Response.ok("User add-Delete test failed").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/task/new/{crisisID}")
	public Response TestGetNewTask(@PathParam("crisisID") long crisisID) {
		System.out.println("Going to fetch new task collection for crisis ID = " + crisisID);
		try {
			List<DocumentDTO> dtoList = taskManager.getNewTaskCollection(crisisID, 10, "DESC", null);
			if (dtoList != null) {
				return Response.ok(dtoList).build();
			} else {
				return Response.ok("Null object returned for crisis ID = " + crisisID).build();
			}
		} catch (Exception e) {
			logger.error("Error in TestGetNewTask.", e);
			return Response.ok("Exception: " + e).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/document/nominalLabel/{nominalLabelID}")
	public Response TestGetLabeled(@PathParam("nominalLabelID") long nominalLabelID) {
		logger.info("Going to fetch labled documents for nominalLabel ID = " + nominalLabelID);
		try {
			List<DocumentDTO> dtoList = taskManager.getNominalLabelDocumentCollection(nominalLabelID);
			if (dtoList != null) {
				return Response.ok(dtoList).build();
			} else {
				return Response.ok("Null object returned for nominalLabelID = " + nominalLabelID).build();
			}
		} catch (Exception e) {
			logger.error("Error in TestGetLabeled for label id : " + nominalLabelID, e);
			return Response.ok("Exception: " + e).build();
		}
	}

}

