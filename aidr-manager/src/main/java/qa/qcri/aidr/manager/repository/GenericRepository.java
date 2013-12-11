package qa.qcri.aidr.manager.repository;

import java.io.Serializable;
import java.util.List;

public interface GenericRepository<T, ID extends Serializable> {
	public T findById(ID id);

	public List<T> findAll();

	public void save(T entity);

	public void update(T entity);

	public void delete(T entity);
}
