package qa.qcri.aidr.predict.communication;

import qa.qcri.aidr.predict.common.*;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * OutputWorkerIndex is a collection of OutputWorkers organized by eventID.
 *
 * @author jrogstadius
 */
public class HttpOutputWorkerIndex extends Loggable {

    private static HttpOutputWorkerIndex instance;
    HashMap<String, ArrayList<HttpOutputWorker>> workers;

    private static Logger logger = Logger.getLogger(HttpOutputWorkerIndex.class);
    		
    private HttpOutputWorkerIndex() {
        workers = new HashMap<String, ArrayList<HttpOutputWorker>>();
    }

    public static HttpOutputWorkerIndex getInstance() {
        if (instance == null)
            instance = new HttpOutputWorkerIndex();
        return instance;
    }

//    public void addWorker(int eventID, HttpOutputWorker worker) {
//        synchronized (this) {
//            if (!workers.containsKey(eventID))
//                workers.put(cri, new ArrayList<HttpOutputWorker>());
//            workers.get(eventID).add(worker);
//
//            worker.onConnectionClosed
//                    .subscribe(new Function<EventArgs<Object>>() {
//                        public void execute(EventArgs<Object> args) {
//                            removeWorker((HttpOutputWorker) args.sender);
//                        }
//                    });
//        }
//    }

    public void addWorker(String crisisCode, HttpOutputWorker worker) {
        synchronized (this) {
            if (!workers.containsKey(crisisCode))
                workers.put(crisisCode, new ArrayList<HttpOutputWorker>());
            workers.get(crisisCode).add(worker);

            worker.onConnectionClosed
                    .subscribe(new Function<EventArgs<Object>>() {
                        public void execute(EventArgs<Object> args) {
                            removeWorker((HttpOutputWorker) args.sender);
                        }
                    });
        }
    }


    public void removeWorker(HttpOutputWorker worker) {
        logger.info("Removing worker from index");

        synchronized (this) {
            if (!workers.containsKey(worker.filter.crisisCode))
                return;
            ArrayList<HttpOutputWorker> list = workers
                    .get(worker.filter.crisisCode);
            list.remove(worker);
            if (list.isEmpty())
                workers.remove(worker.filter.crisisCode);
        }
    }

    public List<HttpOutputWorker> getWorkers(String crisisCode) {
        synchronized (this) {
            if (workers.containsKey(crisisCode))
                return new ArrayList<HttpOutputWorker>(workers.get(crisisCode));
            else
                return new ArrayList<HttpOutputWorker>();
        }
    }
}
