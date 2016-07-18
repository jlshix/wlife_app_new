package com.jlshix.wlife_v03.data;


import com.jlshix.wlife_v03.tool.L;

/**
 * Created by Leo on 2016/6/16.
 * 环境数据 Recycler 调用进行初始化
 */
public class EnvirData {

    /**
     * 对应数据库name字段 先期设计为place 不再修改
     */
    private String place;
    /**
     * 温湿度光照信息
     */
    private String temp;
    private String humi;
    private String light;
    /**
     * 标志颜色与地点标识
     */
    private int sign;
    private int placeNo;

    public EnvirData(String place, String state, int sign, int placeNo) {
        // 01020304
        // 01234567
        this.place = place;
        this.temp = state.substring(0, 2) + "°";
        this.humi = state.substring(2, 4) + "%";
        this.light = state.substring(4) + "lx";
        this.sign = sign;
        this.placeNo = placeNo;
    }

    public EnvirData() {
        this.place = "未指定";
        this.temp = "--°";
        this.humi = "--%";
        this.light = "----lx";
        this.sign = L.RED;
        this.placeNo = L.LIVING_ROOM;
    }

    public EnvirData(String place, String temp, String humi, String light, int sign, int placeNo) {

        this.place = place;
        this.temp = temp;
        this.humi = humi;
        this.light = light;
        this.sign = sign;
        this.placeNo = placeNo;
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
