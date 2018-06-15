package com.example.other.ttms.beans;

public class ShowTimeInfo {

    private int planID;
    private String startTime;
    private String endTime;
    private String showType;
    private String showStudioId;
    private float price;


    public int getPlanID() {
        return planID;
    }

    public void setPlanID(int planID) {
        this.planID = planID;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getShowStudioId() {
        return showStudioId;
    }

    public void setShowStudioId(String showStudioID) {
        this.showStudioId = showStudioID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
