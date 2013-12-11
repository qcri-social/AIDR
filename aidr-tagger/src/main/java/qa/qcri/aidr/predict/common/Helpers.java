package qa.qcri.aidr.predict.common;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.OctalUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * A collection of helper methods.
 * 
 * @author jrogstadius
 */
public class Helpers {
    public static String join(Collection<?> s, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iter = s.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next().toString());
            if (!iter.hasNext()) {
                break;
            }
            builder.append(delimiter);
        }
        return builder.toString();
    }

    public static String[] toStringArray(JSONArray jsonArr) {
        String[] arr = new String[jsonArr.length()];
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                arr[i] = jsonArr.getString(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        return arr;
    }

    static final CharSequenceTranslator ESCAPE_JSON = new AggregateTranslator(
            new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()),
            UnicodeEscaper.outsideOf(32, 0x7f), new LookupTranslator(
                    new String[][] { { "<", "\\u003c" }, { ">", "\\u003e" },
                            // {"\\", "\\\\"},
                            { "\n", "\\n" }, { "\r", "" } }));

    public static String escapeJson(String text) {
        return ESCAPE_JSON.translate(text);
    }

    static final CharSequenceTranslator UNESCAPE_JSON = new AggregateTranslator(
            new OctalUnescaper(), // .between('\1', '\377'),
            new UnicodeUnescaper(), new LookupTranslator(
                    EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()),
            new LookupTranslator(new String[][] { { "\\\u003c", "<" },
                    { "\\u003e", ">" },
            // {"\\\\", "\\"},
            // {"\n", "\\n"},
            // {"\r", "\\r"}
                    }));

    public static String unescapeJson(String text) {
        return UNESCAPE_JSON.translate(text);
    }
}
