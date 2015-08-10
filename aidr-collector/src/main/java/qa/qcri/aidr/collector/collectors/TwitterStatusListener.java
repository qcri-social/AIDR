package qa.qcri.aidr.collector.collectors;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.java7.Predicate;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.CollectorConfigurator;
import qa.qcri.aidr.collector.utils.CollectorErrorLog;
import qa.qcri.aidr.collector.utils.GenericCache;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterException;

/**
 * This class is responsible for dispatching incoming tweets.
 * First it invokes all the filters which must be specified at
 * creation time. In case all the filters return TRUE for
 * given tweet, this tweet is passed to all the publishers.
 * 
 * In the very basic case there are two publishers. The first 
 * one is responsible for saving tweets and the second one
 * reports about the progress back to UI.
 * 
 * This approach allows to create unit tests for every single
 * filter and for every single publisher.
 * 
 */
class TwitterStatusListener implements StatusListener, ConnectionLifeCycleListener{

	private static Logger logger = Logger.getLogger(TwitterStatusListener.class.getName());

	private static CollectorConfigurator configProperties = CollectorConfigurator.getInstance();
	
	private CollectionTask task;

	private List<Predicate<JsonObject>> filters = new ArrayList<>();
	private List<Publisher> publishers = new ArrayList<>();
	private JsonObject aidr;
	private String channelName;
	private long timeToSleep = 0;
	private static int max  = 3;
	private static int min = 1;
	private GenericCache cache;
	
	public TwitterStatusListener(CollectionTask task, String channelName) {
		this.task = task;
		this.channelName = channelName;
		this.aidr = Json.createObjectBuilder()
			.add("doctype", "twitter")
			.add("crisis_code", task.getCollectionCode())
			.add("crisis_name", task.getCollectionName())
			.build();
		
		cache = GenericCache.getInstance();
	}

	/**
	 * Adds a filter which is able to ignore some tweets.
	 * 
	 * @param filter
	 *            A function that returns true when document is approved and
	 *            false when document must be ignored.
	 */
	public void addFilter(Predicate<JsonObject> filter) {
		filters.add(filter);
	}

	public void addPublisher(Publisher publisher) {
		publishers.add(publisher);
	}

	@Override
	public void onStatus(Status status) {
		String json = TwitterObjectFactory.getRawJSON(status);
		JsonObject originalDoc = Json.createReader(new StringReader(json)).readObject();
		for (Predicate<JsonObject> filter : filters) {
			if (!filter.test(originalDoc)) {
				//logger.info(originalDoc.get("text").toString() + ": failed on filter = " + filter.getFilterName());
				return;
			}
		}
		JsonObjectBuilder builder = Json.createObjectBuilder();
		for (Entry<String, JsonValue> entry: originalDoc.entrySet())
			builder.add(entry.getKey(), entry.getValue());
		builder.add("aidr", aidr);
		JsonObject doc = builder.build();
		for (Publisher p : publishers)
			p.publish(channelName, doc);
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		logger.info(task.getCollectionName() + ": Track limitation notice: " + numberOfLimitedStatuses);
		// TODO: thread safety
		task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_RUNNING_WARNING));
		task.setStatusMessage("Track limitation notice: " + numberOfLimitedStatuses);
	}

	@Override
	public void onException(Exception ex) {
		logger.error("Exception for collection " + task.getCollectionCode(), ex);
		if(ex instanceof TwitterException)
		{
			int attempt = cache.incrAttempt(task.getCollectionCode());
			task.setStatusMessage(ex.getMessage());
			if(((TwitterException) ex).getStatusCode() == -1)
			{
				if(attempt > Integer.parseInt(configProperties.getProperty(CollectorConfigurationProperty.RECONNECT_NET_FAILURE_RETRY_ATTEMPTS)))
					task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
				else
				{
					timeToSleep = (long) (getRandom()*attempt*
							Integer.parseInt(configProperties.getProperty(CollectorConfigurationProperty.RECONNECT_NET_FAILURE_WAIT_SECONDS)));
					logger.warn("Error -1, Waiting for " + timeToSleep + " seconds, attempt: " + attempt);
					task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_WARNING));
					task.setStatusMessage("Collection Stopped due to Twitter Error. Reconnect Attempt: " + attempt);
				}
			}
			else if(((TwitterException) ex).getStatusCode() == 420)
			{
				if(attempt > Integer.parseInt(configProperties.getProperty(CollectorConfigurationProperty.RECONNECT_RATE_LIMIT_RETRY_ATTEMPTS)))
					task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
				else
				{
					timeToSleep = (long) (getRandom()*(2^(attempt-1))*
							Integer.parseInt(configProperties.getProperty(CollectorConfigurationProperty.RECONNECT_RATE_LIMIT_WAIT_SECONDS)));
					logger.warn("Error 420, Waiting for " + timeToSleep + " seconds, attempt: " + attempt);					
					task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_WARNING));
					task.setStatusMessage("Collection Stopped due to Twitter Error. Reconnect Attempt: " + attempt);
				}
			}
			else if(((TwitterException) ex).getStatusCode() == 503)
			{
				if(attempt > Integer.parseInt(configProperties.getProperty(CollectorConfigurationProperty.RECONNECT_SERVICE_UNAVAILABLE_RETRY_ATTEMPTS)))
					task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
				else
				{
					timeToSleep = (long) (getRandom()*attempt*
							Integer.parseInt(configProperties.getProperty(CollectorConfigurationProperty.RECONNECT_SERVICE_UNAVAILABLE_WAIT_SECONDS)));
					logger.warn("Error 503, Waiting for " + timeToSleep + " seconds, attempt: " + attempt);					
					task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_WARNING));
					task.setStatusMessage("Collection Stopped due to Twitter Error. Reconnect Attempt: " + attempt);
				}
			}
			else
				task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
			
			if(task.getStatusCode().equals(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR)))
				CollectorErrorLog.sendErrorMail(task.getCollectionCode(),ex.toString());
			else
			{
				try {
					Thread.sleep(timeToSleep*1000);
				} catch (InterruptedException ignore) {
				}
				timeToSleep=0;
			}
		}
		else
		{
			task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
			CollectorErrorLog.sendErrorMail(task.getCollectionCode(),ex.toString());			
		}
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
	}

	@Override
	public void onStallWarning(StallWarning msg) {
		logger.warn(task.getCollectionCode() + " Stall Warning: " + msg.getMessage());
	}
	
	private static double getRandom()
	{
		return (Math.random() * (max - min) + min);
	}

	@Override
	public void onConnect() {
		if(task.getStatusCode() == configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_WARNING))
		{
			task.setStatusMessage("was disconnected due to network failure, reconnected OK");
			cache.resetAttempt(task.getCollectionCode());
		}
		else
			task.setStatusMessage(null);
		task.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_RUNNING));
	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onCleanUp() {
		// TODO Auto-generated method stub
		
	}
}