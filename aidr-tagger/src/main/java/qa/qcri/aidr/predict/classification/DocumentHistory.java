package qa.qcri.aidr.predict.classification;

import java.util.LinkedList;

import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.featureextraction.WordSet;

public class DocumentHistory {
	LinkedList<WordSet> recentWordVectors = new LinkedList<WordSet>();
	int bufferSize;
	double similarity;

	public DocumentHistory(int bufferSize, double similarity) {
		
		this.bufferSize = bufferSize;
		this.similarity = similarity;
	}
	
	public boolean addItemIfNovel(Document doc) {
		WordSet w1 = doc.getFeatures(WordSet.class).get(0);

		for (WordSet w2 : recentWordVectors) {
			double sim = w2.getSimilarity(w1);
			if (sim > similarity) {
				// TODO: This threshold needs some tuning,
				// probably
				return false;
			}
		}

		recentWordVectors.add(w1);
		if (recentWordVectors.size() > bufferSize)
			recentWordVectors.remove();

		return true;
	}
}