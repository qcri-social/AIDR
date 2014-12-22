package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAssignmentDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskAssignmentResourceFacade;
import qa.qcri.aidr.dbmanager.entities.task.TaskAssignment;

/**
 * 
 * @author Koushik
 *
 */
@Stateless(name="TaskAssignmentResourceFacadeImp")
public class TaskAssignmentResourceFacadeImp extends CoreDBServiceFacadeImp<TaskAssignment, Long> implements TaskAssignmentResourceFacade {

	private Logger logger = Logger.getLogger("db-manager-log");
	private ErrorLog elog = new ErrorLog();

	public TaskAssignmentResourceFacadeImp() {
		super(TaskAssignment.class);
	}


	@Override
	public int insertTaskAssignment(List<DocumentDTO> taskList, Long userID) {
		// hard code, will create user service
		System.out.println("Going to insert/create task list of size = " + taskList.size() + ", for userID: " + userID);
		try {
			for (Iterator<DocumentDTO> it = taskList.iterator(); it.hasNext();){
				DocumentDTO tb = (DocumentDTO) it.next();
				List<TaskAssignment> taskAssignments = getAllByCriteria(Restrictions.eq("id.documentId", tb.getDocumentID()));
				if(null == taskAssignments || taskAssignments.size()== 0){
					// No assigned tasks currently for user
					TaskAssignment taskAssignment = new TaskAssignment(tb.getDocumentID(), userID, new Date());
					save(taskAssignment);
					return 1;
				}
			}
		} catch (Exception e) {
			logger.error("Error in insert operation!");
			logger.error(elog.toStringException(e));
			e.printStackTrace();
		}
		return 0;


	}

	@Override
	public int insertOneTaskAssignment(Long documentID, Long userID) {
		try {
			List<TaskAssignment> taskAssignments = getAllByCriteria(Restrictions.eq("id.documentId", documentID));
			if(null == taskAssignments || taskAssignments.size()== 0){
				TaskAssignment taskAssignment = new TaskAssignment(documentID, userID, new Date());
				save(taskAssignment);
				return 1;
			}
		} catch (Exception e) {
			logger.error("Error in insert operation!");
			logger.error(elog.toStringException(e));
		}
		return 0;
	}

	@Override
	public int undoTaskAssignment(List<DocumentDTO> taskList, Long userID) {
		if (taskList != null) {
			try {
				int deleteCount = 0;
				for (Iterator<DocumentDTO> it = taskList.iterator(); it.hasNext();){
					DocumentDTO tb = (DocumentDTO) it.next();
					TaskAssignment taskAssignment = (TaskAssignment) getTaskAssignment(tb.getDocumentID(), userID);
					delete(taskAssignment);
					++deleteCount;
				}
				return deleteCount;
			} catch (Exception e) {
				logger.error("Error in undo operation!");
				logger.error(elog.toStringException(e));
			}
		}
		return 0;
	}

	@Override
	public int undoTaskAssignment(Map<Long, Long> taskMap) {
		if (taskMap != null) {
			try {
				int deleteCount = 0;
				Iterator entries = taskMap.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry entry = (Map.Entry) entries.next();
					Long key = (Long)entry.getKey();
					Long value = (Long)entry.getValue();
					TaskAssignment taskAssignment = (TaskAssignment) getTaskAssignment(key, value);
					delete(taskAssignment);
					++deleteCount;
				}
				return deleteCount;
			} catch (Exception e) {
				logger.error("Error in undo operation!");
				logger.error(elog.toStringException(e));
			}
		}
		return 0;
	}

	@Override
	public int undoTaskAssignment(Long documentID, Long userID) {
		try {
			TaskAssignment taskAssignment = (TaskAssignment) getTaskAssignment(documentID, userID);
			if(taskAssignment!=null){
				delete(taskAssignment);
				return 1;
			}
		} catch (Exception e) {
			logger.error("Error in undo operation!");
			logger.error(elog.toStringException(e));
		}
		return 0;
	}

	@Override
	public void undoTaskAssignmentByTimer() {
		try {
			// System.out.println("undoTaskAssignmentByTimer is called");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, -12);
			String calDate = dateFormat.format(cal.getTime());
			List<TaskAssignment> taskAssignments = getAllByCriteria(Restrictions.le("assignedAt",dateFormat.parse(calDate)));
			// System.out.println("undoTaskAssignmentByTimer size : " + taskAssignments);
			if (taskAssignments != null) {
				for (Iterator it =taskAssignments.iterator(); it.hasNext();){
					TaskAssignment taskAssignment = (TaskAssignment) it.next();
					undoTaskAssignment(taskAssignment.getId().getDocumentId(), taskAssignment.getId().getUserId());
				}
			}
		} catch (Exception e) {
			logger.error(elog.toStringException(e));
		}

	}

	private TaskAssignment getTaskAssignment(Long documentID, Long userID) {
		Map<String, Long> attMap = new HashMap<String, Long>();
		attMap.put("id.documentId", documentID);
		attMap.put("id.userId", userID);
		try {
			TaskAssignment t = getByCriteria(Restrictions.allEq(attMap));
			return t;
		} catch (Exception e) {
			logger.error("Error in find operation: documentID = " + documentID + ", userID = " + userID);
			logger.error(elog.toStringException(e));
			return null;
		}
	}
	
	@Override
	public TaskAssignmentDTO findTaskAssignment(Long documentID, Long userID) {
		Map<String, Long> attMap = new HashMap<String, Long>();
		attMap.put("id.documentId", documentID);
		attMap.put("id.userId", userID);
		try {
			TaskAssignment t = getByCriteria(Restrictions.allEq(attMap));
			return t != null ? new TaskAssignmentDTO(t) : null;
		} catch (Exception e) {
			logger.error("Error in find operation: documentID = " + documentID + ", userID = " + userID);
			logger.error(elog.toStringException(e));
			return null;
		}
	}

	@Override
	public List<TaskAssignmentDTO> findTaskAssignmentByID(Long documentID) {
		try {
			List<TaskAssignment> list = getAllByCriteria(Restrictions.eq("id.documentId", documentID));  
			if (list != null) {
				List<TaskAssignmentDTO> dtoList = new ArrayList<TaskAssignmentDTO>();
				for (TaskAssignment t: list) {
					dtoList.add(new TaskAssignmentDTO(t));
				}
				return dtoList;
			}
		} catch (Exception e) {
			logger.error("Error in find operation: documentID = " + documentID);
			logger.error(elog.toStringException(e));
			System.out.println("Error in find operation: documentID = " + documentID);
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public Integer getPendingTaskCount(Long userID) {
		try {
			List<TaskAssignment> taskAssignments = getAllByCriteria(Restrictions.eq("id.userId",userID));
			logger.info("pending for userID " + userID + "tasks = " + (taskAssignments != null ? taskAssignments.size() : "null"));
			return (taskAssignments != null ? taskAssignments.size() : 0);
		} catch (Exception e) {
			logger.error("Error in find operation: userID = " + userID);
			logger.error(elog.toStringException(e));
		}
		return -1;
	}

}