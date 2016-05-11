/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.collector.beans;


/**
 * @author Imran
 * Provides data structure to hold various collector-specific details.
 */
public class CollectorStatus {
    
    private String startDate;
    private String currentStatus;
    private int runningCollectionsCount;

    public CollectorStatus() {
    }

    public CollectorStatus(String startDate, String currentStatus, int runningCollectionsCount) {
        this.startDate = startDate;
        this.currentStatus = currentStatus;
        this.runningCollectionsCount = runningCollectionsCount;
    }
    
    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the currentStatus
     */
    public String getCurrentStatus() {
        return currentStatus;
    }

    /**
     * @param currentStatus the currentStatus to set
     */
    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * @return the runningCollectionsCount
     */
    public int getRunningCollectionsCount() {
        return runningCollectionsCount;
    }

    /**
     * @param runningCollectionsCount the runningCollectionsCount to set
     */
    public void setRunningCollectionsCount(int runningCollectionsCount) {
        this.runningCollectionsCount = runningCollectionsCount;
    }
}
