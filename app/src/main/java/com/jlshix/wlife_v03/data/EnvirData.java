package com.jlshix.wlife_v03.data;


/**
 * Created by Leo on 2016/6/16.
 * 环境数据 Recycler 调用进行初始化
 */
public class EnvirData {

    private String place;
    private String temp;
    private String humi;
    private String light;

    public EnvirData(String place, String state) {
        // 01020304
        // 01234567
        this.place = place;
        this.temp = state.substring(0, 2) + "°";
        this.humi = state.substring(2, 4) + "%";
        this.light = state.substring(4) + "lx";
    }

    public EnvirData() {
        this.place = "未指定";
        this.temp = "--°";
        this.humi = "--%";
        this.light = "----lx";
    }

    public EnvirData(String place, String temp, String humi, String light) {

        this.place = place;
        this.temp = temp;
        this.humi = humi;
        this.light = light;
    }


    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumi() {
        return humi;
    }

    public void setHumi(String humi) {
        this.humi = humi;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

}
