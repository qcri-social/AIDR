package qa.qcri.aidr.trainer.pybossa.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/20/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class ClientAppServiceTest {

   // @Autowired
   // private ClientAppService clientAppService;

    @Test
    public void testGetAllCrisis() throws Exception {
      /**
       List<ClientApp> clientAppList =   clientAppService.getAllCrisis();
        for (int i = 0; i < clientAppList.size(); i++) {
            Object obj =  clientAppList.get(i);

            System.out.println("CrisisID: " + clientAppList.get(i));


            Long id = (Long)obj;
            List<ClientApp> appList = clientAppService.getAllClientAppByCrisisID(id );
                if(appList!=null){
                for (int index = 0; index < appList.size() ; index++){
                    ClientApp currentClientApp = appList.get(index);
                    System.out.println("ClientAppID: " + currentClientApp.getClientAppID());

                }
            }
        }
       System.out.print("clientAppList: " + clientAppList);    **/




    }

   // @Test
    public void testCreateClientAPP() throws Exception{
        //ClientApp clientApp1 = new ClientApp(new Long(1), new Long(2), "name","description", new Long(34),"code", new Long(1));
      /**
        //clientAppService.createClientApp(clientApp1);
        Long clientID = new Long(1);
        Long crisisID= new Long(14);
        Long nominalAttributeID= new Long(3);
        Long platformAppID= new Long(172);
        String name = "Syria Civil War: Violence";
        String description= "Syria Civil War(14)";
        String shortName= "Syria_Civil_War_Violence";

        ClientApp clientApp = new ClientApp(clientID,crisisID,name, description,platformAppID,shortName,nominalAttributeID);

        clientAppService.createClientApp(clientApp);  **/
    }
}
