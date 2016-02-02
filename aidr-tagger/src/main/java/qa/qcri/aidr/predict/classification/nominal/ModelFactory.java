package qa.qcri.aidr.predict.classification.nominal;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import qa.qcri.aidr.predict.DataStore;
import qa.qcri.aidr.predict.classification.ClassifierFactory;
import qa.qcri.aidr.predict.common.AlgorithmType;
import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * ModelFactory performs delegated encapsulated training of new classifiers.
 * When a model is built, the class handles retrieval of training and evaluation
 * data from the database, model training and evaluation.
 *
 * @author jrogstadius
 */
public class ModelFactory {

	/**
	 * Train a new model for the specified event and ontology.
	 *
	 * @param crisisID
	 * @param attributeID
	 * @param oldModel An existing model to compare performance against. Null if
	 * no previous model exists.
	 * @return A new model if it outperforms the old model, otherwise the old
	 * model.
	 * @throws Exception
	 */
	private static Logger logger = Logger.getLogger(ModelFactory.class);

	//private static final double EPSILON = 0.05;		// Tolerance for comparing two models: added by koushik
	private static final double PERFORMANCE_IMPROVEMENT_MARGIN = Double
			.parseDouble(TaggerConfigurator.getInstance().getProperty(
					TaggerConfigurationProperty.PERFORMANCE_IMPROVEMENT_MARGIN));
	private static final int TRAINING_EXAMPLES_FORCE_RETRAIN = Integer
			.parseInt(TaggerConfigurator
					.getInstance()
					.getProperty(
							TaggerConfigurationProperty.TRAINING_EXAMPLES_FORCE_RETRAIN));
	private static final int sampleCountThreshold = Integer
			.parseInt(TaggerConfigurator.getInstance().getProperty(
					TaggerConfigurationProperty.SAMPLE_COUNT_THRESHOLD));

	public static Model buildModel(int crisisID, int attributeID, Model oldModel)
			throws Exception {

		// TODO: Improve model training to try different classifiers and
		// different mixes of old and new data

		// Get training and evaluation data
		Instances trainingSet = DataStore.getTrainingSet(crisisID, attributeID);
		Instances evaluationSet = DataStore.getEvaluationSet(crisisID,
				attributeID, trainingSet);

		if (trainingSet.attribute(trainingSet.numAttributes() - 1).numValues() < 2) {
			logger.info("ModelFactory" + 
					"All training examples have the same label. Postponing training.");
			return oldModel;
		}
		if (evaluationSet.numInstances() < 2) {
			logger.info("ModelFactory" +
					"The evaluation set is too small. Postponing training.");
			return oldModel;
		}

		// Do attribute selection
		AttributeSelection selector = getAttributeSelector(trainingSet);
		trainingSet = selector.reduceDimensionality(trainingSet);
		evaluationSet = selector.reduceDimensionality(evaluationSet);

		// Train classifier
		Classifier classifier = trainClassifier(trainingSet);

		// Create the model object
		Model model = new Model(attributeID, classifier, getTemplateSet(trainingSet));
		model.setTrainingSampleCount(trainingSet.size());

		// Evaluate classifier
		model.evaluate(evaluationSet);
		double newPerformance = model.getWeightedPerformance();
		double oldPerformance = 0;
		if (oldModel != null) {
			oldModel.evaluate(evaluationSet);
			oldPerformance = oldModel.getWeightedPerformance();
		}

		// Koushik: Changed as per ChaTo's suggestion
		/*
        if (newPerformance > oldPerformance - EPSILON) {
            return model;
        } else {
            return oldModel;
        }*/
		if (newPerformance > oldPerformance - PERFORMANCE_IMPROVEMENT_MARGIN) {
			return model;
		} else if( model.getTrainingSampleCount() > oldModel.getTrainingSampleCount() + TRAINING_EXAMPLES_FORCE_RETRAIN) {
			return model;
		} else {
			return oldModel;
		}
	}

	private static Instances getTemplateSet(Instances dataSet) {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>(
				dataSet.numAttributes());
		for (int i = 0; i < dataSet.numAttributes(); i++) {
			attributes.add(dataSet.attribute(i));
		}
		Instances specification = new Instances("spec", attributes, 0);
		specification.setClassIndex(specification.numAttributes() - 1);
		return specification;
	}

	private static AttributeSelection getAttributeSelector(
			Instances trainingData) throws Exception {
		AttributeSelection selector = new AttributeSelection();
		InfoGainAttributeEval evaluator = new InfoGainAttributeEval();
		Ranker ranker = new Ranker();
		ranker.setNumToSelect(Math.min(500, trainingData.numAttributes() - 1));
		selector.setEvaluator(evaluator);
		selector.setSearch(ranker);
		selector.SelectAttributes(trainingData);
		return selector;
	}

	private static Classifier trainClassifier(Instances trainingSet)
			throws Exception {
		Classifier model = (Classifier) ClassifierFactory.getClassifier(AlgorithmType.RANDOM_FOREST);
		model.buildClassifier(trainingSet);
		return model;
	}
}
