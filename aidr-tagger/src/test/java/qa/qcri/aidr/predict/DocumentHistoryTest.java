/**
 * 
 */
package qa.qcri.aidr.predict;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import qa.qcri.aidr.predict.classification.DocumentHistory;
import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;
import qa.qcri.aidr.predict.data.Tweet;
import qa.qcri.aidr.predict.featureextraction.FeatureExtractor;
import qa.qcri.aidr.predict.featureextraction.WordSet;

/**
 * @author Latika
 *
 */
public class DocumentHistoryTest {
	
	
	String[] tweetTextArray = {
									"night neutral white test",
									"test there is test",
									"white pearl clear light"
							};
	
	DocumentHistory history;
	
	@Before
	public void setUp(){
		history = new DocumentHistory(
				Integer.parseInt(TaggerConfigurator.getInstance().getProperty(TaggerConfigurationProperty.TAGGER_TASK_BUFFER_SIMILARITY_BUFFER)),
				Double.parseDouble(TaggerConfigurator.getInstance().getProperty(TaggerConfigurationProperty.TAGGER_TASK_BUFFER_MAX_SIMILARITY)));
		
		
		for(String tweetText : tweetTextArray) {
			Tweet tweet = new Tweet();
			WordSet wordSet = new WordSet();
			wordSet.addAll(FeatureExtractor.getWordsInStringWithBigrams(tweetText, false));
			tweet.addFeatureSet(wordSet);
			
			history.addItemIfNovel(tweet);
		}
	}

	@Test
	public void testAddItemIfNovel() {
		
		// Test
		// add exactly same to the existing, should fail to add
		Tweet tweet = new Tweet();
		WordSet wordSet = new WordSet();
		String text = "white pearl clear light";
		wordSet.addAll(FeatureExtractor.getWordsInStringWithBigrams(text, false));
		
		tweet.addFeatureSet(wordSet);
		
		// addItemIfNovel should return false
		Assert.assertFalse("testAddItemIfNovel", history.addItemIfNovel(tweet));
		
		
		// should be added to doc history
		Tweet tweet1 = new Tweet();
		WordSet wordSet1 = new WordSet();
		String text1 = "pearl white light clear";
		wordSet1.addAll(FeatureExtractor.getWordsInStringWithBigrams(text1, false));
		
		tweet1.addFeatureSet(wordSet1);
		
		// addItemIfNovel should return true
		Assert.assertTrue("testAddItemIfNovel", history.addItemIfNovel(tweet1));
	}
	
	
}
