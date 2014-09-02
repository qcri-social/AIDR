package qa.qcri.aidr.analysis.api;

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


import qa.qcri.aidr.analysis.utils.MiscUtilities;
import qa.qcri.aidr.output.getdata.ChannelBufferManager;
import qa.qcri.aidr.output.utils.ErrorLog;


@Path("/analytics/stats")
public class GetStatistics implements ServletContextListener {

	// Debugging
	private static Logger logger = Logger.getLogger(GetStatistics.class);
	private static ErrorLog elog = new ErrorLog();

	// Related to channel buffer management
	private static final String CHANNEL_REG_EX = "aidr_predict.*";
	private static final String CHANNEL_PREFIX_STRING = "aidr_predict.";

	private volatile static ChannelBufferManager masterCBManager = null; 			// managing buffers for each publishing channel

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		if (null == masterCBManager) {
			logger.info("Initializing channel buffer manager");
			System.out.println("[contextInitialized] Initializing channel buffer manager");
			//cbManager = new ChannelBufferManager(CHANNEL_REG_EX);
			masterCBManager = new ChannelBufferManager();
			masterCBManager.initiateChannelBufferManager(CHANNEL_REG_EX);
			logger.info("Done initializing channel buffer manager");
			System.out.println("[contextInitialized] Done initializing channel buffer manager");
		}
		logger.info("Context Initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		masterCBManager.close();
		logger.info("Context destroyed");
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
			final String statusStr = "{\"aidr-output analytics service\":\"RESTARTED\"}";
			return Response.ok(statusStr).build();
		}
		return Response.ok(new String("{\"password\":\"invalid\"}")).build();
	}
		
	@GET
	@Path("/getTagCount/{classifierCode}/{labelCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSingleLabelCount(@PathParam("classifierCode") String attributeCode,
			@PathParam("labelCode") String labelCode, 
			@DefaultValue("5m") @QueryParam("granularity") String granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime) {
			
		long timeGranularity = MiscUtilities.parseTime(granularity);
		
		return Response.ok(JsonValue.NULL).build();
	}
	
	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		JsonObject obj = Json.createObjectBuilder()
							.add("aidr-analysis", "RUNNING")
							.build();
		
		return Response.ok(obj).build();
	}

}
