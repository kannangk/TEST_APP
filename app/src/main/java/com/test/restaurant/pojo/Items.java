package com.test.restaurant.pojo;

import com.google.gson.annotations.SerializedName;

public class Items {
    public ItemDetails getItem_details() {
        return item_details;
    }

    @SerializedName("item_details")
    private ItemDetails item_details;
}
