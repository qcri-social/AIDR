package qa.qcri.aidr.analysis.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.analysis.entity.TagDataPK;
import qa.qcri.aidr.common.values.ReturnCode;

@Local
public interface TagDataStatisticsResourceFacade {
	public ReturnCode writeData(TagData tagData);
	public TagData getDataByPK(TagDataPK tagDataPK);
	public List<TagData> getDataByCrisisAttributeLabel(String crisisCode, String attributeCode, String labelCode);
}
