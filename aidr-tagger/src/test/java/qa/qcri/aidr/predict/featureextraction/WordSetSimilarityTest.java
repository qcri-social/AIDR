/**
 * 
 */
package qa.qcri.aidr.predict.featureextraction;

import org.junit.Assert;
import org.junit.Test;

import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;

/**
 * @author Latika
 *
 */
public class WordSetSimilarityTest {

	private Double maxSimilarity = Double.parseDouble(TaggerConfigurator.getInstance().getProperty(TaggerConfigurationProperty.TAGGER_TASK_BUFFER_MAX_SIMILARITY));
	@Test
	public void testWordSetExactSimilarity() {
		WordSet wordSet = new WordSet();
		String text = "neutral night coal coffee ink";
		wordSet.addAll(FeatureExtractor.getWordsInStringWithBigrams(text, false));
		
		WordSet wordSet1 = new WordSet();
		String text1 = "neutral night coal coffee ink";
		wordSet1.addAll(FeatureExtractor.getWordsInStringWithBigrams(text1, false));
		
		Assert.assertEquals("testWordSetExactSimilarity", 1.0, wordSet.getSimilarity(wordSet1), 0.0);
	}
	
	@Test
	public void testWordSetNoSimilarity() {
		WordSet wordSet = new WordSet();
		String text = "neutral night coal coffee ink";
		wordSet.addAll(FeatureExtractor.getWordsInStringWithBigrams(text, false));
	
		WordSet wordSet1 = new WordSet();
		String text1 = "hello what is your name";
		wordSet1.addAll(FeatureExtractor.getWordsInStringWithBigrams(text1, false));
		
		wordSet.getSimilarity(wordSet1);
		
		Assert.assertEquals("testWordSetNoSimilarity", 0.0, wordSet.getSimilarity(wordSet1), 0.0);
	}
	
	@Test
	public void testWordSetSimilarityBelowMaxSimilarity() {
		
		WordSet wordSet = new WordSet();
		String text = "neutral night coal coffee ink";
		wordSet.addAll(FeatureExtractor.getWordsInStringWithBigrams(text, false));
	
		WordSet wordSet1 = new WordSet();
		String text1 = "neutral hello coal name";
		wordSet1.addAll(FeatureExtractor.getWordsInStringWithBigrams(text1, false));
		
		wordSet.getSimilarity(wordSet1);
		
		Assert.assertTrue("testWordSetSimilarityBelowMaxSimilarity", wordSet.getSimilarity(wordSet1) < maxSimilarity);
	}
	
	@Test
	public void testWordSetSimilarityAboveMaxSimilarity() {
		WordSet wordSet = new WordSet();
		String text = "neutral night coal ";
		wordSet.addAll(FeatureExtractor.getWordsInStringWithBigrams(text, false));
	
		WordSet wordSet1 = new WordSet();
		String text1 = "neutral neutral night coal";
		wordSet1.addAll(FeatureExtractor.getWordsInStringWithBigrams(text1, false));
		
		wordSet.getSimilarity(wordSet1);
		
		Assert.assertTrue("testWordSetSimilarityAboveMaxSimilarity", wordSet.getSimilarity(wordSet1) > maxSimilarity );
	}
}
