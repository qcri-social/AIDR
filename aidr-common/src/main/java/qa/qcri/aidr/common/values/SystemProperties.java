package qa.qcri.aidr.common.values;

/**
 * Common URLs pointing to the APIs of different modules.
 * 
 * TODO: REMOVE. Remove this class and move these to the common configuration file.
 */
public class SystemProperties {
	/**
	 * The URL of the  API of aidr-collector.
	 */
	public static final String fetchMainUrl = "http://localhost:8084/AIDRCollector/webresources";

	/**
	 * The URL of the  API of aidr-tagger.
	 */
	public static final String taggerMainUrl = "http://localhost:8084/AIDRTaggerAPI/rest";

	/**
	 * The URL of the  API of aidr-persister
	 */
	public static final String persisterMainUrl = "http://localhost:8084/AIDRPersister/webresources";

	/**
	 * The URL of the  API of aidr-trainer
	 */
	public static final String crowdsourcingAPIMainUrl = "http://localhost:8084/AIDRTrainerAPI/rest";

	/**
	 * The URL of the API of aidr-output
	 */
	public static final String outputAPIMainUrl = "http://localhost:8084/AIDROutput/rest";

	/**
	 * The URL of the home page.
	 */
	public static final String serverUrl = "http://localhost/";
}
