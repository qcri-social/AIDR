package qa.qcri.aidr.predict.data;

import java.io.Serializable;

import qa.qcri.aidr.predict.common.DocumentType;

/**
 * Structured representation of a single Tweet.
 * 
 * @author jrogstadius
 */
public class SMS extends Document implements Serializable {
    private static final long serialVersionUID = 1L;

    
    //GeoLabel.LonLatPair geotag;
    //String text;
    
    
    private AIDR aidr;
    private String text;

    public AIDR getAidr() {
        return aidr;
    }

    public void setAidr(AIDR aidr) {
        this.aidr = aidr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getDoctype() {
        return DocumentType.SMS_DOC;
    }

    @Override
	public void setDoctype(String type) {
		this.doctype = type;
	}

    @Override
    public boolean isNovel() {
        return true;
    }
    
}
