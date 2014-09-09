package qa.qcri.aidr.predictui.facade.imp;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.predictui.facade.TaskBufferScannerFacade;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;


/**
 *
 * @author Koushik
 */
@Stateless
public class TaskBufferScannerImp implements TaskBufferScannerFacade {

	private static Logger logger = Logger.getLogger(TaskBufferScannerImp.class);
	private static ErrorLog elog = new ErrorLog();
	
	//@PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
	//private EntityManager em;
	@EJB
	private TaskManagerRemote<qa.qcri.aidr.task.entities.Document, Long> taskManager;

	@Override
	public void ScanTaskBuffer(final String maxTaskAge, final String scanInterval) {
		try {
			/*String deleteStaleDocsSql = "DELETE d FROM aidr_predict.document d LEFT JOIN "
					+ "aidr_predict.task_assignment t ON d.documentID = t.documentID WHERE "
					+ "(!d.hasHumanLabels && t.documentID IS NULL && TIMESTAMPDIFF(" 
					+ getMetric(scanInterval) + ", d.receivedAt, now()) > :task_expiry_age);";

			Query querySelect1 = em.createNativeQuery(deleteStaleDocsSql);
			querySelect1.setParameter("task_expiry_age", Integer.parseInt(getTimeValue(maxTaskAge)));

			// no need - Hibernate automatically acquires LockModeType.WRITE lock
			// Also, test showed that the problem is still coming from trainer-api
			//querySelect1.setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT);		
			int result = querySelect1.executeUpdate();
			*/
			int result = taskManager.deleteStaleTasks("LEFT JOIN", 
											"aidr_predict.task_assignment", "documentID", 
											"ASC", null, maxTaskAge, scanInterval);
			logger.info("number of deleted stale records = " + result);
		} catch (Exception e) {
			logger.error("Exception in executing SQL delete stale docs query");
			logger.error(elog.toStringException(e));
		}
		try {
			/*
			String deleteNoAnswerDocsSql = "DELETE d FROM aidr_predict.document d JOIN "
					+ "aidr_predict.task_assignment t ON d.documentID = t.documentID WHERE "
					+ "(!d.hasHumanLabels && TIMESTAMPDIFF(" 
					+ getMetric(scanInterval) + ", t.assignedAt, now()) > :task_expiry_age);";
			
			Query querySelect2 = em.createNativeQuery(deleteNoAnswerDocsSql);
			querySelect2.setParameter("task_expiry_age", Integer.parseInt(getTimeValue(maxTaskAge)));

			// no need - Hibernate automatically acquires LockModeType.WRITE lock
			// Also, test showed that the problem is still coming from trainer-api
			//querySelect2.setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT);		
			int result = querySelect2.executeUpdate();
			*/
			int result = taskManager.deleteStaleTasks("JOIN", 
										"aidr_predict.task_assignment", "documentID", 
										"ASC", null, maxTaskAge, scanInterval);
			logger.info("number of deleted no answer records = " + result);
		} catch (Exception e) {
			logger.error("Exception in executing SQL delete no answer docs query");
			logger.error(elog.toStringException(e));
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
