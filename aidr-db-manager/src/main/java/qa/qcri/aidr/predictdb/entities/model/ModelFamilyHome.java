// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class ModelFamily.
 * @see .ModelFamily
 * @author Hibernate Tools
 */
@Stateless
public class ModelFamilyHome {

	private static final Log log = LogFactory.getLog(ModelFamilyHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(ModelFamily transientInstance) {
		log.debug("persisting ModelFamily instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(ModelFamily persistentInstance) {
		log.debug("removing ModelFamily instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public ModelFamily merge(ModelFamily detachedInstance) {
		log.debug("merging ModelFamily instance");
		try {
			ModelFamily result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ModelFamily findById(Integer id) {
		log.debug("getting ModelFamily instance with id: " + id);
		try {
			ModelFamily instance = entityManager.find(ModelFamily.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
