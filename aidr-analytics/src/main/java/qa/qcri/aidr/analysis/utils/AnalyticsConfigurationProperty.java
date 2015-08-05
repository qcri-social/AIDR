/**
 * 
 */
package qa.qcri.aidr.analysis.utils;

import qa.qcri.aidr.common.code.ConfigurationProperty;

/**
 * @author dhruv-sharma
 * 
 *         Enum containing all the property keys required by the aidr-analysis
 *         module.
 *
 */
public enum AnalyticsConfigurationProperty implements ConfigurationProperty {

	GRANULARITY("granularity"), TAGGER_REST_URI("TAGGER_REST_URI"), REDIS_HOST("REDIS_HOST"), REDIS_PORT("REDIS_PORT"), LOGGER("logger"), 
	PERSISTER_LOAD_LIMIT("PERSISTER_LOAD_LIMIT"), PERSISTER_LOAD_CHECK_INTERVAL_MINUTES("PERSISTER_LOAD_CHECK_INTERVAL_MINUTES"), 
	MAX_MESSAGES_COUNT("MAX_MESSAGES_COUNT"), TAGGER_CHANNEL_BASENAME("TAGGER_CHANNEL_BASENAME");

	private final String configurationProperty;

	private AnalyticsConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
