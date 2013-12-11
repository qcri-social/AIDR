package qa.qcri.aidr.predict.classification.geo;

import java.io.Serializable;

/**
 * A geotag label for a DocumentSet.
 * 
 * @author jrogstadius
 */
public class GeoLabel implements Serializable {

    public static class LonLatPair implements Serializable {
        private static final long serialVersionUID = 1L;

        double latitude;
        double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public LonLatPair() {
        }

        public LonLatPair(double lon, double lat) {
            longitude = lon;
            latitude = lat;
        }
    }

    private static final long serialVersionUID = 1L;
    LonLatPair geotag;
    double confidence;
    long sourceID;

    public String getLabelType() {
        return "coordinate";
    }

    public LonLatPair getCoordinates() {
        return geotag;
    }

    public void setCoordinates(LonLatPair geo) {
        geotag = geo;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double score) {
        confidence = score;
    }

    public long getSourceID() {
        return sourceID;
    }

    public void setSourceID(long source) {
        sourceID = source;
    }
}
