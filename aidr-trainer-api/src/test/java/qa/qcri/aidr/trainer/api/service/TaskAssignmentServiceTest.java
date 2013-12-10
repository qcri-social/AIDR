package qa.qcri.aidr.trainer.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/15/13
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})

public class TaskAssignmentServiceTest {

    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @Test
    public void testGetPendingTaskCount() throws Exception {
      //  Integer size = taskAssignmentService.getPendingTaskCount(new Long(1));

      //  System.out.println(size + "\n");

      //  Long documentID = new Long(379958);
      //  String userName = "Pybossa";

      //  taskAssignmentService.revertTaskAssignment(documentID,userName );
    }
}
