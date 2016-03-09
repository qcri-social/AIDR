/**
 * 
 */
package qa.qcri.aidr.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.dao.ImageFeedDao;
import qa.qcri.aidr.entity.ImageFeed;

/**
 * @author Latika
 *
 */

@Service
public class ImageFeedService {

	protected static Logger logger = Logger.getLogger(ImageFeedService.class);

	@Autowired
    ImageFeedDao imageFeedDAO;

	@Transactional(readOnly = false)
	public void checkAndSaveIfNotExists(Long dataFeedId, String collectionCode, String imageURL) {
		ImageFeed imageFeed = imageFeedDAO.findByImageURLAndCollectionCode(collectionCode, imageURL);
		if(imageFeed == null) {
			imageFeed = new ImageFeed();
			imageFeed.setCollectionFeedId(dataFeedId);
			imageFeed.setImageURL(imageURL);
			imageFeed.setCollectionCode(collectionCode);
			imageFeedDAO.save(imageFeed);
		}
	}
}
