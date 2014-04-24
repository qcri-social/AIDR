package qa.qcri.aidr.task.api;

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

import qa.qcri.aidr.task.api.TaskManagerRemote;
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
	public Response test() {
		StringBuilder respString = new StringBuilder().append("Fetched new doc = ");
		try {
			//Properties props = new Properties();
	        //props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
	        //props.setProperty("java.naming.factory, url.pkgs", "com.sun.enterprise.naming");
	        //props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
	        //props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
	        //props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
	 
	        InitialContext ctx = new InitialContext();
			this.taskManager = (TaskManagerRemote<Document, Serializable>) ctx.lookup("qa.qcri.aidr.task.api.TaskManagerRemote");
			Document document = taskManager.getNewTask(117L);
			respString.append(document.getDocumentID());
			System.out.println("[main] Retreived new doc = " + document.getDocumentID());
		} catch (NamingException e) {
			System.err.println("Error in JNDI lookup");
			respString.append("Error in JNDI lookup");
			e.printStackTrace();
		}
		return Response.ok(respString.toString()).build();
	}
}
