package com.example.other.ttms.beans;

public class PlayInfo {

    private int id;
    private String name="无";
    private String kinds="无";
    private int timeLength=0;
    private String makePlace="无";
    private String introduce="无";
    private String show="无";
    private String src="";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public String getMakePlace() {
        return makePlace;
    }

    public void setMakePlace(String makePlace) {
        this.makePlace = makePlace;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
