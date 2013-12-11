package qa.qcri.aidr.predict.classification.nominal;

import qa.qcri.aidr.predict.classification.DocumentLabel;
import qa.qcri.aidr.predict.classification.DocumentLabelFilter;

public class NominalLabelFilter implements DocumentLabelFilter {

    private int attributeID;
    private Integer labelID;

    public NominalLabelFilter(int attributeID, Integer labelID) {
        this.attributeID = attributeID;
        this.labelID = labelID;
    }

    @Override
    public boolean match(DocumentLabel label) {
        if (!(label instanceof NominalLabelBC))
            return false;

        NominalLabelBC nl = (NominalLabelBC) label;

        if (nl.getAttributeID() == attributeID
                && (labelID == null || nl.getNominalLabelID() == labelID))
            return true;

        return false;
    }
}
