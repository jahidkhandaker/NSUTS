package com.nsu499.nsuts.ui.schedule;

public class ScheduleList {

    private String departTime;
    private String departFrom;
    private String arrivedTo;
    private String busTitle;

    public ScheduleList(String departTime, String departFrom, String arrivedTo, String busTitle) {
        this.departTime = departTime;
        this.departFrom = departFrom;
        this.arrivedTo = arrivedTo;
        this.busTitle = busTitle;
    }

    public ScheduleList() {
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getDepartFrom() {
        return departFrom;
    }

    public void setDepartFrom(String departFrom) {
        this.departFrom = departFrom;
    }

    public String getArrivedTo() {
        return arrivedTo;
    }

    public void setArrivedTo(String arrivedTo) {
        this.arrivedTo = arrivedTo;
    }

    public String getBusTitle() {
        return busTitle;
    }

    public void setBusTitle(String busTitle) {
        this.busTitle = busTitle;
    }
}
