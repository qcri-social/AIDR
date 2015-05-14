package qa.qcri.aidr.trainer.pybossa.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.trainer.pybossa.service.Worker;

/**
 * An asynchronous worker
 * 
 * TODO: delete this deprecated class.
 * 
 */
@Deprecated
@Component("asyncWorker")
public class AsyncWorker implements Worker {

	private static Logger logger = Logger.getLogger(AsyncWorker.class);

	/**
     * @Component("asyncWorker")
	 * This method will be wrapped in a proxy so that the method is 
	 * actually invoked by a TaskExecutor instance
     *
     */
	public void work() {
    }
}
