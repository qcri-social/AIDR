package qa.qcri.aidr.common.log;

import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 11/3/14
 * Time: 9:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class LogThrowable implements ItemInterface, Serializable
{
    private static final long serialVersionUID = 1252974212673830094L;

    private static final StackTraceElement[] EMPTY_TRACE = new StackTraceElement[0];

    private static final String LOGGED_INTERFACE = ItemInterface.class.getName();

    private static int nextID = 1;

    private final Throwable delegator;

    private final int errorCode;

    private final List<LogItem> logItems			= new Vector<LogItem>();

    private final transient	List<LogItem> logDetailsItems	= new Vector<LogItem>();

    private final Throwable sourceThrowable;

    private final transient StackTraceElement[] trace;

    private int			logCallsCount;

    private final int 	exceptionID;

    public LogThrowable(Throwable delegator, Throwable sourceThrowable, int errorCode) {
        super();

        this.delegator		 = delegator;
        this.sourceThrowable = sourceThrowable;
        this.errorCode		 = errorCode;
        exceptionID			 = generateID();

        final List<StackTraceElement> stack = new ArrayList<StackTraceElement>(Arrays.asList(getTraceSource().getStackTrace()));

        if (!stack.isEmpty()) {
            int first;
            for (first = 0; implementsItemInterface(stack.get(first).getClassName()); first++);
            trace = stack.subList(first, stack.size()).toArray(new StackTraceElement[stack.size() - first]);
        } else {
            trace = EMPTY_TRACE;
        }
    }

    @Override
    public void addDetailsItem(String label, Object value) {
        logDetailsItems.add(new LogItem(label, value));
    }

    @Override
    public void addItem(String label, boolean value) {
        logItems.add(new LogItem(label, value));
    }

    @Override
    public void addItem(String label, char value) {
        logItems.add(new LogItem(label, value));
    }

    @Override
    public void addItem(String label, byte value) {
        logItems.add(new LogItem(label, value));
    }

    @Override
    public void addItem(String label, short value) {
        logItems.add(new LogItem(label, value));
    }

    @Override
    public void addItem(String label, int value) {
        logItems.add(new LogItem(label, value));
    }

    @Override
    public void addItem(String label, long value) {
        logItems.add(new LogItem(label, value));
    }

    @Override
    public void addItem(String label, float value) {
        logItems.add(new LogItem(label, value));
    }

    @Override
    public void addItem(String label, double value) {
        logItems.add(new LogItem(label, value));
    }

    @Override
    public void addItem(String label, Object value) {
        logItems.add(new LogItem(label, value));
    }

    Collection<LogItem> getAllItems() {
        final Collection<LogItem> allItems = new ArrayList<LogItem>();
        allItems.addAll(logItems);
        allItems.addAll(logDetailsItems);
        return allItems;
    }

    public String getCallersMethodName() {
        return (trace.length > 0) ? trace[0].getMethodName() : "<unknown>";
    }

    @Override
    public String getDescription() {
        return getFullName() + ": " + delegator.getMessage();
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    public String getFullName() {
        return getTraceSource().getClass().getName();
    }

    public String getMessage() {
        return delegator.getMessage();
    }

    @Override
    public String stackTraceToString() {
        final ByteArrayOutputStream stackTrace = new ByteArrayOutputStream();
        printStackTrace(new PrintWriter(stackTrace, true));
        return stackTrace.toString();
    }

    public Throwable getThrowable() {
        return sourceThrowable;
    }

    private Throwable getTraceSource() {
        return ((getThrowable() == null) ? delegator : getThrowable());
    }

    public static boolean implementsItemInterface(String className) {
        boolean result = false;

        try {
            forClasses: for (Class<?> theClass = Class.forName(className); theClass != null; theClass = theClass.getSuperclass()) {
                final Class<?>[] implemented = theClass.getInterfaces();

                for (Class<?> i : implemented) {
                    if (LOGGED_INTERFACE.equals(i.getName())) {
                        result = true;
                        break forClasses;
                    }
                }
            }
        } catch (Throwable ignored) {
            // Not reported.
        }

        return result;
    }

    private static synchronized int generateID() {
        return nextID++;
    }

    @Override
    public void log() {
        if (sourceThrowable == null || !implementsItemInterface(sourceThrowable.getClass().getName())) {
            final Logger LOG = Logger.getLogger(delegator.getClass().getName());
            LOG.log(
                    ItemType.ERROR.getLog4jLevel(),
                    new LogMessage(
                            Integer.toString(exceptionID),
                            errorCode,
                            getCallersMethodName(),
                            getDescription(),
                            (logCallsCount < 1) ? trace : null,
                            LOG.isDebugEnabled() ? getAllItems() : logItems
                    )
            );

            logItems.clear();
            ++logCallsCount;
        }
    }

    public void printStackTrace(PrintWriter pw) {
        getTraceSource().printStackTrace(pw);
    }
}
