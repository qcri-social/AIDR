package qa.qcri.aidr.output.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class JsonDateDeSerializer  extends JsonDeserializer<Date>{

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		 String date = jp.getText();
		 if(date!= null && !date.equals("")){
		       try {
		            return dateFormat.parse(date);
		         } catch (ParseException e) {
		            throw new RuntimeException(e);
		         }
		 }
		 return null;
  
	}

}
