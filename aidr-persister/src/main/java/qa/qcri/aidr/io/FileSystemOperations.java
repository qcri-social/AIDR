/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.io;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import qa.qcri.aidr.utils.PersisterConfigurationProperty;
import qa.qcri.aidr.utils.PersisterConfigurator;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author Imran
 */
public class FileSystemOperations {
    
    public static ArrayList<String> get100KFilesList(String collectionCode){
        String filesPath = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + collectionCode + "/";
        File folder = new File(filesPath);
        File[] listOfFiles = folder.listFiles();
        Integer volNum = 1;
        ArrayList<String> fileNameList = new ArrayList<String>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String currentFileName = listOfFiles[i].getName();
                if (StringUtils.contains(currentFileName, collectionCode)) {
                    if (!(StringUtils.contains(currentFileName, ".csv"))
                    		&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) { //do not consider CSV files here, only consider JSON files
                        Integer currentVolN = Integer.parseInt(StringUtils.substringBetween(currentFileName, "vol-", ".json"));
                        if (currentVolN >= volNum) {
                            volNum = currentVolN;
                            fileNameList.add(currentFileName);
                            
                        }
                    }
                }
            }
        }
        
        return null;
    }

    public static String getLatestFileVolume(String collectionCode) {

        String filesPath = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + collectionCode + "/";
        File folder = new File(filesPath);
        File[] listOfFiles = folder.listFiles();
        Integer volNum = 1;
        String fileName = "";
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String currentFileName = listOfFiles[i].getName();
                if (StringUtils.contains(currentFileName, collectionCode)) {
                    if (!(StringUtils.contains(currentFileName, ".csv"))
                    		&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) { //do not consider CSV files here, only consider JSON files
                        Integer currentVolN = Integer.parseInt(StringUtils.substringBetween(currentFileName, "vol-", ".json"));
                        if (currentVolN >= volNum) {
                            volNum = currentVolN;
                            fileName = currentFileName;
                        }
                    }
                }
            }
        }
        return fileName;
    }

    public static int getLatestFileVolumeNumber(String collectionCode) {

        String filesPath = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + collectionCode + "/";
        File folder = new File(filesPath);
        File[] listOfFiles = folder.listFiles();
        Integer volNum = 1;
        String fileName = "";
        if (!(listOfFiles == null)) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String currentFileName = listOfFiles[i].getName();
                    if (StringUtils.contains(currentFileName, collectionCode)) {
                        if (!(StringUtils.contains(currentFileName, ".csv"))
                        		&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) { //do not consider CSV files here, only consider JSON files
                            Integer currentVolN = Integer.parseInt(StringUtils.substringBetween(currentFileName, "vol-", ".json"));
                            if (currentVolN > volNum) {
                                volNum = currentVolN;
                            }
                        }
                    }
                }
            }
        }
        return volNum;
    }
    
    @Deprecated
    public static int getLatestFileVolumeNumber4Tagger(String collectionCode) {

        String filesPath = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + collectionCode + "/";
    	//String filesPath = getProperty("DEFAULT_PERSISTER_FILE_PATH") + collectionCode + "/";
        File folder = new File(filesPath);
        File[] listOfFiles = folder.listFiles();
        Integer volNum = 1;
        String fileName = "";
        if (!(listOfFiles == null)) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String currentFileName = listOfFiles[i].getName();
                    if (StringUtils.contains(currentFileName, collectionCode) && StringUtils.startsWith("Classified_", fileName)) {
                        if (!(StringUtils.contains(currentFileName, ".csv"))
                        		&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) { //do not consider CSV files here, only consider JSON files
                            Integer currentVolN = Integer.parseInt(StringUtils.substringBetween(currentFileName, "vol-", ".json"));
                            if (currentVolN > volNum) {
                                volNum = currentVolN;
                            }
                        }
                    }
                }
            }
        }
        return volNum;
    }

    public static List<String> getAllJSONFileVolumes(String collectionCode) {

        String filesPath = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + collectionCode + "/";
        List<String> fileNames = new ArrayList();
        File folder = new File(filesPath);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String currentFileName = listOfFiles[i].getName();
                if (StringUtils.contains(currentFileName, collectionCode)) {
                    if (!StringUtils.contains(currentFileName, ".csv") && !StringUtils.contains(currentFileName, ".txt")
                    		&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol")) { //do not consider CSV files here, only consider JSON files
                        fileNames.add(currentFileName);
                    }
                }
            }
        }
        return fileNames;
    }
    
    @Deprecated
    public static List<String> getClassifiedFileVolumes(String collectionCode) {

        String filesPath = PersisterConfigurator.getInstance().getProperty(PersisterConfigurationProperty.DEFAULT_PERSISTER_FILE_PATH) + collectionCode + "/";
    	//String filesPath = getProperty("DEFAULT_PERSISTER_FILE_PATH") + collectionCode + "/";
        List<String> fileNames = new ArrayList();
        File folder = new File(filesPath);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String currentFileName = listOfFiles[i].getName();
                if (StringUtils.contains(currentFileName, collectionCode)) {
                    if (!(StringUtils.contains(currentFileName, ".csv"))) { //do not consider CSV files here, only consider JSON files
                        if (StringUtils.startsWith(currentFileName, "Classified_")
                        		&& StringUtils.containsIgnoreCase(listOfFiles[i].getName(), "vol"))
                            fileNames.add(currentFileName);
                    }
                }
            }
        }
        return fileNames;
    }
    
    public static boolean deleteFile(String fileName) {
        boolean isDeleted = false;
        try {
            File file = new File(fileName);
            if (file.delete()) {
                isDeleted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public int countNumberofLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
}
