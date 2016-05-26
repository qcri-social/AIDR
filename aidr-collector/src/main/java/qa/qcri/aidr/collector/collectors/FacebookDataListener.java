package qa.qcri.aidr.collector.collectors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.utils.GenericCache;

import com.google.gson.Gson;

import facebook4j.Event;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Group;
import facebook4j.Ordering;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;

@Component
public class FacebookDataListener {

	 @Async
	    public void pagePosts(Facebook facebook, CollectionTask task, Date toTimestamp) throws FacebookException,
	    IOException {
	    	int pageOffset = 0;
	    	//ToDO: move limit to config.properties
	    	Integer limit = 100; 
	    	List<String> pageIds = new ArrayList<String>();
	    	while(pageOffset>=0){
	    		ResponseList<Page> pages = facebook.searchPages(task.getToTrack(), new Reading().fields("id").limit(limit).order(Ordering.CHRONOLOGICAL).offset(pageOffset));
	    		pageOffset = pages.size() == limit ? pageOffset + limit : -1;
	    		for (Page page : pages) {
	    			String id = page.getId();
	    			pageIds.add(id);
	    		}
	    	}
	    	processPost(facebook, task, toTimestamp, limit, pageIds);
	    }
	    
	    @Async
	    public void eventPosts(Facebook facebook, CollectionTask task, Date toTimestamp) throws FacebookException,
	    IOException {
	    	int eventOffset = 0;
	    	//ToDO: move limit to config.properties
	    	Integer limit = 100; 
	    	List<String> eventIds = new ArrayList<String>();
	    	while(eventOffset>=0){
	    		ResponseList<Event> events = facebook.searchEvents(task.getToTrack(), new Reading().fields("id").limit(limit).order(Ordering.CHRONOLOGICAL).offset(eventOffset));
	    		eventOffset = events.size() == limit ? eventOffset + limit : -1;
	    		for (Event event : events) {
	    			String id = event.getId();
	    			eventIds.add(id);
	    		}
	    	}
	    	processPost(facebook, task, toTimestamp, limit, eventIds);
	    }
	    
	    @Async
	    public void groupPosts(Facebook facebook, CollectionTask task, Date toTimestamp) throws FacebookException,
	    IOException {
	    	int groupOffset = 0;
	    	//ToDO: move limit to config.properties
	    	Integer limit = 100; 
	    	List<String> groupIds = new ArrayList<String>();
	    	while(groupOffset>=0){
	    		ResponseList<Group> groups = facebook.searchGroups(task.getToTrack(), new Reading().fields("id").limit(limit).order(Ordering.CHRONOLOGICAL).offset(groupOffset));
	    		groupOffset = groups.size() == limit ? groupOffset + limit : -1;
	    		for (Group group : groups) {
	    			String id = group.getId();
	    			groupIds.add(id);
	    		}
	    	}
	    	processPost(facebook, task, toTimestamp, limit, groupIds);
	    }

	    private void processPost(Facebook facebook, CollectionTask task,
	    		Date toTimestamp, Integer limit, List<String> Ids)
	    				throws IOException, FacebookException {
	    	Gson gson = new Gson();
	    	
	    	//Move to Redis
	    	FileWriter fw = new FileWriter("C:/Users/Kushal/Desktop/abcPage.txt", true);
	    	BufferedWriter bw = new BufferedWriter(fw);
	    	for (String id : Ids) {
	    		int postsOffset=0;
	    		while(postsOffset>=0){
	    			ResponseList<Post> feed = facebook.getFeed(id, new Reading().since(new Date(task.getLastCollectedAt())).until(toTimestamp).order(Ordering.CHRONOLOGICAL).limit(limit).offset(postsOffset));
	    			postsOffset = feed.size() == limit ? postsOffset + limit : -1;
	    			for (Post post : feed) {
	    				bw.write(gson.toJson(post) + "\n\n\n");
	    				//bw.write(post + "\n\n\n");
	    			}
	    			GenericCache.getInstance().incrCounter(task.getCollectionCode(), (long) feed.size());
	    			if(feed != null && feed.size()>0){
	    				String lastDownloadedDoc = feed.get(feed.size()-1).getMessage();
	    				GenericCache.getInstance().setLastDownloadedDoc(task.getCollectionCode(), lastDownloadedDoc);
	    			}

	    		}

	    	}
	    	bw.close();
	    }
}
