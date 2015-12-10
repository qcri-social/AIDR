/**
 * 
 */
package qa.qcri.aidr.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.manager.persistence.entities.WordDictionary;
import qa.qcri.aidr.manager.repository.WordDictionaryRepository;
import qa.qcri.aidr.manager.service.WordDictionaryService;

/**
 * @author Latika
 *
 */
@Service
public class WordDictionaryServiceImpl implements WordDictionaryService {

	private static List<String> STOP_WORDS_LIST;
	
	@Autowired
	private WordDictionaryRepository wordRepository;
	
	@Override
	public List<String> fetchAllStopWords() {
		
		List<WordDictionary> wordDictionary = new ArrayList<WordDictionary>();
		if(STOP_WORDS_LIST == null) {
			STOP_WORDS_LIST = new ArrayList<String>();
			
			wordDictionary = wordRepository.findAll();
			for(WordDictionary word : wordDictionary) {
				STOP_WORDS_LIST.add(word.getWord());
			}
			
			for(WordDictionary word : wordDictionary) {
				STOP_WORDS_LIST.add("#" +word.getWord());
			}
		}		
		
		return STOP_WORDS_LIST;
	}

	@Override
	public List<String> fetchAllStopWordsByLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

}
