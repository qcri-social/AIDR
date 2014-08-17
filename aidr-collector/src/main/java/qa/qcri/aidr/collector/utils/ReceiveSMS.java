/**
 * Created with IntelliJ IDEA.
 * User: mjr
 * Date: 27.07.14
 * Time: 10:53
 */
package qa.qcri.aidr.collector.utils;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import qa.qcri.aidr.collector.beans.SMS;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReceiveSMS {
    public static void main(String[] args) throws IOException {
        String url = null;
        String collectionCode = null;
        String file = null;

        if (args.length == 0) {
            throw new IllegalArgumentException("Argument not found, please restart application with arguments -url={url} -collection-code={id} -file={path_to_json_file}");
        }

        for (String arg : args) {
            if (arg.startsWith("-url"))
                url = arg.replaceFirst(".*?=(.*)","$1");
            else if (arg.startsWith("-collection-code"))
                collectionCode = arg.replaceFirst(".*?=(.*)","$1");
            else if (arg.startsWith("-file"))
                file = arg.replaceFirst(".*?=(.*)","$1");
        }

        if(StringUtils.isEmpty(url))
            throw new IllegalArgumentException("Argument '-url' not defined.");
        if(StringUtils.isEmpty(collectionCode))
            throw new IllegalArgumentException("Argument '-collection-code' not defined.");
        if(StringUtils.isEmpty(file))
            throw new IllegalArgumentException("Argument '-file' not defined.");

        File json = new File(file);
        if(!json.exists())
            throw new IllegalArgumentException("File not found by path: " + json.getCanonicalPath());

        ObjectMapper objectMapper = new ObjectMapper();
        List<SMS> sms = objectMapper.readValue(json, TypeFactory.parametricType(List.class, SMS.class));

        String uri = url + "/webresources/sms/endpoint/receive/" + collectionCode;

        System.out.println("Read " + sms.size() + " sms from file.");
        System.out.println("Send sms to url: " + uri);

        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        WebTarget webResource = client.target(uri);

        List<Response> responses = new ArrayList<Response>(sms.size());
        for (SMS message : sms) {
            Response response = webResource.request(MediaType.APPLICATION_JSON).post(Entity.entity(message, MediaType.APPLICATION_JSON));
            responses.add(response);
        }

        System.out.println("Application receive " + responses.size() + " responses from server.");
    }
}
