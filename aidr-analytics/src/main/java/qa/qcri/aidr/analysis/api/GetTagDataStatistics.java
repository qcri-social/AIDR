
package qa.qcri.aidr.analysis.api;


import javax.ejb.EJB;
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

import qa.qcri.aidr.analysis.facade.TagDataStatisticsResourceFacade;
import qa.qcri.aidr.analysis.service.GetTagDataStatisticsService;

/** 
 * This is the REST API interface for accessing the aidr_analytics DB's tag_data entity. 
 */

@Path("/tagdata/")
public class GetTagDataStatistics implements ServletContextListener {

	// Debugging
	private static Logger logger = Logger.getLogger(GetTagDataStatistics.class);

	@EJB
	private GetTagDataStatisticsService tagDataService;
	
	
	@GET
	@Path("/getlabelsum/{crisisCode}/{granularity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagCountSumForAllAttributesFromTime(@PathParam("crisisCode") String crisisCode,
			@PathParam("granularity") Long granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime) {
		
		System.out.println("local EJB in REST API = " + tagDataService);
		JSONObject json = tagDataService.getTagCountSumForAllAttributesFromTime(crisisCode, granularity, startTime);
		return Response.ok(json.toJSONString()).build();
	}

	
	@GET
	@Path("/getlabelsum/{crisisCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagCountSumForAllGranularitiesFromTime(@PathParam("crisisCode") String crisisCode,
			@DefaultValue("0") @QueryParam("startTime") Long startTime) {
		
		System.out.println("local EJB in REST API = " + tagDataService);
		JSONObject json = tagDataService.getTagCountSumByGranularity(crisisCode, startTime);
		return Response.ok(json.toJSONString()).build();
	}

	
	/**
	 * 
	 * @param crisisCode
	 * @param attributeCode
	 * @param granularity
	 * @param startTime
	 * @return Count sum per label from startTime to current time at the given granularity, for crisisCode and attributeCode
	 */
	@GET
	@Path("/getlabelsum/{crisisCode}/{attributeCode}/{granularity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagCountSumFromTime(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("granularity") Long granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime) {
		
		System.out.println("local EJB in REST API = " + tagDataService);
		JSONObject json = tagDataService.getTagCountSumFromTime(crisisCode, attributeCode, granularity, startTime);
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
	@Path("/getlabelcount/{crisisCode}/{attributeCode}/{granularity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagCountInTimeWindow(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("granularity") Long granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime) {

		JSONObject json = tagDataService.getTagCountInTimeWindow(crisisCode, attributeCode, granularity, startTime);
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
	@Path("/getlabeltimeseries/{crisisCode}/{attributeCode}/{granularity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagCountTimeSeries(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("granularity") Long granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime,
			@QueryParam("endTime") Long endTime) {

		if (null == endTime || endTime < startTime) {
			endTime = System.currentTimeMillis();
		}
		JSONObject json = tagDataService.getTagCountTimeSeries(crisisCode, attributeCode, granularity, startTime, endTime);
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
	@Path("/getintervallabelsum/{crisisCode}/{attributeCode}/{granularity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagCountSumInInterval(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("granularity") Long granularity,
			@DefaultValue("0") @QueryParam("startTime") Long startTime,
			@QueryParam("endTime") Long endTime) {

		JSONObject json = tagDataService.getTagCountSumInInterval(crisisCode, attributeCode, granularity, startTime, endTime);
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
	@Path("/getonelabeldata/{crisisCode}/{attributeCode}/{labelCode}/{granularity}/{startTime}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSingleItem(@PathParam("crisisCode") String crisisCode,
			@PathParam("attributeCode") String attributeCode,
			@PathParam("labelCode") String labelCode, 
			@PathParam("granularity") Long granularity,
			@PathParam("startTime") Long startTime) {
		JSONObject json = tagDataService.getSingleItem(crisisCode, attributeCode, labelCode, granularity, startTime);
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
