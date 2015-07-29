package qa.qcri.aidr.predict.communication;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.predict.classification.DocumentLabel;
import qa.qcri.aidr.predict.classification.nominal.NominalLabelBC;

import org.apache.log4j.Logger;
import org.json.*;

/**
 * OutputFilter contains a filter specified by an AIDR consumer, such as a
 * geographic bounding box or a specific ontology label.
 *
 * @author jrogstadius
 */
public class OutputFilter {

    public static final String ATTRIBUTE_FILTER = "attribute";

    private static Logger logger = Logger.getLogger(OutputFilter.class);

    private static final String STR_CRISIS_ID = "crisis_id",
            STR_FILTERS = "filters", STR_TYPE = "type",
            STR_ATTRIBUTE_ID = "attribute_id";

    public static class NominalAttributeFilter {
        public int attributeID;
    }

    public int crisisID;
    public String crisisCode;
    public ArrayList<NominalAttributeFilter> attributeFilters = new ArrayList<>();

    public boolean hasAttributeFilters() {
        return attributeFilters.isEmpty();
    }

    public boolean match(List<DocumentLabel> labels) {

        for (DocumentLabel label : labels) {
            if (label instanceof NominalLabelBC) {
                if (match((NominalLabelBC) label))
                    return true;
            } else
                logger.error("Unsupported label type: " + label);
            	throw new RuntimeException("Unsupported label type");
        }
        return false;
    }

    public boolean match(NominalLabelBC label) {
        for (NominalAttributeFilter filter : attributeFilters) {
            if (filter.attributeID == label.getAttributeID())
                return true;
        }

        return false;
    }

    public static OutputFilter fromJson(String json) {
        try {
            //System.out.println("Received JSON : " + json);
            //logger.info("Received JSON: " + json);
            JSONObject obj = new JSONObject(json);
            OutputFilter filter = new OutputFilter();
            //filter.crisisID = obj.getInt(STR_CRISIS_ID);
            filter.crisisCode = obj.getString("crisisCode");

            if (obj.has(STR_FILTERS)) {
                JSONArray filterArr = obj.getJSONArray(STR_FILTERS);

                for (int i = 0; i < filterArr.length(); i++) {
                    JSONObject fobj = filterArr.getJSONObject(i);
                    String filterType = fobj.getString(STR_TYPE);

                    if (filterType.equals(ATTRIBUTE_FILTER)) {
                        NominalAttributeFilter label = new NominalAttributeFilter();
                        label.attributeID = fobj.getInt(STR_ATTRIBUTE_ID);
                        filter.attributeFilters.add(label);
                    }
                }
            }

            return filter;
        } catch (JSONException e) {
        	logger.error("Error in parsing received json: " + json);
            throw new RuntimeException(e);
        }
    }
}
