package com.test.restaurant.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MenuDetails {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public ArrayList<Categories> getCategories() {
        return categories;
    }

    @SerializedName("categories")
    private ArrayList<Categories> categories;
}
