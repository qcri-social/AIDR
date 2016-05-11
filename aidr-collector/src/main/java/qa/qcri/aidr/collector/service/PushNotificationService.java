package qa.qcri.aidr.collector.service;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.pusher.rest.Pusher;

@Service
public class PushNotificationService {

	private static Logger logger = Logger.getLogger(PushNotificationService.class);
	
	public void publishMessage(String channel, int message, String event) {

		Pusher pusher = new Pusher("143040", "1eb98c94c2976297709d", "cd8e0fcfc1cc785ccd4a");
		
		try {
			JSONObject jsonData = new JSONObject();
			jsonData.put("data", message);
			pusher.trigger(channel, event, jsonData);

		} catch (Exception e) {
			logger.warn("Error while publishing message to channel", e);
		}
	}
}
