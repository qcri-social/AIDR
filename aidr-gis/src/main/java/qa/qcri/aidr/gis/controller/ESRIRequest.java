package qa.qcri.aidr.gis.controller;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import qa.qcri.aidr.gis.service.ESRIWorker;
import qa.qcri.aidr.gis.servlet.ESRIService;
import qa.qcri.aidr.gis.store.LookUp;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 6/6/14
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/esri")
public class ESRIRequest {

    @Autowired
    ESRIWorker esriWorker;



    @RequestMapping(value="/test", method=RequestMethod.GET)
    public @ResponseBody String testing() {
        //System.out.println("fileURL : " + LookUp.DEFAULT_ESRI_DATA + fileURL);
        return "testing";
    }

    @RequestMapping(value="/getgeo", method=RequestMethod.GET)
    public String uriComponentsBuilder() {
        //esri201406061800.geojson
        //clickers.micromappers.com/esri_data/esri201406061800.geojson
        String fileURL = lastFileModified();

       // String fileURL = "201406031415export.geojson";
        System.out.println("fileURL : " + LookUp.DEFAULT_ESRI_DATA + fileURL);
        if(fileURL != null){
            return "redirect:" + LookUp.DEFAULT_ESRI_DATA + fileURL;
        }
        return "redirect:" + LookUp.DEFAULT_ESRI_DATA + "esri201406081400bk.geojson";
    }

    public String lastFileModified() {
        System.out.println("lastFileModified : " + LookUp.DEFAULT_ESRI_GEO_FILE_PATH);
        System.out.println("esriWorker : " + esriWorker);
        File choise = null;

        try{
            if(esriWorker != null){
                System.out.println("esriWorker getLastCreatedFileName: " +esriWorker.getLastCreatedFileName() );
            }
            File fl = new File(LookUp.DEFAULT_ESRI_GEO_FILE_PATH);
            File[] files = fl.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.isFile();
                }
            });
            long lastMod = Long.MIN_VALUE;


            for (File file : files) {
                if (file.lastModified() > lastMod) {
                    choise = file;
                    lastMod = file.lastModified();
                }
            }

            System.out.println("choise : " + choise);
        }
        catch(Exception e){
            System.out.println("choise Exception: " + e.getMessage());
            return null;
        }


        return choise.getName();
    }


}
