package qa.qcri.aidr.manager.dto;

import java.util.List;

public class TrainingDataRequest {

    private List<TrainingDataDTO> trainingData;

    public List<TrainingDataDTO> getTrainingData() {
        return trainingData;
    }

    public void setTrainingData(List<TrainingDataDTO> trainingData) {
        this.trainingData = trainingData;
    }
}
