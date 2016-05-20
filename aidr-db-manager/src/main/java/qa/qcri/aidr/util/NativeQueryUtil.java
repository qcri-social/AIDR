package qa.qcri.aidr.util;

public class NativeQueryUtil {

	public static final String TRAINING_COUNT_FOR_CRISIS = " select na.nominalAttributeID, na.name, "
			+ " mf.modelFamilyID, mf.isActive, count(1) as humanTaggedCount from model_family mf "
			+ " join nominal_attribute na on na.nominalAttributeID = mf.nominalAttributeID"
			+ " join document d on d.crisisID = :crisisID "
			+ " join document_nominal_label dnl on d.documentID = dnl.documentID"
			+ " join nominal_label lbl on lbl.nominalLabelID=dnl.nominalLabelID"
			+ " where mf.crisisID = :crisisID group by mf.nominalAttributeID;";
	
	public static final String MODEL_DETAILS_FOR_CRISIS = "select m.modelID, m.avgAuc, m.modelFamilyID, "
			+ "sum(mnl.classifiedDocumentCount) from model_nominal_label mnl "
			+ "join model m on m.modelID = mnl.modelID "
			+ "where m.modelFamilyID in (:modelFamilyIds) and m.isCurrentModel group by m.modelID;";		


}
