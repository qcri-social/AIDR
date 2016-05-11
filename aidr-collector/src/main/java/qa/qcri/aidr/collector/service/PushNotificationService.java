package qa.qcri.aidr.collector.service;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.pusher.rest.Pusher;

@Service
public class PushNotificationService {

	private static Logger logger = Logger.getLogger(PushNotificationService.class);
	
	public void publishMessage() {

		Pusher pusher = new Pusher("143040", "1eb98c94c2976297709d", "cd8e0fcfc1cc785ccd4a");
		
		try {
			
			JSONObject json = new JSONObject();
			json.put("collectionCount", 10);
			JSONObject jsonData = new JSONObject();
			json.put("data", json);
			pusher.trigger("collection", "count_updated", jsonData);

		} catch (Exception e) {
			logger.warn("Error while publishing message to channel", e);
		}
	}
}
