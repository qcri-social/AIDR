package qa.qcri.aidr.analysis.utils;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.Session;


public class CommonOperations {
	private Logger logger = Logger.getLogger(CommonOperations.class);
	
	public Session getCurrentSession(EntityManager em) {
		try { 
			return em.unwrap(Session.class);
		} catch (Exception e) {
			logger.error("Error in getCurrentSession()", e);
		}
		return null;
	}
}
