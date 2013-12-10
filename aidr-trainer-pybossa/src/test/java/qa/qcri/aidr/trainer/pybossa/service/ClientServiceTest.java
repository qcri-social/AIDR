package qa.qcri.aidr.trainer.pybossa.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/19/13
 * Time: 1:12 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Test
    public void testFindClientbyID() throws Exception {

     //   Client client = clientService.findClientbyID("clientID", new Long(1));
      //  System.out.print("client : " + client);
    }

    public void testFindClientByCriteria() throws Exception {

    }
}
