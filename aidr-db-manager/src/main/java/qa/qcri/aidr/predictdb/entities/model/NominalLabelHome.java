// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class NominalLabel.
 * @see .NominalLabel
 * @author Hibernate Tools
 */
@Stateless
public class NominalLabelHome {

	private static final Log log = LogFactory.getLog(NominalLabelHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(NominalLabel transientInstance) {
		log.debug("persisting NominalLabel instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(NominalLabel persistentInstance) {
		log.debug("removing NominalLabel instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public NominalLabel merge(NominalLabel detachedInstance) {
		log.debug("merging NominalLabel instance");
		try {
			NominalLabel result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NominalLabel findById(Integer id) {
		log.debug("getting NominalLabel instance with id: " + id);
		try {
			NominalLabel instance = entityManager.find(NominalLabel.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
