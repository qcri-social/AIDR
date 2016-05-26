package qa.qcri.aidr.collector.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.collector.api.FacebookCollectorAPI;
import qa.qcri.aidr.collector.beans.CollectionTask;


@Component
public class ScheduledTask {

	private final Logger logger = Logger.getLogger(getClass());

	public static final long HOUR = 3600*1000; // in milli-seconds.

	@Autowired
	FacebookCollectorAPI facebookCollectorAPI;
	
	@Scheduled(cron = "0 * * * * *") // 1 minutes - in milliseconds
	public void scheduledTaskUpdateFacebookCollections() {
		GenericCache cache = GenericCache.getInstance();
		List<CollectionTask> runningCollections = cache.getAllRunningCollectionTasks();
		try{
			if (runningCollections != null) {
				//logger.info("Update collections scheduled task started for " + collections.size() + " collections");
				for (CollectionTask collection : runningCollections) {
					if(StringUtils.isNotEmpty(collection.getProvider()) &&
							collection.getProvider().equalsIgnoreCase("facebook")){
						facebookCollectorAPI.startTask(collection);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error while updating facebook collection", e);
		}
	}
}
