package qa.qcri.aidr.collector.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Imran
 */
public class TwitterStreamQueryBuilder {

    private String[] toTrackArray;
    private long[] toFollowerArray;
    private double[][] geoLocations; // -180,-90,180,90 for any geo tagged tweet
    private Map<Integer, String> langMap;
    

	// add geolocation
	public TwitterStreamQueryBuilder(String track, String follow, String geoLocation, String langFilters) {
		this();
		setToFollow(follow);
		setToTrack(track);
		setGeoLocation(geoLocation);
		setLanguageFilter(langFilters.toLowerCase());
	}

	public TwitterStreamQueryBuilder() {
		langMap = new HashMap<Integer, String>();
	}
	
	/**
	 * Setting this parameter to a comma-separated list of BCP 47
	 * language identifiers corresponding to any of the languages
	 * listed on Twitter’s advanced search page will only return
	 * Tweets that have been detected as being written in the
	 * specified languages. For example, connecting with
	 * language=en will only stream Tweets detected to be in
	 * the English language.
	 */
    public void setLanguageFilter(String langFilters) {
        if (StringUtils.isNotEmpty(langFilters)) {
            String[] languages = langFilters.split(",");
            int count=1;
            for (String language : languages) {
                langMap.put(count++, language);
            }
        }
    }

    public boolean isLanguageAllowed(String lang) {
         if (langMap.containsValue(lang.toLowerCase())) {
            return true;
        }

        return false;
    }

    /**
     * A comma-separated list of longitude,latitude pairs specifying
     * a set of bounding boxes to filter Tweets by. Only geolocated
     * Tweets falling within the requested bounding boxes will be
     * included—unlike the Search API, the user’s location field
     * is not used to filter tweets.
     * @see https://dev.twitter.com/streaming/overview/request-parameters#locations
     */
    public void setGeoLocation(String geoLocationStr) {

        if (StringUtils.isNotEmpty(geoLocationStr)) {
            String[] XYs = geoLocationStr.split(",");
            if (XYs.length % 4 != 0) {
                throw new IllegalArgumentException("GeoLocation coordinates error. Must have coordinates that number in multiples of 4 (each 4-set represents one rectangle/bounding box.)");
            }

            geoLocations = new double[XYs.length / 2][2];
            for (int i = 0; i < XYs.length; i = i + 2) {
                // Read 2 elements at a time, into each 2-element sub-array of 'locations'
                geoLocations[i / 2][0] = Double.parseDouble(XYs[i].trim());
                geoLocations[i / 2][1] = Double.parseDouble(XYs[i + 1].trim());
            }
        }
    }

    public double[][] getGeoLocation() {

        return this.geoLocations;
    }

    /**
     * A comma-separated list of user IDs, indicating the users whose Tweets
     * should be delivered on the stream.
     */
    public void setToFollow(String toFollow) {

        if (StringUtils.isNotEmpty(toFollow)) {

            final String[] parsed = toFollow.split(",");
            toFollowerArray = new long[parsed.length];

            for (int i = 0; i < parsed.length; i++) {
                toFollowerArray[i] = Long.parseLong(parsed[i]);
            }
        }
    }

    /**
     * A comma-separated list of phrases which will be used to determine
     * what Tweets will be delivered on the stream. A phrase may be one or
     * more terms separated by spaces, and a phrase will match if all of
     * the terms in the phrase are present in the Tweet, regardless of
     * order and ignoring case. By this model, you can think of commas
     * as logical ORs, while spaces are equivalent to logical ANDs (e.g.
     * ‘the twitter’ is the AND twitter, and ‘the,twitter’ is the OR twitter).
     * @see https://dev.twitter.com/streaming/overview/request-parameters#track
     */
    public void setToTrack(String track) {
        if (StringUtils.isNotEmpty(track)) {
            this.toTrackArray = track.split(",");
        }
    }

    public String[] getToTrack() {
        return this.toTrackArray;
    }

    public String getToTrackToString() {
        String str = "";
        if (toTrackArray != null) {
            for (String s : this.toTrackArray) {
                str += s;
            }
        }
        return str;
    }

    public String getToFollowToString() {
        String str = "";
        if (toFollowerArray != null) {
            for (Long s : this.toFollowerArray) {
                str += s;
            }
        }
        return str;
    }
    
    public long[] getToFollow() {
        return toFollowerArray;
    }
}
