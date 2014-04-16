package qa.qcri.aidr.task.dao.impl;

import org.hibernate.criterion.Restrictions;
//import org.springframework.stereotype.Repository;

import qa.qcri.aidr.task.dao.TaskAssignmentDao;
import qa.qcri.aidr.task.entities.Document;
import qa.qcri.aidr.task.entities.TaskAssignment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//@Repository
public class TaskAssignmentDaoImpl extends AbstractDaoImpl<TaskAssignment, String> implements TaskAssignmentDao{

    protected TaskAssignmentDaoImpl(){
        super(TaskAssignment.class);
    }

    @Override
    public void insertTaskAssignment(List<Document> documents, Long userID) {
        // hard code, will create user service

        for (Iterator it =documents.iterator(); it.hasNext();){
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
    public void undoTaskAssignment(List<Document> taskBuffer, Long userID) {
        for (Iterator it =taskBuffer.iterator(); it.hasNext();){
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
            List<TaskAssignment> taskAssignments = findByCriteria(Restrictions.le("assignedAt",dateFormat.parse(calDate)));
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

        return  findByCriterionID(Restrictions.allEq(attMap));
    }

    @Override
    public List<TaskAssignment> findTaskAssignmentByID(Long documentID) {
        return findByCriteria(Restrictions.eq("documentID",documentID));  //To change body of implemented methods use File | Settings | File Templates.

    }

    @Override
    public Integer getPendingTaskCount(Long userID) {
        List<TaskAssignment> taskAssignments = findByCriteria(Restrictions.eq("userID",userID));
        return taskAssignments.size();  //To change body of implemented methods use File | Settings | File Templates.
    }
}
