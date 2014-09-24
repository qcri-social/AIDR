package qa.qcri.aidr.analysis.api;

import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import qa.qcri.aidr.common.code.DateFormatConfig;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.output.getdata.ChannelBufferManager;
import qa.qcri.aidr.analysis.api.GetStatistics;
import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.analysis.entity.TagDataPK;
import qa.qcri.aidr.analysis.facade.TagDataStatisticsResourceFacade;

@Path("/tagData/")
public class GetTagDataStatistics extends GetStatistics implements ServletContextListener {

	// Debugging
	private static Logger logger = Logger.getLogger(GetTagDataStatistics.class);
	private static ErrorLog elog = new ErrorLog();
	
	@EJB
    private TagDataStatisticsResourceFacade tagDataEJB;
		
	@GET
	@Path("/getTagCount/{crisisCode}/{classifierCode}/{labelCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSingleItem(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("labelCode") String labelCode, 
			@DefaultValue("5m") @QueryParam("granularity") String granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime) {
			
		long timeGranularity = DateFormatConfig.parseTime(granularity);
		TagDataPK tagDataPK = new TagDataPK();
		tagDataPK.setCrisisCode(crisisCode);
		tagDataPK.setTimestamp(startTime);
		tagDataPK.setGranularity(timeGranularity);
		tagDataPK.setAttributeCode(attributeCode);
		tagDataPK.setLabelCode(labelCode);
		
		TagData obj = tagDataEJB.getSingleDataByPK(tagDataPK);
		
		List<TagData> fetchedList = tagDataEJB.getDataByCrisisAttributeLabel(crisisCode, attributeCode, labelCode);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return Response.ok(mapper.writeValueAsString(fetchedList)).build();
		} catch (Exception e) {
			logger.error("Serialization error");
			logger.error(elog.toStringException(e));
		}
		return Response.ok(JsonValue.NULL).build();
	}
	
	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		JsonObject obj = Json.createObjectBuilder()
							.add("aidr-analysis/tagData", "RUNNING")
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
			final String statusStr = "{\"aidr-output analytics TagStats service\":\"RESTARTED\"}";
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
