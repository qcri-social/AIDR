package qa.qcri.aidr.manager.service;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.common.util.NotificationEvent;

import com.pusher.rest.Pusher;

@Service
public class PushNotificationService {

	private static Logger logger = Logger.getLogger(PushNotificationService.class);
	
	@Autowired
	private CollectionService collectionService;
	
	public void publishMessage(String channel, NotificationEvent event) {

		Pusher pusher = new Pusher("143040", "1eb98c94c2976297709d", "cd8e0fcfc1cc785ccd4a");
		
		try {
			pusher.trigger(channel, event.name(), generateResponse(event));

		} catch (Exception e) {
			logger.warn("Error while publishing message to channel", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject generateResponse(NotificationEvent event) throws Exception {
		
		JSONObject jsonData = new JSONObject();
		if(NotificationEvent.COLLECTION_UPDATED.equals(event)) {
			Long runningCollectionsCount = collectionService.getRunningCollectionsCount(null);
			Long totalCollectionCount = collectionService.getTotalCollectionsCount();
			
			jsonData.put("total_collection", totalCollectionCount);
			jsonData.put("total_running", runningCollectionsCount);
			jsonData.put("new_tweets", 2000);
			jsonData.put("total_offline", totalCollectionCount - runningCollectionsCount);
		}
		JSONObject responseMessage = new JSONObject();
		responseMessage.put("data", jsonData);
		return responseMessage;
	}
}
