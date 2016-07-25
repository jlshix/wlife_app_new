package com.jlshix.wlife_v03.data;

/**
 * Created by Leo on 2016/7/25.
 * order data
 */
public class OrderData {

    private int id;
    private String name;
    private int equal;
    private String type;
    private String actions;
    private String des;

    public OrderData() {
        this.id = 0;
        this.name = "无";
        this.equal = 1;
        this.type = "voice";
        this.actions = "00000000";
        this.des = "无描述";
    }

    public OrderData(int id, String name, int equal, String type, String actions, String des) {
        this.id = id;
        this.name = name;
        this.equal = equal;
        this.type = type;
        this.actions = actions;
        this.des = des;
    }

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

    public int getEqual() {
        return equal;
    }

    public void setEqual(int equal) {
        this.equal = equal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
