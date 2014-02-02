package qa.qcri.aidr.trainer.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import qa.qcri.aidr.trainer.api.template.TaskBufferJsonModel;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class TaskBufferServiceTest {

    @Autowired
    private TaskBufferService taskBufferService;

    @Autowired
    private TaskAssignmentService taskAssignmentService;



    private final String columnName = "assignedCount";
    private final Integer value = 0;

    @Test
    public void testFindAllTaskBuffer() throws Exception {
       // List<TaskBufferJsonModel> taskBuffers = taskBufferService.findOneTaskBufferByCririsID(new Long(14),"Pybossa", 0, 1) ;
       // System.out.println("taskBuffers : " + taskBuffers.size());
      //  taskAssignmentService.revertTaskAssignment(new Long(367577), "pybossa");

      //  for(int i = 0; i < taskBuffers.size(); i++){
      //      taskAssignmentService.revertTaskAssignment(taskBuffers.get(i).getDocumentID(), "Pybossa");
      //  }
      //  int originalReqCount = taskBuffers.size();

    }
}
