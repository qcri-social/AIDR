// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class ModelNominalLabel.
 * @see .ModelNominalLabel
 * @author Hibernate Tools
 */
@Stateless
public class ModelNominalLabelHome {

	private static final Log log = LogFactory
			.getLog(ModelNominalLabelHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(ModelNominalLabel transientInstance) {
		log.debug("persisting ModelNominalLabel instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(ModelNominalLabel persistentInstance) {
		log.debug("removing ModelNominalLabel instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public ModelNominalLabel merge(ModelNominalLabel detachedInstance) {
		log.debug("merging ModelNominalLabel instance");
		try {
			ModelNominalLabel result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ModelNominalLabel findById(ModelNominalLabelId id) {
		log.debug("getting ModelNominalLabel instance with id: " + id);
		try {
			ModelNominalLabel instance = entityManager.find(
					ModelNominalLabel.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
