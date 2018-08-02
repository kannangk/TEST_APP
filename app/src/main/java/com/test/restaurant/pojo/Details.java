package com.test.restaurant.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Details {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getCover_img() {
        return cover_img;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getReviews_count() {
        return reviews_count;
    }

    public String getReviews_avg() {
        return reviews_avg;
    }

    public ArrayList<Menu> getMenu() {
        return menu;
    }

    @SerializedName("phone_number")
    private String phone_number;

    @SerializedName("cover_img")
    private String cover_img;

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("end_time")
    private String end_time;

    @SerializedName("reviews_count")
    private String reviews_count;

    @SerializedName("reviews_avg")
    private String reviews_avg;

    @SerializedName("menu")
    private ArrayList<Menu> menu;
}
