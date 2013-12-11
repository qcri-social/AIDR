package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.manager.hibernateEntities.AidrTask;
import qa.qcri.aidr.manager.repository.TaskRepository;

@Repository("taskRepository")
public class TaskRepositoryImpl extends GenericRepositoryImpl<AidrTask, Serializable> implements TaskRepository{

	@SuppressWarnings("unchecked")
	@Override
	public List<AidrTask> getAllTasksForACollection(Integer collectionId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(AidrTask.class);
		criteria.add(Restrictions.eq("jobId.id", collectionId));
		return (List<AidrTask>) criteria.list();
	}

}
