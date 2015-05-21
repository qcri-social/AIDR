package qa.qcri.aidr.persister.filter;

public enum ComparatorType {
	is_before, 
	is_after,
	is,
	is_not,
	has_confidence;
	
	ComparatorType() {}
}
