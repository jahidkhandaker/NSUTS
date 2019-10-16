package com.nsu499.nsudriver;


public class StopageList {
    private String stopageName;
    private String WaitingNum;

    public StopageList(String stopageName, String WaitingNum) {
        this.stopageName = stopageName;
        this.WaitingNum = WaitingNum;
    }

    public StopageList() {
    }

    public void setStopageName(String stopageName) {
        this.stopageName = stopageName;
    }

    public void setWaitingNum(String waitingNum) {
        WaitingNum = waitingNum;
    }

    public String getStopageName() {
        return stopageName;
    }


    public String getWaitingNum() {

        return WaitingNum;
    }



}