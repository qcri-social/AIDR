package qa.qcri.aidr.predictdb.ejb.local.task;


import java.util.List;

import javax.ejb.Local;


@Local
public interface CrisisService extends AbstractTaskManagerService<Crisis, Long> {

	Crisis findByCrisisID(Long id);
	List<Crisis> findByCriteria(String columnName, Long value);
	List<Crisis> findByCriteria(String columnName, String value);
}
