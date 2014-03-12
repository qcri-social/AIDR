package qa.qcri.aidr.trainer.pybossa.format.impl;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.net.*;
import java.util.*;
/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/18/13
 * Time: 2:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class CVSRemoteFileFormatter {

    public List<MicromapperInput> getInputData(String url) throws Exception{
        //[Twitter username] // [Tweet message] // [optional: time-stamp] // [optional: location] // [optional: latitude] // [optional: longitude] // [image link]


        String[] row = null;
        List<MicromapperInput> sourceSet = new ArrayList<MicromapperInput>();

        URL stockURL = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));

        CSVReader csvReader = new CSVReader(in);
        List content = csvReader.readAll();

        for (Object object : content) {
            row = (String[]) object;

           //  tweetID,tweet,author,lat,lng,url,created
            MicromapperInput source = new MicromapperInput(row[0], row[1], row[2], row[3], row[4], row[5], row[6]);
            sourceSet.add(source);
        }
        csvReader.close();

        if(sourceSet.size() > 1){
            sourceSet.remove(0);
        }

        return sourceSet;
    }


    private CSVReader getCVSContentReader(String source) throws Exception{
        CSVReader csvReader = null;
        if(source.toLowerCase().startsWith("http")){
            URL stockURL = new URL(source);
            BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));

            csvReader = new CSVReader(in);

        }
        else{
            csvReader = new CSVReader(new FileReader(source));
        }

        return csvReader;
    }

    public List<MicromapperInput> getClickerInputData(String url) throws Exception{
         //[Twitter username] // [Tweet message] // [optional: time-stamp] // [optional: location] // [optional: latitude] // [optional: longitude] // [image link]
        String[] row = null;
        List<MicromapperInput> sourceSet = new ArrayList<MicromapperInput>();

        CSVReader csvReader = getCVSContentReader(url) ;
        List content = csvReader.readAll();

        for (Object object : content) {
            //User-Name(0)	Tweet(1)	Time-stamp(2)	Location(3)	Latitude(4)	Longitude(5)	Image-link(6)	TweetID(7)
           // public MicromapperInput(String tweetID, String tweet, String author, String lat, String lng , String url, String created){
            row = (String[]) object;
            if(row!=null){
                if(row.length > 7){

                    String tweetID = row[7];
                    String tweet=row[1];
                    String author=row[0];
                    String lat=row[4];
                    String lng=row[5];
                    String imgURL = row[6];
                    String created = row[2];
                    String dataSourceLocation;

                    MicromapperInput source = new MicromapperInput(tweetID, tweet, author, lat, lng, imgURL, created);
                    sourceSet.add(source);
                }
            }
            //  tweetID,tweet,author,lat,lng,url,created



        }
        csvReader.close();
        // REMOVEW HEADER
        if(sourceSet.size() > 1){
            sourceSet.remove(0);
        }

        return sourceSet;
    }


    public List<MicromapperInput> getGeoClickerInputData(String url) throws Exception{
        //"tweetID","tweet","author","lat","lng","url","created","answer"
        String[] row = null;
        List<MicromapperInput> sourceSet = new ArrayList<MicromapperInput>();

        CSVReader csvReader = getCVSContentReader(url) ;
        List content = csvReader.readAll();

        for (Object object : content) {
            row = (String[]) object;
            if(row!=null){
                if(row.length > 7){
                    String tweetID = row[0];
                    String tweet=row[1];
                    String author=row[2];
                    String lat=row[3];
                    String lng=row[4];
                    String imgURL = row[5];
                    String created = row[6];
                    String answer = row[7];

                    MicromapperInput source = new MicromapperInput(tweetID, tweet, author, lat, lng, imgURL, created, answer);
                    sourceSet.add(source);
                }
            }
        }
        csvReader.close();

        if(sourceSet.size() > 1){
            sourceSet.remove(0);
        }

        return sourceSet;
    }


    public List<MicromapperInput> getClickerLocalFileInputData(String csvFilename) throws Exception{
        //[Twitter username] // [Tweet message] // [optional: time-stamp] // [optional: location] // [optional: latitude] // [optional: longitude] // [image link]
        String[] row = null;
        List<MicromapperInput> sourceSet = new ArrayList<MicromapperInput>();

        CSVReader csvReader = getCVSContentReader(csvFilename) ;
        List content = csvReader.readAll();

        for (Object object : content) {
            //User-Name(0)	Tweet(1)	Time-stamp(2)	Location(3)	Latitude(4)	Longitude(5)	Image-link(6)	TweetID(7)
            // public MicromapperInput(String tweetID, String tweet, String author, String lat, String lng , String url, String created){
            row = (String[]) object;
            if(row!=null){
                if(row.length > 7){

                    String tweetID = row[7];
                    String tweet=row[1];
                    String author=row[0];
                    String lat=row[4];
                    String lng=row[5];
                    String imgURL = row[6];
                    String created = row[2];
                    String dataSourceLocation;

                    MicromapperInput source = new MicromapperInput(tweetID, tweet, author, lat, lng, imgURL, created);
                    sourceSet.add(source);
                }
            }
            //  tweetID,tweet,author,lat,lng,url,created



        }
        csvReader.close();
        // REMOVEW HEADER
        if(sourceSet.size() > 1){
            sourceSet.remove(0);
        }

        return sourceSet;
    }

    public List<MicromapperInput> getFileBaseImageClickerInputData(String csvFilename) throws Exception{
        //[Twitter username] // [Tweet message] // [optional: time-stamp] // [optional: location] // [optional: latitude] // [optional: longitude] // [image link]

        List<MicromapperInput> sourceSet = new ArrayList<MicromapperInput>();

        CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
        String[] row = null;
        while ((row = csvReader.readNext()) != null) {
            if(row!=null){
                if(row.length > 8){
                    MicromapperInput source = new MicromapperInput(row[8], row[1], row[0], row[5], row[6], row[7], row[2]);
                    sourceSet.add(source);
                }
            }
        }

        csvReader.close();

        // REMOVEW HEADER
        if(sourceSet.size() > 1){
            sourceSet.remove(0);
        }

        return sourceSet;
    }


    public CSVWriter instanceToOutput(String fileName) throws Exception{
        File file = new File(fileName);
        //file.getAbsolutePath();
        CSVWriter writer = new CSVWriter(new FileWriter(fileName, true));
        //  public MicromapperOuput(String tweetID, String tweet, String author, String lat, String lng, String url, String created, String answer){

        String[] header = {"tweetID", "tweet","author", "lat", "lng", "url", "created", "answer"};
        writer.writeNext(header);

        return writer;

    }
    public void addToCVSOuputFile(String[] data, CSVWriter writer) throws Exception{

        writer.writeNext(data);

    }

    public void finalizeCVSOutputFile(CSVWriter writer) throws Exception{
        writer.close();

    }



}
