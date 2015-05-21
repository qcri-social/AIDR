package qa.qcri.aidr.common.exception;

public class ConfigurationPropertyFileException extends RuntimeException {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -1192943457771876063L;

	public ConfigurationPropertyFileException(String fileName, Throwable cause) {
		super(
				"File"
						+ fileName
						+ "  cannot be read. Please verify if this is a valid configuration file.",
				cause);
	}

}
