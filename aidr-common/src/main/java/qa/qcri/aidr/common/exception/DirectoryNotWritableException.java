package qa.qcri.aidr.common.exception;

public class DirectoryNotWritableException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8924441840552052829L;

	public DirectoryNotWritableException(String propertyName,
			String directoryLocation) {
		super(
				propertyName
						+ " = "
						+ directoryLocation
						+ " is not writable. Please verify if this is a valid writable directory.");
	}
}
