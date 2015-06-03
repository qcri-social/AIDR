package qa.qcri.aidr.predictui.util;

import qa.qcri.aidr.common.code.ConfigurationProperty;

public enum TaggerAPIConfigurationProperty implements ConfigurationProperty {

	AIDR_TAGGER_CONFIG_URL("AIDR_TAGGER_CONFIG_URL"), AIDR_PERSISTER_URL(
			"AIDR_PERSISTER_URL"), COMMON_CONFIG_PROPERTIES_PATH(
			"common.config.properties.path"), TASK_EXPIRY_AGE_LIMIT(
			"TASK_EXPIRY_AGE_LIMIT"), TASK_BUFFER_SCAN_INTERVAL(
			"TASK_BUFFER_SCAN_INTERVAL"), PUBLIC_LANDING_PAGE_TOP(
			"PUBLIC_LANDING_PAGE_TOP"), PUBLIC_LANDING_PAGE_BOTTOM(
			"PUBLIC_LANDING_PAGE_BOTTOM"), CLASSIFIER_DESCRIPTION_PAGE(
			"CLASSIFIER_DESCRIPTION_PAGE"), CLASSIFIER_TUTORIAL_ONE(
			"CLASSIFIER_TUTORIAL_ONE"), CLASSIFIER_TUTORIAL_TWO(
			"CLASSIFIER_TUTORIAL_TWO"), CUSTOM_CURATOR("CUSTOM_CURATOR"), CLASSIFIER_SKIN(
			"CLASSIFIER_SKIN"), STATUS_CODE_SUCCESS("STATUS_CODE_SUCCESS"), STATUS_CODE_FAILED(
			"STATUS_CODE_FAILED");

	private final String configurationProperty;

	private TaggerAPIConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
