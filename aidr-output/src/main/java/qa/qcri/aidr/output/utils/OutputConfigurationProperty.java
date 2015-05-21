package qa.qcri.aidr.output.utils;

import qa.qcri.aidr.common.code.ConfigurationProperty;

public enum OutputConfigurationProperty implements ConfigurationProperty {

	REDIS_HOST("host"), REDIS_PORT("port"), LOGGER("logger"), MANAGER_URL(
			"managerUrl"), PERSISTER_LOAD_LIMIT("PERSISTER_LOAD_LIMIT"), PERSISTER_LOAD_CHECK_INTERVAL_MINUTES(
			"PERSISTER_LOAD_CHECK_INTERVAL_MINUTES");

	private final String configurationProperty;

	private OutputConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
