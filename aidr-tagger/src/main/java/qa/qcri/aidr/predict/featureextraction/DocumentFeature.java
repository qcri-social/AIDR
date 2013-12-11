package qa.qcri.aidr.predict.featureextraction;

import org.json.JSONObject;

/**
 * A DocumentFeature is a transformed representation of a Document, such as a
 * word vector.
 * 
 * @author jrogstadius
 */
public interface DocumentFeature {
    public JSONObject toJSONObject();
}
