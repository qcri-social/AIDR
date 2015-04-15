package qa.qcri.aidr.collector.collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.JsonObject;

import org.apache.log4j.Logger;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.java7.Predicate;


public class TrackFilter implements Predicate<JsonObject> {

	private static Logger logger = Logger.getLogger(TrackFilter.class.getName());

	private String[] toTrack = null;
	private String patternString = "([^\"]\\S*|\".+?\")\\s*";
	private Pattern pattern = null;

	public TrackFilter() {
		pattern = Pattern.compile(patternString);
	}

	public TrackFilter(CollectionTask task){
		this();
		if (task != null) {
			this.setToTrack(task.getToTrack());
		} else {
			logger.error("Collection can't be null!");
		}
	}

	public TrackFilter(String keywords) {
		this();
		this.setToTrack(keywords);
	}

	public String[] getToTrack() {
		return this.toTrack;
	}

	public void setToTrack(final String keywords) {
		if (keywords != null && !keywords.isEmpty()) {
			this.toTrack = keywords.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			for (int i = 0; i < toTrack.length;i++) {
				this.toTrack[i] = this.toTrack[i].trim();		// remove leading and trailing whitespaces
			}
		} else {
			this.toTrack = null;
		}
	}

	private Set<String> toLowerCase(Set<String> wordSet) {
		Set<String> toLower = new HashSet<String>();
		for (String word : wordSet) {
			toLower.add(word.toLowerCase());
		}
		return toLower;
	}

	@Override
	public boolean test(JsonObject t) {
		if (null == toTrack) return true;

		String tweetText = t.get("text").toString();
		if (null == tweetText) {
			return false;		// there are filter-keywords but no text and hence reject tweet
		}
		// Otherwise test the tweet text for matching at least one of the keywords
		boolean result = hasKeyWords(tweetText);
		logger.info("Filtering result for tweet text : \"" + tweetText + "\": " + result);
		return result;
	}

	public boolean test(String text) {
		if (null == toTrack) return true;

		String tweetText = text;
		if (null == tweetText) {
			return false;		// there are filter-keywords but no text and hence reject tweet
		}
		boolean result = hasKeyWords(tweetText);
		logger.info("Filtering result for text : \"" + tweetText + "\": " + result);
		return result;
	}

	private boolean hasKeyWords(final String tweetText) {
		try {
			Set<String> keywordSet = toLowerCase(new HashSet<String>(Arrays.asList(toTrack)));
			Set<String> tweetTextSet = toLowerCase(new HashSet<String>(splitOnWhitespace(tweetText)));		

			// first test - if a simple intersection is not null, then great!
			Set<String> intersection = new HashSet<String>(keywordSet);
			intersection.retainAll(tweetTextSet);
			if (intersection != null && !intersection.isEmpty()) {
				//System.out.println("Match found: " + intersection);
				return true;
			}
			// if the above test failed - then we need to check for #-tag matches
			// first expand the keyword set by creating new #-tagged entries for each non #-tagged keyword
			Set<String> hashWordSet = new HashSet<String>();
			for (String word: keywordSet) {
				if (!word.startsWith("#")) {
					String hashTaggedWord = "#" + word;
					hashWordSet.add(hashTaggedWord);
				}
			}
			if (!hashWordSet.isEmpty()) keywordSet.addAll(hashWordSet);

			// Check again for intersection with tweet text
			intersection = new HashSet<String>(keywordSet);
			intersection.retainAll(tweetTextSet);
			if (intersection != null && !intersection.isEmpty()) {
				//System.out.println("#-tag Match found: " + intersection);
				return true;
			}

			// Still false - next check multiword keywords for a match
			for (String word: keywordSet) {
				//System.out.println("Looking at keyword: " + word);
				List<String> wordList = splitOnWhitespace(word);
				if (!wordList.isEmpty()) {
					// this is a multi-word keyword - check if ALL words are present in the tweet
					boolean flag = true;
					for (String w : wordList) {
						//System.out.println(wordList.size() + ": For keyword = " + word + ", Check " + w + " contained in = " + tweetTextSet.contains(w));
						if (!tweetTextSet.contains(w)) {
							flag = false;
							break;
						}
					}
					if (flag) {
						//System.out.println("Multiword match found: " + word);
						return true;		// found a match!
					}
				}
			}

			//Otherwise return false - all attempts to find a match failed
			return false;
		} catch (Exception e) {
			logger.error("Exception", e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param str string to split
	 * @return
	 */
	private List<String> splitOnWhitespace(String str) {
		List<String> list = new ArrayList<String>();

		Matcher m = pattern.matcher(str);
		while (m.find()) {
			list.add(m.group(1)); // Add .replace("\"", "") to remove surrounding quotes.
		}
		return list;
	}

	public static void main(String args[]) throws Exception {
		String tweet = "The quick brown fox jumped over the internet #fence #Fox @google \"yeah right!\"";
		String keywords = "hello, brown Internet, \"yeah, babby\", yeah,";
		TrackFilter filter = new TrackFilter(keywords);
		System.out.println("Keyword List: ");
		for (String w: filter.getToTrack()) {
			System.out.println(w);
		}
		System.out.println("Match result = " + filter.test(tweet));
	}

	@Override
	public String getFilterName() {
		return this.getClass().getSimpleName();
	}


}

