package qa.qcri.aidr.common.exception;

public class ConfigurationPropertyNotSetException extends RuntimeException {

	/**
	 * Generated Serial Version ID.
	 */
	private static final long serialVersionUID = -3865562168278567988L;

	/**
	 * 
	 * @param propertyName
	 *            Name of the property which is not recognized
	 * @param fileName
	 *            Name of the file where the property was read from
	 * @param cause
	 *            underlying cause
	 */
	public ConfigurationPropertyNotSetException(String propertyName,
			String fileName) {
		super(
				"Property : "
						+ propertyName
						+ " in file: "
						+ fileName
						+ " is not set. Please verify if this is a valid configuration.");
	}
}
