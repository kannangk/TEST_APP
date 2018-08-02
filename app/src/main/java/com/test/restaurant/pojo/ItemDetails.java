package com.test.restaurant.pojo;

import com.google.gson.annotations.SerializedName;

public class ItemDetails {

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private int price;

}
