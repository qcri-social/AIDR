package qa.qcri.aidr.gis.service.impl;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import qa.qcri.aidr.gis.service.ESRIWorker;
import qa.qcri.aidr.gis.store.LookUp;
import qa.qcri.aidr.gis.util.Communicator;
import qa.qcri.aidr.gis.util.DateTimeConverter;

import java.io.*;

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
    protected static String lastRecordUpdated = null;
    protected static String lastCreatedFileName = null;

    @Override
    public void generateGeoJson() throws Exception {

        try{
            logger.info("generateGeoJson started ");
            Communicator com = new Communicator();

            String output = com.sendGet(LookUp.MAP_GEOJSON_URL) ;
            logger.info("generateGeoJson output :  " + output);
            boolean isNewFileRequired = validateOutputContent(output);;
            //System.out.println("output : " + output);

            String updated = getUpdated(output);

            if(lastRecordUpdated != null){
                if(lastRecordUpdated.equalsIgnoreCase(updated)){
                    isNewFileRequired = false;
                }

            }

            lastRecordUpdated = updated;
            this.generateFile(output, isNewFileRequired);
            System.out.println("**lastCreatedFileName : " + lastCreatedFileName);
            System.out.println("**lastRecordUpdated : " + lastRecordUpdated);
        }
        catch(Exception e){
            logger.error("generateGeoJson : " + e.getMessage() );
        }

    }

    private String getUpdated(String output){
        String updated = null;
        try{
            JSONParser parser = new JSONParser();

            Object obj = parser.parse(output);

            JSONArray jsonArray = (JSONArray) obj;
            if(jsonArray.size() > 0){
                int index = jsonArray.size() -1;

                JSONObject lastObject = (JSONObject)jsonArray.get(index);

                JSONObject aInfo = (JSONObject)lastObject.get("info");
                updated= (String)aInfo.get("updated");
            }

        }
        catch(Exception e){
            logger.error("getUpdated "  + e.getMessage());
        }

        return updated;

    }

    private boolean validateOutputContent(String output){
        if(output== null) return false;
        if(output.isEmpty()) return false;

        return true;

    }
    private void generateFile(String output, boolean processStep){
        if(!processStep)
            return;

        String fileName = LookUp.DEFAULT_ESRI_GEO_FILE_PATH + LookUp.DEFAULT_ESRI_STORY_MAP_FILE_NAME + DateTimeConverter.reformattedCurrentDateForFileName() + LookUp.DEFAULT_ESRI_STORY_MAP_FILE_EXTENSION;

        File file = new File(fileName);

        lastCreatedFileName = file.getName();
        FileWriter fw ;
        BufferedWriter bw;
        try{
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
        catch (Exception e){
            logger.error("generateFile : " + e.getMessage() );
        }


    }
    public  File lastFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choise = null;

        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choise = file;
                lastMod = file.lastModified();
            }
        }
        return choise;
    }

    @Override
    public String getLastCreatedFileName(){
        return lastCreatedFileName;
    }
}
