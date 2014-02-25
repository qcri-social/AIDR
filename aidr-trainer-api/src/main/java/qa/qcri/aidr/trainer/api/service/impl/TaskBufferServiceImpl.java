package qa.qcri.aidr.trainer.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.*;
import qa.qcri.aidr.trainer.api.entity.*;
import qa.qcri.aidr.trainer.api.service.TaskBufferService;
import qa.qcri.aidr.trainer.api.template.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@Service("taskBufferService")
@Transactional(readOnly = true)
public class TaskBufferServiceImpl implements TaskBufferService {

    @Autowired
    private TaskBufferDao taskBufferDao;

    @Autowired
    private TaskAssignmentDao taskAssignmentDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private CrisisDao crisisDao;



    @Override
    @Transactional(readOnly = false)
    public List<TaskBuffer> findAssignableTaskBuffer(String columnName, Integer value, String userName , Integer maxresult) {
        // tractional process for get & insert
        List<TaskBuffer> taskBufferList =  taskBufferDao.findAllTaskBuffer(columnName, value, maxresult);
        // TO DO : NEED TO EXTRACT IT.
        Users users = usersDao.findUserByName(userName);

        taskAssignmentDao.insertTaskAssignment(taskBufferList, users.getUserID());

        return  taskBufferList;
    }

    @Override
    @Transactional(readOnly = false)
    public List<TaskBuffer> findAvailableaskBufferByCririsID(Long cririsID,String userName , Integer assignedCount, Integer maxresult) {
        // tractional process for get & insert
        List<TaskBuffer> taskBufferList = null;
        Users users = usersDao.findUserByName(userName);
        if(users != null){
            taskBufferList =  taskBufferDao.findAllTaskBufferByCririsID(cririsID, assignedCount, maxresult);
            taskAssignmentDao.insertTaskAssignment(taskBufferList, users.getUserID());
        }

        return  taskBufferList;
    }

    @Override
    @Transactional(readOnly = false)
    public List<TaskBufferJsonModel> findOneTaskBufferByCririsID(Long cririsID, String userName, Integer assignedCount, Integer maxresult) {
        List<TaskBufferJsonModel> jsonModelList = new ArrayList<TaskBufferJsonModel>();

        Users users = usersDao.findUserByName(userName);

        if(users != null){
            List<TaskBuffer> taskBufferList =  taskBufferDao.findAllTaskBufferByCririsID(cririsID, assignedCount, maxresult);
            Crisis crisis =  crisisDao.findByCrisisID(cririsID) ;
            CrisisJsonModel jsonOutput = new CrisisJsonOutput().crisisJsonModelGenerator(crisis);
            Set<NominalAttributeJsonModel> attributeJsonModelSet = jsonOutput.getNominalAttributeJsonModelSet() ;
            for(int i =0; i < taskBufferList.size(); i++){
                TaskBuffer taskBuffer = taskBufferList.get(0);
                TaskBufferJsonModel jsonModel = new TaskBufferJsonModel(taskBuffer.getDocumentID(),taskBuffer.getCrisisID(),attributeJsonModelSet,taskBuffer.getLanguage(), taskBuffer.getDoctype(), taskBuffer.getData(), taskBuffer.getValueAsTrainingSample(), taskBuffer.getAssignedCount());
                jsonModelList.add(jsonModel);
            }
            taskAssignmentDao.insertTaskAssignment(taskBufferList, users.getUserID());
        }

        return  jsonModelList;
    }



}
