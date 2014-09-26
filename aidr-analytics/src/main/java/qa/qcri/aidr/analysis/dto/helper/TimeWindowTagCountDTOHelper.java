package qa.qcri.aidr.analysis.dto.helper;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.analysis.dto.TagCountDTO;
import qa.qcri.aidr.analysis.dto.TimeWindowTagCountDTO;


public class TimeWindowTagCountDTOHelper {
	public static TimeWindowTagCountDTO convertTagCountDTOListToDTO(long timestamp, List<TagCountDTO> tList) {
		TimeWindowTagCountDTO dto = new TimeWindowTagCountDTO();
		dto.setTimestamp(timestamp);
		dto.setTagCountData(tList);
		return dto;
	}
	
	public static List<TagCountDTO> convertDTOToTagCountDTOList(TimeWindowTagCountDTO dto) {
		List<TagCountDTO> tList = new ArrayList<TagCountDTO>();
		if (dto.getTagCountData() != null) {
			for (TagCountDTO t: dto.getTagCountData()) {
				tList.add(t);
			}
		}		
		return tList;
	}
}
