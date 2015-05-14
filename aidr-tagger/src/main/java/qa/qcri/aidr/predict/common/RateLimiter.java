package qa.qcri.aidr.predict.common;

import java.util.LinkedList;

/**
 * Rate limits an actions.
 * 
 * TODO: REMOVE and replace with the LoadShedder in aidr-common, which provides the same functionality.
 * 
 * @author jrogstadius
 */
public class RateLimiter {
    LinkedList<Long> timestamps = new LinkedList<Long>();
    public int maxItemsPerMinute = 100;		

    public RateLimiter(int maxItemsPerMinute) {
        this.maxItemsPerMinute = maxItemsPerMinute;
    }

    public void logEvent() {
        timestamps.addLast(System.currentTimeMillis());
        if (timestamps.size() > maxItemsPerMinute)
            timestamps.removeFirst();
    }

    public boolean isLimited() {
        boolean hasEnoughItems = timestamps.size() > maxItemsPerMinute;
        if (!hasEnoughItems)
            return false;

        long ageOfOldestItem = System.currentTimeMillis()
                - timestamps.peekFirst();
        boolean oldestItemIsRecent = ageOfOldestItem < 60000;

        return hasEnoughItems && oldestItemIsRecent;
    }
}