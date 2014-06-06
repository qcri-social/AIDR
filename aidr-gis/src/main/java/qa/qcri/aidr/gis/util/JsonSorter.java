package qa.qcri.aidr.gis.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/7/13
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonSorter {
    public static JSONArray sortJsonByKey(JSONArray json, String key)
    {
        JSONArray sorted = new JSONArray();
        SortedMap map = new TreeMap();

        for (Object o : json) {
            JSONObject tmp = (JSONObject) o;
            map.put(tmp.get(key),tmp);
        }

        Set<String> numbers = map.keySet();

        for (String number : numbers) {
            sorted.add(map.get(number));
        }

        return sorted;
    }
}
