package qa.qcri.aidr.analysis.utils;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import qa.qcri.aidr.common.logging.ErrorLog;

public class CommonOperations {
	private Logger logger = Logger.getLogger(CommonOperations.class);
	private ErrorLog elog = new ErrorLog();
	
	
	public Session getCurrentSession(EntityManager em) {
		try { 
			return em.unwrap(Session.class);
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return null;
	}
}
