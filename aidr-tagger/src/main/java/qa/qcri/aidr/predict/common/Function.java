package qa.qcri.aidr.predict.common;

/**
 * A Java attempt at delegates/function pointers. Easiest to use with inline
 * functions.
 * 
 * @author jrogstadius
 * @param <T>
 *            Data type of input.
 */
public interface Function<T> {
    public void execute(T arg);
}
