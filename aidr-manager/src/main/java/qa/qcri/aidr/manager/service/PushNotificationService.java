package qa.qcri.aidr.manager.service;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.common.util.NotificationEvent;
import qa.qcri.aidr.manager.dto.CollectionStatsInfo;
import qa.qcri.aidr.manager.util.ManagerConfigurationProperty;
import qa.qcri.aidr.manager.util.ManagerConfigurator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pusher.rest.Pusher;

@Service
public class PushNotificationService {

	private static Logger logger = Logger.getLogger(PushNotificationService.class);
	
	@Autowired
	private CollectionService collectionService;
	
	private static String pusherAppID;
	
	private static String pusherAppKey;
	
	private static String pusherAppSecret;
	
	public PushNotificationService() {
		super();
		pusherAppID = ManagerConfigurator.getInstance().getProperty
				(ManagerConfigurationProperty.PUSHER_APP_ID);
		pusherAppKey = ManagerConfigurator.getInstance().getProperty
				(ManagerConfigurationProperty.PUSHER_APP_KEY);
		pusherAppSecret = ManagerConfigurator.getInstance().getProperty
				(ManagerConfigurationProperty.PUSHER_APP_SECRET);
	}

	public void publishMessage(String channel, NotificationEvent event) {

		Pusher pusher = new Pusher(pusherAppID, pusherAppKey, pusherAppSecret);
		
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
