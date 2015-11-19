/**
 * 
 */
package qa.qcri.aidr.data.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Latika
 *
 */
@Component
public class Configurations {

	private static ApplicationConfigurations applicationConfigurations;

	/**To load all configuration file by taking instances of each configuration file.
	 * @param applicationConfigurations
	 */
	@Autowired
	public void init(ApplicationConfigurations applicationConfigurations){
		Configurations.applicationConfigurations = applicationConfigurations;
	}
	
	/**
	 * @return Application root url
	 */
	public static String getAidrRootUrl() {
		return applicationConfigurations.aidrRootUrl.trim();
	}
	
	/**
	 * @return Collection Data API
	 */
	public static String getCollectionDataAPI() {
		return getAidrRootUrl() + applicationConfigurations.collectionDataAPI.trim();
	}
	
	/**
	 * @return Persister URL
	 */
	public static String getPersisterURL() {
		return applicationConfigurations.persisterURL.trim();
	}
	
	/**
	 * @return Alternate Persister URL for older collections
	 */
	public static String getPersisterURLForOlderCollections() {
		return applicationConfigurations.persisterURLForOlderCollections.trim();
	}
	

}
