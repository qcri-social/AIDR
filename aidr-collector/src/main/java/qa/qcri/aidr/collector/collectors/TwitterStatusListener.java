package qa.qcri.aidr.collector.collectors;

import static qa.qcri.aidr.collector.utils.ConfigProperties.getProperty;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.log4j.Logger;

import qa.qcri.aidr.collector.beans.CollectionTask;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;

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
 * @author Anthony Ananich <anton.ananich@inpun.com>
 */
class TwitterStatusListener implements StatusListener {

	private static Logger logger = Logger.getLogger(TwitterStatusListener.class.getName());

	private CollectionTask task;

	private List<Predicate<JsonObject>> filters = new ArrayList<>();
	private List<Publisher> publishers = new ArrayList<>();
	private JsonObject aidr;
	private String channelName;

	public TwitterStatusListener(CollectionTask task, String channelName) {
		this.task = task;
		this.channelName = channelName;
		this.aidr = Json.createObjectBuilder()
			.add("doctype", "twitter")
			.add("crisis_code", task.getCollectionCode())
			.add("crisis_name", task.getCollectionName())
			.build();
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
		JsonObject doc = Json.createReader(new StringReader(json)).readObject();
		for (Predicate<JsonObject> filter : filters) {
			if (!filter.test(doc))
				return;
		}
		doc.put("aidr", aidr);
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
		task.setStatusCode(getProperty("STATUS_CODE_COLLECTION_RUNNING_WARNING"));
		task.setStatusMessage("Track limitation notice: " + numberOfLimitedStatuses);
	}

	@Override
	public void onException(Exception ex) {
		logger.error("Twitter Exception for collection " + task.getCollectionCode(), ex);
		// TODO: thread safety
		task.setStatusCode(getProperty("STATUS_CODE_COLLECTION_ERROR"));
		task.setStatusMessage(ex.getMessage());
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
	}

	@Override
	public void onStallWarning(StallWarning msg) {
		logger.error(task.getCollectionCode() + " Stall Warning: " + msg.getMessage());
	}
}