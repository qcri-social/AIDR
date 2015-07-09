/**
 * Implements the business logic for providing the REST API functionalities on tag_data table.
 */
package qa.qcri.aidr.analysis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;

import net.minidev.json.JSONObject;

import org.apache.log4j.Logger;

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


public class GetTagDataStatisticsService {

	// Debugging
	private static Logger logger = Logger.getLogger(GetTagDataStatisticsService.class);

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

	public JSONObject getTagCountSumFromTime(String crisisCode, String attributeCode, Long granularity, Long startTime) {

		// First get the list of data points from DB
		List<TagData> tagDataList = tagDataEJB.getDataAfterTimestampGranularity(crisisCode, attributeCode, null, startTime, granularity);

		// Now the real work - count and send response
		JSONObject json = new JsonResponse().getNewJsonResponseObject(crisisCode, attributeCode, granularity, startTime, null);

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
				json = JsonResponse.addError(json);
				logger.error("Error in serializing fetched tag count data", e);
			}
		}
		return json;
	}

	/**
	 * 
	 * @param crisisCode
	 * @param attributeCode
	 * @param granularity
	 * @param startTime
	 * @return Count per label in the specified time window at the given granularity, for crisisCode and attributeCode
	 */
	public JSONObject getTagCountInTimeWindow(String crisisCode, String attributeCode, Long granularity, Long startTime) {

		//long timeGranularity = DateFormatConfig.parseTime(granularity);
		// First get the list of data points from DB
		List<TagData> tagDataList = tagDataEJB.getDataByGranularityInTimeWindow(crisisCode, attributeCode, null, startTime, granularity);

		// Now the real work - count and send response
		TimeWindowTagCountDTO dto = new TimeWindowTagCountDTO();
		JSONObject json = new JsonResponse().getNewJsonResponseObject(crisisCode, attributeCode, granularity, startTime, null);

		if (tagDataList != null) {
			try {
				List<TagCountDTO> dtoList = new ArrayList<TagCountDTO>();
				for (TagData t: tagDataList) {
					dtoList.add(TagCountDTOHelper.convertTagDataToDTO(t));
					logger.info("tag: " + t.getLabelCode() + ", count: " + t.getCount());
				}
				dto = TimeWindowTagCountDTOHelper.convertTagCountDTOListToDTO(startTime, dtoList);
				json.put("data", dto);
			} catch (Exception e) {
				json = JsonResponse.addError(json);
				logger.error("Error in serializing fetched tag count data", e);
			}
		}
		return json;
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
	public JSONObject getTagCountTimeSeries(String crisisCode, String attributeCode, Long granularity, Long startTime, Long endTime) {

		if (null == endTime || endTime < startTime) {
			endTime = System.currentTimeMillis();
		}
		//long timeGranularity = DateFormatConfig.parseTime(granularity);
		// First get the list of data points from DB
		List<TagData> tagDataList = tagDataEJB.getDataInIntervalWithGranularity(crisisCode, attributeCode, null, startTime, endTime, granularity);

		// Now the real work - creat time series, format and send response
		JSONObject json = new JsonResponse().getNewJsonResponseObject(crisisCode, attributeCode, granularity, startTime, endTime);
		
		if (tagDataList != null) {
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
			try {
				for (Long key: tagCountMap.keySet()) {
					TimeWindowTagCountDTO timeWindowDTO = TimeWindowTagCountDTOHelper.convertTagCountDTOListToDTO(key, tagCountMap.get(key));
					timeWindowDTOList.add(timeWindowDTO);
				}
				//System.out.println("Finished creating TimeWindowTagCountDTO list");
				TagCountSeriesDTO dto = TagCountSeriesDTOHelper.convertTimeWindowTagCountDTOListToDTO(crisisCode, attributeCode, granularity, timeWindowDTOList);
				json.put("data", dto);
				//System.out.println("Finished creating json object: " + json);
				return json;
			} catch (Exception e) {
				json = JsonResponse.addError(json);
				logger.error("Error in serializing fetched tag count data", e);
			}
		}
		return json;
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
	public JSONObject getTagCountSumInInterval(String crisisCode, String attributeCode, Long granularity, Long startTime, Long endTime) {

		//long timeGranularity = DateFormatConfig.parseTime(granularity);
		// First get the list of data points from DB
		List<TagData> tagDataList = tagDataEJB.getDataInIntervalWithGranularity(crisisCode, attributeCode, null, startTime, endTime, granularity);

		// Now the real work - count and send response
		JSONObject json = new JsonResponse().getNewJsonResponseObject(crisisCode, attributeCode, granularity, startTime, endTime);

		if (tagDataList != null) {
			Map<String, Integer> tagCountMap = new TreeMap<String, Integer>();
			for (TagData t: tagDataList) {
				logger.info("Looking at tag: " + t.getLabelCode() + ", having count = " + t.getCount());
				if (tagCountMap.containsKey(t.getLabelCode())) {
					tagCountMap.put(t.getLabelCode(), tagCountMap.get(t.getLabelCode()) + t.getCount());
					logger.info("Update Map for OLD tag = " + t.getLabelCode() + " with count = " + tagCountMap.get(t.getLabelCode()));
				} else {
					tagCountMap.put(t.getLabelCode(), t.getCount());
					logger.info("Update Map with NEW tag = " + t.getLabelCode() + " with count = " + tagCountMap.get(t.getLabelCode()));
				}
			}
			try {
				json.put("data", tagCountMap);
			} catch (Exception e) {
				json = JsonResponse.addError(json);
				logger.error("Error in serializing fetched tag count data", e);
			}
		}
		return json;
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
	public JSONObject getSingleItem(String crisisCode, String attributeCode, String labelCode, Long granularity, Long startTime) {

		//long timeGranularity = DateFormatConfig.parseTime(granularity);
		TagDataPK tagDataPK = new TagDataPK();
		tagDataPK.setCrisisCode(crisisCode);
		tagDataPK.setTimestamp(startTime);
		tagDataPK.setGranularity(granularity);
		tagDataPK.setAttributeCode(attributeCode);
		tagDataPK.setLabelCode(labelCode);

		TagData obj = tagDataEJB.getSingleDataByPK(tagDataPK);
		JSONObject json = new JsonResponse().getNewJsonResponseObject(crisisCode, attributeCode, granularity, startTime, null);
		try {
			json.put("data", obj);
		} catch (Exception e) {
			json = JsonResponse.addError(json);
			logger.error("Exception:", e);
		}
		return json;
	}

}
