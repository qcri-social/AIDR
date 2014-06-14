package qa.qcri.aidr.trainer.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.TaskAssignmentDao;
import qa.qcri.aidr.trainer.api.dao.UsersDao;
import qa.qcri.aidr.trainer.api.entity.Document;
import qa.qcri.aidr.trainer.api.entity.Users;
import qa.qcri.aidr.trainer.api.service.TaskAssignmentService;

import java.util.List;
import java.util.Map;


@Service("taskAssignmentService")
@Transactional(readOnly = true)
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    @Autowired
    private TaskAssignmentDao taskAssignmentDao;

    @Autowired
    private UsersDao usersDao;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void revertTaskAssignmentByUserName(Long documentID, String userName) {
        Users users = usersDao.findUserByName(userName);
        if(users!= null){
            Long userID = users.getUserID();
           // System.out.println("userID : " + userID);
            taskAssignmentDao.undoTaskAssignment(documentID,userID);
        }
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void revertTaskAssignment(Long documentID, Long userID) {
        taskAssignmentDao.undoTaskAssignment(documentID,userID);
    }


    @Override
    public Integer getPendingTaskCount(Long userID) {
        return taskAssignmentDao.getPendingTaskCount(userID);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void addToTaskAssignment(List<Document> documents, long userID){
        taskAssignmentDao.insertTaskAssignment(documents, userID);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void addToOneTaskAssignment(long documentID, long userID){
        taskAssignmentDao.insertOneTaskAssignment(documentID, userID);
    }


}
