package qa.qcri.aidr.manager.util;

/**
 * @author Kushal
 * Specifies the AIDR-SMS document structure.
 */
public class SMS {
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
}
