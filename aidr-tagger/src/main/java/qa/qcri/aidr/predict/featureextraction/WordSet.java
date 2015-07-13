package qa.qcri.aidr.predict.featureextraction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import qa.qcri.aidr.common.logging.ErrorLog;


/**
 * A DocumentFeature implementation consisting of a set of words.
 * 
 * @author jrogstadius
 */
public class WordSet implements DocumentFeature, Serializable {

	private static Logger logger = Logger.getLogger(WordSet.class);
	private static ErrorLog elog = new ErrorLog();
			
    private static final long serialVersionUID = 1L;

    private static final String STR_TYPE = "type",
            STR_WORDVECTOR = "wordvector", STR_WORDS = "words";

    HashSet<String> words = new HashSet<String>();

    public void addAll(Collection<String> words) {
        this.words.addAll(words);
    }

    public void addAll(String[] words) {
        this.words.addAll(Arrays.asList(words));
    }

    public List<String> getWords() {
        return new ArrayList<String>(words);
    }

    public JSONObject toJSONObject() {
        if (words.isEmpty())
            return null;

        JSONArray wordsArr = new JSONArray();
        for (String w : words)
            wordsArr.put(w);
        JSONObject obj = new JSONObject();
        try {
            obj.put(STR_TYPE, STR_WORDVECTOR);
            obj.put(STR_WORDS, wordsArr);
        } catch (JSONException e) {
        	logger.error("Error in json parsing: " + words);
        	logger.error(elog.toStringException(e));
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static WordSet join(Collection<WordSet> sets) {
        WordSet set = new WordSet();
        for (WordSet s : sets)
            set.addAll(s.getWords());
        return set;
    }

    public double getSimilarity(WordSet other) {
        int l1 = words.size();
        int l2 = other.words.size();
        
        // intersection of two sets
        HashSet<String> intersection = (HashSet<String>) (l1 < l2 ? words.clone() : other.words.clone());
        intersection.retainAll(l1 < l2 ? other.words : words);
        int l3 = intersection.size();
        
        // similarity using Jaccard coefficient
        return l3 / (double) (l1 + l2 -l3);
    }
}
