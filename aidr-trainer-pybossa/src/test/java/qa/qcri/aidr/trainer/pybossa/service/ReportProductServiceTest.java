package qa.qcri.aidr.trainer.pybossa.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/25/13
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class ReportProductServiceTest {

    @Autowired
     ReportProductService reportProductService;

    @Test
    public void testGenerateCVSReportForGeoClicker() throws Exception {
       // reportProductService.generateCVSReportForGeoClicker();
    }
}
