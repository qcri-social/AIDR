package qa.qcri.aidr.predict.common;

/**
 * Helper class for error logging.
 * 
 * @author jrogstadius & Imran
 */
public abstract class Loggable {
    public enum LogLevel {
        INFO(1), WARNING(2), ERROR(3);

        private int value;

        private LogLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /* Static methods */
    private static RateLimiter globalRateLimiter = new RateLimiter(10);

    protected static void log(LogLevel messageLevel, String sourceName,
            String message) {
        if (globalRateLimiter.isLimited())
            return;

        if (messageLevel.getValue() >= Config.LOG_LEVEL.getValue())
            ErrorLog.Print(sourceName, message);

        globalRateLimiter.logEvent();
    }

    protected static void log(String sourceName, String message, Exception error) {
        if (globalRateLimiter.isLimited())
            return;

        ErrorLog.Print(sourceName, message, error);

        globalRateLimiter.logEvent();
    }

    /* Instance methods */
    private final String name = this.getClass().getName();
    private static long logObjID = 0;
    protected long objID = logObjID++;
    private RateLimiter instanceRateLimiter = new RateLimiter(10);
    int rateLimitCount = 0;

    protected void log(LogLevel messageLevel, String message) {
        if (instanceRateLimiter.isLimited()) {
            rateLimitCount++;
            return;
        }

        writeFloodStatus();
        if (messageLevel.getValue() >= Config.LOG_LEVEL.getValue())
            ErrorLog.Print(name + "#" + objID, message);

        instanceRateLimiter.logEvent();
    }

    protected void log(String message, Exception error) {
        if (instanceRateLimiter.isLimited()) {
            rateLimitCount++;
            return;
        }

        writeFloodStatus();
        ErrorLog.Print(name + "#" + objID, message, error);

        instanceRateLimiter.logEvent();
    }

    void writeFloodStatus() {
        if (rateLimitCount > 0)
            ErrorLog.Print(name, "Log rate limited: " + rateLimitCount);
        rateLimitCount = 0;
    }

    protected void setMaxLogWritesPerMinute(int limit) {
        instanceRateLimiter.maxItemsPerMinute = limit;
    }
}
