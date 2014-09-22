package qa.qcri.aidr.common.values;

public enum ReturnCode {
	FAIL(-1), SUCCESS(1);
	
	int value;
	
	ReturnCode(int value) {
		this.value = value;
	}
}
