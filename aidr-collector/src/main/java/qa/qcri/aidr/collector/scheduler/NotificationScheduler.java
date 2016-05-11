package qa.qcri.aidr.collector.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.collector.service.PushNotificationService;
import qa.qcri.aidr.collector.utils.GenericCache;

@Service
public class NotificationScheduler {

	@Autowired
	private PushNotificationService pushNotificationService;
	
	@Scheduled(cron = "${collection.count.update.cron}")
	void sendCollectionCountNotification() {
	
		pushNotificationService.publishMessage("collection", GenericCache.totalCollectionCount, "count_updated");
		
	}
}
