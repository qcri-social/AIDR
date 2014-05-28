package qa.qcri.aidr.task.ejb.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.task.ejb.TaskAssignmentService;
import qa.qcri.aidr.task.entities.Document;
import qa.qcri.aidr.task.entities.TaskAssignment;


@Stateless(name="TaskAssignmentServiceBean")
public class TaskAssignmentServiceBean extends AbstractTaskManagerServiceBean<TaskAssignment, Long> implements TaskAssignmentService {

	public TaskAssignmentServiceBean() {
		super(TaskAssignment.class);
	}

	@Override
	public int insertTaskAssignment(List<Document> taskList, Long userID) {
		// hard code, will create user service
		try {
			for (Iterator it = taskList.iterator(); it.hasNext();){
				Document tb = (Document) it.next();
				List<TaskAssignment> taskAssignments = findTaskAssignmentByID(tb.getDocumentID());
				if(taskAssignments.size()== 0){
					// No assigned tasks currently for user
					TaskAssignment taskAssignment = new TaskAssignment(tb.getDocumentID(), userID, new Date());
					save(taskAssignment);
					return 1;
				}
			}
		} catch (Exception e) {
			System.err.println("[insertTaskAssignment] Error in insert operation!");
			e.printStackTrace();
		}
		return 0;


	}

	@Override
	public int insertOneTaskAssignment(Long documentID, Long userID) {
		try {
			List<TaskAssignment> taskAssignments = findTaskAssignmentByID(documentID);
			if(taskAssignments.size()== 0){
				TaskAssignment taskAssignment = new TaskAssignment(documentID, userID, new Date());
				save(taskAssignment);
				return 1;
			}
		} catch (Exception e) {
			System.err.println("[insertOneTaskAssignment] Error in insert operation!");
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int undoTaskAssignment(List<Document> taskList, Long userID) {
		try {
			for (Iterator it = taskList.iterator(); it.hasNext();){
				Document tb = (Document) it.next();
				TaskAssignment taskAssignment = (TaskAssignment)findTaskAssignment(tb.getDocumentID(), userID);
				delete(taskAssignment);
				return 1;
			}
		} catch (Exception e) {
			System.err.println("[undoTaskAssignment] Error in undo operation!");
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int undoTaskAssignment(Map<Long, Long> taskMap) {
		try {
			Iterator entries = taskMap.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				Long key = (Long)entry.getKey();
				Long value = (Long)entry.getValue();
				TaskAssignment taskAssignment = (TaskAssignment)findTaskAssignment(key, value);
				delete(taskAssignment);
				return 1;
			}
		} catch (Exception e) {
			System.err.println("[undoTaskAssignment] Error in undo operation!");
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int undoTaskAssignment(Long documentID, Long userID) {
		try {
			TaskAssignment taskAssignment = (TaskAssignment)findTaskAssignment(documentID, userID);
			if(taskAssignment!=null){
				delete(taskAssignment);
				return 1;
			}
		} catch (Exception e) {
			System.err.println("[undoTaskAssignment] Error in undo operation!");
			e.printStackTrace();
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
			for (Iterator it =taskAssignments.iterator(); it.hasNext();){
				TaskAssignment taskAssignment = (TaskAssignment) it.next();
				undoTaskAssignment(taskAssignment.getDocumentID(), taskAssignment.getUserID());
			}

		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

	}

	@Override
	public TaskAssignment findTaskAssignment(Long documentID, Long userID) {
		Map<String, Long> attMap = new HashMap<String, Long>();
		attMap.put("documentID", documentID);
		attMap.put("userID", userID);
		try {
			return getByCriteria(Restrictions.allEq(attMap));
		} catch (Exception e) {
			System.err.println("[findTaskAssignment] Error in find operation: documentID = " + documentID + ", userID = " + userID);
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return null;
	}

	@Override
	public List<TaskAssignment> findTaskAssignmentByID(Long documentID) {
		try {
			return getAllByCriteria(Restrictions.eq("documentID", documentID));  
		} catch (Exception e) {
			System.err.println("[findTaskAssignmentByID] Error in find operation: documentID = " + documentID);
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return null;

	}

	@Override
	public Integer getPendingTaskCount(Long userID) {
		try {
			List<TaskAssignment> taskAssignments = getAllByCriteria(Restrictions.eq("userID",userID));
			return taskAssignments.size();
		} catch (Exception e) {
			System.err.println("[getPendingTaskCount] Error in find operation: userID = " + userID);
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		return -1;
	}

}