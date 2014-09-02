package qa.qcri.aidr.predict.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.apache.log4j.Logger;

import qa.qcri.aidr.predict.DataStore;
import qa.qcri.aidr.predict.classification.DocumentLabel;
import qa.qcri.aidr.predict.classification.geo.GeoLabel;
import qa.qcri.aidr.predict.classification.nominal.NominalLabelBC;
import qa.qcri.aidr.predict.common.ErrorLog;
import qa.qcri.aidr.predict.common.Helpers;
import qa.qcri.aidr.predict.common.Loggable;
import qa.qcri.aidr.predict.dbentities.ModelFamilyEC;
import qa.qcri.aidr.predict.dbentities.NominalAttributeEC;
import qa.qcri.aidr.predict.dbentities.NominalLabelEC;
import qa.qcri.aidr.predict.featureextraction.DocumentFeature;
import qa.qcri.aidr.predict.featureextraction.WordSet;

/**
 * Helper class for converting between native Document objects and their JSON
 * representation.
 * 
 * @author jrogstadius
 */
public class DocumentJSONConverter extends Loggable {

	private static Logger logger = Logger.getLogger(DocumentJSONConverter.class);
	private static ErrorLog elog = new ErrorLog();

	public static enum Doctype {
		sms("sms"), TWEET("twitter");

		private String value;

		private Doctype(String value) {
			this.value = value;
		}

		public static Doctype parse(String value) {
			if (value.equals(TWEET.value))
				return TWEET;
			if (value.equals(sms.value))
				return sms;
			logger.error("Unknow doctype - can't parse: " + value);
			throw new RuntimeException("Unknown doctype: " + value);
		}
	}

	private static final String name = "DocumentJsonConverter";
	private static long lastModelInfoUpdate = 0;
	private static HashMap<Integer, HashMap<Integer, ModelFamilyEC>> activeModelFamiliesByID = new HashMap<>();
	private static HashMap<Integer, HashMap<String, ModelFamilyEC>> activeModelFamiliesByCode = new HashMap<>();
	private static HashMap<String, Integer> activeCrisisIDs = new HashMap<>();
	private static UnicodeEscaper unicodeEscaper = UnicodeEscaper.above(127); 

	public static Document parseDocument(String jsonInput)
			throws JSONException, IOException {

		JSONObject jsonObj = new JSONObject(jsonInput);

		if (!jsonObj.has("aidr")) {
			logger.error("Missing aidr field in input object");
			throw new JSONException("Missing aidr field in input object");
		}
		JSONObject aidr = jsonObj.getJSONObject("aidr");

		if (!aidr.has("doctype")) {
			logger.error("Missing doctype in input object");
			throw new JSONException("Missing doctype in input object");
		}
		Doctype doctype = Doctype.parse(aidr.getString("doctype"));
		
		Document doc = null;
		switch (doctype) {
		case TWEET:
			doc = parseTweet(jsonObj);
		case sms:
			doc = parseSMS(jsonObj);
			break;
		default: {
			logger.error("Exception when parsing input document: Unhandled doctype");
			throw new RuntimeException(
					"Exception when parsing input document: Unhandled doctype");
		}
		}

		if (!aidr.has("crisis_code")) {
			logger.error("Exception when parsing input document: Missing crisis_code");
			throw new RuntimeException(
					"Exception when parsing input document: Missing crisis_code");
		}
		doc.crisisID = new Long(getCrisisID(aidr.getString("crisis_code")));
		doc.crisisCode = aidr.getString("crisis_code");
		doc.inputJson = jsonObj;

		if (aidr.has("nominal_labels")) {
			JSONArray labels = aidr.getJSONArray("nominal_labels");
			for (int i = 0; i < labels.length(); i++) {
				doc.addLabel(parseNominalLabel(labels.getJSONObject(i)));
			}
		} 
		return doc;
	}

	public static SMS parseSMS(JSONObject input) {
		// TODO: the following code is only a placeholder!
		try {
			SMS sms = new SMS();

			JSONObject user;
			user = input.getJSONObject("user");
			sms.userID = user.getLong("id");
			sms.text = input.getString("text");
			sms.isReSent = false;		// TODO: implement a method to check for duplicate SMS
			sms.setDoctype(Doctype.sms.toString());
			return sms;
		} catch (JSONException e) {
			logger.error("Json exception in parsing tweet: " + input);
			logger.error(elog.toStringException(e));
			throw new RuntimeException(e);
		}
	}

	public static Tweet parseTweet(JSONObject input) {
		// Example of a tweet in JSON format:
		// https://dev.twitter.com/docs/api/1/get/search

		try {
			Tweet t = new Tweet();

			JSONObject user;
			user = input.getJSONObject("user");
			t.userID = user.getLong("id");
			t.text = input.getString("text");
			t.isRetweet = !input.isNull("retweeted_status");
			t.setDoctype(Doctype.TWEET.toString());
			if (input.has("coordinates") && !input.isNull("coordinates")) {
				JSONObject geo = (JSONObject) input
						.getJSONObject("coordinates");
				if (geo.getString("type") == "Point") {
					JSONArray coords = geo.getJSONArray("coordinates");
					GeoLabel.LonLatPair geotag = new GeoLabel.LonLatPair();
					geotag.setLongitude(coords.getDouble(0));
					geotag.setLatitude(coords.getDouble(1));
				}
			}

			return t;
		} catch (JSONException e) {
			logger.error("Json exception in parsing tweet: " + input);
			logger.error(elog.toStringException(e));
			throw new RuntimeException(e);
		}
	}

	public static String getDocumentSetJson(Document doc) {
		try {
			JSONObject input = doc.getInputJson();
			JSONObject aidr = input.getJSONObject("aidr");

			// Add features
			if (!aidr.has("features")) {
				ArrayList<DocumentFeature> features = doc
						.getFeatures(DocumentFeature.class);
				JSONArray featureArray = new JSONArray();
				for (DocumentFeature f : features)
					featureArray.put(f.toJSONObject());
				aidr.put("features", featureArray);
			}

			// Add labels
			if (!aidr.has("nominal_labels")) {
				ArrayList<NominalLabelBC> labels = doc
						.getLabels(NominalLabelBC.class);
				JSONArray labelArray = new JSONArray();
				if (!labels.isEmpty()) {
					for (NominalLabelBC l : labels) {
						try {
							JSONObject labelJson = getLabelJson(doc.crisisID.intValue(), l);
							labelArray.put(labelJson);
						}
						catch (RuntimeException e) {
							logger.error("Exception while converting document to JSON:" + l);
							logger.error(elog.toStringException(e));
						}
					}
				} else {
					logger.warn("Empty nominal_labels field! Inserting a dummy nominal label.");
					labelArray.put(createEmptyLabelJson());
				}
				aidr.put("nominal_labels", labelArray);

			}

			return unicodeEscaper.translate(input.toString());
		} catch (JSONException e) {
			logger.error("Error in creating JSON from document: " + doc);
			logger.error(elog.toStringException(e));
			throw new RuntimeException(e);
		}
	}

	public static <T extends DocumentFeature> String getFeaturesJson(
			Class<T> featureFilter, Document docSet) {

		if (featureFilter == WordSet.class) {
			ArrayList<T> items = docSet.getFeatures(featureFilter);

			ArrayList<String> allWords = new ArrayList<String>();
			for (T item : items) {
				WordSet words = (WordSet) item;
				allWords.addAll(words.getWords());
			}
			String s = "{\"words\":[\"" + Helpers.join(allWords, "\",\"")
					+ "\"]}";
			return s;
		} else {
			logger.warn("Not implemented: " + featureFilter);
			throw new RuntimeException("Not implemented");
		}
	}

	public static NominalLabelBC parseNominalLabel(JSONObject input) {
		try {
			int crisisID = getCrisisID(input.getString("crisis_code"));
			ModelFamilyEC modelFamily = getModelFamily(crisisID, input.getString("attribute_code"));
			NominalAttributeEC attr = modelFamily.getNominalAttribute();
			NominalLabelBC l = new NominalLabelBC(
					input.getLong("source_id"),
					attr.getNominalAttributeID(),
					attr.getNominalLabel(input.getString("label_code")).getNominalLabelID(),
					input.getDouble("confidence")); //TODO: Remove this, training samples should be "true"
			if (input.has("from_human"))
				l.setHumanLabel(input.getBoolean("from_human"));
			return l;
		} catch (JSONException e) {
			logger.error("Error in parsing nominal label for: " + input);
			logger.error(elog.toStringException(e));
			throw new RuntimeException(e);
		}
	}

	public static JSONObject createEmptyLabelJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("source_id", 0);
			obj.put("attribute_code", JSONObject.NULL);
			obj.put("attribute_name", JSONObject.NULL);
			obj.put("attribute_description", JSONObject.NULL);
			obj.put("label_code", JSONObject.NULL);
			obj.put("label_name", JSONObject.NULL);
			obj.put("label_description", JSONObject.NULL);
			obj.put("confidence", JSONObject.NULL);
			obj.put("from_human", false); 
		} catch (JSONException e) {
			logger.error("Error in creating empty json object");
			logger.error(elog.toStringException(e));
			throw new RuntimeException(e);
		}
		return obj;
	}
	
	
	public static JSONObject getLabelJson(int crisisID, DocumentLabel label) {
		try {
			if (label instanceof NominalLabelBC) {
				NominalLabelBC l = (NominalLabelBC) label;
				ModelFamilyEC family = getModelFamily(crisisID, l.getAttributeID());

				JSONObject obj = new JSONObject();
				obj.put("source_id", l.getSourceID());
				obj.put("attribute_code", family.getNominalAttribute().getCode());
				obj.put("attribute_name", family.getNominalAttribute().getName());
				obj.put("attribute_description", family.getNominalAttribute().getDescription());
				NominalLabelEC lEC = family.getNominalAttribute().getNominalLabel(l.getNominalLabelID());
				obj.put("label_code", lEC.getNominalLabelCode());
				obj.put("label_name", lEC.getName());
				obj.put("label_description", lEC.getDescription());
				obj.put("confidence", l.getConfidence());
				obj.put("from_human", l.isHumanLabel());
				return obj;
			}
		} catch (JSONException e) {
			logger.error("Error in creating json object from: " + label);
			logger.error(elog.toStringException(e));
			throw new RuntimeException(e);
		}
		logger.error("Unsupported label type: " + label.getClass().getSimpleName());
		throw new RuntimeException("Unsupported label type: " + label.getClass().getSimpleName());
	}

	private static int getCrisisID(String crisisCode) {
		if ((System.currentTimeMillis() - lastModelInfoUpdate) > 300000
				|| (!activeCrisisIDs.containsKey(crisisCode) && (System
						.currentTimeMillis() - lastModelInfoUpdate) > 10000)) {
			updateModelInfo();
		}

		if (!activeCrisisIDs.containsKey(crisisCode))
			throw new RuntimeException("Crisis code has not been defined: " + crisisCode);

		return activeCrisisIDs.get(crisisCode);
	}

	private static ModelFamilyEC getModelFamily(int crisisID, int attributeID) {
		if ((System.currentTimeMillis() - lastModelInfoUpdate) > 300000
				|| ((!activeModelFamiliesByID.containsKey(crisisID) 
						|| !activeModelFamiliesByID.get(crisisID).containsKey(attributeID)) 
						&& (System.currentTimeMillis() - lastModelInfoUpdate) > 10000)) {
			updateModelInfo();
		}

		if (!activeModelFamiliesByID.containsKey(crisisID)
				|| !activeModelFamiliesByID.get(crisisID).containsKey(attributeID))
			throw new RuntimeException("ModelInfo is missing for crisis " + crisisID + " and attribute " + attributeID);

		return activeModelFamiliesByID.get(crisisID).get(attributeID);
	}

	private static ModelFamilyEC getModelFamily(int crisisID, String attributeCode) {
		if ((System.currentTimeMillis() - lastModelInfoUpdate) > 300000
				|| ((!activeModelFamiliesByCode.containsKey(crisisID) 
						|| !activeModelFamiliesByCode.get(crisisID).containsKey(attributeCode)) 
						&& (System.currentTimeMillis() - lastModelInfoUpdate) > 10000)) {
			updateModelInfo();
		}

		if (!activeModelFamiliesByCode.containsKey(crisisID)
				|| !activeModelFamiliesByCode.get(crisisID).containsKey(attributeCode))
			throw new RuntimeException(
					"ModelInfo is missing for crisis " + crisisID + " and attribute " + attributeCode);

		return activeModelFamiliesByCode.get(crisisID).get(attributeCode);
	}

	private static void updateModelInfo() {
		activeModelFamiliesByID.clear();
		activeModelFamiliesByCode.clear();
		activeCrisisIDs.clear();

		activeCrisisIDs = DataStore.getCrisisIDs();

		ArrayList<ModelFamilyEC> families = DataStore.getActiveModels();
		for (ModelFamilyEC family : families) {

			int crisisID = family.getCrisisID();
			int attributeID = family.getNominalAttribute().getNominalAttributeID();
			String attributeCode = family.getNominalAttribute().getCode();

			if (!activeModelFamiliesByID.containsKey(crisisID)) {
				activeModelFamiliesByID.put(crisisID, new HashMap<Integer, ModelFamilyEC>());
				activeModelFamiliesByCode.put(crisisID, new HashMap<String, ModelFamilyEC>());
			}
			activeModelFamiliesByID.get(crisisID).put(attributeID, family);
			activeModelFamiliesByCode.get(crisisID).put(attributeCode, family);
		}

		lastModelInfoUpdate = System.currentTimeMillis();
	}
}
