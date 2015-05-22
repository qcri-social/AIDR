package qa.qcri.aidr.analysis.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.code.DateFormatConfig;
import qa.qcri.aidr.common.code.impl.BaseConfigurator;
import qa.qcri.aidr.common.exception.ConfigurationPropertyFileException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotRecognizedException;
import qa.qcri.aidr.common.exception.ConfigurationPropertyNotSetException;

public class AnalyticsConfigurator extends BaseConfigurator {

	private static final Logger logger = Logger
			.getLogger(AnalyticsConfigurator.class);

	public static String configLoadFileName = "granularity.properties";

	private static final AnalyticsConfigurator instance = new AnalyticsConfigurator();

	private Map<String, String> granularities;

	private AnalyticsConfigurator() {
		this.initProperties(configLoadFileName,
				AnalyticsConfigurationProperty.values());

	}

	public static AnalyticsConfigurator getInstance() {
		return instance;
	}

	public List<Long> getGranularities()
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException {
		List<Long> granularityList = new ArrayList<Long>();
		logger.info("Reading granularities from file: " + configLoadFileName);
		if (granularities != null) {
			String[] values = granularities.get(
					AnalyticsConfigurationProperty.GRANULARITY.getName())
					.split(",");
			for (int i = 0; i < values.length; i++) {
				granularityList.add(DateFormatConfig.parseTime(values[i]));
			}
			Collections.sort(granularityList);
		} else {
			return null;
		}
		return granularityList;
	}

}
