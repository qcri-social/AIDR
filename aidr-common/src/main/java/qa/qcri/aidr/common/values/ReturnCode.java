package qa.qcri.aidr.common.values;

/**
 * Generic return codes: ERROR, WARNING, and SUCCESS.
 * 
 * TODO: Remove dead code that has been commented.
 * 
 */
public enum ReturnCode {
	/**
	 * Indicates there is a problem that should stop the execution of the following instructions. 
	 */
	ERROR,
	/**
	 * Indicates there is a problem, but execution should continue.
	 */
	WARNING,
	/**
	 * Indicates the absence of a problem.
	 */
	SUCCESS;
	// ERROR(-1), WARNING(0), SUCCESS(1);

	// int value;

	// ReturnCode(int value) {
	// this.value = value;
	// }
}
