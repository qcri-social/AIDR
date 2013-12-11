package qa.qcri.aidr.predict.classification.nominal;

import java.util.HashMap;

/**
 * ModelSet is an internal collection of currently running classifiers; one for
 * each combination of event and ontology.
 * 
 * @author jrogstadius
 */
class ModelSet {
    HashMap<Integer, HashMap<Integer, Model>> models = new HashMap<Integer, HashMap<Integer, Model>>();

    public void setModel(int eventID, int attributeID, Model m) {
        if (!models.containsKey(eventID))
            models.put(eventID, new HashMap<Integer, Model>());
        if (models.get(eventID).containsKey(attributeID))
            models.get(eventID).remove(attributeID);
        models.get(eventID).put(attributeID, m);
    }

    public void removeModel(int eventID, int attributeID) {
        if (!models.containsKey(eventID))
            return;
        if (!models.get(eventID).containsKey(attributeID))
            return;
        models.get(eventID).remove(attributeID);
    }

    public Model getModel(int eventID, int attributeID) {
        if (!models.containsKey(eventID)
                || !models.get(eventID).containsKey(attributeID))
            return null;
        return models.get(eventID).get(attributeID);
    }

    public Model[] getModels(int eventID) {
        if (!models.containsKey(eventID) || models.get(eventID).isEmpty())
            return new Model[0];
        return models.get(eventID).values().toArray(new Model[0]);
    }

    public boolean hasModel(int eventID, int attributeID) {
        return models.containsKey(eventID)
                && models.get(eventID).containsKey(attributeID);
    }
}
