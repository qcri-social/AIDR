package qa.qcri.aidr.trainer.api.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.trainer.api.dao.UsersDao;
import qa.qcri.aidr.trainer.api.entity.Users;
import qa.qcri.aidr.trainer.api.service.TaskAssignmentService;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;

@Service("taskAssignmentService")
@Transactional(readOnly = true)
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    //@Autowired
    //private TaskAssignmentDao taskAssignmentDao;

    @Autowired
    private UsersDao usersDao;
    
    @Autowired TaskManagerRemote<DocumentDTO, Long> taskManager;
    
	protected static Logger logger = Logger.getLogger(TaskAnswerServiceImpl.class);

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void revertTaskAssignmentByUserName(Long documentID, String userName) {
        /*
    	Users users = usersDao.findUserByName(userName);
        if(users!= null){
            Long userID = users.getUserID();
           // System.out.println("userID : " + userID);
            taskAssignmentDao.undoTaskAssignment(documentID,userID);
        }
        */
    	Users users = usersDao.findUserByName(userName);
        if(users!= null){
            Long userID = users.getUserID();
            //System.out.println("userID : " + userID);
            //taskAssignmentDao.undoTaskAssignment(documentID,userID);
            try {
				taskManager.undoTaskAssignment(documentID, userID);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void revertTaskAssignment(Long documentID, Long userID) {
        //taskAssignmentDao.undoTaskAssignment(documentID,userID);
    	try {
			taskManager.undoTaskAssignment(documentID, userID);
			//logger.info("Removed from taskAssignment table: documentID = " + documentID + ", userID = " + userID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    @Override
    public Integer getPendingTaskCount(Long userID) {
        //return taskAssignmentDao.getPendingTaskCount(userID);
    	//System.out.println("[getPendingTaskCount] received request for userID = " + userID);
    	return taskManager.getPendingTaskCountByUser(userID);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void addToTaskAssignment(List<DocumentDTO> documents, Long userID){
        //taskAssignmentDao.insertTaskAssignment(documents, userID);
 
    	//List<qa.qcri.aidr.task.entities.Document> docList = mapper.deSerializeList(jsonString, new TypeReference<List<qa.qcri.aidr.task.entities.Document>>() {});
    	try {
    		//System.out.println("[addToTaskAssignment] Going to insert task list of size = " + documents.size() + ", for userID: " + userID);
    		taskManager.assignNewTaskToUser(documents, userID);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void addToOneTaskAssignment(Long documentID, Long userID){
        //taskAssignmentDao.insertOneTaskAssignment(documentID, userID);
    	try {
    		//System.out.println("[addToOneTaskAssignment] Going to insert document = " + documentID + ", for userID: " + userID);
    		taskManager.assignNewTaskToUser(documentID, userID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


}
