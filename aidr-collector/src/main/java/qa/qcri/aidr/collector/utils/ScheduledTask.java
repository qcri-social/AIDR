package qa.qcri.aidr.collector.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.collector.service.CollectionService;

@Component
public class ScheduledTask {

	public static final long HOUR = 3600*1000; // in milli-seconds.

	@Autowired
	CollectionService collectionService;
	
	//@Scheduled(cron = "0 * * * * *") // 1 minutes - in milliseconds
	public void scheduledTaskUpdateFacebookCollections() {
		collectionService.processFacebookDataCollection();
	}
}
