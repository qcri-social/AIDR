package qa.qcri.aidr.collector.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.collector.utils.GenericCache;

@Service
public class CollectionService {
	
	@Async
	public void processFacebookDataCollection() {
		
		List<String> collectionsToRun = GenericCache.getInstance().getEligibleFacebookCollectionsToRun();
		for(String collection : collectionsToRun) {
			GenericCache.getInstance().getFacebookTracker(collection).start();
		}
	}

}
