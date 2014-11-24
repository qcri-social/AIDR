package qa.qcri.aidr.predictdb.ejb.local.task;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;


import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

@Local
public interface AbstractTaskManagerService<E, I extends Serializable> {
	
	public Session getCurrentSession();
	
	public E getById(I id);
	public E getByCriteria(Criterion criterion);
	public E getByCriterionID(Criterion criterion);
	
	public List<E> getAll();
	public List<E> getAllByCriteria(Criterion criterion);
	public List<E> getByCriteriaWithLimit(Criterion criterion, Integer count);
	
	public List<E> getByCriteriaByOrder(Criterion criterion, String order, String[] orderBy, Integer count);
	public List<E> getByCriteriaWithAliasByOrder(Criterion criterion, String order, String[] orderBy, Integer count, String aliasTable, Criterion aliasCriterion);
	public List<E> getByCriteriaWithInnerJoinByOrder(Criterion criterion, String order, String[] orderBy, Integer count, String aliasTable);
	
	public void update(E e);
	public void update(List<E> entityCollection);
	
	public void merge(E e);
	public void merge(List<E> entityCollection);
	
	public void save(E e);
	public void save(List<E> entityCollection);
	
	public void delete(E e);
	public void delete(List<E> entityCollection);
	public void deleteByCriteria(Criterion criterion);

	public EntityManager getEntityManager();
	public int setEntityManager(EntityManager em);
}
