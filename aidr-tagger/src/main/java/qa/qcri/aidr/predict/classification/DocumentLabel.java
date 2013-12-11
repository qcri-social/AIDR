package qa.qcri.aidr.predict.classification;

/**
 * A DocumentLabel is any label on a Document, such as a geotag, a named entity
 * or a topic tag.
 * 
 * @author jrogstadius
 */
public interface DocumentLabel {
    public double getConfidence();

    public boolean isHumanLabel();
}
