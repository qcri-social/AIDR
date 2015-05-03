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
	
	@Override
	public MyClass readValue(String jsonString) throws IOException {
		return mapper.readValue(jsonString, MyClass.class);
	}
	
	@Test
	public void testGetObjectMapper() {
		super.testGetObjectMapper();
	}
}
