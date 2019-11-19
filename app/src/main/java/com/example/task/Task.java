package com.example.task;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 21:14 2019/10/22
 */
public class Task {
    private String name;
    private String detail;
    private String supplement;
    private String startTime;
    private String restTime;

    public Task(String name, String detail, String supplement, String startTime, String restTime) {
        this.name = name;
        this.detail = detail;
        this.supplement = supplement;
        this.startTime = startTime;
        this.restTime = restTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSupplement() {
        return supplement;
    }

    public void setSupplement(String supplement) {
        this.supplement = supplement;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRestTime() {
        return restTime;
    }

    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }
}
