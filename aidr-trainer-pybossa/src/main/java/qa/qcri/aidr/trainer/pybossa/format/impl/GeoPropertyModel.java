package qa.qcri.aidr.trainer.pybossa.format.impl;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 1/26/14
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeoPropertyModel {

    private int startPointIndex;
    private int endPointIndex;
    private double distance;

    public GeoPropertyModel(int startPointIndex, int endPointIndex, double distance){
        this.startPointIndex = startPointIndex;
        this.endPointIndex = endPointIndex;
        this.distance = distance;
    }

    public int getStartPointIndex() {
        return startPointIndex;
    }

    public void setStartPointIndex(int startPointIndex) {
        this.startPointIndex = startPointIndex;
    }

    public int getEndPointIndex() {
        return endPointIndex;
    }

    public void setEndPointIndex(int endPointIndex) {
        this.endPointIndex = endPointIndex;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
