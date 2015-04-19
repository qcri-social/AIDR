package qa.qcri.aidr.collector.collectors;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordPredicate {

	private String patternString = "([^\"]\\S*|\".+?\")\\s*";
	private Pattern pattern = null;

	private Set<String> phraseSet = null;
	private Set<String> unorderedWords = null;

	public KeywordPredicate() {
		pattern = Pattern.compile(patternString);
		phraseSet = new HashSet<String>();
		unorderedWords = new HashSet<String>();
	}

	public KeywordPredicate(final String keyword) {
		this();
		if (keyword != null) {
			// first split on whitespace
			List<String> words = splitOnWhitespace(keyword);

			// If word startWith("\"") then split further and put in ordered list
			// Otherwise put in unordered list
			// TODO: handle boundary conditions!
			for (String w: words) {
				if (w.startsWith("\"")) {
					String strippedWord = null;
					int end = w.lastIndexOf("\"");
					if (end > 0) {
						strippedWord = w.substring(1, end);
					} else {
						// Handle the special case of malformed quotations: runaway quote in text
						strippedWord = w.substring(1).trim();
					}
					StringBuffer buf = new StringBuffer();
					buf.append(" ").append(strippedWord);
					phraseSet.add(new String(buf));		// Will match end of a string
					
					buf.append(" ");
					phraseSet.add(new String(buf));		// Will match middle of a string
					
					buf.delete(0, 1);					// delete leading whitespace
					phraseSet.add(new String(buf));		// Will match beginning of a string
				} else {
					unorderedWords.add(w.trim());
				}
			}
		}
	}

	public Set<String> getPhraseSet() {
		return this.phraseSet;
	}

	public Set<String> getUnorderedWords() {
		return this.unorderedWords;
	}

	/**
	 * 
	 * @param str string to split
	 * @return
	 */
	private List<String> splitOnWhitespace(String str) {
		List<String> list = new LinkedList<String>();

		Matcher m = pattern.matcher(str);
		while (m.find()) {
			list.add(m.group(1)); // Add .replace("\"", "") to remove surrounding quotes.
		}
		return list;
	}
}
