package com.test.restaurant.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Categories {
    public CategoryDetails getCategory_details() {
        return category_details;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    @SerializedName("category_details")
    private CategoryDetails category_details;

    @SerializedName("items")
    private ArrayList<Items> items;

}
