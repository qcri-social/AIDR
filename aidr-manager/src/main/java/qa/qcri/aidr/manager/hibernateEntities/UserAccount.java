package qa.qcri.aidr.manager.hibernateEntities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="account")
public class UserAccount extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3485170416057248803L;

	@Column(name="user_name", unique=true, nullable=false)
	private String userName;
	private String provider;
	private String locale;
	
	@Column(name="api_key", nullable=false, unique=true)
	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}
