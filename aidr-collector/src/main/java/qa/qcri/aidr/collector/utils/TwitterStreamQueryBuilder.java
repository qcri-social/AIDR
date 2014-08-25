/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.collector.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        setToFollow(follow);
        setToTrack(track);
        setGeoLocation(geoLocation);
        langMap = new HashMap<Integer, String>();
        setLanguageFilter(langFilters.toLowerCase());
    }

    public TwitterStreamQueryBuilder() {
    }
    
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

    public void setToFollow(String toFollow) {

        if (StringUtils.isNotEmpty(toFollow)) {

            final String[] parsed = toFollow.split(",");
            toFollowerArray = new long[parsed.length];

            for (int i = 0; i < parsed.length; i++) {
                toFollowerArray[i] = Long.parseLong(parsed[i]);
            }
        }
    }

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
