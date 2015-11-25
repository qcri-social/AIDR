package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import qa.qcri.aidr.trainer.pybossa.service.*;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/18/13
 * Time: 1:19 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("pybossaMicroMapperWorker")
@Transactional(readOnly = false)
public class PybossaMicroMapperWorker implements MicroMapperWorker {

    protected static Logger logger = Logger.getLogger(PybossaMicroMapperWorker.class);

    @Autowired
    private ReportProductService reportProductService;


    @Override
    public void processTaskExport() throws Exception {
        System.out.println("processTaskExport : Start : " + new Date());
        reportProductService.generateCVSReportForGeoClicker();
    }

}
