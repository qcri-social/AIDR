package qa.qcri.aidr.trainer.pybossa.service.impl;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.pybossa.entity.*;
import qa.qcri.aidr.trainer.pybossa.format.impl.CVSRemoteFileFormatter;
import qa.qcri.aidr.trainer.pybossa.format.impl.GeoJsonOutputModel;
import qa.qcri.aidr.trainer.pybossa.service.*;
import qa.qcri.aidr.trainer.pybossa.store.PybossaConf;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;
import qa.qcri.aidr.trainer.pybossa.store.UserAccount;
import qa.qcri.aidr.trainer.pybossa.util.DateTimeConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
        client = clientService.findClientByCriteria("name", UserAccount.SYSTEM_USER_NAME);
    }


    @Override
    public void generateCVSReportForGeoClicker() throws Exception{
        setClassVariable();
        if(client == null){
            return;
        }
        List<ClientApp> appList = clientAppService.getAllClientAppByClientID(client.getClientID() );
       // System.out.println("appList size: " + appList.size() + " - " + client.getClientID());
        Iterator itr= appList.iterator();

        while(itr.hasNext()){
            ClientApp clientApp = (ClientApp)itr.next();
            List<ReportTemplate> templateList =  reportTemplateService.getReportTemplateByClientApp(clientApp.getClientAppID(), StatusCodeType.TEMPLATE_IS_READY_FOR_EXPORT);

            if(templateList.size() > 0){
                CVSRemoteFileFormatter formatter = new CVSRemoteFileFormatter();
                String sTemp = DateTimeConverter.reformattedCurrentDateForFileName() + clientApp.getShortName() + "export.csv";
                String fileName = PybossaConf.DEFAULT_TRAINER_FILE_PATH + sTemp;
                String mmFetchFileName = "http://aidr-prod.qcri.org/data/trainer/" +sTemp;
                CSVWriter writer = formatter.instanceToOutput(fileName);

                for(int i=0; i < templateList.size(); i++){
                    ReportTemplate rpt = templateList.get(i);
                    String answer = rpt.getAnswer().trim().toLowerCase();

                   // if(!answer.equals("05_not_relevant") && !answer.equals("null") ) {
                        formatter.addToCVSOuputFile(generateOutputData(rpt),writer);
                    //}

                    rpt.setStatus(StatusCodeType.TEMPLATE_EXPORTED);
                    reportTemplateService.updateReportItem(rpt);
                }
                formatter.finalizeCVSOutputFile(writer);
                ClientAppEvent targetClinetApp = clientAppEventService.getNextSequenceClientAppEvent(clientApp.getClientAppID());
                ClientAppSource appSource = new ClientAppSource(targetClinetApp.getClientAppID(), StatusCodeType.EXTERNAL_DATA_SOURCE_ACTIVE, fileName);
                clientAppSourceService.insertNewClientAppSource(appSource);

                JSONArray jsonArray = new JSONArray();
                JSONObject obj= new JSONObject();
                obj.put("fileURL",mmFetchFileName);
                obj.put("appID",260);

                jsonArray.add(obj);

                String returnValue = pybossaCommunicator.sendPostGet(jsonArray.toJSONString(), "http://gis.micromappers.org/MMAPI/rest/source/save");

                System.out.println("generateCVSReportForGeoClicker returnValue :" + returnValue);

            }
            // insert into source for file
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private String[] generateOutputData(ReportTemplate rpt){

        String[] data = new String[8];
        data[0] =   rpt.getTweetID().toString();
        data[1] =  rpt.getTweet();
        data[2] = rpt.getAuthor();
        data[3] = rpt.getLat();
        data[4] = rpt.getLon();
        data[5] = rpt.getUrl();
        data[6] = rpt.getCreated();
        data[7] = rpt.getAnswer();

        return data;
    }
}
