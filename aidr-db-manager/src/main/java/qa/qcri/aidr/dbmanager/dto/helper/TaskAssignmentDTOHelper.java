package qa.qcri.aidr.dbmanager.dto.helper;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.dbmanager.dto.TaskAssignmentDTO;



public class TaskAssignmentDTOHelper {

	public static TaskAssignment toTaskAssignment(TaskAssignmentDTO t) {
		if (t != null) {
			TaskAssignment taskAssignment = new TaskAssignment();
			taskAssignment.setDocumentID(t.getDocumentID());
			taskAssignment.setUserID(t.getUserID());
			taskAssignment.setAssignedAt(t.getAssignedAt());

			return taskAssignment;
		}
		return null;

	}
	
	public static TaskAssignmentDTO toTaskAssignmentDTO(TaskAssignment t) {
		if (t != null) {
			TaskAssignmentDTO taskAssignmentDTO = new TaskAssignmentDTO();
			taskAssignmentDTO.setDocumentID(t.getDocumentID());
			taskAssignmentDTO.setUserID(t.getUserID());
			taskAssignmentDTO.setAssignedAt(t.getAssignedAt());

			return taskAssignmentDTO;
		}
		return null;
	}
	
	public static List<TaskAssignment> toTaskAssignmentList(List<TaskAssignmentDTO> list) {
		if (list != null) {
			List<TaskAssignment> taskAssignmentList = new ArrayList<TaskAssignment>();
			for (TaskAssignmentDTO t: list) {
				taskAssignmentList.add(TaskAssignmentDTOHelper.toTaskAssignment(t));
			}
			return taskAssignmentList;
		}
		return null;
	}
	
	public static List<TaskAssignmentDTO> toTaskAssignmentDTOList(List<TaskAssignment> list) {
		if (list != null) {
			List<TaskAssignmentDTO> taskAssignmentDTOList = new ArrayList<TaskAssignmentDTO>();
			for (TaskAssignment t: list) {
				taskAssignmentDTOList.add(TaskAssignmentDTOHelper.toTaskAssignmentDTO(t));
			}
			return taskAssignmentDTOList;
		}
		return null;
	}
}
