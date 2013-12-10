package qa.qcri.aidr.trainer.pybossa.dao;

import org.hibernate.criterion.Criterion;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/18/13
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AbstractDao<E, I extends Serializable> {

    E findById(I id);
    List<E> findAll();
    void save(E e);
    void saveOrUpdate(E e);
    void delete(E e);
    List<E> findByCriteria(Criterion criterion);
    E findByCriterionID(Criterion criterion);
    List<E> findByCriteria(Criterion criterion, Integer count) ;
}
