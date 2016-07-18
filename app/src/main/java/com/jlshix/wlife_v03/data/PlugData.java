package com.jlshix.wlife_v03.data;


import com.jlshix.wlife_v03.tool.L;

/**
 * Created by Leo on 2016/6/16.
 * 插座数据 Recycler 调用进行初始化
 */
public class PlugData  {

    private String name;
    private boolean[] state;
    private int sign;
    private int placeNo;

    public PlugData() {
        name = "未指定";
        state = new boolean[4];
        this.sign = L.RED;
        this.placeNo = L.LIVING_ROOM;
    }

    public PlugData(String s) {
        // 注意初始化啊啊啊啊
        name = "未指定";
        state = new boolean[s.length()];
        setState(s);
        this.sign = L.RED;
        this.placeNo = L.LIVING_ROOM;
    }

    public PlugData(String name, String state, int sign, int placeNo){
        this.name = name;
        this.state = new boolean[state.length()];
        setState(state);
        this.sign = sign;
        this.placeNo = placeNo;
    }

    public boolean[] getState() {
        return state;
    }

    public boolean getState(int i) {
        return state[i];
    }


    public void setState(boolean[] state) {
        this.state = state;
    }

    public void setState(int i, boolean state) {
        this.state[i] = state;
    }


    public void setState(String s) {
        for (int i = 0; i < state.length; i++) {
            state[i] = s.charAt(i) == '1';
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
