package qa.qcri.aidr.manager.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import qa.qcri.aidr.manager.hibernateEntities.AidrCollection;
import qa.qcri.aidr.manager.service.CollectionService;

import java.util.List;

@Service
public class ScheduledTask {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private CollectionService collectionService;

    @Scheduled(fixedDelay = 10 * 60 * 1000) // 10 minutes - in milliseconds
    private void scheduledTask() {
        List<AidrCollection> collections;
        try {
            collections = collectionService.getRunningCollections();
        } catch (Exception e) {
            logger.error("Error while executing update collections scheduled task");
            e.printStackTrace();
            return;
        }

        logger.info("Update collections scheduled task started for " + collections.size() + " collections");

        for (AidrCollection item : collections) {
            try {
                collectionService.statusByCollection(item);
            } catch (Exception e) {
                logger.error("Error while updating collection with ID: " + item.getId());
                e.printStackTrace();
            }
        }

        logger.info("Update collections scheduled task completed.");
    }

}
