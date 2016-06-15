package qa.qcri.aidr.collector.api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.google.gson.Gson;

import facebook4j.Event;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Group;
import facebook4j.Ordering;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.internal.org.json.JSONException;

/**
 * @author Kushal RESTFul APIs to start and stop Twitter collections. TODO:
 *         remove non-API related operations such as startPersister to other
 *         appropriate classes.
 */
public class FbCollectorTest {
	
	private static String accessToken = "EAACEdEose0cBAJwNtjZCHT1RtPkwHQCAqtqrpojdNX6n6dnRzkiRDfrXo4QHdv1ZAvoChFszc10uLVrkfZBNGChVnbrxKYHMQFXX2CnwgxjPllnnmoO94SeZAZC5S20F5YkagP19kviBTV3X2wuKT1W0JHweGUF4eNQFZAtUS8YydvytSiWhZBn";
	private static String permissions = "public_profile,user_friends,email,user_about_me,user_actions.books,user_actions.fitness,user_actions.music,user_actions.news,user_actions.video,user_birthday,user_education_history,user_events,user_games_activity,user_hometownuser_likes,user_location,user_managed_groups,user_photos,user_posts,user_relationships,user_relationship_details,user_religion_politics,user_tagged_places,user_videos,user_website,user_work_history,read_custom_friendlists,read_insights,read_audience_network_insights,read_page_mailboxes,manage_pages,publish_pages,publish_actions,rsvp_event,pages_show_list,pages_manage_cta,pages_manage_instant_articles,ads_read,ads_management,pages_messaging,pages_messaging_phone_number ";
	private static String appId = "1733662670179050";
	private static String appSecret = "230e58387a4a4041be2ad0108ef8f30b";
	private static Integer limit = 100;
	
	public static void main(String atgs[]) throws IOException, JSONException {
		Facebook facebook = new FacebookFactory().getInstance();
		
		facebook.setOAuthAppId(appId,appSecret);
		facebook.setOAuthPermissions(permissions);
		facebook.setOAuthAccessToken(new AccessToken(accessToken,1463769000L));
		String keyword = "spallions";
		Long fromTimestamp = 1463855400L; //22 may
		Long toTimestamp = ((new Date()).getTime())/1000;
		
	}

	private static void pagePosts(Facebook facebook, String keyword, Long fromTimestamp, Long toTimestamp) throws FacebookException,
			IOException {
		Gson gson = new Gson();
		//Post post = facebook.getPost("854029464652397_1020536088001733", new Reading().summary());
		int pageOffset = 0;
		while(pageOffset>=0){
			ResponseList<Page> pages = facebook.searchPages(keyword,  new Reading().limit(limit).order(Ordering.CHRONOLOGICAL).offset(pageOffset));
			pageOffset = pages.size() == limit ? pageOffset + limit : -1;
			
			FileWriter fw = new FileWriter("C:/Users/Kushal/Desktop/abcPage.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			System.out.println(" page size "+ pages.size());
			for (Page page : pages) {
				int postsOffset=0;
				while(postsOffset>=0){
					ResponseList<Post> feed = facebook.getFeed(page.getId(),new Reading().since(fromTimestamp.toString()).fields("likes.summary(true)").until(toTimestamp.toString()).order(Ordering.CHRONOLOGICAL).limit(limit).offset(postsOffset));
					postsOffset = feed.size() == limit ? postsOffset + limit : -1;
					System.out.println("post size : "+feed.size()+ " for page : " +page.getId());
					for (Post post : feed) {
						//bw.write(gson.toJson(post) + "\n\n\n");
						bw.write(post + "\n\n\n");
					}
				}
				
			}
			bw.close();
		}
	}
	
	private static void eventPosts(Facebook facebook, String keyword, Long fromTimestamp, Long toTimestamp) throws FacebookException,
	IOException {
		Gson gson = new Gson();
		//Post post = facebook.getPost("854029464652397_1020536088001733", new Reading().summary());
		int eventsOffset = 0;
		while(eventsOffset>=0){
			ResponseList<Event> events = facebook.searchEvents(keyword,  new Reading().limit(limit).order(Ordering.CHRONOLOGICAL).offset(eventsOffset));
			eventsOffset = events.size() == limit ? eventsOffset + limit : -1;
			
			FileWriter fw = new FileWriter("C:/Users/Kushal/Desktop/abcEvent.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			System.out.println("Event size "+ events.size());
			for (Event event : events) {
				int postsOffset=0;
				while(postsOffset>=0){
					ResponseList<Post> feed = facebook.getFeed(event.getId(),new Reading().since(fromTimestamp.toString()).until(toTimestamp.toString()).order(Ordering.CHRONOLOGICAL).limit(limit).offset(postsOffset).summary());
					postsOffset = feed.size() == limit ? postsOffset + limit : -1;
					System.out.println("post size : "+feed.size()+ " for event : " +event.getId());
					for (Post post : feed) {
						bw.write(gson.toJson(post) + "\n\n\n");
					}
				}
				
			}
			bw.close();
		}
	}
	
	private static void groupPosts(Facebook facebook, String keyword, Long fromTimestamp, Long toTimestamp) throws FacebookException,
	IOException {
		Gson gson = new Gson();
		//Post post = facebook.getPost("854029464652397_1020536088001733", new Reading().summary());
		int groupsOffset = 0;
		while(groupsOffset>=0){
			ResponseList<Group> groups = facebook.searchGroups(keyword,  new Reading().limit(limit).order(Ordering.CHRONOLOGICAL).offset(groupsOffset));
			groupsOffset = groups.size() == limit ? groupsOffset + limit : -1;
			
			FileWriter fw = new FileWriter("C:/Users/Kushal/Desktop/abcGroup.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			System.out.println("Group size "+ groups.size());
			for (Group group : groups) {
				int postsOffset=0;
				while(postsOffset>=0){
					ResponseList<Post> feed = facebook.getFeed(group.getId(),new Reading().since(fromTimestamp.toString()).until(toTimestamp.toString()).order(Ordering.CHRONOLOGICAL).limit(limit).offset(postsOffset));
					postsOffset = feed.size() == limit ? postsOffset + limit : -1;
					System.out.println("post size : "+feed.size()+ " for group : " +group.getId());
					for (Post post : feed) {
						bw.write(gson.toJson(post) + "\n\n\n");
					}
				}
				
			}
			bw.close();
		}
	}
	
}
