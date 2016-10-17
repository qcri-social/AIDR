package qa.qcri.aidr.trainer.api.service;


import qa.qcri.aidr.trainer.api.entity.ImageTaskQueue;

import java.util.List;

public interface ImageTaskQueueService {

    List<ImageTaskQueue> getImageTaskQueueSetByTask(Long taskID, Long clientAppID);
    List<ImageTaskQueue> getImageTaskQueueByClientApp(Long clientAppID);
	Long getCountImageTaskByClientApp(Long clientAppID);
    List<ImageTaskQueue> getImageTaskQueueByCrisis(Long crisisID);
    Long getCountImageTaskByCrisis(Long crisisID);

}
