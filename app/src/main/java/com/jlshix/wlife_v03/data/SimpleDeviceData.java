package com.jlshix.wlife_v03.data;

/**
 * Created by Leo on 2016/7/23.
 * 用于 dialog 相关信息
 */
public class SimpleDeviceData {
    private String no;
    private String name;

    public SimpleDeviceData() {
        this.no = "00";
        this.name = "请选择...";
    }

    public SimpleDeviceData(String no, String name) {
        this.no = no;
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
