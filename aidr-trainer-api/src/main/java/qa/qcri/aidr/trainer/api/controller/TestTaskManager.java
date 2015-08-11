package qa.qcri.aidr.trainer.api.controller;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;


@Path("/test")
@Component
public class TestTaskManager {
	
	protected static Logger logger = Logger.getLogger(TestTaskManager.class);

	@Context
	private UriInfo context;

	private static final String remoteEJBJNDIName = "java:global/AIDRTaskManager/aidr-task-manager-1.0/TaskManagerBean!qa.qcri.aidr.task.ejb.TaskManagerRemote";
	//@EJB(mappedName=remoteEJBJNDIName)

	@Autowired TaskManagerRemote<DocumentDTO, Long> taskManager;

	public TestTaskManager() {
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remoteEJB")
	public Response test() {
		StringBuilder respString = new StringBuilder().append("Fetched new doc details = ");
		try {
			long startTime = System.currentTimeMillis();
			/*
			Properties props = new Properties();
			props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
			props.setProperty("java.naming.factory, url.pkgs", "com.sun.enterprise.naming");
			props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
			props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
			props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

			InitialContext ctx = new InitialContext();

			taskManager = (TaskManagerRemote<qa.qcri.aidr.task.entities.Document, Long>) ctx.lookup(remoteEJBJNDIName);
			 */
			//System.out.println("taskManager: " + taskManager + ", time taken to initialize = " + (System.currentTimeMillis() - startTime));
			if (taskManager != null) {
				logger.info("Success in connecting to remote EJB to initialize taskManager");
			}
			long elapsed = 0L;

			DocumentDTO dto = taskManager.getTaskById(4579257L);
			elapsed = System.currentTimeMillis() - startTime;
			if (dto != null) {
				respString.append("documentID: ").append(dto.getDocumentID()).append(", crisisID: ").append(dto.getCrisisDTO().getCrisisID());
				respString.append(", taskAssignment: ").append(dto.getTaskAssignmentsDTO() != null ? dto.getTaskAssignmentsDTO().size() : null);
				//respString.append(", nominalLabel size: ").append(document.getNominalLabelCollection() != null ? document.getNominalLabelCollection().size() : 0);
			} else {
				respString.append("null");
			}
			logger.info("[main] " + respString.toString() + ", time taken = " + elapsed);
		} catch (Exception e) {
			logger.error("Error in JNDI lookup",e);
			respString.append("Error in JNDI lookup");
		}
		return Response.ok(respString.toString()).build();
	}

	public static void main(String[] args) throws Exception {
		TestTaskManager tc = new TestTaskManager(); 
		logger.info("Result: " + tc.test().toString());
	}
}
