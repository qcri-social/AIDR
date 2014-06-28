package qa.qcri.aidr.trainer.pybossa.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import qa.qcri.aidr.trainer.pybossa.entity.ClientApp;
import qa.qcri.aidr.trainer.pybossa.service.impl.PybossaCommunicator;
import qa.qcri.aidr.trainer.pybossa.util.JsonSorter;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/29/13
 * Time: 7:24 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class ClientAppCreateWorkerTest {

 //   @Autowired
 //   private ClientAppCreateWorker clientAppCreateWorker;

  //  @Autowired
  //  private ClientAppService clientAppService;

    @Test
    public void testDoCreateApp() throws Exception {

     //   clientAppCreateWorker.doCreateApp();
      // clientAppCreateWorker.doCreateApp();
    //    ClientApp clientApp = clientAppService.findClientAppByCriteria("shortName", "clex_20131201_related_v1");
    //    clientAppCreateWorker.doAppTemplateUpdate(clientApp, clientApp.getNominalAttributeID());
    }


}
