package qa.qcri.aidr.manager.service;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.common.util.NotificationEvent;
import qa.qcri.aidr.manager.dto.CollectionStatsInfo;

import com.fasterxml.jackson.databind.ObjectMapper;
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
		
		ObjectMapper mapper = new ObjectMapper();
		CollectionStatsInfo collectionStatsInfo = null;
		if(NotificationEvent.COLLECTION_UPDATED.equals(event)) {
			collectionStatsInfo = collectionService.getCollectionStatistics();
		}
		
		JSONObject responseMessage = new JSONObject();
		responseMessage.put("data", collectionStatsInfo);
		return responseMessage;
	}
}
