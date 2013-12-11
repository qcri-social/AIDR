package qa.qcri.aidr.predict.classification;

/**
 * A filter for fetching document labels that match a particular criterion.
 * 
 * @author jrogstadius
 */
public interface DocumentLabelFilter {
    public boolean match(DocumentLabel label);
}
