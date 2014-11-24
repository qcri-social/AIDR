// default package
// Generated Nov 24, 2014 4:55:08 PM by Hibernate Tools 4.0.0
package qa.qcri.aidr.predictdb.entities.task;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class TaskAssignment.
 * @see .TaskAssignment
 * @author Hibernate Tools
 */
@Stateless
public class TaskAssignmentHome {

	private static final Log log = LogFactory.getLog(TaskAssignmentHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(TaskAssignment transientInstance) {
		log.debug("persisting TaskAssignment instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaskAssignment persistentInstance) {
		log.debug("removing TaskAssignment instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public TaskAssignment merge(TaskAssignment detachedInstance) {
		log.debug("merging TaskAssignment instance");
		try {
			TaskAssignment result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TaskAssignment findById(TaskAssignmentId id) {
		log.debug("getting TaskAssignment instance with id: " + id);
		try {
			TaskAssignment instance = entityManager.find(TaskAssignment.class,
					id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
