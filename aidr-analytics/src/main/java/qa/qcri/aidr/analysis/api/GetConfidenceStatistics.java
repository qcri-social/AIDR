/*
 * 
 * This is the REST API interface for accessing the aidr_analytics DB's confidence_data entity. 
 *
 * This class is not used at the moment.
 */
package qa.qcri.aidr.analysis.api;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import qa.qcri.aidr.analysis.facade.ConfidenceStatisticsResourceFacade;
import qa.qcri.aidr.common.code.DateFormatConfig;
import qa.qcri.aidr.output.getdata.ChannelBufferManager;

@Path("/confData/")
public class GetConfidenceStatistics extends GetStatistics implements ServletContextListener {
	
	// Debugging
	private static Logger logger = Logger.getLogger(GetConfidenceStatistics.class);
	
	@EJB
    private ConfidenceStatisticsResourceFacade confDataEJB;
	
	@GET
	@Path("/getBinCount/{classifierCode}/{labelCode}/{bin}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSingleLabelCount(@PathParam("classifierCode") String attributeCode,
			@PathParam("labelCode") String labelCode, 
			@PathParam("bin") Integer bin,
			@DefaultValue("5m") @QueryParam("granularity") String granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime) {
			
		long timeGranularity = DateFormatConfig.parseTime(granularity);
		
		return Response.ok(JsonValue.NULL).build();
	}
	
	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		JsonObject obj = Json.createObjectBuilder()
							.add("aidr-analysis/FreqData", "RUNNING")
							.build();
		
		return Response.ok(obj).build();
	}


	@GET
	@Path("/manage/restart/{passcode}")
	@Produces("application/json")
	public Response restartAnalyticsService(@PathParam("passcode") String passcode) {
		logger.info("Request received");
		if (passcode.equals("sysadmin2013")) {
			if (masterCBManager != null) {
				masterCBManager.close();
			}
			masterCBManager = new ChannelBufferManager();
			masterCBManager.initiateChannelBufferManager(CHANNEL_REG_EX);
			logger.info("aidr-output analytics service restarted...");
			final String statusStr = "{\"aidr-output analytics ConfidenceStats service\":\"RESTARTED\"}";
			return Response.ok(statusStr).build();
		}
		return Response.ok(new String("{\"password\":\"invalid\"}")).build();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
