package qa.qcri.aidr.data.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.data.config.Configurations;


@Service
public class PersisterService {

    public String generateDownloadLink(String code, String queryString, String userName, Integer count, boolean removeRetweet, String jsonType, Date createdTimestamp) throws UnsupportedEncodingException, IOException {
		String url = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);
		
    	if(createdTimestamp.before(cal.getTime())){
    		url = Configurations.getPersisterURLForOlderCollections() ;
    	}
    	else{
    		url = Configurations.getPersisterURL();
    	}
    	
    	url += "/AIDRPersister/webresources/taggerPersister/filter/";
		
		if(jsonType.equalsIgnoreCase("CSV")){
			url += "genCSV";
		}
		else if (jsonType.equalsIgnoreCase("JSON")){
			url += "genJson";
		}
		url += "?collectionCode=" + code + "&exportLimit=" + count + "&jsonType=" + jsonType + "&userName=" + userName + "&removeRetweet=" + removeRetweet;
		return sendPost(queryString, url);
    }
    
    public String sendPost(String data, String url) {
        int responseCode = -1;
        HttpClient httpClient = new DefaultHttpClient();
        String str = "";
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
                    while ((output = br.readLine()) != null) {
                    	str = str + output;
                    }
                }
            }
            else{
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

        }catch (Exception ex) {
        	return null;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return str;
    }
}
