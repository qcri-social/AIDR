package qa.qcri.aidr.trainer.api.dao;

import org.hibernate.criterion.Criterion;

import java.io.Serializable;
import java.util.List;

public interface AbstractDao<E, I extends Serializable> {

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
