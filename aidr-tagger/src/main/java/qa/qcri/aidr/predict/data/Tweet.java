package qa.qcri.aidr.predict.data;

import java.io.Serializable;

import qa.qcri.aidr.predict.classification.geo.GeoLabel;
import qa.qcri.aidr.predict.common.DocumentType;

/**
 * Structured representation of a single Tweet.
 * 
 * @author jrogstadius
 */
public class Tweet extends Document implements Serializable {
    private static final long serialVersionUID = 1L;

    Long userID;
    boolean isRetweet;
    GeoLabel.LonLatPair geotag;
    String text;

    @Override
    public Long getUserID() {
        return userID;
    }

    @Override
    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public GeoLabel.LonLatPair getGeotag() {
        return geotag;
    }

    public void setGeotag(GeoLabel.LonLatPair geotag) {
        this.geotag = geotag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getDoctype() {
        return DocumentType.TWIITER_DOC;
    }

    @Override
    public boolean isNovel() {
        return !isRetweet;
    }

	@Override
	public void setDoctype(String type) {
		this.doctype = type;
	}
}
