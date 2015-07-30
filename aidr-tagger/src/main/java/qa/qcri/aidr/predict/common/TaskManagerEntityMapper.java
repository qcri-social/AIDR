package qa.qcri.aidr.predict.common;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskManagerEntityMapper {

	private static Logger logger = Logger.getLogger(TaskManagerEntityMapper.class);
	
	public TaskManagerEntityMapper() {}

	@Deprecated
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
			System.err.println("JSON deserialization exception");
			e.printStackTrace();
			
		}
		return null;
	}

	@Deprecated
	public <E> E deSerialize(String jsonString, Class<E> entityType) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		try {
			if (jsonString != null) {
				E entity = mapper.readValue(jsonString, entityType);
				return entity;
			}	
		} catch (IOException e) {
			logger.error("JSON deserialization exception", e);
		}
		return null;
	}

	@Deprecated
	public <E> String serializeTask(E task) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String jsonString = null;
		try {
			if (task != null) jsonString = mapper.writeValueAsString(task);
		} catch (IOException e) {
			logger.error("JSON deserialization exception", e);
		}
		return jsonString;
	}





	
	
/*
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
*/
}
