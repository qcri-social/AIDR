// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class ActiveAttributes.
 * @see .ActiveAttributes
 * @author Hibernate Tools
 */
@Stateless
public class ActiveAttributesHome {

	private static final Log log = LogFactory
			.getLog(ActiveAttributesHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(ActiveAttributes transientInstance) {
		log.debug("persisting ActiveAttributes instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(ActiveAttributes persistentInstance) {
		log.debug("removing ActiveAttributes instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public ActiveAttributes merge(ActiveAttributes detachedInstance) {
		log.debug("merging ActiveAttributes instance");
		try {
			ActiveAttributes result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ActiveAttributes findById(ActiveAttributesId id) {
		log.debug("getting ActiveAttributes instance with id: " + id);
		try {
			ActiveAttributes instance = entityManager.find(
					ActiveAttributes.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
