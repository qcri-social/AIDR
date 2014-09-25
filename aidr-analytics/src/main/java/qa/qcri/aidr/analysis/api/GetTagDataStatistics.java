package qa.qcri.aidr.analysis.api;

import java.util.Date;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import qa.qcri.aidr.common.code.FasterXmlWrapper;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.analysis.dto.TagCountDTO;
import qa.qcri.aidr.analysis.dto.TagCountSeriesDTO;
import qa.qcri.aidr.analysis.dto.TimeWindowTagCountDTO;
import qa.qcri.aidr.analysis.dto.helper.TagCountDTOHelper;
import qa.qcri.aidr.analysis.dto.helper.TagCountSeriesDTOHelper;
import qa.qcri.aidr.analysis.dto.helper.TimeWindowTagCountDTOHelper;
import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.analysis.entity.TagDataPK;
import qa.qcri.aidr.analysis.facade.TagDataStatisticsResourceFacade;
import qa.qcri.aidr.analysis.utils.JsonResponse;


@Path("/tagData/")
public class GetTagDataStatistics extends GetStatistics implements ServletContextListener {

	// Debugging
	private static Logger logger = Logger.getLogger(GetTagDataStatistics.class);
	private static ErrorLog elog = new ErrorLog();

	@EJB
	private TagDataStatisticsResourceFacade tagDataEJB;

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

		//long timeGranularity = DateFormatConfig.parseTime(granularity);
		// First get the list of data points from DB
		List<TagData> tagDataList = tagDataEJB.getDataAfterTimestampGranularity(crisisCode, attributeCode, null, startTime, granularity);

		// Now the real work - count and send response
		JSONObject json = JsonResponse.getNewJsonResponseObject(crisisCode, attributeCode, granularity);
		json.put("startTime", new Date(startTime));
		if (tagDataList != null) {
			Map<String, Integer> tagCountMap = new TreeMap<String, Integer>();
			for (TagData t: tagDataList) {
				if (tagCountMap.containsKey(t.getLabelCode())) {
					tagCountMap.put(t.getLabelCode(), tagCountMap.get(t.getLabelCode()) + t.getCount());
				} else {
					tagCountMap.put(t.getLabelCode(), t.getCount());
				}
			}
			try {
				json.put("data", tagCountMap);
			} catch (Exception e) {
				logger.info("Error in serializing fetched tag count data");
				e.printStackTrace();
			}
		}
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

		//long timeGranularity = DateFormatConfig.parseTime(granularity);
		// First get the list of data points from DB
		List<TagData> tagDataList = tagDataEJB.getDataByGranularityInTimeWindow(crisisCode, attributeCode, null, startTime, granularity);
		
		// Now the real work - count and send response
		TimeWindowTagCountDTO dto = new TimeWindowTagCountDTO();
		JSONObject json = JsonResponse.getNewJsonResponseObject(crisisCode, attributeCode, granularity);
		json.put("timestamp", new Date(startTime));

		if (tagDataList != null) {
			try {
				List<TagCountDTO> dtoList = new ArrayList<TagCountDTO>();
				for (TagData t: tagDataList) {
					dtoList.add(TagCountDTOHelper.convertTagDataToDTO(t));
					System.out.println("tag: " + t.getLabelCode() + ", count: " + t.getCount());
				}
				dto = TimeWindowTagCountDTOHelper.convertTagCountDTOListToDTO(startTime, dtoList);
				json.put("data", dto);
			} catch (Exception e) {
				logger.info("Error in serializing fetched tag count data");
				e.printStackTrace();
			}
		}
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
		//long timeGranularity = DateFormatConfig.parseTime(granularity);
		// First get the list of data points from DB
		List<TagData> tagDataList = tagDataEJB.getDataInIntervalWithGranularity(crisisCode, attributeCode, null, startTime, endTime, granularity);

		// Now the real work - creat time series, format and send response
		JSONObject json = new JSONObject();
		if (tagDataList != null) {
			try {
				// Create time series Map data first
				Map<Long, List<TagCountDTO>> tagCountMap = new TreeMap<Long, List<TagCountDTO>>();
				for (TagData t: tagDataList) {
					if (tagCountMap.containsKey(t.getTimestamp())) {
						List<TagCountDTO> tagsList = tagCountMap.get(t.getTimestamp());
						if (null == tagsList || tagsList.isEmpty()) {
							tagsList = new ArrayList<TagCountDTO>();
							tagCountMap.put(t.getTimestamp(), tagsList); 
						}
						tagsList.add(TagCountDTOHelper.convertTagDataToDTO(t));
					} else {
						tagCountMap.put(t.getTimestamp(), new ArrayList<TagCountDTO>());
						List<TagCountDTO> tagsList = tagCountMap.get(t.getTimestamp());
						tagsList.add(TagCountDTOHelper.convertTagDataToDTO(t));
					}
				}
				//System.out.println("Finished creating Map of timestamp versus TagCountDTO list");
				// Now convert the above time series data Map to DTO object for response
				List<TimeWindowTagCountDTO> timeWindowDTOList = new ArrayList<TimeWindowTagCountDTO>();
				for (Long key: tagCountMap.keySet()) {
					TimeWindowTagCountDTO timeWindowDTO = TimeWindowTagCountDTOHelper.convertTagCountDTOListToDTO(key, tagCountMap.get(key));
					timeWindowDTOList.add(timeWindowDTO);
				}
				//System.out.println("Finished creating TimeWindowTagCountDTO list");
				TagCountSeriesDTO dto = TagCountSeriesDTOHelper.convertTimeWindowTagCountDTOListToDTO(crisisCode, attributeCode, granularity, timeWindowDTOList);
				//System.out.println("Finished creating TagCountSeriesDTO");
				ObjectMapper mapper = FasterXmlWrapper.getObjectMapper();
				//json.put("time_series", dto);
				//System.out.println("Finished creating json object: " + json);
				return Response.ok(mapper.writeValueAsString(dto)).build();
			} catch (Exception e) {
				logger.info("Error in serializing fetched tag count data");
				e.printStackTrace();
			}
		}
		return Response.ok(JsonResponse.getNewJsonResponseObject(crisisCode, attributeCode, granularity).toJSONString()).build();
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

		//long timeGranularity = DateFormatConfig.parseTime(granularity);
		// First get the list of data points from DB
		List<TagData> tagDataList = tagDataEJB.getDataInIntervalWithGranularity(crisisCode, attributeCode, null, startTime, endTime, granularity);

		// Now the real work - count and send response
		JSONObject json = JsonResponse.getNewJsonResponseObject(crisisCode, attributeCode, granularity);
		json.put("startTime", new Date(startTime));
		json.put("endTime", new Date(endTime));

		if (tagDataList != null) {
			Map<String, Integer> tagCountMap = new TreeMap<String, Integer>();
			for (TagData t: tagDataList) {
				System.out.println("Looking at tag: " + t.getLabelCode() + ", having count = " + t.getCount());
				if (tagCountMap.containsKey(t.getLabelCode())) {
					tagCountMap.put(t.getLabelCode(), tagCountMap.get(t.getLabelCode()) + t.getCount());
					System.out.println("Update Map for OLD tag = " + t.getLabelCode() + " with count = " + tagCountMap.get(t.getLabelCode()));
				} else {
					tagCountMap.put(t.getLabelCode(), t.getCount());
					System.out.println("Update Map with NEW tag = " + t.getLabelCode() + " with count = " + tagCountMap.get(t.getLabelCode()));
				}
			}
			try {
				json.put("data", tagCountMap);
			} catch (Exception e) {
				logger.info("Error in serializing fetched tag count data");
				e.printStackTrace();
			}
		}
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

		//long timeGranularity = DateFormatConfig.parseTime(granularity);
		TagDataPK tagDataPK = new TagDataPK();
		tagDataPK.setCrisisCode(crisisCode);
		tagDataPK.setTimestamp(startTime);
		tagDataPK.setGranularity(granularity);
		tagDataPK.setAttributeCode(attributeCode);
		tagDataPK.setLabelCode(labelCode);

		TagData obj = tagDataEJB.getSingleDataByPK(tagDataPK);
		JSONObject json = JsonResponse.getNewJsonResponseObject(crisisCode, attributeCode, granularity);
		try {
			json.put("timestamp", new Date(startTime));
			json.put("data", obj);
		} catch (Exception e) {
			logger.error("Serialization error");
			logger.error(elog.toStringException(e));
		}
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
