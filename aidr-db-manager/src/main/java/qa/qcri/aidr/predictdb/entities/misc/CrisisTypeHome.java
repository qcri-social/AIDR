// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0

package qa.qcri.aidr.predictdb.entities.misc;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class CrisisType.
 * @see .CrisisType
 * @author Hibernate Tools
 */
@Stateless
public class CrisisTypeHome {

	private static final Log log = LogFactory.getLog(CrisisTypeHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(CrisisType transientInstance) {
		log.debug("persisting CrisisType instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(CrisisType persistentInstance) {
		log.debug("removing CrisisType instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public CrisisType merge(CrisisType detachedInstance) {
		log.debug("merging CrisisType instance");
		try {
			CrisisType result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CrisisType findById(Integer id) {
		log.debug("getting CrisisType instance with id: " + id);
		try {
			CrisisType instance = entityManager.find(CrisisType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
