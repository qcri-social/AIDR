package qa.qcri.aidr.collector.java7;

/**
 * This class needs to be replaced with java.util.function.Predicate<T>
 * which is introduced in Java 8
 * @author Anthony Ananich <anton.ananich@inpun.com>
 */
@Deprecated
public interface Predicate<T> {

	static String filterName = null;
    boolean test(T t);

    String getFilterName();
}
