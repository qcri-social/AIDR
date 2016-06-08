package qa.qcri.aidr.manager.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.common.util.EmailClient;
import qa.qcri.aidr.common.util.NotificationEvent;
import qa.qcri.aidr.manager.persistence.entities.Collection;
import qa.qcri.aidr.manager.service.CollectionService;
import qa.qcri.aidr.manager.service.PushNotificationService;
import qa.qcri.aidr.manager.service.TaggerService;

@Service
public class ScheduledTask {

	private final Logger logger = Logger.getLogger(getClass());

	public static final long HOUR = 3600*1000; // in milli-seconds.

	@Value("${fetchMainUrl}")
	private String fetchMainUrl;
	
	@Autowired
	private CollectionService collectionService;
	
	@Autowired
	private TaggerService taggerService;
	
	@Autowired
	private PushNotificationService pushNotificationService;

	@Transactional
	@Scheduled(fixedDelay = 10 * 60 * 1000) // 10 minutes - in milliseconds
	private void scheduledTaskUpdateCollections() {
		List<Collection> collections;
		try {
			collections = collectionService.getRunningCollections();
		} catch (Exception e) {
			logger.error("Error while executing update collections scheduled task", e);
			taggerService.sendMailService("Error in ScheduledTask","Error while executing update collections scheduled task in ScheduledTask.scheduledTaskUpdateCollections");
			return;
		}
		if (collections != null) {
			//logger.info("Update collections scheduled task started for " + collections.size() + " collections");

			for (Collection item : collections) {
				try {
					collectionService.statusByCollection(item, 1L);
				} catch (Exception e) {
					logger.error("Error while updating collection with ID: " + item.getId(), e);
					taggerService.sendMailService("Error in ScheduledTask","Error while executing  updating collection with ID: " + item.getId() +" in ScheduledTask.scheduledTaskUpdateCollections");
				}
			}
		}
		//logger.info("Update collections scheduled task completed.");
	}

	@Scheduled(cron="0 0 * * * *") // each hour
	private void scheduledTaskStopCollections() {
		List<Collection> collections;
		try {
			collections = collectionService.getRunningCollections();
		} catch (Exception e) {
			logger.error("Error while executing checking for collections running duration",e);
			taggerService.sendMailService("Error in ScheduledTask","Error while executing checking for collections running duration in ScheduledTask.scheduledTaskStopCollections");
			return;
		}
		if (collections != null) {
			//logger.info("Checking for collections running duration started for " + collections.size() + " running collections");

			for (Collection item : collections) {
				Date stopAtTime = new Date(item.getStartDate().getTime() + item.getDurationHours() * HOUR);
				Date current = new Date();
				if (current.compareTo(stopAtTime) > 0){
					try {
						collectionService.stop(item.getId(), 1L);
						logger.info("Collection with ID: " + item.getId() + " was automatically stopped as it reached duration interval.");
					} catch (Exception e) {
						logger.error("Error while stopping collection with ID: " + item.getId(),e);
						taggerService.sendMailService("Error in ScheduledTask","Error while stopping collection with ID: " + item.getId() +" in ScheduledTask.scheduledTaskStopCollections");
					}
				}
			}
		}
		//logger.info("Checking for collections running duration completed.");
	}
	
	@Scheduled(cron = "${collection.update.notification.cron}")
	void sendCollectionCountNotification() {
		pushNotificationService.publishMessage("collection", NotificationEvent.COLLECTION_UPDATED);
	}
	
	@Scheduled(cron = "${facebook.collection.fetch.data.cron}")
	public void scheduledTaskUpdateFacebookCollections() {
		
		List<String> collectionsToRun = collectionService.fetchEligibleFacebookCollectionsToReRun();
		if(collectionsToRun != null && collectionsToRun.size() > 0) {
			for(String code : collectionsToRun) {
				collectionService.rerunFacebookCollection(code);
			}
		}
	}
	
	@Scheduled(cron = "${start.unexpextedly.stopped.collections.cron}")
	public void startUnexpectedlyStoppedCollections() throws ParseException {
		
		int runningCollections = collectionService.getRunningCollectionsCountFromCollector();
		if(runningCollections == 0) {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date today = formatter.parse(formatter.format(new Date()));
			List<Collection> unexpectedlyStoppedCollections = collectionService.getUnexpectedlyStoppedCollections(today);
			StringBuffer sb = new StringBuffer("Following collections are restarted.\n\n");
			int count = 0;
			for (Collection collection : unexpectedlyStoppedCollections) {
				try {
					collectionService.start(collection.getId());
					count++;
					sb.append(count + ". "+ collection.getName() + " (" + collection.getCode() + ")\n");
				} catch (Exception e) {
					logger.error("Error in startUnexpectedlyStoppedCollections for collection: " + collection.getId());
					e.printStackTrace();
				}
			}
			if(count > 0) {
				EmailClient.sendErrorMail(" " + count + " Collection Restarted", sb.toString());
			}
		}
	}

}
