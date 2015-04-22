package qa.qcri.aidr.trainer.pybossa.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import qa.qcri.aidr.trainer.pybossa.service.ClientAppCreateWorker;
import qa.qcri.aidr.trainer.pybossa.service.ClientAppRunWorker;
import qa.qcri.aidr.trainer.pybossa.service.MicroMapperWorker;
import qa.qcri.aidr.trainer.pybossa.service.Worker;



/**
 * A synchronous worker
 */
@Component("syncWorker")
public class SyncWorker implements Worker {

	protected static Logger logger = Logger.getLogger(SyncWorker.class);

	
    @Autowired
    ClientAppCreateWorker pybossaWorker;

    @Autowired
    private ClientAppRunWorker clientAppRunWorker;

    @Autowired
    private MicroMapperWorker microMapperWorker;

	public void work() {
		String threadName = Thread.currentThread().getName();

        logger.info("Scheduler is starting");
        try {

            pybossaWorker.doCreateApp();
            clientAppRunWorker.processTaskPublish();
            clientAppRunWorker.processTaskRunImport();

            microMapperWorker.processTaskExport();

            Thread.sleep(600000); // simulates work

        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        logger.info("Scheduler is going sleep");
    }
	
}
