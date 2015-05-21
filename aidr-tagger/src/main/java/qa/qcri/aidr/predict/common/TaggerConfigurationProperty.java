package qa.qcri.aidr.predict.common;

import qa.qcri.aidr.common.code.ConfigurationProperty;

public enum TaggerConfigurationProperty implements ConfigurationProperty {

	MYSQL_PATH("mysql_path"), MYSQL_USERNAME("mysql_username"), MYSQL_PASSWORD(
			"mysql_password"), REDIS_HOST("redis_host"), LOG_FILE_NAME(
			"log_file_name"), LOG_LEVEL("log_level"), MODEL_STORE_PATH(
			"model_store_path"), HTTP_INPUT_PORT("http_input_port"), HTTP_OUTPUT_PORT(
			"http_output_port"), REDIS_INPUT_CHANNEL("redis_input_channel"), REDIS_OUTPUT_CHANNEL_PREFIX(
			"redis_output_channel_prefix"), REDIS_FOR_EXTRACTION_QUEUE(
			"redis_for_extraction_queue"), REDIS_FOR_CLASSIFICATION_QUEUE(
			"redis_for_classification_queue"), REDIS_FOR_OUTPUT_QUEUE(
			"redis_for_output_queue"), REDIS_TRAINING_SAMPLE_INFO_QUEUE(
			"redis_training_sample_info_queue"), REDIS_NEXT_MODEL_ID(
			"redis_next_model_id"), REDIS_LABEL_TASK_WRITE_QUEUE(
			"redis_label_task_write_queue"), LABELLING_TASK_BUFFER_MAX_LENGTH(
			"labeling_task_buffer_max_length"), MAX_TASK_WRITE_FQ_MS(
			"max_task_write_fq_ms"), MAX_NEW_TASKS_PER_MINUTE(
			"max_new_tasks_per_minute"), MIN_TRUNCATE_INTERVAL_MS(
			"min_truncate_interval_ms"), TRUNCATE_RUN_INTERVAL_MS(
			"truncate_run_interval_ms"), NOMINAL_ATTRIBUTE_NULL_VALUE(
			"nominal_attribute_null_value"), SAMPLE_COUNT_THRESHOLD(
			"sampleCountThreshold"), PERSISTER_LOAD_LIMIT(
			"persister_load_limit"), PERSISTER_LOAD_CHECK_INTERVAL_MINUTES(
			"persister_load_check_interval_minutes"), LOG_INTERVAL_MINUTES(
			"LOG_INTERVAL_MINUTES"), REMOTE_TASK_MANAGER_JNDI_NAME(
			"REMOTE_TASK_MANAGER_JNDI_NAME"), PERFORMANCE_IMPROVEMENT_MARGIN(
			"PERFORMANCE_IMPROVEMENT_MARGIN"), TRAINING_EXAMPLES_FORCE_RETRAIN(
			"TRAINING_EXAMPLES_FORCE_RETRAIN");

	private final String configurationProperty;

	private TaggerConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
