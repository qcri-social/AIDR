package qa.qcri.aidr.gis.service.impl;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import qa.qcri.aidr.gis.service.ESRIWorker;
import qa.qcri.aidr.gis.store.LookUp;
import qa.qcri.aidr.gis.util.DateTimeConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/3/14
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("ESRIWorker")
public class ESRIWorkerImpl implements ESRIWorker{

    protected static Logger logger = Logger.getLogger("ESRIWorkerImpl");

    @Override
    public void generateGeoJson() throws Exception {

        try{
            logger.info("generateGeoJson started ");
            GISCommunicator com = new GISCommunicator();

            String output = com.sendGet(LookUp.MAP_GEOJSON_URL) ;
            logger.info("generateGeoJson output :  " + output);
            FileWriter fw ;
            BufferedWriter bw;

            String fileName = LookUp.DEFAULT_ESRI_GEO_FILE_PATH + LookUp.DEFAULT_ESRI_STORY_MAP_FILE_NAME;

            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }
            else{
                fw = new FileWriter(file.getAbsoluteFile());
                bw = new BufferedWriter(fw);
                bw.write("");
                bw.close();
            }

            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(output);
            bw.close();
        }
        catch(Exception e){
            logger.error("generateGeoJson : " + e.getMessage() );
        }

    }
}
