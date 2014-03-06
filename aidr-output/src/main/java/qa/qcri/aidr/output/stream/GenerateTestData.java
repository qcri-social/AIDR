package qa.qcri.aidr.output.stream;

public class GenerateTestData {
	private int sendCount;
	private static int MAX_COUNT = 100;
	
	public GenerateTestData() {
		sendCount = 0;
	}
	
	public GenerateTestData(final int MAX) {
		MAX_COUNT = MAX;
	}
	
	public String getNextJsonString() {
    	if (sendCount < MAX_COUNT) {
    		StringBuilder str = new StringBuilder();
    		str.append("{").append("\"count\":").append(Integer.toString(sendCount)).append("}");
    		
    		++sendCount;
    		return str.toString();
    	}
    	return null;
    }
}
