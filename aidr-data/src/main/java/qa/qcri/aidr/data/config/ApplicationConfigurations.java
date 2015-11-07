package qa.qcri.aidr.data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfigurations {

	@Value("${aidr.root.url}")
	String aidrRootUrl;
	
	@Value("${aidr.collection.data.fetch.api}")
	String collectionDataAPI;
}
