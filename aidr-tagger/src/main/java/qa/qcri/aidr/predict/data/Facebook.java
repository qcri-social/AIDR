package qa.qcri.aidr.predict.data;

import java.io.Serializable;

import qa.qcri.aidr.predict.common.DocumentType;

/**
 * 
 * @author Aman
 *
 */
public class Facebook extends Document implements Serializable {
    private static final long serialVersionUID = 1L;

    Long userID;
    boolean isShared;
    String text;

    @Override
    public Long getUserID() {
        return userID;
    }

    @Override
    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getDoctype() {
        return DocumentType.FACEBOOK_DOC;
    }

    @Override
    public boolean isNovel() {
        return !isShared;
    }

	@Override
	public void setDoctype(String type) {
		this.doctype = type;
	}
}
