package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import qa.qcri.aidr.trainer.pybossa.service.AbstractCommunicator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/17/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */

public class PybossaCommunicator extends AbstractCommunicator {
    // will be placed on config.
    protected static Logger logger = Logger.getLogger("service");

    public PybossaCommunicator(){
        super(PybossaCommunicator.class);
    }

    @Override
    public int sendPut(String data, String url) {
        int responseCode = -1;
        HttpClient httpClient = new DefaultHttpClient();
        try {

            HttpPut request = new HttpPut(url);
            StringEntity params =new StringEntity(data,"UTF-8");
            params.setContentType("application/json");
            request.addHeader("content-type", "application/json");
            request.addHeader("Accept", "*/*");
            request.addHeader("Accept-Encoding", "gzip,deflate,sdch");
            request.addHeader("Accept-Language", "en-US,en;q=0.8");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 204) {

                BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));

                String output;
               // System.out.println("Output from Server ...." + response.getStatusLine().getStatusCode() + "\n");
                while ((output = br.readLine()) != null) {
                   // System.out.println(output);
                }
            }
            else{

                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

        }catch (Exception ex) {
            System.out.println("ex Code sendPut: " + ex);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseCode;

    }

    @Override
    public int sendPost(String data, String url) {
        // dataoutput="{\"info\": {\"username\": \" drippingmind\", \"userid\": \"449077875\", \"n_answers\": 2, \"date\": \" Wed Dec 05 11:13:18 CET 2012\", \"text\": \" Google provides support in the #Philippines w/ crisis response map 4 typhoon #Pablo #Bopha http://t.co/mJCNBHAJ #DRM #ICT #EmergencyResponse\", \"question\": \"Please tag the following tweet or SMS based on the category or categories that best describes the link(s) included in the tweet/Sms\", \"tweetid\": \"2.76E17\"}, \"state\": 0, \"n_answers\": 30, \"quorum\": 0, \"calibration\": 0, \"app_id\": 4, \"priority_0\": 0}";
        int responseCode = -1;
        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpPost request = new HttpPost(url);
            StringEntity params =new StringEntity(data, "UTF-8");
            params.setContentType("application/json");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            responseCode = response.getStatusLine().getStatusCode();

            if ( responseCode == 200 || responseCode == 204) {
                if(response.getEntity()!=null){
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader((response.getEntity().getContent())));

                    String output;
                   // System.out.println("Output from Server ...." + response.getStatusLine().getStatusCode() + "\n");
                    while ((output = br.readLine()) != null) {
                     //   System.out.println(output);
                    }
                }
            }
            else{
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }


        }catch (Exception ex) {
            System.out.println("ex Code sendPost: " + ex);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseCode;
    }

    @Override
    public String sendPostGet(String data, String url) {
        // dataoutput="{\"info\": {\"username\": \" drippingmind\", \"userid\": \"449077875\", \"n_answers\": 2, \"date\": \" Wed Dec 05 11:13:18 CET 2012\", \"text\": \" Google provides support in the #Philippines w/ crisis response map 4 typhoon #Pablo #Bopha http://t.co/mJCNBHAJ #DRM #ICT #EmergencyResponse\", \"question\": \"Please tag the following tweet or SMS based on the category or categories that best describes the link(s) included in the tweet/Sms\", \"tweetid\": \"2.76E17\"}, \"state\": 0, \"n_answers\": 30, \"quorum\": 0, \"calibration\": 0, \"app_id\": 4, \"priority_0\": 0}";

        HttpClient httpClient = new DefaultHttpClient();
        StringBuffer responseOutput = new StringBuffer();
        try {
            HttpPost request = new HttpPost(url);
            StringEntity params =new StringEntity(data, "UTF-8");
            params.setContentType("application/json");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            int responseCode = response.getStatusLine().getStatusCode();

            if (responseCode == 200 || responseCode == 204) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));

                String output;
               // System.out.println("Output from Server ...." + response.getStatusLine().getStatusCode() + "\n");
                while ((output = br.readLine()) != null) {
                    responseOutput.append(output);
                }
            }
            else{

                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }


        }catch (Exception ex) {
           // System.out.println("Exception Code : " + ex);
            responseOutput.append("Exception Code : " + ex);

        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseOutput.toString();
    }


    @Override
    public String sendGet(String url) {
        HttpURLConnection con = null;
        StringBuffer response = new StringBuffer();
       // System.out.println("sendGet url : " + url);
       // logger.debug("[sendGet url  for debugger: ]" + url);

        try {
            URL connectionURL = new URL(url);
            con = (HttpURLConnection) connectionURL.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream(),"UTF-8"));
            String inputLine;
           // logger.debug("[response code ]" + responseCode);
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }catch (Exception ex) {
            System.out.println("ex Code sendGet: " + ex + " : sendGet url = " + url);
            logger.debug("[errror on sendGet ]" + url);
        }

        return response.toString();
    }

}

