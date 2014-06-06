package qa.qcri.aidr.gis.service.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import qa.qcri.aidr.gis.service.Worker;

/**
 * Scheduler for handling jobs
 */
@Service
public class SchedulerService {

	protected static Logger logger = Logger.getLogger("service");

	@Autowired
	@Qualifier("syncWorker")
	private Worker syncWorker;

	@Scheduled(cron="0 0/30 * * * ?")
   // @Scheduled(cron="0/3 * * * * ?")
    public void doSchedule() {

        syncWorker.work();

	}
	

}
