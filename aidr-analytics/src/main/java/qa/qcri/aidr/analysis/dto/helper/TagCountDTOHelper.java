package qa.qcri.aidr.analysis.dto.helper;

import qa.qcri.aidr.analysis.dto.TagCountDTO;
import qa.qcri.aidr.analysis.entity.TagData;

public class TagCountDTOHelper {
	public static TagCountDTO convertTagDataToDTO(TagData t) {
		TagCountDTO dto = new TagCountDTO();
		dto.setLabelCode(t.getLabelCode());
		dto.setCount(t.getCount());
		
		return dto;
	}
	
	public static TagData convertDTOToTagData(TagCountDTO dto) {
		TagData t = new TagData();
		t.setLabelCode(dto.getLabelCode());
		t.setCount(dto.getCount());
		
		return t;
	}
}
