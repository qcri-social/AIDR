package qa.qcri.aidr.trainer.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.CrisisDao;
import qa.qcri.aidr.trainer.api.dao.DocumentDao;
import qa.qcri.aidr.trainer.api.dao.TaskAssignmentDao;
import qa.qcri.aidr.trainer.api.dao.UsersDao;
import qa.qcri.aidr.trainer.api.entity.Crisis;
import qa.qcri.aidr.trainer.api.entity.Document;
import qa.qcri.aidr.trainer.api.entity.Users;
import qa.qcri.aidr.trainer.api.service.CrisisService;
import qa.qcri.aidr.trainer.api.service.DocumentService;
import qa.qcri.aidr.trainer.api.service.TaskAssignmentService;
import qa.qcri.aidr.trainer.api.service.UsersService;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.CrisisJsonOutput;
import qa.qcri.aidr.trainer.api.template.NominalAttributeJsonModel;
import qa.qcri.aidr.trainer.api.template.TaskBufferJsonModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/1/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("documentService")
@Transactional(readOnly = true)
public class DocumentServiceImpl implements DocumentService {

    protected static Logger logger = Logger.getLogger("service");

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private CrisisService crisisService;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateHasHumanLabel(Long documentID, boolean value) {
       // logger.debug("documentID : " + documentID) ;

        Document document = new Document(documentID, true);
       // logger.debug("document : " + document) ;
        if(document != null ) {
            documentDao.updateHasHumanLabel(document);
        }
    }

    @Override
    public Document findDocument(Long documentID) {
        return documentDao.findDocument(documentID);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public List<Document> getDocumentForTask(Long crisisID, int count, String userName) {

        List<Document> documents = null;
        Users users = usersService.findUserByName(userName);

        if(users != null){
            documents =  this.getAvailableDocument(crisisID, count)  ;
            if(documents != null && documents.size() > 0){
                taskAssignmentService.addToTaskAssignment(documents, users.getUserID());
            }
        }

        return documents;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<TaskBufferJsonModel> findOneDocumentForTaskByCririsID(Document document, Long crisisID) {
        List<TaskBufferJsonModel> jsonModelList = new ArrayList<TaskBufferJsonModel>();
        if(document != null){
            jsonModelList = getJsonModeForTask(crisisID, document);
        }
        return  jsonModelList;
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void addToOneTaskAssignment(long documentID, long userID){
       // addToOneTaskAssignment(documentID, userID);
        taskAssignmentService.addToOneTaskAssignment(documentID, userID);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void addToOneTaskAssignmentWithUserName(long documentID, String userName){
        Users users = usersService.findUserByName(userName);
        taskAssignmentService.addToOneTaskAssignment(documentID, users.getUserID());
    }


    private List<TaskBufferJsonModel> getJsonModeForTask(long crisisID, Document document){
        List<TaskBufferJsonModel> jsonModelList = new ArrayList<TaskBufferJsonModel>();
        Crisis crisis =  crisisService.findByCrisisID(crisisID) ;
        CrisisJsonModel jsonOutput = new CrisisJsonOutput().crisisJsonModelGenerator(crisis);
        Set<NominalAttributeJsonModel> attributeJsonModelSet = jsonOutput.getNominalAttributeJsonModelSet() ;

        TaskBufferJsonModel jsonModel = new TaskBufferJsonModel(document.getDocumentID(),document.getCrisisID(),attributeJsonModelSet,document.getLanguage(), document.getDoctype(), document.getData(), document.getValueAsTrainingSample(),0);
        jsonModelList.add(jsonModel);

        return jsonModelList;
    }


    private  List<Document> getAvailableDocument(long crisisID, int maxresult){
        return documentDao.findDocumentForTask(crisisID, maxresult)  ;

    }

}
