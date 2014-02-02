package qa.qcri.aidr.trainer.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 1/19/14
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class GeoServiceTest {

    @Autowired
    private GeoService geoService;

    @Test
    public void testGetGeoJsonOuputJSONP() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateInString = "2014-01-26 13:44:48";
        Date date = sdf.parse(dateInString);

       // String output = geoService.getGeoJsonOuputJSONP(date);
       // System.out.println(output);

    }
}
