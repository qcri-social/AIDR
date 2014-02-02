package qa.qcri.aidr.trainer.pybossa.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/29/13
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class ClientAppRunWorkerTest {

    @Autowired
    private ClientAppRunWorker clientAppRunWorker;

    @Test
    public void testProcessTaskPublish() throws Exception {
        //clientAppRunWorker.processTaskPublish();
        //clientAppRunWorker.processTaskRunImport();
    }
}
