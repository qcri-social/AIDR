package qa.qcri.aidr.trainer.pybossa.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.common.logging.ErrorLog;
import qa.qcri.aidr.trainer.pybossa.service.ClientAppCreateWorker;
import qa.qcri.aidr.trainer.pybossa.service.ClientAppRunWorker;
import qa.qcri.aidr.trainer.pybossa.service.MicroMapperWorker;
import qa.qcri.aidr.trainer.pybossa.service.Worker;



/**
 * An asynchronous worker
 */
@Component("asyncWorker")
public class AsyncWorker implements Worker {

	private static Logger logger = Logger.getLogger(AsyncWorker.class);
	private static ErrorLog elog = new ErrorLog();
	/**
     * @Component("asyncWorker")
	 * This method will be wrapped in a proxy so that the method is 
	 * actually invoked by a TaskExecutor instance
     *
     */

    @Autowired
    private ClientAppRunWorker clientAppRunWorker;

    @Autowired
    ClientAppCreateWorker pybossaWorker;

    @Autowired
    private MicroMapperWorker microMapperWorker;


    @Async
	public void work() {
		String threadName = Thread.currentThread().getName();
		//logger.debug("   " + threadName + " has began working.(DO PUBLISHING)");

        try {
          //  microMapperWorker.processTaskPublish();
         //   microMapperWorker.processTaskImport();
        //	logger.debug("****************do publishing and importing working**************");
            Thread.sleep(180000); // simulates work
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        catch (Exception e) {
            Thread.currentThread().interrupt();
        }
       // logger.debug("   " + threadName + " has completed work.(AsyncWorker - run doCreateApp)");
	}
}
