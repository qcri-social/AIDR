package qa.qcri.aidr.predict.featureextraction;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by noora on 5/20/15.
 */
public class FeatureExtractorTest {

   @Test
    public void testGetWordsInStringWithBigrams() throws Exception {
	   String[] actual = FeatureExtractor.getWordsInStringWithBigrams("hello what is your name", false);
	   //
	   String[] expected = {"hello", "your_name", "is", "hello_what", "what", "name", "your", "is_your", "what_is"};
	   assertArrayEquals(expected, actual);
	   //
	   String[] actualEmpty = FeatureExtractor.getWordsInStringWithBigrams("", false);
	   String[] expectedEmpty = {""}; 
	   assertArrayEquals(expectedEmpty, actualEmpty);
    }
}