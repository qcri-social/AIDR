package qa.qcri.aidr.manager.dto;


public class TaskAnswer {

    private Integer user_id;

    private TaskInfo info;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public TaskInfo getInfo() {
        return info;
    }

    public void setInfo(TaskInfo info) {
        this.info = info;
    }
    
    public TaskAnswer() {}
    
}
