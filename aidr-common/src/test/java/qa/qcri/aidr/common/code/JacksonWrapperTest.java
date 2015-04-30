package qa.qcri.aidr.common.code;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class JacksonWrapperTest extends JSONWrapperTest {

	ObjectMapper mapper = null;

	@Before
	public void setUp() throws Exception {
		mapper = JacksonWrapper.getObjectMapper();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public MyClass readValue(String jsonString, Class aClass) throws IOException {
		return mapper.readValue(jsonString, aClass);
	}
	
	@Test
	public void testGetObjectMapper() {
		super.testGetObjectMapper();
	}
}
