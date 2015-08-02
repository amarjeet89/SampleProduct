package com.prafull.product.pojo;

import org.json.JSONArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by john.francis on 10-06-2015.
 */
public class Product{
    String userId;
    String contacts;
    String description;
    String icon_url;
    String address;
    String title;
    Double placeLatitude,placeLongitude;
    String product_pics;
  //  ArrayList<ProductImage> imagesList;

    public Product(String title, String userId, String address, String  item_pics,
                   String description,String contacts,Double latitude,Double logitude) {
       try {
           this.userId = userId;
           this.address = address;
           this.placeLatitude = latitude;
           this.placeLongitude = logitude;
           JSONArray itemArray = new JSONArray(item_pics);
           this.icon_url = itemArray.getString(0);
           this.title = title;
           this.description = description;
           this.contacts = contacts;
           this.product_pics = item_pics;
           //this.imagesList=imagesList;
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    public String getUserId() {
        return userId;
    }

    public String getContacts() {
        return contacts;
    }

    public String getDescription() {
        return description;
    }


    public String getAddress() {
        return address;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public String getProduct_pics() {
        return product_pics;
    }

    public String getTitle() {
        return title;
    }
    public Double getPlaceLatitude() {
        return placeLatitude;
    }

    public Double getPlaceLongitude() {
        return placeLongitude;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPlaceLatitude(Double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public void setPlaceLongitude(Double placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public void setProduct_pics(String product_pics) {
        this.product_pics = product_pics;
    }

    /* public ArrayList<ProductImage> getImagesList() {

            return imagesList;
        }*/




}
