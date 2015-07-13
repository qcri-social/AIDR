package qa.qcri.aidr.collector.utils;

import qa.qcri.aidr.common.code.ConfigurationProperty;

/**
 * @author dhruv-sharma
 * 
 *         Enum containing all the property keys required by the aidr-collector
 *         module.
 *
 */
public enum CollectorConfigurationProperty implements ConfigurationProperty {

	COLLECTOR_REST_URI("FETCHER_REST_URI"), PERSISTER_REST_URI(
			"PERSISTER_REST_URI"), LANGUAGE_ALLOWED_ALL("LANGUAGE_ALLOWED_ALL"), DEFAULT_PERSISTANCE_MODE(
			"DEFAULT_PERSISTANCE_MODE"), COLLECTOR_CHANNEL("FETCHER_CHANNEL"), TAGGER_CHANNEL(
			"TAGGER_CHANNEL"), COLLECTOR_REDIS_COUNTER_UPDATE_THRESHOLD(
			"FETCHER_REDIS_COUNTER_UPDATE_THRESHOLD"), LOG_FILE_NAME(
			"LOG_FILE_NAME"), STATUS_CODE_COLLECTION_INITIALIZING(
			"STATUS_CODE_COLLECTION_INITIALIZING"), STATUS_CODE_COLLECTION_RUNNING(
			"STATUS_CODE_COLLECTION_RUNNING"), STATUS_CODE_COLLECTION_ERROR(
			"STATUS_CODE_COLLECTION_ERROR"), STATUS_CODE_COLLECTION_RUNNING_WARNING(
			"STATUS_CODE_COLLECTION_RUNNING_WARNING"), STATUS_CODE_COLLECTION_NOTFOUND(
			"STATUS_CODE_COLLECTION_NOTFOUND"), STATUS_CODE_COLLECTION_STOPPED(
			"STATUS_CODE_COLLECTION_STOPPED"), INPUT_PORT("INPUT_PORT"), OUTPUT_PORT(
			"OUTPUT_PORT"), PERSISTER_LOAD_LIMIT("PERSISTER_LOAD_LIMIT"), PERSISTER_LOAD_CHECK_INTERVAL_MINUTES(
			"PERSISTER_LOAD_CHECK_INTERVAL_MINUTES"), STATUS_CODE_SUCCESS(
			"STATUS_CODE_SUCCESS"), STATUS_CODE_ERROR("STATUS_CODE_ERROR"), STATUS_CODE_WARNING(
			"STATUS_CODE_WARNING"), REDIS_HOST("REDIS_HOST"), REDIS_PORT("REDIS_PORT"),TAGGER_REST_URI("TAGGER_REST_URI"),
			RECONNECT_NET_FAILURE_WAIT_SECONDS("RECONNECT_NET_FAILURE_WAIT_SECONDS"),
			RECONNECT_NET_FAILURE_RETRY_ATTEMPTS("RECONNECT_NET_FAILURE_RETRY_ATTEMPTS"),
			RECONNECT_RATE_LIMIT_WAIT_SECONDS("RECONNECT_RATE_LIMIT_WAIT_SECONDS"),
			RECONNECT_RATE_LIMIT_RETRY_ATTEMPTS("RECONNECT_RATE_LIMIT_RETRY_ATTEMPTS"),
			RECONNECT_SERVICE_UNAVAILABLE_WAIT_SECONDS("RECONNECT_SERVICE_UNAVAILABLE_WAIT_SECONDS"),
			RECONNECT_SERVICE_UNAVAILABLE_RETRY_ATTEMPTS("RECONNECT_SERVICE_UNAVAILABLE_RETRY_ATTEMPTS");

	private final String configurationProperty;

	private CollectorConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
