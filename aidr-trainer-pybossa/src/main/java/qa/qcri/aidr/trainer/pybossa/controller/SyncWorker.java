package qa.qcri.aidr.trainer.pybossa.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import qa.qcri.aidr.trainer.pybossa.service.ClientAppCreateWorker;
import qa.qcri.aidr.trainer.pybossa.service.ClientAppRunWorker;
import qa.qcri.aidr.trainer.pybossa.service.MicroMapperWorker;
import qa.qcri.aidr.trainer.pybossa.service.Worker;
import qa.qcri.aidr.trainer.pybossa.store.StatusCodeType;

import java.util.Calendar;
import java.util.Date;


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
		String threadName = "aidr_trainer_pyboss_" + Thread.currentThread().getName();
        Calendar cal = Calendar.getInstance();
        System.out.println("************************************ Scheduler is starting : " + threadName + " - " + new Date());
        try {

            microMapperWorker.processTaskExport();
            clientAppRunWorker.processTaskRunImport();
            clientAppRunWorker.processTaskPublish();
            pybossaWorker.doCreateApp();

            int hour = cal.get(Calendar.HOUR_OF_DAY) ;
            if(hour == StatusCodeType.CLIENT_APP_DELETION_TIME){
                pybossaWorker.doAppDelete();
            }

        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("************************************ Scheduler is going sleep : "  + new Date());
    }
	
}
