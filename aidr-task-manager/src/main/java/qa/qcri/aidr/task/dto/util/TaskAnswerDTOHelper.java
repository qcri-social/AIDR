package qa.qcri.aidr.task.dto.util;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.task.dto.TaskAnswerDTO;
import qa.qcri.aidr.task.entities.TaskAnswer;

public class TaskAnswerDTOHelper {

	public static TaskAnswer toTaskAnswer(TaskAnswerDTO dto) {
		if (dto != null) {
			TaskAnswer t = new TaskAnswer(dto.getDocumentID(), dto.getUserID(), dto.getAnswer());
			return t;
		}
		return null;
	}

	public static TaskAnswerDTO toTaskAnswerDTO(TaskAnswer t) {
		if (t != null) {
			TaskAnswerDTO dto = new TaskAnswerDTO(t.getDocumentID(), t.getUserID(), t.getAnswer());
			return dto;
		}
		return null;
	}

	public static List<TaskAnswer> toTaskAnswerList(List<TaskAnswerDTO> list) {
		if (list != null) {
			List<TaskAnswer> tList = new ArrayList<TaskAnswer>();
			for (TaskAnswerDTO dto: list) {
				TaskAnswer t = new TaskAnswer(dto.getDocumentID(), dto.getUserID(), dto.getAnswer());
				tList.add(t);
			}
			return tList;
		}
		return null;
	}

	public static List<TaskAnswerDTO> toTaskAnswerDTO(List<TaskAnswer> list) {
		if (list != null) {
			List<TaskAnswerDTO> dtoList = new ArrayList<TaskAnswerDTO>();
			for (TaskAnswer t: list) {
				TaskAnswerDTO dto = new TaskAnswerDTO(t.getDocumentID(), t.getUserID(), t.getAnswer());
				dtoList.add(dto);
			}
			return dtoList;
		}
		return null;
	}
}
