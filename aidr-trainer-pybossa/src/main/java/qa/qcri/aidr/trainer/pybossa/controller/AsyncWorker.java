package qa.qcri.aidr.trainer.pybossa.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import qa.qcri.aidr.trainer.pybossa.service.ClientAppCreateWorker;
import qa.qcri.aidr.trainer.pybossa.service.ClientAppRunWorker;
import qa.qcri.aidr.trainer.pybossa.service.MicroMapperWorker;
import qa.qcri.aidr.trainer.pybossa.service.Worker;



/**
 * An asynchronous worker
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
