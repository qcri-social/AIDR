package qa.qcri.aidr.task.util;


import org.apache.log4j.Logger;


import qa.qcri.aidr.common.logging.ErrorLog;

public class TaskManagerEntityMapper {

	private static Logger logger = Logger.getLogger(TaskManagerEntityMapper.class);
	private static ErrorLog elog = new ErrorLog();

	public TaskManagerEntityMapper() {}
	



	
/*

	public TaskAssignment toTaskAssignment(TaskAssignmentDTO t) {
		if (t != null) {
			TaskAssignment taskAssignment  = new TaskAssignment(t.getDocumentID(), t.getUserID(), t.getAssignedAt());
			return taskAssignment;
		}
		return null;
	}

	public TaskAssignmentDTO toTaskAssignmentDTO(TaskAssignment t) {
		if (t != null) {
			TaskAssignmentDTO taskAssignmentDTO  = new TaskAssignmentDTO(t.getDocumentID(), t.getUserID(), t.getAssignedAt());
			return taskAssignmentDTO;
		}
		return null;
	}
	

	
	public DocumentNominalLabel toDocumentNominalLabel(DocumentNominalLabelDTO doc) {
		if (doc != null) {
			DocumentNominalLabel nominalDoc = new DocumentNominalLabel(doc.getDocumentID(), doc.getNominalLabelID(), doc.getUserID());
			return nominalDoc;
		}
		return null;
	}
	
	public DocumentNominalLabelDTO toDocumentNominalLabelDTO(DocumentNominalLabel doc) {
		if (doc != null) {
			DocumentNominalLabelDTO nominalDocDTO = new DocumentNominalLabelDTO(doc.getDocumentID(), doc.getNominalLabelID(), doc.getUserID());
			return nominalDocDTO;
		}
		return null;
	}
	
	*/
}
