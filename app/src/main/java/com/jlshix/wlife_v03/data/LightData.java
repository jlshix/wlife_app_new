package com.jlshix.wlife_v03.data;

/**
 * Created by Leo on 2016/6/16.
 */
public class LightData {
    private String name;
    private int state;

    public LightData() {
        this.name = "未指定";
        this.state = 0;
    }

    public LightData(String name, int state) {
        this.name = name;
        this.state = state;
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
}
