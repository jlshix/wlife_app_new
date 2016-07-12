package com.jlshix.wlife_v03.data;

import org.json.JSONObject;

/**
 * Created by Leo on 2016/6/18.
 * 网关数据
 */
public class GateData {
    // id 字段未支持
    // 用户对设备为一对一 后期改为一对多时需要更改更多数据
    private String name;
    private String imei;
    private String online;
    private String mode;

    public GateData(String name, String imei, String online, String mode) {
        this.name = name;
        this.imei = imei;
        this.online = online;
        this.mode = mode;
    }

    /**
     * 使用JSON初始化
     * @param object JSON
     */
    public GateData(JSONObject object) {
        this.name = object.optString("name");
        this.imei = object.optString("imei");
        this.online = object.optString("online");
        this.mode = object.optString("mode");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
