package qa.qcri.aidr.manager.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.service.CollectionService;

import java.util.Date;
import java.util.List;

@Service
public class ScheduledTask {

	private Logger logger = Logger.getLogger(getClass());

	public static final long HOUR = 3600*1000; // in milli-seconds.

	@Autowired
	private CollectionService collectionService;

	@Scheduled(fixedDelay = 10 * 60 * 1000) // 10 minutes - in milliseconds
	private void scheduledTaskUpdateCollections() {
		List<AidrCollection> collections;
		try {
			collections = collectionService.getRunningCollections();
		} catch (Exception e) {
			logger.error("Error while executing update collections scheduled task");
			logger.error("exception: ", e);
			return;
		}
		if (collections != null) {
			logger.info("Update collections scheduled task started for " + collections.size() + " collections");

			for (AidrCollection item : collections) {
				try {
					collectionService.statusByCollection(item);
				} catch (Exception e) {
					logger.error("Error while updating collection with ID: " + item.getId());
					logger.error("exception: ", e);
				}
			}
		}
		logger.info("Update collections scheduled task completed.");
	}

	@Scheduled(cron="0 0 * * * *") // each hour
	private void scheduledTaskStopCollections() {
		List<AidrCollection> collections;
		try {
			collections = collectionService.getRunningCollections();
		} catch (Exception e) {
			logger.error("Error while executing checking for collections running duration");
			logger.error("exception: ", e);
			return;
		}
		if (collections != null) {
			logger.info("Checking for collections running duration started for " + collections.size() + " running collections");

			for (AidrCollection item : collections) {
				Date stopAtTime = new Date(item.getStartDate().getTime() + item.getDurationHours() * HOUR);
				Date current = new Date();
				if (current.compareTo(stopAtTime) > 0){
					try {
						collectionService.stop(item.getId());
						logger.info("Collection with ID: " + item.getId() + " was automatically stopped as it reached duration interval.");
					} catch (Exception e) {
						logger.info("Error while stopping collection with ID: " + item.getId());
						logger.error("exception: ", e);
					}
				}
			}
		}
		logger.info("Checking for collections running duration completed.");
	}

}
