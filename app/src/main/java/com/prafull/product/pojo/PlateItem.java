package com.prafull.product.pojo;

import org.json.JSONArray;

import java.lang.reflect.Array;

/**
 * Created by Amar on 04-08-2015.
 */
public class PlateItem {

    String title,cookingTime,currency,id,description,dishType,userId,rating;
    int qty;
    int price;
    JSONArray itemPics;
    String itemPic;




    public PlateItem(String title,String cookingTime,String currency,String id,String description,
                     String dishType,String userId, int qty, int price,String rating, JSONArray itemPics) {
        this.title = title;
        this.cookingTime=cookingTime;
        this.currency=currency;
        this.id=id;
        this.description=description;
        this.dishType=dishType;
        this.userId=userId;
        this.itemPics=itemPics;
        this.qty = qty;
        this.price = price;
        this.rating=rating;

    }


    public String getRating() {
        return rating;
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
    public String getCookingTime() {
        return cookingTime;
    }

    public String getCurrency() {
        return currency;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDishType() {
        return dishType;
    }

    public String getUserId() {
        return userId;
    }

    public JSONArray getItemPics() {
        return itemPics;
    }

    public String getItemPic() {
        String itemPic=null;
        try {
         if (itemPics.length()>0){
             itemPic=itemPics.getString(0);
         }
        }catch (Exception e){
            e.printStackTrace();
        }

        return itemPic;
    }
}
