package com.test.restaurant.adapterCallBack;

import com.test.restaurant.pojo.ContentItem;

public interface OrderChangeCallBack {
    void onChangeCallback(Boolean isAdd, ContentItem item);
}
