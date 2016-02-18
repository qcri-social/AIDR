package qa.qcri.aidr.predict.classification;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

import qa.qcri.aidr.predict.common.AlgorithmType;
import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;

public class ClassifierFactory {

	private static Logger logger = Logger.getLogger(ClassifierFactory.class);
	
	public static Classifier getClassifier(AlgorithmType type) {
		
		Classifier classifier = null;
		
		try {
			if(type == null) {
				type = AlgorithmType.RANDOM_FOREST;
			}
			
			Constructor constructor = type.getClazz().getConstructor();
			classifier = (Classifier)constructor.newInstance();
			
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		
			logger.warn("Unable to instantiate classifier : " + type.getClazz().getName());
			classifier = (Classifier) new RandomForest();
		}

		return classifier;
	}
}
