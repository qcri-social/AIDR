package qa.qcri.aidr.common.log;

import org.apache.log4j.Level;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/3/14
 * To change this template use File | Settings | File Templates.
 */
public final class ItemType implements Serializable {

    private static final long serialVersionUID = -8557763869968102495L;

    public static final ItemType ERROR		= new ItemType("Error", "Error", "E", Level.ERROR);

    public static final ItemType WARNING	= new ItemType("Warning", "Warning", "w", Level.WARN);

    public static final ItemType INFO		= new ItemType("Info", "Info", " ", Level.INFO);

    private static final List<ItemType> selectors = new ArrayList<ItemType>(
            Arrays.asList(
                    ERROR,
                    WARNING,
                    INFO
            )
    );

    private transient Level log4jLevel;

    private transient String shortLabel;

    private ItemType(String name, String label, String shortLabel, Level level) {
        this.shortLabel = shortLabel;
        log4jLevel		= level;
    }

    public static Enumeration<ItemType> getEnumeration() {
        return Collections.enumeration(selectors);
    }

    public Level getLog4jLevel() {
        return log4jLevel;
    }

    public String getShortLabel() {
        return shortLabel;
    }



    public static ItemType selectorFromLog4jLevel(Level level) throws NoSuchElementException {
        if (level == null) throw new NullPointerException("Null: level.");

        final ItemType type;

        if		(level.equals(Level.DEBUG))	type = INFO;
        else if (level.equals(Level.ERROR))	type = ERROR;
        else if (level.equals(Level.FATAL))	type = ERROR;
        else if (level.equals(Level.INFO))	type = INFO;
        else if (level.equals(Level.WARN))	type = WARNING;
        else {
            throw new NoSuchElementException("No equivalent for Log4j Level." + level + ".");
        }

        return type;
    }


}