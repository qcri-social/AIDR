package qa.qcri.aidr.data.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import qa.qcri.aidr.data.util.ActivityType;

@Entity
@Table(name="account_activity")
public class UserAccountActivity extends BaseEntity {

	private static final long serialVersionUID = 3485170416057248803L;
	
	@ManyToOne
	@JoinColumn(name="account_id")
	private UserAccount account;
	
	@Column(name="activity_date")
	private Date activityDate;
	
	@Column(name="download_count")
	private Integer downloadCount;
	
	@Enumerated(EnumType.STRING)
	@Column(name="activity_type", nullable=false)
	private ActivityType activityType;
	
	public UserAccountActivity() {
		super();
	}

	public UserAccountActivity(UserAccount account, Date activityDate, Integer downloadCount,
			ActivityType activityType) {
		super();
		this.account = account;
		this.activityDate = activityDate;
		this.downloadCount = downloadCount;
		this.activityType = activityType;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}

	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}
	
}
