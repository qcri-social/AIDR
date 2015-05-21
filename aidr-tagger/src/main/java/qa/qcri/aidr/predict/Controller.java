package qa.qcri.aidr.predict;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import qa.qcri.aidr.predict.classification.LabelingTaskWriter;
import qa.qcri.aidr.predict.classification.nominal.ModelController;
import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;
import qa.qcri.aidr.predict.communication.*;
import qa.qcri.aidr.predict.featureextraction.FeatureExtractor;
import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.common.redis.LoadShedder;

/**
 * Controller is the main entrypoint of AIDR. It starts all subprocesses. In the
 * future it should makes sure the different processes are running and restart
 * them if necessary. Each subprocess is running in a separate thread.
 * 
 * @author jrogstadius
 */
public class Controller  {

    static HttpInputManager httpInputManager;
    static AidrFetcherJsonInputProcessor aidrInputProcessor;
    static HttpOutputManager outputManager;
    static OutputMatcher outputMatcher;
    static FeatureExtractor featureExtractor;
    static ModelController modelController;
    static LabelingTaskWriter labelingTaskWriter;
    
    static ArrayList<Thread> workers = new ArrayList<Thread>();

    // Debugging
 	private static Logger logger = Logger.getLogger(Controller.class);
 	private static ErrorLog elog = new ErrorLog();
    
    public static void main(String[] args) {
    	
		// Initializing the properties enumeration.
		TaggerConfigurator.getInstance().initProperties(
				TaggerConfigurator.configLoadFileName,
				TaggerConfigurationProperty.values());

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                for (Thread t : workers)
                    t.interrupt();
            }
        }));
        
        DataStore.initDBPools();
        DataStore.clearRedisPipeline();
        DataStore.initTaskManager();
        
        httpInputManager = new HttpInputManager();
        outputManager = new HttpOutputManager();
        outputMatcher = new OutputMatcher();

        aidrInputProcessor = new AidrFetcherJsonInputProcessor();
        if (null == AidrFetcherJsonInputProcessor.redisLoadShedder) {
        	AidrFetcherJsonInputProcessor.redisLoadShedder = new ConcurrentHashMap<String, LoadShedder>(20);
        }
		aidrInputProcessor.inputQueueName = TaggerConfigurator.getInstance()
				.getProperty(TaggerConfigurationProperty.REDIS_INPUT_CHANNEL);
		aidrInputProcessor.outputQueueName = TaggerConfigurator.getInstance()
				.getProperty(
						TaggerConfigurationProperty.REDIS_FOR_EXTRACTION_QUEUE);

        featureExtractor = new FeatureExtractor();
		featureExtractor.inputQueueName = TaggerConfigurator
				.getInstance()
				.getProperty(
						TaggerConfigurationProperty.REDIS_FOR_EXTRACTION_QUEUE)
				.getBytes();
		featureExtractor.outputQueueName = TaggerConfigurator
				.getInstance()
				.getProperty(
						TaggerConfigurationProperty.REDIS_FOR_CLASSIFICATION_QUEUE)
				.getBytes();

        modelController = new ModelController();
		modelController.inputQueueName = TaggerConfigurator
				.getInstance()
				.getProperty(
						TaggerConfigurationProperty.REDIS_FOR_CLASSIFICATION_QUEUE)
				.getBytes();
		modelController.outputQueueName = TaggerConfigurator
				.getInstance()
				.getProperty(TaggerConfigurationProperty.REDIS_FOR_OUTPUT_QUEUE)
				.getBytes();
        modelController.initialize();

        labelingTaskWriter = new LabelingTaskWriter();
		labelingTaskWriter.inputQueueName = TaggerConfigurator
				.getInstance()
				.getProperty(
						TaggerConfigurationProperty.REDIS_LABEL_TASK_WRITE_QUEUE)
				.getBytes();

        startProcess(aidrInputProcessor);
        startProcess(httpInputManager);
        startProcess(featureExtractor);
        startProcess(modelController);
        startProcess(outputManager);
        startProcess(outputMatcher);
        startProcess(labelingTaskWriter);

        while (true) {
            if (Thread.interrupted())
                return;

            System.out.print("extractor: "
                    + (int) featureExtractor.getCurrentItemsPerSecond() + "/"
                    + (int) featureExtractor.getMaxItemsPerSecond());
            logger.info("extractor: "
                    + (int) featureExtractor.getCurrentItemsPerSecond() + "/"
                    + (int) featureExtractor.getMaxItemsPerSecond());
            
            System.out.print(", classifier: "
                    + (int) modelController.getCurrentItemsPerSecond() + "/"
                    + (int) modelController.getMaxItemsPerSecond());
            logger.info(", classifier: "
                    + (int) modelController.getCurrentItemsPerSecond() + "/"
                    + (int) modelController.getMaxItemsPerSecond());
            
            System.out.print(", taskwriter: "
                    + (int) labelingTaskWriter.getCurrentItemsPerSecond() + "/"
                    + (int) labelingTaskWriter.getMaxItemsPerSecond());
            logger.info(", taskwriter: "
                    + (int) labelingTaskWriter.getCurrentItemsPerSecond() + "/"
                    + (int) labelingTaskWriter.getMaxItemsPerSecond());
            
            System.out.print(" | extractor: " + (int) featureExtractor.inputCount
                    + "->" + (int) featureExtractor.outputCount);
            logger.info(" | extractor: " + (int) featureExtractor.inputCount
                    + "->" + (int) featureExtractor.outputCount);
            
            System.out.print(", classifier: " + (int) modelController.inputCount + "->"
                    + (int) modelController.outputCount);
            logger.info(", classifier: " + (int) modelController.inputCount + "->"
                    + (int) modelController.outputCount);
                    
            System.out.print(", taskwriter: " + (int) labelingTaskWriter.inputCount
                    + "->" + (int) labelingTaskWriter.writeCount);
            logger.info(", taskwriter: " + (int) labelingTaskWriter.inputCount
                    + "->" + (int) labelingTaskWriter.writeCount);
            
            System.out.println(", output: " + (int) outputMatcher.outputCount);
            logger.info(", output: " + (int) outputMatcher.outputCount);
            
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("Exception in starting tagger Controller!");
                logger.error(elog.toStringException(e));
            }
        }
    }

    static Thread startProcess(Runnable process) {
        Thread t = new Thread(process);
        workers.add(t);
        t.setName(process.getClass().getSimpleName());
        t.start();
        return t;
    }
}
