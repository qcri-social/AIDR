package qa.qcri.aidr.common.code;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FasterXmlWrapperTest extends JSONWrapperTest {

	ObjectMapper mapper = null;

	@Before
	public void setUp() throws Exception {
		mapper = FasterXmlWrapper.getObjectMapper();
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
