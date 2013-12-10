package qa.qcri.aidr.trainer.pybossa.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/8/13
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class MicroMapperWorkerPublishTest {

    @Autowired
    private MicroMapperWorker microMapperWorker;

    @Test
    public void testProcessTaskPublish() throws Exception {
       // microMapperWorker.processTaskPublish();

    }
}
