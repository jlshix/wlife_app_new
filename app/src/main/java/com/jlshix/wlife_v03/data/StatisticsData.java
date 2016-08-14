package com.jlshix.wlife_v03.data;

/**
 * Created by Leo on 2016/8/13.
 */
public class StatisticsData {

    private String no;
    private int temp;
    private int humi;
    private int light;
    private String date;
    private String time;

    public StatisticsData() {
        this.no = "01";
        this.temp = 26;
        this.humi = 75;
        this.light = 128;
        this.date = "2016-01-01";
        this.time = "11:11";
    }

    public StatisticsData(String no) {
        this.no = no;
        this.temp = 0;
        this.humi = 0;
        this.light = 0;
        this.date = "2016-01-01";
        this.time = "00:00";
    }




    public StatisticsData(String no, int temp, int humi, int light, String date, String time) {
        this.no = no;
        this.temp = temp;
        this.humi = humi;
        this.light = light;
        this.date = date;
        this.time = time;
    }

    public StatisticsData(String no, String state, String date, String time) {
        this.no = no;
        this.temp = Integer.valueOf(state.substring(0, 2));
        this.humi = Integer.valueOf(state.substring(2, 4));
        this.light = Integer.valueOf(state.substring(4));
        this.date = date;
        this.time = time;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHumi() {
        return humi;
    }

    public void setHumi(int humi) {
        this.humi = humi;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}