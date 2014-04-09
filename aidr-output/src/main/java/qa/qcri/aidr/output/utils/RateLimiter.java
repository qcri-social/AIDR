package qa.qcri.aidr.output.utils;

import java.util.Iterator;
import java.util.TreeMap;


public class RateLimiter {

	public static final double MAX_INTERVAL_VAL = 0.8;		// [0,1]
	public static final double MIN_INTERVAL_VAL = 0.01;		// [0,1]
	private double[] interval = null;		
	private TreeMap<String, Double> channelLambdaList = null;	// values must be all +ve

	public RateLimiter(TreeMap<String, Double> channelLambdaList) {
		interval = new double[channelLambdaList.size() + 1];
		this.channelLambdaList = new TreeMap<String, Double>();

		this.channelLambdaList.putAll(channelLambdaList);
		transform();
		buildIntervalList();
	}

	public void buildIntervalList() {
		double sum = 0;
		for (String channel: channelLambdaList.keySet()){
			sum += channelLambdaList.get(channel);
		}
		interval[0] = 0;
		Iterator<Double>it = channelLambdaList.values().iterator();
		for (int i = 1; i < interval.length;i++) {
			interval[i] = interval[i-1] + it.next() / sum;
		}
	}
	
	public String selectChannel() {
		double p = Math.random();
		Iterator<String>it = channelLambdaList.keySet().iterator();
		for (int i = 1;i < interval.length;i++) {
			if (p >= interval[i-1] && p < interval[i]) {
				return it.next();
			} else {
				if (it.hasNext()) it.next();
			}
		}
		return null;
	}
	
	public void transform() {
		for (String channel: channelLambdaList.keySet()) {
			double transformedValue = Math.abs(Math.log(channelLambdaList.get(channel)));
			channelLambdaList.put(channel, transformedValue);
		}
	}
	
	public void showInterval() {
		for (int i = 0;i < interval.length;i++) {
			System.out.println("interval[" + i + "] = " + interval[i]);
		}
	}
}

