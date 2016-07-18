package com.jlshix.wlife_v03.data;

import com.jlshix.wlife_v03.tool.L;

/**
 * Created by Leo on 2016/6/16.
 * data for light
 */
public class LightData {
    private String name;
    private int state;
    private int sign;
    private int placeNo;

    public LightData() {
        this.name = "未指定";
        this.state = 0;
        this.sign = L.RED;
        this.placeNo = L.LIVING_ROOM;
    }

    public LightData(String name, int state, int sign, int placeNo) {
        this.name = name;
        this.state = state;
        this.sign = sign;
        this.placeNo = placeNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
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
