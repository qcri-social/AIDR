package qa.qcri.aidr.collector.collectors;

import java.util.Arrays;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import qa.qcri.aidr.collector.beans.CollectionTask;
import qa.qcri.aidr.collector.java7.Predicate;

public class StrictLocationFilter implements Predicate<JsonObject> {
	
	private class BRect {
		public double minLon, minLat, maxLon, maxLat;
	}
	
	private BRect[] square;
	
	public StrictLocationFilter(CollectionTask task){
		String locations = task.getGeoLocation();
		if (locations != null && !locations.isEmpty()) {
			List<String> list = Arrays.asList(locations.split(","));
			// TODO: Java 8 update. Replace the following block with one single line
			// double[] flat = list.stream().mapToDouble(Double::parseDouble).toArray();
			double[] flat = new double[list.size()];
			for (int i=0; i<list.size(); ++i) {
				double val = Double.parseDouble(list.get(i));
				flat[i] = val;
			}
			// End of Java 8 Update
			assert flat.length % 4 == 0;
			square = new BRect[flat.length / 4];
			for (int i = 0; i < flat.length; i = i + 4) {
				BRect r = new BRect();
				r.minLon = flat[i];
				r.minLat = flat[i+1];
				r.maxLon = flat[i+2];
				r.maxLat = flat[i+3];
				square[i/4] = r;
			}
		}
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
		JsonArray a = coordinates.getJsonArray("coordinates");
		assert a.size() == 2;
		double lon = a.getJsonNumber(0).doubleValue();
		double lat = a.getJsonNumber(1).doubleValue();
		for (BRect r : square) {
			if (lon < r.minLon || lon > r.maxLon)
				return false;
			if (lat < r.minLat || lat > r.maxLat)
				return false;
		}
		return true;
	}

}
