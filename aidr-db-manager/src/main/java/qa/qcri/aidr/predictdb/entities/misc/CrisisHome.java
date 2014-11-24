// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.misc;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Crisis.
 * @see .Crisis
 * @author Hibernate Tools
 */
@Stateless
public class CrisisHome {

	private static final Log log = LogFactory.getLog(CrisisHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Crisis transientInstance) {
		log.debug("persisting Crisis instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Crisis persistentInstance) {
		log.debug("removing Crisis instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Crisis merge(Crisis detachedInstance) {
		log.debug("merging Crisis instance");
		try {
			Crisis result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Crisis findById(Integer id) {
		log.debug("getting Crisis instance with id: " + id);
		try {
			Crisis instance = entityManager.find(Crisis.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
