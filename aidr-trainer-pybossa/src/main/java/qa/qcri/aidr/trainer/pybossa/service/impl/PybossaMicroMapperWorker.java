package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import qa.qcri.aidr.trainer.pybossa.entity.*;
import qa.qcri.aidr.trainer.pybossa.format.impl.CVSRemoteFileFormatter;
import qa.qcri.aidr.trainer.pybossa.format.impl.GeoJsonOutputModel;
import qa.qcri.aidr.trainer.pybossa.format.impl.MicroMapperPybossaFormatter;
import qa.qcri.aidr.trainer.pybossa.format.impl.MicromapperInput;
import qa.qcri.aidr.trainer.pybossa.service.*;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;
import qa.qcri.aidr.trainer.pybossa.store.URLPrefixCode;
import qa.qcri.aidr.trainer.pybossa.store.UserAccount;
import qa.qcri.aidr.trainer.pybossa.util.DataFormatValidator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        reportProductService.generateCVSReportForGeoClicker();
    }

}
