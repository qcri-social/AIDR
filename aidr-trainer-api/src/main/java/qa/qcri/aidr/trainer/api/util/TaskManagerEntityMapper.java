package qa.qcri.aidr.trainer.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.map.DeserializationConfig;

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

	@Deprecated
	public <E> E deSerializeList(String jsonString, TypeReference<E> type) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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

	@Deprecated
	public <E> E deSerialize(String jsonString, Class<E> entityType) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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

	@Deprecated
	public <E> String serializeTask(E task) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String jsonString = null;
		try {
			if (task != null) jsonString = mapper.writeValueAsString(task);
		} catch (IOException e) {
			logger.error("JSON serialization exception");
			logger.error(elog.toStringException(e));
		}
		return jsonString;
	}

	/*
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
	*/
}
