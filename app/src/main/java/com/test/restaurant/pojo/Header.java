package com.test.restaurant.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Header extends ListItem implements Parcelable {
    private String header;


    protected Header(Parcel in) {
        header = in.readString();
        itemCount = in.readInt();
        headerPos = in.readInt();
    }

    public static final Creator<Header> CREATOR = new Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel in) {
            return new Header(in);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getHeaderPos() {
        return headerPos;
    }

    public void setHeaderPos(int headerPos) {
        this.headerPos = headerPos;
    }

    private int itemCount;
    private int headerPos;

    public Header(){}

    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(header);
        dest.writeInt(itemCount);
        dest.writeInt(headerPos);
    }
}