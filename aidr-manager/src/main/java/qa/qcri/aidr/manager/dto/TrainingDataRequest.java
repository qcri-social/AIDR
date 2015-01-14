package qa.qcri.aidr.manager.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;

@XmlRootElement
public class TrainingDataRequest implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement
	private List<TrainingDataDTO> trainingData;

    public List<TrainingDataDTO> getTrainingData() {
        return trainingData;
    }

    public void setTrainingData(List<TrainingDataDTO> trainingData) {
        this.trainingData = trainingData;
    }
}
