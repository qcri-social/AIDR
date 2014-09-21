package qa.qcri.aidr.trainer.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.trainer.api.entity.Document;
import qa.qcri.aidr.trainer.api.entity.TaskAssignment;
import qa.qcri.aidr.trainer.api.entity.DocumentNominalLabel;
import qa.qcri.aidr.trainer.api.entity.TaskAnswer;
import qa.qcri.aidr.trainer.api.entity.NominalLabel;

public class TaskManagerEntityMapper {

	private static Logger logger = Logger.getLogger(TaskManagerEntityMapper.class);
	private static ErrorLog elog = new ErrorLog();
			
	public TaskManagerEntityMapper() {}

	public <E> E deSerializeList(String jsonString, TypeReference<E> type) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (jsonString != null) {
				E docList = mapper.readValue(jsonString, type);
				return docList;
			}	
		} catch (Exception e) {
			logger.error("JSON deserialization exception");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	public <E> E deSerialize(String jsonString, Class<E> entityType) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (jsonString != null) {
				System.out.println("received json string for deserialization: " + jsonString);
				E entity = mapper.readValue(jsonString, entityType);
				System.out.println("entity: " + entity);
				return entity;
			}	
		} catch (Exception e) {
			logger.error("JSON deserialization exception");
			logger.error(elog.toStringException(e));
		}
		return null;
	}

	public <E> String serializeTask(E task) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			if (task != null) jsonString = mapper.writeValueAsString(task);
		} catch (IOException e) {
			logger.error("JSON serialization exception");
			logger.error(elog.toStringException(e));
		}
		return jsonString;
	}

	public Document transformDocument(qa.qcri.aidr.task.entities.Document document) {
		Document doc = new Document();
		if (document != null) {
			doc.setDocumentID(document.getDocumentID());
			doc.setCrisisID(document.getCrisisID());
			doc.setDoctype(document.getDoctype());
			doc.setData(document.getData());
			doc.setIsEvaluationSet(document.getIsEvaluationSet());
			doc.setGeoFeatures(document.getGeoFeatures());
			doc.setLanguage(document.getLanguage());
			doc.setHasHumanLabels(document.getHasHumanLabels());

			doc.setReceivedAt(document.getReceivedAt());
			//doc.setSourceIP(document.getSourceIP().longValue());
			doc.setWordFeatures(document.getWordFeatures());
			doc.setValueAsTrainingSample(document.getValueAsTrainingSample());
			doc.setTaskAssignment(transformTaskAssignment(document.getTaskAssignment()));

			//doc.setNominalLabelCollection(transformNominalLabelCollection(document.getNominalLabelCollection()));
			return doc;
		} 
		return null;
	}

	public qa.qcri.aidr.task.entities.Document reverseTransformDocument(Document document) {
		qa.qcri.aidr.task.entities.Document doc = new qa.qcri.aidr.task.entities.Document();
		if (document != null) {
			doc.setDocumentID(document.getDocumentID());
			doc.setCrisisID(document.getCrisisID());
			doc.setDoctype(document.getDoctype());
			doc.setData(document.getData());
			doc.setIsEvaluationSet(document.getIsEvaluationSet());
			doc.setGeoFeatures(document.getGeoFeatures());
			doc.setLanguage(document.getLanguage());
			doc.setHasHumanLabels(document.getHasHumanLabels());

			doc.setReceivedAt(document.getReceivedAt());
			//doc.setSourceIP(document.getSourceIP().intValue());
			doc.setWordFeatures(document.getWordFeatures());
			doc.setValueAsTrainingSample(document.getValueAsTrainingSample());
			doc.setTaskAssignment(reverseTransformTaskAssignment(document.getTaskAssignment()));

			//doc.setNominalLabelCollection(reverseTransformNominalLabelCollection(document.getNominalLabelCollection()));
			return doc;
		} 
		return null;
	}


	public TaskAssignment transformTaskAssignment(qa.qcri.aidr.task.entities.TaskAssignment t) {
		if (t != null) {
			TaskAssignment taskAssignment  = new TaskAssignment(t.getDocumentID(), t.getUserID(), t.getAssignedAt());
			return taskAssignment;
		}
		return null;
	}

	public qa.qcri.aidr.task.entities.TaskAssignment reverseTransformTaskAssignment(TaskAssignment t) {
		if (t != null) {
			qa.qcri.aidr.task.entities.TaskAssignment taskAssignment  = new qa.qcri.aidr.task.entities.TaskAssignment(t.getDocumentID(), t.getUserID(), t.getAssignedAt());
			return taskAssignment;
		}
		return null;
	}

	public Collection<NominalLabel> transformNominalLabelCollection(Collection<qa.qcri.aidr.task.entities.NominalLabel> list) {
		if (list != null) {
			Collection<NominalLabel> nominalLabelList = new ArrayList<NominalLabel>();
			for (qa.qcri.aidr.task.entities.NominalLabel t: list) {
				if (t != null) {
					NominalLabel nominalLabel  = new NominalLabel(t.getNominalLabelID().longValue(), t.getNominalLabelCode(), t.getName(), t.getDescription());
					nominalLabelList.add(nominalLabel);
				}
			}
			return nominalLabelList;
		}
		return null;
	}

	public Collection<qa.qcri.aidr.task.entities.NominalLabel> reverseTransformNominalLabelCollection(Collection<NominalLabel> list) {
		if (list != null) {
			Collection<qa.qcri.aidr.task.entities.NominalLabel> nominalLabelList = new ArrayList<qa.qcri.aidr.task.entities.NominalLabel>();
			for (NominalLabel t: list) {
				if (t != null) {
					qa.qcri.aidr.task.entities.NominalLabel nominalLabel = new qa.qcri.aidr.task.entities.NominalLabel(t.getNominalLabelID().intValue(), t.getNominalLabelCode(), t.getName(), t.getDescription());
					nominalLabelList.add(nominalLabel);
				}
			}
			return nominalLabelList;
		}
		return null;
	}
	
	public TaskAnswer transformTaskAnswer(qa.qcri.aidr.task.entities.TaskAnswer t) {
		if (t != null) {
			TaskAnswer taskAnswer = new TaskAnswer(t.getDocumentID(), t.getUserID(), t.getAnswer());
			return taskAnswer;
		}
		return null;
	}
	
	public qa.qcri.aidr.task.entities.TaskAnswer reverseTransformTaskAnswer(TaskAnswer t) {
		if (t != null) {
			qa.qcri.aidr.task.entities.TaskAnswer taskAnswer = new qa.qcri.aidr.task.entities.TaskAnswer(t.getDocumentID(), t.getUserID(), t.getAnswer());
			return taskAnswer;
		}
		return null;
	}
	
	public DocumentNominalLabel transformDocumentNominalLabel(qa.qcri.aidr.task.entities.DocumentNominalLabel doc) {
		if (doc != null) {
			DocumentNominalLabel nominalDoc = new DocumentNominalLabel(doc.getDocumentID(), doc.getNominalLabelID(), doc.getUserID());
			return nominalDoc;
		}
		return null;
	}
	
	public qa.qcri.aidr.task.entities.DocumentNominalLabel reverseTransformDocumentNominalLabel(DocumentNominalLabel doc) {
		if (doc != null) {
			qa.qcri.aidr.task.entities.DocumentNominalLabel nominalDoc = new qa.qcri.aidr.task.entities.DocumentNominalLabel(doc.getDocumentID(), doc.getNominalLabelID(), doc.getUserID());
			return nominalDoc;
		}
		return null;
	}
	

	public static void main(String args[]) {
		TaskManagerEntityMapper mapper = new TaskManagerEntityMapper();
		qa.qcri.aidr.task.entities.Document doc = new qa.qcri.aidr.task.entities.Document(12345678L, false);
		qa.qcri.aidr.task.entities.Document doc2 = new qa.qcri.aidr.task.entities.Document(12345679L, true);
		String jsonString = mapper.serializeTask(doc);
		System.out.println("serialized document: " + jsonString);
		
		Document newDoc1 = mapper.transformDocument(doc);
		System.out.println("New document 1 = " + newDoc1.getDocumentID());

		Document newDoc2 = mapper.transformDocument(mapper.deSerialize(jsonString, qa.qcri.aidr.task.entities.Document.class));
		System.out.println("New document 2 = " + newDoc2.getDocumentID());

		try {
		Document newDoc3 = mapper.deSerialize(jsonString, Document.class);
		System.out.println("New document 3 = " + newDoc3.getDocumentID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<qa.qcri.aidr.task.entities.Document> docList = new ArrayList<qa.qcri.aidr.task.entities.Document>();
		docList.add(doc);
		docList.add(doc2);
		String jsonString2 = mapper.serializeTask(docList);

		List<Document> newDocList = mapper.deSerializeList(jsonString2, new TypeReference<List<Document>>() {});
		for (Document d: newDocList) {
			System.out.println("New document = " + d.getDocumentID());
		}
		
		qa.qcri.aidr.task.entities.NominalLabel nb = new qa.qcri.aidr.task.entities.NominalLabel(1978, "345", "testNominalLabel", "for testing serialization");
		jsonString = mapper.serializeTask(nb);
		System.out.println("serialized NominalLabel: " + jsonString);
		NominalLabel nb1 = mapper.deSerialize(jsonString, NominalLabel.class);
		System.out.println("Deserialized NominalLabel: " + nb1.getNominalLabelID() + ", " + nb1.getNominalLabelCode());
	}
}
