package qa.qcri.aidr.trainer.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.trainer.api.dao.ImageTaskQueueDao;
import qa.qcri.aidr.trainer.api.entity.ImageTaskQueue;
import qa.qcri.aidr.trainer.api.service.ImageTaskQueueService;

@Service("imageStatusLookUpService")
@Transactional(readOnly = true)
public class ImageTaskQueueServiceImpl implements ImageTaskQueueService {

    @Autowired
    private ImageTaskQueueDao imageTaskQueueDao;
   
    @Override
    public Long getCountImageTaskByClientApp(Long clientAppID) {
        List<ImageTaskQueue> taskQueueList = imageTaskQueueDao.findImageTaskQueueSetByClientApp(clientAppID);
        if(taskQueueList!=null)
            return (long) taskQueueList.size();
        return 0L;
    }

	@Override
	public List<ImageTaskQueue> getImageTaskQueueSetByTask(Long taskID, Long clientAppID) {
		return imageTaskQueueDao.findImageTaskQueue(taskID,clientAppID);
	}

	@Override
	public List<ImageTaskQueue> getImageTaskQueueByClientApp(Long clientAppID) {
		return imageTaskQueueDao.findImageTaskQueueSetByClientApp(clientAppID);
	}

	@Override
	public List<ImageTaskQueue> getImageTaskQueueByCrisis(Long crisisID) {
		return imageTaskQueueDao.findImageTaskQueueSetByCrisis(crisisID);
	}

	@Override
	public Long getCountImageTaskByCrisis(Long crisisID) {
		return imageTaskQueueDao.findImageTaskQueueCountByCrisis(crisisID);
	}

}
