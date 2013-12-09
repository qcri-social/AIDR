package qa.qcri.aidr.trainer.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import qa.qcri.aidr.trainer.api.entity.Crisis;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.CrisisNominalAttributeModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:spring/hibernateContext.xml"})
public class CrisisServiceTest {

    @Autowired
    private CrisisService crisisService;

    @Test
    public void testFindByCrisisID() throws Exception {
       Long id = new Long("14");

       Crisis crisis = crisisService.findByCrisisID(id) ;
       //CrisisJsonModel findByOptimizedCrisisID(Long id)
       System.out.println(crisis + "\n");

       CrisisJsonModel crisisJsonModel = crisisService.findByOptimizedCrisisID(id);

       System.out.println(crisisJsonModel + "\n");
    }


    public void testFindAllTest() throws Exception{
       List<Crisis> crisisList = crisisService.findAllActiveCrisis();
       System.out.println(crisisList + "\n");
       List<CrisisNominalAttributeModel> tm = crisisService.getAllActiveCrisisNominalAttribute();

       System.out.println(tm + "\n");

    }
}
