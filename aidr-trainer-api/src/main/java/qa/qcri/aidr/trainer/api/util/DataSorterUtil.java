package qa.qcri.aidr.trainer.api.util;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 10/7/13
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSorterUtil {
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

    public static SortedMap sortNominalLabelByCode(List<NominalLabelDTO> nominalLabels){
        SortedMap map = new TreeMap();

        for (NominalLabelDTO o : nominalLabels) {
            map.put(o.getSequence(),o);
        }

        return map;
    }
}
