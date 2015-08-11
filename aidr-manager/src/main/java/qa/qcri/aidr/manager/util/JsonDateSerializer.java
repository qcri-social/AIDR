package qa.qcri.aidr.manager.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class JsonDateSerializer extends JsonSerializer<Date> {
	private static Logger logger = Logger.getLogger(JsonDateSerializer.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException
             {
        String formattedDate = null;
        if (date != null) {
            formattedDate = dateFormat.format(date);
        }
        try {
			jsonGenerator.writeString(formattedDate);
		} catch (IOException e) {
			logger.error("Exception while generating the json for the string:"+formattedDate, e);
			throw e;
		}
    }
}
