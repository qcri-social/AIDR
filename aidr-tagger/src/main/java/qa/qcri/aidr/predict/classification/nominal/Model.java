package qa.qcri.aidr.predict.classification.nominal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import qa.qcri.aidr.predict.DataStore;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.featureextraction.WordSet;

/**
 * Model contains an instance of a Weka classifier, and transforms a feature
 * vector into a Weka Instance for classification. Model also contains
 * information about what features are used in the classifier and the model's
 * latest performance metrics.
 *
 * @author jrogstadius
 */
public class Model {

	private static Logger logger = Logger.getLogger(Model.class);
    private final int attributeID;
    private Classifier classifier;
    private Instances attributeSpecification;
    private Evaluation evaluation;
    private double[] missingVal;
    private double meanPrecision, meanRecall, meanAuc;
    private int modelID = -1;
    private Integer trainingSampleCount;
    private List<ModelNominalLabelPerformance> labelPerformanceList;

    public Model(int attributeID, Classifier classifier, Instances specification) {
        this.attributeID = attributeID;
        this.classifier = classifier;
        this.attributeSpecification = specification;
        this.missingVal = new double[attributeSpecification.numAttributes()];
        labelPerformanceList = new ArrayList<ModelNominalLabelPerformance>();
    }

    public Evaluation evaluate(Instances evaluationSet) throws Exception {
        Evaluation evaluation = new Evaluation(evaluationSet);
        evaluation.evaluateModel(classifier, evaluationSet);

        Integer nullLabelID = DataStore.getNullLabelID(attributeID);

        // Calculate performance score
        Attribute classAttribute = attributeSpecification
                .attribute(attributeSpecification.numAttributes() - 1);
        double precSum = 0, recSum = 0, aucSum = 0;
        for (int i = 0; i < classAttribute.numValues(); i++) {

            double precTemp = evaluation.precision(i);
            double recTemp = evaluation.recall(i);
            double aucTemp = evaluation.areaUnderROC(i); //.areaUnderPRC(i);
            if (!(aucTemp >= 0 && aucTemp <= 1)) {
                logger.warn("AUC is not available for the trained model");
                aucTemp = 0;
            }
            
            //Per-label classification performance
            ModelNominalLabelPerformance labelPerformance = new ModelNominalLabelPerformance(
                    Integer.parseInt(classAttribute.value(i)), precTemp, recTemp, aucTemp, 0);

            labelPerformanceList.add(labelPerformance);

            //Average classification performance across all non-null labels
            //if (Integer.parseInt(classAttribute.value(i)) != nullLabelID) {
                precSum += precTemp;
                recSum += recTemp;
                aucSum += aucTemp;
            //}
        }
        double numValues = classAttribute.numValues();// - 1; // ignore "null"
        
        meanPrecision = precSum / numValues;
        meanRecall = recSum / numValues;
        meanAuc = aucSum / numValues;

        return evaluation;
    }

    public Classifier getClassifier() {
        return classifier;
    }

    public Instances getAttributeSpecification() {
        return attributeSpecification;
    }

    public Evaluation getEvaluationResults() {
        return evaluation;
    }

    public int getAttributeID() {
        return attributeID;
    }

    public NominalLabelBC classify(Document item) {
        if (modelID == -1) {
        	logger.error("Model has not been initialized");
        	throw new RuntimeException("Model has not been initialized");
    	}
        
        ArrayList<WordSet> wordSets = item.getFeatures(WordSet.class);
        if (wordSets.isEmpty()) {
            return null;
        }

        Instance instance = wordsToInstance(WordSet.join(wordSets));
        Attribute classAttribute = attributeSpecification
                .attribute(attributeSpecification.numAttributes() - 1);

        try {
            double[] labelProbabilities = classifier
                    .distributionForInstance(instance);
            //labelIndex refers to the position of this label in the class
            //attribute's list of possible values
            int labelIndex = findLargestIndex(labelProbabilities);

            //Weka's class attribute has only string values, so we parse the value
            //to get a nominalLabelID
            int labelID = Integer.parseInt(classAttribute.value(labelIndex));
            NominalLabelBC label = new NominalLabelBC(
                    modelID, 
                    attributeID,
                    labelID,
                    labelProbabilities[labelIndex]);
            item.addLabel(label);
            return label;
        } catch (Exception e) {
            logger.error("Exception when classifying document set", e);
        }
        return null;
    }

    int findLargestIndex(double[] probabilities) {
        int i = 0;
        for (int j = 1; j < probabilities.length; j++) {
            if (probabilities[j] > probabilities[i]) {
                i = j;
            }
        }
        return i;
    }

    Instance wordsToInstance(WordSet words) {
        Instance item = new SparseInstance(
                attributeSpecification.numAttributes());
        item.setDataset(attributeSpecification);
        // Words
        for (String word : words.getWords()) {
            Attribute attribute = attributeSpecification.attribute(word);
            if (attribute != null) {
                item.setValue(attribute, 1);
            }
        }

        item.replaceMissingValues(missingVal);

        return item;
    }

    public double getMeanPrecision() {
        return meanPrecision;
    }

    public double getMeanRecall() {
        return meanRecall;
    }

    public double getMeanAuc() {
        return meanAuc;
    }

    public double getWeightedPerformance() {
        return meanPrecision + 0.5 * meanRecall;
    }

    public void setTrainingSampleCount(int count) {
        trainingSampleCount = count;
    }

    public int getTrainingSampleCount() {
        if (trainingSampleCount == null) {
        	logger.error("trainingSampleCount has not been set");
            throw new RuntimeException("trainingSampleCount has not been set");
        }

        return trainingSampleCount;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    public int getModelID() {
        return modelID;
    }

    public List<ModelNominalLabelPerformance> getLabelPerformanceList() {
        return labelPerformanceList;
    }

}
