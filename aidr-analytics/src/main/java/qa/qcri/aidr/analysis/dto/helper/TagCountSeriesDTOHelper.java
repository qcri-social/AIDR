package qa.qcri.aidr.analysis.dto.helper;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.analysis.dto.TagCountSeriesDTO;
import qa.qcri.aidr.analysis.dto.TimeWindowTagCountDTO;

public class TagCountSeriesDTOHelper {
	public static TagCountSeriesDTO convertTimeWindowTagCountDTOListToDTO(String crisisCode, String attributeCode, 
																			Long granularity, List<TimeWindowTagCountDTO> tList) {
		TagCountSeriesDTO dto = new TagCountSeriesDTO ();
		dto.setCrisisCode(crisisCode);
		dto.setAttributeCode(attributeCode);
		dto.setGranularity(granularity);
		dto.setTimeSeriesData(tList);
		dto.setTotal(tList.size());
		
		return dto;
	}
	
	public static List<TimeWindowTagCountDTO> convertDTOToTimeWindowTagCountDTOList(TagCountSeriesDTO dto) {
		List<TimeWindowTagCountDTO> tList = new ArrayList<TimeWindowTagCountDTO>();
		if (dto.getTimeSeriesData() != null) {
			for (TimeWindowTagCountDTO t: dto.getTimeSeriesData()) {
				tList.add(t);
			}
		}		
		return tList;
	}
}
