// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.task;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class TaskAnswer.
 * @see .TaskAnswer
 * @author Hibernate Tools
 */
@Stateless
public class TaskAnswerHome {

	private static final Log log = LogFactory.getLog(TaskAnswerHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(TaskAnswer transientInstance) {
		log.debug("persisting TaskAnswer instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaskAnswer persistentInstance) {
		log.debug("removing TaskAnswer instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public TaskAnswer merge(TaskAnswer detachedInstance) {
		log.debug("merging TaskAnswer instance");
		try {
			TaskAnswer result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TaskAnswer findById(TaskAnswerId id) {
		log.debug("getting TaskAnswer instance with id: " + id);
		try {
			TaskAnswer instance = entityManager.find(TaskAnswer.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
