package com.jlshix.wlife_v03.data;


/**
 * Created by Leo on 2016/6/16.
 * 其它传感器数据 Recycler 调用进行初始化
 */
public class OthersData {

    private String type;
    private String no;
    private String place;
    private String state;

    public OthersData() {
        this.type = "04";
        this.no = "01";
        this.place = "客厅-烟雾";
        this.state = "0";
    }


    public OthersData(String type, String no, String place, String state) {

        this.type = type;
        this.no = no;
        this.place = place;
        this.state = state;
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
}
