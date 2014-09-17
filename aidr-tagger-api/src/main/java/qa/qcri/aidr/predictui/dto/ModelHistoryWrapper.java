package qa.qcri.aidr.predictui.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alex Mikhashchuk
 */
@XmlRootElement
public class ModelHistoryWrapper implements Serializable {

	@XmlElement private Integer modelID;

	@XmlElement private double avgPrecision;

	@XmlElement private double avgRecall;

	@XmlElement private double avgAuc;

	@XmlElement private int trainingCount;

	@XmlElement private Date trainingTime;

    public Integer getModelID() {
        return modelID;
    }

    public void setModelID(Integer modelID) {
        this.modelID = modelID;
    }

    public double getAvgPrecision() {
        return avgPrecision;
    }

    public void setAvgPrecision(double avgPrecision) {
        this.avgPrecision = avgPrecision;
    }

    public double getAvgRecall() {
        return avgRecall;
    }

    public void setAvgRecall(double avgRecall) {
        this.avgRecall = avgRecall;
    }

    public double getAvgAuc() {
        return avgAuc;
    }

    public void setAvgAuc(double avgAuc) {
        this.avgAuc = avgAuc;
    }

    public int getTrainingCount() {
        return trainingCount;
    }

    public void setTrainingCount(int trainingCount) {
        this.trainingCount = trainingCount;
    }

    public Date getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(Date trainingTime) {
        this.trainingTime = trainingTime;
    }
}
