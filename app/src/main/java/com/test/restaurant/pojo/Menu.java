package com.test.restaurant.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Menu {
    public MenuDetails getMenu_details() {
        return menu_details;
    }

    @SerializedName("menu_details")
    private MenuDetails menu_details;


}
