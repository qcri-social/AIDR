package qa.qcri.aidr.trainer.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/27/13
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Test
    public void testFindClientbyID() throws Exception {
          // Client client = clientService.findClientbyID("clientID", new Long(1));
          // System.out.print("client : " + client);
    }
}
