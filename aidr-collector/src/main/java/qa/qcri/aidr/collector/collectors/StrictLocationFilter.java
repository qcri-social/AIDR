package qa.qcri.aidr.collector.collectors;

import java.util.function.Predicate;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

public class StrictLocationFilter implements Predicate<JsonObject> {
	
	public StrictLocationFilter(){

	}

	@Override
	public boolean test(JsonObject t) {
		JsonValue c = t.get("coordinates");
		if (c.getValueType() != ValueType.OBJECT) {
			return false;
		}
		JsonObject coordinates = (JsonObject) c;
		if (!"Point".equals(coordinates.getString("type"))){
			return false;
		}
		return true;
	}

}
