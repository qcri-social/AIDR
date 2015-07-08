/*
 * Interface for operations on the tag_data table
 */
package qa.qcri.aidr.analysis.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.analysis.entity.TagDataPK;
import qa.qcri.aidr.common.values.ReturnCode;


@Local
public interface TagDataStatisticsResourceFacade {
	public ReturnCode writeData(TagData tagData);
	
	public List<TagData> getDataByCrisis(String crisisCode);
	
	public TagData getSingleDataByPK(TagDataPK tagDataPK);
	
	public List<TagData> getDataByCrisisAttributeLabel(String crisisCode, String attributeCode, String labelCode);
	
	public List<TagData> getDataByCrisisAttributeLabelGranularity(String crisisCode, String attributeCode, String labelCode, Long granularity);
	
	public List<TagData> getDataByGranularityInTimeWindow(String crisisCode, String attributeCode, String labelCode, Long timestamp, Long granularity);
	
	public List<TagData> getDataAfterTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp);
	
	public List<TagData> getDataAfterTimestampGranularity(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity);
	
	public List<TagData> getDataBeforeTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp);
	
	public List<TagData> getDataBeforeTimestampGranularity(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity);
	
	
	public List<TagData> getDataInInterval(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp1, Long timestamp2);
	
	public List<TagData> getDataInIntervalWithGranularity(String crisisCode, String attributeCode, String labelCode, 
			  											Long timestamp1, Long timestamp2, Long granularity);
}
