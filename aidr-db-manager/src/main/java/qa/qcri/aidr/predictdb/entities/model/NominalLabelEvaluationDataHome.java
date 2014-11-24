// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class NominalLabelEvaluationData.
 * @see .NominalLabelEvaluationData
 * @author Hibernate Tools
 */
@Stateless
public class NominalLabelEvaluationDataHome {

	private static final Log log = LogFactory
			.getLog(NominalLabelEvaluationDataHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(NominalLabelEvaluationData transientInstance) {
		log.debug("persisting NominalLabelEvaluationData instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(NominalLabelEvaluationData persistentInstance) {
		log.debug("removing NominalLabelEvaluationData instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public NominalLabelEvaluationData merge(
			NominalLabelEvaluationData detachedInstance) {
		log.debug("merging NominalLabelEvaluationData instance");
		try {
			NominalLabelEvaluationData result = entityManager
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NominalLabelEvaluationData findById(NominalLabelEvaluationDataId id) {
		log.debug("getting NominalLabelEvaluationData instance with id: " + id);
		try {
			NominalLabelEvaluationData instance = entityManager.find(
					NominalLabelEvaluationData.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
