package com.prafull.product.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amar on 26-07-2015.
 */
public class ProductImage implements Parcelable {
    String imageUrl;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProductImage(String imageUrl){
       this.imageUrl=imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
     parcel.writeString(imageUrl);
    }

    /**
     * Retrieving Student data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private ProductImage(Parcel in) {
        this.imageUrl = in.readString();
    }


    public static final Parcelable.Creator<ProductImage> CREATOR = new Parcelable.Creator<ProductImage>() {

        @Override
        public ProductImage createFromParcel(Parcel source) {
            return new ProductImage(source);
        }

        @Override
        public ProductImage[] newArray(int size) {
            return new ProductImage[size];
        }
    };
}
