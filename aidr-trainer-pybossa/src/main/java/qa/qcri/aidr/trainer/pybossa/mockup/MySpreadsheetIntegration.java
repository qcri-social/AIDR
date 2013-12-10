package qa.qcri.aidr.trainer.pybossa.mockup;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/18/13
 * Time: 12:22 AM
 * To change this template use File | Settings | File Templates.
 */


import au.com.bytecode.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

public class MySpreadsheetIntegration {
    public static void main(String[] args)
            throws Exception{

        String[] row = null;

        URL stockURL = new URL("http://localhost:8888/tornadotweets.csv");
        BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));

        CSVReader csvReader = new CSVReader(in);
        List content = csvReader.readAll();

        for (Object object : content) {
            row = (String[]) object;

            System.out.println(row[0]
                    + " # " + row[1]
                    + " #  " + row[2]);
        }
        csvReader.close();
    }
}