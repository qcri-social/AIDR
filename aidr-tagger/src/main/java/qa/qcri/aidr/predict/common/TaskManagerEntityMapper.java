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
			logger.error("JSON deserialization exception", e);
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
}
