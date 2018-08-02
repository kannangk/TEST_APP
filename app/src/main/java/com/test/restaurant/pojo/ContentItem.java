package com.test.restaurant.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class ContentItem  extends ListItem implements Parcelable {

    private String name;
    private int itemPos;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static Creator<ContentItem> getCREATOR() {
        return CREATOR;
    }

    private int price;

    public ContentItem(){}
    protected ContentItem(Parcel in) {
        name = in.readString();
        itemPos = in.readInt();
        count = in.readInt();
        itemName = in.readString();
        itemSubName = in.readString();
        price = in.readInt();
    }

    public static final Creator<ContentItem> CREATOR = new Creator<ContentItem>() {
        @Override
        public ContentItem createFromParcel(Parcel in) {
            return new ContentItem(in);
        }

        @Override
        public ContentItem[] newArray(int size) {
            return new ContentItem[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSubName() {
        return itemSubName;
    }

    public void setItemSubName(String itemSubName) {
        this.itemSubName = itemSubName;
    }

    private int count;
    private String itemName;
    private String itemSubName;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getItemPos() {
        return itemPos;
    }

    public void setItemPos(int itemPos) {
        this.itemPos = itemPos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(itemPos);
        dest.writeInt(count);
        dest.writeString(itemName);
        dest.writeString(itemSubName);
        dest.writeInt(price);
    }
}
