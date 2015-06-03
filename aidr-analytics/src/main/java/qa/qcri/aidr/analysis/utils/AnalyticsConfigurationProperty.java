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

	GRANULARITY("granularity");

	private final String configurationProperty;

	private AnalyticsConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
