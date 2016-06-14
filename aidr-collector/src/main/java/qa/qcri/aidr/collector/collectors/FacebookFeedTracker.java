package qa.qcri.aidr.collector.collectors;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.FacebookCollectionTask;
import qa.qcri.aidr.collector.beans.FacebookEntityType;
import qa.qcri.aidr.collector.utils.CollectorConfigurationProperty;
import qa.qcri.aidr.collector.utils.CollectorConfigurator;
import qa.qcri.aidr.collector.utils.GenericCache;
import qa.qcri.aidr.common.redis.LoadShedder;

import com.google.gson.Gson;

import facebook4j.Event;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Group;
import facebook4j.Ordering;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;
import facebook4j.internal.logging.Logger;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

public class FacebookFeedTracker implements Closeable {

	private static Logger logger = Logger.getLogger(FacebookFeedTracker.class);
	private static CollectorConfigurator configProperties = CollectorConfigurator.getInstance();
	private JedisPublisher publisher;
	private final Facebook facebook;
	private final FacebookCollectionTask task;
	private final LoadShedder fbApiHitShedder;

	private static final int DEFAULT_LIMIT = 100;
	private static final Long HOUR_IN_MILLISECS = 60 * 60 * 1000L;
	private static final Long SEVEN_DAYS_IN_MILLISECS = 7 * 24 * HOUR_IN_MILLISECS;

	private static String FIELDS_TO_FETCH = "id,updated_time,message_tags,scheduled_publish_time,"
			+ "created_time, full_picture,object_id,with_tags, is_published, "
			+ "from,to,message,picture,link,name,caption,description,source,properties,"
			+ "icon,actions,privacy,type,shares,status_type,place,story,"
			+ "application,targeting,likes.summary(true),comments.summary(true)";

	public FacebookFeedTracker(FacebookCollectionTask task) {

		logger.info("Waiting to aquire Jedis connection for collection " + task.getCollectionCode());
		Configuration config = task2configuration(task);
		this.publisher = JedisPublisher.newInstance();
		logger.info("Jedis connection acquired for collection " + task.getCollectionCode());
		fbApiHitShedder = new LoadShedder(Integer.parseInt(configProperties.getProperty(CollectorConfigurationProperty.FACEBOOK_MAX_API_HITS_HOURLY_PER_USER)),
				Integer.parseInt(configProperties.getProperty(CollectorConfigurationProperty.FACEBOOK_LOAD_CHECK_INTERVAL_MINUTES)),
				true, "FACEBOOK_API_CALL_SHEDDER." + task.getCollectionCode());
		this.facebook = new FacebookFactory(config).getInstance();
		this.task = task;
	}

	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Boolean syncObj = GenericCache.getInstance().getFbSyncObjMap(task.getCollectionCode()) == null ? Boolean.TRUE : GenericCache.getInstance().getFbSyncObjMap(task.getCollectionCode());
				synchronized (syncObj) {
					GenericCache.getInstance().setFbSyncObjMap(task.getCollectionCode(), syncObj);
					GenericCache.getInstance().setFbSyncStateMap(task.getCollectionCode(), 0);
					collectFacebookData();
				}

			}
		}).start();
	}

	@Override
	public void close() throws IOException {
		facebook.shutdown();
		publisher.close();
		logger.info("AIDR-Fetcher: Collection stopped which was tracking ");
	}

	private static Configuration task2configuration(CollectionTask task) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setDebugEnabled(false)
				.setOAuthAppId(configProperties.getProperty(CollectorConfigurationProperty.FACEBOOK_CONSUMER_KEY))
				.setOAuthAppSecret(
						configProperties.getProperty(CollectorConfigurationProperty.FACEBOOK_CONSUMER_SECRET))
				.setJSONStoreEnabled(true).setOAuthAccessToken(task.getAccessToken());

		Configuration configuration = builder.build();
		return configuration;
	}

	public void collectFacebookData() {

		this.publisher = JedisPublisher.newInstance();
		logger.info("Jedis connection acquired for collection " + task.getCollectionCode());

		Date toTimestamp = new Date();
		long fetchFromInMiliSecs = task.getFetchFrom() * HOUR_IN_MILLISECS;
		Date fromTimestamp = new Date(System.currentTimeMillis() - fetchFromInMiliSecs);
		try {
			task.setPullInProgress(true);
			if(task.getLastExecutionTime() != null && 
					(System.currentTimeMillis() - task.getLastExecutionTime().getTime()) <= fetchFromInMiliSecs) {
				fromTimestamp = task.getLastExecutionTime();
			}
			
			for (FacebookEntityType type : FacebookEntityType.values()) {
				if (GenericCache.getInstance().getFbSyncStateMap(task.getCollectionCode()) == 0) {
					this.fetchPosts(fromTimestamp, toTimestamp, type);
				} else {
					GenericCache.getInstance().getFbSyncObjMap(task.getCollectionCode()).notifyAll();
					break;
				}
			}
			
			task.setPullInProgress(false);
			task.setLastExecutionTime(toTimestamp);
			GenericCache.getInstance().setFbConfigMap(task.getCollectionCode(), task);
		} catch (FacebookException e) {
			GenericCache.getInstance().setFailedCollection(task.getCollectionCode(), task);
		}
	}

	private void fetchPosts(Date fromTimestamp, Date toTimestamp, FacebookEntityType type) throws FacebookException {
		List<String> entityIds = new ArrayList<String>();

		try {
			switch (type) {
			case PAGE:
				entityIds = this.fetchPageIds();
				break;

			case EVENT:
				entityIds = this.fetchEventIds();
				break;

			case GROUP:
				entityIds = this.fetchGroupIds();
				break;
			}

		} catch (FacebookException e) {
			logger.error("Error in processing request for : " + type, e);
			handleFacebookException(e, task.getCollectionCode());
		}

		if (entityIds != null && !entityIds.isEmpty()) {
			processPost(toTimestamp, fromTimestamp, entityIds, type);
		}
	}

	private List<String> fetchPageIds() throws FacebookException {
		List<String> entityIds = new ArrayList<String>();
		int offset = 0;
		while (offset >= 0) {
			ResponseList<Page> pageList = null;
			if (GenericCache.getInstance().getFbSyncStateMap(task.getCollectionCode()) == 0) {
				pageList = facebook.searchPages(task.getToTrack(),
						new Reading().fields("id").order(Ordering.CHRONOLOGICAL).limit(DEFAULT_LIMIT).offset(offset));
			} else {
				GenericCache.getInstance().getFbSyncObjMap(task.getCollectionCode()).notifyAll();
				break;
			}

			if (pageList != null) {
				for (Page page : pageList) {
					String id = page.getId();
					entityIds.add(id);
				}
			}
			offset = pageList != null && pageList.size() == DEFAULT_LIMIT ? offset + DEFAULT_LIMIT : -1;
		}
		return entityIds;
	}

	private List<String> fetchEventIds() throws FacebookException {
		List<String> entityIds = new ArrayList<String>();
		int offset = 0;
		while (offset >= 0) {
			ResponseList<Event> eventList = null;
			if (GenericCache.getInstance().getFbSyncStateMap(task.getCollectionCode()) == 0) {
				eventList = facebook.searchEvents(task.getToTrack(),
						new Reading().fields("id").order(Ordering.CHRONOLOGICAL).limit(DEFAULT_LIMIT).offset(offset));
			} else {
				GenericCache.getInstance().getFbSyncObjMap(task.getCollectionCode()).notifyAll();
				break;
			}
			if (eventList != null) {
				for (Event event : eventList) {
					String id = event.getId();
					entityIds.add(id);
				}
			}
			offset = eventList != null && eventList.size() == DEFAULT_LIMIT ? offset + DEFAULT_LIMIT : -1;
		}
		return entityIds;
	}

	private List<String> fetchGroupIds() throws FacebookException {
		List<String> entityIds = new ArrayList<String>();
		int offset = 0;
		while (offset >= 0) {
			ResponseList<Group> groupList = null;
			if (GenericCache.getInstance().getFbSyncStateMap(task.getCollectionCode()) == 0) {
				groupList = facebook.searchGroups(task.getToTrack(),
						new Reading().fields("id").order(Ordering.CHRONOLOGICAL).limit(DEFAULT_LIMIT).offset(offset));
			} else {
				GenericCache.getInstance().getFbSyncObjMap(task.getCollectionCode()).notifyAll();
				break;
			}
			if (groupList != null) {
				for (Group group : groupList) {
					String id = group.getId();
					entityIds.add(id);
				}
			}

			offset = groupList != null && groupList.size() == DEFAULT_LIMIT ? offset + DEFAULT_LIMIT : -1;
		}

		return entityIds;
	}

	private void processPost(Date toTimestamp, Date since, List<String> entityIds, FacebookEntityType parent)
			throws FacebookException {

		String channelName = configProperties.getProperty(CollectorConfigurationProperty.COLLECTOR_CHANNEL) + "."
				+ task.getCollectionCode();
		Gson gson = new Gson();
		
		for (String parentId : entityIds) {
			int postsOffset = 0;
			if (GenericCache.getInstance().getFbSyncStateMap(task.getCollectionCode()) == 0) {
				while (postsOffset >= 0 ) {

					while(!fbApiHitShedder.canProcess() && GenericCache.getInstance().getFbSyncStateMap(task.getCollectionCode()) == 0)
					{
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							logger.warn("Interrupted exception while sleeping in load shedder for collection code: "
									+ task.getCollectionCode());
						}
					}
					if (GenericCache.getInstance().getFbSyncStateMap(task.getCollectionCode()) == 0) {
						try {
							ResponseList<Post> feed = facebook.getFeed(parentId, new Reading().fields(FIELDS_TO_FETCH)
									.since(since).until(toTimestamp).order(Ordering.CHRONOLOGICAL).limit(DEFAULT_LIMIT)
									.offset(postsOffset));
							postsOffset = feed.size() == DEFAULT_LIMIT ? postsOffset + DEFAULT_LIMIT : -1;
							for (Post post : feed) {
								try {
									JSONObject aidrJson = new JSONObject();
									aidrJson.put("doctype", "facebook");
									aidrJson.put("crisis_code", task.getCollectionCode());
									aidrJson.put("crisis_name", task.getCollectionName());
									aidrJson.put("parent_type", parent.name().toLowerCase());

									JSONObject docJson = new JSONObject(gson.toJson(post));
									docJson.put("aidr", aidrJson);

									int likeCount = post.getLikes().getSummary() != null ? post.getLikes().getSummary()
											.getTotalCount() : 0;
									docJson.put("likesCount", likeCount);
									int commentCount = post.getComments().getSummary() != null ? post.getComments()
											.getSummary().getTotalCount() : 0;
									docJson.put("commentsCount", commentCount);

									publisher.publish(channelName, docJson.toString());
								} catch (JSONException e) {
									logger.warn("Post error for parent id : " + parentId + " and type : " + parent);
								}
							}

							GenericCache.getInstance().incrCounter(task.getCollectionCode(), (long) feed.size());
							if (feed != null && feed.size() > 0) {
								String lastDownloadedDoc = feed.get(feed.size() - 1).getMessage();
								if (lastDownloadedDoc != null && !lastDownloadedDoc.isEmpty()
										&& lastDownloadedDoc.length() > 500) {
									lastDownloadedDoc = lastDownloadedDoc.substring(0, 250) + "...";
								}

								GenericCache.getInstance().setLastDownloadedDoc(task.getCollectionCode(),
										lastDownloadedDoc);
							}
						} catch (FacebookException e) {
							logger.warn("Exception while fetching feeds for id: " + parentId);
							handleFacebookException(e, task.getCollectionCode());
						}
					} else {
						GenericCache.getInstance().getFbSyncObjMap(task.getCollectionCode()).notifyAll();
						break;
					}
				}
			} else {
				GenericCache.getInstance().getFbSyncObjMap(task.getCollectionCode()).notifyAll();
				break;
			}
		}

	}

	private void handleFacebookException(FacebookException e, String collectionCode) throws FacebookException {
		boolean found = false;
		switch (e.getErrorCode()) {
		case 1:
		case 2:
		case 4:
		case 17:
		case 341:
			logger.error("Facebook api is rate limited for collectionCode: " + collectionCode, e);
			found = true;
			break;

		case 102:
			logger.error("Oauth Exception. May be access token got expired for collectionCode: " + collectionCode, e);
			throw new FacebookException(e);
		}

		if (!found) {
			switch (e.getErrorSubcode()) {
			case 458:
			case 459:
			case 460:
			case 463:
			case 464:
			case 467:
				logger.error("Oauth Exception. May be access token got expired for collectionCode: " + collectionCode,
						e);
				throw new FacebookException(e);
			}
		}
	}
}
