package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

import qa.qcri.aidr.manager.repository.GenericRepository;

public abstract class GenericRepositoryImpl<T, ID extends Serializable> implements GenericRepository<T, ID> {
    private Class<T> entityType;
    
    @Autowired
    private HibernateTemplate hibernateTemplate;
    
    public GenericRepositoryImpl()  {
        this.entityType = getEntityType();
    }  

	@SuppressWarnings("unchecked")
    protected final Class<T> getEntityType() {
		
        Class<? extends Object> thisClass = getClass();
		Type genericSuperclass = thisClass.getGenericSuperclass();
		if( genericSuperclass instanceof ParameterizedType ) {
			Type[] argumentTypes = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
	        Class<T> entityBeanType = (Class<T>)argumentTypes[0];
	        return entityBeanType;
		} else {
			return null;
		}
    }
    
    @Override
    @SuppressWarnings(value="unchecked")
    public T findById(ID id) {
        return (T) hibernateTemplate.get(entityType.getName(), id);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<T> findAll() {
        return hibernateTemplate.execute(new HibernateCallback<List<T>>() {
            public List<T> doInHibernate(Session session) throws HibernateException {
                Criteria criteria = session.createCriteria(GenericRepositoryImpl.this.entityType);
                return criteria.list();
            }
        });
    }

    @Override
    public void save(T entity) {
    	hibernateTemplate.save(entity);
    }
    
    @Override
    public void update(T entity) {
    	hibernateTemplate.update(entity);
    }
    
    @Override
    public void delete(T entity) {
    	hibernateTemplate.delete(entity);
    }
    
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
    
}