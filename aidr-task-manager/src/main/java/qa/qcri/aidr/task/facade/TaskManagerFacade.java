package qa.qcri.aidr.task.facade;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;

public interface TaskManagerFacade <E, I extends Serializable> {

	E findById(I id);
	void save(E e);
	List<E> findAll();
	void saveOrUpdate(E e);
	void delete(E e);
	List<E> findByCriteria(Criterion criterion);
	E findByCriterionID(Criterion criterion);
	List<E> findByCriteria(Criterion criterion, Integer count) ;
	List<E> findByCriteriaByOrder(Criterion criterion, String[] orderBy, Integer count);
	List<E> findByCriteriaWithAliasByOrder(Criterion criterion, String[] orderBy, Integer count, String aliasTable, Criterion aliasCriterion);

}
