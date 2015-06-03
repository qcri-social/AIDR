package qa.qcri.aidr.predict.classification.nominal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import weka.classifiers.Classifier;
import weka.core.Instances;
import qa.qcri.aidr.predict.DataStore;
import qa.qcri.aidr.predict.common.*;
import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.dbentities.ModelFamilyEC;

/**
 * ModelController handles classification of DocumentSet objects, with the
 * assumption that feature extraction has been done previously in the pipeline.
 *
 * @author jrogstadius & Imran
 */
public class ModelController extends PipelineProcess {

	private static Logger logger = Logger.getLogger(ModelController.class);
	
    ModelSet models = new ModelSet();
    ModelRetrainTrigger trainingFeedMonitor;
    long lastCheckForDefinitionUpdates = 0;
    final long definitionChangeFrequencyMs = 5 * 60000;
    //labelTrainingWeights contains <attributeID,<labelID, trainingWeight>>
    HashMap<Integer, HashMap<Integer, Double>> labelTrainingWeights;
    long lastLabelTrainingWeightUpdate = 0;
    //classifiedDocCount contains <modelID,<labelID, documentCount>>
    HashMap<Integer, HashMap<Integer, Integer>> classifiedDocCount = new HashMap<>();
    long lastDocCountSaveTime = 0;
    long nowTime;

    public ModelController() {
        trainingFeedMonitor = new ModelRetrainTrigger();
        trainingFeedMonitor.onRetrain
                .subscribe(new Function<EventArgs<CrisisAttributePair>>() {
            @Override
            public void execute(EventArgs<CrisisAttributePair> args) {
                onRetrainModel(args.result);
            }
        });
        Thread t = new Thread(trainingFeedMonitor);
        t.start();
    }

    @Override
    protected void processItem(Document item) {
        // TODO: Decide if pre-labeled items should be be re-classified.
        // Currently they are considered training examples and skipped.
        if (item.hasHumanLabels()) {
            return;
        }

        synchronized (this) {
            loadModelsIfNeeded();

            Model[] ms = models.getModels(item.getCrisisID().intValue());

            for (Model m : ms) {
                //Classify the document and get the output label
                NominalLabelBC label = m.classify(item);

                if (!classifiedDocCount.containsKey(m.getModelID())) {
                    classifiedDocCount.put(m.getModelID(), new HashMap<Integer, Integer>());
                }
                HashMap<Integer, Integer> labelCount = classifiedDocCount.get(m.getModelID());
                if (!labelCount.containsKey(label.getNominalLabelID())) {
                    labelCount.put(label.getNominalLabelID(), 1);
                } else {
                    int count = labelCount.get(label.getNominalLabelID());
                    labelCount.put(label.getNominalLabelID(), count + 1);
                }
            }

            item.setValueAsTrainingSample(calculateValueAsTrainingSample(item));

            writeClassifiedDocCountToDB();
        }
    }

    private void writeClassifiedDocCountToDB() {

        Calendar d = Calendar.getInstance();
        nowTime = d.getTimeInMillis();
        if (nowTime - lastDocCountSaveTime < 60000 || classifiedDocCount.isEmpty()) {
            return;
        }

        HashMap<Integer, HashMap<Integer, Integer>> docCounts = classifiedDocCount;
        classifiedDocCount = new HashMap<>();

        Function<HashMap<Integer, HashMap<Integer, Integer>>> task =
                new Function<HashMap<Integer, HashMap<Integer, Integer>>>() {
            @Override
            public void execute(HashMap<Integer, HashMap<Integer, Integer>> data) {
                DataStore.saveClassifiedDocumentCounts(data);
                lastDocCountSaveTime = nowTime;
            }
        };

        AsyncWorker<HashMap<Integer, HashMap<Integer, Integer>>> worker = new AsyncWorker<>(task, docCounts);
        worker.start();
    }

    @Override
    protected void idle() {
        writeClassifiedDocCountToDB();
    }

    private void loadModelsIfNeeded() {
        long now = (new Date()).getTime();

        if ((now - lastCheckForDefinitionUpdates) > definitionChangeFrequencyMs) {
            
            ArrayList<ModelFamilyEC> activeModels = DataStore.getActiveModels();
            
            for (ModelFamilyEC family : activeModels) {
                if (family.getState() == ModelFamilyEC.State.RUNNING 
                        && !models.hasModel(family.getCrisisID(), family.getNominalAttribute().getNominalAttributeID())) {
                    
                    loadModel(family.getCrisisID(), 
                            family.getNominalAttribute().getNominalAttributeID(),
                            family.getCurrentModelID());
                }
            }
        }

        lastCheckForDefinitionUpdates = now;
    }

    // This method is called when the training set grows sufficiently (event
    // based, see constructor)
    private void onRetrainModel(CrisisAttributePair info) {

        logger.info("Training a new model for event "
                + info.eventID + " and attribute " + info.attributeID);

        Model oldModel = models.getModel(info.eventID, info.attributeID);
        Model newModel;
        try {
            newModel = ModelFactory.buildModel(info.eventID, info.attributeID,
                    oldModel);
        } catch (Exception e) {
            logger.error("Exception while attempting to build model", e);
            return;
        }

        if (newModel != null && newModel != oldModel) {
            replaceModel(info.eventID, info.attributeID, newModel);
        } else if (newModel == null) {
            logger.info(
                    "Performance was too low, new model was discarded");
        } else {
            logger.info("New model did not outperform old model");
        }
    }

    private void replaceModel(int eventID, int attributeID, Model newModel) {
        logger.error("Replacing model for event " + eventID
                + " and attribute " + attributeID);

        // Insert the new model into the database and update the currentModelID
        // for this event and ontology
        int modelID = DataStore.saveModelToDatabase(eventID, attributeID,
                newModel);
        if (modelID == DataStore.MODEL_ID_ERROR) {
            throw new RuntimeException("Model was not correctly written to the database. Aborting write to disk.");
        }

        newModel.setModelID(modelID);

        // Write the new model to the model store
        try {
            weka.core.SerializationHelper.writeAll(
                    getModelPath(eventID, attributeID, newModel.getModelID()), // filename
                    new Object[]{newModel.getClassifier(),
                newModel.getAttributeSpecification()} // classifier
                    );
        } catch (Exception e) {
            logger.error("Exception when writing new model to file", e);
            return; // There is no point proceeding if the model could not be
            // written
        }

        // Pause classification and change the active model
        synchronized (this) {
            models.setModel(eventID, attributeID, newModel);
        }
    }

    public void initialize() {
        // Load models from disk into memory
        ArrayList<ModelFamilyEC> families = DataStore.getActiveModels();
        for (ModelFamilyEC family : families) {

            if (family.getState() == ModelFamilyEC.State.RUNNING) {
                loadModel(family.getCrisisID(), 
                        family.getNominalAttribute().getNominalAttributeID(),
                        family.getCurrentModelID());
            }
        }

        trainingFeedMonitor.initialize(families);
    }

    private boolean loadModel(int eventID, int attributeID, int modelID) {
        // Load models from dish and deserialize
        Object[] o;
        try {
            String path = getModelPath(eventID, attributeID, modelID);
            o = weka.core.SerializationHelper.readAll(path);
        } catch (Exception e) {
        	System.out.println("Could not load model from disk (crisis " + eventID
                    + ", attribute " + attributeID + ", model " + modelID
                    + "). Delete model reference in DB and retrain? [y/n]");
            try {
                if (System.in.read() == 'y') {
                    DataStore.deleteModel(modelID);
                    onRetrainModel(new CrisisAttributePair(eventID, attributeID));
                }
            } catch (IOException ex) {
                //do nothing
            }
            //System.out.println();
            return false;
        }
        Classifier classifier = (Classifier) o[0];
        Instances specification = (Instances) o[1];
        Model model = new Model(attributeID, classifier, specification);
        model.setModelID(modelID);

        models.setModel(eventID, attributeID, model);

        logger.info("Loaded model for crisis " + eventID
                + ", attribute " + attributeID);

        return true;
    }

    private static String getModelPath(int eventID, int attributeID, int modelID) {
		String modelsPath = TaggerConfigurator.getInstance().getProperty(
				TaggerConfigurationProperty.MODEL_STORE_PATH);
		if (!modelsPath.endsWith(File.separator)) {
            modelsPath += File.separator;
        }
        return modelsPath + eventID + "_" + attributeID + "_"
                + modelID + ".model";
    }

    private void unloadModel(int eventID, int attributeID) {
        // TODO: Unload models from memory when they get deleted or are no
        // longer in use
        models.removeModel(eventID, attributeID);
    }

    public double calculateValueAsTrainingSample(Document doc) {
        ArrayList<NominalLabelBC> labels = doc.getLabels(NominalLabelBC.class);

        if (labels.isEmpty()) {
            return 0.5;
        }

        double sum = 0;
        for (NominalLabelBC label : labels) {
            double a = label.getConfidence();
            double b = getLabelTrainingWeight(label.getAttributeID(), label.getNominalLabelID());
            // weight increases with:
            // increasing probability of this item belonging to a class with few
            // samples decreasing confidence in the classification range of 
            // weight is [0-1]. For domain see
            // http://www.wolframalpha.com/input/?i=plot+y%3D1+-+%280.5+%2B+0.5*%28%282*a-1%29*%282*b-1%29%29%29+from+a%3D0+to+1%2C+b%3D0+to+1
            double weight = 1 - (0.5 + 0.5 * ((2 * a - 1) * (2 * b - 1)));
            sum += weight;
        }

        return sum / (double) labels.size();
    }

    private double getLabelTrainingWeight(int attributeID, int labelID) {
        if (labelTrainingWeights == null
                || (System.currentTimeMillis() - lastLabelTrainingWeightUpdate) > 300000) { // 5
            // minutes
            updateLabelTrainingWeights();
        }

        if (!labelTrainingWeights.containsKey(attributeID)
                || !labelTrainingWeights.get(attributeID)
                .containsKey(labelID)) {
            return 1;
        }

        return labelTrainingWeights.get(attributeID).get(labelID);
    }

    private void updateLabelTrainingWeights() {
        labelTrainingWeights = DataStore.getNominalLabelTrainingValues();
        lastLabelTrainingWeightUpdate = System.currentTimeMillis();
    }
}
