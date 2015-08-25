package com.prafull.product.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amar on 03-08-2015.
 */
public class Plate implements Parcelable {
    String title;
    int qty;
    int price;
    public boolean box;

    public Plate(String title, int qty, int price, boolean box) {
        this.title = title;
        this.qty = qty;
        this.price = price;
        this.box = box;
    }

    public String getTitle() {
        return title;
    }

    public int getQty() {
        return qty;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(qty);
        dest.writeInt(price);
    }

    public static final Parcelable.Creator<Plate> CREATOR = new Parcelable.Creator<Plate>() {

        @Override
        public Plate[] newArray(int size) {
            return new Plate[size];
        }

        @Override
        public Plate createFromParcel(Parcel source) {
            return new Plate(source);
        }
    };

    public Plate(Parcel in) {
        this.title = in.readString();
        this.qty = in.readInt();
        this.price = in.readInt();
    }
}
