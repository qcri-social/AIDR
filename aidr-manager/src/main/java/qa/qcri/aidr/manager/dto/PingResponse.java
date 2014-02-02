package qa.qcri.aidr.manager.dto;


public class PingResponse {

    private String  application;

    private String  status;

    private String  startDate;

    private String  currentStatus;

    private String  runningCollectionsCount;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getRunningCollectionsCount() {
        return runningCollectionsCount;
    }

    public void setRunningCollectionsCount(String runningCollectionsCount) {
        this.runningCollectionsCount = runningCollectionsCount;
    }
}
