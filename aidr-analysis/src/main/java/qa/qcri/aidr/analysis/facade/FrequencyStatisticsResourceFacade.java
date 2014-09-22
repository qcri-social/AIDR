package qa.qcri.aidr.analysis.facade;

import javax.ejb.Local;

import qa.qcri.aidr.analysis.entity.FrequencyData;
import qa.qcri.aidr.analysis.entity.FrequencyDataPK;


@Local
public interface FrequencyStatisticsResourceFacade {
	public int writeData(FrequencyData freqData);
	public FrequencyData getDataByPK(FrequencyDataPK freqDataPK);
}
