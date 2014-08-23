package qa.qcri.aidr.predict.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import qa.qcri.aidr.predict.classification.nominal.NominalLabelBC;
import qa.qcri.aidr.predict.dbentities.TaggerDocument;
import qa.qcri.aidr.predict.dbentities.NominalLabel;
import qa.qcri.aidr.predict.dbentities.TaskAssignment;
import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.data.DocumentJSONConverter;
import qa.qcri.aidr.predict.data.Tweet;
import qa.qcri.aidr.predict.featureextraction.FeatureExtractor;
import qa.qcri.aidr.predict.featureextraction.WordSet;

public class TaskManagerEntityMapper {

	private static Logger logger = Logger.getLogger(TaskManagerEntityMapper.class);
	private static ErrorLog elog = new ErrorLog();
	
	public TaskManagerEntityMapper() {}

	public <E> E deSerializeList(String jsonString, TypeReference<E> type) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		try {
			if (jsonString != null) {
				E docList = mapper.readValue(jsonString, type);
				return docList;
			}	
		} catch (IOException e) {
			logger.error("JSON deserialization exception");
			logger.error(elog.toStringException(e));
			System.err.println("JSON deserialization exception");
			e.printStackTrace();
			
		}
		return null;
	}

	public <E> E deSerialize(String jsonString, Class<E> entityType) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		try {
			if (jsonString != null) {
				E entity = mapper.readValue(jsonString, entityType);
				return entity;
			}	
		} catch (IOException e) {
			logger.error("JSON deserialization exception");
			logger.error(elog.toStringException(e));
			System.err.println("JSON deserialization exception");
			e.printStackTrace();
		}
		return null;
	}

	public <E> String serializeTask(E task) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String jsonString = null;
		try {
			if (task != null) jsonString = mapper.writeValueAsString(task);
		} catch (IOException e) {
			logger.error("JSON deserialization exception");
			logger.error(elog.toStringException(e));
			System.err.println("JSON serialization exception");
			e.printStackTrace();
		}
		return jsonString;
	}

	public TaggerDocument transformDocument(qa.qcri.aidr.task.entities.Document document) {
		TaggerDocument doc = new TaggerDocument();
		if (document != null) {
			try {
				doc.setDocumentID(document.getDocumentID());
				doc.setCrisisID(document.getCrisisID());
				doc.setDoctype(document.getDoctype());
				doc.setData(document.getData());
				doc.setEvaluationSet(document.isEvaluationSet());
				doc.setGeoFeatures(document.getGeoFeatures());
				doc.setLanguage(document.getLanguage());
				doc.setHasHumanLabels(document.isHasHumanLabels());

				doc.setReceivedAt(document.getReceivedAt());
				//doc.setSourceIP(document.getSourceIP());
				doc.setWordFeatures(document.getWordFeatures());
				doc.setValueAsTrainingSample(document.getValueAsTrainingSample());
				doc.setTaskAssignment(transformTaskAssignment(document.getTaskAssignment()));

				doc.setNominalLabelCollection(transformNominalLabelCollection(document.getNominalLabelCollection()));
				return doc;
			} catch (Exception e) {
				logger.error("Error in transformation from task-manager Document to tagger Document: " + document);
				logger.error(elog.toStringException(e));
				System.err.println("Error in transformation from task-manager Document to tagger Document");
				e.printStackTrace();
			}
		} 
		return null;
	}

	public qa.qcri.aidr.task.entities.Document reverseTransformDocument(TaggerDocument document) {
		qa.qcri.aidr.task.entities.Document doc = new qa.qcri.aidr.task.entities.Document();
		if (document != null) {
			try {
				doc.setDocumentID(document.getDocumentID());
				doc.setCrisisID(document.getCrisisID());
				doc.setDoctype(document.getDoctype());
				doc.setData(document.getData());
				doc.setEvaluationSet(document.isEvaluationSet());
				doc.setGeoFeatures(document.getGeoFeatures());
				doc.setLanguage(document.getLanguage());
				doc.setHasHumanLabels(document.isHasHumanLabels());

				doc.setReceivedAt(document.getReceivedAt());
				//doc.setSourceIP(document.getSourceIPAsLong());
				doc.setWordFeatures(document.getWordFeatures());
				doc.setValueAsTrainingSample(document.getValueAsTrainingSample());
				doc.setTaskAssignment(reverseTransformTaskAssignment(document.getTaskAssignment()));

				doc.setNominalLabelCollection(reverseTransformNominalLabelCollection(document.getNominalLabelCollection()));
				return doc;
			} catch (Exception e) {
				logger.error("Error in transformation from tagger Document to task-manager Document: " + document);
				logger.error(elog.toStringException(e));
				System.err.println("Error in transformation from tagger Document to task-manager Document");
				e.printStackTrace();
			}
		} 
		return null;
	}


	public TaskAssignment transformTaskAssignment(qa.qcri.aidr.task.entities.TaskAssignment t) {
		if (t != null) {
			try {
				TaskAssignment taskAssignment  = new TaskAssignment(t.getDocumentID(), t.getUserID(), t.getAssignedAt());
				return taskAssignment;
			} catch (Exception e) {
				logger.error("error in transforming task-manager entity to local task-assignment entity: " + t);
				logger.error(elog.toStringException(e));
				e.printStackTrace();
			}
		}
		return null;
	}

	public qa.qcri.aidr.task.entities.TaskAssignment reverseTransformTaskAssignment(TaskAssignment t) {
		if (t != null) {
			try {
				qa.qcri.aidr.task.entities.TaskAssignment taskAssignment  = new qa.qcri.aidr.task.entities.TaskAssignment(t.getDocumentID(), t.getUserID(), t.getAssignedAt());
				return taskAssignment;
			} catch (Exception e) {
				logger.error("error in transforming local task-assignment entity to task-manager's task-assignment entity: " + t);
				logger.error(elog.toStringException(e));
				e.printStackTrace();
			}
		}
		return null;
	}

	public Collection<NominalLabel> transformNominalLabelCollection(Collection<qa.qcri.aidr.task.entities.NominalLabel> list) {
		if (list != null) {
			try {
				Collection<NominalLabel> nominalLabelList = new ArrayList<NominalLabel>();
				for (qa.qcri.aidr.task.entities.NominalLabel t: list) {
					if (t != null) {
						NominalLabel nominalLabel  = new NominalLabel(t.getNominalLabelID(), t.getNominalLabelCode(), t.getName(), t.getDescription());
						nominalLabelList.add(nominalLabel);
					}
				}
				return nominalLabelList;
			} catch (Exception e) {
				logger.error("error in transforming task-manager entity to local nominal_label collection");
				logger.error(elog.toStringException(e));
				e.printStackTrace();
			}
		}
		return null;
	}

	public Collection<qa.qcri.aidr.task.entities.NominalLabel> reverseTransformNominalLabelCollection(Collection<NominalLabel> list) {
		if (list != null) {
			try {
				Collection<qa.qcri.aidr.task.entities.NominalLabel> nominalLabelList = new ArrayList<qa.qcri.aidr.task.entities.NominalLabel>();
				for (NominalLabel t: list) {
					if (t != null) {
						qa.qcri.aidr.task.entities.NominalLabel nominalLabel = new qa.qcri.aidr.task.entities.NominalLabel(t.getNominalLabelID(), t.getNominalLabelCode(), t.getName(), t.getDescription());
						nominalLabelList.add(nominalLabel);
					}
				}
				return nominalLabelList;
			} catch (Exception e) {
				logger.error("error in transforming local nominal_label entity to task-manager's");
				logger.error(elog.toStringException(e));
				e.printStackTrace();
			}
		}
		return null;
	}

	public TaggerDocument fromDocumentToTaggerDocument(Document doc) {
		TaggerDocument document = new TaggerDocument();
		
		// NOTE: documentID needs to be set separately as Auto Generation ID from DB/Hibernate
		try {
			// Now copy the remaining fields
			document.setHasHumanLabels(doc.hasHumanLabels());
			document.setCrisisID(doc.getCrisisID());
			document.setCrisisCode(doc.getCrisisCode());
			document.setReceivedAt(new java.sql.Timestamp(
					java.util.Calendar.getInstance().getTimeInMillis()));
			document.setLanguage(doc.getLanguage());
			//document.setSourceIP(doc.getSourceIP());

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
			
		} catch (Exception e) {
			logger.error("error in transforming document entity to tagger document entity: " + doc);
			logger.error(elog.toStringException(e));
			e.printStackTrace();
			return null;
		}
	}

	public Document fromTaggerDocumentToDocument(TaggerDocument doc) {
		Document document = new Tweet();
		
		try {
			document.setDocumentID(doc.getDocumentID());
			document.setCrisisID(doc.getCrisisID());
			document.humanLabelCount = (doc.hasHumanLabels() == false) ? 0 : 1;
			document.setCrisisCode(doc.getCrisisCode());
			document.setLanguage(doc.getLanguage());
			//document.setSourceIP(doc.getSourceIP());

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
			
		} catch (Exception e) {
			logger.error("error in transforming tagger document entity to document entity: " + doc);
			logger.error(elog.toStringException(e));
			e.printStackTrace();
			return null;
		}
	}
	
	

	public static void main(String args[]) {
		TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
		qa.qcri.aidr.task.entities.Document doc = new qa.qcri.aidr.task.entities.Document(12345678L, false);
		qa.qcri.aidr.task.entities.Document doc2 = new qa.qcri.aidr.task.entities.Document(12345679L, true);

		doc.setCrisisID(999L);
		doc.setValueAsTrainingSample(0.8);
		//doc.setSourceIP(77334455L);

		doc2.setCrisisID(1000L);
		doc2.setValueAsTrainingSample(1.0);
		//doc2.setSourceIP(555555L);

		String jsonString = mapper.serializeTask(doc);
		TaggerDocument newDoc1 = mapper.transformDocument(doc);
		System.out.println("Serialized json string: " + jsonString); 
		System.out.println("New document 1 = " + newDoc1.getDocumentID());

		TaggerDocument newDoc2 = mapper.deSerialize(jsonString, TaggerDocument.class);
		System.out.println("Deserialized document ID = " + newDoc2.getDocumentID());

		List<qa.qcri.aidr.task.entities.Document> docList = new ArrayList<qa.qcri.aidr.task.entities.Document>();
		docList.add(doc);
		docList.add(doc2);
		String jsonString2 = mapper.serializeTask(docList);
		System.out.println("Serialized List: " + jsonString2);

		List<TaggerDocument> newDocList = mapper.deSerializeList(jsonString2, new TypeReference<List<TaggerDocument>>() {});
		System.out.println("\nList deserialization: ");
		for (TaggerDocument d: newDocList) {
			System.out.println("Deserialized document ID = " + d.getDocumentID());
		}

		TaggerDocument doc3 = new TaggerDocument(1111111L, false);
		doc3.setCrisisID(111L);
		doc3.setValueAsTrainingSample(0.8);
		//doc3.setSourceIP(77334455L);

		qa.qcri.aidr.task.entities.Document newDoc3 = mapper.reverseTransformDocument(doc3);
		String jsonString3 = mapper.serializeTask(newDoc3);
		System.out.println("Serialized TaggerDocument: " + jsonString3);

		qa.qcri.aidr.task.entities.Document newDoc4 = mapper.deSerialize(jsonString3, qa.qcri.aidr.task.entities.Document.class);
		System.out.println("Deserialized document ID: " + newDoc4.getDocumentID());
	}
}
