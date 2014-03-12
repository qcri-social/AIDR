package qa.qcri.aidr.predictui.facade.imp;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.qcri.aidr.predictui.facade.TaskBufferScannerFacade;


/**
 *
 * @author Koushik
 */
@Stateless
public class TaskBufferScannerImp implements TaskBufferScannerFacade {

	private static Logger logger = LoggerFactory.getLogger(TaskBufferScannerImp.class);
	
	@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	private EntityManager em;

	@Override
	public void ScanTaskBuffer(final String maxTaskAge, final String scanInterval) {
		// TODO Auto-generated method stub
		try {
			String deleteStaleDocsSql = "DELETE * FROM aidr_predict.document t LEFT JOIN "
					+ "aidr_predict.task_assignment b ON t.documentID = b.documentID WHERE "
					+ "(b.documentID IS NULL && TIMESTAMPDIFF(" 
					+ getMetric(scanInterval) + ", t.receivedAt, now()) > :task_expiry_age);";
			
				Query querySelect = em.createNativeQuery(deleteStaleDocsSql);
				querySelect.setParameter("task_expiry_age", Integer.parseInt(getTimeValue(maxTaskAge)));
				
				// no need - Hibernate automatically acquires LockModeType.WRITE lock
				// Also, test showed that the problem is still coming from trainer-api
				//querySelect.setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT);		
				int result = querySelect.executeUpdate();
				logger.info("[ScanTaskBuffer] number of deleted records = " + result);
		} catch (Exception e) {
			logger.error("[ScanTaskBuffer] Exception in executing SQL query");
			e.printStackTrace();
		}
		try {
			String deleteNoAnswerDocsSql = "DELETE * FROM aidr_predict.document d WHERE "
					+ " (d.documentID IN (SELECT t.documentID from task_assignment t "
					+ " LEFT JOIN aidr_predict.task_answer s ON t.documentID = s.documentID WHERE " 
					+ " ((s.documentID IS NULL) && (TIMESTAMPDIFF(" 
					+ getMetric(scanInterval) + ", t.assignedAt, now()) > :task_expiry_age))));";
			
				Query querySelect = em.createNativeQuery(deleteNoAnswerDocsSql);
				querySelect.setParameter("task_expiry_age", Integer.parseInt(getTimeValue(maxTaskAge)));
				
				// no need - Hibernate automatically acquires LockModeType.WRITE lock
				// Also, test showed that the problem is still coming from trainer-api
				//querySelect.setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT);		
				int result = querySelect.executeUpdate();
				logger.info("[ScanTaskBuffer] number of deleted records = " + result);
		} catch (Exception e) {
			logger.error("[ScanTaskBuffer] Exception in executing SQL query");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param timeString
	 * @return duration in milliseconds. Negative indicates an invalid parse result
	 */
	@Override
	public long parseTime(final String timeString) {
		long duration = -1;		
		assert timeString != null;
		float value = Float.parseFloat(timeString.substring(0, timeString.length()-1));
		if (value > 0) {
			String suffix = timeString.substring(timeString.length() - 1, timeString.length());
			if (suffix != null) {
				if (suffix.equalsIgnoreCase("s"))
					duration = Math.round(value * 1000);
				else if (suffix.equalsIgnoreCase("m"))
					duration = Math.round(value * 60 * 1000) ;
				else if (suffix.equalsIgnoreCase("h"))
					duration = Math.round(value * 60 * 60 * 1000);
				else if (suffix.equalsIgnoreCase("d"))
					duration = Math.round(value * 60 * 60 * 24 * 1000);
				else
					duration = Math.round(value * 60 * 1000);		// default is minutes
			}
			else
				duration = Math.round(value * 60 * 1000);		// default is minutes
		}
		return duration;
	}

	@Override
	public String getTimeValue(final String timeString) {
		assert timeString != null;
		return timeString.substring(0, timeString.length()-1);
	}

	@Override
	public String getMetric(final String timeString) {
		assert timeString != null;
		String metric = "HOUR";			// default
		String suffix = timeString.substring(timeString.length() - 1, timeString.length());
		if (suffix != null) {
			if (suffix.equalsIgnoreCase("s"))
				metric = "SECOND"; 
			else if (suffix.equalsIgnoreCase("m"))
				metric = "MINUTE";
			else if (suffix.equalsIgnoreCase("h"))
				metric = "HOUR";
			else if (suffix.equalsIgnoreCase("d"))
				metric = "DAY";
		}
		return metric;
	}
}
