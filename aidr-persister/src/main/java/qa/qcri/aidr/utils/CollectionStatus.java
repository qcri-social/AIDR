package qa.qcri.aidr.utils;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;

import qa.qcri.aidr.logging.ErrorLog;

public class CollectionStatus {
	   
	private static Logger logger = Logger.getLogger(CollectionStatus.class);
	private ErrorLog elog = new ErrorLog();
	
	@SuppressWarnings({ "unused", "unchecked" })
	public Integer getTotalDownloadedCount(final String collectionCode) {
			Response clientResponse = null;
			Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
			try {
				WebTarget webResource = client.target(Config.managerUrl 
						+ "/public/collection/findTotalCount?channelCode=" + collectionCode);

				clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
				Map<String, Integer> collectionMap = new HashMap<String, Integer>();

				if (clientResponse.getStatus() == 200) {
					//convert JSON string to Map
					collectionMap = clientResponse.readEntity(Map.class);
					logger.info("Channel info received from manager: " + collectionMap);
					if (collectionMap != null) {
						return collectionMap.get(collectionCode);
					}
				} else {
					logger.warn("Couldn't contact AIDRFetchManager for publiclyListed status, channel: " + collectionCode);
				}
			} catch (Exception e) {
				logger.error("Error in querying manager for running collections: " + clientResponse);
				logger.error(elog.toStringException(e));
			}
			return null;
	    }
	
	public Map<String, Object> getUIWrapper(String collectionCode, String message, String fileName, Boolean success) {
		Map<String, Object> modelMap = new HashMap<String, Object>(4);
		modelMap.put("code", collectionCode);
		modelMap.put("message", message);
		modelMap.put("url", fileName);
		modelMap.put("success", success);
		
		return modelMap;
	}
}
