package qa.qcri.aidr.trainer.pybossa.service.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import qa.qcri.aidr.trainer.pybossa.service.Worker;

/**
 * Scheduler for handling jobs
 */
@Service
public class SchedulerService {

	protected static Logger logger = Logger.getLogger("service");

	@Autowired
	@Qualifier("syncWorker")
	private Worker syncWorker;

    //@Autowired
   // @Qualifier("asyncWorker")
    //private Worker asyncWorker;
	
    //set 5 sec. for testing. update to 5min later
    //"0 0/5 * * * ?"
	@Scheduled(cron="0/3 * * * * ?")
	public void doSchedule() {
	//	logger.debug("Start schedule");

        syncWorker.work();
       // asyncWorker.work();

	//	logger.debug("End schedule");
	}
	

}
