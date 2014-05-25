package qa.qcri.aidr.task.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.task.entities.Crisis;
import qa.qcri.aidr.task.entities.Document;

@Path("/test")
public class TestTaskManager {
	
	@EJB
	private TaskManagerRemote<Document, Serializable> taskManager;

	public TestTaskManager() {
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/remoteEJB")
	public <T> String test() {
		StringBuilder respString = new StringBuilder().append("Fetched new doc = ");
		try {
			Properties props = new Properties();
			props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
			props.setProperty("java.naming.factory, url.pkgs", "com.sun.enterprise.naming");
			props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
			props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
			props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

			InitialContext ctx = new InitialContext(props);
			this.taskManager = (TaskManagerRemote<Document, Serializable>) ctx.lookup("java:global/aidr-task-manager/TaskManagerBean!qa.qcri.aidr.task.api.TaskManagerRemote");
			String jsonString = taskManager.getNewTask(117L);
			
			//this.taskManager = (TaskManagerRemote<Document, Serializable>) ctx.lookup("java:global/aidr-task-manager/TaskManagerBean");
			//String jsonString = taskManager.getTaskById(103L);
			
			if (jsonString != null) {
				ObjectMapper mapper = new ObjectMapper();
				Object obj = null;
				try {
					obj = mapper.readValue(jsonString, obj.getClass());
				} catch (Exception e) {
					e.printStackTrace();
				} 
				if (obj != null) {
					respString.append(obj);
				} else {
					respString.append("null");
				}
			}
			System.out.println("[main] " + respString.toString());
		} catch (NamingException e) {
			System.err.println("Error in JNDI lookup");
			respString.append("Error in JNDI lookup");
			e.printStackTrace();
		}
		//return Response.ok(respString.toString()).build();
		return respString.toString();
	}

	public static void main(String[] args) throws Exception {
		TestTaskManager tc = new TestTaskManager(); 
		System.out.println("Result: " + tc.test().toString());
	}
}
