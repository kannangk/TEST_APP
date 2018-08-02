package com.test.restaurant.pojo;

import com.google.gson.annotations.SerializedName;

public class CategoryDetails {
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;


}
