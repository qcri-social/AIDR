/**
 * 
 */
package qa.qcri.aidr.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.entity.ImageFeed;

/**
 * @author Latika
 *
 */

@Repository
public class ImageFeedDao extends AbstractDao<ImageFeed, Long>{

	private Logger logger = Logger.getLogger(DataFeedDAO.class);

	protected ImageFeedDao() {
		super(ImageFeed.class);
	}

	public ImageFeed findByImageURLAndCollectionCode(String collectionCode, String imageURL) {
		ImageFeed imageFeed = null;
		try{
			
			Criterion criterion = Restrictions.conjunction()
					.add(Restrictions.eq("imageURL", imageURL))
					.add(Restrictions.eq("collectionCode", collectionCode));
			List<ImageFeed> imageFeeds = findByCriteria(criterion);
			
			if(imageFeeds != null && imageFeeds.size() > 0) {
				imageFeed = imageFeeds.get(0); 
			}
		}
		catch(Exception e){
			logger.error("Exception while fetching image feed for url : " + imageURL , e);
		}
		return imageFeed;
	}

	@Override
	public Serializable save(ImageFeed imageFeed) {
		
		Long savedId = -1L;
		Date currentDate = new Date();
		if(imageFeed != null) {
			if(imageFeed.getId() == null) {
				imageFeed.setCreatedAt(currentDate);
			}

			imageFeed.setUpdatedAt(currentDate);
			savedId = (Long)super.save(imageFeed);
		}
		
		return savedId;
	}
}
