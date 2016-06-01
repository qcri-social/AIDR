package qa.qcri.aidr.collector.collectors;

import javax.json.JsonObject;

import org.apache.log4j.Logger;

import qa.qcri.aidr.collector.beans.TwitterCollectionTask;
import qa.qcri.aidr.collector.java7.Predicate;

/**
 * Defines follow-filter i.e. collecting tweets from one or more Twitter users.
 * If a follow-filter is defined, this class also validates correct followers for each incoming tweet.
 */
public class FollowFilter implements Predicate<JsonObject> {
	private static Logger logger = Logger.getLogger(FollowFilter.class.getName());

	private String[] toFollow = null;
	
	public FollowFilter() {}

	public FollowFilter(TwitterCollectionTask task) {
		if (task != null) {
			this.setToFollow(task.getToFollow());
		} else {
			this.toFollow = null;
			logger.error("Collection can't be null!");
		}
	}

	public FollowFilter(String toFollow) {
		this.setToFollow(toFollow);
	}

	public void setToFollow(final String toFollow) {
		if (toFollow != null) {
			this.toFollow = toFollow.split(",");
			for (int i = 0; i < this.toFollow.length;i++) {
				this.toFollow[i] = this.toFollow[i].trim();
			}
		} else {
			this.toFollow = null;
		}
	}

	@Override
	public boolean test(JsonObject t) {
		try {
			if (null == toFollow) return true;

			JsonObject user = t.getJsonObject("user");
			if (null == user) {
				return false;
			}
			String tweetFrom = user.get("id").toString();
			if (null == tweetFrom) {
				return false;		// there are follow-filters but this tweet is not from the one's being followed. Hence reject tweet
			}
			// Otherwise check if any of the toFollow matches the tweet creator
			if (toFollow != null) {
				for (int i = 0; i < toFollow.length; i++) {
					if (tweetFrom.equals(toFollow[i])) {		// Note: case may be important if comparing Twitter User names!
						return true;
					}
				}
			}
			return false;	

		} catch (Exception e) {
			logger.error("Exception:", e);
			return false;
		}
	}

	@Override
	public String getFilterName() {
		return this.getClass().getSimpleName();
	}


}
