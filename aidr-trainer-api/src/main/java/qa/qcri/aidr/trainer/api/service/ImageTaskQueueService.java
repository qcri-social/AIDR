package qa.qcri.aidr.trainer.api.service;

import qa.qcri.aidr.trainer.api.dto.ImageTaskQueueDTO;
import qa.qcri.aidr.trainer.api.entity.ImageTaskQueue;

import java.util.List;

public interface ImageTaskQueueService {

	public List<ImageTaskQueue> getImageTaskQueueSetByTask(Long taskID, Long clientAppID);
    public List<ImageTaskQueue> getImageTaskQueueByClientApp(Long clientAppID);
    public Long getCountImageTaskByClientApp(Long clientAppID);
	public List<ImageTaskQueue> getImageTaskQueueByCrisis(Long crisisID);

	public Long getCountImageTaskByCrisis(Long crisisID);
    public List<ImageTaskQueueDTO> getImageTaskQueueByCrisis(Long crisisID, int fromRecord, int limit, String sortColumn, String sortDirection);


}
