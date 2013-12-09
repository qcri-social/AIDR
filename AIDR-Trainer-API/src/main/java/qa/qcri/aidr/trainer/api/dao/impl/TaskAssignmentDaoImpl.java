package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.TaskAssignmentDao;
import qa.qcri.aidr.trainer.api.entity.TaskAssignment;
import qa.qcri.aidr.trainer.api.entity.TaskBuffer;

import java.util.*;

@Repository
public class TaskAssignmentDaoImpl extends AbstractDaoImpl<TaskAssignment, String> implements TaskAssignmentDao{

    protected TaskAssignmentDaoImpl(){
        super(TaskAssignment.class);
    }

    @Override
    public void insertTaskAssignment(List<TaskBuffer> taskBuffer, Long userID) {
        // hard code, will create user service

        for (Iterator it =taskBuffer.iterator(); it.hasNext();){
            TaskBuffer tb = (TaskBuffer) it.next();
            TaskAssignment taskAssignment = new TaskAssignment(tb.getDocumentID(), userID, new Date());
            save(taskAssignment);
        }


    }

    @Override
    public void undoTaskAssignment(List<TaskBuffer> taskBuffer, Long userID) {
        for (Iterator it =taskBuffer.iterator(); it.hasNext();){
            TaskBuffer tb = (TaskBuffer) it.next();
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
    public TaskAssignment findTaskAssignment(Long documentID, Long userID) {
        Map<String, Long> attMap = new HashMap<String, Long>();
        attMap.put("documentID", documentID);
        attMap.put("userID", userID);

        return  findByCriterionID(Restrictions.allEq(attMap));
    }

    @Override
    public Integer getPendingTaskCount(Long userID) {
        List<TaskAssignment> taskAssignments = findByCriteria(Restrictions.eq("userID",userID));
        return taskAssignments.size();  //To change body of implemented methods use File | Settings | File Templates.
    }
}
