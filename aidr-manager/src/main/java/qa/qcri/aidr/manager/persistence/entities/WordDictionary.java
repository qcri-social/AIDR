/**
 * 
 */
package qa.qcri.aidr.manager.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Latika
 *
 */

@Entity
@Table(name = "word_dictionary")
public class WordDictionary extends BaseEntity {

	
	@Column(nullable = false)
	private String word;
	private String language;
	
	@Column(name="is_stop_word")
	private Boolean isStopWord;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Boolean getIsStopWord() {
		return isStopWord;
	}

	public void setIsStopWord(Boolean isStopWord) {
		this.isStopWord = isStopWord;
	}
	
}
