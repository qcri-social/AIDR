/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.collectors;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import qa.qcri.aidr.collector.beans.AIDR;
import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.beans.FetcherResponseToStringChannel;
import qa.qcri.aidr.collector.utils.Config;
import qa.qcri.aidr.collector.logging.Loggable;
import static qa.qcri.aidr.collector.logging.Loggable.LOG_LEVEL;
import qa.qcri.aidr.collector.redis.JedisConnectionPool;
import qa.qcri.aidr.collector.utils.GenericCache;
import qa.qcri.aidr.collector.utils.TwitterStreamQueryBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Imran
 */
public class TwitterStreamTracker extends Loggable implements Serializable {

    private TwitterStream twitterStream;
    private ConfigurationBuilder configBuilder;
    private TwitterStreamQueryBuilder streamQuery;
    private Jedis publisherJedis;
    private String collectionCode; // CrisisID
    private String cacheKey;
    private long counter = 1;
    private String collectionName;

    public TwitterStreamTracker() {
    }

    public TwitterStreamTracker(TwitterStreamQueryBuilder streamFilterQuery, ConfigurationBuilder configurationBuilder, CollectionTask collectionTask) throws Exception {

        this.publisherJedis = JedisConnectionPool.getJedisConnection();
        this.streamQuery = new TwitterStreamQueryBuilder();
        this.collectionCode = collectionTask.getCollectionCode();
        this.collectionName = collectionTask.getCollectionName();
        this.cacheKey = collectionTask.getCollectionCode();
        this.streamQuery = streamFilterQuery;
        this.configBuilder = configurationBuilder;

        GenericCache.getInstance().setTwtConfigMap(cacheKey, collectionTask);
        GenericCache.getInstance().setTwitterTracker(cacheKey, this);
        GenericCache.getInstance().incrCounter(cacheKey, new Long(0));
        collectThroughStreaming();
    }

    private void collectThroughStreaming() {

        StatusListener listener = new StatusListener() {
            JSONObject tweetJSONObject = null;
            CollectionTask collection = GenericCache.getInstance().getTwtConfigMap(getCacheKey());
            JSONObject aidrObject = new JSONObject(new FetcherResponseToStringChannel(new AIDR(getCollectionCode(), getCollectionName(), "twitter")));
            String aidrJson = StringUtils.replace(aidrObject.toString(), "{", ",", 1); // replacing the first occurance of { with ,
            boolean allowAllLanguages = getStreamQuery().isLanguageAllowed(Config.LANGUAGE_ALLOWED_ALL);
            String channelName = Config.FETCHER_CHANNEL + "." + getCollectionCode();
            GenericCache cache = GenericCache.getInstance();

            @Override
            public void onStatus(Status status) {
                String lang = status.getLang();
                if (allowAllLanguages) {
                    publishMessage(status);
                } else {
                    if (getStreamQuery().isLanguageAllowed(lang)) {
                        publishMessage(status);
                    }
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                collection.setStatusCode(Config.STATUS_CODE_COLLECTION_RUNNING_WARNING);
                collection.setStatusMessage("Track limitation notice: " + numberOfLimitedStatuses);
                GenericCache.getInstance().setTwtConfigMap(getCacheKey(), collection);
                log(LogLevel.WARNING, "Track limitation notice: " + numberOfLimitedStatuses);

            }

            @Override
            public void onException(Exception ex) {
                System.out.println("Twitter Exception for collection " + collection.getCollectionCode() + " - " + ex.toString());
                log(LogLevel.WARNING, ex.toString());
                collection.setStatusCode(Config.STATUS_CODE_COLLECTION_ERROR);
            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
            }

            @Override
            public void onStallWarning(StallWarning arg0) {
                System.out.println("Stall Warning: " + arg0.getMessage());
                log(LogLevel.WARNING, arg0.toString());
            }

            public void publishMessage(Status status) {
                    StringBuilder rawTweetJSON = new StringBuilder(TwitterObjectFactory.getRawJSON(status));
                    int length = rawTweetJSON.length();
                    rawTweetJSON.replace(length - 1, length, aidrJson);
                    publishToRedis(rawTweetJSON.toString());
                    counter++;
                    if (counter >= Config.FETCHER_REDIS_COUNTER_UPDATE_THRESHOLD) {
                        cache.incrCounter(collectionCode, counter);
                        cache.setLastDownloadedDoc(collectionCode, status.getText());
                        counter = 0;
                    }
            }

            public void publishMessageDeprecated(Status status, String rawTweetJSON) {

                try {
                    StringBuilder tweet = new StringBuilder(rawTweetJSON);
                    JSONObject aidrObject = new JSONObject(new FetcherResponseToStringChannel(new AIDR(getCollectionCode(), getCollectionName(), "twitter")));
                    String aidrJson = StringUtils.replace(aidrObject.toString(), "{", ",", 1); // replacing first occurance of { with ,
                    tweet.replace(rawTweetJSON.lastIndexOf("}"), rawTweetJSON.lastIndexOf("}") + 1, aidrJson);
                    //publisherJedis.publish(Config.FETCHER_CHANNEL + "." + getCollectionCode(), tweet.toString());
                    publishToRedis(tweet.toString());
                    counter++;
                    if (counter >= Config.FETCHER_REDIS_COUNTER_UPDATE_THRESHOLD) {
                        GenericCache.getInstance().incrCounter(getCacheKey(), counter);
                        GenericCache.getInstance().setLastDownloadedDoc(getCacheKey(), status.getText());
                        counter = new Long(0);
                    }
                } catch (JedisConnectionException e) {
                    System.out.println("JedisConnectionException: " + collectionName);
                } catch (Exception exp) {
                    System.out.println("Exception occured in " + collectionName);
                    exp.printStackTrace();
                }
            }

            public void publishToRedis(String tweet) {
                publisherJedis.publish(channelName, tweet);
            }
        };

        twitterStream = new TwitterStreamFactory(getConfigBuilder().build()).getInstance();
        twitterStream.addListener(listener);

        // Setup the filter
        FilterQuery query = new FilterQuery();
        if (getStreamQuery().getToFollow() != null) {
            query.follow(getStreamQuery().getToFollow());
        }
        if (getStreamQuery().getGeoLocation() != null) {
            query.locations(getStreamQuery().getGeoLocation());

        }

        query.track(getStreamQuery().getToTrack());
        twitterStream.filter(query);

        // if twitter streaming connection successful then change the status code
        CollectionTask coll = GenericCache.getInstance().getTwtConfigMap(getCacheKey());
        coll.setStatusCode(Config.STATUS_CODE_COLLECTION_RUNNING);
        coll.setStatusMessage(null);
        GenericCache.getInstance().setTwtConfigMap(getCacheKey(), coll);

    }
    volatile boolean finished = false;

    public void stopMe() {
        finished = true;
    }

    public void abortCollection() {

        JedisConnectionPool.close(publisherJedis);
        twitterStream.cleanUp();
        twitterStream.shutdown();
        cleanCache();
        log(LOG_LEVEL.INFO, "AIDR-Fetcher: Collection aborted which was tracking [" + getStreamQuery().getToTrackToString() + "] AND following [" + getStreamQuery().getToFollowToString() + "]");

    }

    public void cleanCache() {
        GenericCache.getInstance().deleteCounter(getCacheKey());
        GenericCache.getInstance().delTwtConfigMap(getCacheKey());
        GenericCache.getInstance().delLastDownloadedDoc(getCacheKey());
    }

    /**
     * @return the configBuilder
     */
    public ConfigurationBuilder getConfigBuilder() {
        return configBuilder;
    }

    /**
     * @param configBuilder the configBuilder to set
     */
    public void setConfigBuilder(ConfigurationBuilder cb) {
        this.configBuilder = cb;
    }

    /**
     * @return the streamQuery
     */
    public TwitterStreamQueryBuilder getStreamQuery() {
        return streamQuery;
    }

    /**
     * @param streamQuery the streamQuery to set
     */
    public void setStreamQuery(TwitterStreamQueryBuilder streamQuery) {
        this.streamQuery = streamQuery;
    }

    /**
     * @return the collectionCode
     */
    public String getCollectionCode() {
        return collectionCode;
    }

    /**
     * @param collectionCode the collectionCode to set
     */
    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    /**
     * @return the cacheKey
     */
    public String getCacheKey() {
        return cacheKey;
    }

    /**
     * @param cacheKey the cacheKey to set
     */
    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    /**
     * @return the collectionName
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * @param collectionName the collectionName to set
     */
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
