/**
 * 
 */
package qa.qcri.aidr.predict.featureextraction;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Latika
 *
 */
public class WordSetSimilarityTest {

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
	public void testWordSetSimilarity() {
		WordSet wordSet = new WordSet();
		String text = "neutral night coal coffee ink";
		wordSet.addAll(FeatureExtractor.getWordsInStringWithBigrams(text, false));
	
		WordSet wordSet1 = new WordSet();
		String text1 = "neutral hello coal name";
		wordSet1.addAll(FeatureExtractor.getWordsInStringWithBigrams(text1, false));
		
		wordSet.getSimilarity(wordSet1);
		
		Assert.assertTrue("testWordSetSimilarity", wordSet.getSimilarity(wordSet1) > 0.0);
	}
	
	@Test
	public void testWordSetSimilarity1() {
		WordSet wordSet = new WordSet();
		String text = "neutral night coal ";
		wordSet.addAll(FeatureExtractor.getWordsInStringWithBigrams(text, false));
	
		WordSet wordSet1 = new WordSet();
		String text1 = "neutral neutral night coal";
		wordSet1.addAll(FeatureExtractor.getWordsInStringWithBigrams(text1, false));
		
		wordSet.getSimilarity(wordSet1);
		
		Assert.assertTrue("testWordSetSimilarity", wordSet.getSimilarity(wordSet1) > 0.5 );
	}
}
