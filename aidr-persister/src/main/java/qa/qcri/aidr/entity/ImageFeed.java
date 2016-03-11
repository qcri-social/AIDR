/**
 * 
 */
package qa.qcri.aidr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Latika
 *
 */
@Entity
@Table(name="image_feed")
public class ImageFeed extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column(name="image_url")
	private String imageURL;
	
	@Column(name="collection_feed_id")
	private Long collectionFeedId;
	
	@Column(name="collection_code")
	private String collectionCode;
	
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public Long getCollectionFeedId() {
		return collectionFeedId;
	}
	public void setCollectionFeedId(Long collectionFeedId) {
		this.collectionFeedId = collectionFeedId;
	}

	public String getCollectionCode() {
		return collectionCode;
	}
	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}
}
