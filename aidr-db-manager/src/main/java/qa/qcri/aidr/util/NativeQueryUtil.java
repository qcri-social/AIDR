package qa.qcri.aidr.util;

public class NativeQueryUtil {

	public static final String TRAINING_COUNT_FOR_CRISIS = " SELECT mf_temp.nominalAttributeID, mf_temp.name as attributeName,"
			+ " mf_temp.modelFamilyID, mf_temp.isActive,"
			+ " COALESCE(doc_temp.taggedCount, 0) as totalTaggedCount "
			+ " FROM "
				
			+ "(SELECT mf.nominalAttributeID, mf.isActive, mf.modelFamilyID, na.name "
			+ "FROM model_family mf, nominal_attribute na "
			+ "WHERE crisisID = :crisisID and mf.nominalAttributeID = na.nominalAttributeID) mf_temp "
			
			+ "LEFT JOIN "
			
			+ "(SELECT nl.nominalAttributeID, count(1) AS taggedCount "
			+ "FROM document_nominal_label dnl, document d, nominal_label nl "
			+ "WHERE dnl.documentID = d.documentID AND d.crisisID = :crisisID and nl.nominalLabelID = dnl.nominalLabelID "
			+ "GROUP BY nl.nominalAttributeID) doc_temp ON mf_temp.nominalAttributeID = doc_temp.nominalAttributeID "
		
			+ "GROUP BY mf_temp.nominalAttributeID;";

;
	
	public static final String MODEL_DETAILS_FOR_CRISIS = "select m.modelID, m.avgAuc, m.modelFamilyID, "
			+ "sum(mnl.classifiedDocumentCount) from model_nominal_label mnl "
			+ "join model m on m.modelID = mnl.modelID "
			+ "where m.modelFamilyID in (:modelFamilyIds) and m.isCurrentModel group by m.modelID;";		


}
