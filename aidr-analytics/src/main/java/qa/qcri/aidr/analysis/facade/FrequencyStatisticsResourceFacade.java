package qa.qcri.aidr.analysis.facade;

import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.analysis.entity.FrequencyData;
import qa.qcri.aidr.analysis.entity.FrequencyDataPK;
import qa.qcri.aidr.analysis.entity.TagData;
import qa.qcri.aidr.common.values.ReturnCode;


@Local
public interface FrequencyStatisticsResourceFacade {
	public ReturnCode writeData(FrequencyData freqData);
	
	public FrequencyData getSingleDataByPK(FrequencyDataPK freqDataPK);
	
	public List<FrequencyData> getDataByCrisis(String crisisCode);
	
	public List<FrequencyData> getDataByCrisisAttributeLabel(String crisisCode, String attributeCode, String labelCode);
	
	public List<FrequencyData> getDataByCrisisAttributeLabelGranularity(String crisisCode, String attributeCode, String labelCode, Long granularity);
	
	public List<FrequencyData> getDataAfterTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp);
	
	public List<FrequencyData> getDataAfterTimestampGranularity(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity);
	
	public List<FrequencyData> getDataBeforeTimestamp(String crisisCode, String attributeCode, String labelCode, Long timestamp);
	
	public List<FrequencyData> getDataBeforeTimestampGranularity(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity);
	
	
	public List<FrequencyData> getDataInInterval(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp1, Long timestamp2);
	
	public List<FrequencyData> getDataInIntervalWithGranularity(String crisisCode, String attributeCode, String labelCode, 
			  											Long timestamp1, Long timestamp2, Long granularity);
	
	////////////////////////////////////////////////
	// 				Bin related
	///////////////////////////////////////////////
	public List<FrequencyData> getDataByCrisisWithBin(String crisisCode, Integer bin);
	
	public List<FrequencyData> getDataByCrisisAttributeLabelWithBin(String crisisCode, String attributeCode, String labelCode, Integer bin);
	
	public List<FrequencyData> getDataByCrisisAttributeLabelGranularityWithBin(String crisisCode, String attributeCode, String labelCode, 
														Long granularity, Integer bin);
	
	public List<FrequencyData> getDataAfterTimestampWithBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Integer bin);
	
	public List<FrequencyData> getDataAfterTimestampGranularityWithBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity, Integer bin);
	
	public List<FrequencyData> getDataBeforeTimestampWithBin(String crisisCode, String attributeCode, String labelCode, Long timestamp, Integer bin);
	
	public List<FrequencyData> getDataBeforeTimestampGranularityWithBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity, Integer bin);
	
	
	public List<FrequencyData> getDataInIntervalWithBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp1, Long timestamp2, Integer bin);
	
	public List<FrequencyData> getDataInIntervalWithGranularityWithBin(String crisisCode, String attributeCode, String labelCode, 
			  											Long timestamp1, Long timestamp2, Long granularity, Integer bin);
	
	public List<FrequencyData> getDataByCrisisInBin(String crisisCode, Integer bin);
	
	public List<FrequencyData> getDataByCrisisAttributeLabelInBin(String crisisCode, String attributeCode, String labelCode, Integer bin);
	
	public List<FrequencyData> getDataByCrisisAttributeLabelGranularityInBin(String crisisCode, String attributeCode, String labelCode, 
														Long granularity, Integer bin);
	
	public List<FrequencyData> getDataAfterTimestampInBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Integer bin);
	
	public List<FrequencyData> getDataAfterTimestampGranularityInBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity, Integer bin);
	
	public List<FrequencyData> getDataBeforeTimestampInBin(String crisisCode, String attributeCode, String labelCode, Long timestamp, Integer bin);
	
	public List<FrequencyData> getDataBeforeTimestampGranularityInBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp, Long granularity, Integer bin);
	
	
	public List<FrequencyData> getDataInIntervalInBin(String crisisCode, String attributeCode, String labelCode, 
														Long timestamp1, Long timestamp2, Integer bin);
	
	public List<FrequencyData> getDataInIntervalWithGranularityInBin(String crisisCode, String attributeCode, String labelCode, 
			  											Long timestamp1, Long timestamp2, Long granularity, Integer bin);
}
