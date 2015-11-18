package qa.qcri.aidr.manager.util;

import java.util.Comparator;

import qa.qcri.aidr.manager.persistence.entities.Collection;

/**
 * @author Kushal
 *	This class sorts the collections on the basis of status i.e. Running or Running_Warning, 
 *  after that reverse sort on the basis of start date.
 */
public class CollectionComparator implements Comparator<Collection> {

	@Override
	public int compare(Collection c1, Collection c2) {
		int result = orderByStatus(c1.getStatus()) - orderByStatus(c2.getStatus());
		if(result == 0){
			if(c1.getStartDate()==null){
				return -1;
			}
			if(c2.getStartDate()==null){
				return 1;
			}
			result = c2.getStartDate().compareTo(c1.getStartDate());
		}
		return result;
	}
	
	//Running or Running_Warning will come first
	public int orderByStatus(CollectionStatus cs){
		switch(cs.getStatus()){
			case "RUNNING" : return 0 ;
			case "RUNNING_WARNING" : return 0;
			default : return 9;
		}
	}
}
