/**
 * 
 */
package qa.qcri.aidr.manager.service;

import java.util.List;

/**
 * @author Latika
 *
 */
public interface WordDictionaryService {
	
	public List<String> fetchAllStopWords();
	public List<String> fetchAllStopWordsByLanguage();
}
