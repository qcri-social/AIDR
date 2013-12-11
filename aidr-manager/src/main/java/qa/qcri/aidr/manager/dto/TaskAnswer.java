package qa.qcri.aidr.manager.dto;

public class TaskAnswer {

    private Integer user_id;

    private DateHistory dateHistory;

    private TaskInfo info;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public DateHistory getDateHistory() {
        return dateHistory;
    }

    public void setDateHistory(DateHistory dateHistory) {
        this.dateHistory = dateHistory;
    }

    public TaskInfo getInfo() {
        return info;
    }

    public void setInfo(TaskInfo info) {
        this.info = info;
    }
}
