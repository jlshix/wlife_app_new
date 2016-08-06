package com.jlshix.wlife_v03.data;


import com.jlshix.wlife_v03.tool.L;

/**
 * Created by Leo on 2016/6/16.
 * 其它传感器数据 Recycler 调用进行初始化
 */
public class ApplianceData {

    private String type;
    private String no;
    private String place;
    private String state;
    private int sign;
    private int placeNo;

    public ApplianceData() {
        this.type = "04";
        this.no = "01";
        this.place = "空调";
        this.state = "0";
        this.sign = L.RED;
        this.placeNo = L.LIVING_ROOM;
    }


    public ApplianceData(String type, String no, String place, String state, int sign, int placeNo) {
        this.type = type;
        this.no = no;
        this.place = place;
        this.state = state;
        this.sign = sign;
        this.placeNo = placeNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getPlaceNo() {
        return placeNo;
    }

    public void setPlaceNo(int placeNo) {
        this.placeNo = placeNo;
    }
}
