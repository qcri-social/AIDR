package qa.qcri.aidr.task.common;

public enum TrainingDataFetchType {
	BATCH_FETCH("BATCH_FETCH"), INTERNAL_TRAINING("INTERNAL_TRAINING");

	private String value;

	private TrainingDataFetchType(String value) {
		this.value = value;
	}
	
	private String getValue() {
		return this.value;
	}
	
	private void setValue(String value) {
		this.value = value;
	}

}
