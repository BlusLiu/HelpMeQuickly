package com.example.task;

public class MyTask {
    private String user_name;
    private String task_state;
    private String task_detail;
    private String task_price;
    private String task_title;
    private String task_id;

    public MyTask(String name, String state, String detail, String price,String title,String taskid) {
        this.user_name = name;
        this.task_state = state;
        this.task_detail = detail;
        this.task_price = price;
        this.task_title = title;
        this.task_id = taskid;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }

    public String getTaskState() {
        return task_state;
    }

    public void setTaskState(String state) {
        this.task_state = state;
    }

    public String getTaskDetail() {
        return task_detail;
    }

    public void setTaskDetail(String detail) {
        this.task_detail = detail;
    }

    public String getTaskPrice() {
        return task_price;
    }

    public void setTaskPrice(String price) {
        this.task_price = price;
    }

    public String getTaskTitle(){
        return task_title;
    }

    public void setTaskTitle(String title){
        this.task_title = title;
    }

    public String getTaskId(){
        return task_id;
    }

    public void setTaskId(String taskid){
        this.task_id = taskid;
    }

}
