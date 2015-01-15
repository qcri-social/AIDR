package qa.qcri.aidr.output.utils;

import java.util.ArrayList;
import java.util.List;

public class TwitterUserLookUp {
	List<String> userNames = null;
	List<Long> userIds = null;
	
	public TwitterUserLookUp() {
		userNames = new ArrayList<String>();
		userIds = new ArrayList<Long>();
	}
	
	public List<String> getUserNames() {
		return this.userNames;
	}
	
	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}
	
	public List<Long> getUserIds() {
		return this.userIds;
	}
	
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
}
