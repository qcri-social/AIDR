package qa.qcri.aidr.trainer.pybossa.service.impl;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.pybossa.entity.*;
import qa.qcri.aidr.trainer.pybossa.format.impl.CVSRemoteFileFormatter;
import qa.qcri.aidr.trainer.pybossa.service.*;
import qa.qcri.aidr.trainer.pybossa.store.PybossaConf;
import qa.qcri.aidr.trainer.pybossa.store.LookupCode;
import qa.qcri.aidr.trainer.pybossa.store.URLPrefixCode;
import qa.qcri.aidr.trainer.pybossa.util.DateTimeConverter;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/22/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("reportProductService")
@Transactional(readOnly = false)
public class ReportProductServiceImpl implements ReportProductService {

    protected static Logger logger = Logger.getLogger(ReportProductServiceImpl.class);

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientAppService clientAppService;

    @Autowired
    private ReportTemplateService reportTemplateService;

    @Autowired
    private ClientAppSourceService clientAppSourceService;

    @Autowired
    private ClientAppEventService clientAppEventService;

    private Client client;
    private PybossaCommunicator pybossaCommunicator = new PybossaCommunicator();

    public void setClassVariable() throws Exception{
        client = clientService.findClientByCriteria("name", LookupCode.SYSTEM_USER_NAME);
    }


    @Override
    public void generateCVSReportForGeoClicker() throws Exception{
        setClassVariable();
        if(client == null){
            return;
        }

        List<ReportTemplate> temps =  reportTemplateService.getReportTemplateWithUniqueKey("clientAppID");

        Iterator itr= temps.iterator();

        while(itr.hasNext()){

            Long clientAppID = (long)itr.next();
            List<ReportTemplate> templateList =  reportTemplateService.getReportTemplateByClientApp(clientAppID, LookupCode.TEMPLATE_IS_READY_FOR_EXPORT);

            if(templateList.size() > LookupCode.MIN_REPORT_TEMPLATE_EXPORT_SIZE){
                CVSRemoteFileFormatter formatter = new CVSRemoteFileFormatter();
                ClientApp clientApp = clientAppService.findClientAppByID("clientAppID", clientAppID);
                String sTemp = reformatFileName(clientApp.getShortName()) ;

                String fileName = PybossaConf.DEFAULT_TRAINER_FILE_PATH + sTemp;

                CSVWriter writer = formatter.instanceToOutput(fileName);

                for(int i=0; i < templateList.size(); i++){
                    ReportTemplate rpt = templateList.get(i);
                    String answer = rpt.getAnswer().trim().toLowerCase();

                    formatter.addToCVSOuputFile(generateOutputData(rpt),writer);

                    rpt.setStatus(LookupCode.TEMPLATE_EXPORTED);
                    reportTemplateService.updateReportItem(rpt);
                }
                formatter.finalizeCVSOutputFile(writer);
                ClientAppEvent targetClinetApp = clientAppEventService.getNextSequenceClientAppEvent(clientApp.getClientAppID());
                if(targetClinetApp != null ){
                    ClientAppSource appSource = new ClientAppSource(targetClinetApp.getClientAppID(), LookupCode.EXTERNAL_DATA_SOURCE_ACTIVE, fileName);
                    clientAppSourceService.insertNewClientAppSource(appSource);
                }

            }

        }

    }

    private String[] generateOutputData(ReportTemplate rpt){

        String[] data = new String[8];
        data[0] = rpt.getTweetID().toString();
        data[1] = rpt.getTweet();
        data[2] = rpt.getAuthor();
        data[3] = rpt.getLat();
        data[4] = rpt.getLon();
        data[5] = rpt.getUrl();
        data[6] = rpt.getCreated();
        data[7] = rpt.getAnswer();

        return data;
    }

    private String reformatFileName(String shortName){
        String sTemp = DateTimeConverter.reformattedCurrentDateForFileName() + shortName ;

        if(sTemp.length() > 50){
            int iCutCount = sTemp.length() - 50;
            iCutCount = sTemp.length() - iCutCount;

            sTemp = sTemp.substring(0, iCutCount) ;

        }
        sTemp = sTemp + "export.csv";

        return sTemp;
    }
}
