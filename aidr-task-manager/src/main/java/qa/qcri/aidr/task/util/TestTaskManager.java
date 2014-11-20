package qa.qcri.aidr.task.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import qa.qcri.aidr.task.dto.DocumentDTO;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;
//import qa.qcri.aidr.task.ejb.bean.TaskManagerBean;
import qa.qcri.aidr.task.entities.Crisis;
import qa.qcri.aidr.task.entities.Document;

@Path("/test")
public class TestTaskManager {

	//@EJB
	private TaskManagerRemote<Document, Long> taskManager;
	public final String jndiLookup = "java:global/AIDRTaskManager/aidr-task-manager-1.0/TaskManagerBean!qa.qcri.aidr.task.ejb.TaskManagerRemote";
	
	public TestTaskManager() {
		
		try {
			/*
			Properties props = new Properties();
			props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
			props.setProperty("java.naming.factory, url.pkgs", "com.sun.enterprise.naming");
			props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
			props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
			props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
			InitialContext ctx = new InitialContext(props);
			*/
			
			InitialContext ctx = new InitialContext();	// use no-args for glassfish
			this.taskManager = (TaskManagerRemote<Document, Long>) ctx.lookup(jndiLookup);
			//String jsonString = taskManager.getNewTask(117L);
			//this.taskManager = (TaskManagerRemote<Document, Serializable>) ctx.lookup("java:global/aidr-task-manager/TaskManagerBean");
		} catch (NamingException e) {
			System.err.println("Error in JNDI lookup");
			e.printStackTrace();
		}
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remoteEJB")
	public <T> Response test(@QueryParam("id") Long id) {
		StringBuilder respString = new StringBuilder().append("Fetched new doc = ");

		//String jsonString = taskManager.getTaskById((id != null) ? id : new Long(4579254));
		DocumentDTO doc = taskManager.getTaskById((id != null) ? id : new Long(4579254));
		if (doc != null) {
			respString.append(taskManager.serializeTask(doc));
		} else {
			respString.append("Error in JNDI lookup");
		}
		System.out.println("[main] " + respString.toString());
		return Response.ok(respString.toString()).build();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/getNewTask")
	public <T> Response getNewTask(@QueryParam("id") Long id) {
		StringBuilder respString = new StringBuilder().append("Fetched new doc = ");

		//String jsonString = taskManager.getNewTask(id != null ? id : new Long(117));
		DocumentDTO doc = taskManager.getNewTask(id != null ? id : new Long(117));
		if (doc != null) {
			respString.append(taskManager.serializeTask(doc));
		} else {
			respString.append("Error in JNDI lookup");
		}
		System.out.println("[main] " + respString.toString());
		return Response.ok(respString.toString()).build();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/testSetParameters")
	public Response testSetParameters(@QueryParam("id") Long id) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("setHasHumanLabels", new Boolean(true).toString());
		paramMap.put("setCrisisID", new Long(117L).toString());
		//qa.qcri.aidr.task.entities.Document newDoc = taskManager.deSerialize(taskManager.setTaskParameter(qa.qcri.aidr.task.entities.Document.class, (id != null) ? id : new Long(4579250), paramMap), Document.class);
		qa.qcri.aidr.task.entities.Document newDoc = (qa.qcri.aidr.task.entities.Document) taskManager.setTaskParameter(qa.qcri.aidr.task.entities.Document.class, (id != null) ? id : new Long(4579250), paramMap);
		if (newDoc != null) {
			System.out.println("newDoc = " + newDoc.getDocumentID() + ": " + newDoc.getHasHumanLabels());
			String jsonString = taskManager.serializeTask(newDoc);
			return Response.ok(jsonString).build();
		} else {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		TestTaskManager tc = new TestTaskManager(); 
		System.out.println("Result: " + tc.test(null).toString());

		tc.testSetParameters(null);
	}
}
