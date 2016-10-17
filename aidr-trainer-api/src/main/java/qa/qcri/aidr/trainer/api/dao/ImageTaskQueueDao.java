package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.dto.ImageTaskQueueDTO;
import qa.qcri.aidr.trainer.api.entity.ImageTaskQueue;

import java.util.List;


public interface ImageTaskQueueDao extends AbstractDao<ImageTaskQueue, String>  {

    List<ImageTaskQueue> findImageTaskQueue(Long taskID, Long clientAppID);
    List<ImageTaskQueue> findImageTaskQueueSetByClientApp(Long clientAppID);
    List<ImageTaskQueue> findImageTaskQueueSetByCrisis(Long crisisID);
	Long findImageTaskQueueCountByCrisis(Long crisisID);
	List<ImageTaskQueueDTO> findImageTaskQueueSetByCrisis(Long crisisID, int fromRecord, int limit, String sortColumn,
			String sortDirection);
}
