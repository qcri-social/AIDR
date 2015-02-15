package qa.qcri.aidr.manager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemProperties.class);
    private static final Properties PROPERTIES = new Properties();

    private static final String SYSTEM_PROPERTIES = "system.properties";

    static {
        initProperties();
    }

    private static void initProperties() {
        try (InputStream input = SystemProperties.class.getClassLoader().getResourceAsStream(SYSTEM_PROPERTIES);){
            PROPERTIES.load(input);
        } catch (IOException e) {
            LOGGER.error("Error in reading properties file: " + SYSTEM_PROPERTIES, e);
        }
    }

    public static final String getProperty(final String key) {
        return PROPERTIES.getProperty(key);
    }
}

