package qa.qcri.aidr.predict.common;

import weka.classifiers.trees.RandomForest;

public enum AlgorithmType {

	RANDOM_FOREST(RandomForest.class);
	
    private Class clazz;

	private AlgorithmType(Class clazz) {
		this.setClazz(clazz);
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
}
