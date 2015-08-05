package qa.qcri.aidr.trainer.api.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 4/2/14
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Communicator {

    // will be placed on config.
    protected static Logger logger = Logger.getLogger(Communicator.class);

    public Communicator(){

    }


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
            	logger.error("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

        }catch (Exception ex) {
            logger.warn("Error in processing request for data : " + data + " and url : " + url,ex);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseCode;

    }


    public String deleteGet(String url){
        int responseCode = -1;
        HttpClient httpClient = new DefaultHttpClient();
        StringBuffer responseOutput = new StringBuffer();
        try {
            HttpDelete request = new HttpDelete(url);
            request.addHeader("content-type", "application/json");
            HttpResponse response = httpClient.execute(request);
            responseCode = response.getStatusLine().getStatusCode();

            if ( responseCode == 200 || responseCode == 204) {
                if(response.getEntity()!=null){
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader((response.getEntity().getContent())));

                    String output;
                    while ((output = br.readLine()) != null) {
                        responseOutput.append(output);
                    }
                }
            }
            else{
                logger.error("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            	throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }


        }catch (Exception ex) {
        	logger.error("Exception in deleteGet2: " + url,ex);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        if(responseCode == -1){
            return "Exception";
        }
        return responseOutput.toString();

    }


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
                    //System.out.println("Output from Server ...." + response.getStatusLine().getStatusCode() + "\n");
                    while ((output = br.readLine()) != null) {
                        logger.debug(output);
                    }
                }
            }
            else{
                logger.error("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            	throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }


        }catch (Exception ex) {            
            logger.error("Exception in sendPost2: " + data,ex);
            logger.error("Exception in sendPost3: " + url,ex);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseCode;
    }


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
            	logger.error("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }


        }catch (Exception ex) {
            logger.error("Error in processing request for data : " + data + " and url :" + url,ex);
            responseOutput.append("Exception Code : " + ex);

        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return responseOutput.toString();
    }

    public String sendGet(String url) {
        HttpURLConnection con = null;
        StringBuffer response = new StringBuffer();
        // System.out.println("sendGet url : " + url);
        // logger.debug("[sendGet url  for debugger: ]" + url);

        try {
            URL connectionURL = new URL(url);
            con = (HttpURLConnection) connectionURL.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

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
            logger.error("sendGet url = " + url,ex);
        }

        return response.toString();
    }
     /**
    public String requestGet(String url, String contentType) {

        HttpClient httpClient = new DefaultHttpClient();
        StringBuffer responseOutput = new StringBuffer();

        try {

            HttpGet request = new HttpGet(url);
            request.addHeader("content-type", contentType);
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
    }   **/

}
