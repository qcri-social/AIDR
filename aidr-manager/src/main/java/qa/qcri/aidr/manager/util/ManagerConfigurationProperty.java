package qa.qcri.aidr.manager.util;

import qa.qcri.aidr.common.code.ConfigurationProperty;

/**
 * @author dhruv-sharma
 * 
 *         Enum containing all the property keys required by the aidr-manager
 *         module.
 * 
 */
public enum ManagerConfigurationProperty implements ConfigurationProperty {

	TWITTER_REQUEST_TOKEN_URL("twitter.RequesttokenURL"), 
	TWITTER_AUTHORIZE_URL("twitter.AuthorizeURL"), 
	TWITTER_ACCESS_TOKEN_URL("twitter.AccessTokenURL"), 
	SOCIAL_SIGN_IN_SIGN_UP_VIEW("socialsignin.signUpView"), 
	SOCIAL_SIGN_IN_DEFAULT_AUTHENTICATION_SUCCESS_URL("socialsignin.defaultAuthenticationSuccessUrl"), 
	SOCIAL_SIGN_IN_DEFAULT_ACCESS_DENIED_URL("socialsignin.defaultAccessDeniedUrl"), 
	TWITTER_CONSUMER_KEY("twitter.consumerKey"), 
	TWITTER_CONSUMER_SECRET("twitter.consumerSecret"), 
	FACEBOOK_CONSUMER_KEY("facebook.consumerKey"), 
	FACEBOOK_CONSUMER_SECRET("facebook.consumerSecret"), 
	GOOGLE_CONSUMER_KEY("google.consumerKey"), 
	GOOGLE_CONSUMER_SECRET("google.consumerSecret"), 
	TWITTER_CALLBACK_URL("twitter.callBackURL"), 
	APPLICATION_SECURE_URL("application.secureUrl"), 
	SQL_SERVER_HOST("SQL_SERVER_HOST"), 
	SQL_SERVER_PORT("SQL_SERVER_PORT"), 
	SQL_DRIVER("SQL_DRIVER"), 
	SQL_SERVER_TYPE("SQL_SERVER_TYPE"), 
	HIBERNATE_DIALECT("HIBERNATE_DIALECT"), 
	MANAGER_DB_NAME("MANAGER_DB_NAME"), 
	MANAGER_DB_USERNAME("MANAGER_DB_USERNAME"), 
	MANAGER_DB_PASSWORD("MANAGER_DB_PASSWORD"), 
	MANAGER_DB_HBM2DDL("MANAGER_DB_HBM2DDL"), 
	COLLECTOR_MAIN_URL("fetchMainUrl"), 
	TAGGER_MAIN_URL("taggerMainUrl"), 
	PERSISTER_MAIN_URL("persisterMainUrl"), 
	CROWDSOURCING_API_MAIN_URL("crowdsourcingAPIMainUrl"), 
	OUTPUT_MAIN_URL("outputAPIMainUrl"), 
	SERVER_URL("serverUrl"), 
	COLLECTION_COUNT_UPDATE_CRON("collection.update.notification.cron"),
	PUSHER_APP_ID("pusher.app.id"),
	PUSHER_APP_KEY("pusher.app.key"),
	PUSHER_APP_SECRET("pusher.app.secret"), 
	FACEBOOK_COLLECTION_DATA_FETCH_CRON("facebook.collection.fetch.data.cron"),
	START_UNEXPECTEDLY_STOPPED_COLLECTIONS_CRON("start.unexpextedly.stopped.collections.cron"),
	SAMPLE_COUNT_THRESHOLD("sample.count.threshold");
	
	private final String configurationProperty;

	private ManagerConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
