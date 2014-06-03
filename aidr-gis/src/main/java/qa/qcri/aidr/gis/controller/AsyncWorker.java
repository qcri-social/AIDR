package qa.qcri.aidr.gis.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import qa.qcri.aidr.gis.service.Worker;


/**
 * An asynchronous worker
 */
@Component("asyncWorker")
public class AsyncWorker implements Worker {

	protected static Logger logger = Logger.getLogger("service");
	/**
     * @Component("asyncWorker")
	 * This method will be wrapped in a proxy so that the method is 
	 * actually invoked by a TaskExecutor instance
     *
     */

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
