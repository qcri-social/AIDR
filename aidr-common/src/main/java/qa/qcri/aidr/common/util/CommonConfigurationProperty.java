package qa.qcri.aidr.common.util;

import qa.qcri.aidr.common.code.ConfigurationProperty;

/**
 * @author meghnas
 * 
 *         Enum containing all the property keys required by the aidr-common module.
 *
 */

public enum CommonConfigurationProperty implements ConfigurationProperty {
	
	SENDER_EMAIL("SENDER_EMAIL"),SENDER_PASS("SENDER_PASS"),RECIPIENT_EMAIL("RECIPIENT_EMAILS"),
	SMTP_HOST("SMTP_HOST"),SMTP_PORT("SMTP_PORT"),AIDR_PROFILE("AIDR_PROFILE");
	
	private final String configurationProperty;

	private CommonConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
