
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.predictui.facade;

import javax.ejb.Local;

/**
 *
 * @author Koushik
 */
@Local
public interface TaskBufferScannerFacade {    
	   
	   public void ScanTaskBuffer(final String maxTaskAge, final String scanInterval);
	   public long parseTime(final String timeString);
	   public String getTimeValue(final String timeString);
	   public String getMetric(final String timeString);

}
