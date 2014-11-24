// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class NominalLabelTrainingData.
 * @see .NominalLabelTrainingData
 * @author Hibernate Tools
 */
@Stateless
public class NominalLabelTrainingDataHome {

	private static final Log log = LogFactory
			.getLog(NominalLabelTrainingDataHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(NominalLabelTrainingData transientInstance) {
		log.debug("persisting NominalLabelTrainingData instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(NominalLabelTrainingData persistentInstance) {
		log.debug("removing NominalLabelTrainingData instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public NominalLabelTrainingData merge(
			NominalLabelTrainingData detachedInstance) {
		log.debug("merging NominalLabelTrainingData instance");
		try {
			NominalLabelTrainingData result = entityManager
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NominalLabelTrainingData findById(NominalLabelTrainingDataId id) {
		log.debug("getting NominalLabelTrainingData instance with id: " + id);
		try {
			NominalLabelTrainingData instance = entityManager.find(
					NominalLabelTrainingData.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
