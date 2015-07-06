package qa.qcri.aidr.analysis.api;

import javax.json.Json;
import javax.json.JsonObject;
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

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;


import qa.qcri.aidr.analysis.service.GetTagDataStatisticsService;

@Path("/tagData/")
public class GetTagDataStatistics extends GetStatistics implements ServletContextListener {

	// Debugging
	private static Logger logger = Logger.getLogger(GetTagDataStatistics.class);

	/**
	 * 
	 * @param crisisCode
	 * @param attributeCode
	 * @param granularity
	 * @param startTime
	 * @return Count sum per label from startTime to current time at the given granularity, for crisisCode and attributeCode
	 */
	@GET
	@Path("/getLabelSum/{crisisCode}/{classifierCode}/{granularity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagCountSumFromTime(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("granularity") Long granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime) {
		
		JSONObject json = new GetTagDataStatisticsService().getTagCountSumFromTime(crisisCode, attributeCode, granularity, startTime);
		return Response.ok(json.toJSONString()).build();
	}

	/**
	 * 
	 * @param crisisCode
	 * @param attributeCode
	 * @param granularity
	 * @param startTime
	 * @return Count per label in the specified time window at the given granularity, for crisisCode and attributeCode
	 */
	@GET
	@Path("/getLabelCount/{crisisCode}/{attributeCode}/{granularity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagCountInTimeWindow(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("granularity") Long granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime) {

		JSONObject json = new GetTagDataStatisticsService().getTagCountInTimeWindow(crisisCode, attributeCode, granularity, startTime);
		return Response.ok(json.toJSONString()).build();
	}

	/**
	 * 
	 * @param crisisCode
	 * @param attributeCode
	 * @param granularity
	 * @param startTime
	 * @param endTime
	 * @return Time series data for each label in the interval [startTime, endTime] at the given granularity, for crisisCode and attributeCode
	 */
	@GET
	@Path("/getLabelTimeSeries/{crisisCode}/{attributeCode}/{granularity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagCountTimeSeries(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("granularity") Long granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime,
			@QueryParam("endTime") Long endTime) {

		if (null == endTime || endTime < startTime) {
			endTime = System.currentTimeMillis();
		}
		JSONObject json = new GetTagDataStatisticsService().getTagCountTimeSeries(crisisCode, attributeCode, granularity, startTime, endTime);
		return Response.ok(json.toJSONString()).build();
	}

	/**
	 * 
	 * @param crisisCode
	 * @param attributeCode
	 * @param granularity
	 * @param startTime
	 * @param endTime
	 * @return Count sum for each label in the interval [startTime, endTime] at the given granularity, for crisisCode and attributeCode
	 */
	@GET
	@Path("/getIntervalLabelSum/{crisisCode}/{attributeCode}/{granularity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagCountSumInInterval(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("granularity") Long granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime,
			@QueryParam("endTime") Long endTime) {

		JSONObject json = new GetTagDataStatisticsService().getTagCountSumInInterval(crisisCode, attributeCode, granularity, startTime, endTime);
		return Response.ok(json.toJSONString()).build();
	}


	/**
	 * 
	 * @param crisisCode
	 * @param attributeCode
	 * @param labelCode
	 * @param granularity
	 * @param startTime
	 * @return The count for a label in the specified time window at the given granularity, for crisisCode and attributeCode
	 */
	@GET
	@Path("/getOneLabelData/{crisisCode}/{attributeCode}/{labelCode}/{granularity}/{startTime}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSingleItem(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("labelCode") String labelCode, 
			@PathParam("granularity") Long granularity,
			@PathParam("startTime") Long startTime) {
		JSONObject json = new GetTagDataStatisticsService().getSingleItem(crisisCode, attributeCode, labelCode, granularity, startTime);
		return Response.ok(json.toJSONString()).build();
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



	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

}
