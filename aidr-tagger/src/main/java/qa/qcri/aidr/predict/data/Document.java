package qa.qcri.aidr.predict.data;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.json.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qa.qcri.aidr.predict.classification.DocumentLabel;
import qa.qcri.aidr.predict.classification.DocumentLabelFilter;
import qa.qcri.aidr.predict.classification.nominal.NominalLabelBC;
import qa.qcri.aidr.predict.common.Helpers;
import qa.qcri.aidr.predict.dbentities.NominalLabel;
import qa.qcri.aidr.predict.dbentities.TaggerDocument;
import qa.qcri.aidr.predict.featureextraction.DocumentFeature;
import qa.qcri.aidr.predict.featureextraction.FeatureExtractor;
import qa.qcri.aidr.predict.featureextraction.WordSet;

/**
 * Document is an abstract representation of a work item in the processing
 * pipeline. Each process in the pipeline annotates the Document with additional
 * information such as features and class labels.
 * 
 * @author jrogstadius
 */
public abstract class Document implements java.io.Serializable {

	static final long serialVersionUID = 1L;
	public Long crisisID;
	public String crisisCode;
	public JSONObject inputJson;
	//public InetAddress sourceIP;
	public String language = "en";
	public Long documentID;

	public String doctype;

	public ArrayList<DocumentFeature> features = new ArrayList<DocumentFeature>();
	public ArrayList<DocumentLabel> labels = new ArrayList<DocumentLabel>();
	public int humanLabelCount = 0;
	public double valueAsTrainingSample = 0.5;

	// added by koushik: 21/12/2014
	Long userID;

	public Document() {

	}

	public void setDocumentID(Integer documentID) {
		this.documentID = new Long(documentID);
	}

	public void setDocumentID(Long documentID) {
		this.documentID = documentID;
	}

	public Long getDocumentID() {
		return documentID;
	}

	public void setInputJson(JSONObject inputJson) {
		this.inputJson = inputJson;
	}

	public JSONObject getInputJson() {
		return inputJson;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setValueAsTrainingSample(double value) {
		this.valueAsTrainingSample = value;
	}

	public double getValueAsTrainingSample() {
		return valueAsTrainingSample;
	}

	public abstract String getDoctype();

	public abstract void setDoctype(String type);

	public abstract boolean isNovel();

	public void setCrisisID(int crisisID) {
		this.crisisID = new Long(crisisID);
	}

	public void setCrisisID(Long crisisID) {
		this.crisisID = crisisID;
	}

	public Long getCrisisID() {
		return crisisID;
	}

	public void setCrisisCode(String crisisCode) {
		this.crisisCode = crisisCode;
	}

	public String getCrisisCode() {
		return crisisCode;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}


	public void addLabel(DocumentLabel label) {
		labels.add(label);

		if (label.isHumanLabel())
			humanLabelCount++;
	}

	@SuppressWarnings("unchecked")
	public <T extends DocumentLabel> ArrayList<T> getLabels(Class<T> classFilter) {
		ArrayList<T> items = new ArrayList<T>();

		for (DocumentLabel label : labels) {
			if (classFilter.isAssignableFrom(label.getClass()))
				items.add((T) label);
		}
		return items;
	}

	@SuppressWarnings("unchecked")
	public <T extends DocumentLabel> ArrayList<T> getHumanLabels(
			Class<T> classFilter) {
		ArrayList<T> items = new ArrayList<T>();

		for (DocumentLabel label : labels) {
			if (label.isHumanLabel()
					&& classFilter.isAssignableFrom(label.getClass()))
				items.add((T) label);
		}
		return items;
	}

	public boolean hasLabel(DocumentLabelFilter filter) {
		for (DocumentLabel label : labels) {
			if (filter.match(label))
				return true;
		}
		return false;
	}

	public boolean hasHumanLabels() {
		return humanLabelCount > 0;
	}

	@SuppressWarnings("unchecked")
	public <T extends DocumentFeature> ArrayList<T> getFeatures(
			Class<T> classFilter) {
		ArrayList<T> items = new ArrayList<T>();

		for (DocumentFeature feature : features) {
			if (classFilter.isAssignableFrom(feature.getClass()))
				items.add((T) feature);
		}
		return items;
	}

	public void addFeatureSet(DocumentFeature set) {
		features.add(set);
	}

	public static TaggerDocument fromDocumentToTaggerDocument(Document doc) {
		TaggerDocument document = new TaggerDocument();
		if (doc != null) {
			// NOTE: documentID needs to be set separately as Auto Generation ID from DB/Hibernate
			// Now copy the remaining fields
			document.setHasHumanLabels(doc.hasHumanLabels());
			document.setCrisisID(doc.getCrisisID());
			document.setCrisisCode(doc.getCrisisCode());
			document.setReceivedAt(new java.sql.Timestamp(
					java.util.Calendar.getInstance().getTimeInMillis()));
			document.setLanguage(doc.getLanguage());

			document.setDoctype(doc.getClass().getSimpleName().toString());
			if (doc.getInputJson() != null) {
				document.setData(Helpers.escapeJson(doc.getInputJson().toString())); 
			} else {
				document.setData(null);
			}
			if (doc.features != null) {
				document.setWordFeatures(DocumentJSONConverter.getFeaturesJson(WordSet.class, doc));
			}
			document.setGeoFeatures(null);
			document.setValueAsTrainingSample(doc.getValueAsTrainingSample());

			List<NominalLabelBC> labels = doc.getHumanLabels(NominalLabelBC.class);
			if (!labels.isEmpty()) {
				List<NominalLabel> nbList = new ArrayList<NominalLabel>();
				for (NominalLabelBC label : labels) {
					NominalLabel nb = new NominalLabel(label.getNominalLabelID());
					nbList.add(nb);
				}
				document.setNominalLabelCollection(nbList);
			} else {
				document.setNominalLabelCollection(null);
			}

			return document;
		}
		return null;
	}

	public static Document fromTaggerDocumentToDocument(TaggerDocument doc) {
		Document document = new Tweet();

		if (doc != null) {
			document.setDocumentID(doc.getDocumentID());
			document.setCrisisID(doc.getCrisisID());
			document.humanLabelCount = (doc.hasHumanLabels() == false) ? 0 : 1;
			document.setCrisisCode(doc.getCrisisCode());
			document.setLanguage(doc.getLanguage());

			WordSet wordSet = new WordSet();
			String text = doc.getWordFeatures();
			wordSet.addAll(FeatureExtractor.getWordsInStringWithBigrams(text, false));
			document.addFeatureSet(wordSet);

			document.setValueAsTrainingSample(doc.getValueAsTrainingSample());

			List<NominalLabelBC> labels = doc.getHumanLabels(NominalLabelBC.class);
			if (!labels.isEmpty()) {
				for (NominalLabelBC label : labels) {
					document.addLabel(label);
				}
			}

			return document;

		} 
		return null;
	}
}
