package qa.qcri.aidr.analysis.utils;

import qa.qcri.aidr.common.code.ConfigurationProperty;

public enum OutputConfigurationProperty implements ConfigurationProperty {

	REDIS_HOST("REDIS_HOST"), REDIS_PORT("REDIS_PORT"), LOGGER("logger"), 
	PERSISTER_LOAD_LIMIT("PERSISTER_LOAD_LIMIT"), PERSISTER_LOAD_CHECK_INTERVAL_MINUTES("PERSISTER_LOAD_CHECK_INTERVAL_MINUTES"), 
	MAX_MESSAGES_COUNT("MAX_MESSAGES_COUNT"), TAGGER_CHANNEL_BASENAME("TAGGER_CHANNEL_BASENAME");

	private final String configurationProperty;

	private OutputConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
