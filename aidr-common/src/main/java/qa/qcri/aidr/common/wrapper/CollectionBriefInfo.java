/**
 * 
 */
package qa.qcri.aidr.common.wrapper;

import java.io.Serializable;


/**
 * @author Latika
 *
 */
public class CollectionBriefInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	private String owner;
	private String language;
	private int trainingCount;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getTrainingCount() {
		return trainingCount;
	}
	public void setTrainingCount(int trainingCount) {
		this.trainingCount = trainingCount;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}