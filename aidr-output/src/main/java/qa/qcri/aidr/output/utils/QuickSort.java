package qa.qcri.aidr.output.utils;

import java.util.List;
import qa.qcri.aidr.output.filter.ClassifiedFilteredTweet;

public class QuickSort  {
	private List<String> dataList;
	
	public void sort(List<String> dataList) {
		// check for empty or null array
		if (dataList == null || dataList.size() == 0){
			return;
		}
		this.dataList = dataList;
		quicksort(0, this.dataList.size() - 1);
	}

	private void quicksort(int low, int high) {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		final String pivot = dataList.get(low + (high-low)/2);

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller then the pivot
			// element then get the next element from the left list
			ClassifiedFilteredTweet tweet = new ClassifiedFilteredTweet().deserialize(dataList.get(i));
			ClassifiedFilteredTweet pivotTweet = new ClassifiedFilteredTweet().deserialize(pivot);
			
			while (tweet.getCreatedAt().getTime() < pivotTweet.getCreatedAt().getTime()) {
				i++;
			}
			// If the current value from the right list is larger then the pivot
			// element then get the next element from the right list
			while (tweet.getCreatedAt().getTime() > pivotTweet.getCreatedAt().getTime()) {
				j--;
			}

			// If we have found a values in the left list which is larger then
			// the pivot element and if we have found a value in the right list
			// which is smaller then the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j);
				i++;
				j--;
			}
		}
		// Recursion
		if (low < j)
			quicksort(low, j);
		if (i < high)
			quicksort(i, high);
	}

	private void exchange(int i, int j) {
		final String temp1 = dataList.get(i);
		final String temp2 = dataList.get(j);
		dataList.remove(i);
		dataList.add(i, temp2);
		dataList.remove(j);
		dataList.add(j, temp1);
	}
} 