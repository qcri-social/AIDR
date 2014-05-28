package qa.qcri.aidr.task.ejb;


import java.util.List;

import javax.ejb.Local;

import qa.qcri.aidr.task.entities.Crisis;


@Local
public interface CrisisService extends AbstractTaskManagerService<Crisis, Long> {

	Crisis findByCrisisID(Long id);
	List<Crisis> findByCriteria(String columnName, Long value);
	List<Crisis> findByCriteria(String columnName, String value);
}
