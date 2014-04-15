package qa.qcri.aidr.output.utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SimpleRateLimiter extends RateLimiter {
		
		public static final int DEFAULT_SIZE = 10;
		public int size = DEFAULT_SIZE;
		
		public static final int DEFAULT_MAX_SELECTIONS = (int) (DEFAULT_SIZE * 0.5);
		public int maxSelections = DEFAULT_MAX_SELECTIONS;
		
		private Map<String, Integer>freq = new TreeMap<String, Integer>();
		
		public SimpleRateLimiter() {}
		
		/**
		 * 
		 * @param key reset to 0 the count for key
		 */
		public void initialize(String key) {
			freq.put(key, 0);
		}
		
		/**
		 * 
		 * @param key initialize to 0 only if key already not present
		 */
		public void initializeNew(String key) {
			if (!freq.containsKey(key)) {
				System.out.println("[initializeNew] new key = " + key);
				freq.put(key, 0);
			} else {
				System.out.println("[initializeNew] skipping existing key = " + key);
			}
		}
		
		/**
		 * hard reset of all key values
		 */
		public void initializeAll() {
			for (String key: freq.keySet()) {
				freq.put(key, 0);
			}
		}
		
		/**
		 * 
		 * @param key increment key occurrence count by 1
		 */
		public void increment(String key) {
			freq.put(key, freq.containsKey(key) ? freq.get(key) + 1 : 0);
		}
		
		public void decrement(String key) {
			freq.put(key, freq.containsKey(key) ? freq.get(key) - 1 : 0);
		}
		
		public void setValue(String key, int value) {
			freq.put(key, value);
		}
		
		public int getValue(String key) {
			return (freq.containsKey(key) ? freq.get(key) : 0);
		}
		
		/**
		 * 
		 * @param key to test for rate limited
		 * @return false if not rate limited, true otherwise
		 */
		public boolean isRateLimited(String key) {
			return (freq.get(key) < maxSelections ? false : true);  
		}
		
		/**
		 * 
		 * @return the first encountered key that is not Rate Limited
		 */
		public String findFirstIsNotRateLimitedKey() {
			for (String key : freq.keySet()) {
				if (!isRateLimited(key)) {
					return key;
				}
			}
			return null;
		}
		
		public String findFirstIsNotRateLimitedKeyExclude(String excludeKey) {
			for (String key : freq.keySet()) {
				if (!isRateLimited(key) && !key.equalsIgnoreCase(excludeKey)) {
					return key;
				}
			}
			return null;
		}
		
		/**
		 * 
		 * @return a randomly selected key from not Rate Limited keys
		 */
		public String findRandomIsNotRateLimitedKey() {
			ArrayList<String>tempList = new ArrayList<String>();
			for (String key : freq.keySet()) {
				if (!isRateLimited(key)) {
					tempList.add(key);
				}
			}
			if (!tempList.isEmpty()) {
				// Now choose a key randomly from the eligible list
				int choice = (int) Math.floor(Math.random() * tempList.size());
				return tempList.get(choice);
			}
			return null;
		}
		
		public String findRandomIsNotRateLimitedKeyExclude(String excludeKey) {
			ArrayList<String>tempList = new ArrayList<String>();
			for (String key : freq.keySet()) {
				if (!isRateLimited(key) && !key.equalsIgnoreCase(excludeKey)) {
					tempList.add(key);
				}
			}
			if (!tempList.isEmpty()) {
				// Now choose a key randomly from the eligible list
				int choice = (int) Math.floor(Math.random() * tempList.size());
				return tempList.get(choice);
			}
			return null;
		}
		
		public boolean existsNotRateLimitedKey() {
			for (String key : freq.keySet()) {
				if (!isRateLimited(key)) {
					return true;
				}
			}
			return false;
		}
		
		public boolean existsNotRateLimitedKeyExclude(String excludeKey) {
			for (String key : freq.keySet()) {
				if (!isRateLimited(key) && !key.equalsIgnoreCase(excludeKey)) {
					return true;
				}
			}
			return false;
		}
		
		public void setMaxSelections(int newValue) {
			maxSelections = newValue;
		}
		
		public void setSize(int size) {
			this.size = size;
		}
		
		public int getMaxSelections() {
			return maxSelections;
		}
		
		public int getSize() {
			return size;
		}
}

