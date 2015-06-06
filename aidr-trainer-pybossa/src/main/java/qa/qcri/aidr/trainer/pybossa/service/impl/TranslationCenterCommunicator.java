package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import qa.qcri.aidr.trainer.pybossa.entity.TaskTranslation;
import qa.qcri.aidr.trainer.pybossa.format.impl.TranslationProjectModel;
import qa.qcri.aidr.trainer.pybossa.format.impl.TranslationRequestModel;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kamal on 4/25/15.
 */
public class TranslationCenterCommunicator {
    final private static String BASE_URL = "https://twb.translationcenter.org/api/v1";
    final private static String API_KEY = "jk26fh2yzwo4";
    protected static Logger logger = Logger.getLogger("service.TranslationCenterCommunicator");


    public static Map pushTranslationRequest(TranslationRequestModel request) {
        Map documentResult = pushDocumentForRequest(request);
        // maybe throw exceptions
        if (documentResult == null) {
            return null;
        }

        long documentIds[] = new long[1];
        documentIds[0] = ((Integer)documentResult.get("document_id")).longValue();
        request.setSourceDocumentIds(documentIds);
        final String url=BASE_URL+"/orders";
        HttpHeaders requestHeaders=new HttpHeaders();
        requestHeaders.add("X-Proz-API-Key", API_KEY);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate=new RestTemplate();
        //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpEntity entity = new HttpEntity(getJsonForRequest(request), requestHeaders);

        ResponseEntity<Map> response=restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        logger.debug(response);
        return response.getBody();

    }


    //brute force but simpler to debug than using Jackson
    private static String getJsonForRequest(TranslationRequestModel request) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = formatter.format(request.getDeadline());

        String jsonString = "{            \n" +
                "            \"contact_email\": \"" + request.getContactEmail() + "\",\n" +
                "            \"title\": \"" + request.getTitle() + "\",\n" +
                "            \"source_lang\": \"" + request.getSourceLanguage() + "\",\n" +
                "            \"target_langs\": [\"" + request.getTargetLanguages()[0] + "\"],\n" +
                "            \"source_document_ids\": [" + request.getSourceDocumentIds()[0] + "],\n" +
                "            \"source_wordcount\":" + request.getSourceWordCount() + ",\n" +
                "            \"instructions\": \"" + request.getInstructions() + "\",\n" +
                "            \"deadline\": \"" + formattedDate + "\",\n" +
                "            \"urgency\": \"" + request.getUrgency() + "\",\n" +
                "            \"project_id\": " + request.getProjectId() + ",\n" +
                "            \"callback_url\": \"" + request.getCallbackURL() + "\"\n" +
                "}";
        return jsonString;
    }

    public static Map pushDocumentForRequest(TranslationRequestModel request) {
        String filename = "TWB_Source_"+System.currentTimeMillis()+".csv";

        //decide whether its better to send file or content
        String content = getCSVData(request.getTranslationList());
        //generateCsvFile(filename, request.getTranslationList());

        final String url=BASE_URL+"/documents";
        HttpHeaders requestHeaders=new HttpHeaders();
        requestHeaders.add("X-Proz-API-Key", API_KEY);
        requestHeaders.setContentType(new MediaType("multipart","form-data"));
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.add("document", content.getBytes(StandardCharsets.UTF_8));
        map.add("name", "translation_source.csv");

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new    HttpEntity<LinkedMultiValueMap<String, Object>>(
                map, requestHeaders);

        ResponseEntity<Map> result = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        logger.debug("Result of document push:"+result.getBody());
        return result.getBody();

    }

    public static void updateTranslationOrder(String selfLink, String status, String comment) {
        final String url=selfLink;
        HttpHeaders requestHeaders=new HttpHeaders();
        requestHeaders.add("X-Proz-API-Key", API_KEY);
        requestHeaders.add("X-HTTP-Method-Override", "PATCH");
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate=new RestTemplate();
        //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String json = "{ \"delivery_status\": \""+status+"\", \"reject_reason\": \""+comment+"\"}";
        HttpEntity entity = new HttpEntity(json, requestHeaders);

        ResponseEntity<Map> response=restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        logger.debug(response);
    }


    private static String getCSVData(List<TaskTranslation> list) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Task Id");
        buffer.append(",");
        buffer.append("Original Text");
        buffer.append(",");
        buffer.append("Translated Text");
        buffer.append(",");
        buffer.append("Answer Code");
        buffer.append("\n");

        if (list != null) {
            Iterator<TaskTranslation> iterator = list.iterator();
            while (iterator.hasNext()) {
                TaskTranslation translation = iterator.next();
                buffer.append(Long.toString(translation.getTaskId()));
                buffer.append(",");
                buffer.append("\""+translation.getCSVFormattedOriginalText()+"\"");
                buffer.append(",");
                buffer.append(",");
                buffer.append("\n");

            }
        }
        return buffer.toString();
    }


    public static List<Map> pullAllTranslationResponses (Long clientAppId, Long twbProjectId) {
        final String url=BASE_URL+"/orders?delivery_status=to_accept";
        HttpHeaders requestHeaders=new HttpHeaders();
        requestHeaders.add("X-Proz-API-Key", API_KEY);
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Map>(requestHeaders), Map.class);
            logger.debug(response.getBody().toString());
            return (List<Map>)response.getBody().get("data");
        } catch (HttpClientErrorException exception) {
            logger.debug("Exception caught: " +exception.getResponseBodyAsString());
        }
        return null;


    }



    public static List<TranslationProjectModel> pullTranslationProjects(String clientId) {
        final String url=BASE_URL+"/projects?client="+clientId;
        HttpHeaders requestHeaders=new HttpHeaders();
        requestHeaders.add("X-Proz-API-Key", API_KEY);
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        try {
            ResponseEntity<TranslationProjectModel[]> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), TranslationProjectModel[].class);
            logger.debug(response);
            TranslationProjectModel[] projectArray = response.getBody();
            ArrayList<TranslationProjectModel> list = new ArrayList<TranslationProjectModel>(Arrays.asList(projectArray));
            return list;
        } catch (HttpClientErrorException exception) {
            logger.debug("Exception caught: " +exception.getResponseBodyAsString());
        }
        return null;
    }

    public static String getTranslationDocumentContent(String download_link) throws Exception {
        final String url=download_link;
        HttpHeaders requestHeaders=new HttpHeaders();
        requestHeaders.add("X-Proz-API-Key", API_KEY);
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(requestHeaders), String.class);
        logger.debug(response.getBody());
        return response.getBody();
    }

    //temporary for testing
    public static String pullTranslationProjectsAsString(String clientId) {
        final String url=BASE_URL+"/projects?client="+clientId;
        HttpHeaders requestHeaders=new HttpHeaders();
        requestHeaders.add("X-Proz-API-Key", API_KEY);
        RestTemplate restTemplate=new RestTemplate();


        ResponseEntity<String> response=restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), String.class);
        logger.debug(response);
        return response.getBody();
    }

    /*
    private static void generateCsvFile(String sFileName, List<TaskTranslation> list)
    {
        String result = null;
        try
        {
            File file = new File(sFileName);
            file.createNewFile();
            String data = getCSVData(list);
            FileWriter writer = new FileWriter(sFileName);
            writer.append(data);
            writer.flush();
            writer.close();


        }
        catch(IOException e)
        {
            logger.error("Error create translation csv file", e);
        }
    }
*/


}
