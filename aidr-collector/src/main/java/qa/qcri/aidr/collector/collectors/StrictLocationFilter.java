package qa.qcri.aidr.collector.collectors;

import java.util.Arrays;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import qa.qcri.aidr.collector.beans.TwitterCollectionTask;
import qa.qcri.aidr.collector.java7.Predicate;

/**
 * Validates use-defined geo-coordinates.
 * Validates geo-coordinates strictness with the user-defined locations for each collected tweet, if geo-strict is ON.
 * 
 */

public class StrictLocationFilter implements Predicate<JsonObject> {
		
	private class BRect {
		public double minLon, minLat, maxLon, maxLat;
	}
	
	private BRect[] square;
	
	public StrictLocationFilter(TwitterCollectionTask task) throws ParseException{
		String locations = task.getGeoLocation();
		try {
			if (locations == null || locations.trim().isEmpty())
				throw new ParseException("GeoLocation should be a non-empty string");
			List<String> list = Arrays.asList(locations.split(","));
			// TODO: Java 8 update. Replace the following block with one single line
			// double[] flat = list.stream().mapToDouble(Double::parseDouble).toArray();
			double[] flat = new double[list.size()];
			for (int i=0; i<list.size(); ++i) {
				double val = Double.parseDouble(list.get(i));
				flat[i] = val;
			}
			// End of Java 8 Update
			if (flat.length % 4 != 0)
				throw new ParseException("GeoLocation should contain N numbers, where N is multiple of 4");
			square = new BRect[flat.length / 4];
			for (int i = 0; i < flat.length; i = i + 4) {
				BRect r = new BRect();
				r.minLon = flat[i];
				r.minLat = flat[i+1];
				r.maxLon = flat[i+2];
				r.maxLat = flat[i+3];
				square[i/4] = r;
			}
		} catch (NumberFormatException ex) {
			throw new ParseException(String.format("Can not parse locations '%s'", locations), ex);
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
				continue;
			if (lat >= r.minLat || lat <= r.maxLat)
				return true;
		}
		return false;
	}

	@Override
	public String getFilterName() {
		return this.getClass().getSimpleName();
	}
}

class ParseException extends Exception {
	public ParseException(String message) {
		super(message);
	}
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
