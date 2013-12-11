package qa.qcri.aidr.predict.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Contains common constants used throughout the system.
 * 
 * @author jrogstadius
 */
public class Config extends Loggable {

    private static String className = "Config";
    public static final LogLevel LOG_LEVEL;
    public static final String LOG_FILE_NAME;
    public static final int HTTP_INPUT_PORT;
    public static final int HTTP_OUTPUT_PORT;
    public static final String REDIS_HOST;
    public static final String REDIS_FROM_FETCHER_CHANNEL;
    public static final String REDIS_OUTPUT_CHANNEL_PREFIX;
    public static final String REDIS_FOR_EXTRACTION_QUEUE;
    public static final String REDIS_FOR_CLASSIFICATION_QUEUE;
    public static final String REDIS_FOR_OUTPUT_QUEUE;
    public static final String REDIS_TRAINING_SAMPLE_INFO_QUEUE;
    public static final String REDIS_NEXT_MODEL_ID;
    public static final String REDIS_LABEL_TASK_WRITE_QUEUE;
    public static final int LABELING_TASK_BUFFER_MAX_LENGTH;
    public static final String MODEL_STORE_PATH;
    public static final String MYSQL_USERNAME;
    public static final String MYSQL_PASSWORD;
    public static final String MYSQL_PATH;
    public static final String NOMINAL_ATTRIBUTE_NULL_VALUE;
    public static final int MAX_TASK_WRITE_FQ_MS;
    public static final int MAX_NEW_TASKS_PER_MINUTE;

    static {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("config/config.txt"));
        } catch (IOException ex) {
            log(className, "Exception when initializing " + className, ex);
            throw new RuntimeException(ex);
        }

        LOG_FILE_NAME = prop.getProperty("log_file_name");
        LOG_LEVEL = LogLevel.valueOf(prop.getProperty("log_level"));

        String modelsPath = prop.getProperty("model_store_path");
        if (!modelsPath.endsWith(File.separator)) {
            modelsPath += File.separator;
        }
        MODEL_STORE_PATH = modelsPath; 

        MYSQL_PATH = prop.getProperty("mysql_path");
        MYSQL_USERNAME = prop.getProperty("mysql_username");
        MYSQL_PASSWORD = prop.getProperty("mysql_password");
        NOMINAL_ATTRIBUTE_NULL_VALUE = prop
                .getProperty("nominal_attribute_null_value");

        HTTP_INPUT_PORT = Integer.parseInt(prop.getProperty("http_input_port"));
        HTTP_OUTPUT_PORT = Integer.parseInt(prop
                .getProperty("http_output_port"));

        REDIS_HOST = prop.getProperty("redis_host");
        REDIS_FROM_FETCHER_CHANNEL = prop.getProperty("redis_input_channel");
        REDIS_OUTPUT_CHANNEL_PREFIX = prop
                .getProperty("redis_output_channel_prefix");
        REDIS_FOR_EXTRACTION_QUEUE = prop
                .getProperty("redis_for_extraction_queue");
        REDIS_FOR_CLASSIFICATION_QUEUE = prop
                .getProperty("redis_for_classification_queue");
        REDIS_FOR_OUTPUT_QUEUE = prop.getProperty("redis_for_output_queue");
        REDIS_TRAINING_SAMPLE_INFO_QUEUE = prop
                .getProperty("redis_training_sample_info_queue");
        REDIS_NEXT_MODEL_ID = prop.getProperty("redis_next_model_id");
        REDIS_LABEL_TASK_WRITE_QUEUE = prop
                .getProperty("redis_label_task_write_queue");

        LABELING_TASK_BUFFER_MAX_LENGTH = Integer.parseInt(prop
                .getProperty("labeling_task_buffer_max_length"));
        MAX_TASK_WRITE_FQ_MS = Integer.parseInt(prop
                .getProperty("max_task_write_fq_ms"));
        MAX_NEW_TASKS_PER_MINUTE = Integer.parseInt(prop
                .getProperty("max_new_tasks_per_minute"));
    }
}
