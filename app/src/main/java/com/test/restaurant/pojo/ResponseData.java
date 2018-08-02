package com.test.restaurant.pojo;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class ResponseData {
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Details getData() {
        return data;
    }

    public void setData(Details data) {
        this.data = data;
    }

    @SerializedName("status")
    private Boolean status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private Details data;

}
