package qa.qcri.aidr.common.log;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/3/14
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */

import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;

public final class LogCollection {

    private static final String indents = ".......................";
    private static final Logger logger = Logger.getLogger(LogCollection.class);

    private static void logEntry(Object key, Object value, int indent)
    {
        // Add indents based on recursion depth and add the key.
        StringBuffer entry = new StringBuffer();
        entry.append(indents.substring(0,indent));
        entry.append(key).append("=");

        // Add the value type and, for common types, the actual value.
        if (value == null)
            entry.append("null");
        else {
            String type = value.getClass().getName();
            entry.append(type);

            if (type.equals("String") ||
                    type.equals("Integer") ||
                    type.equals("Long") ||
                    type.equals("BigDecimal"))
                entry.append(" (").append(value).append(")");
        }
        logger.info(entry.toString());
    }

    public static void logList(List list, String name) {
        logger.info("=== List " + name);
        logList(list, 0);
    }

    private static void logList(List list, int indent){
        int i = 0;
        for (Object value : list) {
            // For a list, the key is the index.
            String key = String.valueOf(i);
            logEntry(key, value, indent);

            // Recurse if this entry is a Map or List.
            if (value instanceof Map)
                logMap((Map)value, indent + 1);
            if (value instanceof List)
                logList((List)value, indent + 1);
        }
    }

    public static void logMap(Map map, String name) {
        logger.info("=== Map " + name);
        logMap(map, 0);
    }

    private static void logMap(Map map, int indent){
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            logEntry(key, value, indent);

            if (value instanceof Map)
                logMap((Map)value, indent + 1);
            if (value instanceof List)
                logList((List)value, indent + 1);
        }
    }

}


