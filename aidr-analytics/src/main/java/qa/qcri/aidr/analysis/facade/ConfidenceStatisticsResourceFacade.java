/**
 * This facade is not used at the moment.
 */
package qa.qcri.aidr.analysis.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.analysis.entity.ConfidenceData;
import qa.qcri.aidr.analysis.entity.ConfidenceDataPK;
import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.common.values.ReturnCode;


@Local
public interface ConfidenceStatisticsResourceFacade {
	public ReturnCode writeData(ConfidenceData freqData);
	
	public ConfidenceData getSingleDataByPK(ConfidenceDataPK freqDataPK);
	
	public List<ConfidenceData> getDataByCrisis(String crisisCode);
	
	public List<ConfidenceData> getDataByCrisisAttributeLabel(String crisisCode, String attributeCode, String labelCode);
	
	public List<ConfidenceData> getDataByCrisisAttributeLabelGranularity(String crisisCode, String attributeCode, String labelCode, Long granularity);
	
	public List<ConfidenceData> getDataAfterTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp);
	
	public List<ConfidenceData> getDataAfterTimestampGranularity(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity);
	
	public List<ConfidenceData> getDataBeforeTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp);
	
	public List<ConfidenceData> getDataBeforeTimestampGranularity(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity);
	
	
	public List<ConfidenceData> getDataInInterval(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp1, Long timestamp2);
	
	public List<ConfidenceData> getDataInIntervalWithGranularity(String crisisCode, String attributeCode, String labelCode, 
			  											Long timestamp1, Long timestamp2, Long granularity);
	
	////////////////////////////////////////////////
	// 				Bin related
	///////////////////////////////////////////////
	public List<ConfidenceData> getDataByCrisisWithBin(String crisisCode, Integer bin);
	
	public List<ConfidenceData> getDataByCrisisAttributeLabelWithBin(String crisisCode, String attributeCode, String labelCode, Integer bin);
	
	public List<ConfidenceData> getDataByCrisisAttributeLabelGranularityWithBin(String crisisCode, String attributeCode, String labelCode, 
														Long granularity, Integer bin);
	
	public List<ConfidenceData> getDataAfterTimestampWithBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Integer bin);
	
	public List<ConfidenceData> getDataAfterTimestampGranularityWithBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity, Integer bin);
	
	public List<ConfidenceData> getDataBeforeTimestampWithBin(String crisisCode, String attributeCode, String labelCode, Long timestamp, Integer bin);
	
	public List<ConfidenceData> getDataBeforeTimestampGranularityWithBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity, Integer bin);
	
	
	public List<ConfidenceData> getDataInIntervalWithBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp1, Long timestamp2, Integer bin);
	
	public List<ConfidenceData> getDataInIntervalWithGranularityWithBin(String crisisCode, String attributeCode, String labelCode, 
			  											Long timestamp1, Long timestamp2, Long granularity, Integer bin);
	
	public List<ConfidenceData> getDataByCrisisInBin(String crisisCode, Integer bin);
	
	public List<ConfidenceData> getDataByCrisisAttributeLabelInBin(String crisisCode, String attributeCode, String labelCode, Integer bin);
	
	public List<ConfidenceData> getDataByCrisisAttributeLabelGranularityInBin(String crisisCode, String attributeCode, String labelCode, 
														Long granularity, Integer bin);
	
	public List<ConfidenceData> getDataAfterTimestampInBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Integer bin);
	
	public List<ConfidenceData> getDataAfterTimestampGranularityInBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity, Integer bin);
	
	public List<ConfidenceData> getDataBeforeTimestampInBin(String crisisCode, String attributeCode, String labelCode, Long timestamp, Integer bin);
	
	public List<ConfidenceData> getDataBeforeTimestampGranularityInBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity, Integer bin);
	
	
	public List<ConfidenceData> getDataInIntervalInBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp1, Long timestamp2, Integer bin);
	
	public List<ConfidenceData> getDataInIntervalWithGranularityInBin(String crisisCode, String attributeCode, String labelCode, 
			  											Long timestamp1, Long timestamp2, Long granularity, Integer bin);
}
