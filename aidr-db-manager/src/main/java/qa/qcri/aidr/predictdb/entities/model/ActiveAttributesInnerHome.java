// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class ActiveAttributesInner.
 * @see .ActiveAttributesInner
 * @author Hibernate Tools
 */
@Stateless
public class ActiveAttributesInnerHome {

	private static final Log log = LogFactory
			.getLog(ActiveAttributesInnerHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(ActiveAttributesInner transientInstance) {
		log.debug("persisting ActiveAttributesInner instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(ActiveAttributesInner persistentInstance) {
		log.debug("removing ActiveAttributesInner instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public ActiveAttributesInner merge(ActiveAttributesInner detachedInstance) {
		log.debug("merging ActiveAttributesInner instance");
		try {
			ActiveAttributesInner result = entityManager
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ActiveAttributesInner findById(ActiveAttributesInnerId id) {
		log.debug("getting ActiveAttributesInner instance with id: " + id);
		try {
			ActiveAttributesInner instance = entityManager.find(
					ActiveAttributesInner.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
