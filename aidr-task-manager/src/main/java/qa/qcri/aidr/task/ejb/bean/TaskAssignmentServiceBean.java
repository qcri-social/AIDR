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
	public void insertTaskAssignment(List<Document> taskList, Long userID) {
		// hard code, will create user service

		for (Iterator it = taskList.iterator(); it.hasNext();){
			Document tb = (Document) it.next();
			List<TaskAssignment> taskAssignments = findTaskAssignmentByID(tb.getDocumentID());
			if(taskAssignments.size()== 0){
				TaskAssignment taskAssignment = new TaskAssignment(tb.getDocumentID(), userID, new Date());
				save(taskAssignment);
			}
		}


	}

	@Override
	public void insertOneTaskAssignment(Long documentID, Long userID) {
		List<TaskAssignment> taskAssignments = findTaskAssignmentByID(documentID);
		if(taskAssignments.size()== 0){
			TaskAssignment taskAssignment = new TaskAssignment(documentID, userID, new Date());
			save(taskAssignment);
		}
	}

	@Override
	public void undoTaskAssignment(List<Document> taskList, Long userID) {
		for (Iterator it = taskList.iterator(); it.hasNext();){
			Document tb = (Document) it.next();
			TaskAssignment taskAssignment = (TaskAssignment)findTaskAssignment(tb.getDocumentID(), userID);
			delete(taskAssignment);
		}
	}

	@Override
	public void undoTaskAssignment(Map<Long, Long> taskMap) {
		Iterator entries = taskMap.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			Long key = (Long)entry.getKey();
			Long value = (Long)entry.getValue();
			TaskAssignment taskAssignment = (TaskAssignment)findTaskAssignment(key, value);
			delete(taskAssignment);
		}
	}

	@Override
	public void undoTaskAssignment(Long documentID, Long userID) {
		TaskAssignment taskAssignment = (TaskAssignment)findTaskAssignment(documentID, userID);
		if(taskAssignment!=null){
			delete(taskAssignment);
		}
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

		return getByCriteria(Restrictions.allEq(attMap));
	}

	@Override
	public List<TaskAssignment> findTaskAssignmentByID(Long documentID) {
		return getAllByCriteria(Restrictions.eq("documentID", documentID));  

	}

	@Override
	public Integer getPendingTaskCount(Long userID) {
		List<TaskAssignment> taskAssignments = getAllByCriteria(Restrictions.eq("userID",userID));
		return taskAssignments.size();  
	}

}